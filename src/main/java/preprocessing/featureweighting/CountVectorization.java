package preprocessing.featureweighting;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CountVectorization extends AbstractVectorization{

	public CountVectorization(Set<String> dictionary) {
		super(dictionary);
	}

	@Override
	public Double[] vectorize(List<String> docFeatures) {

		// TODO JB: implementiere hier eine Methode, die
		// für jedes Merkmale im Korpus eine absolute
		// Gewichtung vornimmt.
		
		// Double[] vector ist der (noch) leere Gewichtungsvektor
		Double[] vector = new Double[allFeatures.size()];
		
		// TODO JB: bestimme für jedes Merkmal in allFeatures die
		// absolute Häufigkeit im aktuellen Dokument/Bag of Words

		
		return vector;
	}

}
