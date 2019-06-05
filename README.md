# LinguDV
BA Informationsverarbeitung: Linguistische Datenverarbeitung

## About
Dieses Repository enthält ein Java-Programm mit Text Mining-Methoden zur Vorverarbeitung
und Klassifikation von Wikipedia-Artikeln.

## Installation
Die Wikipedia-Artikel müssen zunächst gecrawlt werden ( -> src/main/java/applications/CrawlArticles.java)

Die gecrawlten Wikipedia-Artikel werden entsprechend als .txt-Dateien gespeichert. Für die weitere
Verarbeitung müssen die input-Pfade in den entsprechenden Applikationen (im package src/main/java/applications) angepasst werden

##### Für die Vorverarbeitung werden folgende zusätzliche Dateien benötigt:

Lemmatizer ([Mate Tools](https://drive.google.com/file/d/0B-qbj-8rtoUMaUVsWUFuOE81ZW8)): -> src/main/resources/MateTools

Sentence Detector ([OpenNLP](http://opennlp.sourceforge.net/models-1.5/de-sent.bin)): -> src/main/resources/OpenNLP

Tokenizer ([OpenNLP](http://opennlp.sourceforge.net/models-1.5/de-token.bin)): -> src/main/resources/OpenNLP

[Stopword Filter](https://github.com/stopwords-iso/stopwords-de/blob/master/stopwords-de.txt): -> src/main/resources
