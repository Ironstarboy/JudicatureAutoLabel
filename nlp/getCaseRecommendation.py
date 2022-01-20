from my_package import my_IO
from varSavePath import Vars
import os
import searchRecommend as sr
import jieba
from gensim import corpora, models, similarities
import globarVar

globarVar.set('overWrite',1)

def caseReccommend(casePath,rootPath):
    content = my_IO.readFile(casePath)

    doc_vectors = sr.loadDocVec(rootPath)
    dictionary = sr.loadDictionary(rootPath)
    kw_vector = dictionary.doc2bow(filter(
        lambda x:x not in [w.strip() for w in open('哈工大停用词表.txt', encoding='utf-8').readlines()],
        jieba.lcut(content)))
    feature_cnt = len(dictionary.token2id)
    fileNames = sr.loadFilePath(rootPath)
    tfidf = models.TfidfModel(doc_vectors)
    index = similarities.SparseMatrixSimilarity(tfidf[doc_vectors], feature_cnt)
    sim = index[tfidf[kw_vector]]  # 很多0的稀疏矩阵
    fileSimDic: dict = dict(zip(fileNames, sim))
    fileSimDic = sorted(fileSimDic.items(), key=lambda x: x[1], reverse=True)  # 从大到小排
    count = 0
    caseRecommendFileName = []
    caseRecommendScore=[]
    for k, v in fileSimDic:
        if v > 0:
            caseRecommendFileName.append(k)
            caseRecommendScore.append(v)
            count += 1
        if count == 6: # 由于文书读进去必定推荐自己，所以推荐按五个要6个
            break
    return caseRecommendFileName,caseRecommendScore

import pandas as pd
from tqdm import tqdm
def saveRecommend():
    rootPath = 'caseRec'
    index_columns = ['currentFile', 'recommend1', 'recommend2', 'recommend3', 'recommend4', 'recommend5']
    currentFilePath=[]
    recFilePaths=[]
    recFileSocres=[]
    for type in tqdm(my_IO.getTypeNameList()):
        caseRootPath = '../project-dev/judicature/src/main/resources/case/txt/{}'.format(type)
        caseNames=my_IO.getFileNameList(caseRootPath)
        for caseName in tqdm(caseNames):
            recFileNameList,recFileSocreList = caseReccommend(caseRootPath+'/'+caseName, rootPath)
            currentFilePath.append(type+'/'+caseName)
            recFilePaths.append(list(map(lambda x:type+'/'+x,recFileNameList)))
            recFileSocres.append(recFileSocreList)
        print('{} done'.format(type))
    pathDic={
        'currentFilePath':currentFilePath,
        'recFilePaths':recFilePaths
    }
    dfPath=pd.DataFrame(pathDic)
    dfPath.to_excel('相似文章路径.xlsx')

    socreDic={
        'currentFilePath':currentFilePath ,
        'recFileScores': recFileSocres
    }
    dfScore=pd.DataFrame(socreDic)
    dfScore.to_excel('相似文章得分.xlsx')



def loadRecommend():
    pass
if __name__=='__main__':
    # 以下是生成本地已有文章的相似文章xlsx
    # 我已经生成好了，最好不要取消注释运行。如果运行了就等运行完，我在控制台做了进度输出
    # saveRecommend()
    # print('{} done'.format(__file__))

    # 输入当个文章，给出推荐文章路径列表和分数
    rootPath='caseRec'
    casePath='../project-dev/judicature/src/main/resources/case/txt/adjudication/中欧汽车电器有限公司吴国琳等合伙协议纠纷股权转让纠纷其他民事民事裁定书.txt'
    recFilename,recFileScore=caseReccommend(casePath, rootPath)
    print(recFilename,recFileScore)

