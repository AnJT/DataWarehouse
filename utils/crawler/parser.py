from bs4 import BeautifulSoup
import re
import calendar
from parser_utils import spoken_word_to_number

def parse_home(html):
    soup = BeautifulSoup(html.text.encode('gbk', 'ignore').decode('gbk'), 'lxml')
    res = {}
    detail = soup.find('div', id='detailBullets_feature_div').ul
    for li in detail.find_all('li'):
        spans = li.span.find_all('span')
        key = spans[0].text.split('\n')[0]
        value = spans[1].text
        res[key] = value
    return res

def parse_review(html):
    html.encoding = 'utf-8'
    soup = BeautifulSoup(html.text.encode('gbk', 'ignore').decode('gbk'), 'lxml')
    res = []
    reviews = soup.findAll(name="div", attrs={"data-hook" : "review"})
    for idx, review in enumerate(reviews):
        customer_review_div = review.div.div
        divs = list(customer_review_div.children)

        user_id = re.search('account.(.*)/ref', divs[0].a['href']).group(1)
        user_name = divs[0].a.div.next_sibling.span.text
        helpfulness = parse_helpfulness(str(divs[5].span.div.span.text).strip())
        text = str(divs[4].span.span.text).strip()
        score = str(divs[1].a.i.span.text).split()[0]   
        review_time = parse_time(divs[2].text)
        summary = divs[1].a.next_sibling.next_sibling.span.text
        print('summary:', divs[1].a.next_sibling.next_sibling.span)
        res.append(
            {
                'review_id': review['id'],
                'user_id': user_id,
                'user_name': user_name,
                'helpfulness': helpfulness,
                'text': text,
                'score': score,
                'time': review_time,
                'summary': summary
            }
        )
    return res

def parse_time(time):
    res = re.search('on (\w+) (\d+), (\d+)', time)
    month = list(calendar.month_name).index(res.group(1))
    day = res.group(2)
    year = res.group(3)
    return f'{month} {day}, {year}'

def parse_helpfulness(helpfulness):
    if helpfulness == 'Helpful':
        helpfulness = 0
    elif str(helpfulness[0]).isdigit():
        helpfulness = int(str(helpfulness).split()[0])
    else:
        word = re.search('(.*) person', helpfulness).group(1)
        helpfulness = spoken_word_to_number(word)
    return helpfulness



if __name__ =='__main__':
    from request import *
    from batch import *
    asin = 'B00005JLI6'
    headers = get_random_headers()
    html, review_html = get_html(asin.strip(), headers)
    res = parse_review(review_html)
    # res = parse_home(html)
    # print(res)
    with open('hh.json', 'w+') as f:
        json.dump(res, f, ensure_ascii=False, indent=4, separators=(',', ':'))
    # for item in res:
    #     print(item["helpfulness"])