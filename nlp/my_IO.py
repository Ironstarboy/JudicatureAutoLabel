import os
import CaseType
def readFile(filename):
    content = ''
    encodings = ['utf-8', 'GBK', 'gb2312']
    index = 0
    while 1:
        if content != '' or index == len(encodings):
            break
        try:
            f = open(filename, 'r+', encoding=encodings[index])
            content = f.read()
            f.close()
            break
        except:
            pass
            # print('{} {}编码失败，正在尝试下一种编码'.format(filename,encodings[index]))
        finally:
            index += 1
    if content == '':
        print('{}读取失败'.format(filename))
    return content

def mkDir(dirPath):
    if not os.path.exists(dirPath):
        os.makedirs(dirPath) # 可以递归创建 ，即可以创建多层目录结构
    return dirPath

# 获取文件列表，该目录下放着一同个类别的文档,数量为几百份。不进行递归获取
def getFileNameList(path):
    filelist = []
    files = os.listdir(path)
    for f in files:
        if (f[0] == '.'):
            pass
        else:
            filelist.append(f)
    return filelist

# 可递归获取置顶目录下的文件路径
def recusiveGetFilePathList(dir):
    res=[]
    import os
    fileNames=[]
    for root, dirs, files in os.walk(dir):
        for file in files:
            res.append(os.path.join(root, file))
        fileNames.extend(files)
    return res,fileNames



def saveFile(file_path, content:str,mode='w'):
    with open(file_path,mode,encoding='utf-8') as f:
        f.write(content)

def getTypeNameList()->list:
    return [type for type, member in CaseType.CaseType.__members__.items()]

import pickle
def dumpVar(var,filePath):
    if not os.path.exists(filePath):
        with open(filePath,'wb') as f:
            pickle.dump(var, f)


def loadVar(filePath):
    with open(filePath,'rb') as f:
        res=pickle.load(f)
    return res