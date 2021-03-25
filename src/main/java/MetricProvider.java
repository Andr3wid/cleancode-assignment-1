public abstract class MetricProvider {

    final HtmlGrabber baseGrabber;
    final String metricName;

    public MetricProvider(HtmlGrabber baseGrabber, String metricName) {
        this.baseGrabber = baseGrabber;
        this.metricName = metricName;
    }

    public abstract int getMetric();

    public abstract int calc();

    public HtmlGrabber getBaseGrabber() {
        return this.baseGrabber;
    }
}
