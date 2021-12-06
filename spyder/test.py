import execjs

with open(r'strToBinary.js', 'r', encoding='utf-8') as f:
    js = f.read()
ct = execjs.compile(js, cwd=r'D:\nodejs\node_global\node_modules')
result = ct.call('cipher')
print(result)
