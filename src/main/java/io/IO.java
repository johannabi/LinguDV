package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import data.Article;

public class IO {
	

	public static Set<String> readStopwords(String path) throws IOException {
		// TODO JB: entwickle eine Methode, die alle Füllwörter aus der Datei
		// unter dem gegebenen Pfad liest und auf ein Set<String> schreibt
		return null;
	}

	/**
	 * Methode liest alle .txt-Dateien mit Artikeln ein, die sich im übergebenen 
	 * Verzeichnis befinden und schreibt diese auf die übergebene List<Article>
	 * @param rootPath
	 * @param articles
	 * @return
	 * @throws IOException
	 */
	public static List<Article> readArticles(String rootPath, List<Article> articles) throws IOException {

		File rootDir = new File(rootPath);
		String category = rootDir.getName();

		File[] dirs = rootDir.listFiles();
		for (int i = 0; i < dirs.length; i++) {
			
			if(dirs[i].isDirectory()) {
				readArticles(dirs[i].getAbsolutePath(), articles);
			} else {				
				articles.add(readArticle(dirs[i], category));
			}	
		}

		return articles;
	}
	
	private static Article readArticle(File file, String category) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		StringBuilder sb = new StringBuilder();
		String line = "";
		String url = br.readLine(); // 1. Zeile enthält URL
		String subCategory = br.readLine(); // 2. Zeile enthält (Sub)Kategorie
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		br.close();
		return new Article(sb.toString(), file.getName(), url, category, subCategory);
	}

	/**
	 * die gecrawlten Artikel können mit dieser Methode in .txt-Dateien gespeichert
	 * werden. Der jeweilige Ordnername bezeichnet die Kategorie des Artikels
	 * @param path
	 * @param articles
	 * @throws IOException
	 */
	public static void exportArticles(String path, Set<Article> articles) throws IOException {
		
		
		System.out.println(articles.size() + " Artikel werden exportiert");
		File data = new File(path);
		data.mkdirs();

		if (data.list() != null) //Verzeichnis wird zu Beginn bereinigt
			FileUtils.cleanDirectory(data);

		for (Article a : articles) {
			String category = a.getCategory();
			File catDir = new File(data.getAbsolutePath() + "/" + category);
			catDir.mkdirs();

			// url und Artikelinhalt werden in die .txt-Datei geschrieben
			String content = a.getContent();
			String url = a.getUrl();
			String subCategory = a.getSubCategory();
			StringBuilder sb = new StringBuilder();
			sb.append(url + "\n" + subCategory + "\n\n");
			sb.append(content + "\n");
			// Sonderzeichen werden aus dem Dateinamen entfernt
			String title = a.getTitle().replaceAll("[^\\w]+", ""); 
			
			File export = new File(catDir.getAbsolutePath() + "/" + title + ".txt");
			if (!export.exists())
				export.createNewFile();
			FileWriter fw = new FileWriter(export);
			fw.write(sb.toString());
			fw.close();
		}
	}

	/**
	 * Methode exportiert das Wörterbuch in eine .txt-Datei
	 * @param allFeatures
	 */
	public static void exportAllFeatures(Set<String> allFeatures) {
		String filePath = "allFeatures.txt";
		//TODO JB: schreibe eine Methode, die unser Merkmal-Wörterbuch
		// in eine .txt-Datei schreibt

	}

}
