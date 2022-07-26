from math import floor

import adj_list
import distance
import load
import routing
import simulate


def run_simulation():
    # Runs the simulation for a full delivery of all packages
    # tracks total distance traveled

    # ---------------------------------------------------------------------------------------------------------1/5:SETUP
    truck_one = []  # early deliveries
    truck_two = []  # Late Arrivals
    truck_three = []  # errors + the rest
    # -------------------------------------------------------------------------------------------------------2/5:LOADING
    # load trucks
    package_id_lists = load.load_trucks(truck_one, truck_two, truck_three)
    # ------------------------------------------------------------------------------------------------------3/5:ADJ LIST
    # create adj lists for trucks
    # determine delivery order - nearest neighbor algorithm
    t1_adj_list = adj_list.make_adj_list(truck_one, package_id_lists[0])
    route_one = routing.sort(t1_adj_list[0], t1_adj_list[1])
    t2_adj_list = adj_list.make_adj_list(truck_two, package_id_lists[1])
    route_two = routing.sort(t2_adj_list[0], t2_adj_list[1])
    t3_adj_list = adj_list.make_adj_list(truck_three, package_id_lists[2])
    route_three = routing.sort(t3_adj_list[0], t3_adj_list[1])
    # -------------------------------------------------------------------------------------------------------5/5:DELIVER
    # delivery sim
    total_distance = 0
    total_distance += simulate.delivery(route_one, 8.0)
    total_distance += simulate.delivery(route_two, 9.1)
    total_distance += simulate.delivery(route_three, 10.35)
    print('|++++++++++++++++++++++++++++ALL PACKAGES DELIVERED ON TIME++++++++++++++++++++++++++++|')
    print('|++++++++++++++++++++++++++++++FINAL MILEAGE:', total_distance, 'miles++++++++++++++++++++++++++++++|\n')


