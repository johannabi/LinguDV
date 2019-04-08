package applications;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import data.Article;
import io.IO;
import preprocessing.featureselection.AbstractFeatureSelector;
import preprocessing.featureselection.MyTokenizer;

/**
 * Diese Application dient dazu die gecrawlten Artikel
 * einzulesen und sie computerlinguistisch zu verarbeiten.
 * @author Johanna Binnewitt
 *
 */
public class PreproArticles {
	

	
	public static void main(String[] args) throws IOException {

		List<Article> articles = new ArrayList<Article>();
		IO.readArticles("src/main/resources/data/export/", articles);
		System.out.println(articles.size() + " Artikel gefunden");
		
		AbstractFeatureSelector fs = new MyTokenizer(false, "de");
		for(Article a : articles) {
			List<String> features = fs.selectFeatures(a.getContent());
			System.out.println(features);
		}
		

		
		
		
		
	}

}
