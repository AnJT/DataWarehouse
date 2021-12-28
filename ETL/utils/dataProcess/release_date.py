import json
import pandas as pd
import csv
from tqdm import tqdm
import re


def get_data_count():
    df = pd.read_csv('./data/processed_data/deduplicated_detail.csv', encoding='utf-8')
    df = df.where(df.notnull(), None)
    date = {'year':{}, 'season':{}, 'month':{}, 'day':{}}
    for detail in tqdm(df.values):
        if detail[5] is None:
            continue
        res = re.match('(\d+?)-(\d+?)-(\d+)', detail[5])
        year = f'{res.group(1)}'
        month =  f'{res.group(1)}-{res.group(2)}'
        day = f'{res.group(1)}-{res.group(2)}-{res.group(3)}'
        season = year + '-' + str((int(res.group(2)) - 1) // 3 + 1)
        try:
            date['year'][year] += 1
        except:
            date['year'][year] = 1
        try:
            date['month'][month] += 1
        except:
            date['month'][month] = 1
        try:
            date['day'][day] += 1
        except:
            date['day'][day] = 1
        try:
            date['season'][season] += 1
        except:
            date['season'][season] = 1

    with open(f'./data/processed_data/release_date.json','w+', encoding='utf-8') as f:
        json.dump(date,f,ensure_ascii=False, indent=4, separators=(',', ':'))   

def generate_date():
    with open('./data/processed_data/release_date.json', encoding='utf-8') as f:
        date = json.load(f)
    with open('./data/processed_data/release_date.csv', 'w', newline='', encoding='utf-8') as f:
        writer = csv.writer(f)
        writer.writerow(['id', 'year', 'year_count','season','season_count', 'month', 'month_count', 'day', 'day_count'])
        for idx, key in enumerate(date['day'].keys()):
            year, month, day = key.split('-')
            season = str((int(month) - 1) // 3 + 1)
            d = [idx, year, date['year'][year],season,date['season'][year+'-'+season], month, date['month'][year+'-'+month], day, date['day'][key]]
            writer.writerow(d)

def generate_detail():
    df = pd.read_csv('./data/processed_data/release_date.csv', encoding='utf-8')
    date = df.values
    date_dict = {}
    for d in date:
        date_dict[str(d[1])+'-'+str(d[5])+'-'+str(d[7])] = d[0]
    df = pd.read_csv('./data/processed_data/deduplicated_detail.csv', encoding='utf-8')
    df = df.where(df.notnull(), None)
    with open('./data/processed_data/deduplicated_detail_date.csv', 'w', newline='', encoding='utf-8') as f:
        writer = csv.writer(f)
        writer.writerow(['asin', 'title', 'score', 'director', 'actor', 'release_data', 'label'])
        for detail in df.values:
            if detail[5] is not None:   
                detail[5] = date_dict[detail[5]]
            writer.writerow(detail)
    
def run():
    get_data_count()
    generate_date()
    generate_detail()

if __name__ =='__main__':
    get_data_count()
    generate_date()
    generate_detail()