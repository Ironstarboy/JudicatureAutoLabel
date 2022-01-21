'''
搜索内容实时推荐 文书标题
其实也能根据文书内容推荐，看下计算速度吧
'''
import time
from functools import wraps
def func_timer(function):
    '''
    用装饰器实现函数计时
    :param function: 需要计时的函数
    :return: None
    '''
    @wraps(function)
    def function_timer(*args, **kwargs):
        print('[Function: {name} start...]'.format(name = function.__name__))
        t0 = time.time()
        result = function(*args, **kwargs)
        t1 = time.time()
        print('[Function: {name} finished, spent time: {time:.2f}s]'.format(name = function.__name__,time = t1 - t0))
        return result
    return function_timer

# 对单一文书，进行分词，并去除停用词
# 在my_tfidf里，用fenci() 和seg()已经存储好分好词的文档了
import jieba
def tokenization(content):
    stopWords=[[w.strip() for w in open('哈工大停用词表.txt', encoding='utf-8').readlines()]]
    seg_list = jieba.cut(content, cut_all=True)

    # 对空格，换行符 停用词 进行处理
    result = []
    for seg in seg_list:
        seg = ''.join(seg.split())
        if (seg != '' and seg != "\n" and seg != "\n\n" and seg not in stopWords):
            result.append(seg)
    return result

# 读取所有分词后的文件内容或者文件名，生成文本集
from my_package import my_IO
from varSavePath import Vars
import os
from gensim import corpora, models, similarities

rootPath='searchRec'
my_IO.mkDir(rootPath)
def saveFilePath(rootPath,fatherDir='segfile'):
    outFilePath=rootPath+'/'+Vars.filePath.value
    if not os.path.exists(outFilePath):
        filePaths, fileNames = my_IO.recusiveGetFilePathList(fatherDir)
        filePath:list=[filePath for filePath in filePaths]
        my_IO.dumpVar(fileNames, outFilePath)

# @func_timer
def loadFilePath(rootPath='searchRec'):
    filePath=rootPath+'/'+Vars.filePath.value
    if not os.path.exists(filePath):
        saveFilePath(rootPath)
    return my_IO.loadVar(filePath)

import globarVar
def saveSegContent(rootPath='searchRec',fatherDir='segfile'):
    # 设计的时候没考虑到这个函数会重写，所以没写成对象
    '''
    fileContent 可以是文件名，也可以是文件内容,就看需要什么作为语料库了
    :param fatherDir:
    :param contentPath:
    :return:
    '''
    contentPath=rootPath+'/'+Vars.fileContent.value
    if not os.path.exists(contentPath):
        filePaths, fileNames = my_IO.recusiveGetFilePathList(fatherDir)
        a=globarVar.get('overWrite')
        if a:
            fileContent:list = [my_IO.readFile(filePath) for filePath in filePaths] # 文书内容,已经分词且去除停用词语了 不分类别全部作为语料
        # fileCleanName=[' '.join(tokenization(i.replace('.txt',''))) for i in fileNames] # 文件名
        # fileContent=list(map(lambda x,y:x+y,fileContent,fileCleanName))
        else:
            fileContent=[i.replace('.txt','') for i in fileNames]
        my_IO.dumpVar(fileContent, contentPath)



# @func_timer
def loadContent(rootPath='searchRec'):
    contentPath = rootPath + '/' + Vars.fileContent.value
    if not os.path.exists(contentPath):
        saveSegContent(rootPath)
    return my_IO.loadVar(contentPath)


def saveCorpus(rootPath='searchRec'):
    corpusPath = rootPath + '/' + Vars.corpus.value
    content=loadContent(rootPath)
    if not os.path.exists(corpusPath):
        # 不存在就写
        corpus = [tokenization(name) for name in content]

        my_IO.dumpVar(corpus, corpusPath)

# @func_timer
def loadCorpus(rootPath='searchRec'):
    corpusPath = rootPath + '/' + Vars.corpus.value
    if not os.path.exists(corpusPath):
        saveCorpus(rootPath)
    return my_IO.loadVar(corpusPath)

def saveDictionary(rootPath='searchRec',corpus=loadCorpus(rootPath)):
    dicPath = rootPath+'/'+Vars.dic.value
    corpus=loadCorpus(rootPath)
    if not os.path.exists(dicPath):
        dictionary = corpora.Dictionary(corpus)
        my_IO.dumpVar(dictionary, dicPath)

# @func_timer
def loadDictionary(rootPath='searchRec'):
    dicPath=rootPath+'/'+Vars.dic.value
    if not os.path.exists(dicPath):
        saveDictionary(rootPath)
    return my_IO.loadVar(dicPath)

def saveDocVec(rootPath='searchRec'):
    docVecPath =rootPath +'/'+Vars.docVectors.value
    dictionary=loadDictionary(rootPath)
    corpus = loadCorpus(rootPath)
    # 提取词典特征数
    feature_cnt = len(dictionary.token2id)
    # 基于语料库，生成词向量
    if not os.path.exists(docVecPath):
        doc_vectors = [dictionary.doc2bow(text) for text in corpus]
        my_IO.dumpVar(doc_vectors, docVecPath)

# @func_timer
def loadDocVec(rootPath='searchRec'):
    docVecPath=rootPath+'/'+Vars.docVectors.value
    if not os.path.exists(docVecPath):
        saveDocVec(rootPath)
    return my_IO.loadVar(docVecPath)

# @func_timer
def textSet(keyword,rootPath='searchRec'):
    doc_vectors = loadDocVec(rootPath)
    dictionary = loadDictionary(rootPath)
    kw_vector = dictionary.doc2bow(jieba.lcut(keyword))
    feature_cnt = len(dictionary.token2id)
    fileNames=loadFilePath(rootPath)
    # 用向量形式的语料库训练tf-idf
    tfidf = models.TfidfModel(doc_vectors)  # TF-IDF模型对语料库建模
    # 相似度计算
    index=similarities.SparseMatrixSimilarity(tfidf[doc_vectors],feature_cnt)
    # print('\nTF-IDF模型的稀疏向量集：')
    # for i in tfidf[doc_vectors]:
    #     print(i)
    # print('\nTF-IDF模型的keyword稀疏向量：')
    # print(tfidf[kw_vector])
    # print('\n相似度计算：')
    sim = index[tfidf[kw_vector]] # 很多0的稀疏矩阵
    fileSimDic:dict=dict(zip(fileNames,sim))
    fileSimDic=sorted(fileSimDic.items(),key=lambda x:x[1],reverse=True) # 从大到小排
    count=0
    caseRecommendReslut=[]
    for k,v in fileSimDic:
        if v>0:
            caseRecommendReslut.append(k)
            # print(k,v)
            count+=1
        if count==5:
            break
    return caseRecommendReslut

import sys
if __name__=='__main__':
    res=textSet('公司')
    print(res)
    print('done')
