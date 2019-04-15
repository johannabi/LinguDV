package preprocessing.featureselection;

import java.util.List;

import org.tartarus.snowball.SnowballProgram;
import org.tartarus.snowball.ext.EnglishStemmer;
import org.tartarus.snowball.ext.German2Stemmer;

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
		// TODO JB: tokenisiere den Text mithilfe des Tokenizers und reduziere die
		// entstandenen Tokens auf den Wortstamm (mithilfe des Stemmers)
		return null;
	}
	
	

}
