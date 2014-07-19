package com.baoyz.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

/**
 * 
 * @author baoyz
 * @date 2014-7-19
 * 
 */
public class BubbleImageView extends ImageView {

	private static final int LOCATION_LEFT = 0;
	private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
	private static final int COLORDRAWABLE_DIMENSION = 1;

	private int mAngle = dp2px(10);
	private int mArrowTop = dp2px(40);
	private int mArrowWidth = dp2px(20);
	private int mArrowHeight = dp2px(20);
	private int mArrowOffset = 0;
	private int mArrowLocation = LOCATION_LEFT;

	private Rect mDrawableRect;
	private Bitmap mBitmap;
	private BitmapShader mBitmapShader;
	private Paint mBitmapPaint;
	private Matrix mShaderMatrix;
	private int mBitmapWidth;
	private int mBitmapHeight;

	public BubbleImageView(Context context) {
		super(context);
		initView(null);
	}

	public BubbleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(attrs);
	}

	public BubbleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(attrs);
	}

	private void initView(AttributeSet attrs) {
		if (attrs != null) {
			TypedArray a = getContext().obtainStyledAttributes(attrs,
					R.styleable.BubbleImageView);
			mAngle = (int) a.getDimension(
					R.styleable.BubbleImageView_bubble_angle, mAngle);
			mArrowHeight = (int) a.getDimension(
					R.styleable.BubbleImageView_bubble_arrowHeight,
					mArrowHeight);
			mArrowOffset = (int) a.getDimension(
					R.styleable.BubbleImageView_bubble_arrowOffset,
					mArrowOffset);
			mArrowTop = (int) a.getDimension(
					R.styleable.BubbleImageView_bubble_arrowTop, mArrowTop);
			mArrowWidth = (int) a.getDimension(
					R.styleable.BubbleImageView_bubble_arrowWidth, mAngle);
			mArrowLocation = a.getInt(
					R.styleable.BubbleImageView_bubble_arrowLocation,
					mArrowLocation);
			a.recycle();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (getDrawable() == null) {
			return;
		}
		RectF rect = new RectF(getPaddingLeft(), getPaddingTop(), getRight()
				- getLeft() - getPaddingRight(), getBottom() - getTop()
				- getPaddingBottom());

		Path path = new Path();

		if (mArrowLocation == LOCATION_LEFT) {
			leftPath(rect, path);
		} else {
			rightPath(rect, path);
		}

		canvas.drawPath(path, mBitmapPaint);
	}

	public void rightPath(RectF rect, Path path) {
		path.moveTo(mAngle, rect.top);
		path.lineTo(rect.width(), rect.top);
		path.arcTo(new RectF(rect.right - mAngle * 2 - mArrowWidth, rect.top,
				rect.right - mArrowWidth, mAngle * 2 + rect.top), 270, 90);
		path.lineTo(rect.right - mArrowWidth, mArrowTop);
		path.lineTo(rect.right, mArrowTop - mArrowOffset);
		path.lineTo(rect.right - mArrowWidth, mArrowTop + mArrowHeight);
		path.lineTo(rect.right - mArrowWidth, rect.height() - mAngle);
		path.arcTo(new RectF(rect.right - mAngle * 2 - mArrowWidth, rect.bottom
				- mAngle * 2, rect.right - mArrowWidth, rect.bottom), 0, 90);
		path.lineTo(rect.left, rect.bottom);
		path.arcTo(new RectF(rect.left, rect.bottom - mAngle * 2, mAngle * 2
				+ rect.left, rect.bottom), 90, 90);
		path.lineTo(rect.left, rect.top);
		path.arcTo(new RectF(rect.left, rect.top, mAngle * 2 + rect.left,
				mAngle * 2 + rect.top), 180, 90);
		path.close();
	}

	public void leftPath(RectF rect, Path path) {
		path.moveTo(mAngle + mArrowWidth, rect.top);
		path.lineTo(rect.width(), rect.top);
		path.arcTo(new RectF(rect.right - mAngle * 2, rect.top, rect.right,
				mAngle * 2 + rect.top), 270, 90);
		path.lineTo(rect.right, rect.top);
		path.arcTo(new RectF(rect.right - mAngle * 2, rect.bottom - mAngle * 2,
				rect.right, rect.bottom), 0, 90);
		path.lineTo(rect.left + mArrowWidth, rect.bottom);
		path.arcTo(new RectF(rect.left + mArrowWidth, rect.bottom - mAngle * 2,
				mAngle * 2 + rect.left + mArrowWidth, rect.bottom), 90, 90);
		path.lineTo(rect.left + mArrowWidth, mArrowTop + mArrowHeight);
		path.lineTo(rect.left, mArrowTop - mArrowOffset);
		path.lineTo(rect.left + mArrowWidth, mArrowTop);
		path.lineTo(rect.left + mArrowWidth, rect.top);
		path.arcTo(new RectF(rect.left + mArrowWidth, rect.top, mAngle * 2
				+ rect.left + mArrowWidth, mAngle * 2 + rect.top), 180, 90);

		path.close();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setup();
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		mBitmap = bm;
		setup();
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		mBitmap = getBitmapFromDrawable(drawable);
		setup();
	}

	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
		mBitmap = getBitmapFromDrawable(getDrawable());
		setup();
	}

	private Bitmap getBitmapFromDrawable(Drawable drawable) {
		if (drawable == null) {
			return null;
		}

		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}

		try {
			Bitmap bitmap;

			if (drawable instanceof ColorDrawable) {
				bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION,
						COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
			} else {
				bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(), BITMAP_CONFIG);
			}

			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
			drawable.draw(canvas);
			return bitmap;
		} catch (OutOfMemoryError e) {
			return null;
		}
	}

	private void setup() {
		if (mBitmap == null) {
			return;
		}

		mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP,
				Shader.TileMode.CLAMP);

		mBitmapPaint = new Paint();
		mBitmapPaint.setAntiAlias(true);
		mBitmapPaint.setShader(mBitmapShader);

		mBitmapHeight = mBitmap.getHeight();
		mBitmapWidth = mBitmap.getWidth();

		updateShaderMatrix();
		invalidate();
	}

	private void updateShaderMatrix() {
		float scale;
		float dx = 0;
		float dy = 0;

		mShaderMatrix = new Matrix();
		mShaderMatrix.set(null);

		mDrawableRect = new Rect(0, 0, getRight() - getLeft(), getBottom()
				- getTop());

		if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width()
				* mBitmapHeight) {
			scale = mDrawableRect.height() / (float) mBitmapHeight;
			dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
		} else {
			scale = mDrawableRect.width() / (float) mBitmapWidth;
			dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
		}

		mShaderMatrix.setScale(scale, scale);
		mShaderMatrix.postTranslate((int) (dx + 0.5f), (int) (dy + 0.5f));

		mBitmapShader.setLocalMatrix(mShaderMatrix);
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getContext().getResources().getDisplayMetrics());
	}
}
