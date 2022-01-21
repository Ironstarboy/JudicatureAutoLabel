# https://blog.csdn.net/FontThrone/article/details/82792377
from jieba import analyse
import docx

import jieba.posseg
import os
import csv
import CaseType
import json
from pyhanlp import *
import pandas as pd
import re
from my_package import my_IO
def readfile(file):
    text=''
    with open(file,encoding='utf-8') as f:
        text=f.read()
    return text.replace(',','')


def jiebaPOS(sentence):
    # TODO 结巴形容词 词性标注好像效果不行
    sentence_seged = jieba.posseg.cut(sentence.strip())
    dong_ci = []
    xing_rong_ci = []

    for x in sentence_seged:
        if x.flag in ['v','vd','vn']:
            dong_ci.append(x.word)
        if x.flag in ['a']:
            xing_rong_ci.append(x.word)

    return ','.join(set(dong_ci)),','.join(set(xing_rong_ci))

def hanlpPOS(sentence):
    # 默认的标准分词器也就是维特比分词器
    ans=HanLP.segment(sentence)
    dong_ci = []
    xing_rong_ci = []
    for word in list(ans):
        if str(word.nature) in ['v','vd','vn']:
            dong_ci.append(str(word.word))
        if str(word.nature).__contains__('a'):

            xing_rong_ci.append(str(word.word))

    return ','.join(set(dong_ci)), ','.join(set(xing_rong_ci))

def NLPPos(sentence):
    NLPTokenizer = JClass("com.hankcs.hanlp.tokenizer.NLPTokenizer")
    ans = NLPTokenizer.segment(sentence)
    # print(NLPTokenizer.analyze("情节恶劣").translateLabels())
    # print(NLPTokenizer.analyze("情节恶劣"))
    dong_ci = []
    xing_rong_ci = []
    for word in list(ans):
        if str(word.nature) in ['v', 'vd', 'vn']:
            dong_ci.append(str(word.word))
        if str(word.nature).__contains__('a'):
            xing_rong_ci.append(str(word.word))

    return ','.join(set(dong_ci)), ','.join(set(xing_rong_ci))

def shortPos(sentence):

    NshortNewSegment=HanLP.newSegment('nshort')
    ans=NshortNewSegment.seg(sentence)

    dong_ci = []
    xing_rong_ci = []
    for word in list(ans):
        if str(word.nature) in ['v', 'vd', 'vn']:
            dong_ci.append(str(word.word))
        if str(word.nature).__contains__('a'):
            xing_rong_ci.append(str(word.word))

    return ','.join(set(dong_ci)), ','.join(set(xing_rong_ci))

def save_dic_as_json(path,fileName, dic):
    # 先将字典对象转化为可写入文本的字符串
    dic = json.dumps(dic,ensure_ascii=False)
    dic=json.loads(dic)
    my_IO.mkDir(path)
    try:
        with open(fileName, "w", encoding='utf-8') as f:
            json.dump(dic, f, indent=4, ensure_ascii=False)
    except Exception as e:
        print("write error==>", e)
    finally:
        pass


def saveTag():
    outRootPath='./词性标注'
    if not os.path.exists(outRootPath):
        os.mkdir(outRootPath)
    for type,member in CaseType.CaseType.__members__.items():
        originDir='../project-dev/judicature/src/main/resources/case/txt/{}/'.format(type) # 直接读取原文书，动词效果不好，形容词好
        tfidfDir='./tfidffile/{}/'.format(type) # 读取关键词文书，动词效果好，形容词不好
        files=os.listdir(originDir) # 文件名没有区别，两个目录一样

        for file in files:
            dic = {'fileName':file}
            dongCi,xingRongCi=hanlpPOS(readfile(tfidfDir + file))
            dic['动词'] = dongCi
            dongCi, xingRongCi = shortPos(my_IO.readFile(originDir + file))
            dic['形容词']=xingRongCi
            outFileName='{}/{}/{}'.format(outRootPath,type,file).replace('txt','json')
            outPath='{}/{}'.format(outRootPath,type)
            save_dic_as_json(outPath,outFileName,dic)
    print('分词词性标注完成')

def sigleFilePos(userCasePath):

    fileContent=my_IO.readFile(userCasePath)
    dic = {'fileName': userCasePath}
    dongCi, xingRongCi = hanlpPOS(fileContent)
    dic['动词'] = dongCi
    dic['形容词'] = xingRongCi

    outPath = '词性标注/用户上传'
    outFileName=my_IO.getFileName(userCasePath)
    outFileName = '{}/{}'.format(outPath,outFileName)
    save_dic_as_json(outPath, outFileName, dic)


if __name__=='__main__':
    # 下面是给本地已有的百份文档进行标注的函数
    # saveTag()

    # 根据用户上传内容，进行词性标注
    userCasePath=input()
    #userCasePath='../project-dev/judicature/src/main/resources/case/txt/adjudication/中欧汽车电器有限公司吴国琳等合伙协议纠纷股权转让纠纷其他民事民事裁定书.txt'
    sigleFilePos(userCasePath)