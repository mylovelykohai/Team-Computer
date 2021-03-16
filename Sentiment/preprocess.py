"""
This is a hub for all preprocessing of data to be handled, 
aswell as importation of models and vectorizers
"""

import pickle

#%%
#Preprocess the text for use in the model
def processText(data):
    print()
    rows = []
    labels = []
    errorCounter = 0
    for index, row in data.iterrows():
        try:
            
            #Normalise the data so that all text is set to lower case.
            row[0] = row[0].lower()
            labels.append(row[1])
            rows.append(row[0])
        except:
            errorCounter += 1
            continue
    return rows, labels
#%%

def preprocess(Data, vec):
    # Tokenize data
    dataRows, labels = processText(Data)
    tokens = vec.fit_transform(dataRows)
    
    return tokens, labels

#Vectorizer importer
def impVec():
    inVec = open("vecSen.pickle","rb")
    vec = pickle.load(inVec)
    inVec.close()
    return vec

#Model importer
def impMod():
    inVec = open("modelSen.pickle","rb")
    vec = pickle.load(inVec)
    inVec.close()
    return vec

#Function for vectorizing only one piece of text
def ProcessSingleText(text):
    vec = impVec()
    text = text.lower()
    text = text.strip()
    text = [text]
    text = vec.transform(text)
    
    return text