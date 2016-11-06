package bogdan_shvets.eleks.com.a3dmenulibrary;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.view.View;

import java.util.Locale;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import bogdan_shvets.eleks.com.a3dmenulibrary.util.Helper;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glViewport;

/**
 * Created by Богдан on 22.10.2016
 */
public class CubeRenderer implements GLSurfaceView.Renderer {

	private final static long ROTATION_TIME = 500;

	private static final int FRONT = 0;
	private static final int LEFT = 1;
	private static final int BACK = 2;
	private static final int RIGHT = 3;
	private static final int TOP = 4;
	private static final int BOTTOM = 5;

	private Rectangle[] mRectangles = {
			new Rectangle(), new Rectangle(), new Rectangle(),
			new Rectangle(), new Rectangle(), new Rectangle()
	};

	private final float[] mTextureCoordinates = {
			0.0f, 1.0f,
			1.0f, 1.0f,
			0.0f, 0.0f,
			1.0f, 0.0f
	};

	private float[][] mCoordinates = {
			// FRONT
			{
					-1f, -1f, 1f,
					1f, -1f, 1f,
					-1f, 1f, 1f,
					1f, 1f, 1f
			},
			// LEFT
			{
					-1f, -1f, -1f,
					-1f, -1f, 1f,
					-1f, 1f, -1f,
					-1f, 1f, 1f
			},
			// BACK
			{
					-1f, -1f, -1f,
					1f, -1f, -1f,
					-1f, 1f, -1f,
					1f, 1f, -1f
			},
			// RIGHT
			{
					1f, -1f, 1f,
					1f, -1f, -1f,
					1f, 1f, 1f,
					1f, 1f, -1f
			},
			// TOP
			{
					-1f, 1f, 1f,
					1f, 1f, 1f,
					-1f, 1f, -1f,
					1f, 1f, -1f
			},
			// BOTTOM
			{
					-1f, -1f, 1f,
					1f, -1f, 1f,
					-1f, -1f, -1f,
					1f, -1f, -1f
			}
	};

	private int mProgram;
	private int[] mTextureIndexes;

	private float[] mMVPMatrix = new float[16];
	private float[] mModelMatrix = new float[16];
	private float[] mViewMatrix = new float[16];
	private float[] mProjectionMatrix = new float[16];

	private Context mContext;
	private MenuItem[] mMenuItems;
	private int mCurrentPosition = FRONT;

	private int mRotationAngle;
	private long mRotationStartTime;
	private int mCurrentRotationAngle;

	public CubeRenderer(Context mContext) {
		this.mContext = mContext;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		for (int i = 0; i < mRectangles.length; i++) {
			mRectangles[i].setTextureCoordinates(mTextureCoordinates);
			mRectangles[i].setCoordinates(mCoordinates[i]);
		}

		glClearColor(0.5f, 0.5f, 0.5f, 0.5f);

		glEnable(GL_DEPTH_TEST);

		Matrix.setIdentityM(mModelMatrix, 0);

		mProgram = Helper.linkProgram(
				Helper.compileShader(Rectangle.VERTEX_SHADER, GL_VERTEX_SHADER),
				Helper.compileShader(Rectangle.FRAGMENT_SHADER, GL_FRAGMENT_SHADER)
		);

		mTextureIndexes = new int[mRectangles.length];
//		for (int i = 0; i < mTextureIndexes.length; i++)
//			mTextureIndexes[i] = Helper.loadTexture(mContext, mMenuItems[i].getDrawableRes());

		glUseProgram(mProgram);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		glViewport(0, 0, width, height);

		final float ratio = (float) width / height;
		final float left = -ratio;
		final float right = ratio;
		final float bottom = -1.0f;
		final float top = 1.0f;
		final float near = 1.0f;
		final float far = 10.0f;

		Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);

//		final float eyeX = (float) (4 * Math.sin(Math.toRadians(mAzimuthalRotation)) * Math.cos(Math.toRadians(mZenithalRotation)));
//		final float eyeY = (float) (4 * Math.sin(Math.toRadians(mAzimuthalRotation)) * Math.sin(Math.toRadians(mZenithalRotation)));
//		final float eyeZ = (float) (4 * Math.cos(Math.toRadians(mAzimuthalRotation)));

		if (SystemClock.uptimeMillis() > (mRotationStartTime + ROTATION_TIME)) {
			mCurrentRotationAngle += mRotationAngle;
			mRotationAngle = 0;
		}

		float delta = ((SystemClock.uptimeMillis() - mRotationStartTime) / ROTATION_TIME) * mRotationAngle;

		mRotationAngle += delta;
		mCurrentRotationAngle += delta;

		final float eyeX = (float) (Math.cos(Math.toRadians(mCurrentRotationAngle)) * 4f);
		final float eyeY = 0f;
		final float eyeZ = (float) (Math.sin(Math.toRadians(mCurrentRotationAngle)) * 4f);

		final float lookX = 0.0f;
		final float lookY = 0.0f;
		final float lookZ = 0.0f;

		final float upX = 0.0f;
		final float upY = 1.0f;
		final float upZ = 0.0f;

		Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

		Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

		int i = 0;
		for (Rectangle rectangle : mRectangles)
			rectangle.draw(mMVPMatrix, mProgram, mTextureIndexes[i++]);
	}

	public void setMenuItems(MenuItem[] menuItems) {
		if (menuItems.length != mRectangles.length)
			throw new IllegalArgumentException(String.format(Locale.getDefault(), "Set %d MenuItems", mRectangles.length));

		mMenuItems = menuItems;
	}

	public void onUp() {

	}

	public void onDown() {

	}

	public void onLeft() {
		if (++mCurrentPosition > RIGHT)
			mCurrentPosition = FRONT;

		mRotationStartTime = SystemClock.uptimeMillis();
		mRotationAngle = -90;
	}

	public void onRight() {
		if (--mCurrentPosition < FRONT)
			mCurrentPosition = RIGHT;

		mRotationStartTime = SystemClock.uptimeMillis();
		mRotationAngle = -90;
	}

	public void onSingleTap() {
		View.OnClickListener clickListener = mMenuItems[mCurrentPosition].getListener();
		if (clickListener != null)
			clickListener.onClick(new View(mContext));
	}
}
