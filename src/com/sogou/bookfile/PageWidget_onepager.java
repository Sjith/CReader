package com.sogou.bookfile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Region;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

public class PageWidget_onepager extends View {

	
	
	private static final String TAG = "lx";
	private int mWidth = 480;//Ĭ��ֵ���������setScreen����
	private int mHeight = 800;//Ĭ��ֵ���������setScreen����
	public boolean ToNext = true;
	private boolean movelock = true;
	private int mCornerX = 0; //ȷ���������
	private int mCornerY = 0;
	private final int shadow_width = 30;
	Bitmap mCurPageBitmap = null; 
	Bitmap mNextPageBitmap = null;
	Bitmap mPrePageBitmap = null;

	PointF mTouch_down = new PointF();
	PointF mTouch = new PointF();
//	PointF mTouch_lastmove = new PointF(); 

	float mMiddleX;
	float mMiddleY;
	float mDegrees;
	public int mTouchToCornerDis,lastdis;
	ColorMatrixColorFilter mColorMatrixFilter;
	Matrix mMatrix;
	float[] mMatrixArray = { 0, 0, 0, 0, 0, 0, 0, 0, 1.0f };

	float mMaxLength;
	int[] mFrontShadowColors = new int[] { 0x80111111, 0x111111 };


	public Scroller mScroller;
	public PageWidget_onepager(Context context, AttributeSet attrs) {
		super(context, attrs);
	
		// TODO Auto-generated constructor stub
//		createDrawable();

		ColorMatrix cm = new ColorMatrix();
		float array[] = { 0.55f, 0, 0, 0, 80.0f, 0, 0.55f, 0, 0, 80.0f, 0, 0,
				0.55f, 0, 80.0f, 0, 0, 0, 0.2f, 0 };
		cm.set(array);
		mColorMatrixFilter = new ColorMatrixColorFilter(cm);
		mMatrix = new Matrix();
		mScroller = new Scroller(getContext());

		mTouch.x = 0; 
		mTouch.y = 0;
		
	}
	public PageWidget_onepager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
//		createDrawable();

		ColorMatrix cm = new ColorMatrix();
		float array[] = { 0.55f, 0, 0, 0, 80.0f, 0, 0.55f, 0, 0, 80.0f, 0, 0,
				0.55f, 0, 80.0f, 0, 0, 0, 0.2f, 0 };
		cm.set(array);
		mColorMatrixFilter = new ColorMatrixColorFilter(cm);
		mMatrix = new Matrix();
		mScroller = new Scroller(getContext());

		mTouch.x = 0; 
		mTouch.y = 0;
	}
	

	public void calcCornerXY(float x, float y) {
		if (x <= mWidth / 2)
			ToNext = false;
		else
			ToNext = true;
	}
	
	public void protect_touch(float x, float y){
		mTouch.x = x; 
		mTouch.y = y;
		calcCornerXY(x,y);
		mTouchToCornerDis = 0;
		startAnimation(800);
		this.postInvalidate();
	}
	
	public void fist_move(float x, float y){
		mTouch_down.x = x;
		mTouch_down.y = y;
		movelock = true;
	}
	
	
	public void setCornerX_forfixbug(){
		mCornerX = 0;
		mCornerY = 0;
		mTouch = new PointF();
		mTouch.x = 0; 
		mTouch.y = 0;
		mTouch_down.x = 0;
		mTouch_down.y = 0;
		mTouchToCornerDis = 0;
		movelock = true;
		lastdis = 0;
	}
	
	public boolean doTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			
			lastdis = (int) (event.getX() - mTouch.x);
			
			mTouch.x = event.getX();
			mTouch.y = event.getY();
			calcPoints();
			this.postInvalidate();
		}
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			mTouch_down.x = event.getX();
			mTouch_down.y = event.getY();
//			mTouch_lastmove.x = event.getX();
//			mTouch_lastmove.y = event.getY();
			mTouch.x = event.getX();
			mTouch.y = event.getY();
			mTouchToCornerDis = 0;
			movelock = true;
			lastdis = 0;
//			System.out.println(" ACTION_DOWN mTouch.x is = "+mTouch_down.x+" , mTouch_down.y is = "+mTouch.y);
			calcCornerXY(mTouch_down.x, mTouch_down.y);
