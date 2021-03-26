import assignment1.HtmlGrabber;
import assignment1.metrics.LinkCountProvider;
import assignment1.metrics.MetricProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LinkCountTests {

    // TODO: refactor tests to be beautiful

    HtmlGrabber grabber;
    MetricProvider linkCountMetric;
    final String URL_NGINX_DEFAULT_PAGE = "http://andref.xyz/";
    final String URL_LINKLIST = "http://andref.xyz/linklist_simple.html";
    final String URL_GOOGLE = "http://google.at";

    @BeforeEach
    void setupValidGrabber() {
        instantiateObjectsByUrl(URL_NGINX_DEFAULT_PAGE);
    }

    private void instantiateObjectsByUrl(String url) {
        grabber = new HtmlGrabber(url);
        linkCountMetric = new MetricProvider(grabber, new LinkCountProvider());
    }

    @Test
    void linkCountNoRecursion1() {
        grabber.setBrokenLinkDepth(0);
        final int expectedLinks = 2;
        Assertions.assertEquals(expectedLinks, linkCountMetric.getMetric());
    }

    @Test
    void linkCountNoRecursion2() {
        instantiateObjectsByUrl(URL_GOOGLE);
        grabber.setBrokenLinkDepth(0);
        final int expectedLinks = 17;
        Assertions.assertEquals(expectedLinks, linkCountMetric.getMetric());
    }


    @Test
    void linkCountRecursion1() {
        instantiateObjectsByUrl(URL_LINKLIST);
        grabber.setBrokenLinkDepth(1);
        final int expectedLinks = 21;
        Assertions.assertEquals(expectedLinks, linkCountMetric.getMetric());
    }

    @Test
    void linkCountRecursion2() {
        instantiateObjectsByUrl(URL_LINKLIST);
        final int expectedLinks = 1119;
        Assertions.assertEquals(expectedLinks, linkCountMetric.getMetric());
    }

    @Test
    void linkCountCircular() {
        // TODO: Check which behavior is required for circular page references.
        Assertions.fail();
    }

}
