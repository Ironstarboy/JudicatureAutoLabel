'''
https://wenshu.court.gov.cn/waf_verify.htm
自动识别验证码
'''
# import pytesseract
# from PIL import Image
#
# # pytesseract.pytesseract.tesseract_cmd = "C:\Program Files (x86)\Tesseract-OCR\tesseract.exe"
# # tessdata_dir_config = '--tessdata-dir "D:/Program Files (x86)/Tesseract-OCR/tessdata"'
#
# image = Image.open("verifyImage/code.png")
# code = pytesseract.image_to_string(image)
# print(code)


import ddddocr

ocr = ddddocr.DdddOcr()
with open('verifyImage/ewzw.png', 'rb') as f:
    img_bytes = f.read()
res = ocr.classification(img_bytes)

print(res)