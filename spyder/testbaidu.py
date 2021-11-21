from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.wait import WebDriverWait
import time
# from tools.UtilsHelper import PageID, Ip_proxy

"""
selenium在百度上操作，看看有什么效果
"""
def login_with_selenium():
    driver=webdriver.Chrome()
    url="https://www.baidu.com"
    driver.get(url)
    # driver.refresh()
    s=driver.find_element_by_xpath('//*[@id="kw"]')#input
    butten=driver.find_element_by_xpath('//*[@id="su"]')#百度一下按钮

    s.send_keys('selenium')
    time.sleep(3)
    butten.click()
    time.sleep(3)

login_with_selenium()