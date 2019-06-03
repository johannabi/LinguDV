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
import preprocessing.featureweighting.TFIDFVectorization;

public class WeightArticles {

	public static void main(String[] args) {
		List<Article> articles = new ArrayList<Article>();
		IO.readArticles("src/main/resources/data/ActionKriminal", articles);
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

		// für die Ausgabe der relevanten Merkmale benötigen wir den Index der
		// Merkmale im Wörterbuch (-> deshalb hier Liste und kein Set)
		List<String> featureList = new ArrayList<String>(allFeatures);

		for (Article a : articles) {
			Double[] tfidfVec = tfidf.vectorize(a.getFeatures());
			System.out.println("*******" + a.getTitle() + "*******");
			printMostRelevantFeatures(featureList, tfidfVec);
			System.out.println("-----------------");
			Double[] countVec = count.vectorize(a.getFeatures());
			printMostRelevantFeatures(featureList, countVec);

		}

	}

	private static void printMostRelevantFeatures(List<String> featureList, Double[] vec) {
		// TODO JB: gib in der Konsole die 10
		// Merkmale mit der höchsten Gewichtung aus



	}

}
