package preprocessing.featureweighting;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Java-Klasse, die die Term-Dokument-Relation durch die
 * Tf-Idf-Gewichtung berechnet
 * @author Johanna Binnewitt
 *
 */
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


		//Double[] tfidf ist der (noch) leere Gewichtungsvektor
		Double[] tfidf = new Double[super.allFeatures.size()];

		
		// Lösung
		
		//1) absolute Häufigkeit für jedes Merkmal in docFeatures berechnen
		// (siehe Methode unten (countVectorize()))
		Double[] count = countVectorize(docFeatures);
		
		//2) Frequenz des häufigsten Merkmals ermitteln
		Double maxFreq = Collections.max(Arrays.asList(count));
		
		//3) für jedes Merkmal in allFeatures die relative tf & idf berechnen
		// und auf den Vektor legen
		int i = 0;
		for (String f : allFeatures) {

			double tf = count[i] / maxFreq;
			double idf = Math.log(corpusSize /( (double) documentFrequency.get(f)));

			tfidf[i] = tf * idf;
			i++;
		}		

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

		int i = 0;
		for(String f : allFeatures) {
			vector[i] = new Double(Collections.frequency(docFeatures, f));			
			i++;
		}

		
		return vector;
	}

}
