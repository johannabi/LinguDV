package preprocessing.featureselection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import io.IO;

public abstract class AbstractFeatureSelector {

	/**
	 * enthält alle Merkmale im Korpus (ehemals dictionary)
	 */
	protected Set<String> allFeatures;
	
	protected Map<String, Integer> featureCount;

	/**
	 * Set, das alle Funktionswörter aus einer .txt-Datei enthält
	 */
	protected Set<String> stopwords;

	public AbstractFeatureSelector(boolean stopwordFilter, String language) {

		this.allFeatures = new TreeSet<String>();
		this.featureCount = new HashMap<String, Integer>();

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
	 * @param trainDoc falls es sich um ein trainings-Dokument handelt, werden die
	 * Merkmale an das Korpus-Wörterbuch übergeben
	 * @return
	 */
	public abstract List<String> selectFeatures(String text, boolean trainDoc) ;


	/**
	 * trennt den Text am übergebenen regulären Ausdruck. Entfernt anführende und
	 * nachfolgende Leerzeichen
	 * 
	 * @param text
	 * @return
	 */
	public List<String> regexTokenize(String text, String regex) {
		String[] tokenArray = text.split(regex);
		List<String> tokens = new ArrayList<String>();
		for (int i = 0; i < tokenArray.length; i++) {
			String token = tokenArray[i];
			// entfernt vorangestellte/nachfolgende Leerzeichen
			token = token.trim();
			token = token.replaceAll("\u00A0", "");
			if (!token.isEmpty()) {
				tokens.add(token);
			}
		}
		return tokens;
	}

	/**
	 * bereinigt Strings, indem führende oder nachfolgende Punktuationen (z.B.
	 * Bindestriche oder Anführungszeichen) entfernt werden
	 * 
	 * @param f
	 * @return leeren String, falls f nur aus nicht-Buchstaben und nicht-Ziffern
	 *         besteht
	 */
	public String cleanFeature(String f) {

		// TODO regex url
		if (f.contains("http"))
			return "http://";

		// falls feature komplett aus Zahlen besteht
		if (f.chars().allMatch(Character::isDigit))
			return f;
		// falls im feature kein einziger Buchstabe enthalten ist
		if (f.chars().noneMatch(Character::isLetter)) {
			return "";
		}

		// falls nicht alle Zeichen im String Buchstaben sind
		// anführende bzw. schließende Punktuationen (Anführungszeichen, Bindestriche,
		// ..) werden hier
		// entfernt
		if (!f.chars().allMatch(Character::isLetter)) {

			int start = 0;
			while (Pattern.matches("[\\p{IsPunctuation}\\p{Punct}]", f.subSequence(start, start + 1))) {
				start++;
			}

			int end = f.length() - 1;
			while (Pattern.matches("[\\p{IsPunctuation}\\p{Punct}]", f.substring(end, end + 1))) {
				end--;
			}

			f = f.substring(start, end + 1);
			return f;

		}
		return f;
	}
	
	/**
	 * entfernt Merkmale, die nur selten im Korpus vorkommen
	 * @param threshold Schwellwert, in wie vielen Dokumenten ein Merkmale
	 * mindestens vorkommen muss, damit es ins Wörterbuch aufgenommen wird
	 */
	public void removeRareFeatures(int threshold) {
		
		for(Map.Entry<String, Integer> e : featureCount.entrySet()) {
			if(e.getValue() < threshold) 
				allFeatures.remove(e.getKey());
		}
	}

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
