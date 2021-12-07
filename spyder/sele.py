from selenium import webdriver
import time
import selenium.webdriver.support.ui as ui

def login():
    driver=webdriver.Chrome()
    url='https://wenshu.court.gov.cn/website/wenshu/181010CARHS5BS3C/index.html?open=login'
    # url='https://account.court.gov.cn/app#/login'# F12中找到app authorize元素
    # driver.switch_to.frame('contentIframe')
    driver.get(url)
    time.sleep(1)
    driver.refresh()
    driver.refresh()# 刷新多次才会加载出来？
    time.sleep(2)

    driver.switch_to.frame(driver.find_element_by_id("contentIframe"))
    phone=driver.find_element_by_xpath('//*[@id="root"]/div/form/div/div[1]/div/div/div/input')

    pwd=driver.find_element_by_xpath('//*[@id="root"]/div/form/div/div[2]/div/div/div/input')
    # 登陆按钮
    log_button=driver.find_element_by_xpath('//*[@id="root"]/div/form/div/div[3]/span')
    # TODO 登陆网页会跳出验证码验证
    phone.send_keys('18851751056')
    # time.sleep(1)
    pwd.send_keys('wenshuCourt666')
    # time.sleep(1)
    log_button.click()
    time.sleep(2) # 点击登陆之后，需要加载一会才能进度。不Sleep下面一刷新就没了
    # 登陆进去之后仍然需要多次刷新，才会显示账号

    driver.refresh();driver.refresh();driver.refresh()
    # TODO 这一步会跳出验证码验证
    c=driver.get_cookies()
    print(c)
    print()

"""cookie:
[{'domain': 'wenshu.court.gov.cn', 'expiry': 1637829083, 'httpOnly': True, 'name': 'wzws_reurl', 'path': '/', 'secure': False, 'value': 'L2NvbW1vbi9zdGF0aWMvc2NyaXB0cy9sYXd5ZWV1aS93ZWJzaXRlLmpzP3Y9MTU2NTY3NDYwMjYzNw=='}, {'domain': '.court.gov.cn', 'expiry': 1653553576, 'httpOnly': False, 'name': 'UM_distinctid', 'path': '/', 'secure': False, 'value': '17d5633d0eff7-08f4db7643f52-978183a-190140-17d5633d0f09da'}, {'domain': 'wenshu.court.gov.cn', 'httpOnly': True, 'name': 'SESSION', 'path': '/', 'secure': False, 'value': '190446d9-f448-4813-8d86-0ca09dd0b8b6'}]
"""
login()



