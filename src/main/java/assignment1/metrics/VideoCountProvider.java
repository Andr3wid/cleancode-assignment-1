package assignment1.metrics;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import assignment1.HtmlGrabber;

public class VideoCountProvider implements MetricCalcStrategy {

    public int calc(HtmlGrabber grabber) {
        // TODO: Check if iframes should be included as well because they can reference videos
        // also note that only HTML gets parsed, YouTube for example generates the HTML for video with Javascript
        // which is the reason why youtube-videos are not visible with this type of grabbing
        Elements video = grabber.getGrabbedHtml().body().select("video");
        return video.toArray().length;
    }

    @Override
    public String getMetricLabel() {
        return "Number of video-elements: ";
    }

    @Override
    public MetricCalcStrategy buildConcreteProvider() {
        return new VideoCountProvider();
    }


}
