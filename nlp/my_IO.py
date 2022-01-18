def readfile(filename):
    content = ''
    encodings = ['utf-8', 'GBK', 'gb2312']
    index = -1
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