package bogdan_shvets.eleks.com.a3dmenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import bogdan_shvets.eleks.com.a3dmenulibrary.CubeGLSurfaceView;

public class MainActivity extends AppCompatActivity {

	private CubeGLSurfaceView glSurfaceView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		glSurfaceView = new CubeGLSurfaceView(this);
		setContentView(glSurfaceView);
	}
}
