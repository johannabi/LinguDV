package applications;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import data.Article;
import io.IO;

public class PreproArticles {
	


	public static void main(String[] args) throws IOException {

		List<Article> articles = new ArrayList<Article>();
		articles = IO.readArticles("src/main/resources/data/export", articles);
//		articles.addAll(IO.readArticles("src/main/resources/data/export/Biologie"));
		System.out.println(articles.size());
		
		articles = new ArrayList<Article>();
		articles = IO.readArticles("src/main/resources/data/export", articles);
		System.out.println(articles.size());
		List<Integer> lens = new ArrayList<Integer>();
		for (int i = 0; i < articles.size(); i++) {
			int len = articles.get(i).getContent().length();
			if (len < 100) {
				System.out.println(articles.get(i).getUrl());
				System.out.println(articles.get(i).getTitle());
			}
			lens.add(len);
		}

//		Set<Integer> distValues = new HashSet<Integer>(lens);
//		for(Integer val : distValues) {
//			System.out.println(val + "=" + Collections.frequency(lens, val));
//		}
		
//		calcHistogram(lens);
		
		System.out.println("Min=" + Collections.min(lens));
		System.out.println("Max=" + Collections.max(lens));
		
		
		
		
	}

	private static void calcHistogram(List<Integer> lens) {
		int[] count = new int[10];
		
		for(Integer l : lens) {
			if (l < 100)
				count[0]++;
			else if (l < 500)
				count[1]++;
			else if (l < 1000)
				count[2]++;
			else if (l < 1500)
				count[3]++;
			else if (l < 2000)
				count[4]++;
			else if (l < 2500)
				count[5]++;
			else if (l < 3000)
				count[6]++;
			else if (l < 4500)
				count[7]++;
			else if (l < 5000)
				count[8]++;
			else
				count[9]++;	
		}
		for(int i = 0; i < count.length; i++) {
			System.out.println(count[i]);
		}
	}





}
