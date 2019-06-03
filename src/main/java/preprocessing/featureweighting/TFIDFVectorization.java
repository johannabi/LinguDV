package preprocessing.featureweighting;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class TFIDFVectorization extends AbstractVectorization{
	
	private Map<String, Integer> documentFrequency;
	int corpusSize;

	public TFIDFVectorization(Set<String> dictionary, Map<String, Integer> docFreq, int corpusSize) {
		super(dictionary);
		this.documentFrequency = docFreq;
		this.corpusSize = corpusSize;
		
	}

	@Override
	public Double[] vectorize(List<String> docFeatures) {
		// TODO JB: implementiere hier eine Tf-Idf-Gewichtung
		// der übergebenen Merkmale

		//Double[] tfidf ist der (noch) leere Gewichtungsvektor
		Double[] tfidf = new Double[super.allFeatures.size()];

		//TODO JB: 1) absolute Häufigkeit für jedes Merkmal in docFeatures berechnen
		// (siehe Methode unten (countVectorize()))


		//TODO JB: 2) Frequenz des häufigsten Merkmals ermitteln

		
		//TODO JB: 3) für jedes Merkmal in allFeatures die relative tf & idf berechnen
		// und auf den Vektor legen

		

		return tfidf;
	}
	
	/**
	 * erstellt für jedes Merkmal im Wörterbuch einen Wert,
	 * 
	 * @param docFeatures
	 * @return
	 */
	private Double[] countVectorize(List<String> docFeatures) {

		
		Double[] vector = new Double[allFeatures.size()];
		//TODO JB: implementiere hier (nochmal) die Count Vectorization
		// (hier reicht ein Copy-Paste aus der Klasse CountVectorization
		
		return vector;
	}

}
