package com.dcapi.picturegame.view;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dcapi.mymap.R;
import com.dcapi.picturegame.picutils.ImagePiece;
import com.dcapi.picturegame.picutils.ImageSplitterUtil;

/**
 * Created by Dcapi on 2015/12/23.
 * 自定义容器，对应游戏界面的布局
 */
public class GamePintuLayout extends RelativeLayout implements View.OnClickListener {
    public int[]  strGamePicBackground = {R.drawable.gamebk1,R.drawable.gamebk2,R.drawable.gamebk3,R.drawable.gamebk4};
    // 初始情况下，游戏第一关，是切成三行三列的
    private int mColumn = 3;
    //容器的内边距
    private int mPadding;
    // 每张小图之间的距离（横、纵） dp
    private int mMargin = 3;

    private ImageView[] mGamePintuItems;
    //每张小图的长宽，正方形
    private int mItemWidth;
    // 游戏的图片
    private Bitmap mBitmap;
    //返回的切图的信息
    private List<ImagePiece> mItemBitmaps;

    private boolean once;

    /**
     * 游戏面板的宽度，也就是容器的宽度
     *可以在布局文件中设定，matchparent，我们在这里需要动态纠正为正方形
     */
    private int mWidth;

    private boolean isGameSuccess;
    private boolean isGameOver;

    public interface GamePintuListener
    {
        void nextLevel(int nextLevel);

        void timechanged(int currentTime);

        void gameover();
    }

    public GamePintuListener mListener;

    /**
     * 设置接口回调
     *
     * @param mListener
     */
    public void setOnGamePintuListener(GamePintuListener mListener)
    {
        this.mListener = mListener;
    }

    private int mLevel = 1;
    private static final int TIME_CHANGED = 0x110;
    private static final int NEXT_LEVEL = 0x111;

    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case TIME_CHANGED:
                    if (isGameSuccess || isGameOver || isPause)
                        return;
                    if (mListener != null)
                    {
                        mListener.timechanged(mTime);
                    }
                    if (mTime == 0)
                    {
                        isGameOver = true;
                        mListener.gameover();
                        return;
                    }
                    mTime--;
                    mHandler.sendEmptyMessageDelayed(TIME_CHANGED, 1000);

