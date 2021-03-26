import assignment1.HtmlGrabber;
import assignment1.metrics.ImageCountProvider;
import assignment1.metrics.MetricProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ImageCountTests {

    // TODO: refactor tests to be beautiful

    HtmlGrabber grabber;
    MetricProvider imageCountMetric;
    final String URL_NGINX_DEFAULT_PAGE = "http://andref.xyz/";
    final String URL_FLAC = "https://de.wikipedia.org/wiki/Free_Lossless_Audio_Codec";
    final String URL_GOOGLE = "http://google.at";

    @BeforeEach
    void setupValidGrabber() {
        instantiateObjectsByUrl(URL_NGINX_DEFAULT_PAGE);
    }

    private void instantiateObjectsByUrl(String url) {
        grabber = new HtmlGrabber(url);
        imageCountMetric = new MetricProvider(grabber, new ImageCountProvider());
    }

    @Test
    void imageCountNoRecursion() {
        instantiateObjectsByUrl(URL_FLAC);
        grabber.setBrokenLinkDepth(0);
        final int expectedImages = 8;
        Assertions.assertEquals(expectedImages, imageCountMetric.getMetric());
    }

    @Test
    void imageCountNoRecursionNoImages() {
        grabber.setBrokenLinkDepth(0);
        Assertions.assertEquals(0, imageCountMetric.getMetric());
    }

    @Test
    void imageCountRecursion1() {
        instantiateObjectsByUrl(URL_GOOGLE);
        grabber.setBrokenLinkDepth(1);
        Assertions.assertEquals(556, imageCountMetric.getMetric());
    }

}
