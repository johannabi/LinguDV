package preprocessing.featureweighting;

import java.util.List;
import java.util.Set;

/**
 * Java-Klasse, die die Term-Dokument-Relation durch die
 * Anwesenheit bzw. Abwesenheits eines Merkmals im Dokument gewichtet
 * @author Johanna Binnewitt
 *
 */
public class BinaryVectorization extends AbstractVectorization {

	public BinaryVectorization(Set<String> dictionary) {
		super(dictionary);
		
	}

	@Override
	public Double[] vectorize(List<String> docFeatures) {
		Double[] vector = new Double[allFeatures.size()];
		
		int i = 0;
		for(String f : allFeatures) {			
			if (docFeatures.contains(f)) {
				vector[i] = 1.0;
			} else {
				vector[i] = 0.0;
			}
			i++;
		}

		return vector;
	}

}
