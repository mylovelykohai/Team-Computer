"""
This script handles the prediction of messages 
Its only purpose is to take a file name and then 
output an umber depicting a number
"""
from preprocess import ProcessSingleText, impMod

def pred(text):
    #Import the model from preprocessing
    model = impMod()
    #Convert the text into a vectorized string
    text = ProcessSingleText(text)
    res = model.predict(text)
    
    resprec = model.predict_proba(text)
    resprec = int(resprec[0][1]*100)
    print(resprec)
    
    #The data comes out as a array. Convert into a int
    res = res[0]
    
    return res