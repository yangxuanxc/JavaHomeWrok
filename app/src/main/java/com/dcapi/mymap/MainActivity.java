package com.dcapi.mymap;



import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

public class MainActivity extends Activity {

	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sp = getSharedPreferences("config", MODE_PRIVATE);
		Editor editor = sp.edit();
		//如果App第一次安装，那么就运行FirstTimeInActivity.class并且把first设置yes
		//否则直接运行
		if (sp.getString("first", "").equals("")) {
			editor.putString("first", "yes");
			editor.commit();
			startActivity(new Intent(this, FirstTimeInActivity.class));
		}

	}

}
