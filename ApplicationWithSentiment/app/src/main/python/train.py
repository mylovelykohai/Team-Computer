#%%
from sklearn.model_selection import train_test_split
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import plot_confusion_matrix
from nltk.corpus import stopwords
import pandas as pd
import pickle
import matplotlib.pyplot as plt  
from sklearn import naive_bayes
import preprocess

#%%
def printMatrix(matrix):
    tn,fp,fn,tp = matrix.flatten()
    print(f"True negative: {tn}")
    print(f"False negative: {fn}")
    print(f"True positive: {tp}")
    print(f"False positive: {fp}")
#Export both the vectorizer and model for future use
def export(vec, model):
    with open("vecSen.pickle", "wb") as out:
        pickle.dump(vec, out)
    
    with open("modelSen.pickle", "wb") as out:
        pickle.dump(model, out)

#Load the data from the excel file
fileData = pd.read_excel("SentimentAnalysisData.xlsx")

#Create the training and testing lists.
training = []
testing = []

#Create stopwords
stopwords = set(stopwords.words('english'))

#Create the configure the vectorizer
vec = CountVectorizer(lowercase= True, analyzer='word', stop_words=stopwords, ngram_range=(1,3), strip_accents='ascii')

#Create the naive bayes classifier
#Classifier = naive_bayes.ComplementNB()
Classifier = LogisticRegression()
#Get the data and randomize it
fileData = fileData.sample(frac=1)
Text = fileData[:]

#Preprocess data, get data and labels
data, labels = preprocess.preprocess(Text, vec)

#Test to see if label and data size is the same
print(f"Training size: {data.shape[0]}")
print(f"Labels size: {len(labels)}")

#Split data into training and testing
train_x, test_x, train_y, test_y = train_test_split(data, labels, test_size=0.2, shuffle=True)

#Train the model
nb = Classifier.fit(train_x, train_y)

#Export the vectorizer and the model for use in other programs.
export(vec,nb)

#Display a % prediction for the first 10 labels.
for index in range (1, 10):
    res = nb.predict_proba(test_x[index])
    res = int(res[0][1]*100)
    print(f"{res}%")

#Displayt the accuracy of the model
print(f"Accuracy is: {nb.score(test_x,test_y)*100}%")

#Predict the test set again so it can be used in the confusion matrix.

#%%
pred_y = nb.predict(test_x)

plot_confusion_matrix(nb, test_x, test_y)

plt.show()