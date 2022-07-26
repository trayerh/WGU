# Original file from console.cloud.google.comdr

import os
import sys
from google.cloud import automl_v1beta1


# 'content' is base-64-encoded image data.
def get_prediction(file_path, project_id, model_id):
    os.environ['GOOGLE_APPLICATION_CREDENTIALS'] = "euphoric-column-356222-e359b95d3101.json"

    # Create a client
    prediction_client = automl_v1beta1.PredictionServiceClient()

    # Initialize request argument(s)
    name = 'projects/{}/locations/us-central1/models/{}'.format(project_id, model_id)
    with open(file_path, 'rb') as ff:
        content = ff.read()
    payload = {'image': {'image_bytes': content}}
    params = {}
    request = automl_v1beta1.PredictRequest(
      name=name,
      payload=payload,
    )

    try:
        # Make the request
        response = prediction_client.predict(request=request)
        # Handle the response
        return response  # waits till request is returned
    except:
        response = '_NOT_DEPLOYED_'
        return response


if __name__ == '__main__':
    file_path = sys.argv[1]
    project_id = sys.argv[2]
    model_id = sys.argv[3]

    with open(file_path, 'rb') as ff:
        content = ff.read()

    get_prediction(content, project_id, model_id)