# snapshot all at given time
def snapshot_of_all(time):
    # Simulates each truck's delivery to show the status of any and every package for given time

    # ---------------------------------------------------------------------------------------------------------1/5:SETUP
    truck_one = []    # early deliveries
    truck_two = []    # Late Arrivals
    truck_three = []  # errors + the rest
    # get time in military time, convert to float
    chosen_time = float(time[0:2]) + (float(time[-2::]) / 60)
    # -------------------------------------------------------------------------------------------------------2/5:LOADING
    # load trucks
    package_id_lists = load.load_trucks(truck_one, truck_two, truck_three)
    t1_package_ids = package_id_lists[0].copy()  # truck 1 list
    t2_package_ids = package_id_lists[1].copy()  # truck 2 list
    t3_package_ids = package_id_lists[2].copy()  # truck 3 list
    # ------------------------------------------------------------------------------------------------------3/5:ADJ LIST
    # create adj lists for trucks
    t1_adj_list = adj_list.make_adj_list(truck_one, package_id_lists[0])
    route_one = routing.sort(t1_adj_list[0], t1_adj_list[1])
    # ------------------------------------------------------------------------------------------------------5/5:SNAPSHOT
    # truck one
    print('Chosen Time:', chosen_time)
    print('TRUCK ONE--------------------------------')
    if chosen_time > 8.0:
        # print status updates
        current_location = ' HUB'
        current_time = 8.0
        time_elapsed = 0
        print('Package IDs: ', t1_package_ids)
        temp_id_list = t1_package_ids.copy()
        for package_id in temp_id_list:
            for stop in route_one:
                driving_time = float(str(distance.get_distance(current_location, stop))) / float(18)
                current_location = stop
                time_elapsed += driving_time
                current_time = 8.0 + time_elapsed
                for package_hash in truck_one:
                    if package_hash.get(package_id) is not None:
                        address = package_hash.get(package_id)[0]
                        if str(stop) == str(address):
                            delivered_time = current_time
                            if chosen_time > delivered_time:
                                status = 'delivered'
                                filler = '@ ' + ('' if + floor(delivered_time) >= 10 else '0') + \
                                          str(floor(delivered_time)) + ':' + \
                                          '{1:02.0f}'.format(*divmod(delivered_time % 1 * 60, 60))
                                print(status, filler, ': Package #', package_id, 'for', address)
                            else:
                                status = '         en route'
                                print(status, ': Package #', package_id, 'for', address)
                            current_location = ' HUB'
                            current_time = 8.0
                            time_elapsed = 0
                            driving_time = 0
                            break
                        else:
                            continue
                        break
                    else:
                        continue
                    break
                else:
                    continue
                break

    else:
        temp_id_list = t1_package_ids.copy()
        for package_id in temp_id_list:
            for stop in route_one:
                for package_hash in truck_one:
                    if package_hash.get(package_id) is not None:
                        address = package_hash.get(package_id)[0]
                        if str(stop) == str(address):
                            status = '            at hub'
                            print(status, ': Package #', package_id, 'for', address)
    # ------------------------------------------------------------------------------------------------------
    # create truck 2 adj list and route
    t2_adj_list = adj_list.make_adj_list(truck_two, package_id_lists[1])
    route_two = routing.sort(t2_adj_list[0], t2_adj_list[1])
    # truck two
    print('Chosen Time:', chosen_time)
    print('TRUCK TWO--------------------------------')
    if chosen_time > 9.1:
        # print status updates
        current_location = ' HUB'
        current_time = 9.1
        time_elapsed = 0
        print('Package IDs: ', t2_package_ids)
        temp_id_list = t2_package_ids.copy()
        for package_id in temp_id_list:
            for stop in route_two:
                driving_time = float(str(distance.get_distance(current_location, stop))) / float(18)
                current_location = stop
                time_elapsed += driving_time
                current_time = 9.1 + time_elapsed
                for package_hash in truck_two:
                    if package_hash.get(package_id) is not None:
                        address = package_hash.get(package_id)[0]
                        if str(stop) == str(address):
                            delivered_time = current_time
                            if chosen_time > delivered_time:
                                status = 'delivered'
                                filler = '@ ' + ('' if + floor(delivered_time) >= 10 else '0') + \
                                         str(floor(delivered_time)) + ':' + \
                                         '{1:02.0f}'.format(*divmod(delivered_time % 1 * 60, 60))
                                print(status, filler, ': Package #', package_id, 'for', address)
                            else:
                                status = '         en route'
                                print(status, ': Package #', package_id, 'for', address)
                            current_location = ' HUB'
                            current_time = 8.0
                            time_elapsed = 0
                            driving_time = 0
                            break
                        else:
                            continue
                        break
                    else:
                        continue
                    break
                else:
                    continue
                break

                current_location = stop

    else:
        temp_id_list = t2_package_ids.copy()
        for package_id in temp_id_list:
            for stop in route_two:
                for package_hash in truck_two:
                    if package_hash.get(package_id) is not None:
                        address = package_hash.get(package_id)[0]
                        if str(stop) == str(address):
                            status = '            at hub'
                            print(status, ': Package #', package_id, 'for', address)

    # ------------------------------------------------------------------------------------------------------
    # create truck 3 adj list and route
    t3_adj_list = adj_list.make_adj_list(truck_three, package_id_lists[2])
    route_three = routing.sort(t3_adj_list[0], t3_adj_list[1])
    # truck three
    print('Chosen Time:', chosen_time)
    print('TRUCK THREE--------------------------------')
    if chosen_time > 10.35:
        # print status updates
        current_location = ' HUB'
        current_time = 10.35
        time_elapsed = 0
        print('Package IDs: ', t3_package_ids)
        temp_id_list = t3_package_ids.copy()
        for package_id in temp_id_list:
            for stop in route_three:
                driving_time = float(str(distance.get_distance(current_location, stop))) / float(18)
                current_location = stop
                time_elapsed += driving_time
                current_time = 10.35 + time_elapsed
                for package_hash in truck_three:
                    if package_hash.get(package_id) is not None:
                        address = package_hash.get(package_id)[0]
                        if str(stop) == str(address):
                            delivered_time = current_time
                            if chosen_time > delivered_time:
                                status = 'delivered'
                                filler = '@ ' + ('' if + floor(delivered_time) >= 10 else '0') + \
                                         str(floor(delivered_time)) + ':' + \
                                         '{1:02.0f}'.format(*divmod(delivered_time % 1 * 60, 60))
                                print(status, filler, ': Package #', package_id, 'for', address)
                            else:
                                status = '         en route'
                                print(status, ': Package #', package_id, 'for', address)
                            current_location = ' HUB'
                            current_time = 8.0
                            time_elapsed = 0
                            driving_time = 0
                            break
                        else:
                            continue
                        break
                    else:
                        continue
                    break
                else:
                    continue
                break

                current_location = stop

    else:
        temp_id_list = t3_package_ids.copy()
        for package_id in temp_id_list:
            for stop in route_three:
                for package_hash in truck_three:
                    if package_hash.get(package_id) is not None:
                        address = package_hash.get(package_id)[0]
                        if str(stop) == str(address):
                            status = '            at hub'
                            print(status, ': Package #', package_id, 'for', address)


