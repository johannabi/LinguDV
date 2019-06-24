package textmining.classification;

import java.util.List;
import data.Article;
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
		
	}

	@Override
	public String classify(Article article) {
		// TODO JB: implementiere hier eine Methode, die
		// den als Parameter übergebenen Artikel klassifiziert
		
		// TODO JB: 1) Distanz zu jedem Artikel in trainDocs berechnen
		
		// TODO JB: 2) die k Artikel mit den kleinsten Distanzen berechnen
		
		// TODO JB: 3) unter diesen k Artikeln das häufigste Label ermitteln
		
		return "";
	}

}
