from jieba import analyse
import docx
from nlp import doc2docx
import jieba.posseg
import os
import csv
import pandas as pd

def readfile(file):
    text=''
    with open(file,encoding='utf-8') as f:
        text=f.read()
    return text.replace(',','')
def dosegment_all(sentence):
    sentence_seged = jieba.posseg.cut(sentence.strip())
    dong_ci = []
    xing_rong_ci = []

    for x in sentence_seged:
        if x.flag in ['v','vd','vn']:
            dong_ci.append(x.word)
        if x.flag in ['a','an','ad','ag']:
            xing_rong_ci.append(x.word)

    return ','.join(set(dong_ci)),','.join(set(xing_rong_ci))


dir='./tfidffile/'
files=os.listdir(dir)
for file in files:
    a,b=dosegment_all(readfile(dir+file))
    print(file)
    print('动词:',a)
    print('形容词:',b)

