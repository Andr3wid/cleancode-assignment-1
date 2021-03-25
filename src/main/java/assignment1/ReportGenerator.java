package assignment1;

import assignment1.metrics.MetricProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Creates a textual report based on a crawled HTML-file and its statistics.
 * TODO: actually implement saving / printing operations
 */
public class ReportGenerator {
    private HashMap<String, Object> reportValues;
    private HtmlGrabber grabber;

    public ReportGenerator(HtmlGrabber grabber) {
        this.reportValues = new HashMap<>();
        this.grabber = grabber;
    }

    public void printReportToConsole() {
        System.out.print(this.generateReportString());
    }

    public void saveReportToFile(String filepath) {

    }

    public String generateReportString() {
        StringBuilder builder = new StringBuilder();

        builder.append("--- Crawler metrics for ").append(this.grabber.getUrl()).append("---\n");

        Set<String> keys = this.reportValues.keySet();
        for(String key : keys) {
            builder.append(key).append(": ").append(this.reportValues.get(key)).append("\n");
        }

        builder.append("--- Crawler report end ---");

        return builder.toString();
    }

    public void addMetricToReport(String label, Object value) {
        this.reportValues.put(label, value);
    }
}
