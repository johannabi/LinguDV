package evaluation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import data.Article;
import data.LabelEvalResult;
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
		
		// für jede Gruppe wird die Trainingsmenge gebildet und klassifiziert
		for (List<Article> testDocs : groups) {
			List<Article> trainDocs = new ArrayList<Article>(allDocs);
			trainDocs.removeAll(testDocs);
			// Modell bauen ...
			classifier.train(trainDocs);

			for (Article d : testDocs) {
				// ... und klassifizieren
				String predicted = classifier.classify(d);
				String actual = d.getCategory();

				// tatsächliches und vorhergesagtes Label vergleichen
				int predictedIndex = labels.indexOf(predicted);
				int actualIndex = labels.indexOf(actual);

				// prevCount = Anzahl wie häufig diese Kombination bisher schon aufgetaucht ist
				Integer prevCount = confusionmatrix[actualIndex][predictedIndex];
				if (prevCount == null)
					prevCount = 0;
				confusionmatrix[actualIndex][predictedIndex] = prevCount + 1;
			}
		}

		System.out.println("Confusion Matrix (Zeile=Tatsächlich;Spalte=Vorhergesagt):");
		System.out.print("\t\t");
		for(int i = 0; i < confusionmatrix.length; i++) { //Kopfzeile
			System.out.print(labels.get(i).substring(0,4) + "\t\t");
		}
		System.out.println();
		for (int i = 0; i < confusionmatrix.length; i++) {
			System.out.print(labels.get(i).substring(0,4) + "\t\t");
			for (int j = 0; j < confusionmatrix[i].length; j++) {
				if(confusionmatrix[i][j] == null)
					confusionmatrix[i][j] = 0;
				System.out.print(confusionmatrix[i][j] + "\t\t");
			}
			System.out.println();
		}
		System.out.println();
		
		// Evaluationswerte ermitteln

		int allDocSize = allDocs.size();		
		List<LabelEvalResult> labelResults = new ArrayList<>();
		// Evaluationsmaße für jedes Label ermitteln
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
			int predictedCount = 0;
			for (int j = 0; j < confusionmatrix.length; j++) {
				predictedCount += confusionmatrix[j][i];
			}
			System.out.println("Vorhergesagt: " + predictedCount);
			int fp = predictedCount - tp;

			int tn = allDocSize - (tp + fn + fp);

			System.out.println("TP=" + tp + " - FP=" + fp + " - FN=" + fn + " - TN=" + tn);
			double prec = tp / (double) (fp + tp);
			double rec = tp / (double) (fn + tp);
			double f1 = 2 * prec * rec / (prec + rec);
			double acc = (tp + tn) / (double) (tp + fp + fn + tn);
			System.out.println("Accuracy=" + acc + "-- Precision=" + prec + " -- Recall=" + rec + " -- F1=" + f1 + "\n");
			labelResults.add(new LabelEvalResult(label, tp, tn, fp, fn));
		}
		
		
		//micro und macro Mittelung berechnen
		computeMacroAverages(labelResults);
		computeMicroAverages(labelResults);

		System.out.println("---------------------------");
	}
	
	
	private void computeMicroAverages(List<LabelEvalResult> labelResults) {
		int tpSum = 0;
		int fpSum = 0;
		int fnSum = 0;
		int tnSum = 0;
		//summiert alle beobachteten Verhältnisse auf
		for (LabelEvalResult result : labelResults) {
			tpSum += result.getTp();
			fpSum += result.getFp();
			fnSum += result.getFn();
			tnSum += result.getTn();
		}
		
		//bildet über die summierten Verhältnisse Precision, Recall, F1-Maß und Accuracy
		double microPrec = tpSum / (double)(tpSum + fpSum);
		double microRec = tpSum / (double)(tpSum + fnSum);
		double microF1 = 2 * microPrec * microRec / (microPrec + microRec);
		double microAcc = (tpSum + tnSum) / (double) (tpSum + fpSum + fnSum + tnSum);
		
		System.out.println("MICRO\nAccuracy=" + microAcc + "-- Precision=" 
		 + microPrec + " -- Recall=" + microRec + " -- F1=" + microF1 + "\n");
	}
	
	private void computeMacroAverages(List<LabelEvalResult> labelResults) {
		int numLabels = labelResults.size();

		double precSum = 0.0;
		double recSum = 0.0;
		double f1Sum = 0.0;
		double accSum = 0.0;

		//summiert alle Evaluationswerte auf
		for (LabelEvalResult result : labelResults) {
			precSum += result.getPrecision();
			recSum += result.getRecall();
			f1Sum += result.getF1();
			accSum += result.getAccuracy();
		}
		//bildet für jeden Evaluationswert das arithmetische Mittel
		double macroPrec = precSum / numLabels;
		double macroRec = recSum / numLabels;
		double macroF1 = f1Sum / numLabels;
		double macroAcc = accSum / numLabels;

		System.out.println("MACRO\nAccuracy=" + macroAcc + "-- Precision=" + macroPrec 
				+ " -- Recall=" + macroRec + " -- F1=" + macroF1 + "\n");
	}

}
