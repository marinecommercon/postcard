package com.xinlan.imageeditlibrary.editimage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ViewFlipper;

import com.xinlan.imageeditlibrary.BaseActivity;
import com.xinlan.imageeditlibrary.R;
import com.xinlan.imageeditlibrary.editimage.fragment.CropFragment;
import com.xinlan.imageeditlibrary.editimage.fragment.FilterListFragment;
import com.xinlan.imageeditlibrary.editimage.fragment.MainMenuFragment;
import com.xinlan.imageeditlibrary.editimage.fragment.RotateFragment;
import com.xinlan.imageeditlibrary.editimage.fragment.StickerFragment;
import com.xinlan.imageeditlibrary.editimage.view.CropImageView;
import com.xinlan.imageeditlibrary.editimage.view.CustomViewPager;
import com.xinlan.imageeditlibrary.editimage.view.RotateImageView;
import com.xinlan.imageeditlibrary.editimage.view.StickerView;
import com.xinlan.imageeditlibrary.editimage.view.imagezoom.ImageViewTouch;
import com.xinlan.imageeditlibrary.editimage.utils.BitmapUtils;
import com.xinlan.imageeditlibrary.editimage.view.imagezoom.ImageViewTouchBase;

/**
 * 图片编辑 主页面
 * 
 * @author panyi
 * 
 *         包含 1.贴图 2.滤镜 3.剪裁 4.底图旋转 功能
 * 
 */
public class EditImageActivity extends BaseActivity {
	public static final String FILE_PATH = "file_path";
	public static final String EXTRA_OUTPUT = "extra_output";

	public static final int MODE_NONE = 0;
	public static final int MODE_STICKERS = 1;
	public static final int MODE_FILTER = 2;
	public static final int MODE_CROP = 3;
	public static final int MODE_TEXT = 4;
	public static final int MODE_ROTATE = 5;

	public String filePath;
	public String saveFilePath;
	private int imageWidth, imageHeight;
	private LoadImageTask mLoadImageTask;

	public int mode = MODE_NONE;
	private EditImageActivity mContext;
	public Bitmap mainBitmap;
	public ImageViewTouch mainImage;
	private View backBtn;

	public ViewFlipper bannerFlipper;
	private View applyBtn;
	private View saveBtn;

	public StickerView mStickerView;
	public CropImageView mCropPanel;
	public RotateImageView mRotatePanel;

