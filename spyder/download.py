import requests
import execjs
from selenium import webdriver
from time import sleep
import random
import threading

# 这个方法可行
# TODO ip代理，ip池

class wenshu:

    def __init__(self):
        self.headers = {
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) '
                          'Chrome/89.0.4389.128 Safari/537.36 ',
            'Host': 'wenshu.court.gov.cn',
            'Origin': 'https://wenshu.court.gov.cn',
            'sec-ch-ua': 'Google Chrome";v="89", "Chromium";v="89", ";Not A Brand";v="99',
            'Referer': 'https://wenshu.court.gov.cn/website/wenshu/181217BMTKHNT2W0/index.html?pageId=27cc21'
                       '6a1f2f1f52fb1f145752d59ee2&s21=%E7%BB%8F%E6%B5%8E%E7%8A%AF%E7%BD%AA',
            'Accept': '*/*',
            'Accept-Encoding': 'gzip, deflate, br',
            'Accept-Language': 'zh-CN,zh;q=0.9',
            'Connection': 'keep-alive',
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
            'Cookie': ''
        }
        self.url = 'https://wenshu.court.gov.cn/website/parse/rest.q4w'
        self.request = requests.session()
        # 初始化execjs
        node = execjs.get()
        # 加载需要执行的js文件
        # self.ctx = node.compile(open('cipher.js', encoding='utf-8').read())
        chrome_options = webdriver.ChromeOptions()
        # 让浏览器不显示自动化测试
        chrome_options.add_argument('disable-infobars')
        self.chrome = webdriver.Chrome( options=chrome_options)

    def downpage(self):
        print('开始下载了')
        AllSelect = self.chrome.find_element_by_xpath(
            '//*[@id="_view_1545184311000"]/div[2]/div[4]/a[1]/label')  # 点击全选
        Alldownload = self.chrome.find_element_by_xpath('//*[@id="_view_1545184311000"]/div[2]/div[4]/a[3]')  # 批量下载
        sleep(5)
        AllSelect.click()
        print('点中全选了')
        self.chrome.implicitly_wait(10)
        Alldownload.click()

    def next_page(self):
        next_butten = self.chrome.find_element_by_xpath('//*[@id="_view_1545184311000"]/div[18]/a[9]').click()

    def send_login(self):
        # 模拟登陆获取到登陆的Cookie
        url='https://wenshu.court.gov.cn/website/wenshu/181010CARHS5BS3C/index.html?open=login'
        # url='https://account.court.gov.cn/app#/login'
        self.chrome.get(url)
        self.chrome.implicitly_wait(5)
        # 最大化浏览器,防止被遮挡？
        self.chrome.maximize_window()
        # 因为登录框在iframe框中，需要先切换到iframe中
        sleep(1.5)
        self.chrome.refresh()
        sleep(1)
        self.chrome.refresh()  # 刷新多次才会加载出来？
        sleep(2)
        self.chrome.switch_to.frame('contentIframe')
        self.chrome.find_element_by_xpath('//*[@id="root"]/div/form/div[1]/div[1]/div/div/div/input').send_keys('18851751056')
        sleep(2.5)
        self.chrome.find_element_by_xpath('//*[@id="root"]/div/form/div[1]/div[2]/div/div/div/input').send_keys('wenshuCourt666')
        sleep(random.randint(1, 3))
        self.chrome.find_element_by_xpath('//*[@id="root"]/div/form/div/div[3]/span').click() # 登陆按钮
        sleep(2)
        # TODO 偶尔会跳出验证码，验证码自动识别可以先从盟课网试试，或者试试ip代理
        self.chrome.refresh();self.chrome.refresh();
        xpath_search_base = '//div[@class="header"]/div[@class="item_table"]//div[@class="search-con clearfix"]/div[1]'
        self.chrome.find_element_by_xpath(xpath_search_base + '/div[2]/input').send_keys('经济犯罪')
        sleep(random.randint(1, 5))
        self.chrome.find_element_by_xpath(xpath_search_base + '/div[3]').click()
        sleep(random.randint(1, 5))
        self.chrome.refresh()
        self.chrome.implicitly_wait(10)
        # 跳出验证码
        self.downpage()
        # 翻页
        # i=0
        # while(i<5):
        #     sleep(5)
        #     self.next_page()
        #     i+=1
        return self.chrome.get_cookies()



    def send_request(self, ws_params):
        # TODO 不能定位
        return self.request.post(url=self.url, headers=self.headers, data=ws_params).json()

    def decrypt_response(self, ws_content):
        # 解密数据接口返回的加密内容
        # 解密的key
        secret_key = ws_content['secretKey']
        # 加密的数据内容
        result = ws_content['result']
        # 需要执行的方法名，第一个参数加密内容，第二个参数key
        func_name = 'DES3.decrypt("{0}", "{1}")'.format(result, secret_key)
        # 获取解密后的数据内容
        # 此处在windows下执行有报编码错误问题，需要将源码下的subprocess.py文件里的encoding改成utf-8
        return self.ctx.eval(func_name)

import js2py
def get_js_ans():
    # TODO jquery这里还有问题
    context = js2py.EvalJs()
    c = open('strToBinary.js', encoding='utf-8').read()
    context.execute(c)
    return context.strToBinary()
if __name__ == '__main__':


    wenshu = wenshu()
    # 获取登陆后的Cookie
    cookies = wenshu.send_login()
    sleep(11111)
    # TODO 下面代码有问题哦，js救救

    # 将cookie转换为字符串
    json_cookie = ''
    for cookie in cookies:
        name = cookie['name']
        value = cookie['value']
        json_cookie += name+'='+value+'; '
    # 退出selenium浏览器自动化
    # wenshu.chrome.quit()
    print(json_cookie)
    wenshu.headers['Cookie'] = json_cookie
    # 通过加载js生成ciphertext参数
    ciphertext =get_js_ans() # TODO python运行js还有点问题

    # ciphertext=
    # 通过加载js生成__RequestVerificationToken参数
    verification_token = wenshu.ctx.eval("random(24)")
    params = {
        'pageId': '2f48cc03c6e38865bcbdaf2e81a09f7d',# 参数由什么决定？
        's21': '经济犯罪',
        'sortFields': 's50:desc',
        'ciphertext': ciphertext, # cipher的js生成函数在网页里面找 website/wenshu/js/strToBinary.js
        'pageNum': '2',#参数是？
        'queryCondition': '[{"key":"s21","value":"经济犯罪"}]',
        'cfg': 'com.lawyee.judge.dc.parse.dto.SearchDataDsoDTO@queryDoc', #真正的接口参数
        '__RequestVerificationToken': verification_token
    }
    # 发送请求获取返回结果
    response = wenshu.send_request(params)
    print(response)
    sleep(999)
    # 获取解密后的内容
    content = wenshu.decrypt_response(response)
    print(content)