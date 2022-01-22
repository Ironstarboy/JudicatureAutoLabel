import re

"""
将分词结果转换为区间
:param segmentation: 商品 和 服务
:return: [(0, 2), (2, 3), (3, 5)]
"""


def to_region(segmentation: str) -> list:
    region = []
    start = 0
    for word in re.compile("\\s+").split(segmentation.strip()):
        end = start + len(word)
        region.append((start, end))
        start = end
    return region


def prf(gold: str, pred: str, dic) -> tuple:
    """
     计算P、R、F1
     :param gold: 黄金标准答案文件，比如“商品 和 服务”
     :param pred: 分词结果文件，比如“商品 和服 务”
     :param dic: 词典
     :return: (P, R, F1, OOV_R, IV_R)
    """
    A_size, B_size, A_cap_B_size, OOV, IV, OOV_R, IV_R = 0, 0, 0, 0, 0, 0, 0
    A, B = set(to_region(gold)), set(to_region(pred))
    A_size += len(A)
    B_size += len(B)
    A_cap_B_size += len(A & B)
    text = re.sub("\\s+", "", gold)

    for (start, end) in A:
        word = text[start: end]
        if word in dic:
            IV += 1
        else:
            OOV += 1

    for (start, end) in A & B:
        word = text[start: end]
        if word in dic:
            IV_R += 1
        else:
            OOV_R += 1
    p, r = A_cap_B_size / B_size * 100, A_cap_B_size / A_size * 100
    return p, r, 2 * p * r / (p + r), OOV_R / OOV * 100, IV_R / IV * 100
def getDict(dicPath):
    return list(map(lambda x:x.split(' ')[0],my_IO.readFile(dicPath).split('\n')))

from my_package import my_IO
if __name__ == '__main__':
    dic = getDict('C:\Anaconda3\Lib\site-packages\jieba\dict.txt')
    gold=my_IO.readFile('source/gold.txt')
    # 黄金标准答案文件.
    # 可以换成可以使用SIGHAN（国际计算语言学会（ACL）中文语言处理小组）
    # 举办的国际中文语言处理竞赛Second International Chinese Word Segmentation Bakeoff
    # （ http://sighan.cs.uchicago.edu/bakeoff2005/）所提供的公开数据来评测，
    # 它包含了多个测试集以及对应的黄金标准分词结果。
    pred=my_IO.readFile('nlp/segfile/mediate/余小清浙江省义乌市人民政府再审行政调解书.txt') # 分词结果文件
    print("Precision:%.2f\nRecall:%.2f\nF1:%.2f\nOOV-R:%.2f\nIV-R:%.2f\n" % prf(gold, pred, dic))


