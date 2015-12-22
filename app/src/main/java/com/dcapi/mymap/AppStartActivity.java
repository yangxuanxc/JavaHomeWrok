package com.dcapi.mymap;




import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
//App启动展示启动页
public class AppStartActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splash);

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					//首页启动图片显示2秒后跳转
					Thread.sleep(2000);
				} catch (InterruptedException e) {
				}
				runOnUiThread(new Runnable() {
					/**
					*跳转到MainTivity
					*/
					@Override
					public void run() {
						startActivity(new Intent(AppStartActivity.this, MainActivity.class));
						finish();
					}
				});
			}
		}).start();

	}
}
