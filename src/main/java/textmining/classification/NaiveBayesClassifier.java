package textmining.classification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import data.Article;

public class NaiveBayesClassifier extends AbstractClassifier {

	//Dokumentenfrequenz von Merkmalen in der "inClass"
	private Map<String, Integer> inClassDFs;
	//Dokumentenfrequenz von Merkmalen in der "notInClass"
	private Map<String, Integer> notInClassDFs;

	//Anzahl der Artikel in der "inClass"
	private int membersInClass;
	//Anzahl der Artikel in der "notInClass"
	private int membersNotInClass;

	//Label (Kategorie) von "inClass"
	private String inClassLabel;
	//Label (Kategorie) von "notInClass"
	private String notInClassLabel;



	public NaiveBayesClassifier(String pos, String neg) {
		this.inClassLabel = pos;
		this.notInClassLabel = neg;
		
		this.inClassDFs = new HashMap<>();
		this.notInClassDFs = new HashMap<>();
		
		this.membersInClass = 0;
		this.membersNotInClass = 0;
		
	}

	@Override
	public void train(List<Article> articles) {

			for (Article article : articles) {
				//DokumentenFrequenzen werden über das Merkmal-Set
				// des Artikels aktualisiert
				Set<String> uniqueFeatures = new TreeSet<String>(article.getFeatures());
				String cat = article.getCategory();
				if (cat.equals(inClassLabel)) { //falls Artikel zu "inClass"-Kategorie gehört
					membersInClass++;

					for (String fu : uniqueFeatures) {
						int df = 0;
						if (inClassDFs.containsKey(fu)) {
							df = inClassDFs.get(fu);
						}
						inClassDFs.put(fu, df + 1);
					}
				} else { //falls Artikel zu "notInClass"-Kategorie gehört
					membersNotInClass++;

					for (String fu : uniqueFeatures) {
						int df = 0;
						if (notInClassDFs.containsKey(fu)) {
							df = notInClassDFs.get(fu);
						}
						notInClassDFs.put(fu, df + 1);
					}
				}
			}

//		printRelevantFeatures();
	}

	/**
	 * Methode soll die 20 häufigsten Merkmale (Dokumentenfrequenz) pro
	 * Kategorie in der Konsole ausgeben
	 */
	private void printRelevantFeatures() {
		
		//inClass DF
		List<Entry<String, Integer>> entries = 
				new ArrayList<Entry<String, Integer>>(
						inClassDFs.entrySet());
		Collections.sort(entries, Entry.comparingByValue());
		Collections.reverse(entries);
		System.out.println("---Merkmale Film---(von "
				+ "" + membersInClass + " Artikeln)");
		for(int i = 0; i < 20; i++) {
			System.out.println(entries.get(i));
		}
		
		//notInClassDF
		entries = 
				new ArrayList<Entry<String, Integer>>(
						notInClassDFs.entrySet());
		Collections.sort(entries, Entry.comparingByValue());
		Collections.reverse(entries);
		System.out.println("---Merkmale Literatur---(von " + membersNotInClass + " Artikeln)");
		for(int i = 0; i < 20; i++) {
			System.out.println(entries.get(i));
		}
		
	}

	@Override
	public double classify(Article article) {

		// P(Article|Class)
		double probArtClass = 1d;
		// P(Article|notClass)
		double probArtNotClass = 1d;
		
		
		for (String feature : article.getFeatures()) {
			
			//Wahrscheinlichkeit, dass das Merkmal in der Klasse auftaucht
			Integer df = inClassDFs.get(feature);
			if (df == null)
				df = 0;
			// smoothed
			double probFeatureClass = ((double) df + 1) / (membersInClass + 1);

			//Wahrscheinlichkeit, dass das Merkmal nicht in der Klasse auftaucht
			df = notInClassDFs.get(feature);
			if (df == null)
				df = 0;
			// smoothed
			double probFeatureNotClass = ((double) df + 1) / (membersNotInClass + 1);
			
			//die Wahrscheinlichkeiten P(Feature|Class) bzw P(Feauture|NotClass) werden aufmultipliziert
			probArtClass = probArtClass * probFeatureClass;
			probArtNotClass = probArtNotClass * probFeatureNotClass;

		}
		// P(class)
		double probClass = ((float) membersInClass) / (membersInClass + membersNotInClass);
		double probNotClass = ((float) membersNotInClass) / (membersInClass + membersNotInClass);

		//P(Class|Article)
		double probClassArticle = ((double) probArtClass) * probClass;
		//P(NotClass|Article)
		double probNotClassArticle = probArtNotClass * probNotClass;

		if (probClassArticle == 0.0) {
			probClassArticle = Double.MIN_VALUE;
		}
		if (probNotClassArticle == 0.0) {
			probNotClassArticle = Double.MIN_VALUE;
		}
		double rel = probClassArticle / (probClassArticle+probNotClassArticle);

		return rel;
	}

}
