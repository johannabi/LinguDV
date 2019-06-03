package preprocessing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import data.Article;
import preprocessing.featureselection.AbstractFeatureSelector;
import preprocessing.featureselection.Stemmer;
import preprocessing.featureweighting.AbstractVectorization;
import preprocessing.featureweighting.BinaryVectorization;
import preprocessing.similarity.Distance;

public class TestDistance {

	@Test
	public void test() {
//		fail("Not yet implemented");
		List<Article> articles = new ArrayList<Article>();
		articles.add(new Article("Der letzte Sommer war warm.", "", "", "", ""));
		articles.add(new Article("Im Sommer sind Semesterferien.", "", "", "", ""));
		
		Set<String> allFeatures = new HashSet<String>();
		
		AbstractFeatureSelector fs = new Stemmer(false, "de");
		
		for(Article a : articles) {
			List<String> features = fs.selectFeatures(a.getContent());
			a.setFeatures(features);
			allFeatures.addAll(features);
		}
		
		AbstractVectorization vectorization = new BinaryVectorization(allFeatures);
		for (Article a : articles) {
			Double[] vec = vectorization.vectorize(a.getFeatures());
			a.setWeightVector(vec);
		}
		
		double dist = Distance.computeEuclideanDistance(articles.get(0).getWeightVector(),
				articles.get(1).getWeightVector());
		
		double correct = 2.6457513110645907;
		System.out.println(dist);
		assertEquals(correct, dist, 0);
		
		
		
		
	}

}
