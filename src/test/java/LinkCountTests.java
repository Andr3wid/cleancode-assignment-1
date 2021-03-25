import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class LinkCountTests {

    // TODO: refactor tests to be beautiful

    HtmlGrabber grabber;
    LinkCountProvider lcp;
    final String URL_NGINX_DEFAULT_PAGE = "http://andref.xyz/";
    final String URL_LINKLIST = "http://andref.xyz/linklist_simple.html";
    final String URL_GOOGLE = "http://google.at";

    @BeforeEach
    void setupValidGrabber() {
        instantiateObjectsByUrl(URL_NGINX_DEFAULT_PAGE);
    }

    private void instantiateObjectsByUrl(String url) {
        grabber = new HtmlGrabber(url);
        lcp = new LinkCountProvider(grabber);
    }

    @Test
    void linkCountNoRecursion1() {
        grabber.setBrokenLinkDepth(0);
        final int expectedLinks = 2;
        Assertions.assertEquals(expectedLinks, lcp.getMetric());
    }

    @Test
    void linkCountNoRecursion2() {
        instantiateObjectsByUrl(URL_GOOGLE);
        grabber.setBrokenLinkDepth(0);
        final int expectedLinks = 17;
        Assertions.assertEquals(expectedLinks, lcp.getMetric());
    }


    @Test
    void linkCountRecursion1() {
        instantiateObjectsByUrl(URL_LINKLIST);
        grabber.setBrokenLinkDepth(1);
        final int expectedLinks = 21;
        Assertions.assertEquals(expectedLinks, lcp.getMetric());
    }

    @Test
    void linkCountRecursion2() {
        instantiateObjectsByUrl(URL_LINKLIST);
        final int expectedLinks = 1118;
        Assertions.assertEquals(expectedLinks, lcp.getMetric());
    }

    @Test
    void linkCountCircular() {
        // TODO: Check which behavior is required for circular page references.
        Assertions.fail();
    }

}
