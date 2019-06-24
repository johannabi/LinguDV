package applications;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import data.Article;
import io.IO;
import preprocessing.featureselection.AbstractFeatureSelector;
import preprocessing.featureselection.OpenNLPTokenizer;
import preprocessing.featureweighting.AbstractVectorization;
import preprocessing.featureweighting.CountVectorization;
import preprocessing.featureweighting.LogLikelihoodVectorization;
import preprocessing.similarity.Distance;

public class ComputeDistances {

	/**
	 * Applikation präprozessiert alle gespeicherten Wikipedia-Artikel, wählt einen
	 * zufälligen Artikel aus und berechnet Entfernungen von diesem Artikel zu allen
	 * anderen.
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

		// seltene Merkmale werden aus dem Wörterbuch entfernt
		for (Article a : articles) {
			fs.deleteRareFeatures(a, 5);
			allFeatures.addAll(a.getFeatures());
		}

		// Merkmalsgewichtung
		AbstractVectorization loglike = new LogLikelihoodVectorization(articles, allFeatures);
		AbstractVectorization count = new CountVectorization(allFeatures);

		for (Article a : articles) {		
			Double[] vec = loglike.vectorize(a.getFeatures());
//			Double[] vec = count.vectorize(a.getFeatures());
			a.setWeightVector(vec);
		}	
		
		
		// von allen Artikel-Objekten wird eins zufällig ausgewählt und
		// aus der article-List entfernt
		Random random = new Random();
		int index = random.nextInt(articles.size()-1);		
		Article a = articles.remove(index);
		System.out.println(a.getTitle() + " " + a.getCategories());
		
		
		// zwischen diesem einen Artikel und allen Artikeln aus der Liste werden
		// Entfernungen berechnet
		Map<Article, Double> distances = new HashMap<Article, Double>();
		for(Article cur : articles) {
			double dist = 1d - Distance.computeCosineSimilarity(a.getWeightVector(), cur.getWeightVector());
//			double dist = Distance.computeEuclideanDistance(a.getWeightVector(), cur.getWeightVector());
			distances.put(cur, dist);
		}
		
		// Entfernungen werden sortiert und die 20 nächsten Artikel werden ausgegeben
		List<Entry<Article, Double>> entries = new ArrayList<Entry<Article, Double>>(distances.entrySet());
		entries.sort(Entry.comparingByValue());
		for(int i = 0; i < 20; i++) {
			Article cur = entries.get(i).getKey();
			Double sim = entries.get(i).getValue();
			System.out.println(cur.getCategory() + "\t" + sim + "\t" + cur.getTitle() + "\t" + cur.getCategories());
		}

	}

}
