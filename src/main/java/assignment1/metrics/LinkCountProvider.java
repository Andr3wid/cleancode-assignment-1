package assignment1.metrics;

import org.jsoup.nodes.Element;
import assignment1.HtmlGrabber;

public class LinkCountProvider implements MetricCalcStrategy{

    @Override
    public int calc(HtmlGrabber grabber) {
        return grabber.getLinkCount();
    }

    @Override
    public String getMetricLabel() {
        return "Number of links: ";
    }

    @Override
    public MetricCalcStrategy buildConcreteProvider() {
        return new LinkCountProvider();
    }
}
