package textmining.classification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import data.Article;
import data.NBClassModel;

/**
 * Naive Bayes Klassifikator für mehr als zwei Labels (Multiclass)
 * @author Johanna Binnewitt
 *
 */
public class MultiNBClassifier extends AbstractClassifier {

	// für jedes Label wird ein Modell trainiert
	private Map<String, NBClassModel> classModels = new HashMap<String, NBClassModel>();

	private List<String> labels;

	public MultiNBClassifier(List<String> labels) {
		this.labels = labels;
	}

	@Override
	public void train(List<Article> articles) {
		for (String label : labels) {

			//Dokumentenfrequenz von Merkmalen in der "inClass"
			Map<String, Integer> inClassDFs = new HashMap<String, Integer>();
			//Dokumentenfrequenz von Merkmalen in der "notInClass"
			Map<String, Integer> notInClassDFs = new HashMap<String, Integer>();

			//Anzahl der Artikel in der "inClass"
			int membersInClass = 0;
			//Anzahl der Artikel in der "notInClass"
			int membersNotInClass = 0;

			for (Article a : articles) {
				//DokumentenFrequenzen werden über das Merkmal-Set
				// des Artikels aktualisiert
				Set<String> uniqueFeatures = new TreeSet<String>();
				uniqueFeatures.addAll(a.getFeatures());
				String cat = a.getCategory();
				if (cat.equals(label)) { //falls Artikel zu "inClass"-Kategorie gehört
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

			// die berechneten Werte werden für das Label-Modell gespeichert
			NBClassModel model = new NBClassModel(inClassDFs, notInClassDFs, membersInClass, membersNotInClass);
			classModels.put(label, model);

		}

	}

	@Override
	public String classify(Article article) {
		
		// in dieser Map wird für jedes Label die Wahrscheinlichkeit gespeichert
		Map<String, Double> probs = new HashMap<String, Double>();

		// für jedes Label wird die separate Wahrscheinlichkeit berechnet
		for (Map.Entry<String, NBClassModel> e : classModels.entrySet()) {
			double prob = computeClassProb(e, article.getFeatures());
			probs.put(e.getKey(), prob);
		}

		// das Label mit der höchsten Wahrscheinlichkeit wird ausgewählt
		List<Entry<String, Double>> probsList = new ArrayList<>(probs.entrySet());
		probsList.sort(Entry.comparingByValue());
		Entry<String, Double> mostProb = probsList.get(probsList.size() - 1);

		String classified = mostProb.getKey();
		return classified;

	}

	private double computeClassProb(Entry<String, NBClassModel> e, List<String> features) {
		Map<String, Integer> inClassDFs = e.getValue().getInClassDFs();
		Map<String, Integer> notInClassDFs = e.getValue().getNotInClassDFs();

		int membersInClass = e.getValue().getMembersInClass();
		int membersNotInClass = e.getValue().getMembersNotInClass();

		// P(Article|Class)
		double probArtClass = 1d;
		// P(Article|notClass)
		double probArtNotClass = 1d;

		for (String feature : features) {

			// Wahrscheinlichkeit, dass das Merkmal in der Klasse auftaucht
			Integer df = inClassDFs.get(feature);
			if (df == null)
				df = 0;
			// smoothed (zähler und nenner wird 1 hinzugefügt, damit keiner
			// von beiden = 0 wird)
			double probFUClass = ((double) df + 1) / (membersInClass + 1);
			if (probFUClass <= 0.0) {
				System.out.println("probFUClass: " + probFUClass);
			}

			// Wahrscheinlichkeit, dass das Merkmal nicht in der Klasse auftaucht
			df = notInClassDFs.get(feature);
			if (df == null)
				df = 0;
			// smoothed (zähler und nenner wird 1 hinzugefügt, damit keiner
			// von beiden = 0 wird)
			double probFUNotClass = ((double) df + 1) / (membersNotInClass + 1);
			if (probFUNotClass == 0.0) {
				System.out.println("probFUNotInclass: " + probFUNotClass);
			}
			
			//die Wahrscheinlichkeiten P(Feature|Class) bzw P(Feauture|NotClass) werden aufmultipliziert
			probArtClass = probArtClass * probFUClass;
			probArtNotClass = probArtNotClass * probFUNotClass;

		}
		// P(Class)
		double probClass = ((float) membersInClass) / (membersInClass + membersNotInClass);
		double probNotClass = ((float) membersNotInClass) / (membersInClass + membersNotInClass);
		
		// P(Class|Article)
		double probClassArticle = ((double) probArtClass) * probClass;
		// P(NotClass|Article)
		double probNotLabelDoc = probArtNotClass * probNotClass;
		
		if (probClassArticle == 0.0) {
			probClassArticle = Double.MIN_VALUE;
		}
		if (probNotLabelDoc == 0.0) {
			probNotLabelDoc = Double.MIN_VALUE;
		}
		// die Wahrscheinlichkeit für das Label wird relativiert
		double relProbLabel = probClassArticle / probNotLabelDoc;
		return relProbLabel;
	}

}
