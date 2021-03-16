# Team-Computer
Team project for Northumbria University Team Computer

List of components:
1. predict.py
2. preprocessing.py
3. train.py
4. test.txt
5. SentimentAnalysisData.xlsx

## train.py
### Run this first
This file is responcible for training and exporting both the model and the vectorizer. It uses SentimentAnalysisData.xlsx

## Predict.py
Predict.py is used to do singular predictions of text, this will be the script used by the app to analyse text data and apply a emotion to them.

### how to use
This is a console application, therefore to run it directly and not through the app you will need to use a consol application or a IDE like spyder.
There is only 1 function in this script called pred. the syntax is pred([the plain text to be predicted])

## preprocessin.py
This is the file which handles all preprocessing of the data, including importation of the model and the vectorizer

## test.txt 
Is a sample file for being used in predict.py

## SentimentAnalysisData.xlsx
The datafile being used in train.py. This file has been preprocessed to only contain data relevant to the application. *(Only keeping happy, sad and neutral for its data)*

## Modules used.
1. sklearn
2. pandas
3. pickle
4. nltk
