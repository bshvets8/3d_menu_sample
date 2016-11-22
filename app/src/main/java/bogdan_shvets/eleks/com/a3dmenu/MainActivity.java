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
					setDrawableRes(R.drawable.green);
					setListener(new ClickListener("green"));
				}},
				new MenuItem() {{
					setDrawableRes(R.drawable.red);
					setListener(new ClickListener("red"));
				}},
				new MenuItem() {{
					setDrawableRes(R.drawable.blue);
					setListener(new ClickListener("blue"));
				}},
				new MenuItem() {{
					setDrawableRes(R.drawable.yellow);
					setListener(new ClickListener("yellow"));
				}},
				new MenuItem() {{
					setDrawableRes(R.drawable.aqua);
					setListener(new ClickListener("aqua"));
				}},
				new MenuItem() {{
					setDrawableRes(R.drawable.purple);
					setListener(new ClickListener("purple"));
				}}
		});

		setContentView(glSurfaceView);
	}

	private class ClickListener implements View.OnClickListener {

		private final String str;

		private ClickListener(String str) {
			this.str = str;
		}

		@Override
		public void onClick(View v) {
			Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
		}
	}
}
