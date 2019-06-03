package preprocessing.featureselection;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import preprocessing.featureselection.AbstractFeatureSelector;
import preprocessing.featureselection.Stemmer;

public class TestStemmer {

	@Test
	public void test() {
		String sentence = "Bis ins Mittelalter wurde der Bergbau nur in geringem Umfang ausgeübt.";
		AbstractFeatureSelector fs = new Stemmer(false, "de");
		List<String> tokens = fs.selectFeatures(sentence);
		System.out.println("Result: " + tokens);
		String result = "";
		if (tokens != null)
			result = tokens.toString();
		
		String expected = "[Bis, ins, Mittelalt, wurd, der, Bergbau, nur, in, gering, umfang, ausgeubt, .]";
		//vergleicht die erhaltene Liste mit der erwünschten Liste
		assertEquals(expected, result);
	}

}
