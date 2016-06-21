package choosetime.com.example.chen.mycalendar.calendar.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import choosetime.com.example.chen.mycalendar.R;
import choosetime.com.example.chen.mycalendar.calendar.abs.RefreshListener;
import choosetime.com.example.chen.mycalendar.calendar.adpter.MonthCalendarAdpter;
import choosetime.com.example.chen.mycalendar.calendar.adpter.WeekCalendarAdpter;
import choosetime.com.example.chen.mycalendar.calendar.fragment.widget.HandMoveLayout;
import choosetime.com.example.chen.mycalendar.calendar.fragment.widget.HasTwoAdapterViewpager;
import choosetime.com.example.chen.mycalendar.calendar.utils.DateUtils;

/**
 * 我的日程
 */
public class MyCalendarFragment extends Fragment implements RefreshListener {
    public static final int back_code = 121;

    public MyCalendarFragment() {
        super();
    }

    private HandMoveLayout handMoveLayout;

    @SuppressLint("ValidFragment")
    public MyCalendarFragment(Handler os) {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_calender, container, false);
    }
    /**
     * 调整到下个月
     * */
    public void pagerNext(){
        if (viewPager!=null){
            viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
        }
    }
    /**
     * 调整到上个月
     * */
    public void pagerLast(){
        if (viewPager!=null){
            viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        handMoveLayout = (HandMoveLayout) getView().findViewById(R.id.handmovelayout);

        initCalendar();


    }
    private HasTwoAdapterViewpager viewPager;
    private HasTwoAdapterViewpager viewpagerWeek;
    private List<View> views;
    private WeekCalendarAdpter weekCalendarAdpter;
    private ArrayList<String> timeList = new ArrayList<>();
    public void initCalendar() {
        viewPager = (HasTwoAdapterViewpager) getView().findViewById(R.id.calendar_viewpager);
        viewpagerWeek = (HasTwoAdapterViewpager) getView().findViewById(R.id.calendar_viewpager_week);

        viewPager.setListener(this);
        viewpagerWeek.setListener(this);

        //制造月视图所需view
        views = new ArrayList<>();
        LinearLayout layout = (LinearLayout) View.inflate(getActivity(), R.layout.mouth, null);
        LinearLayout layout1 = (LinearLayout) View.inflate(getActivity(), R.layout.mouth, null);
        LinearLayout layout2 = (LinearLayout) View.inflate(getActivity(), R.layout.mouth, null);
        LinearLayout layout3 = (LinearLayout) View.inflate(getActivity(), R.layout.mouth, null);
        views.add(layout);
        views.add(layout1);
        views.add(layout2);
        views.add(layout3);

        adpter = new MonthCalendarAdpter(views, getActivity(), timeList);
        adpter.setHandler(os);

        //制造日试图所需view
        List viewss = new ArrayList();
        LinearLayout layoutri = (LinearLayout) View.inflate(getActivity(), R.layout.week, null);
        LinearLayout layout1ri = (LinearLayout) View.inflate(getActivity(), R.layout.week, null);
        LinearLayout layout2ri = (LinearLayout) View.inflate(getActivity(), R.layout.week, null);
        LinearLayout layout3ri = (LinearLayout) View.inflate(getActivity(), R.layout.week, null);
        viewss.add(layoutri);
        viewss.add(layout1ri);
        viewss.add(layout2ri);
        viewss.add(layout3ri);
        weekCalendarAdpter = new WeekCalendarAdpter(viewss, getActivity(), timeList);
        weekCalendarAdpter.setHandler(os);
        viewPager.setAdapter(adpter);
        viewPager.setCurrentItem(1200, true);
        viewpagerWeek.setAdapter(weekCalendarAdpter);
        viewpagerWeek.setCurrentItem(weekCalendarAdpter.getCount() / 2);

        //如果是周日，就翻到下一页
        Calendar today = new GregorianCalendar();
        today.setTimeInMillis(System.currentTimeMillis());

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("position", Integer.toString(position));

            }

            @Override
            public void onPageSelected(int position) {
                Calendar today = new GregorianCalendar();
                today.setTimeInMillis(System.currentTimeMillis());
                //距离当前时间的月数(按月算)
                int month = adpter.getCount() / 2 - position;
                today.add(Calendar.MONTH, -month);

//                setBarTitle(getTopTitleTime(today));
                //更新currentItem
//                    viewPager.setTag(R.id.month_current,position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewpagerWeek.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Calendar today = new GregorianCalendar();
                today.setTimeInMillis(System.currentTimeMillis());

                int day_of_week = today.get(Calendar.DAY_OF_WEEK) - 1;
                if (day_of_week == 0) {
                    day_of_week = 7;
                }
                today.add(Calendar.DATE, -day_of_week);
                //距离当前时间的周数(按周算)
                int week = weekCalendarAdpter.getCount() / 2 - position;
                today.add(Calendar.DATE, -week * 7);
//                setBarTitle(getTopTitleTime(today));
                //刷新本页
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private MonthCalendarAdpter adpter;
    /**
     * 用于接收上面日期改变的消息
     */
    public static final int change = 90;
    public static final int change2=91;

    public static final int pagerNext=101;
    public static final int pagerLast=102;
    Handler os = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 90) {
                //do same thing
            }else if(msg.what==change2){
                handMoveLayout.setRowNum((Integer) msg.obj);
            }else if(msg.what==pagerNext){
                pagerNext();
            }else if(msg.what==pagerLast){
                pagerLast();
            }
        }
    };

    int currentItem = 0;
    @Override
    public void refreshListener(final ViewPager viewPager) {
//        ToastUtils.shortMsg("刷新");
        //得到这个selecttime对应的currentItem
        currentItem = 0;
        if (viewPager.getAdapter() instanceof MonthCalendarAdpter) {
            adpter.getTimeList(timeList);
            //月视图
            currentItem = getMonthCurrentItem();
            int odl = viewPager.getCurrentItem();
            viewPager.setCurrentItem(currentItem, false);
            //刷新已经存在的3个视图view
            if (Math.abs(odl - currentItem) <= 1) {
                adpter.instantiateItem(viewPager, viewPager.getCurrentItem() - 1);

                adpter.instantiateItem(viewPager, viewPager.getCurrentItem());

                adpter.instantiateItem(viewPager, viewPager.getCurrentItem() + 1);
            }
            adpter.notifyDataSetChanged();
        } else {
            //周视图
            currentItem = getWeekCurrentItem();
            //如果是周日，就是下一周，+1
            if (DateUtils.getWeekStr(DateUtils.stringToDate(adpter.getSelectTime())).equals("星期日")) {
                currentItem++;
            }
            weekCalendarAdpter.getTimeList(timeList);
            int odl = viewPager.getCurrentItem();
            viewPager.setCurrentItem(currentItem, false);
            //刷新已经存在的3个视图view
            if (Math.abs(odl - currentItem) <= 1) {
                weekCalendarAdpter.instantiateItem(viewPager, viewPager.getCurrentItem() - 1);

                weekCalendarAdpter.instantiateItem(viewPager, viewPager.getCurrentItem());

                weekCalendarAdpter.instantiateItem(viewPager, viewPager.getCurrentItem() + 1);
            }
            weekCalendarAdpter.notifyDataSetChanged();
        }

    }

    //得到月视图选中日期后的CurrentItem
    private int getMonthCurrentItem() {
        //此刻
        Calendar today = new GregorianCalendar();
        today.setTimeInMillis(System.currentTimeMillis());
        //选中时间
        String time = adpter.getSelectTime();
        Date date = DateUtils.stringToDate(time);
        Calendar sele = new GregorianCalendar();
        sele.setTimeInMillis(date.getTime());

        //选中时间的(MONTH)-此刻(MONTH)=月数
        int aa = sele.get(Calendar.MONTH) - today.get(Calendar.MONTH);

        return adpter.getCount() / 2 + aa;
    }

    //得到周视图选中日期后的CurrentItem
    public int getWeekCurrentItem() {
        //此刻
        Calendar today = new GregorianCalendar();
        today.setTimeInMillis(System.currentTimeMillis());
        //转为本周一
        int day_of_week = today.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        today.add(Calendar.DATE, -day_of_week);
        //选中时间
        String time = weekCalendarAdpter.getSelectTime();
        Date date = DateUtils.stringToDate(time);
        Calendar sele = new GregorianCalendar();
        sele.setTimeInMillis(date.getTime());

        //选中时间的(day of yeay)-此刻(day of yeay)=天数
        int aa = ((int) (sele.getTime().getTime() / 1000) - (int) (today.getTime().getTime() / 1000)) / 3600 / 24;
        int aa2 = 0;
        if (Math.abs(aa) % 7 == 0) {
            aa2 = Math.abs(aa) / 7;
        } else {
            aa2 = Math.abs(aa) / 7;
        }
        if (aa >= 0) {
            return weekCalendarAdpter.getCount() / 2 + aa2;
        } else {
            return weekCalendarAdpter.getCount() / 2 - aa2 - 1;
        }
    }
}
