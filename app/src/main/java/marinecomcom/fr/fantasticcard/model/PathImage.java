package marinecomcom.fr.fantasticcard.model;

/**
 * Created by Technique on 07/02/16.
 */
public class PathImage extends AbstractImage {

    public String path;

    public PathImage(float startXOnPdf, float startYOnPdf, float availableWidthOnPdf, float availableHeightOnPdf){
        super(startXOnPdf,startYOnPdf,availableWidthOnPdf,availableHeightOnPdf);
    }

    public PathImage(String path, float startXOnPdf, float startYOnPdf, float availableWidthOnPdf, float availableHeightOnPdf){
        super(startXOnPdf,startYOnPdf,availableWidthOnPdf,availableHeightOnPdf);
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

