# nearest neighbor

import distance

#  Time Complexity - O(n^2)
# Space Complexity - O(n)
def sort(all_address_list, adj_list):
    # Uses a Nearest Neighbor algorithm to sort the address list
    # returning a list of address to use as a route


    ordered_list = []
    min_distance = 100
    min_key = ''
    temp_address_list = all_address_list.copy()

    # 1- get closest address to start
    for entry in temp_address_list:
        current_distance = distance.get_distance(entry, ' HUB')
        if min_distance == 100 or current_distance < min_distance:
            min_distance = current_distance
            min_key = entry
    else:
        ordered_list.append(min_key)
        for entry in temp_address_list:
            if entry == min_key:
                temp_address_list.remove(entry)

    # 2- Recursively get closest to current
    for current_address in ordered_list:
        curr = current_address
        min_distance = 100
        min_key = ''
        # check current address to current list
        current_adj_list = adj_list.get(curr)
        if current_adj_list is not None:
            for limb in current_adj_list.map:
                if limb is not None:
                    for key, value in limb:
                        address = key
                        current_distance = value

                        is_new = True
                        for older_entry in ordered_list:
                            if address == older_entry:
                                is_new = False
                        if is_new:
                            # compare with current min
                            if float(min_distance) > float(current_distance) > 0.0:
                                # update
                                min_distance = current_distance
                                min_key = address

        if min_key != '':
            # min found, add to list
            ordered_list.append(min_key)

        if len(ordered_list) > len(all_address_list):
            break

    print('\n|----------------------------------------|')
    print('      # of Packages:', len(all_address_list))
    print('  # of Destinations:', len(ordered_list))
    print('  Route:')
    i = 1
    for x in ordered_list:
        print('     Stop', i, ' - ', x)
        i += 1
    print('|--------------------------------------|')

    return ordered_list
