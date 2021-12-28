import json
import os
from tqdm import tqdm

path_raw_data = "./data/neighbors/"
path_deduplicated_list = "./data/processed_data/"

class DSU:
    def __init__(self, N):
        self.root = [i for i in range(N)]
        
    def find(self, k):
        if self.root[k] == k:
            return k
        self.root[k] = self.find(self.root[k])
        return self.root[k]
    
    def union(self, a, b):
        x = self.find(a)
        y = self.find(b)
        if x != y:
            self.root[y] = x
        return

def parse_neighbors(files):
    res = dict()
    for file in files:
        file_name = file.split(".")[0]
        res.update({file_name : []})
        with open(path_raw_data + file, "r") as f:
            neighbors = json.load(f)['neighbors']
            for neighbor in neighbors:
                res[file_name].append(neighbor)
    return res

def init(file_list):
    
    vis = set()
    num_str_map = []
    tar_file = []

    for _self, _neighbors in tqdm(file_list.items()):
        tar_file.append(_self)
        for file in _neighbors:
            if not file in vis:
                vis.add(file)
                num_str_map.append(file)
    
    dsu = DSU(len(num_str_map))

    for _self, _neighbors in tqdm(file_list.items()):
        self_index = num_str_map.index(_self)
        for neighbor in _neighbors:
            neighbor_index = num_str_map.index(neighbor)
            dsu.union(self_index, neighbor_index)

    return dsu, num_str_map, tar_file

def deduplicate(dsu, file_list, num_str_map, tar_file):
    vis = set()
    res = dict()
    tar_dict = dict()
    ret = []
    for index, item in tqdm(enumerate(num_str_map)):
        if dsu.find(index) == index:
            res.update({index : []})
        if not item in tar_file:
            continue
        tar_dict.update({item : index})
    for item, index in tqdm(tar_dict.items()):
        index = dsu.find(index)
        res[index].append(item)
    for i in res.values():
        ret.append(i)
    return ret

def main():
    files = os.listdir(path_raw_data)
    file_list = parse_neighbors(files)
    dsu, num_str_map, tar_file = init(file_list)
    result = deduplicate(dsu, file_list, num_str_map, tar_file)
    print(len(result))
    with open(path_deduplicated_list + "series.json", "w") as f:
        json.dump(result, f)
    

if __name__ == "__main__":
    main()