//			this.postInvalidate();
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
//			System.out.println("ACTION_UP mTouch.x is = "+mTouch.x+" , mTouch.y is = "+mTouch.y);
//			movelock = true;
			System.out.println(lastdis);
			calcPoints();
			if (canDragOver()) {
				startAnimation(600);
//				startAnimation(6000);
			} else {
				startAnimation_bak(400);

			}

			this.postInvalidate();
		}
		// return super.onTouchEvent(event);
		return true;
	}

	/**
	 * Author : hmg25 Version: 1.0 Description : ���ֱ��P1P2��ֱ��P3P4�Ľ������?
	 */

	private int calcPoints() {
		if(movelock){
			mTouchToCornerDis = (int) (mTouch.x - mTouch_down.x);
			if(Math.abs(mTouchToCornerDis) > 0){
				movelock = false;
			}else{
				return 0;
			}
			if(mTouchToCornerDis > 0){
				ToNext = false;
			}else{
				ToNext = true;
			}
		}else{
			mTouchToCornerDis = (int) (mTouch.x - mTouch_down.x);
			if(ToNext){
				if(mTouchToCornerDis > 0){
					mTouchToCornerDis = 0;
				}
			}else{
				if(mTouchToCornerDis < 0){
					mTouchToCornerDis = 0;
				}
			}
			
		}
		
		return mTouchToCornerDis;
	}

	private void drawCurrentPageArea(Canvas canvas, Bitmap bitmap,int m) {
//		if(ToNext){
//			if(mTouchToCornerDis > 0){
//				mTouchToCornerDis = 0;
//			}
//		}else{
//			if(mTouchToCornerDis < 0){
//				mTouchToCornerDis = 0;
//			}
//		}
		canvas.save();
		canvas.drawBitmap(bitmap, m, 0, null);
		canvas.restore();
	}
	
	private void drawNextPageArea(Canvas canvas, Bitmap bitmap) {
		canvas.save();
		canvas.drawBitmap(bitmap, 0, 0, null);
		canvas.restore();
	}
	
	private void drawPrePageArea(Canvas canvas, Bitmap bitmap) {
		canvas.save();
		canvas.drawBitmap(bitmap, mTouchToCornerDis-mWidth, 0, null);
		canvas.restore();
	}


	public void setBitmaps(Bitmap bm1, Bitmap bm2,Bitmap bm3) {
		mCurPageBitmap = bm1;
		mNextPageBitmap = bm2;
		mPrePageBitmap = bm3;
	}

	public void setScreen(int w, int h) {
		mWidth = w;
		mHeight = h;
		mMaxLength = (float) Math.hypot(mWidth, mHeight);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		calcPoints();
		if(movelock){
			drawCurrentPageArea(canvas, mCurPageBitmap,0);
		}else if(ToNext){
			drawNextPageArea(canvas, mNextPageBitmap);
			drawCurrentPageArea(canvas, mCurPageBitmap,mTouchToCornerDis);
			if(mTouchToCornerDis != 0){
				drawShadow(canvas, mWidth + mTouchToCornerDis);
			}
			
		}else{
			drawCurrentPageArea(canvas, mCurPageBitmap,0);
			drawPrePageArea(canvas, mPrePageBitmap);
			if(mTouchToCornerDis != 0){
				drawShadow(canvas,mTouchToCornerDis);
			}
			
		}

	}






	public void drawShadow(Canvas canvas,int m) {		
		canvas.save();
		GradientDrawable mCurrentPageShadow;
		
		//test
		mCurrentPageShadow = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, mFrontShadowColors);
		mCurrentPageShadow.setBounds(m , 0, m + shadow_width, mHeight);
		mCurrentPageShadow.draw(canvas);
		canvas.restore();

	}


	

	public void computeScroll() {
		super.computeScroll();
		if (mScroller.computeScrollOffset()) {
			float x = mScroller.getCurrX();
			float y = mScroller.getCurrY();
			mTouch.x = x;
			mTouch.y = y;
			postInvalidate();
		}
	}

	private void startAnimation(int delayMillis) {
		int dx, dy;
		if(movelock){
			if(ToNext){
				dx = - mWidth - mTouchToCornerDis - shadow_width;
			}else{
				dx = mWidth - mTouchToCornerDis;
			}
		}else if (ToNext) {
			dx = - mWidth - mTouchToCornerDis - shadow_width;
		}else {
			dx = mWidth - mTouchToCornerDis;
		}
		
		dy = 0;
		if(mTouchToCornerDis == 0){
			mScroller.startScroll((int) mTouch_down.x, (int) mTouch_down.y, dx, dy,
					delayMillis);
		}else{
			mScroller.startScroll((int) mTouch.x, (int) mTouch.y, dx, dy,
					delayMillis);
		}
		
	}
	
	
	private void startAnimation_bak(int delayMillis) {
		int dx, dy;
		if(mTouchToCornerDis == 0){
			movelock = true;
			mTouch.x = mTouch_down.x;
			postInvalidate();
			return;
		}
		dx = - mTouchToCornerDis;
		dy = 0;
		mScroller.startScroll((int) mTouch.x, (int) mTouch.y, dx, dy,
				delayMillis);
	}
	

	public void abortAnimation() {
		if (!mScroller.isFinished()) {
			mScroller.abortAnimation();
		}
	}
	
	public boolean same(int a , int b){
		if(a == 0 ){
			return false;
		}
		if(a > 0){
			if(b > 0){
				return true;
			}else{
				return false;
			}
		}else{
			if(b < 0){
				return true;
			}else{
				return false;
			}
		}
	}

	//�޸��������ҳ������
	public boolean canDragOver() {
		if(movelock ||(Math.abs(lastdis) > 0 && same(mTouchToCornerDis,lastdis))){
			return true;
		}
		return false;
	}


	public boolean DragToRight() {
		if (ToNext)
			return false;
		return true;
	}
	
	public boolean DragToNext(){
		if(movelock){
			if(ToNext){
				return true;
			}else{
				return false;
			}
		}else if (ToNext) {
			return true;
		}else {
			return false;
		}
	}

}
