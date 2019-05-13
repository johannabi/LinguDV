package applications;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import data.Article;
import preprocessing.WikipediaCrawler;
import io.IO;

/**
 * Diese Application crawlt Wikipedia-Artikel aus einer bestimmten Kategorie und
 * liest ihren Inhalt aus.
 * @author Johanna Binnewitt
 *
 */
public class CrawlArticles {
	
	public static void main(String[] args) throws IOException {
		List<String> categoryLinks = new ArrayList<String>();
		// fügt Kategorien hinzu, die gesammelt werden sollen
		categoryLinks.add("Biologie");
//		categoryLinks.add("Film");
		
		int limit = 500; // Anzahl der Artikel pro Kategorie
		WikipediaCrawler wc = new WikipediaCrawler();
		
		Set<Article> articles = wc.crawl(categoryLinks, limit);
		//articles enthält nun alle gecrawlten Artikel als Article-Objekte
		
		System.out.println(articles.size() + " Artikel gefunden");

		IO.exportArticles("src/main/resources/data/Biologie", articles);
	}

}
