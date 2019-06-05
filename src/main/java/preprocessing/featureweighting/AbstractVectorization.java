package preprocessing.featureweighting;

import java.util.List;
import java.util.Set;

public abstract class AbstractVectorization {
	
	protected Set<String> allFeatures = null;
	
	public AbstractVectorization(Set<String> dictionary) {
		this.allFeatures = dictionary;
	}
	
	/**
	 * vektorisiert das übergebene Bag of Words nach den Regeln der
	 * entsprechenden Unterklasse (CountVectorization, Tfidf, ...)
	 * @param docFeatures
	 * @return
	 */
	public abstract Double[] vectorize(List<String> docFeatures);
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
