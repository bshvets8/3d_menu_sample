package bogdan_shvets.eleks.com.a3dmenulibrary.shapes;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import bogdan_shvets.eleks.com.a3dmenulibrary.ShaderHelper;

/**
 * Created by Богдан on 09.08.2016
 */
public class Triangle {

	private final String vertexShaderCode =
			"attribute vec4 a_Position;" +
			"void main() {" +
			"	gl_Position = a_Position;" +
			"}";

	private final String fragmentShaderCode =
			"precision mediump float;" +
			"attribute vec a_Color;" +
			"void main() {" +
			"	gl_FragColor = a_Color;" +
			"}";

	private FloatBuffer vertexBuffer;

	private float[] coordinates = {
//			X		Y		Z
		 0.0f,	 0.5f,	 0.0f,
		 0.5f,	-0.5f,	 0.0f,
		-0.5f,	-0.5f,	 0.0f
	};

	private final int mProgram;

	public Triangle() {
		mProgram = ShaderHelper.linkProgram(
				ShaderHelper.compileShader(vertexShaderCode, GLES20.GL_VERTEX_SHADER),
				ShaderHelper.compileShader(fragmentShaderCode, GLES20.GL_FRAGMENT_SHADER),
				"a_Position", "a_Color"
		);

		FloatBuffer floatBuffer =
				ByteBuffer.allocateDirect(coordinates.length * ShaderHelper.FLOAT_BYTES_SIZE)
						.order(ByteOrder.nativeOrder())
						.asFloatBuffer();

		floatBuffer.put(coordinates);
	}
}
