package evaluation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import data.Article;
import textmining.classification.AbstractClassifier;

public class Evaluator {

	// Anzahl der Evaluationsgruppen
	private int fold = 5;
	// Evaluationsgruppen
	private List<List<Article>> groups = new ArrayList<List<Article>>();
	private String posLabel;
	private String negLabel;

	public Evaluator(String posLabel, String negLabel) {
		this.posLabel = posLabel;
		this.negLabel = negLabel;
	}

	public void evaluate(List<Article> allArticles, AbstractClassifier classifier) {

		// Anzahl der Dokumente pro Gruppe (aufgerundet)
		int groupSize = (int) Math.ceil(allArticles.size() / (double) fold);

		// Dokumente werden zufällig sortiert und auf die Gruppen verteilt
		Collections.shuffle(allArticles, new Random(0));
		List<Article> group = new ArrayList<Article>();
		int limit = groupSize - 1;
		for (int i = 0; i < allArticles.size(); i++) {
			Article doc = allArticles.get(i);
			if (i < limit)
				group.add(doc);
			else {
				limit += groupSize;
				groups.add(group);
				group = new ArrayList<Article>();
			}

		}
		if (limit > allArticles.size())
			groups.add(group);

		// alle vier möglichen Verhältnisse zwischen 
		// tatäschlichem und klassifiziertem Label
		int tp = 0;
		int fp = 0;
		int fn = 0;
		int tn = 0;

		for (List<Article> testDocs : groups) {
			List<Article> trainDocs = new ArrayList<Article>(allArticles);
			trainDocs.removeAll(testDocs);
			// Modell bauen ...
			classifier.train(trainDocs);

			for (Article article : testDocs) {
				// ... und klassifizieren
				double prob = classifier.classify(article);
				String classified = "";
				if (prob > 0.5)
					classified = posLabel;
				else
					classified = negLabel;
				String actual = article.getCategory();

				// richtiges und klassifiziertes Label vergleichen
				if (actual.equals(posLabel)) {
					if (classified.equals(posLabel)) {
						tp++;
					} else {
						fn++;
					}

				} else {
					if (classified.equals(posLabel)) {
						fp++;
					} else {
						tn++;
					}

				}
			}
		}

		System.out.println("TP: " + tp + " -- FP: " + fp + " -- FN: " + fn + " -- TN: " + tn);
		double prec = tp / (double) (fp + tp);
		double rec = tp / (double) (fn + tp);
		double f1 = 2 * prec * rec / (prec + rec);
		double acc = (tp + tn) / (double) (tp + fp + fn + tn);
		System.out.println("Accuracy=" + acc + "-- Precision=" + prec + " -- Recall=" + rec + " -- F1=" + f1 + "\n");

	}

}
