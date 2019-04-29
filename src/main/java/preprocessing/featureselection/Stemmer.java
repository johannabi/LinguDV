package preprocessing.featureselection;

import java.util.ArrayList;
import java.util.List;

import opennlp.tools.stemmer.snowball.SnowballStemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer.ALGORITHM;


public class Stemmer extends AbstractFeatureSelector {
	
	private OpenNLPTokenizer tokenizer;
	private SnowballStemmer stemmer;

	

	public Stemmer(boolean stopwordFilter, String language) {
		super(stopwordFilter, language);
		//Um einen Text auf Wortstämme zurückzuführen, benötigen wir
		//zunächst die Tokens
		this.tokenizer = new OpenNLPTokenizer(stopwordFilter, language);
		//Abhängig von der Sprache wird ein deutscher oder englischer
		//Stemmer initialisiert
		if(language.equals("de"))
			this.stemmer = new SnowballStemmer(ALGORITHM.GERMAN);
		else if (language.equals("en"))
			this.stemmer = new SnowballStemmer(ALGORITHM.ENGLISH);
	}

	@Override
	public List<String> selectFeatures(String text) {
		// tokenisiere den Text mithilfe des Tokenizers und reduziere die
		// entstandenen Tokens auf den Wortstamm (mithilfe des Stemmers)
		
		//stemmer benötigt einzelne Tokens, deshalb wird der übergebene Text
		//zunächst tokeniziert
		List<String> tokens = tokenizer.selectFeatures(text);
		
		//leere Liste, die alle Wortstämme speichern soll
		List<String> stems = new ArrayList<String>();
		
		// jedes einzelne Token wird nach und nach gestemmt...
		for(String token : tokens) {
			String stem = (String) stemmer.stem(token);
			// ... und der stems-List hinzugefügt
			stems.add(stem);
		}
		return stems;
	}
	
	

}
