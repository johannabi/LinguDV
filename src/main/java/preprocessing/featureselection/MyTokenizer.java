package preprocessing.featureselection;

import java.util.Arrays;
import java.util.List;

public class MyTokenizer extends AbstractFeatureSelector {

	public MyTokenizer(boolean stopwordFilter, String language) {
		//stopwordFilter und language werden an die Super-Klasse weitergegeben
		super(stopwordFilter, language);
	}

	@Override
	public List<String> selectFeatures(String text) {
		// dieser Tokenizer trennt den übergebenen Text an allen
		// Leerzeichen (\s). Da die Methode split ein Array zurückgibt,
		// muss das Array noch in ein List-Objekt konvertiert werden.
		String[] array = text.split("\\s");
		
		return Arrays.asList(array);
	}

}
