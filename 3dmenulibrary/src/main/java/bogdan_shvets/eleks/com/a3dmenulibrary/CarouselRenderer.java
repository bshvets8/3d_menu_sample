package bogdan_shvets.eleks.com.a3dmenulibrary;

import android.content.Context;
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

	private Rectangle mRectangle;
	private Rectangle mRectangle1;
	private final float[] mTextureCoordinates = {
			0.0f, 1.0f,
			1.0f, 1.0f,
			0.0f, 0.0f,
			1.0f, 0.0f
	};

	private int mProgram;
	private int mTextureIndex;

	private float[] mMVPMatrix = new float[16];
	private float[] mModelMatrix = new float[16];
	private float[] mViewMatrix = new float[16];
	private float[] mProjectionMatrix = new float[16];

	private Context mContext;

	public CarouselRenderer(Context mContext) {
		this.mContext = mContext;
	}

	float f = 0;
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		mRectangle = new Rectangle();
		mRectangle1 = new Rectangle();
		mRectangle.setCoordinates(CoordinateHelper.fromSpherical(CoordinateHelper.getSphericalCoordinatesForRectangle(2.0f, 90.0f)));
		mRectangle1.setCoordinates(CoordinateHelper.fromSpherical(CoordinateHelper.getSphericalCoordinatesForRectangle(2.0f, 90.0f)));
		mRectangle.setTextureCoordinates(mTextureCoordinates);
		mRectangle1.setTextureCoordinates(mTextureCoordinates);

		glClearColor(0.5f, 0.5f, 0.5f, 0.5f);

//		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);

		mProgram = Helper.linkProgram(
				Helper.compileShader(Rectangle.VERTEX_SHADER, GL_VERTEX_SHADER),
				Helper.compileShader(Rectangle.FRAGMENT_SHADER, GL_FRAGMENT_SHADER)
		);

		mTextureIndex = Helper.loadTexture(mContext, R.drawable.text1);

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

		f++;
		final float eyeX = (float) (4 * Math.sin(Math.toRadians(f / 5)) * Math.cos(Math.toRadians(0)));
		final float eyeY = 0.0f;
		final float eyeZ = (float) (4 * Math.cos(Math.toRadians(f / 5)));

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

		mRectangle.draw(mMVPMatrix, mProgram, mTextureIndex);
		mRectangle1.draw(mMVPMatrix, mProgram, mTextureIndex);
	}
}
