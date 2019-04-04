package preprocessing.parsing;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import data.Article;

//import autoChirp.preProcessing.Document;
//import de.unihd.dbs.uima.annotator.heideltime.resources.Language;

/**
 * A Parser for Wikipedia-Urls using the org.cyberneko.html.parsers.DOMParser;
 * Creates an object of class Document, which consists of text, title, url and
 * language. This Parser appends the content of each p-element to the documents
 * text and selects the first h1-element as the documents title. The documents
 * language is read directly from the Wikipedia-Url (e.g.
 * https://de.wikipedia.org/wiki/Köln)
 * 
 * @author Alena Geduldig (adaptiert aus autoChirp)
 *
 */
public class WikipediaParser {

	private StringBuilder builder;
	private boolean hasTitle;
	private String title;
	private DOMParser domParser = new DOMParser();
	// regex for footnotes in wikipedia
	private String regex = "((\\[[0-9]+\\])+(:[0-9]+)?)";

	/**
	 * extracts the plain text (p-elements) and title (first h1-element) of the
	 * given wikipedia-url and returns an object of class Document
	 * 
	 * @param url       the url to parse
	 * @param catString
	 * @param mainCat
	 */

	public Article parse(String url, String mainCategory, String category) {
//		System.out.println(url + " -- " + mainCategory);
		try {
			domParser.setProperty("http://cyberneko.org/html/properties/default-encoding", "UTF-8");
			domParser.parse(url);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		builder = new StringBuilder();
		hasTitle = false;
		try {
			process(domParser.getDocument().getFirstChild());
		} catch (Exception e) {
			return null;
		}
		if(builder.length() > 100) {
//			System.out.println(builder.toString().substring(0, 30));
			return new Article(builder.toString().trim(), title, url, mainCategory, category);
		}
			
		else
			return null;
	}

	/**
	 * appends the content of each p-element to the documents text and selects the
	 * first h1-element as title
	 * 
	 * @param node
	 */
	private void process(Node node) {
		String elementName = node.getNodeName().toLowerCase().trim();
		// takes the content of the first h1-element as title
		if (hasTitle == false && elementName.equals("h1")) {
			title = node.getTextContent().trim();
			hasTitle = true;
		}
		// takes the content of each p-element as text
		if (elementName.equals("p")) {
			String elementContent = node.getTextContent().trim();
			// remove footnotes
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(elementContent);
			while (matcher.find()) {
				elementContent = elementContent.replace(matcher.group(1), "");
			}
			if (elementContent.length() > 0) {
				builder.append(elementContent).append("\n\n");
			}
		}
		Node sibling = node.getNextSibling();
		if (sibling != null) {
			boolean crawl = true;
			NamedNodeMap nnm = sibling.getAttributes();
			if(nnm != null) {
				Node classAttr = nnm.getNamedItem("class");
				if(classAttr != null) {
					String classVal = classAttr.getNodeValue();
					if (classVal.equals("NavFrame navigation-not-searchable"))
						crawl = false;
				}
			} 
			if(crawl)
				process(sibling);
		}
		Node child = node.getFirstChild();
		if (child != null) {
			boolean crawl = true;
			NamedNodeMap nnm = child.getAttributes();
			if(nnm != null) {
				Node classAttr = nnm.getNamedItem("class");
				if(classAttr != null) {
					String classVal = classAttr.getNodeValue();
					if (classVal.equals("NavFrame navigation-not-searchable"))
						crawl = false;
				}
			} 
			if(crawl)
				process(child);
//			Element e = (Element) child;
//			if (!e.getAttribute("class").equals("NavFrame navigation-not-searchable"))
				
		}
		if (hasTitle == false) {
			title = "ohne Titel";
		}
	}
}
