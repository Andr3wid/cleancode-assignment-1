package assignment1.metrics;

import assignment1.HtmlGrabber;

public abstract class MetricProvider {

    final HtmlGrabber baseGrabber;
    final String metricName;

    public MetricProvider(HtmlGrabber baseGrabber, String metricName) {
        this.baseGrabber = baseGrabber;
        this.metricName = metricName;
    }

    public abstract int calc();
    public abstract int recursiveCalc();

    /**
     * Recursively calculates a certain metric for a grabbed page.
     * The calculation-strategy is defined in the "calc" method that gets implemented
     * by each explicit class that extends assignment1.metrics.MetricProvider.
     * @return The total metric with respect to the recursion counter.
     */
    public int getMetric() {
        if(baseGrabber.getBrokenLinkDepth() == 0) {
            return this.calc();
        } else {
            return this.calc() + this.recursiveCalc();
        }
    }


    public HtmlGrabber getBaseGrabber() {
        return this.baseGrabber;
    }
}
