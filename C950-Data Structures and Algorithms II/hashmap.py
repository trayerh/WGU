# Hash Map


class HashMap:
    #  Hashmap to satisfy D, E, and F
    # add(key, value) for E
    #        get(key) for F
    # Complete with resize function for when occupancy gets high
    def __init__(self):
        self.size = 2
        self.occupancy = 0
        self.map = [None] * self.size

    def _get_hash(self, key):
        h = 0
        for char in str(key):
            h += ord(char)
        return h % self.size

    def add(self, key, value):
        key_hash = self._get_hash(key)
        key_value = [key, value]

        # if entry doesn't exist, add it
        if self.map[key_hash] is None:
            self.map[key_hash] = list([key_value])
            self.occupancy += 1
            # is resize necessary?
            if self.occupancy > self.size ** 2:
                self._resize(self.map, self.size)
            return True
        # else update the old entry
        else:
            for pair in self.map[key_hash]:
                if pair[0] == key:
                    pair[1] == value
                    return True
            self.map[key_hash].append(key_value)
            self.occupancy += 1
            # is resize necessary?
            if self.occupancy > self.size ** 2:
                self._resize(self.map, self.size)
            return True

    def get(self, key):
        key_hash = self._get_hash(key)
        if self.map[key_hash] is not None:
            # print(self.map[key_hash])
            for pair in self.map[key_hash]:
                if str(pair[0]) == str(key):
                    return pair[1]
        return None

    def delete(self, key):
        key_hash = self._get_hash(key)

        if self.map[key_hash] is None:
            return False
        for i in range(0, len(self.map[key_hash])):
            if self.map[key_hash][i][0] == key:
                self.map[key_hash].pop(i)
                return True

    def print(self):
        print('----------------FULL HASHMAP---------------')
        for item in self.map:
            if item is not None:
                for key, value in item:
                    print('|     ', key, ':', str(value))
            print('\n')
        print('-------------------------------------------')

    #  Time Complexity - O(n) iterate over all entries in old map
    # Space Complexity - O(n) hashmap grows linearly with more entries
    def _resize(self, old_map, current_size):

        # get next largest prime
        p = 1
        p_old = 0
        while p < current_size ** 2:
            p_temp = p
            p = p + p_old
            p_old = p_temp

        # make new list with bigger size, re-add old elements
        new = self
        new.__init__()
        new.size = p
        new.map = [None] * self.size

        if old_map is not None:
            for limb in old_map:
                if limb is not None:
                    for key, value in limb:
                        new.add(key, value)
