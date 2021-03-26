package assignment1.metrics;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import assignment1.HtmlGrabber;

public class WordCountProvider implements MetricCalcStrategy {

    /**
     * Counts the number of words on a web page.
     * @return the number of words counted
     */
    @Override
    public int calc(HtmlGrabber grabber) {
        Document htmlWithoutTitle = this.removeHtmlElementsBySelector(grabber.getGrabbedHtml(), "title");
        String strippedText = this.preProcessText(htmlWithoutTitle.text());
        String[] words = strippedText.split(" ");

        return words.length;
    }

    @Override
    public String getMetricLabel() {
        return "Number of words: ";
    }

    @Override
    public MetricCalcStrategy buildConcreteProvider() {
        return new WordCountProvider();
    }


    /**
     * Pre-processes text for word-counting by
     * - replacing all non-alphanumeric characters with a space
     * - replace all whitespace-chars with a single space
     * @param text the text to be processed
     * @return the text with removed tokens and whitespace
     */
    private String preProcessText(String text) {
        // TODO: find regex that works for all non-english letter (e.g. scandinavian or balkan-languages, japanese ...)
        text = text.replaceAll("[^a-zA-zöüäÖÜÄß]", " ");
        text = text.trim();
        text = text.replaceAll(" +", " ");

        return text;
    }

    /**
     * Removes all nodes that match the given selector and returns a copy of the document.
     * @param doc document to be removed from
     * @param selector css-selector to match nodes
     * @return a stripped copy of the document
     */
    private Document removeHtmlElementsBySelector(Document doc, String selector) {
        Document strippedDoc = doc.clone();
        Elements matchedElements = strippedDoc.select(selector);

        for(Element element : matchedElements) {
            element.remove();
        }

        return strippedDoc;
    }
}
