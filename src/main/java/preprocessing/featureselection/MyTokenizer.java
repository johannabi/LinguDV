package preprocessing.featureselection;

import java.util.List;

public class MyTokenizer extends AbstractFeatureSelector {

	public MyTokenizer(boolean stopwordFilter, String language) {
		super(stopwordFilter, language);
	}

	@Override
	public List<String> selectFeatures(String text) {
		// TODO JB: tokenisiere den Text mithilfe eines regulären Ausdrucks
		// die Methode gibt alle im Text enthaltenen Wörter zurück (mit Mehrfachnennung)
		return null;
	}

}
