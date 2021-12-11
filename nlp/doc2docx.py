import sys
import pickle
import re
import codecs
import string
import shutil
from win32com import client as wc
import docx
import os
def getfiles(kind:str):
    # kind .docx .doc
    # 到时候的目录结构应该是按照日期分类
    path= 'source\\202112070223562112071NY9FG24BC'
    files=os.listdir('../'+path)
    for file in files:
        if os.path.splitext(file)[1]==kind:
            current_path = os.path.abspath(__file__)
            father_path = os.path.abspath(os.path.dirname(current_path) + os.path.sep + ".")
            grader_father = os.path.abspath(os.path.dirname(current_path) + os.path.sep + "..")

            if not os.path.isdir(file): #判断是否是文件
                yield os.path.basename(file),r'{}\{}\{}'.format(grader_father,path,os.path.basename(file)) # 文件绝对路径

# doc转docx
def doSaveAas(src_file):
    outfile = '{}x'.format(src_file)
    if not os.path.exists(outfile):
        word = wc.Dispatch('Word.Application')
        doc = word.Documents.Open(src_file)
         # 只能使用完整绝对地址，相对地址找不到文件，且，只能用“\\”，不能用“/”，哪怕加了 r 也不行，涉及到将反斜杠看成转义字符。
        doc.SaveAs(outfile, 12, False, "", True, "", False, False, False, False)  # 转化后路径下的文件
        doc.Close()
        word.Quit()



for i in getfiles('.doc'):
    doSaveAas(i[1])





