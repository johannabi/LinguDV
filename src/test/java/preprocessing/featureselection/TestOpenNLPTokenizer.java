package preprocessing.featureselection;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import preprocessing.featureselection.AbstractFeatureSelector;
import preprocessing.featureselection.OpenNLPTokenizer;

public class TestOpenNLPTokenizer {

	@Test
	public void testOpenNLPTokenizer() {
		String sentence = "Bis ins Mittelalter wurde der Bergbau nur in geringem Umfang ausgeübt. Es gab relativ wenig Bergwerke, die den jeweiligen Landesherrn unterstanden.";
		AbstractFeatureSelector fs = new OpenNLPTokenizer(false, "de");
		List<String> tokens = fs.selectFeatures(sentence);
		String result = tokens.toString();
		String expected = "[Bis, ins, Mittelalter, wurde, der, Bergbau, nur, in, geringem, Umfang, ausgeübt, ., Es, gab, relativ, wenig, Bergwerke, ,, die, den, jeweiligen, Landesherrn, unterstanden, .]";
		System.out.println(tokens);
		//vergleicht die erhaltene Liste mit der erwünschten Liste
		assertEquals(expected, result);
			
	}
	


}
