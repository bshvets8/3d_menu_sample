package bogdan_shvets.eleks.com.a3dmenulibrary;

import android.support.annotation.DrawableRes;
import android.view.View;

/**
 * Created by Богдан on 23.10.2016
 */
public class MenuItem {

	@DrawableRes
	private int mDrawableRes;
	private View.OnClickListener mListener;

	@DrawableRes
	public int getDrawableRes() {
		return mDrawableRes;
	}

	public void setDrawableRes(@DrawableRes int drawableRes) {
		this.mDrawableRes = drawableRes;
	}

	public View.OnClickListener getListener() {
		return mListener;
	}

	public void setListener(View.OnClickListener listener) {
		this.mListener = listener;
	}
}
