package applications;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import data.Article;
import io.IO;
import preprocessing.featureselection.AbstractFeatureSelector;
import preprocessing.featureselection.Stemmer;
import preprocessing.featureweighting.AbstractVectorization;
import preprocessing.featureweighting.BinaryVectorization;
import textmining.classification.NaiveBayesClassifier;

public class ClassifyArticles {

	public static void main(String[] args) throws IOException {
		// --- Artikel einlesen ---
		List<Article> articles = new ArrayList<Article>();
		IO.readArticles("src/main/resources/data/LiteraturFilm", articles);
//		IO.readArticles("src/main/resources/data/export", articles);
		
		String posLabel = "Film";
		String negLabel = "Literatur";
		
		System.out.println(articles.size() + " Artikel gefunden");

		// --- Artikel vorverarbeiten ---
		// allFeatures soll am Ende der Vorverarbeitung aller Artikel alle
		// im Korpus enthaltenen Merkmale gesammelt haben
		Set<String> allFeatures = new HashSet<String>();

//		AbstractFeatureSelector fs = new OpenNLPTokenizer(true, "de");
		AbstractFeatureSelector fs = new Stemmer(true, "de");
//		AbstractFeatureSelector fs = new Lemmatizer(false, "de");

		// Merkmale selektieren
		for (int i = 0; i < articles.size(); i++) {
			Article a = articles.get(i);
			List<String> features = fs.selectFeatures(a.getContent());

			// Artikel-Objekt werden die soeben ermittelten Merkmale übergeben
			a.setFeatures(features);
		}
		
		// Merkmale, die nur in wenigen Dokumenten auftauchen, werden entfernt
		for(Article a : articles) {
			fs.deleteRareFeatures(a, 5);
			// die Merkmale des aktuellen Artikels werden zu allFeatures hinzugefügt
			allFeatures.addAll(a.getFeatures());
		}
		
		System.out.println("Das Korpus enthält " + allFeatures.size() + " unterschiedliche Merkmale.");
		IO.exportFeatures(allFeatures, "features.txt");
		
		
		// --- Merkmale gewichten ---
		AbstractVectorization vectorization = new BinaryVectorization(allFeatures);
		
		for (Article a : articles) {
			Double[] vec = vectorization.vectorize(a.getFeatures());
			a.setWeightVector(vec);
		}
		
		

		// Textklassifikation
		Collections.shuffle(articles);
		int trainSize = (int) (articles.size() * 0.8);
		// 80 % der Artikel werden für das Modell verwendet, der Rest
		// kann klassifiziert werden
		List<Article> trainArticles = articles.subList(0, trainSize);
		List<Article> testArticles = articles.subList(trainSize + 1, articles.size() - 1);

		NaiveBayesClassifier nb = new NaiveBayesClassifier(posLabel, negLabel);
		nb.train(trainArticles);
		

		for (Article testArticle : testArticles) {
			String predicted = nb.classify(testArticle);
			System.out.println("Predicted: " + predicted + " -- Actual: " + testArticle.getCategory());
		}
		
	}

}
