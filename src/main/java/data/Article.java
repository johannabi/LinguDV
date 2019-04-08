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
	private String subCategory;
	/**
	 * Bag of Words-Merkmale des Artikels
	 */
	private List<String> features;
	
	public Article(String content, String title, String url, String category, String subCategory) {
		this.content = content;
		this.url = url;
		this.category = category;
		this.subCategory = subCategory;
		this.title = title;
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
	
	public String getSubCategory() {
		return subCategory;
	}
	
	public List<String> getFeatures() {
		return features;
	}
	
	public void setFeatures(List<String> features) {
		this.features = features;
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.url.equals(((Article)obj).getUrl());
	}
	
	@Override
	public int hashCode() {
		return url.hashCode();
	}
	
	

}
