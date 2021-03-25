import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ImageCountProvider extends MetricProvider {

    public ImageCountProvider(HtmlGrabber baseGrabber) {
        super(baseGrabber, "ImageCount");
    }

    @Override
    public int calc() {
        Elements images = baseGrabber.getGrabbedHtml().body().select("img");
        return images.toArray().length;
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

        ImageCountProvider referencePageProvider;
        HtmlGrabber referencePageGrabber;

        for(Element page : baseGrabber.getLinks()) {
            referencePageGrabber = new HtmlGrabber(page.attr("href"), baseGrabber.getBrokenLinkDepth()-1);

            if(!referencePageGrabber.isBroken()) {
                referencePageProvider = new ImageCountProvider(referencePageGrabber);
                metricValueTotal += referencePageProvider.getMetric();
            }
        }
        return metricValueTotal;
    }
}
