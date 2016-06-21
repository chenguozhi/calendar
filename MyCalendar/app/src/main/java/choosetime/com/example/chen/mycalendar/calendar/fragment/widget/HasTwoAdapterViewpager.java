package choosetime.com.example.chen.mycalendar.calendar.fragment.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

import choosetime.com.example.chen.mycalendar.calendar.abs.RefreshListener;

/**
 * Created by Administrator on 2016/6/13 0013.
 */
public class HasTwoAdapterViewpager extends ViewPager {
    public HasTwoAdapterViewpager(Context context) {
        super(context);
//        setViewPagerScrollSpeed( );
    }

    public HasTwoAdapterViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
//        setViewPagerScrollSpeed( );
    }

    private RefreshListener listener;

    public void setListener(RefreshListener listener) {
        this.listener = listener;
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        //刷新，滚动到相应的位置
        if (visibility==VISIBLE){//在viewpager显示前，刷新
            if (listener!=null){
                listener.refreshListener(HasTwoAdapterViewpager.this);
            }
        }
    }
    /**
    * 设置ViewPager的滑动速度
    *
    * */
    private void setViewPagerScrollSpeed( ){
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller( HasTwoAdapterViewpager.this.getContext( ) );
            mScroller.set( HasTwoAdapterViewpager.this, scroller);
        }catch(NoSuchFieldException e){

        }catch (IllegalArgumentException e){

        }catch (IllegalAccessException e){

        }
    }
        public class FixedSpeedScroller extends Scroller {
        private int mDuration = 0;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }


        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }
}
