package textmining.classification;

import java.util.List;

import data.Article;

public abstract class AbstractClassifier {
	
	public abstract void train(List<Article> articles);
	
	public abstract String classify(Article article);

}
