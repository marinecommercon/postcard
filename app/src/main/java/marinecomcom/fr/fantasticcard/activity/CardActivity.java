package marinecomcom.fr.fantasticcard.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.libharu.PdfDocument;
import org.libharu.PdfPage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import marinecomcom.fr.fantasticcard.utils.Consts;
import marinecomcom.fr.fantasticcard.R;
import marinecomcom.fr.fantasticcard.model.AbstractImage;
import marinecomcom.fr.fantasticcard.model.PathImage;
import marinecomcom.fr.fantasticcard.model.ResourceImage;
import marinecomcom.fr.fantasticcard.model.TextImage;

/**
 * Created by Technique on 07/02/16.
 */
public class CardActivity extends AppCompatActivity {

    private ArrayList<String> pathImageList;

    int margin    = 20;

    PdfPage pageOutside;
    PdfPage pageInside;
    float pageOutsideWidth;
    float pageOutsideHeight;
    float pageInsideWidth;
    float pageInsideHeight;

    ProgressBar mProgressBar;
    ArrayList<AbstractImage> abstractOutsideList;
    ArrayList<AbstractImage> abstractInsideList;

    EditText edittext1;
    EditText edittext2;
    EditText edittext3;
    EditText edittext4;

    PdfDocument doc;
    int count = 0;
    ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        mProgressBar = (ProgressBar) findViewById(R.id.pBAsync);
        edittext1    = (EditText) findViewById(R.id.editText1);
        edittext2    = (EditText) findViewById(R.id.editText2);
        edittext3    = (EditText) findViewById(R.id.editText3);
        edittext4    = (EditText) findViewById(R.id.editText4);

        pathImageList = new ArrayList<String>();
        Bundle extras = getIntent().getExtras();
        pathImageList = extras.getStringArrayList("PATHS");

