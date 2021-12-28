from json import dump
import argparse
from utils.dataProcess.hive_transform import Transform as HiveTransform
from utils.crawler.batch import run as crawler_with_processpool
from utils.dataProcess.process import Process
from utils.dataProcess.neighbor_process import main as neighbor_process
from utils.dataProcess.deduplicate import Dedulicate
from utils.dataProcess.data_process_mysql import generate_schema
from utils.dataProcess.review_nlp import run as review_nlp
from utils.dataProcess.decompose import Decomposer
from utils.dataProcess.release_date import run as release_date_process
import os

def generate_dir():
    if not os.path.exists('./data'):
        os.mkdir('./data')

def get_args():

    parser = argparse.ArgumentParser(formatter_class=argparse.ArgumentDefaultsHelpFormatter)
    parser.add_argument('--crawler', dest='crawler', action='store_true')
    parser.add_argument('--extract', dest='extract', action='store_true')
    parser.add_argument('--transform', dest='transform', action='store_true')
    return parser.parse_args()


def main():
    generate_dir()
    args = get_args()
    if args.crawler:
        crawler_with_processpool()
    
    if args.extract:
        process = Process()
        process.process_detail()
        process.process_review()

        neighbor_process()

        deduplicate = Dedulicate()
        deduplicate.dedulicate_detail()
        deduplicate.dedulicate_review()
        deduplicate.dedulicate_moviesource()

        review_nlp()
    if args.transform:
        release_date_process()

        decomposer = Decomposer()
        decomposer.run()

        hive_load = HiveTransform()
        hive_load.run()

        generate_schema()

if __name__ == '__main__':
    main()