# Trayer
# Lee Harvey
# 001341276
# June 2022

# OVERALL PROGRAM SPACETIME COMPLEXITIES
#  TIME COMPLEXITY: O(n^2) -- Limiting Factor: routing.py sorting algo takes O(n^2)
# SPACE COMPLEXITY: O(n)   -- Limiting Factor: number of deliveries to be stored in hashmap

import sys

import main_functions


def pick_option():
    # Basic UI to navigate options

    inp = input("Choose "
                "q-quit, "
                "0-Simulation, "
                "1-View Status of All Packages at Time, "
                "2-View Package Info Given Package and Time: ")
    if inp == "0":
        print("You chose Simulation")
        main_functions.run_simulation()
        return pick_option()
    elif inp == "1":
        print("You chose Snapshot of All")
        prompt = input("Type in your selected screenshot time in military style 00:00 : ")
        main_functions.snapshot_of_all(prompt)
        return pick_option()
    elif inp == "2":
        print("You chose Snapshot of Package")
        id_prompt = input("Enter the ID number of the Package you want to check on: ")
        time_prompt = input("Type in your selected screenshot time in military style 00:00 : ")
        main_functions.package_snapshot(id_prompt, time_prompt)
        return pick_option()
    elif inp == "q":
        sys.exit()
    else:
        print("You must enter 0, 1, or 2")
        return pick_option()


pick_option()
