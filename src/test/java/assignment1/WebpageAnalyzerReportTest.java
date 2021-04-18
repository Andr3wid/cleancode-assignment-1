package assignment1;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class WebpageAnalyzerReportTest {
    private final String filepath = "build/testreport.txt";

    @Test
    public void testInstantiate(){
        WebpageAnalyzerReport webpageAnalyzerReport = new WebpageAnalyzerReport(filepath);
        assertEquals(filepath,webpageAnalyzerReport.getFileName());
    }

    @Test
    public void testAddAndGet(){
        WebpageAnalyzerReport webpageAnalyzerReport = new WebpageAnalyzerReport(filepath);
        webpageAnalyzerReport.addToReport("test");
        assertEquals("test",webpageAnalyzerReport.getReportContent());
    }

    @Test
    public void testCreateFile() throws IOException {
        WebpageAnalyzerReport webpageAnalyzerReport = new WebpageAnalyzerReport(filepath);
        webpageAnalyzerReport.addToReport("test");
        webpageAnalyzerReport.writeReport();
        String fileContent = Files.readString(Path.of(filepath));
        assertEquals("test", fileContent);
    }

}