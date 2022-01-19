# 关键词提取 词性标注

源文书目录是project-dev/judicature/src/main/resources/case/txt

先运行 my_tfidf.py，nlp目录下生成segfile和tfidfile目录。文件格式txt

再运行POStag.py，nlp目录下生成词性标注目录，格式是json

# 自动摘要

## 基于textRank

autoAbstract文件，不分类别读取所有文章，直接运行，摘要目录下按类别生成摘要文件

## 启发式算法

使用关键词信息量、句子位置、句子相似度三个参数来构建一个句子权重的函数

运行summarization.py生成，summarise下按类别输出摘要

# 文章推荐

基于余弦相似度。

运行caseRecommedation.py，读取所有文章作为语料库，输出''相似文章.xlsx"，表格单元存着文书路径