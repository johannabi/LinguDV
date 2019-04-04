package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import data.Article;

public class IO {
	

	public static Set<String> readStopwords(String path) throws IOException {
		// liest die Datei stopwords.txt ein, lege jedes enthaltene Wort
		// auf ein Set und gib dieses Set zurück

		// Set, das mit stopwords gefüllt und zurückgegeben wird
		Set<String> stopwords = new HashSet<String>();

		BufferedReader br = new BufferedReader(new FileReader(new File(path)));
		String line = "";
		while ((line = br.readLine()) != null) {
			stopwords.add(line);
		}
		br.close();
		return stopwords;
	}

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
	
	public static Article readArticle(File file, String category) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		StringBuilder sb = new StringBuilder();
		String line = "";
		String url = br.readLine();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		br.close();
		return new Article(sb.toString(), file.getName(), url, category, "");
	}

	public static void exportArticles(String path, List<Article> articles) throws IOException {
		File data = new File(path);
		data.mkdirs();

		if (data.list() != null)
			FileUtils.cleanDirectory(data);

		for (Article a : articles) {
			String label = a.getCategory();
			String content = a.getContent();
			String url = a.getUrl();
			File cat = new File(data.getAbsolutePath() + "/" + label);
			cat.mkdirs();

			StringBuilder sb = new StringBuilder();
			sb.append(url + "\n\n");
			sb.append(content + "\n");
			String fileName = a.getTitle().replaceAll("[\\/\\s:]+", "");
			File export = new File(cat.getAbsolutePath() + "/" + fileName + ".txt");

			if (!export.exists())
				export.createNewFile();
			FileWriter fw = new FileWriter(export);
			fw.write(sb.toString());
			fw.close();
		}
	}

	public static void exportAllFeatures(String dataType, Set<String> allFeatures) {
		StringBuilder sb = new StringBuilder();
		for (String f : allFeatures) {
			sb.append(f + "\n");
		}
		try {
			FileWriter fw = new FileWriter(new File(dataType + "_allFeatures.txt"));
			fw.write(sb.toString());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
