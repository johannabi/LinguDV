package preprocessing.featureselection;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import data.Article;
import io.IO;

/**
 * Abstrakte Klasse, deren Unterklassen
 *  ein Bag of Words für einen Artikel erstellt
 * @author Johanna Binnewitt
 *
 */
public abstract class AbstractFeatureSelector {
	
	/**
	 * zählt, in wie vielen Dokumenten ein Merkmal vorkommt
	 */
	protected Map<String, Integer> documentFreqs;

	/**
	 * Set, das alle Funktionswörter aus einer .txt-Datei enthält
	 */
	protected Set<String> stopwords;
	
	
	

	
	public AbstractFeatureSelector(boolean stopwordFilter, String language) {

		this.documentFreqs = new HashMap<String, Integer>();

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
	
	/**
	 * entfernt Merkmale aus dem Bag of Words, die nur in wenigen 
	 * Dokumenten vorkommen
	 * @param a
	 * @param limit Anzahl der Dokumente, in denen ein Merkmal mindestens auftauchen muss
	 */
	public void deleteRareFeatures(Article a, int limit) {
		//TODO JB: entwickle eine Methode, die Merkmale, die in zu wenigen
		//Dokumenten vorkommen (weniger als limit), ausschließt
		
		// Tipp: die Map documentFreqs enthält bereits alle
		// Dokumentenfrequenzen der einzelnen Merkmale

		
	}
	
	protected void updateDocumentFrequencies(List<String> features) {
		Set<String> uniqueFeatures = new HashSet<String>(features);
		
		for(String f : uniqueFeatures) {
			Integer freq = documentFreqs.get(f);
			if (freq == null)
				freq = 0;
			documentFreqs.put(f, freq+1);
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
