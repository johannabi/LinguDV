package preprocessing.featureweighting;

import java.util.List;
import java.util.Set;

import org.apache.mahout.math.stats.LogLikelihood;
import org.apache.mahout.math.stats.LogLikelihood.ScoredItem;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;

import data.Article;

/**
 * Java-Klasse, die die Term-Dokument-Relation mithilfe eines 
 * Log Likelihood Ratio ermittelt
 * @author Johanna Binnewitt
 *
 */
public class LogLikelihoodVectorization extends AbstractVectorization {

	/**
	 * enthält alle Merkmale des Corpus (Duplikate inkl.)
	 */
	private Multiset<String> corpusBoW;
	
	public LogLikelihoodVectorization(List<Article> articles, Set<String> dictionary) {
		super(dictionary);
		corpusBoW = HashMultiset.create();
		for (Article a : articles) {
			corpusBoW.addAll(a.getFeatures());
		}
	}

	@Override
	public Double[] vectorize(List<String> docFeatures) {
		Multiset<String> corWithoutDoc = HashMultiset.create(corpusBoW);

		Multiset<String> docBoW = HashMultiset.create();
		docBoW.addAll(docFeatures);
		// Multiset enthält Korpus-Merkmale ohne Doc-Merkmale
		// Bsp.: Corpus: 5 * Hund; Doc: 2 * Hund; corWithoutDoc: 3 * Hund
		Multisets.removeOccurrences(corWithoutDoc, docBoW);
		List<ScoredItem<String>> llhScore = LogLikelihood.compareFrequencies(docBoW, corWithoutDoc, docBoW.size(), 0.0);

		// Double[] vector ist der (noch) leere Gewichtungsvektor
		Double[] vector = new Double[allFeatures.size()];
		int i = 0;
		for (String feature : allFeatures) {
			double value = 0.0;

			for (ScoredItem<String> item : llhScore) {
				if (item.getItem().equals(feature)) {
					value = item.getScore();
					break;
				}
			}
			vector[i] = value;
			i++;
		}

		return vector;
	}

}
