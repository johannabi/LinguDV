package preprocessing;

import static org.junit.Assert.assertArrayEquals;
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

public class TestTFIDF {

	@Test
	public void test() {
		List<Article> articles = new ArrayList<Article>();
		articles.add(new Article("Der letzte Sommer war warm.", "", "", "", ""));
		articles.add(new Article("Im Sommer sind Semesterferien.", "", "", "", ""));
		articles.add(new Article("Ein warmer Sommer.", "", "", "", ""));
		
		
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
			System.out.println(Arrays.asList(vec));
		}
		
		Double[] actual = articles.get(0).getWeightVector();
		Double[] expected = new Double[]{1.0986122886681098, 0.4054651081081644, 1.0986122886681098, 0.0, 0.0, 0.0, 1.0986122886681098, 0.0, 0.0};
		
		assertArrayEquals(expected, actual);
	}

}
