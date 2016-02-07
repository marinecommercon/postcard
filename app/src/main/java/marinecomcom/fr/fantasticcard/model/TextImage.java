package marinecomcom.fr.fantasticcard.model;

/**
 * Created by Technique on 07/02/16.
 */
public class TextImage extends AbstractImage {

    public int             resource;
    public String          message;
    public float           textWidth;
    public float           textHeight;

    public TextImage(String message, float startXOnPdf, float startYOnPdf,
                     float availableWidthOnPdf, float availableHeightOnPdf){
        super(startXOnPdf,startYOnPdf,availableWidthOnPdf,availableHeightOnPdf);
        this.message     = message;
    }

    public TextImage(int ressource, String message, float startXOnPdf, float startYOnPdf,
                     float availableWidthOnPdf, float availableHeightOnPdf){
        super(startXOnPdf,startYOnPdf,availableWidthOnPdf,availableHeightOnPdf);
        this.message     = message;
        this.resource = ressource;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public float getTextWidth() {
        return textWidth;
    }

    public void setTextWidth(float textWidth) {
        this.textWidth = textWidth;
    }

    public float getTextHeight() {
        return textHeight;
    }

    public void setTextHeight(float textHeight) {
        this.textHeight = textHeight;
    }
}
