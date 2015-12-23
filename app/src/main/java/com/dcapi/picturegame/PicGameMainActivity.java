package com.dcapi.picturegame;

import android.app.Activity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.TextView;
import com.dcapi.mymap.R;
import com.dcapi.picturegame.view.GamePintuLayout;
import com.dcapi.picturegame.view.GamePintuLayout.GamePintuListener;
public class PicGameMainActivity extends Activity {

    private GamePintuLayout mGamePintuLayout;
    private TextView mLevel ;
    private TextView mTime;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picgame_main);

        mTime = (TextView) findViewById(R.id.id_time);
        mLevel = (TextView) findViewById(R.id.id_level);

        mGamePintuLayout = (GamePintuLayout) findViewById(R.id.id_gamepintu);
        mGamePintuLayout.setTimeEnabled(true);

        mGamePintuLayout.setOnGamePintuListener(new GamePintuListener()
        {
            @Override
            public void timechanged(int currentTime)
            {
                mTime.setText(""+currentTime);
            }

            @Override
            public void nextLevel(final int nextLevel)
            {
                new AlertDialog.Builder(PicGameMainActivity.this)
                        .setTitle("Game Info").setMessage("过关啦 !!!")
                        .setPositiveButton("下一关", new OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                mGamePintuLayout.nextLevel();
                                mLevel.setText(""+nextLevel);
                            }
                        }).show();
            }

            @Override
            public void gameover()
            {
                new AlertDialog.Builder(PicGameMainActivity.this)
                        .setTitle("Game Info").setMessage("游戏结束 !!!")
                        .setPositiveButton("再来一次", new OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                mGamePintuLayout.restart();
                            }
                        }).setNegativeButton("退出",new OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        finish();
                    }
                }).show();
            }
        });

    }

    @Override
    protected void onPause()
    {
        super.onPause();

        mGamePintuLayout.pause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mGamePintuLayout.resume();
    }
}
