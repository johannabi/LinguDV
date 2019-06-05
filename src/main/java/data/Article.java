package data;

import java.util.List;

/**
 * Diese Klasse repräsentiert den Inhalt eines Wikipedia-Artikels
 * @author Johanna Binnewitt
 *
 */
public class Article {
	
	/**
	 * der gescrapte Inhalt des Artikels
	 */
	private String content;
	/**
	 * der Titel des Artikels
	 */
	private String title;
	/**
	 * die ursprüngliche URL des Artikels
	 */
	private String url;
	/**
	 * die (Über-)Kategorie, zu der der Artikel
	 * gehört
	 */
	private String category;
	/**
	 * die tatsächliche Kategorie des Artikels
	 */
	private List<String> subCategories;
	/**
	 * Bag of Words-Merkmale des Artikels
	 */
	private List<String> features;
	
	private Double[] weightVector;
	
	public Article(String content, String title, String url, String category) {
		this.content = content;
		this.url = url;
		this.category = category;
//		this.subCategory = subCategory;
		this.title = title;
	}
	
	public Article(String string, String name, String url, String category, List<String> subCategories) {
		this(string, name, url, category);
		this.subCategories = subCategories;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getCategory() {
		return category;
	}
	
//	public String getSubCategory() {
//		return subCategory;
//	}
	
	public List<String> getFeatures() {
		return features;
	}
	
	public void setFeatures(List<String> features) {
		this.features = features;
	}
	
	public Double[] getWeightVector() {
		return weightVector;
	}
	
	public void setWeightVector(Double[] weightVector) {
		this.weightVector = weightVector;
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.url.equals(((Article)obj).getUrl());
	}
	
	@Override
	public int hashCode() {
		return url.hashCode();
	}

	public void setCategories(List<String> otherCats) {
		this.subCategories = otherCats;
	}
	
	public List<String> getCategories() {
		return subCategories;
	}
	
	

}