# snapshot specific at given time
def package_snapshot(package_id, time):
    # simulates a full delivery until package is found, then info is displayed

    # ---------------------------------------------------------------------------------------------------------1/5:SETUP
    # init
    truck_one = []  # early deliveries
    truck_two = []  # Late Arrivals
    truck_three = []  # errors + the rest
    # get time in military time, convert to float
    chosen_time = float(time[0:2]) + (float(time[-2::]) / 60)

    # -------------------------------------------------------------------------------------------------------2/5:LOADING
    # load
    package_id_lists = load.load_trucks(truck_one, truck_two, truck_three)
    t1_package_ids = package_id_lists[0].copy()  # truck 1 list
    t2_package_ids = package_id_lists[1].copy()  # truck 2 list
    t3_package_ids = package_id_lists[2].copy()  # truck 3 list
    # --------------------------------------------------------------------------------------------------3/5:FIND PACKAGE
    # find which truck has package
    truck_with_package = []
    package_address = ''
    start_time = 0
    package_index = 0
    i = 0

    # which truck is it on
    is_missing = True
    temp_package_ids = []
    print('###################################')
    print('Which Truck has Package #', package_id, "?")
    while is_missing:
        for curr_id in t1_package_ids:
            if int(curr_id) == int(package_id):
                print('  Truck 1')
                is_missing = False
                start_time = 8.0
                truck_with_package = truck_one
                package_index = curr_id
                temp_package_ids = t1_package_ids.copy()
                for package in truck_with_package:
                    if package.get(curr_id)[0] is not None:
                        package_address = package.get(curr_id)[0]
                        break
        for curr_id in t2_package_ids:
            if int(curr_id) == int(package_id):
                print('  Truck 2')
                is_missing = False
                start_time = 9.1
                truck_with_package = truck_two
                package_index = curr_id
                temp_package_ids = t2_package_ids.copy()
                for package in truck_with_package:
                    if package.get(curr_id) is not None:
                        package_address = package.get(curr_id)[0]
                        break
                break
        for curr_id in t3_package_ids:
            if int(curr_id) == int(package_id):
                print('  Truck 3')
                is_missing = False
                start_time = 10.35
                truck_with_package = truck_three
                package_index = curr_id
                temp_package_ids = t3_package_ids.copy()
                for package in truck_with_package:
                    if package.get(curr_id) is not None:
                        package_address = package.get(curr_id)[0]
                        break
        break

    # ------------------------------------------------------------------------------------------------4/5:ADJ LIST+ROUTE
    # get adj list, make route
    destination_adj_list = adj_list.make_adj_list(truck_with_package, temp_package_ids)
    route = routing.sort(destination_adj_list[0], destination_adj_list[1])

    # ----------------------------------------------------------------------------------------------------5/5:GET STATUS
    # use route to sim and get info about package
    delivered_time = start_time
    current_location = ' HUB'
    if len(route) > 0:
        for stop in route:
            # print('distance:', distance.get_distance(current_location, stop))
            # print('curr loc:', current_location)
            # print('stop:', stop)
            driving_time = float(str(distance.get_distance(current_location, stop))) / float(18)
            delivered_time += driving_time
            delivery_address = package_address
            if stop == delivery_address:
                # set status based on time
                filler = ''
                status = 'en route'
                if chosen_time < start_time:
                    status = 'at hub'
                elif chosen_time > delivered_time:
                    # if delivery time has passed, mark package as delivered
                    status = 'delivered'
                    filler = '@ ' + ('' if + floor(delivered_time) >= 10 else '0') + \
                             str(floor(delivered_time)) + ':' + \
                             '{1:02.0f}'.format(*divmod(delivered_time % 1 * 60, 60))

                for package in truck_with_package:
                    if package.get(package_id) is not None:
                        print('\n          Chosen Time: ', chosen_time)
                        print('    Chosen Package ID: ', package_id)
                        print('###################################')
                        print('          Package #', package_id)
                        print('            Status:', status, filler)
                        print('  Delivery Address:', package.get(package_id)[0])
                        print(' Delivery Deadline:', package.get(package_id)[1])
                        print('     Delivery City:', package.get(package_id)[2])
                        print('      Delivery Zip:', package.get(package_id)[3])
                        print('    Package Weight:', package.get(package_id)[4])
                        print('###################################')
                        break

            current_location = stop


