package bogdan_shvets.eleks.com.a3dmenulibrary;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import bogdan_shvets.eleks.com.a3dmenulibrary.util.CoordinateHelper;
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
public class CarouselRenderer implements GLSurfaceView.Renderer {

	private static final float DISTANCE_FROM_CENTER = 3f;

	private Rectangle[] mRectangles;
	private final float[] mTextureCoordinates = {
			0.0f, 1.0f,
			1.0f, 1.0f,
			0.0f, 0.0f,
			1.0f, 0.0f
	};

	private int mProgram;
	private int[] mTextureIndexes;

	private float[] mMVPMatrix = new float[16];
	private float[] mModelMatrix = new float[16];
	private float[] mViewMatrix = new float[16];
	private float[] mProjectionMatrix = new float[16];

	private Bitmap[] mBitmaps;

	private String mBackgroundColor = "#9E9E9E";
	private float mRotation;
	private float mScale = 5.0f;

	private boolean mWasInited = false;

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		int color = Color.parseColor(mBackgroundColor);
		glClearColor(Color.red(color), Color.green(color), Color.blue(color), Color.alpha(color));

//		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);

		mProgram = Helper.linkProgram(
				Helper.compileShader(Rectangle.VERTEX_SHADER, GL_VERTEX_SHADER),
				Helper.compileShader(Rectangle.FRAGMENT_SHADER, GL_FRAGMENT_SHADER)
		);

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

		if (!mWasInited) {
			initRectangles();
			initTextures();
			mWasInited = true;
		}

		final float eyeX = (float) (mScale * Math.sin(Math.toRadians(mRotation)) * Math.cos(Math.toRadians(0)));
		final float eyeY = 0.0f;
		final float eyeZ = (float) (mScale * Math.cos(Math.toRadians(mRotation)));

		final float lookX = 0.0f;
		final float lookY = 0.0f;
		final float lookZ = 0.0f;

		final float upX = 0.0f;
		final float upY = 1.0f;
		final float upZ = 0.0f;

		Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

		Matrix.setIdentityM(mModelMatrix, 0);

		Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

		if (mWasInited)
			drawRectangles();
	}

	public void setBackgroundColor(String color) {
		mBackgroundColor = color;
	}

	public float getRotation() {
		return mRotation;
	}

	public void setRotation(float rotation) {
		this.mRotation = rotation;
	}

	public void setBitmaps(Bitmap[] bitmaps) {
		this.mBitmaps = bitmaps;
		mTextureIndexes = new int[bitmaps.length];
		mRectangles = new Rectangle[bitmaps.length];
		mWasInited = false;
	}

	private void initRectangles() {
		float offset = 360f / mRectangles.length;
		float width = offset / 2;

		float currentOffset = 0f;

		for (int i = 0; i < mRectangles.length; i++, currentOffset += offset) {
			mRectangles[i] = new Rectangle();
			mRectangles[i].setTextureCoordinates(mTextureCoordinates);
			mRectangles[i].setCoordinates(
					CoordinateHelper.fromSpherical(
							CoordinateHelper.getSphericalCoordinatesForRectangle(DISTANCE_FROM_CENTER, width, currentOffset)));
		}
	}

	private void initTextures() {
		for (int i = 0; i < mTextureIndexes.length; i++)
			mTextureIndexes[i] = Helper.loadTexture(mBitmaps[i]);
	}

	private void drawRectangles() {
		for (int i = 0; i < mRectangles.length; i++)
			mRectangles[i].draw(mMVPMatrix, mProgram, mTextureIndexes[i]);
	}
}
