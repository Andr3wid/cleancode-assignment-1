public abstract class MetricProvider {

    final HtmlGrabber baseGrabber;

    public MetricProvider(HtmlGrabber baseGrabber) {
        this.baseGrabber = baseGrabber;
    }

    public abstract int getMetric();

    public abstract int calc();

    public HtmlGrabber getBaseGrabber() {
        return this.baseGrabber;
    }
}
