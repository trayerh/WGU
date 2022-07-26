# delivery sim
from math import floor

import distance


def delivery(route, departure_time):
    # Simulates the truck going on a delivery run, returns total distance traveled
    # - by keeping track of distance between routes and dividing by speed (18mph) we can get time elapsed
    # - adding time elapsed to Truck takeoff time gets current time which is used to set status
    # iterates through every entry in route, time complexity O(n) space complexity O(1)

    # speed 18mph
    distance_traveled = 0
    time_elapsed = 0
    start_time = '{0:02.0f}:{1:02.0f}'.format(*divmod(8 * 60, 60))
    current_time = departure_time
    current_location = ' HUB'
    packages_delivered = 0
    print('\n--------------------------Truck leaving to deliver ', len(route), ' packages-----------------------|')
    print('Starting @', str(floor(current_time)), ':', '{1:02.0f}'.format(*divmod(current_time % 1 * 60, 60)), 'AM')
    print('Route:')
    i = 1
    for x in route:
        print('  Stop ', i, ' - ', x)
        i += 1
    if len(route) > 0:
        for drop_off in route:
            driving_time = float(str(distance.get_distance(current_location, drop_off))) / float(18)
            time_elapsed += driving_time
            print('     Leaving', current_location,
                  '@', str(floor(current_time)), ':', '{1:02.0f}'.format(*divmod(current_time % 1 * 60, 60)),
                  'PM' if floor(current_time) >= 12 else 'AM',
                  'heading to', drop_off)
            current_time = departure_time + time_elapsed
            current_location = drop_off
            packages_delivered += 1
            print('     Delivery Complete! Remaining Deliveries:',
                len(route)-packages_delivered, '/', len(route))
            print('          Current Time', str(floor(current_time)), ':',
                '{1:02.0f}'.format(*divmod(current_time % 1 * 60, 60)), 'PM' if floor(current_time) >= 12 else 'AM')

    print('\nAll Deliveries Done! onto Return Trip')
    driving_time = float(str(distance.get_distance(current_location, ' HUB'))) / float(18)
    time_elapsed += driving_time
    print('     Leaving', current_location,
          '@', str(floor(current_time)), ':', '{1:02.0f}'.format(*divmod(current_time % 1 * 60, 60)),
          'PM' if floor(current_time) >= 12 else 'AM',
          'heading back to hub')
    current_time = departure_time + time_elapsed
    current_location = ' HUB'
    distance_traveled = time_elapsed * 18
    print('\n         Arrived back to Hub '
          '@', str(floor(current_time)), ':', '{1:02.0f}'.format(*divmod(current_time % 1 * 60, 60)),
          'PM' if floor(current_time) >= 12 else 'AM',
          '\n     Total Distance Traveled -', distance_traveled, ' miles')
    
    print('             Total Trip Time -', str(floor(time_elapsed)), ':',
          '{1:02.0f}'.format(*divmod(time_elapsed % 1 * 60, 60)), 'hrs\n'
          '---------------------------------------------------------------------------------------|')

    return distance_traveled
