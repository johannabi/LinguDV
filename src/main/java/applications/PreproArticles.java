package applications;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import data.Article;
import io.IO;
import preprocessing.featureselection.AbstractFeatureSelector;
import preprocessing.featureselection.MyTokenizer;
import preprocessing.featureselection.OpenNLPTokenizer;

/**
 * Diese Application dient dazu die gecrawlten Artikel
 * einzulesen und sie computerlinguistisch zu verarbeiten.
 * @author Johanna Binnewitt
 *
 */
public class PreproArticles {
	

	
	public static void main(String[] args) throws IOException {
		

		List<Article> articles = new ArrayList<Article>();
		IO.readArticles("src/main/resources/data/export/Bergbau", articles);
		System.out.println(articles.size() + " Artikel gefunden");
		
		AbstractFeatureSelector fs = new OpenNLPTokenizer(false, "de");
		for(int i = 0; i < articles.size(); i++) {
			Article a = articles.get(i);
			List<String> features = fs.selectFeatures(a.getContent());
			System.out.println(features);
			
		}
		

		
		
		
		
	}

}
