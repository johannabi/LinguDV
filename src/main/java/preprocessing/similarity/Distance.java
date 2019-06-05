package preprocessing.similarity;

public class Distance {
	
	public static double computeEuclideanDistance(Double[] vec1, Double[] vec2) {
		
		//TODO JB: implementiere hier eine Methode, die
		// die euklidische Distanz zwischen den übergebenen
		// Vektoren (Arrays) berechnet
		
		return 0d;
	}
	
	public static double computeCosineSimilarity(Double[] vec1, Double[] vec2) {
		
		double dotProduct = 0.0; //speichert das Skalarprodukt
		double vec1Sum = 0.0; //speichert den Betrag von vec1 (quadriert)
		double vec2Sum = 0.0; //speichert den Betrag von vec2 (quadriert)
		for(int i = 0; i < vec1.length; i++) {
			dotProduct+= (vec1[i] * vec2[i]);
			vec1Sum += Math.pow(vec1[i], 2.0);
			vec2Sum += Math.pow(vec2[i], 2.0);
		}
		
		if(vec1Sum == 0.0)
			vec1Sum = Double.MIN_VALUE;
		if(vec2Sum == 0.0)
			vec2Sum = Double.MIN_VALUE;
		
		double cosSim = dotProduct / (Math.sqrt(vec1Sum) * Math.sqrt(vec2Sum));
		
		return cosSim;
	}

}
