package choosetime.com.example.chen.mycalendar.calendar.adpter;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import choosetime.com.example.chen.mycalendar.R;
import choosetime.com.example.chen.mycalendar.calendar.CalendarUtil;
import choosetime.com.example.chen.mycalendar.calendar.fragment.MyCalendarFragment;
import choosetime.com.example.chen.mycalendar.calendar.utils.DateUtils;

/**
 * Created by Administrator on 2016/5/19 0019.
 */
public class MonthCalendarAdpter extends CalendarBaseAdpter {
    private List<View> views;
    private Context context;

    private Handler os = null;
    private int last_msg_tv_color;
    private Drawable yuanOfRed;
    private Drawable white;
    private int text_black;
    private int text_white;
    private final Drawable yuanOfBlack;

    private String strToDay = "";
    private ArrayList<String> list = new ArrayList<>();

    public MonthCalendarAdpter(List<View> views, Context context, ArrayList<String> list) {
        this.views = views;
        this.context = context;
        this.list = list;
        //选中今天
        Calendar today = new GregorianCalendar();
        today.setTimeInMillis(System.currentTimeMillis());

        strToDay = DateUtils.getTagTimeStr(today);

        selectTime = DateUtils.getTagTimeStr(today);
        text_black = context.getResources().getColor(R.color.black_deep);
        last_msg_tv_color = context.getResources().getColor(R.color.last_msg_tv_color);
        text_white = context.getResources().getColor(R.color.white);
        yuanOfRed = context.getResources().getDrawable(R.drawable.yuan);
        yuanOfBlack = context.getResources().getDrawable(R.drawable.calendar_background);
        white = context.getResources().getDrawable(R.drawable.white);
    }

    public void setHandler(Handler os) {
        this.os = os;
    }

    @Override
    public int getCount() {
        return 2400;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ViewGroup view = (ViewGroup) views.get(position % views.size());
        int index = container.indexOfChild(view);
        if (index != -1) {
            container.removeView(view);
        }
        try {
            container.addView(view);
        } catch (Exception e) {

        }
        refresh(view, position, list);
        return view;
    }

    public void getTimeList(ArrayList<String> list) {
        this.list = list;
    }

    /**
     * 提供对外的刷新接口
     */
    public void refresh(ViewGroup view, int position, ArrayList<String> list) {
        //给view 填充内容

        //设置开始时间为本周日
        Calendar today = new GregorianCalendar();
        today.setTimeInMillis(System.currentTimeMillis());
        //距离当前时间的月数
        int month = 1200 - position;
        today.add(Calendar.MONTH, -month);
        view.setTag(today.get(Calendar.MONTH) + "");
        //找到这个月的第一天所在星期的周日
        today.add(Calendar.DAY_OF_MONTH, -(today.get(Calendar.DAY_OF_MONTH) - 1));


        int day_of_week = today.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        today.add(Calendar.DATE, -day_of_week);

        render(view, today);
    }

