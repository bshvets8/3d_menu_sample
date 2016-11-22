package bogdan_shvets.eleks.com.a3dmenulibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import java.util.Calendar;

/**
 * Created by Богдан on 06.11.2016
 */
public class CarouselGLSurfaceView extends GLSurfaceView {

	private static final int MAX_CLICK_DURATION = 200;
	private final float TOUCH_SCALE_FACTOR = 180.0f / 400;
	private long startClickTime;
	private float mPreviousX;

	private CarouselRenderer mCarouselRenderer;

	private MenuItem[] mMenuItems;

	public CarouselGLSurfaceView(@NonNull Context context, @NonNull MenuItem[] menuItems) {
		super(context);
		mCarouselRenderer = new CarouselRenderer();
		mCarouselRenderer.setBitmaps(getBitmaps(menuItems));
		setEGLContextClientVersion(2);
		setRenderer(mCarouselRenderer);

		mMenuItems = menuItems;
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		float x = e.getX();

		switch (e.getAction()) {
			case MotionEvent.ACTION_MOVE:

				float dx = x - mPreviousX;

				mCarouselRenderer.setRotation(mCarouselRenderer.getRotation() + (-dx * TOUCH_SCALE_FACTOR));
				requestRender();
				break;

			case MotionEvent.ACTION_DOWN: {
				startClickTime = Calendar.getInstance().getTimeInMillis();
				break;
			}
			case MotionEvent.ACTION_UP: {
				long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
				if (clickDuration < MAX_CLICK_DURATION) {
					int itemIndex = determineItemNumber();
					OnClickListener listener = mMenuItems[itemIndex].getListener();
					if (listener != null)
						listener.onClick(CarouselGLSurfaceView.this);
				}
			}
		}

		mPreviousX = x;
		return true;
	}

	public void setBackground(@NonNull String color) {
		mCarouselRenderer.setBackgroundColor(color);
	}

	private Bitmap[] getBitmaps(MenuItem[] menuItems) {
		Bitmap[] bitmaps = new Bitmap[menuItems.length];

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = false;

		for (int i = 0; i < bitmaps.length; i++) {
			bitmaps[i] = BitmapFactory.decodeResource(getContext().getResources(), menuItems[i].getDrawableRes(), options);
		}

		return bitmaps;
	}

	private int determineItemNumber() {
		float distanceBetweenItems = 360f / mMenuItems.length;

		float rotation = (mCarouselRenderer.getRotation() - distanceBetweenItems) % 360f;
		if (rotation < 0)
			rotation = 360f + rotation;

		int result = Math.round(rotation / distanceBetweenItems);
		return result >= mMenuItems.length ? 0 : result;
	}
}
