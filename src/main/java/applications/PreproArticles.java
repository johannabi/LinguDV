package applications;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import data.Article;
import io.IO;
import preprocessing.featureselection.AbstractFeatureSelector;
import preprocessing.featureselection.OpenNLPTokenizer;
import preprocessing.featureselection.Stemmer;

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
		
		//TODO JB: erstelle ein Set, das alle Merkmale des Korpus sammelt
		
//		AbstractFeatureSelector fs = new OpenNLPTokenizer(false, "de");
		AbstractFeatureSelector fs = new Stemmer(false, "de");
		// statt OpenNLPTokenizer kann hier auch der MyTokenizer oder
		// der Stemmer initialisiert werden
		
		
		for(int i = 0; i < articles.size(); i++) {
			Article a = articles.get(i);
			List<String> features = fs.selectFeatures(a.getContent());
			System.out.println(features);
			
			// auf Grundlage der "feautures"-List kann nun die Häufigkeitsverteilung
			// der Tokens im aktuellen Artikel ermittelt werden

			//die Map "count" soll am Ende zu jedem Token die Häufigkeit im 
			//Artikel enthalten [Heute=1, jagt=1, die=2, Katze=1, Maus=1, .=1]
			Map<String, Integer> count = new HashMap<String, Integer>();
			
			//TODO JB: ermittle hier die Häufigkeitsverteilung
			
			
			//hier wird "count" am Value-Wert absteigend sortiert
			List<Entry<String, Integer>> entries = new ArrayList<>(count.entrySet());
			entries.sort(Entry.comparingByValue());
			Collections.reverse(entries);
			
			//gibt die Häufigkeitsverteilung in der Konsole aus
			//(häufigstes Merkmal zuerst)
			System.out.println(entries);

			
		}
		

		
		
		
		
	}

}
