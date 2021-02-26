"""
This script handles the prediction of messages 
Its only purpose is to take a file name and then 
output an umber depicting a number
"""
from preprocess import ProcessSingleText, impMod

def getText(file):
    #Open the file
    file = open(file, "r")

    #extract the data
    text = file.readlines()
    #Going through all data change the list into a string
    for t in text:
        data = t
    return data

def pred(file):
    text = getText(file)
    
    #Import the model from preprocessing
    model = impMod()
    #Convert the text into a vectorized string
    text = ProcessSingleText(text)
    res = model.predict(text)
    
    #The data comes out as a array. Convert into a int
    res = res[0]
    
    return res