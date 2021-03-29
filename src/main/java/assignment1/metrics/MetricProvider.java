package assignment1.metrics;

import assignment1.HtmlGrabber;
import org.jsoup.nodes.Element;

public class MetricProvider {

    final HtmlGrabber baseGrabber;
    private MetricCalcStrategy metricCalculationStrategy;

    public MetricProvider(HtmlGrabber baseGrabber, MetricCalcStrategy metricCalculationStrategy) {
        this.baseGrabber = baseGrabber;
        this.metricCalculationStrategy = metricCalculationStrategy;
    }

    /**
     * Recursively calculates a certain metric for a grabbed page.
     * The calculation-strategy is defined in the "calc" method that gets implemented
     * by each explicit class that extends assignment1.metrics.MetricProvider.
     * @return The total metric with respect to the recursion counter.
     */
    public int getMetric() {
        if(baseGrabber.getBrokenLinkDepth() == 0) {
            return this.metricCalculationStrategy.calc(this.baseGrabber);
        } else {
            // TODO: Don't use cumulative calculation but treat each linked page separately.
            return this.metricCalculationStrategy.calc(this.baseGrabber) + this.recursiveCalc();
        }
    }

    /**
     * Starts & collects the metric values for subsequent pages and returns the value.
     * TODO: Handle relative page references and anchors
     * @return Total sum of words for all subsequent pages.
     */
    public int recursiveCalc() {
        int metricValueTotal = 0;

        HtmlGrabber referencePageGrabber;
        MetricCalcStrategy referencePageMetricStrategy;
        MetricProvider referencePageMetric;

        for(Element page : baseGrabber.getLinks()) {
            referencePageGrabber = new HtmlGrabber(page.attr("href"), baseGrabber.getBrokenLinkDepth()-1);

            if(!referencePageGrabber.isBroken()) {
                referencePageMetricStrategy = this.metricCalculationStrategy.buildConcreteProvider();
                referencePageMetric = new MetricProvider(referencePageGrabber, referencePageMetricStrategy);

                metricValueTotal += referencePageMetric.getMetric();
            }
        }
        return metricValueTotal;
    }

    public HtmlGrabber getBaseGrabber() {
        return this.baseGrabber;
    }

}
