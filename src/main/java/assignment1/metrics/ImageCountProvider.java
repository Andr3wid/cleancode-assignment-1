package assignment1.metrics;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import assignment1.HtmlGrabber;

public class ImageCountProvider implements MetricCalcStrategy{

    @Override
    public int calc(HtmlGrabber grabber) {
        Elements images = grabber.getGrabbedHtml().body().select("img");
        return images.toArray().length;
    }

    @Override
    public String getMetricLabel() {
        return "Number of images: ";
    }

    @Override
    public MetricCalcStrategy buildConcreteProvider() {
        return new ImageCountProvider();
    }
}
