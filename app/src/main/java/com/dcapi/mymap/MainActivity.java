package com.dcapi.mymap;



import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.dcapi.picturegame.PicGameMainActivity;
import com.dcapi.more.MoreMainActivity;
import com.dcapi.luckdraw.LuckDrawMainActivity;
import com.dcapi.talk.TalkMainActivity;


public class MainActivity extends Activity implements RadioGroup.OnCheckedChangeListener {

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
			//测试阶段，后一个参数实际中为 yes，现在改为空进行测试
			editor.putString("first", "");
			editor.commit();
			startActivity(new Intent(this, FirstTimeInActivity.class));
		}
		RadioGroup group = (RadioGroup) findViewById(R.id.radiogroup);
		group.setOnCheckedChangeListener(this);
	}
	/**
	 * 点击四个button分别启动四个功能的入口Activity
	 * */
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub

		switch (checkedId) {
			case R.id.picgame: {
				Intent intent=new Intent(this, PicGameMainActivity.class);
				startActivity(intent);
				break;

			}
			case R.id.zhinanzheng: {
				Intent intent=new Intent(this, TalkMainActivity.class);
				startActivity(intent);
				break;

			}
			case R.id.luckdraw: {
				Intent intent=new Intent(this, LuckDrawMainActivity.class);
				startActivity(intent);
				break;

			}
			case R.id.more: {
				Intent intent=new Intent(this,MoreMainActivity.class);
				startActivity(intent);
				break;

			}
		}
	}

}
