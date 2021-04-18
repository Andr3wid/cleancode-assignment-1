package assignment1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

public class WebpageAnalyzerReport {
    private StringBuilder content;
    private File reportFile;
    private FileWriter reportFileWriter;

    public WebpageAnalyzerReport(String filePath) {
        content = new StringBuilder();
        reportFile = new File(filePath);
    }

    public WebpageAnalyzerReport() {
        this(WebpageAnalyzerReport.generateDateBasedFilename());
    }

    /**
     * Writes a report with the given content to disk
     */
    public void writeReport() throws IOException {
        reportFileWriter = new FileWriter(this.reportFile);
        reportFileWriter.write(content.toString());
        reportFileWriter.close();
    }

    private static String generateDateBasedFilename() {
        String filePrefix = "crawl-report_";
        String filePostfix = ".log";
        String timestamp = new Timestamp(System.currentTimeMillis()).toString();

        return filePrefix + timestamp + filePostfix;
    }

    public void addToReport(String contentToAdd) {
        this.content.append(contentToAdd);
    }

    public String getReportContent() {
        return this.content.toString();
    }

    public String getFileName() {
        return this.reportFile.getName();
    }
}
