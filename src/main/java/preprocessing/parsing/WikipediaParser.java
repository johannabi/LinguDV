package preprocessing.parsing;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import data.Article;

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
	 * extrahiert Absätze (p-Tags) und den Titel (erstes h1-Tag), die sich unter der
	 * übergebenen Wikipedia-URL befinden
	 * 
	 * @param url          Artikel-URL
	 * @param mainCategory oberste Kategorie, die durchsucht wird
	 * @param category     konkrete Kategorie des Artikels
	 */

	public Article parse(String url, String mainCategory, String category) {
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
		if (builder.length() > 500) { // nur Artikel, die mehr als 500 Zeichen enthalten, werden aufgenommen.
			return new Article(builder.toString().trim(), title, url, mainCategory);
		}

		else
			return null;
	}

	/**
	 * sammelt alle p-Elemente des Dokuments und wählt das erste h1-Element als
	 * Titel des Artikels
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
			Pattern fnPattern = Pattern.compile(regex);
			Matcher matcher = fnPattern.matcher(elementContent);
			while (matcher.find()) {
				elementContent = elementContent.replace(matcher.group(1), "");
			}

			Pattern ipaPattern = Pattern.compile("\\[.mw-parser-output(.+)\\]");
			Matcher match = ipaPattern.matcher(elementContent);
			while (match.find()) {
				elementContent = elementContent.replace(matcher.group(), "");
//				if(!elementContent.equals(newString))
//					System.out.println(newString);
			}

			if (elementContent.length() > 0) {
				builder.append(elementContent).append("\n\n");
			}
		}
		Node sibling = node.getNextSibling();
		if (sibling != null) {
			boolean crawl = true;

			if (node.getNodeName().equals("STYLE")) {
				crawl = false;
//				System.out.println(node.getTextContent());
			}

			// prüft,ob der Knoten der Klasse "NavFrame navigation-not-searchable" angehört
			// Diese sollen nicht verarbeitet werden
			NamedNodeMap nnm = sibling.getAttributes();
			if (nnm != null) {
				Node classAttr = nnm.getNamedItem("class");
				if (classAttr != null) {
					String classVal = classAttr.getNodeValue();
					if (classVal.equals("NavFrame navigation-not-searchable"))
						crawl = false;
				}
			}

			if (crawl)
				process(sibling); // bearbeitet rekursiv die Geschwister-Knoten
		}
		Node child = node.getFirstChild();
		if (child != null) {
			boolean crawl = true;

			if (node.getNodeName().equals("STYLE")) {
				crawl = false;
//				System.out.println(node.getTextContent());
			}

			// prüft,ob der Knoten der Klasse "NavFrame navigation-not-searchable" angehört
			// Diese sollen nicht verarbeitet werden
			NamedNodeMap nnm = child.getAttributes();
			if (nnm != null) {
				Node classAttr = nnm.getNamedItem("class");
				if (classAttr != null) {
					String classVal = classAttr.getNodeValue();
					if (classVal.equals("NavFrame navigation-not-searchable"))
						crawl = false;
				}
			}
			if (crawl)
				process(child); // bearbeitet rekursiv die Kind-Knoten

		}
		if (hasTitle == false) {
			title = "ohne Titel";
		}
	}
}
