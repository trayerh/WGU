import csv

import hashmap


#  Time Complexity - O(n)
# Space Complexity - O(n)
def load_package(package_id, truck):
    # Gets info from CSV using Package ID as an index,
    # adds info to an index and then that index to a hashmap
    #       w/ the package id as the key
    # that hashmap is appended to the truck package list

    package = []
    package_info = hashmap.HashMap()
    with open('docs/WGUPS Package File.csv') as csv_file:
        reader = csv.reader(csv_file)
        package_rows = list(reader)
        columns = package_rows[package_id]
        key = columns[0]            # package_id
        package.append(columns[1])  # delivery_address  0
        package.append(columns[5])  # delivery_deadline 1
        package.append(columns[2])  # delivery_city     2
        package.append(columns[4])  # delivery_zip      3
        package.append(columns[6])  # package_weight    4
        package.append('At hub')    # delivery_status   5
        package_info.add(key, package)
    truck.append(package_info)
    # print(truck)
    
    
def load_trucks(truck_one, truck_two, truck_three):
    # simulate loading the 40 packages onto their trucks.
    # grouped to satisfy criteria in comments
    # returns a list of the trucks lists of package ids

    package_id_lists = []

    # load truck 1 - departure 8:00am - 15pkgs
    t1_package_id_list = [1, 7, 8, 13, 14, 15, 16, 19, 20, 29, 30, 31, 34, 37, 40]
    load_package(1, truck_one)   # must be delivered by 10:30                 - 195 W
    load_package(7, truck_one)   # *same addr. as 29
    load_package(8, truck_one)   # *same addr. as 30
    load_package(13, truck_one)  # grouped with 16, 20                        - 2010 W
    load_package(14, truck_one)  # must be delivered with 15, 19              - 4300 S
    load_package(15, truck_one)  # must be delivered by 9:00                  - 4580 S
    load_package(16, truck_one)  # must be delivered with 13, 19                *15
    load_package(19, truck_one)  # grouped with 14, 16                        - 177
    load_package(20, truck_one)  # must be delivered with 13, 15              - 3595
    load_package(29, truck_one)  # must be delivered by 10:30                 - 1330
    load_package(30, truck_one)  # must be delivered by 10:30                 - 300 State
    load_package(31, truck_one)  # must be delivered by 10:30                 - 3365 S
    load_package(34, truck_one)  # must be delivered by 10:30                   *15
    load_package(37, truck_one)  # must be delivered by 10:30                 - 410
    load_package(40, truck_one)  # must be delivered by 10:30                 - 380 W
    # load truck 2 - departure 9:06am - 13pkgs
    t2_package_id_list = [2, 3, 6, 12, 18, 25, 27, 28, 32, 33, 35, 36, 38]
    load_package(2, truck_two)   # 2530 S 500 E
    load_package(3, truck_two)   # Can only be on truck 2
    load_package(6, truck_two)   # WVC - Delayed on flight---will not arrive to depot until 9:05 am
    load_package(12, truck_two)  # WVC
    load_package(18, truck_two)  # Can only be on truck 2
    load_package(25, truck_two)  # Delayed on flight---will not arrive to depot until 9:05 am
    load_package(27, truck_two)  # 1060 Dalton
    load_package(28, truck_two)  # Delayed on flight---will not arrive to depot until 9:05 am
    load_package(32, truck_two)  # Delayed on flight---will not arrive to depot until 9:05 am
    load_package(33, truck_two)  # *same addr. as 2
    load_package(35, truck_two)  # *27
    load_package(36, truck_two)  # WVC - Can only be on truck 2
    load_package(38, truck_two)  # Can only be on truck 2
    # load truck 3 - departure 10:21am - 12pkgs
    t3_package_id_list = [4, 5, 9, 10, 11, 17, 21, 22, 23, 24, 26, 39]
    load_package(4, truck_three)
    load_package(5, truck_three)
    load_package(9, truck_three)  # Wrong addr. listed, corrected to 410 S State St., Salt Lake City, UT 84111 @10:20
    load_package(10, truck_three)
    load_package(11, truck_three)
    load_package(17, truck_three)
    load_package(21, truck_three)
    load_package(22, truck_three)  # Murray
    load_package(23, truck_three)
    load_package(24, truck_three)  # Murray
    load_package(26, truck_three)
    load_package(39, truck_three)

    package_id_lists.append(t1_package_id_list)
    package_id_lists.append(t2_package_id_list)
    package_id_lists.append(t3_package_id_list)

    return package_id_lists
