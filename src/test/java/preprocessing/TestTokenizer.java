package preprocessing;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import preprocessing.featureselection.AbstractFeatureSelector;
import preprocessing.featureselection.OpenNLPTokenizer;

public class TestTokenizer {

	@Test
	public void test() {
		String sentence = "Die Mäuse tanzen auf der Treppe.";
		AbstractFeatureSelector fs = new OpenNLPTokenizer(false, "de");
		List<String> tokens = fs.selectFeatures(sentence);
		List<String> correct = new ArrayList<String>();
		correct.add("Die");
		correct.add("Mäuse");
		correct.add("tanzen");
		correct.add("auf");
		correct.add("der");
		correct.add("Treppe");
		correct.add(".");
		System.out.println(tokens);
		assertEquals(tokens, correct);
			
	}

}
