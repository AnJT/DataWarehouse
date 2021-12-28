from numpy import save
import pandas as pd
import json
from tqdm import tqdm

class Transform:

    def __init__(self, detail_csv='./data/processed_data/deduplicated_detail.csv',
                    release_date_csv='./data/processed_data/release_date.csv') -> None:
        self.detail_csv = detail_csv
        self.release_date_csv = release_date_csv
    
    def movie(self, save_path):
        df = pd.read_csv(self.detail_csv, encoding='utf-8')
        df = df.fillna('')
        with open(save_path, 'w', encoding='utf-8') as f:
            for idx, detail in tqdm(enumerate(df.values)):
                f.write('|'.join(map(str, detail)) + '\n')

    def release_date(self, save_path):
        df = pd.read_csv(self.release_date_csv, encoding='utf-8')
        with open(save_path, 'w', encoding='utf-8') as f:
            for idx, detail in tqdm(enumerate(df.values)):
                f.write('|'.join(map(str, detail[1:])) + '\n')
    
    def cooperation(self, save_path):
        with open(save_path, 'r', encoding='utf-8') as f:
            data = json.load(f)
        with open(save_path.replace('cooperation.json', 'actor_actor.txt'), 'w', encoding='utf-8') as f:
            f.write(self._cooperation(data['actor_actor']))
        with open(save_path.replace('cooperation.json', 'director_actor.txt'), 'w', encoding='utf-8') as f:
            f.write(self._cooperation(data['director_actor']))
        with open(save_path.replace('cooperation.json', 'actor_director.txt'), 'w', encoding='utf-8') as f:
            f.write(self._cooperation(data['actor_director']))
        with open(save_path.replace('cooperation.json', 'director_director.txt'), 'w', encoding='utf-8') as f:
            f.write(self._cooperation(data['director_director']))

    def _cooperation(self, data):
        res = ''
        for key, value in data.items():
            s, s1, s2 = key, [], []
            for k, v in value.items():
                s1.append(k)
                s2.append(v)
            s2 = map(str, s2)
            s += '|' + ','.join(s1) + '|' + ','.join(s2)
            res += s + '\n'
        return res

    
    def cooperation_json(self, save_path):
        df = pd.read_csv(self.detail_csv, encoding='utf-8')
        df = df.where(df.notnull(), None)
        cooperation_data = {'actor_actor':{}, 'director_actor':{}, 'director_director':{}, 'actor_director':{}}
        for idx, detail in tqdm(enumerate(df.values)):
            director_data = detail[3]
            actor_data = detail[4]
            if director_data is not None:
                self.cooperation_director_director(cooperation_data, director_data)
            if actor_data is not None:
                self.cooperation_actor_actor(cooperation_data, actor_data)
            if director_data is not None and actor_data is not None:
                self.cooperation_actor_director(cooperation_data, actor_data, director_data)
        with open(save_path, 'w', encoding='utf-8') as f:
            json.dump(cooperation_data, f, ensure_ascii=False, indent=4, separators=(',', ':'))

    def cooperation_actor_director(self, cooperation_data, actor_data, director_data):
        actor_data = actor_data.split(',')
        director_data = director_data.split(',')
        for d in director_data:
            cooperation_data['director_actor'].setdefault(d, {})
            for a in actor_data:
                cooperation_data['actor_director'].setdefault(a, {})
                if a == d:
                    continue
                try:
                    cooperation_data['director_actor'][d][a] += 1
                except:
                    cooperation_data['director_actor'][d][a] = 1
                try:
                    cooperation_data['actor_director'][a][d] += 1
                except:
                    cooperation_data['actor_director'][a][d] = 1


    def cooperation_actor_actor(self, cooperation_data, actor_data):
        actor_data = actor_data.split(',')
        for a1 in actor_data:
            cooperation_data['actor_actor'].setdefault(a1, {})
            for a2 in actor_data:
                if a1 == a2:
                    continue
                try:
                    cooperation_data['actor_actor'][a1][a2] += 1
                except:
                    cooperation_data['actor_actor'][a1][a2] = 1

    def cooperation_director_director(self, cooperation_data, director_data):
        director_data = director_data.split(',')
        for d1 in director_data:
            cooperation_data['director_director'].setdefault(d1, {})
            for d2 in director_data:
                if d1 == d2:
                    continue
                try:
                    cooperation_data['director_director'][d1][d2] += 1
                except:
                    cooperation_data['director_director'][d1][d2] = 1
    
    def score_movie(self, save_path):
        import numpy as np
        df = pd.read_csv(self.detail_csv, encoding='utf-8')
        df = df.where(df.notnull(), None)
        d = {}
        with open(save_path, 'w', encoding='utf-8') as f:
            for detail in df.values:
                if np.isnan(detail[2]):
                    continue
                d.setdefault(float(detail[2]), [])
                d[float(detail[2])].append(detail[0])
            for k, v in d.items():
                f.write(str(k) + '|' + ",".join(v) + '\n')
    
    def director_movie(self, save_path):
        df = pd.read_csv(self.detail_csv, encoding='utf-8')
        df = df.where(df.notnull(), None)
        d = {}
        with open(save_path, 'w', encoding='utf-8') as f:
            for detail in df.values:
                if detail[3] is None:
                    continue
                directors = str(detail[3]).split(',')
                for director in directors:
                    d.setdefault(director, [])
                    d[director].append(detail[0])
            for k, v in d.items():
                s = k + '|' + ','.join(v) + '\n'
                f.write(s)

    def actor_movie(self, save_path):
        df = pd.read_csv(self.detail_csv, encoding='utf-8')
        df = df.where(df.notnull(), None)
        d = {}
        with open(save_path, 'w', encoding='utf-8') as f:
            for detail in df.values:
                if detail[4] is None:
                    continue
                actors = str(detail[3]).split(',')
                for actor in actors:
                    d.setdefault(actor, [])
                    d[actor].append(detail[0])
            for k, v in d.items():
                s = k + '|' + ','.join(v) + '\n'
                f.write(s)
    
    def label_movie(self, save_path):
        df = pd.read_csv(self.detail_csv, encoding='utf-8')
        df = df.where(df.notnull(), None)
        d = {}
        with open(save_path, 'w', encoding='utf-8') as f:
            for detail in df.values:
                if detail[4] is None:
                    continue
                labels = str(detail[-1]).split(',')
                for label in labels:
                    d.setdefault(label, [])
                    d[label].append(detail[0])
            for k, v in d.items():
                s = k + '|' + ','.join(v) + '\n'
                f.write(s)
    
    def generate_load_txt(self):
        self.movie('./data/load/hive/movie.txt')
        self.release_date('./data/load/hive/release_date.txt')
        self.cooperation_json('./data/load/hive/cooperation.json')
        self.cooperation('./data/load/hive/cooperation.json')
        self.score_movie('./data/load/hive/score_movie.txt')
        self.director_movie('./data/load/hive/director_movie.txt')
        self.actor_movie('./data/load/hive/actor_movie.txt')
        self.label_movie('./data/load/hive/label_movie.txt')
    
    def run(self):
        import os
        if not os.path.exists('./data/load'):
            os.mkdir('./data/load')
        if not os.path.exists('./data/load/hive'):
            os.mkdir('./data/load/hive')
        self.generate_load_txt()


if __name__ == '__main__':
    ld = Transform()
    ld.generate_load_txt()
