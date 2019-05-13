package preprocessing.featureselection;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.stemmer.snowball.SnowballStemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer.ALGORITHM;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;


public class Stemmer extends AbstractFeatureSelector {
	
	private TokenizerME tokenizer;
	private SnowballStemmer stemmer;

	

	public Stemmer(boolean stopwordFilter, String language) {
		super(stopwordFilter, language);
		//Um einen Text auf Wortstämme zurückzuführen, benötigen wir
		//zunächst die Tokens
		InputStream tModelIn;
		TokenizerModel tModel = null;
		try {
			tModelIn = new FileInputStream("src/main/resources/OpenNLP/" + language + "-token.bin");
			tModel = new TokenizerModel(tModelIn);

		} catch (IOException e) {
			e.printStackTrace();
		}
		this.tokenizer = new TokenizerME(tModel);
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
		String[] tokens = tokenizer.tokenize(text);
		
		//leere Liste, die alle Wortstämme speichern soll
		List<String> stems = new ArrayList<String>();
		
		// jedes einzelne Token wird nach und nach gestemmt...
		for(String token : tokens) {
			token = cleanFeature(token);
			if (token.isEmpty())
				continue;
			
			if(stopwords != null) {
				if(stopwords.contains(token))
					continue;
			}
			
			String stem = (String) stemmer.stem(token);
			// ... und der stems-List hinzugefügt
			stems.add(stem);
		}
		
		
		updateDocumentFrequencies(stems);
		return stems;
	}
	
	

}
