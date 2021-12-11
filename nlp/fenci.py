from jieba import analyse
import docx
from nlp import doc2docx
import jieba.posseg

name,file=list(doc2docx.getfiles('.docx'))[0]

def readdocx(f):
    file=docx.Document(f)
    out=[]
    for para in file.paragraphs:
        out.append(para.text)
    return out

def get_keyword():
    tfidf = analyse.extract_tags
    text=''.join(readdocx(file))
    # 基于TF-IDF算法进行关键词抽取
    keywords = tfidf(text)
    return keywords


def dosegment_all(sentence):
    sentence_seged = jieba.posseg.cut(sentence.strip())
    dong_ci = []
    xing_rong_ci = []

    for x in sentence_seged:
        if x.flag in ['v','vd','vn']:
            dong_ci.append(x.word)
        if x.flag in ['a','an','ad']:
            xing_rong_ci.append(x.word)

    return set(dong_ci),set(xing_rong_ci)

print(name)
res=dosegment_all(''.join(readdocx(file)))
print(res[0])
print(res[1])

