package preprocessing.featureselection;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import is2.data.SentenceData09;
import is2.tools.Tool;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class Lemmatizer extends AbstractFeatureSelector{

	private SentenceDetectorME sentenceDetector;
	private TokenizerME tokenizer;
	private Tool lemmatizer;

	public Lemmatizer(boolean stopwordFilter, String language) {
		super(stopwordFilter, language);
		System.out.println("Initialize Lemmatizer..");

		InputStream sModelIn;
		SentenceModel sModel = null;

		InputStream tModelIn;
		TokenizerModel tModel = null;
		try {
			sModelIn = new FileInputStream("src/main/resources/OpenNLP/" + language + "-sent.bin");
			sModel = new SentenceModel(sModelIn);

			tModelIn = new FileInputStream("src/main/resources/OpenNLP/" + language + "-token.bin");
			tModel = new TokenizerModel(tModelIn);

		} catch (IOException e) {
			e.printStackTrace();
		}

		this.sentenceDetector = new SentenceDetectorME(sModel);
		this.tokenizer = new TokenizerME(tModel);
		// liest das Model für den Lemmatizer (false = Lemmata werden alle
		// kleingeschrieben)
		if (language.equals("de"))
			this.lemmatizer = new is2.lemmatizer.Lemmatizer("src/main/resources/MateTools/lemma-ger-3.6.model", false);
		else if (language.equals("en"))
			this.lemmatizer = new is2.lemmatizer.Lemmatizer(
					"src/main/resources/MateTools/CoNLL2009-ST-English-ALL.anna-3.3.lemmatizer.model", false);

	}

	/**
	 * zerlegt den Text in Sätze und tokenisiert und lemmatisiert diese Sätze
	 * anschließend
	 * 
	 * @param text
	 * @return Liste aller Lemmata im Text
	 */
	@Override
	public List<String> selectFeatures(String text) {

		// features, die von der Methode zurückgegeben werden
		List<String> features = new ArrayList<String>();

		// 1) Text mit OpenNLP in Sätze zerlegen
		String[] sentences = sentenceDetector.sentDetect(text);

		// 2) Sätze mit OpenNLP tokenisieren
		for (int i = 0; i < sentences.length; i++) {
			String[] tokens = tokenizer.tokenize(sentences[i]);

			// MateToos benötigt als erstes Token eines Satzes <root>
			String[] mateTokens = new String[tokens.length + 1];
			mateTokens[0] = "<root>";
			for (int t = 0; t < tokens.length; t++) {
				mateTokens[t + 1] = tokens[t];
			}

			// 3) Token-Sätze mit MateTools lemmatisieren
			SentenceData09 sentence = new SentenceData09();
			sentence.init(mateTokens);

			sentence = lemmatizer.apply(sentence);
			String[] currLemmas = sentence.plemmas;
			// 3a) vergleiche die enthaltenen Lemmata mit dem stopword-Set und
			// lösche die entsprechenden Funktionswörter aus der Lemmata-Liste

			// 4) alle Lemmata aus dem Text sammeln,
			// dem features-Set hinzufügen und zurückgeben (wie bei tokenize())
			for (int j = 0; j < currLemmas.length; j++) {				
				features.add(currLemmas[j]);
			}
		}
		

		return features;
	}

}
