import pandas as pd
import csv


class Decomposer:
    """
    分解原始数据，获取movie、actor、director、label对应的schema数据
    """

    def __init__(self):
        self.data_source_path = "./data/deduplicated_detail_date.csv"
        self.movie_schema_path = "./data/processed_data/movie_schema.csv"
        self.actor_schema_path = "./data/processed_data/actor_schema.csv"
        self.director_schema_path = "./data/processed_data/director_schema.csv"
        self.label_schema_path = "./data/processed_data/label_schema.csv"
        self.score_schema_path = "./data/processed_data/score_schema.csv"

    def run(self):
        data_source = pd.read_csv(self.data_source_path)
        movie_schema = data_source[['asin', 'title', 'score', 'release_date']]
        movie_schema.to_csv(self.movie_schema_path, index=False)
        actor_cnt = {}
        director_cnt = {}
        label_cnt = {}
        with open(self.actor_schema_path, 'w+') as file:
            for i, row in data_source.iterrows():
                if pd.isna(row[4]):
                    continue
                name_list = row[4].split(',')
                for name in name_list:
                    if len(name) > 0 and name[0].isalpha():
                        actor_cnt[name] = actor_cnt.get(name, 0) + 1
            writer = csv.writer(file)
            writer.writerow(['name', 'asin', 'count'])
            for i, row in data_source.iterrows():
                if pd.isna(row[4]):
                    continue
                name_list = row[4].split(',')
                for name in name_list:
                    if len(name) > 0 and name[0].isalpha():
                        writer.writerow([name, row[0], actor_cnt[name]])
        with open(self.director_schema_path, 'w+') as file:
            for i, row in data_source.iterrows():
                if pd.isna(row[3]):
                    continue
                name_list = row[3].split(',')
                for name in name_list:
                    if len(name) > 0 and name[0].isalpha():
                        director_cnt[name] = director_cnt.get(name, 0) + 1
            writer = csv.writer(file)
            writer.writerow(['name', 'asin', 'count'])
            for i, row in data_source.iterrows():
                if pd.isna(row[3]):
                    continue
                name_list = row[3].split(',')
                for name in name_list:
                    if len(name) > 0 and name[0].isalpha():
                        writer.writerow([name, row[0], director_cnt[name]])
        score_cnt = {}
        with open(self.label_schema_path, 'w+') as file:
            writer = csv.writer(file)
            writer.writerow(['genre', 'asin', 'count'])
            for i, row in data_source.iterrows():
                if pd.isna(row[6]):
                    continue
                name_list = row[6].split(',')
                for name in name_list:
                    if len(name) > 0 and name[0].isalpha():
                        label_cnt[name] = label_cnt.get(name, 0) + 1
            for i, row in data_source.iterrows():
                if pd.notna(row[2]):
                    score_cnt[row[2]] = score_cnt.get(row[2], 0) + 1
                if pd.isna(row[6]):
                    continue
                name_list = row[6].split(',')
                for name in name_list:
                    if len(name) > 0 and name[0].isalpha():
                        writer.writerow([name, row[0], label_cnt[name]])


if __name__ == '__main__':
    a = Decomposer()
    a.run()
