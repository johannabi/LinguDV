package applications;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import data.Article;
import io.IO;
import preprocessing.featureselection.AbstractFeatureSelector;
import preprocessing.featureselection.OpenNLPTokenizer;
import preprocessing.featureweighting.AbstractVectorization;
import preprocessing.featureweighting.CountVectorization;
import preprocessing.featureweighting.LogLikelihoodVectorization;
import preprocessing.featureweighting.TFIDFVectorization;

public class WeightArticles {

	/**
	 * Applikation präprozessiert die Artikel inklusive Merkmalsgewichtung.
	 * Anschließend werden pro Artikel die 10 Merkmale mit den höchsten Gewichten ausgegeben.
	 * @param args
	 */
	public static void main(String[] args) {
		List<Article> articles = new ArrayList<Article>();
		IO.readArticles("src/main/resources/data/wikipedia_korpus", articles);
		System.out.println(articles.size() + " Artikel gefunden");

		Set<String> allFeatures = new HashSet<String>();

		// Merkmalsauswahl
		AbstractFeatureSelector fs = new OpenNLPTokenizer(true, "de");
		for (Article a : articles) {
			List<String> features = fs.selectFeatures(a.getContent());
			a.setFeatures(features);
		}

		for (Article a : articles) {
			fs.deleteRareFeatures(a, 5);
			allFeatures.addAll(a.getFeatures());
		}

		// Merkmalsgewichtung
		AbstractVectorization tfidf = new TFIDFVectorization(allFeatures, fs.getDocumentFrequencies(), articles.size());
		AbstractVectorization count = new CountVectorization(allFeatures);
		AbstractVectorization loglike = new LogLikelihoodVectorization(articles, allFeatures);

		// für die Ausgabe der relevanten Merkmale benötigen wir den Index der
		// Merkmale im Wörterbuch (-> deshalb hier Liste und kein Set)
		List<String> featureList = new ArrayList<String>(allFeatures);

		for (Article a : articles) {
			
			System.out.println("*******" + a.getTitle() + "*******");
			
			System.out.println("count");
			Double[] countVec = count.vectorize(a.getFeatures());
			printMostRelevantFeatures(featureList, countVec, countVec);
			System.out.println("-----------------");
			
//			System.out.println("tfidf");
//			Double[] tfidfVec = tfidf.vectorize(a.getFeatures());		
//			printMostRelevantFeatures(featureList, tfidfVec, countVec);
//			System.out.println("-----------------");
			
			System.out.println("loglikelihood");
			Double[] likeVec = loglike.vectorize(a.getFeatures());
			printMostRelevantFeatures(featureList, likeVec, countVec);

		}

	}

	private static void printMostRelevantFeatures(List<String> featureList, Double[] vec, Double[] countVec) {
		// TODO JB: gib in der Konsole die 10
		// Merkmale mit der höchsten Gewichtung aus
		


	}

}
