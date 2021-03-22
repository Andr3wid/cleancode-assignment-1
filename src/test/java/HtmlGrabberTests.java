import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class HtmlGrabberTests {

    HtmlGrabber grabber;
    final String TEST_URL1 = "https://www.aau.at/";

    @BeforeEach
    void setupValidGrabber() {
        try {
            grabber = new HtmlGrabber(TEST_URL1);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void invalidUrl() {
        Assertions.assertThrows(IOException.class, () -> {
            HtmlGrabber hg = new HtmlGrabber("https://idonotexist.foobar");
        });
    }

    @Test
    void testDefaultRecursionLevel() {
        Assertions.assertEquals(HtmlGrabber.BROKEN_LINK_DEPTH_DEFAULT, grabber.getBrokenLinkDepth());
    }

}
