# https://blog.csdn.net/weixin_42695959/article/details/82903689
#相似文章推荐


#%%
import os;
import numpy;
import os.path;
import codecs;
import my_IO
#首先构建语料库
filePaths = []
fileContents = []
for root, dirs, files in os.walk(
    "../project-dev/judicature/src/main/resources/case/txt/judgment"
):
    for name in files:
        filePath = os.path.join(root, name)
        filePaths.append(filePath)
        fileContent = my_IO.readfile(filePath)
        fileContents.append(fileContent)

import pandas
corpos = pandas.DataFrame({
    'filePath': filePaths,
    'fileContent': fileContents
})


# 接着进行分词处理，提取中文词
import re

zhPattern = re.compile(u'[\u4e00-\u9fa5]+')

import jieba
#分词
segments = []
filePaths = []
for index, row in corpos.iterrows():
    segments = []
    filePath = row['filePath']
    fileContent = row['fileContent']
    segs = jieba.cut(fileContent)
    for seg in segs:
        if zhPattern.search(seg):
            segments.append(seg) #匹配中文分词
    filePaths.append(filePath)
    row['fileContent'] = " ".join(segments); #将分词间添加空格



# 过滤停用词，用sklearn包生成词频矩阵
from sklearn.feature_extraction.text import CountVectorizer
#导入停用词

#保留长度大于0的词，过滤停用词
countVectorizer = CountVectorizer(
    stop_words= [w.strip() for w in open('哈工大停用词表.txt', encoding='utf-8').readlines()],
    min_df=0, token_pattern=r"\b\w+\b"
)
#将分词结果转化成词频矩阵
textVector = countVectorizer.fit_transform(
    corpos['fileContent']
)
# 接着计算每篇文章之间的余弦相似度，用到的包是sklearn.metrics
#计算每行之间的余弦距离
from sklearn.metrics import pairwise_distances# 该方法用来计算余弦相似度
#余弦相似度计算得到每篇文章之间的余弦距离矩阵，数值越小代表越相似
distance_matrix = pairwise_distances(
    textVector,
    metric="cosine"
)#第一个参数为要计算的矩阵，第二个参数为计算公式，即余弦距离
#距离矩阵变成数据框
m = 1- pandas.DataFrame(distance_matrix)
m.columns = filePaths
m.index = filePaths
#对每行进行排序，取所有行中最相似的前五个
sort = numpy.argsort(distance_matrix, axis=1)[:,1:6]
#得到每篇文章最相似的五篇文章
similarity5 = pandas.Index(filePaths)[sort].values
#最后结果生成数据框格式
similarityDF = pandas.DataFrame({
    'filePath':corpos.filePath,
    's1': similarity5[:, 0],
    's2': similarity5[:, 1],
    's3': similarity5[:, 2],
    's4': similarity5[:, 3],
    's5': similarity5[:, 4]
})
similarityDF.to_excel('相似文章.xlsx',encoding='utf-8')


