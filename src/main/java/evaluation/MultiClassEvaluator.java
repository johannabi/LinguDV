package evaluation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import data.Article;
import textmining.classification.AbstractClassifier;

public class MultiClassEvaluator {
	
	private double fold = 5.0;
	private List<List<Article>> groups = new ArrayList<List<Article>>();

	public MultiClassEvaluator(int fold) {
		this.fold = fold;

	}

	/**
	 * evaluiert die Güte des übergebenen Klassifikator. Benutzt dazu eine
	 * Kreuzvalidierung
	 * 
	 * @param allDocs    alle bekannten Dokumente
	 * @param classifier Klassifikator (Naive Bayes oder kNN)
	 * @param config
	 * @return
	 */
	public void evaluate(List<Article> allDocs, AbstractClassifier classifier) {

		// Anzahl der Dokumente pro Gruppe (aufgerundet)
		int groupSize = (int) Math.ceil(allDocs.size() / fold);

		// Dokumente werden zufällig sortiert und auf die Gruppen verteilt
		Collections.shuffle(allDocs, new Random(0));
		List<Article> group = new ArrayList<Article>();
		int limit = groupSize - 1;
		for (int i = 0; i < allDocs.size(); i++) {
			Article doc = allDocs.get(i);
			if (i < limit)
				group.add(doc);
			else {
				limit += groupSize;
				groups.add(group);
				group = new ArrayList<Article>();
			}

		}
		if (limit > allDocs.size())
			groups.add(group);

		// Confusion matrix wird für die Ergebnisse vorbereitet
		Set<String> labelSet = new HashSet<String>();
		for (Article doc : allDocs) {
			labelSet.add(doc.getCategory());
		}

		List<String> labels = new ArrayList<String>(labelSet);
		Integer[][] confusionmatrix = new Integer[labels.size()][labels.size()];

		
		// für jede Gruppe wird die trainingsmenge gebildet und klassifiziert
		for (List<Article> testDocs : groups) {
			List<Article> trainDocs = new ArrayList<Article>(allDocs);
			trainDocs.removeAll(testDocs);
			// Modell bauen ...
			classifier.train(trainDocs);

			for (Article d : testDocs) {
				// ... und klassifizieren
				String classified = classifier.classify(d);
				String actual = d.getCategory();

				// tatsächliches und klassifiziertes Label vergleichen
				int classifiedIndex = labels.indexOf(classified);
				int actualIndex = labels.indexOf(actual);

				Integer count = confusionmatrix[actualIndex][classifiedIndex];
				if (count == null)
					count = 0;
				confusionmatrix[actualIndex][classifiedIndex] = count + 1;
			}
		}

		/*
		 * Zeile: Tatsächliches Label Spalte: Vorhergesagtes Label
		 */
		System.out.println("Confusion Matrix:");
		System.out.print("\t");
		for(int i = 0; i < confusionmatrix.length; i++) { //Kopfzeile
			System.out.print(labels.get(i) + "\t");
		}
		System.out.println();
		for (int i = 0; i < confusionmatrix.length; i++) {
			System.out.print(labels.get(i) + "\t");
			for (int j = 0; j < confusionmatrix[i].length; j++) {
				if(confusionmatrix[i][j] == null)
					confusionmatrix[i][j] = 0;
				System.out.print(confusionmatrix[i][j] + "\t");
			}
			System.out.println();
		}
		System.out.println();

		int allDocSize = allDocs.size();

		// Evaluationsmaße für jedes Label
		for (int i = 0; i < labels.size(); i++) {
			String label = labels.get(i);
			
			System.out.println("Label " + label + ":");
			int tp = confusionmatrix[i][i];
			// Zeile wird addiert
			int actualCount = 0;
			for (int j = 0; j < confusionmatrix[i].length; j++) {
				actualCount += confusionmatrix[i][j];
			}
			System.out.println("Tatsächlich: " + actualCount);
			int fn = actualCount - tp;

			// Spalte wird addiert
			int classifiedCount = 0;
			for (int j = 0; j < confusionmatrix.length; j++) {
				classifiedCount += confusionmatrix[j][i];
			}
			System.out.println("Klassifiziert: " + classifiedCount);
			int fp = classifiedCount - tp;

			int tn = allDocSize - (tp + fn + fp);

			System.out.println("TP=" + tp + " - FP=" + fp + " - FN=" + fn + " - TN=" + tn);
			double prec = tp / (double) (fp + tp);
			double rec = tp / (double) (fn + tp);
			double f1 = 2 * prec * rec / (prec + rec);
			double acc = (tp + tn) / (double) (tp + fp + fn + tn);
			System.out.println("Accuracy=" + acc + "-- Precision=" + prec + " -- Recall=" + rec + " -- F1=" + f1 + "\n");

		}

		System.out.println("---------------------------");
	}

}
