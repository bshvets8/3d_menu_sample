package bogdan_shvets.eleks.com.a3dmenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import bogdan_shvets.eleks.com.a3dmenulibrary.CarouselGLSurfaceView;
import bogdan_shvets.eleks.com.a3dmenulibrary.MenuItem;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CarouselGLSurfaceView glSurfaceView = new CarouselGLSurfaceView(this, new MenuItem[]{
				new MenuItem() {{
					setDrawableRes(R.drawable.texture);
					setListener(new ClickListener(1));
				}},
				new MenuItem() {{
					setDrawableRes(R.drawable.texture);
					setListener(new ClickListener(2));
				}},
				new MenuItem() {{
					setDrawableRes(R.drawable.texture);
					setListener(new ClickListener(3));
				}},
				new MenuItem() {{
					setDrawableRes(R.drawable.text1);
					setListener(new ClickListener(4));
				}},
				new MenuItem() {{
					setDrawableRes(R.drawable.text1);
					setListener(new ClickListener(5));
				}},
				new MenuItem() {{
					setDrawableRes(R.drawable.text1);
					setListener(new ClickListener(6));
				}}
		});

		setContentView(glSurfaceView);
	}

	private class ClickListener implements View.OnClickListener {

		private final int i;

		private ClickListener(int i) {
			this.i = i;
		}

		@Override
		public void onClick(View v) {
			Toast.makeText(MainActivity.this, Integer.toString(i), Toast.LENGTH_SHORT).show();
		}
	}
}
