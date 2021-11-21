from selenium import webdriver
import time
import selenium.webdriver.support.ui as ui

def login():
    driver=webdriver.Chrome()
    url='https://wenshu.court.gov.cn/website/wenshu/181010CARHS5BS3C/index.html?open=login'
    url='https://account.court.gov.cn/app#/login'# F12中找到app authorize元素
    # driver.switch_to.frame('contentIframe')
    driver.get(url)
    time.sleep(1)
    # driver.refresh()
    # driver.refresh()# 刷新多次才会加载出来？
    # time.sleep(2)


    phone=driver.find_element_by_xpath('//*[@id="root"]/div/form/div/div[1]/div/div/div/input')

    pwd=driver.find_element_by_xpath('//*[@id="root"]/div/form/div/div[2]/div/div/div/input')
    # 登陆按钮
    log_button=driver.find_element_by_xpath('//*[@id="root"]/div/form/div/div[3]/span')

    phone.send_keys('18851751056')
    time.sleep(1)
    pwd.send_keys('wenshuCourt666')
    time.sleep(1)
    log_button.click()
    driver.get_cookie()
    # 这个cookie不懂是不是文书网的cookie。登陆操作的代码似乎是嵌入的，但是找不到嵌入模块
    return driver.get_cookies()



