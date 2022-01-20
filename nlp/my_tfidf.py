import os
import jieba
import jieba.posseg as pseg
import sys
import string
import chardet
import CaseType
import my_IO
from sklearn import feature_extraction
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.feature_extraction.text import CountVectorizer


# 对一个类别的单一文档进行分词处理,分词结果存在segPath里面
def fenci(srcPath,srcFile, typePath):
    '''
    :param srcFile: 要读取的判断文书
    :param typePath: 文书六大类别目录
    :return:
    '''
    stop_words = [w.strip() for w in open('哈工大停用词表.txt', encoding='utf-8').readlines()]
    law_stop=['有限公司','有限','公司','诉讼','最高']
    stop_words=stop_words+law_stop

    filename = srcPath + '\\' + srcFile
    content=my_IO.readFile(filename)
    # 对文档进行分词处理，采用默认模式
    seg_list = jieba.cut(content, cut_all=True)

    # 对空格，换行符 停用词 进行处理
    result = []
    for seg in seg_list:
        seg = ''.join(seg.split())
        if (seg != '' and seg != "\n" and seg != "\n\n" and seg not in stop_words):
            result.append(seg)

    # 将分词后的结果用空格隔开，保存至本地。比如"我来到北京清华大学"，分词结果写入为："我 来到 北京 清华大学"
    my_IO.saveFile(typePath+'\\'+ srcFile,' '.join(result))




# 读取几百份份已分词好的文档，进行TF-IDF计算
def Tfidf(type,srcPath):
    corpus = []  # 存取100份文档的分词结果,稀疏矩阵
    fileList=my_IO.getFileNameList(srcPath)
    names=[]
    for fname in fileList:
        fpath=srcPath+'\\'+fname
        names.append(fname)
        content=my_IO.readFile(fpath)# TODO 没有过滤停用词
        corpus.append(content)

    vectorizer = CountVectorizer()
    transformer = TfidfTransformer()
    tfidf = transformer.fit_transform(vectorizer.fit_transform(corpus))

    word = vectorizer.get_feature_names()  # 所有文本的关键字
    weight = tfidf.toarray()  # 对应的tfidf矩阵

    sFilePath = './tfidffile/'+type
    if not os.path.exists(sFilePath):
        os.mkdir(sFilePath)
    # 这里将每份文档词语的TF-IDF写入tfidffile文件夹中保存
    for i in range(len(weight)):
        ans={}.fromkeys(word)
        f = open(sFilePath + '/' + names[i], 'w+',encoding='utf-8')
        for j in range(len(word)):
            ans[word[j]]=weight[i][j]
        out=sorted(ans.items(), key = lambda kv:(kv[1], kv[0]),reverse=True)[:66]
        # print(out)
        f.write(','.join([i[0] for i in out]))
        f.close()

# 对所有文档都分词一遍
def seg():
    for type,member in CaseType.CaseType.__members__.items():
        src_path = r'../project-dev/judicature/src/main/resources/case/txt/' + type
        # 保存分词结果的目录
        typePath = 'segfile' + '\\' + type
        if not os.path.exists(typePath):
            os.mkdir(typePath)
        allfile = my_IO.getFileNameList(src_path)
        for fname in allfile:
            fenci(src_path,fname, typePath)

if __name__ == "__main__":
    seg()
    for type,member in CaseType.CaseType.__members__.items():
        src_path = r'./segfile/' + type
        Tfidf(type,src_path)

