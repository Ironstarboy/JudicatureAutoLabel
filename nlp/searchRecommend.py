'''
搜索内容实时推荐 文书标题
其实也能根据文书内容推荐，看下计算速度吧
'''

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
import my_IO
import os
from gensim import corpora, models, similarities


def saveRawContent(fatherDir='segfile', varPath='caseNameContentDic.pkl'):
    if not os.path.exists(varPath):
        filePaths, fileNames = my_IO.recusiveGetFilePathList(fatherDir)
        fileName:list = [i.replace('.txt', '') for i in fileNames]
        rawCotent:list=[filePath for filePath in filePaths]  # 可改成按照文章内容的的list
        my_IO.dumpVar(rawCotent,varPath)


def loadRawText(varPath='caseNameContentDic.pkl'):
    if not os.path.exists(varPath):
        saveRawContent()
    return my_IO.loadVar(varPath)


def saveCorpus(varPath='corpus.pkl'):
    rawContent=loadRawText()
    if not os.path.exists(varPath):
        # 不存在就写
        corpus = [tokenization(name) for name in rawContent]
        my_IO.dumpVar(corpus,varPath)

def loadCorpus(varPath='corpus.pkl'):
    if not os.path.exists(varPath):
        saveCorpus(varPath)
    return my_IO.loadVar(varPath)

def saveDictionary(varPath='dictionary.pkl'):
    corpus=loadCorpus()
    if not os.path.exists(varPath):
        dictionary = corpora.Dictionary(corpus)
        my_IO.dumpVar(dictionary,varPath)

def loadDictionary(varPath='dictionary.pkl'):
    if not os.path.exists(varPath):
        saveDictionary(varPath)
    return my_IO.loadVar(varPath)

def saveDocVec(varPath='doc_vectors.pkl'):
    dictionary=loadDictionary()
    corpus = loadCorpus()
    # 提取词典特征数
    feature_cnt = len(dictionary.token2id)
    # 基于语料库，生成词向量
    if not os.path.exists(varPath):
        doc_vectors = [dictionary.doc2bow(text) for text in corpus]
        my_IO.dumpVar(doc_vectors,varPath)

def loadDocVec(varPath='doc_vectors.pkl'):
    if not os.path.exists(varPath):
        saveDocVec(varPath)
    return my_IO.loadVar(varPath)

def textSet(keyword):
    doc_vectors = loadDocVec()
    dictionary = loadDictionary()
    # corpus = loadCorpus()
    kw_vector = dictionary.doc2bow(jieba.lcut(keyword))
    feature_cnt = len(dictionary.token2id)
    fileNames=loadRawText()
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
    for k,v in fileSimDic:
        if v>0:
            print(k,v)
            count+=1
        if count==5:
            break
while 1:
    s = input()
    if s=='\n':
        break

    textSet(s)
