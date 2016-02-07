package marinecomcom.fr.fantasticcard.model;

/**
 * Created by Technique on 07/02/16.
 */
public class ResourceImage extends AbstractImage {

    public int resource;

    public ResourceImage(float startXOnPdf, float startYOnPdf, float availableWidthOnPdf, float availableHeightOnPdf){
        super(startXOnPdf,startYOnPdf,availableWidthOnPdf,availableHeightOnPdf);
    }

    public ResourceImage(int ressource, float startXOnPdf, float startYOnPdf, float availableWidthOnPdf, float availableHeightOnPdf){
        super(startXOnPdf,startYOnPdf,availableWidthOnPdf,availableHeightOnPdf);
        this.resource = ressource;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }
}
