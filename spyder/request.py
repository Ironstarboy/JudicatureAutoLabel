from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.wait import WebDriverWait
import requests
from settings import FILE_DIR
from tools.UtilsHelper import PageID, Ip_proxy


# 用request登陆文书网，似乎很难。流程太复杂
def login_with_requests():
    s = requests.session()
    api = "https://account.court.gov.cn/api/login"
    headers = {
        "Accept": "application/json, text/plain, */*",
        "Accept-Encoding": "gzip, deflate, br",
        "Accept-Language": "zh-CN,zh;q=0.9",
        "Host": "account.court.gov.cn",
        "Origin": "https://account.court.gov.cn",
        "Referer": "https://account.court.gov.cn/app",
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.83 Safari/537.36",
        "X-Requested-With": "XMLHttpRequest",
        "Sec-Fetch-Dest": "empty",
        "Sec-Fetch-Mode": "cors",
        "Sec-Fetch-Site": "same-origin"
    }

    proxies = {
        "http": Ip_proxy()['proxies'],
        "https": Ip_proxy()['proxies']
    }

    params = {
        "username": "18851751056",
        "password": "加密后的密码，可通过F12获取到",
        "appDomain": None
    }

    response = s.post(api, data=params, proxies=proxies, headers=headers)
    print(response.json())

login_with_requests()