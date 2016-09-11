package bogdan_shvets.eleks.com.a3dmenulibrary;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * Created by Богдан on 10.09.2016
 */
public class CubeRenderer implements GLSurfaceView.Renderer {

	//	Shaders
	final String vertexShader =
			"uniform mat4 u_MVPMatrix;      \n" +

			"attribute vec4 a_Position;     \n" +

			"void main()                    \n" +
			"{                              \n" +
			"gl_Position = a_Position;		\n" +
			"   gl_Position = u_MVPMatrix   \n" +
			"               * a_Position;   \n" +
			"}                              \n";

	final String fragmentShader =
			"precision mediump float;       \n" +

			"uniform vec4 u_Color;          \n" +

			"void main()                    \n" +
			"{                              \n" +
			"   gl_FragColor = u_Color;     \n" +
			"}                              \n";

	//	Matrices
	private float[] mMVPMatrix = new float[16];
	private float[] mModelMatrix = new float[16];
	private float[] mViewMatrix = new float[16];
	private float[] mProjectionMatrix = new float[16];

	//	Indexes
	private int mMVPMatrixIndex;
	private int mPositionIndex;
	private int mColorIndex;

	private static final int COORDINATES_PER_VERTEX = 3;
	private float[] coordinates = {
			// Front face
			-1.0f, 1.0f, 1.0f,
			-1.0f, -1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
			-1.0f, -1.0f, 1.0f,
			1.0f, -1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,

			// Right face
			1.0f, 1.0f, 1.0f,
			1.0f, -1.0f, 1.0f,
			1.0f, 1.0f, -1.0f,
			1.0f, -1.0f, 1.0f,
			1.0f, -1.0f, -1.0f,
			1.0f, 1.0f, -1.0f,

			// Back face
			1.0f, 1.0f, -1.0f,
			1.0f, -1.0f, -1.0f,
			-1.0f, 1.0f, -1.0f,
			1.0f, -1.0f, -1.0f,
			-1.0f, -1.0f, -1.0f,
			-1.0f, 1.0f, -1.0f,

			// Left face
			-1.0f, 1.0f, -1.0f,
			-1.0f, -1.0f, -1.0f,
			-1.0f, 1.0f, 1.0f,
			-1.0f, -1.0f, -1.0f,
			-1.0f, -1.0f, 1.0f,
			-1.0f, 1.0f, 1.0f,

			// Top face
			-1.0f, 1.0f, -1.0f,
			-1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, -1.0f,
			-1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, -1.0f,

			// Bottom face
			1.0f, -1.0f, -1.0f,
			1.0f, -1.0f, 1.0f,
			-1.0f, -1.0f, -1.0f,
			1.0f, -1.0f, 1.0f,
			-1.0f, -1.0f, 1.0f,
			-1.0f, -1.0f, -1.0f
	};

	private FloatBuffer vertexBuffer;

	private int program;

	public CubeRenderer() {
		vertexBuffer = ByteBuffer.allocateDirect(coordinates.length * ShaderHelper.FLOAT_BYTES_SIZE)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		vertexBuffer.put(coordinates).position(0);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);

		final float eyeX = 0.0f;
		final float eyeY = 0.0f;
		final float eyeZ = -0.5f;

		final float lookX = 0.0f;
		final float lookY = 0.0f;
		final float lookZ = -5.0f;

		final float upX = 0.0f;
		final float upY = 1.0f;
		final float upZ = 0.0f;

		Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

		program = ShaderHelper.linkProgram(
			ShaderHelper.compileShader(vertexShader, GLES20.GL_VERTEX_SHADER),
			ShaderHelper.compileShader(fragmentShader, GLES20.GL_FRAGMENT_SHADER)
		);

		glUseProgram(program);

		mMVPMatrixIndex = glGetUniformLocation(program, "u_MVPMatrix");
		mPositionIndex = glGetAttribLocation(program, "a_Position");
		mColorIndex = glGetUniformLocation(program, "u_Color");
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height);

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
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

		long time = SystemClock.uptimeMillis() % 10000L;
		float angleInDegrees = (360.0f / 10000.0f) * ((int) time);

		Matrix.setIdentityM(mModelMatrix, 0);
		Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 1.0f, 0.0f, 1.0f);
		drawRectangle();
	}

	private void drawRectangle() {
		vertexBuffer.position(0);
		glVertexAttribPointer(mPositionIndex, COORDINATES_PER_VERTEX, GL_FLOAT, false,
				COORDINATES_PER_VERTEX * ShaderHelper.FLOAT_BYTES_SIZE, vertexBuffer);
		glEnableVertexAttribArray(mPositionIndex);

		glUniform4f(mColorIndex, 0.5f, 0.5f, 1.0f, 0.5f);

		Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

		GLES20.glUniformMatrix4fv(mMVPMatrixIndex, 1, false, mMVPMatrix, 0);

		glDrawArrays(GL_TRIANGLE_STRIP, 0, coordinates.length / COORDINATES_PER_VERTEX);
	}
}