        findViewById(R.id.generatePdfButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // PDF generation should be done on a background thread in a real application
                createPdf();
            }
        });

    }

    private void createPdf() {
        doc = PdfDocument.createPdf();
        if (doc == null) {
            // Failed to create PDF document
            showToast("Impossible de créer la carte", Toast.LENGTH_SHORT);
            return;
        }

        doc.setCompressionMode(PdfDocument.HPDF_COMP_ALL);

        pageOutside = doc.addPage();
        pageOutside.setSize(PdfPage.Size.A4, PdfPage.Direction.LANDSCAPE);
        pageInside  = doc.addPage();
        pageInside.setSize(PdfPage.Size.A4, PdfPage.Direction.LANDSCAPE);

        pageOutsideHeight = pageOutside.getHeight();
        pageOutsideWidth  = pageOutside.getWidth();
        pageInsideHeight  = pageInside.getHeight();
        pageInsideWidth   = pageInside.getWidth();

        prepareOutside();
        prepareInside();

        DrawImageAsync drawImageAsync = new DrawImageAsync();
        drawImageAsync.execute();
    }

    private void prepareOutside(){
        abstractOutsideList = new ArrayList<AbstractImage>();

        ResourceImage imageOutside1 = new ResourceImage(R.drawable.winter, 0, 0, pageOutsideWidth / 2, pageOutsideHeight);
        PathImage imageOutside2 = new PathImage(pathImageList.get(0), margin, margin, pageOutsideWidth / 2 - 2 * margin, pageOutsideHeight - 2 * margin);
        ResourceImage imageOutside3 = new ResourceImage(R.drawable.winter2, pageOutsideWidth / 2, 0, pageOutsideWidth / 2, pageOutsideHeight);
        PathImage     imageOutside4 = new PathImage(pathImageList.get(1), pageOutsideWidth / 2 + margin, margin, pageOutsideWidth / 2 - 2 * margin, pageOutsideHeight - 2 * margin);
        TextImage imageOutside5 = new TextImage(R.drawable.marker, edittext1.getText().toString(), 0, 0, pageOutsideWidth / 2, pageOutsideHeight);
        TextImage     imageOutside6 = new TextImage(R.drawable.marker, edittext4.getText().toString(), pageOutsideWidth / 2, 0, pageOutsideWidth / 2, pageOutsideHeight);

        abstractOutsideList.add(imageOutside1);
        abstractOutsideList.add(imageOutside2);
        abstractOutsideList.add(imageOutside3);
        abstractOutsideList.add(imageOutside4);
        abstractOutsideList.add(imageOutside5);
        abstractOutsideList.add(imageOutside6);
    }

    private void prepareInside(){
        abstractInsideList = new ArrayList<AbstractImage>();

        ResourceImage imageInsideBack = new ResourceImage(R.drawable.winter3, 0, 0, pageInsideWidth, pageInsideHeight);
        PathImage imageInsideTopLeft = new PathImage(pathImageList.get(2), margin, pageInsideHeight/2 + margin/2,
                pageInsideWidth/2 - 2*margin, pageInsideHeight/2 - 3*margin/2);
        PathImage imageInsideBottomLeft = new PathImage(pathImageList.get(3), margin, margin,
                pageInsideWidth/2 - 2*margin, pageInsideHeight/2 - 3*margin/2);
        PathImage imageInsideTopRight = new PathImage(pathImageList.get(4), pageInsideWidth/2 + margin, pageInsideHeight/2 + margin/2,
                pageInsideWidth/2 - 2*margin, pageInsideHeight/2 - 3*margin/2);
        PathImage imageInsideBottomRight = new PathImage(pathImageList.get(5), pageInsideWidth/2 + margin, margin,
                pageInsideWidth/2 - 2*margin, pageInsideHeight/2 - 3*margin/2);

        TextImage imageInsideTextLeft = new TextImage(R.drawable.marker, edittext2.getText().toString(),
                0, 0, pageInsideWidth / 2, pageInsideHeight);
        TextImage imageInsideTextRight = new TextImage(R.drawable.marker, edittext3.getText().toString(),
                pageInsideWidth / 2, 0, pageInsideWidth / 2, pageInsideHeight);

        abstractInsideList.add(imageInsideBack);
        abstractInsideList.add(imageInsideTopLeft);
        abstractInsideList.add(imageInsideBottomLeft);
        abstractInsideList.add(imageInsideTextLeft);
        abstractInsideList.add(imageInsideTopRight);
        abstractInsideList.add(imageInsideBottomRight);
        abstractInsideList.add(imageInsideTextRight);
    }

    private void drawImage(AbstractImage img, String origin){

        Bitmap bitmap = null;
        // Get the size that image should take in the pdf
        // Give the available size on Pdf in order to know the real need

        if(img instanceof ResourceImage){
             bitmap = decodeSampledBitmapFromResource(getResources(), ((ResourceImage)img).getResource(),
                    Math.round(img.getAvailableWidthOnPdf()), Math.round(img.getAvailableHeightOnPdf()));
        } else {
            bitmap = decodeSampledBitmapFromPath(getResources(), ((PathImage)img).getPath(),
                    Math.round(img.getAvailableWidthOnPdf()), Math.round(img.getAvailableHeightOnPdf()));
        }

        img.setRescaledSize(bitmap.getWidth(), bitmap.getHeight());
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);

        // Take the resized size, the only one needed and rescaled
        if(Consts.OUTSIDE_PAGE.equals(origin)) {
            pageOutside.drawJpegImage(outStream.toByteArray(), img.getStartXOnPdf(), img.getStartYOnPdf(), img.getResizedWidthForPdf(), img.getResizedHeightForPdf());
        } else if(Consts.INSIDE_PAGE.equals(origin)){
            pageInside.drawJpegImage(outStream.toByteArray(), img.getStartXOnPdf(), img.getStartYOnPdf(), img.getResizedWidthForPdf(), img.getResizedHeightForPdf());
        }

        bitmap.recycle();
        try {
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawTextImage(TextImage imageText, String origin){

        Bitmap bitmap = decodeSampledBitmapFromResource(getResources(), imageText.getResource(),
                Math.round(imageText.getAvailableWidthOnPdf()), Math.round(imageText.getAvailableHeightOnPdf()));
        bitmap = drawTextToBitmap(this, imageText, bitmap);

        ByteArrayOutputStream outStream2 = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream2);

        // Take the resized size, the only one needed and rescaled
        if(Consts.OUTSIDE_PAGE.equals(origin)) {
            pageOutside.drawPngImage(outStream2.toByteArray(), imageText.getStartXOnPdf(), imageText.getStartYOnPdf(), imageText.getAvailableWidthOnPdf(), imageText.getAvailableHeightOnPdf());
        } else if(Consts.INSIDE_PAGE.equals(origin)){
            pageInside.drawPngImage(outStream2.toByteArray(), imageText.getStartXOnPdf(), imageText.getStartYOnPdf(), imageText.getAvailableWidthOnPdf(), imageText.getAvailableHeightOnPdf());
        }

        bitmap.recycle();
        try {
            outStream2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeSampledBitmapFromPath(Resources res, String path,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;

        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private void savePdf() {

        File pdfFile = new File(Environment.getExternalStorageDirectory(), "fantasticcard.pdf");
        pdfFile.delete(); //DELETE existing file
        pdfFile = new File(Environment.getExternalStorageDirectory(), "fantasticcard.pdf");

        boolean success = doc.saveToFile(pdfFile.getAbsolutePath());

        // Close the PDF to free up native memory
        doc.close();

        if (success) {
            showToast("PDF saved to " + pdfFile.getAbsolutePath(), Toast.LENGTH_LONG);

            // Start activity to display PDF
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setType("application/pdf");
            intent.setData(Uri.fromFile(pdfFile));
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                showToast("No activity found to display PDF", Toast.LENGTH_SHORT);
            }
        } else {
            showToast("Failed to save PDF", Toast.LENGTH_SHORT);
        }
    }

    public Bitmap drawTextToBitmap(Context mContext, TextImage imageText, Bitmap bitmap) {
        try {
            Resources resources = mContext.getResources();
            float scale = resources.getDisplayMetrics().density;

            android.graphics.Bitmap.Config bitmapConfig =   bitmap.getConfig();
            // set default bitmap config if none
            if(bitmapConfig == null) {
                bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
            }
            // resource bitmaps are imutable,
            // so we need to convert it to mutable one
            bitmap = bitmap.copy(bitmapConfig, true);

            Canvas canvas = new Canvas(bitmap);
            // new antialised Paint

            Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);

            // new antialiased Paint
            TextPaint paint=new TextPaint(Paint.ANTI_ALIAS_FLAG);
            // text color - #3D3D3D
            paint.setColor(Color.rgb(68, 11, 31));
            // text size in pixels
            paint.setTextSize((int) (16 * scale));
            paint.setTypeface(tf);

            // set text width to canvas width minus 16dp padding
            int textWidth = canvas.getWidth() - (int) (16 * scale);

            // init StaticLayout for text
            StaticLayout textLayout = new StaticLayout(
                    imageText.getMessage(), paint, textWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

            // get height of multiline text
            int textHeight = textLayout.getHeight();

            // get position of text's top left corner
            float x = (bitmap.getWidth() - textWidth)/2;
            float y = bitmap.getHeight() - textHeight;

            imageText.setTextHeight(textHeight);
            imageText.setTextWidth(textWidth);

            // draw text to the Canvas center
            canvas.save();
            canvas.translate(x, y);
            textLayout.draw(canvas);
            canvas.restore();

            return bitmap;
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }

    }

    private void showToast(String text, int duration) {
        Toast.makeText(this, text, duration).show();
    }

    private class DrawImageAsync extends AsyncTask<Void, Integer, Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values){
            super.onProgressUpdate(values);
            mProgressBar.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            int progress  = 0;
            int totalSize = abstractOutsideList.size() + abstractInsideList.size();
            {
                for(int i=0; i< abstractOutsideList.size() ; i++) {
                    if(abstractOutsideList.get(i) instanceof ResourceImage) {
                        drawImage((ResourceImage) abstractOutsideList.get(i), Consts.OUTSIDE_PAGE);
                        progress += 100 / totalSize;
                        publishProgress(progress);
                    } else if (abstractOutsideList.get(i) instanceof PathImage){
                        drawImage((PathImage) abstractOutsideList.get(i), Consts.OUTSIDE_PAGE);
                        progress += 100/ totalSize;
                        publishProgress(progress);
                    } else if (abstractOutsideList.get(i) instanceof TextImage){
                        drawTextImage((TextImage) abstractOutsideList.get(i), Consts.OUTSIDE_PAGE);
                        progress += 100/ totalSize;
                        publishProgress(progress);
                    }
                }

                for(int i=0; i< abstractInsideList.size() ; i++) {
                    if(abstractInsideList.get(i) instanceof ResourceImage) {
                        drawImage((ResourceImage) abstractInsideList.get(i), Consts.INSIDE_PAGE);
                        progress += 100 / totalSize;
                        publishProgress(progress);
                    } else if (abstractInsideList.get(i) instanceof PathImage){
                        drawImage((PathImage) abstractInsideList.get(i), Consts.INSIDE_PAGE);
                        progress += 100/ totalSize;
                        publishProgress(progress);
                    } else if (abstractInsideList.get(i) instanceof TextImage){
                        drawTextImage((TextImage) abstractInsideList.get(i), Consts.INSIDE_PAGE);
                        progress += 100/ totalSize;
                        publishProgress(progress);
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            savePdf();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        edittext1.getText().clear();
        edittext2.getText().clear();
        edittext3.getText().clear();
        edittext4.getText().clear();
    }
}
