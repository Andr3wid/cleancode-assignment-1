package assignment1.metrics;

import assignment1.HtmlGrabber;

public interface MetricCalcStrategy {
    public int calc(HtmlGrabber grabber);
    public String getMetricLabel();
    public MetricCalcStrategy buildConcreteProvider();
}
