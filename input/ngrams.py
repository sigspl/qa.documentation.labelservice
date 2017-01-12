import os, os.path
from nltk import ngrams

datasource='tc_clusters_jan2017.txt'
dataoutput='tc_ngrams'

with open(datasource, 'r') as datasource:
    data=datasource.read().replace('\n', '')

if (os.path.isfile(dataoutput)):
	os.remove(dataoutput)
open(dataoutput, 'a').close()

for i in range(15, 6, -2):
	ngramz = ngrams(data.split(), i)
	for grams in ngramz:
	  with open(dataoutput, "a") as f:
	   f.write(" ".join(grams) +"\n")


