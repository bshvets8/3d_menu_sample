package bogdan_shvets.eleks.com.a3dmenulibrary;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 * Created by Богдан on 11.09.2016
 */
public class CubeGLSurfaceView extends GLSurfaceView {

	private CubeRenderer mRenderer;

	public CubeGLSurfaceView(Context context) {
		super(context);
		setEGLContextClientVersion(2);
		setRenderer(new CubeRenderer());
	}

	@Override
	public void setRenderer(Renderer renderer) {
		if (renderer instanceof CubeRenderer) {
			mRenderer = (CubeRenderer) renderer;
			super.setRenderer(mRenderer);
		} else
			throw new IllegalArgumentException("Only CubeRenderer is allowed");
	}

	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
	private float mPreviousX;
	private float mPreviousY;

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		float x = e.getX();
		float y = e.getY();

		switch (e.getAction()) {
			case MotionEvent.ACTION_MOVE:

				float dx = x - mPreviousX;
				float dy = y - mPreviousY;

				mRenderer.setxAngle(mRenderer.getxAngle() + dx * TOUCH_SCALE_FACTOR);
				mRenderer.setyAngle(mRenderer.getyAngle() + dy * TOUCH_SCALE_FACTOR);
				requestRender();
		}

		mPreviousX = x;
		mPreviousY = y;
		return true;
	}
}
