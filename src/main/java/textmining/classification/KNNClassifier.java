package textmining.classification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import data.Article;
import preprocessing.similarity.Distance;
import preprocessing.similarity.DistanceType;

/**
 * diese Klasse implementiert den k-nearest-neighbors Algorithmus
 * @author Johanna Binnewitt
 *
 */
public class KNNClassifier extends AbstractClassifier{
	
	//Anzahl der nächsten Nachbarn
	private int k = 5;
	
	//Enum, das speichert, welche Distanz-Berechnung
	//genutzt werden soll
	private DistanceType distanceType = DistanceType.EUCLID;
	
	//Trainings-Artikel, aus denen die k nächsten Nachbarn
	// ermittelt werden sollen
	private List<Article> trainDocs = null;
	
	/**
	 * default-Konstruktor
	 */
	public KNNClassifier() {
		
	}
	
	/**
	 * Konstruktor zum Anpassen von k und der
	 * Distanz-Berechnung
	 * @param k
	 * @param distanceType
	 */
	public KNNClassifier(int k, DistanceType distanceType) {
		this.k = k;
		this.distanceType = distanceType;
	}

	@Override
	public void train(List<Article> articles) {
		// TODO JB: implementiere hier eine Methode, die
		// das Modell trainiert
		// Tipp: kNN ist ein Lazy Learner. Ihr müsst die 
		// Daten an dieser Stelle also nicht großartig verarbeiten
		
		//da kNN als lazy learner gilt, werden die Trainingsdokumente nicht wie bei
		//Naive Bayes abstrahiert. Deshalb schreiben wir die Liste der Trainingsdokumente
		//einfach auf ein Klassenattribute, um in classify() darauf zugreifen zu können
		this.trainDocs = articles;
	}

	@Override
	public String classify(Article article) {
		// TODO JB: implementiere hier eine Methode, die
		// den als Parameter übergebenen Artikel klassifiziert
		
		
		// TODO JB: 1) Distanz zu jedem Artikel in trainDocs berechnen
		Map<Article, Double> distances = new HashMap<Article, Double>();
		for (Article trainArticle : trainDocs) {
			
			Double[] trainVec = trainArticle.getWeightVector();
			Double[] classifyVec = article.getWeightVector();
			
			double distance = Double.MAX_VALUE;
			switch (distanceType) {
			case EUCLID:
				distance = Distance.computeEuclideanDistance(trainVec, classifyVec);
				break;
			case COSINE:
				distance = 1d - Distance.computeCosineSimilarity(trainVec, classifyVec);
			default:
				break;
			}
			distances.put(trainArticle, distance);
		}		
		
		// TODO JB: 2) die k Artikel mit den kleinsten Distanzen berechnen
		List<Entry<Article, Double>> distanceList = new ArrayList<>(distances.entrySet());
		// Distanzen werden aufsteigend sortiert
		distanceList.sort(Entry.comparingByValue());
		// zählt welches Label unter den k Nachbarn wie häufig auftaucht
		Map<String, Integer> nearestLabels = new HashMap<String, Integer>();
		for(int i = 0; i < k; i++) {
			Article neighbor = distanceList.get(i).getKey();
			String label = neighbor.getCategory();
			
			int freq = 0;
			if (nearestLabels.containsKey(label))
				freq = nearestLabels.get(label);
			nearestLabels.put(label, freq+1);
		}

		// TODO JB: 3) unter diesen k Artikeln das häufigste Label ermitteln
		int max = 0;
		String maxLabel = "";
		
		for(Map.Entry<String, Integer> e : nearestLabels.entrySet()) {
			int currentFreq = e.getValue();
			String currentLabel = e.getKey();
			
			if(currentFreq > max) {
				max = currentFreq;
				maxLabel = currentLabel;
			}
		}
		
		String classified = maxLabel;
		return classified;
	}

}