	public CustomViewPager bottomGallery;
	private BottomGalleryAdapter mBottomGalleryAdapter;
	private MainMenuFragment mMainMenuFragment;
	public StickerFragment mStickerFragment;
	public FilterListFragment mFilterListFragment;
	private CropFragment mCropFragment;
	public RotateFragment mRotateFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checkInitImageLoader();
		setContentView(R.layout.activity_image_edit);
		initView();
		getData();
	}

	private void getData() {
		filePath = getIntent().getStringExtra(FILE_PATH);
		saveFilePath = getIntent().getStringExtra(EXTRA_OUTPUT);
		loadImage(filePath);
	}

	private void initView() {
		mContext = this;
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		imageWidth = (int) ((float) metrics.widthPixels / 1.5);
		imageHeight = (int) ((float) metrics.heightPixels / 1.5);

		bannerFlipper = (ViewFlipper) findViewById(R.id.banner_flipper);
		bannerFlipper.setInAnimation(this, R.anim.in_bottom_to_top);
		bannerFlipper.setOutAnimation(this, R.anim.out_bottom_to_top);
		applyBtn = findViewById(R.id.apply);
		applyBtn.setOnClickListener(new ApplyBtnClick());
		saveBtn = findViewById(R.id.save_btn);
		saveBtn.setOnClickListener(new SaveBtnClick());

		mainImage = (ImageViewTouch) findViewById(R.id.main_image);
		backBtn = findViewById(R.id.back_btn);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				forceReturnBack();
			}
		});

		mStickerView = (StickerView) findViewById(R.id.sticker_panel);
		mCropPanel = (CropImageView) findViewById(R.id.crop_panel);
		mRotatePanel = (RotateImageView) findViewById(R.id.rotate_panel);

		bottomGallery = (CustomViewPager) findViewById(R.id.bottom_gallery);
		bottomGallery.setOffscreenPageLimit(5);
		mMainMenuFragment = MainMenuFragment.newInstance(this);
		mBottomGalleryAdapter = new BottomGalleryAdapter(
				this.getSupportFragmentManager());
		mStickerFragment = StickerFragment.newInstance(this);
		mFilterListFragment = FilterListFragment.newInstance(this);
		mCropFragment = CropFragment.newInstance(this);
		mRotateFragment = RotateFragment.newInstance(this);

		bottomGallery.setAdapter(mBottomGalleryAdapter);
	}

	/**
	 * 
	 * @author panyi
	 * 
	 */
	private final class BottomGalleryAdapter extends FragmentPagerAdapter {
		public BottomGalleryAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int index) {
			// System.out.println("createFragment-->"+index);
			if (index == 0)
				return mMainMenuFragment;
			if (index == 1)
				return mStickerFragment;
			if (index == 2)
				return mFilterListFragment;
			if (index == 3)
				return mCropFragment;
			if (index == 4)
				return mRotateFragment;
			return MainMenuFragment.newInstance(mContext);
		}

		@Override
		public int getCount() {
			return 5;
		}
	}// end inner class

	/**
	 * 
	 * @param filepath
	 */
	public void loadImage(String filepath) {
		if (mLoadImageTask != null) {
			mLoadImageTask.cancel(true);
		}
		mLoadImageTask = new LoadImageTask();
		mLoadImageTask.execute(filepath);
	}

	private final class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
		@Override
		protected Bitmap doInBackground(String... params) {
			return BitmapUtils.loadImageByPath(params[0], imageWidth,
					imageHeight);
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
			mainImage.setImageBitmap(result);
			mainImage.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
			// mainImage.setDisplayType(DisplayType.FIT_TO_SCREEN);
		}
	}// end inner class

	/**
	 * 按下返回键
	 * 
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			switch (mode) {
			case MODE_STICKERS:
				mStickerFragment.backToMain();
				return true;
			case MODE_FILTER:
				mFilterListFragment.backToMain();
				return true;
			case MODE_CROP:
				mCropFragment.backToMain();
				return true;
			case MODE_ROTATE:
				mRotateFragment.backToMain();
				return true;
			}// end switch

			forceReturnBack();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 */
	private void forceReturnBack() {
		setResult(RESULT_CANCELED);
		this.finish();
	}

	/**
	 * 
	 * @author panyi
	 * 
	 */
	private final class ApplyBtnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (mode) {
			case MODE_STICKERS:
				mStickerFragment.saveStickers();
				break;
			case MODE_FILTER:
				mFilterListFragment.saveFilterImage();
				break;
			case MODE_CROP:
				mCropFragment.saveCropImage();
				break;
			case MODE_ROTATE:
				mRotateFragment.saveRotateImage();
				break;
			default:
				break;
			}// end switch
		}
	}// end inner class

	/**
	 * 
	 * @author panyi
	 * 
	 */
	private final class SaveBtnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent returnIntent = new Intent();
			returnIntent.putExtra("save_file_path", saveFilePath);
			mContext.setResult(RESULT_OK, returnIntent);
			mContext.finish();
		}
	}// end inner class

	/**
	 * 
	 * @param newBit
	 */
	public void changeMainBitmap(Bitmap newBit) {
		if (mainBitmap != null) {
			if (!mainBitmap.isRecycled()) {
				mainBitmap.recycle();
			}
			mainBitmap = newBit;
		} else {
			mainBitmap = newBit;
		}// end if
		mainImage.setImageBitmap(mainBitmap);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mLoadImageTask != null) {
			mLoadImageTask.cancel(true);
		}
	}

}// end class
