import distance
import hashmap


def make_adj_list(truck, package_id_list):
    # For each package in the truck passed to this function,
    # make a hashmap of all the other addresses and their distances from the current package
    # append the map and package id (as key) as an entry to the list of adjacencies
    # line 23: traversing a list for each entry in said list takes O(n^2) time
    if truck != '':
        address_list = []
        temp_id_list = package_id_list

        for package in truck:
            for package_id in temp_id_list:
                package_info = package.get(package_id)
                if package_info is not None:
                    address_list.append(package_info[0])
                    temp_id_list.remove(package_id)
                    break

        address_adj_list = hashmap.HashMap()
        for start_address in address_list:
            address_dict = hashmap.HashMap()
            # make list of distances for each address
            for end_address in address_list:
                address_dict.add(end_address, distance.get_distance(start_address, end_address))
            address_adj_list.add(start_address, address_dict)
        tables = [address_list, address_adj_list]
        return tables

