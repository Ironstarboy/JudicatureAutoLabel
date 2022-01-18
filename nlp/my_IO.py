import os
def readfile(filename):
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

def mkdir(dirPath):
    if not os.path.exists(dirPath):
        os.makedirs(dirPath)

# 获取文件列表，该目录下放着一同个类别的文档,数量为几百份。不进行递归获取
def getFilelist(path):
    filelist = []
    files = os.listdir(path)
    for f in files:
        if (f[0] == '.'):
            pass
        else:
            filelist.append(f)
    return filelist

def saveFile(file_path, content:str):
    with open(file_path,'w',encoding='utf-8') as f:
        f.write(content)