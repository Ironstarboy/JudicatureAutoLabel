# import execjs
#
# with open(r'strToBinary.js', 'r', encoding='utf-8') as f:
#     js = f.read()
# ct = execjs.compile(js, cwd=r'D:\nodejs\node_global\node_modules')
# result = ct.call('cipher')
# print(result)

import random
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
proxy_arr = [
     '--proxy-server=http://171.35.141.103:9999',
     '--proxy-server=http://36.248.132.196:9999',
     '--proxy-server=http://219.239.142.253:3128',
     '--proxy-server=http://119.57.156.90:53281',
     '--proxy-server=http://60.205.132.71:80',
     '--proxy-server=https://139.217.110.76:3128',
     '--proxy-server=https://116.196.85.150:3128'
 ]

chrome_options = Options()
proxy = random.choice(proxy_arr)  # 随机选择一个代理
print(proxy) #如果某个代理访问失败,可从proxy_arr中去除
chrome_options.add_argument(proxy)  # 添加代理
browser = webdriver.Chrome(options=chrome_options)

browser.get("http://httpbin.org/ip")

print(browser.page_source)

