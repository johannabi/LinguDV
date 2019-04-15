package preprocessing;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import preprocessing.featureselection.AbstractFeatureSelector;
import preprocessing.featureselection.OpenNLPTokenizer;

public class TestStopwordFilter {

	@Test
	public void test() {
		String sentence = "Bis ins Mittelalter wurde der Bergbau nur in geringem Umfang ausgeübt.";
		AbstractFeatureSelector fs = new OpenNLPTokenizer(true, "de");
		List<String> tokens = fs.selectFeatures(sentence);
		System.out.println("Result: " + tokens);
		String result = "";
		if (tokens != null)
			result = tokens.toString();
		
		String expected = "[Mittelalter, Bergbau, geringem, Umfang, ausgeübt, .]";
		//vergleicht die erhaltene Liste mit der erwünschten Liste
		assertEquals(expected, result);
	}

}
