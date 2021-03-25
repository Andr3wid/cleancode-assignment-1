import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class VideoCountProvider extends MetricProvider {

    public VideoCountProvider(HtmlGrabber baseGrabber) {
        super(baseGrabber, "VideoCount");
    }

    @Override
    public int calc() {
        // TODO: Check if iframes should be included as well because they can reference videos
        // also note that only HTML gets parsed, YouTube for example generates the HTML for video with Javascript
        // which is the reason why youtube-videos are not visible with this type of grabbing
        Elements video = baseGrabber.getGrabbedHtml().body().select("video");
        return video.toArray().length;
    }

    /**
     * Starts & collects the metric values for subsequent pages and returns the value.
     * TODO: Generalize this method as only the type of the "referencePageProvider" changes between different metric classes.
     * TODO: Handle relative page references and anchors
     * @return Total sum of words for all subsequent pages.
     */
    @Override
    public int recursiveCalc() {
        int metricValueTotal = 0;

        VideoCountProvider referencePageProvider;
        HtmlGrabber referencePageGrabber;

        for(Element page : baseGrabber.getLinks()) {
            referencePageGrabber = new HtmlGrabber(page.attr("href"), baseGrabber.getBrokenLinkDepth()-1);

            if(!referencePageGrabber.isBroken()) {
                referencePageProvider = new VideoCountProvider(referencePageGrabber);
                metricValueTotal += referencePageProvider.getMetric();
            }
        }
        return metricValueTotal;
    }
}
