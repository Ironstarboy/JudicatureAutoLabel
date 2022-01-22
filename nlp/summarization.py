# https://zhuanlan.zhihu.com/p/30603995
import jieba
import numpy as np
import collections
from sklearn import feature_extraction
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.feature_extraction.text import CountVectorizer
import my_IO
 # sigmoid 效果不行呐
def split_sentence(text, punctuation_list='!?。！？；;\n'):
    """
    将文本段安装标点符号列表里的符号切分成句子，将所有句子保存在列表里。
    """
    sentence_set = []
    inx_position = 0  # 索引标点符号的位置
    char_position = 0  # 移动字符指针位置
    for char in text:
        char_position += 1
        if char in punctuation_list:
            next_char = list(text[inx_position:char_position + 1]).pop()
            if next_char not in punctuation_list:
                sentence_set.append(text[inx_position:char_position])
                inx_position = char_position
    if inx_position < len(text):
        sentence_set.append(text[inx_position:])

    sentence_with_index = {i: sent for i, sent in
                           enumerate(sentence_set)}  # dict(zip(sentence_set, range(len(sentences))))
    return sentence_set, sentence_with_index


def get_tfidf_matrix(sentence_set, stop_word):
    corpus = []
    for sent in sentence_set:
        sent_cut = jieba.cut(sent)
        sent_list = [word for word in sent_cut if word not in stop_word]
        sent_str = ' '.join(sent_list)
        corpus.append(sent_str)

    vectorizer = CountVectorizer()
    transformer = TfidfTransformer()
    tfidf = transformer.fit_transform(vectorizer.fit_transform(corpus))
    # word=vectorizer.get_feature_names()
    tfidf_matrix = tfidf.toarray()
    return np.array(tfidf_matrix)


def get_sentence_with_words_weight(tfidf_matrix):
    sentence_with_words_weight = {}
    for i in range(len(tfidf_matrix)):
        sentence_with_words_weight[i] = np.sum(tfidf_matrix[i])

    max_weight = max(sentence_with_words_weight.values())  # 归一化
    min_weight = min(sentence_with_words_weight.values())
    for key in sentence_with_words_weight.keys():
        x = sentence_with_words_weight[key]
        # sentence_with_words_weight[key]=expit(sentence_with_words_weight[key])
        if max_weight - min_weight!=0:
            sentence_with_words_weight[key] = (x - min_weight) / (max_weight - min_weight)
        else:
            sentence_with_words_weight[key]=0 # todo 0比0怎么处理

    return sentence_with_words_weight


def get_sentence_with_position_weight(sentence_set):
    sentence_with_position_weight = {}
    total_sent = len(sentence_set)
    for i in range(total_sent):
        sentence_with_position_weight[i] = (total_sent - i) / total_sent
    return sentence_with_position_weight


def similarity(sent1, sent2):
    """
    计算余弦相似度
    """
    return np.sum(sent1 * sent2) / 1e-6 + (np.sqrt(np.sum(sent1 * sent1)) * \
                                           np.sqrt(np.sum(sent2 * sent2)))


def get_similarity_weight(tfidf_matrix):
    sentence_score = collections.defaultdict(lambda: 0.)
    for i in range(len(tfidf_matrix)):
        score_i = 0.
        for j in range(len(tfidf_matrix)):
            score_i += similarity(tfidf_matrix[i], tfidf_matrix[j])
        sentence_score[i] = score_i

    max_score = max(sentence_score.values())  # 归一化
    min_score = min(sentence_score.values())
    for key in sentence_score.keys():
        x = sentence_score[key]
        # sentence_score[key]=expit(sentence_score[key])
        if max_score - min_score!=0:
            sentence_score[key] = (x - min_score) / (max_score - min_score)
        else:
            sentence_score[key]=0


    return sentence_score


def ranking_base_on_weigth(sentence_with_words_weight,
                           sentence_with_position_weight,
                           sentence_score, feature_weight=[1, 1, 1]):
    sentence_weight = collections.defaultdict(lambda: 0.)
    for sent in sentence_score.keys():
        sentence_weight[sent] = feature_weight[0] * sentence_with_words_weight[sent] + \
                                feature_weight[1] * sentence_with_position_weight[sent] + \
                                feature_weight[2] * sentence_score[sent]

    sort_sent_weight = sorted(sentence_weight.items(), key=lambda d: d[1], reverse=True)
    return sort_sent_weight


def get_summarization(sentence_with_index, sort_sent_weight, topK_ratio=0.3):
    topK = int(len(sort_sent_weight) * topK_ratio)
    summarization_sent = sorted([sent[0] for sent in sort_sent_weight[:topK]])

    summarization = []
    for i in summarization_sent:
        summarization.append(sentence_with_index[i])

    summary = '\n'.join(summarization)
    return summary


def summarize():

    typeRatio={
        'adjudication': 0.07,
        'decision':0.3,
        'judgment':0.07,
        'mediate':0.1,
        'notification':0.2,
        'order':0.1
    }
    for type in my_IO.getTypeNameList():
        filepath = '../project-dev/judicature/src/main/resources/case/txt/{}/'.format(type)
        fileList:list=my_IO.getFileNameList(filepath)
        for fileName in fileList:
            text = my_IO.readFile(filepath+'/'+fileName)
            stop_word = []
            stop_word = [w.strip() for w in open('哈工大停用词表.txt', encoding='utf-8').readlines()]

            sentence_set, sentence_with_index = split_sentence(text)
            tfidf_matrix = get_tfidf_matrix(sentence_set, stop_word)
            sentence_with_words_weight = get_sentence_with_words_weight(tfidf_matrix)
            sentence_with_position_weight = get_sentence_with_position_weight(sentence_set)
            sentence_score = get_similarity_weight(tfidf_matrix)
            sort_sent_weight = ranking_base_on_weigth(sentence_with_words_weight,
                                                      sentence_with_position_weight,
                                                      sentence_score, feature_weight=[1, 1, 1])
            summarization = get_summarization(sentence_with_index, sort_sent_weight, topK_ratio=typeRatio[type])
            '''
            理论应该根据文本长度 选择比例
            order 0.1效果良好
            notification 0.2
            mediate 0.1
            judgment 0.07
            decision 0.3
            adjudication 0.07
            '''
            fileDir=my_IO.mkDir('summarise/{}'.format(type))
            fp=fileDir+'/'+fileName
            my_IO.saveFile(fp,summarization)
        print('{} done'.format(type))


if __name__ == '__main__':
    summarize()
    print('{} done'.format(__file__))

