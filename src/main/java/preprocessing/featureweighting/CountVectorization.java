package preprocessing.featureweighting;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Java-Klasse, die die Term-Dokument-Relation anhand der absoluten
 * Häufigkeit eines Merkmals im Dokument berechnet
 * @author Johanna Binnewitt
 *
 */
public class CountVectorization extends AbstractVectorization{

	public CountVectorization(Set<String> dictionary) {
		super(dictionary);
	}

	@Override
	public Double[] vectorize(List<String> docFeatures) {

		
		// Double[] vector ist der (noch) leere Gewichtungsvektor
		Double[] vector = new Double[allFeatures.size()];
		
		// bestimmt für jedes Merkmal in allFeatures die
		// absolute Häufigkeit im aktuellen Dokument/Bag of Words
		int i = 0;
		for(String f : allFeatures) {
			vector[i] = new Double(Collections.frequency(docFeatures, f));			
			i++;
		}
		
		return vector;
	}

}
