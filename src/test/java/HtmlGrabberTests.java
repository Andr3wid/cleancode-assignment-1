import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

public class HtmlGrabberTests {

    HtmlGrabber grabber;
    final String TEST_URL_ENGLISH_SIMPLE = "http://andref.xyz/";
    final String TEST_URL_GERMAN_SIMPLE = "http://andref.xyz/german.html";

    @BeforeEach
    void setupValidGrabber() {
        grabber = new HtmlGrabber(TEST_URL_ENGLISH_SIMPLE);
    }

    @Test
    void invalidUrl() {
        grabber = new HtmlGrabber("idonotexist.bla");
        Assertions.assertNull(grabber.getGrabbedHtml());
        Assertions.assertTrue(grabber.isBroken());
    }

    @Test
    void testDefaultRecursionLevel() {
        Assertions.assertEquals(HtmlGrabber.BROKEN_LINK_DEPTH_DEFAULT, grabber.getBrokenLinkDepth());
    }

    @Test
    void testLinkCount() {
        Assertions.assertEquals(2, grabber.getLinkCount());
    }

    @Test
    void testLinkContent() {
        Elements links = grabber.getLinks();
        links.attr("href");

        ArrayList<String> hrefValuesExpected = new ArrayList<>();
        hrefValuesExpected.add("http://nginx.org/");
        hrefValuesExpected.add("http://nginx.com/");

        ArrayList<String> hrefValuesActual = new ArrayList<>();

        for (Element l : links) {
            hrefValuesActual.add(l.attr("href"));
        }

        Assertions.assertEquals(hrefValuesExpected, hrefValuesActual);
    }

}