    /**
     * 渲染page中的view：7天
     */
    private void render(final ViewGroup view1, final Calendar today) {
        //一页显示一个月+7天，为42；
        for (int b = 0; b < 6; b++) {
            final ViewGroup view = (ViewGroup) view1.getChildAt(b);
            for (int a = 0; a < 7; a++) {
                final int dayOfMonth = today.get(Calendar.DAY_OF_MONTH);
                // int day_of_year=today.get(Calendar.DAY_OF_YEAR);
                final ViewGroup dayOfWeek = (ViewGroup) view.getChildAt(a);
                //((TextView) dayOfWeek.getChildAt(0)).setText(getStr(today.get(Calendar.DAY_OF_WEEK)));
                ((TextView) dayOfWeek.findViewById(R.id.gongli)).setText(dayOfMonth + "");
                String str = "";
                try {
                    str = new CalendarUtil().getChineseDay(today.get(Calendar.YEAR),
                            today.get(Calendar.MONTH) + 1, today.get(Calendar.DAY_OF_MONTH));
                } catch (Exception e) {

                }
                ((ImageView) dayOfWeek.findViewById(R.id.imv_point)).setVisibility(View.INVISIBLE);
//                String str =
                if (str.equals("初一")) {//如果是初一，显示月份
                    str = new CalendarUtil().getChineseMonth(today.get(Calendar.YEAR),
                            today.get(Calendar.MONTH) + 1, today.get(Calendar.DAY_OF_MONTH));
                }
                ((TextView) dayOfWeek.findViewById(R.id.nongli)).setText(str);
                if (list.contains(DateUtils.getTagTimeStr(today))) {
                    ((ImageView) dayOfWeek.findViewById(R.id.imv_point)).setVisibility(View.VISIBLE);
                    ((ImageView) dayOfWeek.findViewById(R.id.imv_point)).setImageResource(R.drawable.calendar_item_point);
                } else {
                    ((ImageView) dayOfWeek.findViewById(R.id.imv_point)).setVisibility(View.INVISIBLE);
                }
                dayOfWeek.setTag(DateUtils.getTagTimeStr(today));

                dayOfWeek.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        is = true;
                        //TODO:发消息，告诉Activity我改变选中的日期了
                        if (MonthCalendarAdpter.this.os != null) {
                            os.sendEmptyMessage(MyCalendarFragment.change);
                        }

                        selectTime = dayOfWeek.getTag().toString();
                        today.add(Calendar.DATE, -42);//因为已经渲染过42天，所以today往前推42天， 代表当前page重绘；

                        //
                        //恢复上个选中的day的状态
                        if (day != null) {
                            day.findViewById(R.id.cal_container).setBackgroundDrawable(white);
                            ((TextView) day.findViewById(R.id.gongli)).setTextColor(text_black);
                            //特殊情况
                            if (strToDay.equals(tag)) {
                                day.findViewById(R.id.cal_container).setBackgroundDrawable(yuanOfBlack);
                                ((TextView) day.findViewById(R.id.gongli)).setTextColor(text_black);
                                ((TextView) day.findViewById(R.id.nongli)).setTextColor(last_msg_tv_color);

                            } else {
                                day.findViewById(R.id.cal_container).setBackgroundDrawable(white);
                                ((TextView) day.findViewById(R.id.gongli)).setTextColor(text_black);
                                ((TextView) day.findViewById(R.id.nongli)).setTextColor(last_msg_tv_color);
                            }
                        }
                        //变为红色
                        dayOfWeek.findViewById(R.id.cal_container).setBackgroundDrawable(yuanOfRed);
                        ((TextView) dayOfWeek.findViewById(R.id.gongli)).setTextColor(text_white);
                        ((TextView) dayOfWeek.findViewById(R.id.nongli)).setTextColor(text_white);

                        Animator anim = AnimatorInflater.loadAnimator(context, R.anim.soufang);
                        //显示的调用invalidate
                        dayOfWeek.invalidate();
                        anim.setTarget(dayOfWeek);
                        anim.start();
                        //  添加监听：动画结束时执行刷新方法;

                        anim.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                render(view1, today);
                                is = false;
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });

                    }
                });
                if (strToDay.equals(DateUtils.getTagTimeStr(today))) {
                    dayOfWeek.findViewById(R.id.cal_container).setBackgroundDrawable(yuanOfBlack);
                    ((TextView) dayOfWeek.findViewById(R.id.gongli)).setTextColor(text_black);
                    ((TextView) dayOfWeek.findViewById(R.id.nongli)).setTextColor(last_msg_tv_color);
                    if (!selectTime.equals(strToDay)) {
                        today.add(Calendar.DATE, 1);
                        continue;
                    }
                } else {
                    dayOfWeek.findViewById(R.id.cal_container).setBackgroundDrawable(white);
                    ((TextView) dayOfWeek.findViewById(R.id.gongli)).setTextColor(text_black);
                }
                //不是当前月的显示为灰色
                if ((Integer.parseInt((String) view1.getTag())) != today.get(Calendar.MONTH)) {
                    ((TextView) dayOfWeek.findViewById(R.id.gongli)).setTextColor(last_msg_tv_color);
                    if ((Integer.parseInt((String) view1.getTag())) > today.get(Calendar.MONTH)) {
                        //下个月
                        dayOfWeek.setOnClickListener(lastLister);
                    } else {
                        //上个月
                        dayOfWeek.setOnClickListener(nextLister);
                    }
                    today.add(Calendar.DATE, 1);
                    continue;
                } else {
                    dayOfWeek.setAlpha(1.0f);
                }
                //如果是选中天的话显示为红色
                if (selectTime.equals(DateUtils.getTagTimeStr(today))) {

                    dayOfWeek.findViewById(R.id.cal_container).setBackgroundDrawable(yuanOfRed);
                    ((TextView) dayOfWeek.findViewById(R.id.gongli)).setTextColor(text_white);
                    ((TextView) dayOfWeek.findViewById(R.id.nongli)).setTextColor(text_white);

                    if (strToDay.equals(DateUtils.getTagTimeStr(today))) {
                        dayOfWeek.findViewById(R.id.cal_container).setBackgroundDrawable(yuanOfRed);
                        ((TextView) dayOfWeek.findViewById(R.id.gongli)).setTextColor(text_white);
                        ((TextView) dayOfWeek.findViewById(R.id.nongli)).setTextColor(text_white);
                    }

                    day = dayOfWeek;
                    if (MonthCalendarAdpter.this.os != null) {
                        Message message = os.obtainMessage();
                        message.obj = b;
                        message.what = MyCalendarFragment.change2;
                        os.sendMessage(message);
                    }
                    tag = selectTime;
                } else {
                    dayOfWeek.findViewById(R.id.cal_container).setBackgroundDrawable(white);
                    ((TextView) dayOfWeek.findViewById(R.id.gongli)).setTextColor(text_black);
                    ((TextView) dayOfWeek.findViewById(R.id.nongli)).setTextColor(last_msg_tv_color);
                }
                today.add(Calendar.DATE, 1);
            }
        }

    }

    View.OnClickListener nextLister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (MonthCalendarAdpter.this.os != null) {
                Message message = os.obtainMessage();
                message.what = MyCalendarFragment.pagerNext;
                os.sendMessage(message);
            }
        }
    };
    View.OnClickListener lastLister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (MonthCalendarAdpter.this.os != null) {
                Message message = os.obtainMessage();
                message.what = MyCalendarFragment.pagerLast;
                os.sendMessage(message);
            }
        }
    };

    private ViewGroup day = null;

    public ViewGroup getDay() {
        return day;
    }

    private String tag = "";

    public String getSelectTime() {
        return selectTime;
    }

    public void setSelectTime(String selectTime) {
        this.selectTime = selectTime;
    }

}
