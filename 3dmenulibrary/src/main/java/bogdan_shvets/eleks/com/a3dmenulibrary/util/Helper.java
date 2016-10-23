package bogdan_shvets.eleks.com.a3dmenulibrary.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
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
import static android.opengl.GLES20.GL_NEAREST;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLUtils.texImage2D;

/**
 * Created by Богдан on 09.08.2016
 */
public class Helper {

	@Retention(RetentionPolicy.SOURCE)
	@Target(ElementType.PARAMETER)
	@IntDef({GL_VERTEX_SHADER, GL_FRAGMENT_SHADER})
	public @interface ShaderType {
	}

	private static final String SHADER_ERROR = "SHADER_ERROR";
	private static final String PROGRAM_ERROR = "PROGRAM_ERROR";

	public static final int FLOAT_BYTES_SIZE = 4;
	public static final int SHORT_BYTES_SIZE = 2;

	public static int compileShader(String shaderCode, @ShaderType int shaderType) {
		int shader = glCreateShader(shaderType);

		glShaderSource(shader, shaderCode);
		glCompileShader(shader);

		IntBuffer compileStatusBuffer = IntBuffer.allocate(1);
		glGetShaderiv(shader, GL_COMPILE_STATUS, compileStatusBuffer);

		if (compileStatusBuffer.get(0) == 0) {
			Log.e(SHADER_ERROR, "Shader compilation error " +
					(shaderType == GL_VERTEX_SHADER ? "VERTEX" : "FRAGMENT" +
							"\n " + glGetShaderInfoLog(shader)));

			glDeleteShader(shader);
			return 0;
		}

		return shader;
	}

	public static int linkProgram(int vertexShader, int fragmentShader) {
		int program = glCreateProgram();

		glAttachShader(program, vertexShader);
		glAttachShader(program, fragmentShader);

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

	public static int loadTexture(Context context, @DrawableRes int resourceId) {
		final int[] textureHandle = new int[1];

		glGenTextures(1, textureHandle, 0);

		if (textureHandle[0] != 0) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inScaled = false;

			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

			glBindTexture(GL_TEXTURE_2D, textureHandle[0]);

			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

			texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);

			bitmap.recycle();
		}

		if (textureHandle[0] == 0) {
			Log.e(PROGRAM_ERROR, "Error loading texture");
		}

		return textureHandle[0];
	}

}
