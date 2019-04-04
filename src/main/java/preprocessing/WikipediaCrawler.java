package preprocessing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import data.Article;
import preprocessing.parsing.WikipediaParser;

public class WikipediaCrawler {

	private WikipediaParser wp = null;

	Set<Article> allArticles = null;
	Set<Article> catArticles = null;

	public WikipediaCrawler() {
		this.wp = new WikipediaParser();
		this.catArticles = new HashSet<Article>();
		this.allArticles = new HashSet<Article>();

	}

	public void crawlCategory(String catURL, String mainCat, int limit) throws IOException {
		String catString = catURL.split(":")[2];
		Connection con = Jsoup.connect(catURL);
		Document doc = con.get();

		// Artikel mit dieser Kategorie scrapen
		Element mwPages = doc.getElementById("mw-pages"); // Abschnitt, der Artikel-Links enthält
		if (mwPages != null) {
			Elements articleList = mwPages.getElementsByTag("a");
			for (Element e : articleList) {
				String url = "https://de.wikipedia.org" + e.attr("href");
				System.out.println(url);
				if(url.contains("\\:")) //URL verweist auf Portal-Seite
					continue;
				Article article = wp.parse(url, mainCat, catString);
				if (article != null)
					catArticles.add(article);
				if (catArticles.size() == limit)
					return;
			}
		} 

		// weitere Unterkategorien finden
		Element mwSubcategories = doc.getElementById("mw-subcategories");
		if (mwSubcategories != null) {
			Elements subCatList = mwSubcategories.getElementsByTag("a");
			for (Element e : subCatList) {
				String url = "https://de.wikipedia.org" + e.attr("href");
				if (catArticles.size() < limit)
					crawlCategory(url, mainCat, limit);
			}
		}

	}

	public List<Article> crawl(List<String> category, int limit) throws IOException {

		for (String cat : category) {
			String url = "https://de.wikipedia.org/wiki/Kategorie:" + cat;
			crawlCategory(url, cat, limit);
			allArticles.addAll(catArticles);
			catArticles = new HashSet<Article>();
		}
		return new ArrayList<Article>(allArticles);
	}

}
