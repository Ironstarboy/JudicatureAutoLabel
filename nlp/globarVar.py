# 跨文件全局变量
global _global_dict
_global_dict = {'overWrite':0
                }


def set(key, value):
    _global_dict[key] = value


def get(key):
    return _global_dict.get(key,None)

def get_items():
    return _global_dict.items()