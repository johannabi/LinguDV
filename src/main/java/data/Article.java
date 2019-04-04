package data;

import java.util.List;

public class Article {
	
	private String content;
	private String title;
	private String url;
	private String category;
	private List<String> features;
	private double[] vector;
	
	public Article(String content, String url, String category, String subCategory) {
		this.content = content;
		this.url = url;
		this.category = category;
	}
	
	
	public Article(String content, String title, String url, String category, String subCategory) {
		this(content, url, category, subCategory);
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}

	public List<String> getFeatures() {
		return features;
	}
	public void setFeatures(List<String> features) {
		this.features = features;
	}
	public double[] getVector() {
		return vector;
	}
	public void setVector(double[] vector) {
		this.vector = vector;
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
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.content.equals(((Article)obj).getContent());
	}
	
	

}
