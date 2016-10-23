package bogdan_shvets.eleks.com.a3dmenulibrary;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import bogdan_shvets.eleks.com.a3dmenulibrary.util.Helper;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * Created by Богдан on 16.10.2016
 */
public class Rectangle {

	private static final int VERTEXES_COUNT = 4;
	private static final int COORDINATES_PER_VERTEX = 3;
	private static final int VALUES_PER_TEXTURE_VERTEX = 2;

	//	Shaders
	public static final String VERTEX_SHADER =
			"uniform mat4 u_MVPMatrix;						\n" +

					"attribute vec4 a_Position;				\n" +
					"attribute vec2 a_TexCoordinate;		\n" +

					"varying   vec2 v_TexCoordinate;		\n" +

					"void main()							\n" +
					"{										\n" +
					"	gl_Position = a_Position;			\n" +
					"   gl_Position = u_MVPMatrix   		\n" +
					"   			* a_Position;   		\n" +

					"	v_TexCoordinate = a_TexCoordinate;	\n" +
					"}										\n";

	public static final String FRAGMENT_SHADER =
			"precision mediump float;											\n" +

					"uniform sampler2D u_Texture;								\n" +

					"varying vec2 v_TexCoordinate;								\n" +

					"void main()												\n" +
					"{															\n" +
					"   gl_FragColor = texture2D(u_Texture, v_TexCoordinate);	\n" +
					"}															\n";

	private float[] mTextureCoordinates;

	private float[] mCoordinates;

	private FloatBuffer mVertexBuffer;
	private FloatBuffer mTextureCoordinatesBuffer;

	public Rectangle() {
		mVertexBuffer = ByteBuffer.allocateDirect(VERTEXES_COUNT * COORDINATES_PER_VERTEX * Helper.FLOAT_BYTES_SIZE)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();

		mTextureCoordinatesBuffer = ByteBuffer.allocateDirect(VERTEXES_COUNT * VALUES_PER_TEXTURE_VERTEX
				* Helper.FLOAT_BYTES_SIZE).order(ByteOrder.nativeOrder()).asFloatBuffer();
	}

	public void draw(float[] mvpMatrix, int programIndex, int textureIndex) {
		// BS: retrieving indexes of uniforms and attributes
		int mvpMatrixIndex = glGetUniformLocation(programIndex, "u_MVPMatrix");
		int positionIndex = glGetAttribLocation(programIndex, "a_Position");
		int textureUniformIndex = glGetUniformLocation(programIndex, "u_Texture");
		int textureCoordinateIndex = glGetAttribLocation(programIndex, "a_TexCoordinate");

		// BS: pass coordinates
		mVertexBuffer.put(mCoordinates).position(0);
		glVertexAttribPointer(positionIndex, COORDINATES_PER_VERTEX, GL_FLOAT, false,
				COORDINATES_PER_VERTEX * Helper.FLOAT_BYTES_SIZE, mVertexBuffer);
		glEnableVertexAttribArray(positionIndex);

		// BS: pass texture coordinates
		mTextureCoordinatesBuffer.put(mTextureCoordinates).position(0);
		glVertexAttribPointer(textureCoordinateIndex, VALUES_PER_TEXTURE_VERTEX, GL_FLOAT, false,
				VALUES_PER_TEXTURE_VERTEX * Helper.FLOAT_BYTES_SIZE, mTextureCoordinatesBuffer);
		glEnableVertexAttribArray(textureIndex);

		// BS: pass model view projection matrix
		glUniformMatrix4fv(mvpMatrixIndex, 1, false, mvpMatrix, 0);

		glDrawArrays(GL_TRIANGLE_STRIP, 0, VERTEXES_COUNT);

		// BS: using textures
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textureIndex);
		glUniform1i(textureUniformIndex, 0);
	}

	public void setCoordinates(float[] coordinates) {
		if (coordinates == null || coordinates.length != VERTEXES_COUNT * COORDINATES_PER_VERTEX)
			throw new IllegalArgumentException("You should set 4 coordinates");

		this.mCoordinates = coordinates;
	}

	public void setTextureCoordinates(float[] coordinates) {
		if (coordinates == null || coordinates.length != VERTEXES_COUNT * VALUES_PER_TEXTURE_VERTEX)
			throw new IllegalArgumentException("You should set 4 texture coordinates");

		this.mTextureCoordinates = coordinates;
	}
}
