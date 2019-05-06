package preprocessing.featureselection;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	
	private Map<String, String> cleaned = new HashMap<String, String>();

	
	public AbstractFeatureSelector(boolean stopwordFilter, String language) {

		this.allFeatures = new TreeSet<String>();

		if (stopwordFilter)
			try {
				stopwords = IO.readStopwords("src/main/resources/stopwords-" + language + ".txt");
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
	
	/**
	 * normalisiert Strings, indem vorangehende und nachfolgende
	 * nicht-alphanumerische Zeichen entfernt werden und der gesamte
	 * String klein geschrieben wird.
	 * @param f zu normalisierender String
	 * @return
	 */
	public String cleanFeature(String f) {
		
		//entfernt vorangehende und nachfolgende nicht-alphanumerische Zeichen
		String c = f.replaceAll("^[^\\p{L}^\\p{N}\\s%]+|[^\\p{L}^\\p{N}\\s%]+$", "");
		c = c.toLowerCase();
		
		if(!c.equals(f))
			cleaned.put(f, c);
		
		//string muss mindestens einen lateinischen Buchstaben enthalten
		if(c.matches("[\\s\\p{L}\\p{M}&&[^\\p{Alpha}]]+")) {
			return "";
		}
		
		//Jahreszahlen werden generalisiert
		String year = "^[12][0-9]{3}$";
		Pattern p = Pattern.compile(year);
		Matcher m = p.matcher(c);
		if(m.matches())
			c = "YEAR";

		return c;
	}
	
	public void printCleanedFeatures() {
		for(Map.Entry<String, String> e : cleaned.entrySet()) {
			System.out.println(e.getKey() + " - " + e.getValue());
		}
	}
	
	@Override
	public String toString() {
		String sw_filter = "";
		if(stopwords != null)
			sw_filter = "(swFilter)";
		return this.getClass().getSimpleName() + sw_filter;
	}


}
