import pandas as pd
from openpyxl import Workbook
from time import sleep

angry = pd.read_csv("Emotion(angry).csv")
happy = pd.read_csv("Emotion(happy).csv")
sad = pd.read_csv("Emotion(sad).csv")
angry = angry[:]
happy = happy[:]
sad = sad[:]
datas = [angry, happy, sad]

#Open library for working in excel sheets
workbook = Workbook()
sheet = workbook.active

#Assign the labels for each piece of data
sheet["A1"] = "Sentence"
sheet["B1"] = "Sentiment"

#Function for saving the excel file
def savebook(workbook):
    while True:
        try:
            workbook.save(filename="SentimentAnalysisData.xlsx")
            break
        except:
            print("Currently busy trying again...")
            sleep(5)

index = 2
for data in happy["content"]:
    
    sheet[f"A{index}"] = data
    sheet[f"B{index}"] = "Happy"
    index = index + 1
    
for data in sad["content"]:
    
    if data[0] == "[":
        splitup = data.split("', '")
        for s in splitup:
            s = s.replace("[", "")
            s = s.replace("]", "")
            s = s.replace("(Sad Whatsapp Status\xa0", "")
            s = s.replace("\\xa0( Sad Whatsapp Status\\xa0", "")
            s = s.replace("( Sad Quotes\\xa0", "")
            s = s.replace("(Sad Whatsapp Status\xa0", "")
            s = s.replace("'", "")
            s = s.replace("( Sad Status\xa0", "")
            s = s.replace("( Sad Whatsapp Status\\xa0", "")
            s = s.replace("( Sad Status for Whatsapp\\xa0", "")
            s = s.replace("\\xa0(\\xa0Sad Quotes", "")
            sheet[f"A{index}"] = s
            sheet[f"B{index}"] = "Sad"
            index = index + 1
    else:
        sheet[f"A{index}"] = data
        sheet[f"B{index}"] = "Sad"
        index = index + 1

for data in angry["content"]:
    
    if data[0] == "[":
        splitup = data.split("', '")
        for s in splitup:
            s = s.replace("[", "")
            s = s.replace("]", "")
            s = s.replace("( Angry Whatsapp Status\\xa0", "")
            s = s.replace("(\\xa0Angry\\xa0Quotes\\xa0", "")
            s = s.replace("(\\xa0Angry Status\\xa0", "")
            s = s.replace("(\\xa0Angry Whatsapp Status\\xa0", "")
            s = s.replace("\\xa0( Angry Whatsapp Status\\xa0", "")
            s = s.replace("\\xa0(\\xa0Anger Status for Whatsapp\\xa0", "")
            s = s.replace("\\xa0(\\xa0Angry Quotes for Relationship\\xa0", "")
            s = s.replace("\\xa0(\\xa0Angry Love Quotes\\xa0", "")
            sheet[f"A{index}"] = s
            sheet[f"B{index}"] = "Angry"
            index = index + 1
    else:
        sheet[f"A{index}"] = data
        sheet[f"B{index}"] = "Angry"
        index = index + 1

savebook(workbook)