package applications;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import data.Article;
import io.IO;
import preprocessing.WikipediaCrawler;

/**
 * Diese Application crawlt Wikipedia-Artikel aus einer bestimmten Kategorie und
 * liest ihren Inhalt aus.
 * @author Johanna Binnewitt
 *
 */
public class CrawlArticles {
	
	public static void main(String[] args) throws IOException {
		List<String> categoryLinks = new ArrayList<String>();
		categoryLinks.add("Biologie");
//		categoryLinks.add("Wirtschaftswissenschaft");
//		categoryLinks.add("Film");
//		categoryLinks.add("Sport");
		categoryLinks.add("Literatur");
		
		WikipediaCrawler wc = new WikipediaCrawler();
		int limit = 20;
		
		List<Article> articles = wc.crawl(categoryLinks, limit);
		
		
		IO.exportArticles("src/main/resources/data/export", articles);
	}

}