                    break;
                case NEXT_LEVEL:
                    mLevel = mLevel + 1;
                    if (mListener != null)
                    {
                        mListener.nextLevel(mLevel);
                    } else
                    {
                        nextLevel();
                    }
                    break;

            }
        };
    };

    private boolean isTimeEnabled = false;
    private int mTime;

    /**
     * 设置是否开启时间
     *
     * @param isTimeEnabled
     */
    public void setTimeEnabled(boolean isTimeEnabled)
    {
        this.isTimeEnabled = isTimeEnabled;
    }
    /**
     *三个构造函数，第一个调用第二个，第二个调用第三个
     *
     */
    public GamePintuLayout(Context context)
    {
        this(context, null);
    }

    public GamePintuLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public GamePintuLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }
    /**
     *初始化
     */
    private void init()
    {
        /**
         *mMargin 初始化设置的为3dp，现在给转换成px
         *TypedValue用于单位转换
         */
        mMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                3, getResources().getDisplayMetrics());
        /**
         *调用min方法获取布局内边距的最小值，因为布局的内边距在布局文件中可以直接写
         *所以在这里动态的修改，获取最小的，然后让其一直
         *min方法由自己创建
         */
        mPadding = min(getPaddingLeft(), getPaddingRight(), getPaddingTop(),
                getPaddingBottom());
    }
    /**
     *用于修改布局的宽度，即游戏面板的宽度，设置为正方形
     *原参数在布局文件中已经设置好
     *
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 取宽和高中的小值
        mWidth = Math.min(getMeasuredHeight(), getMeasuredWidth());

        if (!once)
        {
            // 进行切图，以及排序
            initBitmap();
            // 设置ImageView(Item)的宽高等属性
            initItem();
            // 判断是否开启时间
            checkTimeEnable();

            once = true;
        }
        //重制游戏面板的宽度
        setMeasuredDimension(mWidth, mWidth);

    }

    private void checkTimeEnable()
    {
        if (isTimeEnabled)
        {
            // 根据当前等级设置时间
            countTimeBaseLevel();
            mHandler.sendEmptyMessage(TIME_CHANGED);
        }

    }

    private void countTimeBaseLevel()
    {
        mTime = (int) Math.pow(2, mLevel) * 60;
    }

    /**
     * 进行切图，以及排序
     */
    private void initBitmap()
    {
        if (mBitmap == null)
        {
            //获取游戏的图片
            mBitmap = BitmapFactory.decodeResource(getResources(),
                    strGamePicBackground[(int)(Math.random()*4%4)]);
        }
        /**
         *mItemBitmaps List 进行切图
         *初始关卡设置为3*3的面板，mColumn = 3；
         */
        mItemBitmaps = ImageSplitterUtil.splitImage(mBitmap, mColumn);

        // 使用sort完成乱序
        Collections.sort(mItemBitmaps, new Comparator<ImagePiece>() {
            @Override
            public int compare(ImagePiece a, ImagePiece b) {
                //返回一个随机数来随机排序
                return Math.random() > 0.5 ? 1 : -1;
            }
        });

    }

    /**
     * 设置ImageView(Item)的宽高等属性
     */
    private void initItem()
    {
        //每个小图片的宽度
        mItemWidth = (mWidth - mPadding * 2 - mMargin * (mColumn - 1))
                / mColumn;
        //数组
        mGamePintuItems = new ImageView[mColumn * mColumn];
        // 生成Item，设置Rule，进行遍历
        for (int i = 0; i < mGamePintuItems.length; i++)
        {
            ImageView item = new ImageView(getContext());
            //监听点击事件
            item.setOnClickListener(this);
            //为该itme控件设置图片
            item.setImageBitmap(mItemBitmaps.get(i).getBitmap());
            //传递到数组
            mGamePintuItems[i] = item;

            item.setId(i + 1);

            // 在Item的tag中存储了index
            item.setTag(i + "_" + mItemBitmaps.get(i).getIndex());
            //每个图片的布局 lp
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    mItemWidth, mItemWidth);

            // 设置Item间横向间隙，通过rightMargin
            // 不是最后一列，那么图片间有右边距
            if ((i + 1) % mColumn != 0)
            {
                lp.rightMargin = mMargin;
            }
            // 不是第一列，设置规则，根据前一个图片，确定其右边的图片
            if (i % mColumn != 0)
            {
                lp.addRule(RelativeLayout.RIGHT_OF,
                        mGamePintuItems[i - 1].getId());
            }
            // 如果不是第一行 , 设置topMargin和rule，根据上一行，确定下一刚的图的规则
            if ((i + 1) > mColumn)
            {
                lp.topMargin = mMargin;
                lp.addRule(RelativeLayout.BELOW,
                        mGamePintuItems[i - mColumn].getId());
            }
            addView(item, lp);
        }

    }

    public void restart()
    {
        isGameOver = false;
        mColumn--;
        nextLevel();
    }

    private boolean isPause ;

    public void pause()
    {
        isPause = true ;
        mHandler.removeMessages(TIME_CHANGED);
    }

    public void resume()
    {
        if(isPause)
        {
            isPause = false ;
            mHandler.sendEmptyMessage(TIME_CHANGED);
        }
    }

    public void nextLevel()
    {
        this.removeAllViews();
        mAnimLayout = null;
        mColumn++;
        isGameSuccess = false;
        checkTimeEnable();
        initBitmap();
        initItem();
    }

    /**
     * 获取多个参数的最小值
     *可以传入多个参数
     */
    private int min(int... params)
    {
        int min = params[0];

        for (int param : params)
        {
            if (param < min)
                min = param;
        }
        return min;
    }

    private ImageView mFirst;
    private ImageView mSecond;

    @Override
    public void onClick(View v)
    {
        if (isAniming)
            return;

        // 两次点击同一个Item
        if (mFirst == v)
        {
            mFirst.setColorFilter(null);
            mFirst = null;
            return;
        }
        if (mFirst == null)
        {
            mFirst = (ImageView) v;
            mFirst.setColorFilter(Color.parseColor("#55FF0000"));
        } else
        {
            mSecond = (ImageView) v;
            // 交换我们的Item
            exchangeView();
        }

    }

    /**
     * 动画层
     */
    private RelativeLayout mAnimLayout;
    private boolean isAniming;

    /**
     * 交换我们的Item
     */
    private void exchangeView()
    {
        mFirst.setColorFilter(null);
        // 构造我们的动画层
        setUpAnimLayout();

        ImageView first = new ImageView(getContext());
        final Bitmap firstBitmap = mItemBitmaps.get(
                getImageIdByTag((String) mFirst.getTag())).getBitmap();
        first.setImageBitmap(firstBitmap);
        LayoutParams lp = new LayoutParams(mItemWidth, mItemWidth);
        lp.leftMargin = mFirst.getLeft() - mPadding;
        lp.topMargin = mFirst.getTop() - mPadding;
        first.setLayoutParams(lp);
        mAnimLayout.addView(first);

        ImageView second = new ImageView(getContext());
        final Bitmap secondBitmap = mItemBitmaps.get(
                getImageIdByTag((String) mSecond.getTag())).getBitmap();
        second.setImageBitmap(secondBitmap);
        LayoutParams lp2 = new LayoutParams(mItemWidth, mItemWidth);
        lp2.leftMargin = mSecond.getLeft() - mPadding;
        lp2.topMargin = mSecond.getTop() - mPadding;
        second.setLayoutParams(lp2);
        mAnimLayout.addView(second);

        // 设置动画
        TranslateAnimation anim = new TranslateAnimation(0, mSecond.getLeft()
                - mFirst.getLeft(), 0, mSecond.getTop() - mFirst.getTop());
        anim.setDuration(300);
        anim.setFillAfter(true);
        first.startAnimation(anim);

        TranslateAnimation animSecond = new TranslateAnimation(0,
                -mSecond.getLeft() + mFirst.getLeft(), 0, -mSecond.getTop()
                + mFirst.getTop());
        animSecond.setDuration(300);
        animSecond.setFillAfter(true);
        second.startAnimation(animSecond);

        // 监听动画
        anim.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
                mFirst.setVisibility(View.INVISIBLE);
                mSecond.setVisibility(View.INVISIBLE);

                isAniming = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {

                String firstTag = (String) mFirst.getTag();
                String secondTag = (String) mSecond.getTag();

                mFirst.setImageBitmap(secondBitmap);
                mSecond.setImageBitmap(firstBitmap);

                mFirst.setTag(secondTag);
                mSecond.setTag(firstTag);

                mFirst.setVisibility(View.VISIBLE);
                mSecond.setVisibility(View.VISIBLE);

                mFirst = mSecond = null;
                mAnimLayout.removeAllViews();
                // 判断用户游戏是否成功
                checkSuccess();
                isAniming = false;
            }
        });

    }

    /**
     * 判断用户游戏是否成功
     */
    private void checkSuccess()
    {
        boolean isSuccess = true;

        for (int i = 0; i < mGamePintuItems.length; i++)
        {
            ImageView imageView = mGamePintuItems[i];
            if (getImageIndexByTag((String) imageView.getTag()) != i)
            {
                isSuccess = false;
            }
        }

        if (isSuccess)
        {
            isGameSuccess = true;
            mHandler.removeMessages(TIME_CHANGED);

            Toast.makeText(getContext(), "过关了， 请挑战下一关 !!!",
                    Toast.LENGTH_LONG).show();
            mHandler.sendEmptyMessage(NEXT_LEVEL);
        }

    }

    /**
     * 根据tag获取Id
     *
     * @param tag
     * @return
     */
    public int getImageIdByTag(String tag)
    {
        String[] split = tag.split("_");
        return Integer.parseInt(split[0]);
    }

    public int getImageIndexByTag(String tag)
    {
        String[] split = tag.split("_");
        return Integer.parseInt(split[1]);
    }

    /**
     * 构造我们的动画层
     */
    private void setUpAnimLayout()
    {
        if (mAnimLayout == null)
        {
            mAnimLayout = new RelativeLayout(getContext());
            addView(mAnimLayout);
        }
    }
}
