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
import preprocessing.featureselection.Lemmatizer;
import preprocessing.featureselection.OpenNLPTokenizer;
import preprocessing.featureselection.Stemmer;

/**
 * Diese Application dient dazu die gecrawlten Artikel einzulesen und sie
 * computerlinguistisch zu verarbeiten.
 * 
 * @author Johanna Binnewitt
 *
 */
public class PreproArticles {

	public static void main(String[] args) throws IOException {

		// --- Artikel einlesen ---
		List<Article> articles = new ArrayList<Article>();
		IO.readArticles("src/main/resources/data/export", articles);
		System.out.println(articles.size() + " Artikel gefunden");

		// --- Artikel vorverarbeiten ---
		// allFeatures soll am Ende der Vorverarbeitung aller Artikel alle
		// im Korpus enthaltenen Merkmale gesammelt haben
		Set<String> allFeatures = new HashSet<String>();

		
//		AbstractFeatureSelector fs = new OpenNLPTokenizer(false, "de");
		AbstractFeatureSelector fs = new Stemmer(true, "de");
//		AbstractFeatureSelector fs = new Lemmatizer(false, "de");
		// statt OpenNLPTokenizer kann hier auch der MyTokenizer oder
		// der Stemmer initialisiert werden
		for (int i = 0; i < articles.size(); i++) {
			Article a = articles.get(i);
			List<String> features = fs.selectFeatures(a.getContent());
			
			//Artikel-Objekt werden die soeben ermittelten Merkmale übergeben
			a.setFeatures(features);
			
			// die Merkmale des aktuellen Artikels werden zu allFeatures hinzugefügt
			allFeatures.addAll(features);

			// auf Grundlage der "features"-List kann nun die Häufigkeitsverteilung
			// der Tokens im aktuellen Artikel ermittelt werden

			// die Map "count" soll am Ende zu jedem Token die Häufigkeit im
			// Artikel enthalten [Heute=1, jagt=1, die=2, Katze=1, Maus=1, .=1]
			Map<String, Integer> count = new HashMap<String, Integer>();

			// Die Häufigkeitsverteilung kann ermittelt werden, indem man über
			// alle einzelnen Merkmale iteriert und ihr Vorkommen zählt
			for (String feature : features) {
				Integer frequency = count.get(feature);
				if (frequency != null) { // Merkmal ist schon mal aufgetaucht
					count.put(feature, frequency + 1);
				} else { // Merkmal taucht zum ersten mal auf
					count.put(feature, 1);
				}
			}

			// hier wird "count" am Value-Wert absteigend sortiert
			List<Entry<String, Integer>> entries = new ArrayList<>(count.entrySet());
			entries.sort(Entry.comparingByValue());
			Collections.reverse(entries);

			// gibt die Häufigkeitsverteilung in der Konsole aus
			// (häufigstes Merkmal zuerst)
//			System.out.println(entries);

		}
		System.out.println("Das Korpus enthält " + allFeatures.size() + " unterschiedliche Merkmale.");
		IO.exportFeatures(allFeatures, "features.txt");
		
		// --- erste Schritte Information Retrieval ---
		//Finde alle Artikel im Korpus, die ein bestimmtes Wort (und Flexionsformen) davon enthalten
		String keyword = "klassifizieren"; //suchbegriff kann an euer Korpus angepasst werden
		for(Article a : articles) {
			if(a.getFeatures().contains(keyword))
				System.out.println(a.getContent());
		}
	}

}
