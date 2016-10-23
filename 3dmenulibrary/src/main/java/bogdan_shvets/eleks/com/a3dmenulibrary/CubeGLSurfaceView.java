package bogdan_shvets.eleks.com.a3dmenulibrary;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by Богдан on 11.09.2016
 */
public class CubeGLSurfaceView extends GLSurfaceView {
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;

	private CubeRenderer mRenderer;

	private float mPreviousX;
	private float mPreviousY;

	private GestureDetector mGestureDetector = new GestureDetector(new FlingDetector());

	public CubeGLSurfaceView(Context context) {
		super(context);
		setEGLContextClientVersion(2);
		setRenderer(new CubeRenderer(getContext()));
	}

	@Override
	public void setRenderer(Renderer renderer) {
		if (renderer instanceof CubeRenderer) {
			mRenderer = (CubeRenderer) renderer;
			super.setRenderer(mRenderer);
		} else
			throw new IllegalArgumentException("Only CubeRenderer is allowed");
	}

	public void setMenuItems(MenuItem[] menuItems) {
		mRenderer.setMenuItems(menuItems);
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		mGestureDetector.onTouchEvent(e);

		float x = e.getX();
		float y = e.getY();

		switch (e.getAction()) {
			case MotionEvent.ACTION_MOVE:
				float dx = x - mPreviousX;
				float dy = y - mPreviousY;
//				float azimuth = dx / mScreenWidth * 360;
//				float zenith = dy / mScreenHeight * 360;

//				mRenderer.setAzimuthalRotation(dx);
//				mRenderer.setZenithalRotation(dy);
				requestRender();
		}

		mPreviousX = x;
		mPreviousY = y;

		return true;
	}

	private class FlingDetector extends GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			mRenderer.onSingleTap();
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				mRenderer.onRight();
				return true; // Right to left
			}  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				mRenderer.onLeft();
				return true; // Left to right
			}

			if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
				mRenderer.onDown();
				return true; // Bottom to top
			}  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
				mRenderer.onUp();
				return true; // Top to bottom
			}

			return false;
		}
	}
}
