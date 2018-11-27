package net.android.anko.utils.banner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.android.anko.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerBanner extends FrameLayout implements CounterTimer.OnTimerListener {

    //普通的RecyclerView  Banner
    private RecyclerView mRecyclerView;

    //数据集
    private List mData;

    //指示器的容器
    private LinearLayout mIndicatorLayout;

    //指示器的view
    private List<ImageView> mIndicatorViews;

    //自定义指示器的资源
    private int[] mIndicatorIds;

    //指示所在位置
    private int mOrientation;

    //循环计时器
    private CounterTimer mTimer;

    private Boolean canLooper = false;

    //指示器在底部的方向(默认居中)
    public interface Orientation {
        int LEFT = 1;
        int RIGHT = 2;
    }

    public RecyclerBanner(@NonNull Context context) {
        this(context, null);
    }

    public RecyclerBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerBanner(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取自定义的值
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RecyclerBanner);
        mOrientation = a.getInteger(R.styleable.RecyclerBanner_orientation, 0);
        canLooper = a.getBoolean(R.styleable.RecyclerBanner_canLoop, false);
        a.recycle();

        mData = new ArrayList<>();
        mIndicatorViews = new ArrayList<>();

        //创建广告栏
        BannerLayoutManager bannerLayout = new BannerLayoutManager(context);
        mRecyclerView = new RecyclerView(context);
        mRecyclerView.setLayoutManager(bannerLayout);

        //创建一个放指示器的容器
        mIndicatorLayout = new LinearLayout(context);

        addView(mRecyclerView);
        addView(mIndicatorLayout);

        if (canLooper) {
            mTimer = new CounterTimer(context, 5000);
            mTimer.setListener(this);
        }
    }

    public RecyclerBanner setOrientation(int orientation) {
        this.mOrientation = orientation;
        return this;
    }

    public RecyclerBanner setData(List<?> mData) {
        this.mData = mData;
        return this;
    }

    public RecyclerBanner setTag(String tag) {
        if (canLooper) {
            mTimer.setTag(tag);
        }
        return this;
    }

    //初始化banner并加入
    @SuppressLint("ClickableViewAccessibility")
    public RecyclerBanner setAdapter(RecyclerView.Adapter mAdapter) {
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                //在检测到数据更新时重新绘制指示器的数量
                updateIndicator();
            }
        });

        //重写OnScrollListener实现相当于viewPager的OnPageChangeListener
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //停止了滑动的状态
                if (newState == 0 && mData.size() > 1) {
                    LinearLayoutManager layout = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int current = layout.findFirstVisibleItemPosition();
                    setPickIndicator(current % mData.size());
                }
            }
        });

        //重写touch事件，用户在滑动的时候，计时器暂停，松开则继续
        mRecyclerView.setOnTouchListener((view, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (canLooper) {
                        mTimer.pause();
                    }
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_OUTSIDE:
                    if (canLooper) {
                        mTimer.start();
                    }
                    break;
                default:
                    break;
            }
            return false;
        });

        //为banner指定snapHelper
        //每次滑动一页
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);
        return this;
    }

    public RecyclerBanner setIndicator(int[] mIndicatorIds) {
        this.mIndicatorIds = mIndicatorIds;
        return this;
    }

    /**
     * 根据数据集创建指示器个数
     * 数据不为空就先定位到第1000个
     * 指示器选中弟1000%mData.size()个
     */
    private void updateIndicator() {
        mIndicatorViews.clear();
        mIndicatorLayout.removeAllViews();

        if (mData.size() > 1) {
            for (int i = 0; i < mData.size(); i++) {
                ImageView dots = new ImageView(getContext());
                dots.setPadding(5, 0, 5, 0);
                dots.setImageResource(mIndicatorIds[0]);
                mIndicatorViews.add(dots);
                mIndicatorLayout.addView(dots);
            }
            mIndicatorLayout.requestLayout();
        }

        if (!mData.isEmpty() && mData.size() > 1) {
            mRecyclerView.post(() -> {
                mRecyclerView.scrollToPosition(mData.size() * 500);
                mIndicatorViews.get(0).setImageResource(mIndicatorIds[1]);
                if (canLooper) {
                    mTimer.start();
                }
            });
        }
    }

    private void setPickIndicator(int position) {
        for (int i = 0; i < mData.size(); i++) {
            mIndicatorViews.get(i).setImageResource(mIndicatorIds[0]);
        }
        mIndicatorViews.get(position).setImageResource(mIndicatorIds[1]);
    }

    /**
     * 在xml绘制完毕之后设置指示器的方向
     * margin 就可以在这里设置
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        MarginLayoutParams mLayoutParams = (MarginLayoutParams) mIndicatorLayout.getLayoutParams();
        mLayoutParams.setMargins(10, 10, 10, 10);
        mIndicatorLayout.setLayoutParams(mLayoutParams);

        switch (mOrientation) {
            case Orientation.LEFT:
                mIndicatorLayout.setGravity(Gravity.BOTTOM | Gravity.START);
                break;

            case Orientation.RIGHT:
                mIndicatorLayout.setGravity(Gravity.BOTTOM | Gravity.END);
                break;

            default:
                mIndicatorLayout.setGravity(Gravity.BOTTOM | Gravity.CENTER);
                break;
        }
    }


    //设置指示器方向
    public void setIndicatorOrientation(int mOrientation) {
        switch (mOrientation) {
            case Orientation.LEFT:
                mIndicatorLayout.setGravity(Gravity.BOTTOM | Gravity.START);
                break;

            case Orientation.RIGHT:
                mIndicatorLayout.setGravity(Gravity.BOTTOM | Gravity.END);
                break;

            default:
                mIndicatorLayout.setGravity(Gravity.BOTTOM | Gravity.CENTER);
                break;
        }
        mIndicatorLayout.requestLayout();
    }


    //设置margin
    public void setIndicatorMargin(int left, int top, int right, int bottom) {
        MarginLayoutParams mLayoutParams = (MarginLayoutParams) mIndicatorLayout.getLayoutParams();
        mLayoutParams.setMargins(left, top, right, bottom);
        mIndicatorLayout.setLayoutParams(mLayoutParams);
    }


    /**
     * 轮播时间的回调
     * 取名就是时间走动声音的拟声词
     * 时间到了就下一张
     */
    @Override
    public void onTick() {
        if (!mData.isEmpty()) {
            LinearLayoutManager layout = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            int current = layout.findFirstCompletelyVisibleItemPosition();
            if (current < Integer.MAX_VALUE) {
                current++;
                mRecyclerView.smoothScrollToPosition(current);
            }
        }
    }
}
