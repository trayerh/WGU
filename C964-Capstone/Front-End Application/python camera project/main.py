import io
import os
import PySimpleGUI as sg
import cv2
import PIL.Image
import predict
from google.protobuf.json_format import MessageToDict

image_file_types = [("JPEG (*.jpg)", "*.jpg"),
                    ("All files (*.*)", "*.*")]
video_file_types = [("mp4 (*.mp4)", "*.mp4"),
                    ("All files (*.*)", "*.*")]


def main():

    # Set default images
    default_image = PIL.Image.open("icon.jpg")
    default_image.thumbnail((400, 400))
    default_bio = io.BytesIO()
    default_image.save(default_bio, format="PNG")
    small_image = PIL.Image.open("icon.jpg")
    small_image.thumbnail((75, 75))
    small_bio = io.BytesIO()
    small_image.save(small_bio, format="PNG")

    # init vars
    score = '--'
    label = '--'
    current_frame = '0'
    layout = [
        [sg.Button("Exit"), sg.Button("Clear")],
        [
            sg.Text("Choose WebCam"),
            sg.Combo(['0', '1', '2', '-1'], default_value='0', key='-USB-'),
            sg.Button("Load Cam"),
        ],
        [
            sg.Text("Video File"),
            sg.Input(size=(25, 1), key="-VIDEO_FILE-"),
            sg.FileBrowse(file_types=video_file_types),
            sg.Button("Load Video"),
        ],
        [
            sg.Text("Image File"),
            sg.Input(size=(25, 1), key="-IMAGE_FILE-"),
            sg.FileBrowse(file_types=image_file_types),
            sg.Button("Load Image"),
        ],
        [sg.Image(key="-IMAGE-", data=default_bio.getvalue())],
        [sg.Text("Most Recent Screenshot:"), sg.Image(key="-SNAPSHOT-", data=small_bio.getvalue())],
        [sg.Text("Current Frame:"), sg.Text(current_frame, key="-FRAME-")],
        [sg.Text("Analysis:"), sg.Text(label, key="-LABEL-", background_color='green')],
        [sg.Text("Confidence:"), sg.Text(score, key="-SCORE-")]
    ]
    window = sg.Window("Wisp Watcher", layout)

    # -----------------------------------------------------------------------------------------------------START APP GUI
    while True:
        event, values = window.read()

        # ---------------------------------------------------------------------------------------------------------IMAGE
        if event == "Load Image":
            filename = values["-IMAGE_FILE-"]
            if os.path.exists(filename):
                # update image
                image = PIL.Image.open(values["-IMAGE_FILE-"])
                image.thumbnail((400, 400))
                bio = io.BytesIO()
                image.save(bio, format="PNG")
                window["-IMAGE-"].update(data=bio.getvalue())

                # submit image for analysis, get and parse values
                result = predict.get_prediction(values["-IMAGE_FILE-"], 318462575065, 'ICN1021579343110864896')
                # check if model is active
                if result == '_NOT_DEPLOYED_':
                    print('Model Not Deployed')
                    score = 'null'
                    label = 'null'
                else:
                    result_dict = MessageToDict(result._pb)
                    print(result_dict)
                    score = result_dict['payload'][0].get('classification').get('score')
                    label = result_dict['payload'][0].get('displayName')

                # update gui
                window["-SCORE-"].update(score)
                if label == 'fire':
                    window["-LABEL-"].update(label, background_color='red')
                else:
                    window["-LABEL-"].update(label, background_color='green')
        # ---------------------------------------------------------------------------------------------------------VIDEO
        if event == "Load Video":
            # init vars
            filename = values["-VIDEO_FILE-"]
            video = cv2.VideoCapture(filename)
            length = int(video.get(cv2.CAP_PROP_FRAME_COUNT))

            # begin looping through all frames
            frame_index = 0
            video.set(1, frame_index)
            _, frame = video.read()
            while frame_index < length:
                print('current frame:', frame_index)

                # grab current image from video file, update display
                cv2.imwrite("current_frame.jpg", frame)
                image = PIL.Image.open("current_frame.jpg")
                image.thumbnail((400, 400))
                bio = io.BytesIO()
                image.save(bio, format="PNG")
                window["-IMAGE-"].update(data=bio.getvalue())
                window.refresh()

                # every 60 frames, send a screenshot for predictions
                if frame_index % 60 == 0:

                    # update thumbnail
                    image = PIL.Image.open("current_frame.jpg")
                    image.thumbnail((75, 75))
                    bio = io.BytesIO()
                    image.save(bio, format="PNG")
                    window["-SNAPSHOT-"].update(data=bio.getvalue())
                    frame_string = str(frame_index) + " of " + str(length) + " total"
                    window["-FRAME-"].update(frame_string)
                    window.refresh()

                    # get prediction
                    result = predict.get_prediction("current_frame.jpg", 318462575065, 'ICN1021579343110864896')
                    if result == '_NOT_DEPLOYED_':
                        print('Model Not Deployed')
                        score = 'null'
                        label = 'null'
                    else:
                        result_dict = MessageToDict(result._pb)
                        print(result_dict)
                        score = result_dict['payload'][0].get('classification').get('score')
                        label = result_dict['payload'][0].get('displayName')

                    # update gui
                    window["-SCORE-"].update(score)
                    if label == 'fire':
                        window["-LABEL-"].update(label, background_color='red')
                    else:
                        window["-LABEL-"].update(label, background_color='green')

                # stop playback on detection of fire or clicking clear, exit, etcd
                if label == 'fire':
                    break
                event, values = window.read(timeout=1)
                if event == '__TIMEOUT__' or event == 'Load Video':
                    event = 'Load Video'
                elif event != "Load Video":
                    break

                # grab next frame
                video.set(1, frame_index)
                _, frame = video.read()
                frame_index += 1
        # --------------------------------------------------------------------------------------------------------WEBCAM
        if event == "Load Cam":
            # init vars
            frame_index = 0
            port = values['-USB-']
            print("Chosen WebCam USB port: ", port)
            stream = cv2.VideoCapture(int(port))

            # load stream
            streaming = True
            while streaming:
                success, frame = stream.read()
                print('current frame:', frame_index)

                # grab current image from video file, update display
                if success:
                    cv2.imwrite("current_frame.jpg", frame)
                    image = PIL.Image.open("current_frame.jpg")
                    image.thumbnail((400, 400))
                    bio = io.BytesIO()
                    image.save(bio, format="PNG")
                    window["-IMAGE-"].update(data=bio.getvalue())
                    window.refresh()

                    # every 60 frames, send a screenshot for predictions
                    if frame_index % 60 == 0:

                        # update thumbnail
                        image = PIL.Image.open("current_frame.jpg")
                        image.thumbnail((75, 75))
                        bio = io.BytesIO()
                        image.save(bio, format="PNG")
                        window["-SNAPSHOT-"].update(data=bio.getvalue())
                        window["-FRAME-"].update(frame_index)
                        window.refresh()

                        # get prediction
                        result = predict.get_prediction("current_frame.jpg", 318462575065, 'ICN1021579343110864896')
                        if result == '_NOT_DEPLOYED_':
                            print('Model Not Deployed')
                            score = 'null'
                            label = 'null'
                        else:
                            result_dict = MessageToDict(result._pb)
                            print(result_dict)
                            score = result_dict['payload'][0].get('classification').get('score')
                            label = result_dict['payload'][0].get('displayName')

                        # update gui
                        window["-SCORE-"].update(score)
                        if label == 'fire':
                            window["-LABEL-"].update(label, background_color='red')
                        else:
                            window["-LABEL-"].update(label, background_color='green')
                else:
                    print('no image from webcam')
                frame_index += 1
                event, values = window.read(timeout=1)
                if event == '__TIMEOUT__' or event == 'Load Cam':
                    streaming = True
                    event = 'Load Cam'
                elif event != "Load Cam":
                    streaming = False

            stream.release()
            cv2.destroyAllWindows()
        # ------------------------------------------------------------------------------------------------MISC-FUNCTIONS
        if event == "Clear":
            # refresh
            window.close()
            main()
        if event == "Exit" or event == sg.WIN_CLOSED:
            break

    window.close()


if __name__ == "__main__":
    main()
