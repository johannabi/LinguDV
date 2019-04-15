package preprocessing.featureselection;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class OpenNLPTokenizer extends AbstractFeatureSelector {

	private TokenizerME tokenizer;

	public OpenNLPTokenizer(boolean stopwordFilter, String language) {
		super(stopwordFilter, language);
		// TokenizerME arbeitet stochastisch, deshalb wird
		// ein Modell benötigt
		InputStream tModelIn;
		TokenizerModel tModel = null;
		try {
			tModelIn = new FileInputStream("src/main/resources/OpenNLP/" + language + "-token.bin");
			tModel = new TokenizerModel(tModelIn);

		} catch (IOException e) {
			e.printStackTrace();
		}
		this.tokenizer = new TokenizerME(tModel);
	}

	@Override
	public List<String> selectFeatures(String text) {
		// OpenNLP Tokenizer bekommt text übergeben
		String[] allTokens = tokenizer.tokenize(text);
		
		//TODO JB: falls ein Stopword-Filter existiert (stopwords != null),
		// soll die Methode nur Tokens zurückgeben, die nicht im 
		//Stopword-Set stehen

		return Arrays.asList(allTokens);

	}
}
