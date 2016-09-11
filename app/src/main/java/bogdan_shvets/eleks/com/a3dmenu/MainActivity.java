package bogdan_shvets.eleks.com.a3dmenu;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import bogdan_shvets.eleks.com.a3dmenulibrary.CubeRenderer;

public class MainActivity extends AppCompatActivity {

	private GLSurfaceView glSurfaceView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		glSurfaceView = new GLSurfaceView(this);
		glSurfaceView.setEGLContextClientVersion(2);
		glSurfaceView.setRenderer(new CubeRenderer());
		setContentView(glSurfaceView);
	}
}
