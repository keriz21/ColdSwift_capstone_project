import pytesseract
import cv2
import numpy as np
import pandas as pd
from PIL import Image
import matplotlib.pyplot as plt
import os
import re
import json

class KTPInformation(object):
    def __init__(self):
        self.nik = ""
        self.name = ""
class KTPOCR(object):
    def __init__(self, image_path):
        self.image_path = image_path
        self.result = KTPInformation()
        self.process()

    def process(self):
        image = cv2.imread(self.image_path)
        gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
        th, threshed = cv2.threshold(gray, 127, 255, cv2.THRESH_TRUNC)
        raw_extracted_text = pytesseract.image_to_string(threshed, lang="ind")
        self.extract(raw_extracted_text)

    def word_to_number_converter(self, word):
        word_dict = {
            "L": "1",
            "l": "1",
            "O": "0",
            "o": "0",
            "?": "7",
            "A": "4",
            "Z": "2",
            "z": "2",
            "S": "5",
            "s": "5",
            "b": "6",
            "B": "8",
            "G": "6"
        }
        res = ""
        for letter in word:
            if letter in word_dict:
                res += word_dict[letter]
            else:
                res += letter

        return res

    def extract(self, extracted_result):
        for word in extracted_result.split("\n"):
            word = self.pun_rem(word)

            if "NIK" in word:
                word = word.split(':')
                self.result.nik = self.word_to_number_converter(word[-1].replace(" ", ""))
                continue
            if "Nama" in word:
              word = word.split(':')
              self.result.nama = word[-1]
              continue

    def pun_rem(self, text):
        punctuations = '''!()[]{}'"\<>?@#$%^&*_~'''
        no_punct = ''

        for char in text:
            if char not in punctuations:
                no_punct = no_punct + char

        return no_punct

    def to_json(self):
        return json.dumps({"nik": self.result.nik, "name": self.result.nama})
    
# ekstrak info
def extract_ktp_info(image_path):
    ocr = KTPOCR(image_path)
    return ocr.to_json()

if __name__ == "__main__":
    import sys
    image_path = sys.argv[1]
    print(extract_ktp_info(image_path))

# if __name__ == "__main__":
#     current_dir = os.path.dirname(__file__)
#     folderPath = os.path.join(current_dir, 'ocr_data-test/')
#     filePath = os.path.join(folderPath, 'ktp_rouf.jpg')
#     images = KTPOCR(filePath)
#     print(images.to_json())