package marinecomcom.fr.fantasticcard.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xinlan.imageeditlibrary.editimage.EditImageActivity;
import com.xinlan.imageeditlibrary.picchooser.SelectPictureActivity;

import java.io.File;
import java.util.ArrayList;

import marinecomcom.fr.fantasticcard.utils.Consts;
import marinecomcom.fr.fantasticcard.utils.FileUtils;
import marinecomcom.fr.fantasticcard.R;

public class MainActivity extends AppCompatActivity {
    public static final int SELECT_GALLERY_IMAGE_CODE = 7;
    public static final int ACTION_REQUEST_EDITIMAGE = 9;
    private MainActivity context;
    private ImageView imgView;
    private TextView countTextView;
    private View openAblum;
    private View editImage;
    private View addImage;
    private Bitmap mainBitmap;
    private int imageWidth, imageHeight;
    private String path;
    private ArrayList<String> pathImageList;
    private int count = Consts.NB_PHOTOS;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        context = this;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        imageWidth = (int) ((float) metrics.widthPixels / 1.5);
        imageHeight = (int) ((float) metrics.heightPixels / 1.5);

        pathImageList = new ArrayList<String>();

        imgView = (ImageView) findViewById(R.id.img);
        openAblum = findViewById(R.id.select_ablum);
        editImage = findViewById(R.id.edit_image);
        addImage = findViewById(R.id.add_image);
        countTextView = (TextView) findViewById(R.id.count_text);
        countTextView.setText("Il vous reste " + count + " image(s) à sélectionner");

        openAblum.setOnClickListener(new SelectClick());
        editImage.setOnClickListener(new EditImageClick());
        addImage.setOnClickListener(new AddImageClick());
    }


    private final class EditImageClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (TextUtils.isEmpty(path)) {
                Toast.makeText(MainActivity.this, R.string.no_choose, Toast.LENGTH_SHORT).show();
                return;
            }

            Intent it = new Intent(MainActivity.this, EditImageActivity.class);
            it.putExtra(EditImageActivity.FILE_PATH, path);
            File outputFile = FileUtils.getEmptyFile("tietu"
                    + System.currentTimeMillis() + ".jpg");
            it.putExtra(EditImageActivity.EXTRA_OUTPUT,
                    outputFile.getAbsolutePath());
            MainActivity.this.startActivityForResult(it,
                    ACTION_REQUEST_EDITIMAGE);
        }
    }

    private final class SelectClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            MainActivity.this.startActivityForResult(new Intent(
                            MainActivity.this, SelectPictureActivity.class),
                    SELECT_GALLERY_IMAGE_CODE);
        }
    }

    private final class AddImageClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(path != null) {
                pathImageList.add(path);
            }
            count--;

            if(pathImageList.size() == Consts.NB_PHOTOS){
                Intent intent = new Intent(getBaseContext(), CardActivity.class);
                intent.putExtra("PATHS", pathImageList);
                startActivity(intent);
            } else {
                countTextView.setText("Il vous reste " + count + " image(s) à sélectionner");
                editImage.setVisibility(View.GONE);
                addImage.setVisibility(View.GONE);
                imgView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_GALLERY_IMAGE_CODE:
                    handleSelectFromAblum(data);
                    break;
                case ACTION_REQUEST_EDITIMAGE:
                    handleEditorImage(data);
                    break;
            }
        }
    }

    private void handleEditorImage(Intent data) {
        String newFilePath = data.getStringExtra("save_file_path");
        path = newFilePath;
        LoadImageTask loadTask = new LoadImageTask();
        loadTask.execute(path);
    }

    private void handleSelectFromAblum(Intent data) {
        String filepath = data.getStringExtra("imgPath");
        path = filepath;
        LoadImageTask task = new LoadImageTask();
        task.execute(path);
    }


    private final class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            return getSampledBitmap(params[0], imageWidth, imageHeight);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        protected void onCancelled(Bitmap result) {
            super.onCancelled(result);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (mainBitmap != null) {
                mainBitmap.recycle();
                mainBitmap = null;
                System.gc();
            }
            mainBitmap = result;
            imgView.setImageBitmap(mainBitmap);
            editImage.setVisibility(View.VISIBLE);
            addImage.setVisibility(View.VISIBLE);
            imgView.setVisibility(View.VISIBLE);
        }
    }

    public static Bitmap getSampledBitmap(String filePath, int reqWidth,
                                          int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(filePath, options);

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = (int) Math.round((float) height / reqHeight);
            } else {
                inSampleSize = (int) Math.round((float)width / (float)reqWidth);
            }
        }

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(pathImageList.size() ==6){
            count = Consts.NB_PHOTOS;
            pathImageList.clear();
            editImage.setVisibility(View.GONE);
            addImage.setVisibility(View.GONE);
            imgView.setVisibility(View.GONE);
            countTextView.setText("Il vous reste " + count + " image(s) à sélectionner");
        }
    }
}
