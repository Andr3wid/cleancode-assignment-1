import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class WordCountProvider extends MetricProvider {

    public WordCountProvider(HtmlGrabber grabber) {
        super(grabber, "WordCount");
    }

    /**
     * Counts the number of words on a web page.
     * @return the number of words counted
     */
    @Override
    public int calc() {
        Document htmlWithoutTitle = this.removeHtmlElementsBySelector(super.getBaseGrabber().getGrabbedHtml(), "title");
        String strippedText = this.preProcessText(htmlWithoutTitle.text());
        String[] words = strippedText.split(" ");

        return words.length;
    }

    /**
     * Starts & collects the metric values for subsequent pages and returns the value.
     * TODO: Generalize this method as only the type of the "referencePageProvider" changes between different metric classes.
     * @return Total sum of words for all subsequent pages.
     */
    @Override
    public int recursiveCalc() {
        int metricValueTotal = 0;

        WordCountProvider referencePageProvider;
        HtmlGrabber referencePageGrabber;

        for(Element page : baseGrabber.getLinks()) {
            referencePageGrabber = new HtmlGrabber(page.attr("href"), baseGrabber.getBrokenLinkDepth()-1);

            if(!referencePageGrabber.isBroken()) {
                referencePageProvider = new WordCountProvider(referencePageGrabber);
                metricValueTotal += referencePageProvider.getMetric();
            }
        }
        return metricValueTotal;
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
