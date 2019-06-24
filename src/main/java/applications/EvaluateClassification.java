package applications;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import data.Article;
import evaluation.Evaluator;
import io.IO;
import preprocessing.featureselection.AbstractFeatureSelector;
import preprocessing.featureselection.Lemmatizer;
import preprocessing.featureselection.OpenNLPTokenizer;
import preprocessing.featureselection.Stemmer;
import preprocessing.featureweighting.AbstractVectorization;
import preprocessing.featureweighting.LogLikelihoodVectorization;
import preprocessing.similarity.DistanceType;
import textmining.classification.KNNClassifier;
import textmining.classification.NaiveBayesClassifier;

public class EvaluateClassification {
	
	
	public static void main(String[] args) throws IOException {
		// --- Artikel einlesen ---
				List<Article> articles = new ArrayList<Article>();
				IO.readArticles("src/main/resources/data/wikipedia_korpus", articles);
//				IO.readArticles("src/main/resources/data/ActionKriminal", articles);
				
				String posLabel = "Historienfilm";
				String negLabel = "Nicht-H-Film";

				
				System.out.println(articles.size() + " Artikel gefunden");

				// --- Artikel vorverarbeiten ---
				// allFeatures soll am Ende der Vorverarbeitung aller Artikel alle
				// im Korpus enthaltenen Merkmale gesammelt haben
				Set<String> allFeatures = new HashSet<String>();

				AbstractFeatureSelector fs = new OpenNLPTokenizer(true, "de");
//				AbstractFeatureSelector fs = new Stemmer(true, "de");
//				AbstractFeatureSelector fs = new Lemmatizer(false, "de");

				for (int i = 0; i < articles.size(); i++) {
					Article a = articles.get(i);
					List<String> features = fs.selectFeatures(a.getContent());

					// Artikel-Objekt werden die soeben ermittelten Merkmale übergeben
					a.setFeatures(features);
				}
				
				//Merkmale, die nur in wenigen Dokumenten auftauchen, werden entfernt
				for(Article a : articles) {
					fs.deleteRareFeatures(a, 5);
					// die Merkmale des aktuellen Artikels werden zu allFeatures hinzugefügt
					allFeatures.addAll(a.getFeatures());
				}

				System.out.println("Das Korpus enthält " + allFeatures.size() + " unterschiedliche Merkmale.");
				IO.exportFeatures(allFeatures, "features.txt");
				
				
				//Artikel vektorisieren
				AbstractVectorization vectorization = new LogLikelihoodVectorization(articles, allFeatures);
				for(Article a : articles) {
					Double[] weights = vectorization.vectorize(a.getFeatures());
					a.setWeightVector(weights);
				}
				
				
				System.out.println("Evaluiere Klassifikation von Artikeln aus den Kategorien: " + posLabel + " und " + negLabel);
				Evaluator evaluator = new Evaluator(posLabel, negLabel);
				System.out.println("Evaluiere KNN");
				evaluator.evaluate(articles, new KNNClassifier(5, DistanceType.COSINE));
				
				System.out.println("Evaluiere Naive Bayes");
				evaluator.evaluate(articles, new NaiveBayesClassifier(posLabel, negLabel));
				
	}

}
