import org.jsoup.nodes.Element;

import java.io.IOException;

public class LinkCountProvider extends MetricProvider {

    public LinkCountProvider(HtmlGrabber baseGrabber) {
        super(baseGrabber, "LinkCount");
    }

    @Override
    public int calc() {
        return baseGrabber.getLinkCount();
    }

    /**
     * Starts & collects the metric values for subsequent pages and returns the value.
     * TODO: Generalize this method as only the type of the "referencePageProvider" changes between different metric classes.
     * TODO: handle relative page references and anchors
     * @return Total sum of words for all subsequent pages.
     */
    @Override
    public int recursiveCalc() {
        int metricValueTotal = 0;

        LinkCountProvider referencePageProvider;
        HtmlGrabber referencePageGrabber;

        for(Element page : baseGrabber.getLinks()) {
            referencePageGrabber = new HtmlGrabber(page.attr("href"), baseGrabber.getBrokenLinkDepth()-1);

            if(!referencePageGrabber.isBroken()) {
                referencePageProvider = new LinkCountProvider(referencePageGrabber);
                metricValueTotal += referencePageProvider.getMetric();
            }
        }
        return metricValueTotal;
    }
}
