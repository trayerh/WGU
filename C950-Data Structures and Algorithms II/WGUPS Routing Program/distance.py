import csv


def get_distance(start_address, finish_address):
    # Returns the distance between two given addresses by referencing the given CSV file
    # Traversing in worst case scenario takes O(n) time

    distance = 0

    if start_address == finish_address:
        distance = '0.0'

    else:
        with open('docs/WGUPS Distance Table.csv') as csv_file:
            reader = csv.reader(csv_file)
            address_rows = list(reader)

            # get start index
            start_address_column = 0
            j = 0
            for row in address_rows:
                if row != '':
                    if row[1] == ' HUB':
                        start_address_column = 1
                    else:
                        current_address = row[1][1:-8]
                        if start_address == current_address:
                            start_address_column = j
                            break
                j += 1

            # get finish index
            finish_address_column = 0
            i = 2
            for row in address_rows:
                if row != '':
                    if row[1] == ' HUB':
                        finish_address_column = 1
                    else:
                        current_address = row[1][1:-8]
                        if finish_address == current_address:
                            finish_address_column = i
                            break
                i += 1

            # find the larger index
            start = 0
            finish = 0

            if finish_address_column == 1:
                # exception for if finishing at HUB
                start = start_address_column
                finish = finish_address_column+1
            elif start_address_column >= (finish_address_column-1):
                start = start_address_column
                finish = finish_address_column-1
            elif (finish_address_column-1) > start_address_column:
                start = finish_address_column-2
                finish = start_address_column+1

            # get the distance
            row = list(address_rows)
            column = list(row[start])
            distance = column[finish]

    return distance
