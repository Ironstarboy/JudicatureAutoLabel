import os
import jieba
import jieba.posseg as pseg
import sys
import string
from sklearn import feature_extraction
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.feature_extraction.text import CountVectorizer

# 获取文件列表（该目录下放着100份文档）
def getFilelist(path):
    filelist = []
    files = os.listdir(path)
    for f in files:
        if (f[0] == '.'):
            pass
        else:
            filelist.append(f)
    return filelist, path


# 对文档进行分词处理
def fenci(fname, path):
    # 保存分词结果的目录
    sFilePath = './segfile'
    if not os.path.exists(sFilePath):
        os.mkdir(sFilePath)
    stop_words = [w.strip() for w in open('哈工大停用词表.txt', encoding='utf-8').readlines()]
    law_stop=['有限公司','有限','公司','合同','诉讼','被告','最高人民法院','法院','人民法院','最高']
    stop_words=stop_words+law_stop
    # 读取文档
    filename = fname
    try:
        f = open(path + filename, 'r+',encoding='utf-8')
        file_list = f.read()
        f.close()
    except Exception as e:
        print(e)
        print(filename)


    # 对文档进行分词处理，采用默认模式
    seg_list = jieba.cut(file_list, cut_all=True)

    # 对空格，换行符 停用词 进行处理
    result = []
    for seg in seg_list:
        seg = ''.join(seg.split())
        if (seg != '' and seg != "\n" and seg != "\n\n" and seg not in stop_words):
            result.append(seg)

    # 将分词后的结果用空格隔开，保存至本地。比如"我来到北京清华大学"，分词结果写入为："我 来到 北京 清华大学"
    f = open(sFilePath + "/" + filename , "w+")
    f.write(' '.join(result))
    f.close()


# 读取100份已分词好的文档，进行TF-IDF计算
def Tfidf(filelist):
    path = './segfile/'
    corpus = []  # 存取100份文档的分词结果
    names=[]
    for ff in filelist:
        fname = path + ff
        names.append(ff)
        f = open(fname, 'r+')
        content = f.read()
        f.close()
        corpus.append(content)

    vectorizer = CountVectorizer()
    transformer = TfidfTransformer()
    tfidf = transformer.fit_transform(vectorizer.fit_transform(corpus))

    word = vectorizer.get_feature_names()  # 所有文本的关键字
    weight = tfidf.toarray()  # 对应的tfidf矩阵

    sFilePath = './tfidffile'
    if not os.path.exists(sFilePath):
        os.mkdir(sFilePath)
    # 这里将每份文档词语的TF-IDF写入tfidffile文件夹中保存
    for i in range(len(weight)):
        ans={}.fromkeys(word)
        f = open(sFilePath + '/' + names[i] + '.txt', 'w+',encoding='utf-8')
        for j in range(len(word)):

            ans[word[j]]=weight[i][j]
        out=sorted(ans.items(), key = lambda kv:(kv[1], kv[0]),reverse=True)[:66]
        # print(out)
        f.write(','.join([i[0] for i in out]))
        f.close()

if __name__ == "__main__":
    path='../project-dev\\judicature\\src\\main\\resources\\case\\txt\\mediate\\'
    (allfile, path) = getFilelist(path)
    for ff in allfile:
        fenci(ff, path)
    Tfidf(allfile)
