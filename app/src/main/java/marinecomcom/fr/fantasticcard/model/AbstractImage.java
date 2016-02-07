package marinecomcom.fr.fantasticcard.model;

/**
 * Created by Technique on 07/02/16.
 */
public class AbstractImage {

    public float startXOnPdf;
    public float startYOnPdf;
    public float availableWidthOnPdf;
    public float availableHeightOnPdf;
    public float resizedWidthForPdf = 0;
    public float resizedHeightForPdf = 0;


    public AbstractImage(float startXOnPdf, float startYOnPdf, float availableWidthOnPdf, float availableHeightOnPdf) {
        this.startXOnPdf = startXOnPdf;
        this.startYOnPdf = startYOnPdf;
        this.availableWidthOnPdf = availableWidthOnPdf;
        this.availableHeightOnPdf = availableHeightOnPdf;
    }

    public float getResizedWidthForPdf() {
        return resizedWidthForPdf;
    }

    public void setResizedWidthForPdf(float resizedWidthForPdf) {
        this.resizedWidthForPdf = resizedWidthForPdf;
    }

    public float getResizedHeightForPdf() {
        return resizedHeightForPdf;
    }

    public void setResizedHeightForPdf(float resizedHeightForPdf) {
        this.resizedHeightForPdf = resizedHeightForPdf;
    }

    public float getStartXOnPdf() {
        return startXOnPdf;
    }

    public void setStartXOnPdf(float startXOnPdf) {
        this.startXOnPdf = startXOnPdf;
    }

    public float getStartYOnPdf() {
        return startYOnPdf;
    }

    public void setStartYOnPdf(float startYOnPdf) {
        this.startYOnPdf = startYOnPdf;
    }

    public float getAvailableWidthOnPdf() {
        return availableWidthOnPdf;
    }

    public void setAvailableWidthOnPdf(float availableWidthOnPdf) {
        this.availableWidthOnPdf = availableWidthOnPdf;
    }

    public float getAvailableHeightOnPdf() {
        return availableHeightOnPdf;
    }

    public void setAvailableHeightOnPdf(float availableHeightOnPdf) {
        this.availableHeightOnPdf = availableHeightOnPdf;
    }

    public void setRescaledSize(float width, float height) {

        float newWidth = 0;
        float newHeight = 0;

        if (width > height) {
            newWidth = availableWidthOnPdf;
            newHeight = (availableWidthOnPdf * height) / width;
            if (newHeight > availableHeightOnPdf) {
                newHeight = availableHeightOnPdf;
                newWidth = (availableHeightOnPdf * width) / height;
            }
        } else if (width < height) {
            newHeight = availableHeightOnPdf;
            newWidth = (availableHeightOnPdf * width) / height;
            if (newWidth > availableWidthOnPdf) {
                newWidth = availableWidthOnPdf;
                newHeight = (availableWidthOnPdf * height) / width;
            }
        } else if (width == height && availableHeightOnPdf < availableWidthOnPdf) {
            newHeight = availableHeightOnPdf;
            newWidth = availableHeightOnPdf;
        } else if (width == height && availableHeightOnPdf > availableWidthOnPdf) {
            newHeight = availableWidthOnPdf;
            newWidth = availableWidthOnPdf;
        }

        setStartXOnPdf(startXOnPdf + (availableWidthOnPdf - newWidth) / 2);
        setStartYOnPdf(startYOnPdf + (availableHeightOnPdf - newHeight) / 2);
        setResizedWidthForPdf(newWidth);
        setResizedHeightForPdf(newHeight);

    }
}
