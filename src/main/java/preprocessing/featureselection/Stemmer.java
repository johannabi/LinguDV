package preprocessing.featureselection;

import java.util.List;


public class Stemmer extends AbstractFeatureSelector {
	
	OpenNLPTokenizer tokenizer;

	

	public Stemmer(boolean stopwordFilter, String language) {
		super(stopwordFilter, language);
		this.tokenizer = new OpenNLPTokenizer(stopwordFilter, language);
	}

	@Override
	public List<String> selectFeatures(String text) {
		// TODO JB: tokenisiere den Text mithilfe des Tokenizers und reduziere die
		// entstandenen Tokens auf den Wortstamm (mithilfe des Stemmers)
		return null;
	}
	
	

}
