package bogdan_shvets.eleks.com.a3dmenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import bogdan_shvets.eleks.com.a3dmenulibrary.CubeGLSurfaceView;
import bogdan_shvets.eleks.com.a3dmenulibrary.MenuItem;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CubeGLSurfaceView glSurfaceView = new CubeGLSurfaceView(this);
		glSurfaceView.setMenuItems(new MenuItem[] {
				new MenuItem(){{setDrawableRes(R.drawable.texture);}},
				new MenuItem(){{setDrawableRes(R.drawable.texture);}},
				new MenuItem(){{setDrawableRes(R.drawable.texture);}},
				new MenuItem(){{setDrawableRes(R.drawable.texture);}},
				new MenuItem(){{setDrawableRes(R.drawable.texture);}},
				new MenuItem(){{setDrawableRes(R.drawable.texture);}}
		});
		setContentView(glSurfaceView);
	}
}
