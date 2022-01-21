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

## 基于余弦相似度

运行caseRecommedation.py，读取所有文章作为语料库，输出''相似文章.xlsx"，表格单元存着文书路径

## 基于词频矩阵

百篇文章的处理结果已经存在 相似文章得分.xlsx 相似文章路径.xlsx里面了

- 相似文章路径.xlsx：
  - 第一列是当前文章路径，第二列是一个列表，里面有6个相似文章，其中第一个是自己
- 相似文章得分
  - 第一列是当前文章路径，第二列是一个列表，里面有6个得分，和相似文章路径的列表一一对应
- 用户上传文书进行相似案例推荐
  - 运行getCaseRecommendation.py的caseReccommend函数，用例在代码里给出了

# 实时搜索建议

需要segfile里面有文件，即mt_tfidf已经运行

searchRecommend.py