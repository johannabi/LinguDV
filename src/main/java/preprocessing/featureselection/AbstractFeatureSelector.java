package preprocessing.featureselection;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import io.IO;

/**
 * Abstrakte Klasse, deren Unterklassen
 *  ein Bag of Words für einen Artikel erstellt
 * @author Johanna Binnewitt
 *
 */
public abstract class AbstractFeatureSelector {

	/**
	 * enthält alle Merkmale, die im Korpus auftauchen
	 */
	protected Set<String> allFeatures;

	/**
	 * Set, das alle Funktionswörter aus einer .txt-Datei enthält
	 */
	protected Set<String> stopwords;

	
	public AbstractFeatureSelector(boolean stopwordFilter, String language) {

		this.allFeatures = new TreeSet<String>();

		if (stopwordFilter)
			try {
				stopwords = IO.readStopwords("src/main/resources/stopwords_" + language + ".txt");
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	

	/**
	 * wählt aus dem übergebenen Text die darin enthaltenen Merkmale aus
	 * @param text Text, in dem Merkmale gesucht werden sollen
	 * @return Bag of Words
	 */
	public abstract List<String> selectFeatures(String text) ;

	public Set<String> getStopwords() {
		return stopwords;
	}

	public Set<String> getAllFeatures() {
		return allFeatures;
	}
	
	@Override
	public String toString() {
		String sw_filter = "";
		if(stopwords != null)
			sw_filter = "(swFilter)";
		return this.getClass().getSimpleName() + sw_filter;
	}


}
