package bogdan_shvets.eleks.com.a3dmenulibrary;

import android.support.annotation.IntDef;
import android.util.Log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.IntBuffer;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glBindAttribLocation;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;

/**
 * Created by Богдан on 09.08.2016
 */
public class ShaderHelper {

	@Retention(RetentionPolicy.SOURCE)
	@Target(ElementType.PARAMETER)
	@IntDef({GL_VERTEX_SHADER, GL_FRAGMENT_SHADER})
	public @interface ShaderType {
	}

	private static final String SHADER_ERROR = "SHADER_ERROR";
	private static final String PROGRAM_ERROR = "PROGRAM_ERROR";

	public static final int FLOAT_BYTES_SIZE = 4;

	public static int compileShader(String shaderCode, @ShaderType int shaderType) {
		int shader = glCreateShader(shaderType);

		glShaderSource(shader, shaderCode);
		glCompileShader(shader);

		IntBuffer compileStatusBuffer = IntBuffer.allocate(1);
		glGetShaderiv(shader, GL_COMPILE_STATUS, compileStatusBuffer);

		if (compileStatusBuffer.get(0) == 0) {
			Log.e(SHADER_ERROR, "Shader compilation error");

			glDeleteShader(shader);
			return 0;
		}

		return shader;
	}

	public static int linkProgram(int vertexShader, int fragmentShader, String... attribs) {
		int program = glCreateProgram();

		glAttachShader(program, vertexShader);
		glAttachShader(program, fragmentShader);

		for (int i = 0; i < attribs.length; i++) {
			glBindAttribLocation(program, i, attribs[i]);
		}

		glLinkProgram(program);

		IntBuffer linkStatusBuffer = IntBuffer.allocate(1);
		glGetProgramiv(program, GL_LINK_STATUS, linkStatusBuffer);

		if (linkStatusBuffer.get(0) == 0) {
			Log.e(PROGRAM_ERROR, "Program linking error");

			glDeleteProgram(program);
			return 0;
		}

		return program;
	}

}
