package classification;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import data.Article;
import preprocessing.featureselection.AbstractFeatureSelector;
import preprocessing.featureselection.Stemmer;
import preprocessing.featureweighting.AbstractVectorization;
import preprocessing.featureweighting.TFIDFVectorization;
import preprocessing.similarity.DistanceType;
import textmining.classification.AbstractClassifier;
import textmining.classification.KNNClassifier;

public class TestKNN {

	@Test
	public void test() {
		List<Article> articles = new ArrayList<Article>();
		articles.add(new Article("Der letzte Sommer war warm.", "", "test1", "Sommer"));
		articles.add(new Article("Im Sommer sind Semesterferien.", "", "test2", "Sommer"));
		articles.add(new Article("Ein warmer Sommer.", "", "test3", "Sommer"));
		articles.add(new Article("Im Winter liegt Schnee.", "", "test4", "Winter"));
		articles.add(new Article("Der Winter ist kalt.", "", "test5", "Winter"));
		articles.add(new Article("Es ist kalt.", "", "test6", "Winter"));
		articles.add(new Article("Im Sommer esse ich Eis.", "", "test7", "Sommer"));
		
		
		Set<String> allFeatures = new HashSet<String>();
		
		AbstractFeatureSelector fs = new Stemmer(false, "de");
		
		for(Article a : articles) {
			List<String> features = fs.selectFeatures(a.getContent());
			a.setFeatures(features);
			allFeatures.addAll(features);
		}
		System.out.println(allFeatures);
		
		AbstractVectorization vectorization = new TFIDFVectorization(allFeatures, fs.getDocumentFrequencies(), articles.size());
		for (Article a : articles) {
			Double[] vec = vectorization.vectorize(a.getFeatures());
			a.setWeightVector(vec);
		}
		
		
		Article toClassify = articles.remove(articles.size()-1);
		System.out.println(toClassify.getContent());
		
		AbstractClassifier classifier = new KNNClassifier(3, DistanceType.COSINE);
		classifier.train(articles);
		String label = classifier.classify(toClassify);
		System.out.println("Klassifiziert: " + label + " (korrekt: " + toClassify.getCategory() + ")");
		
		assertEquals(toClassify.getCategory(), label);
		
		
	}

}
