package choosetime.com.example.chen.mycalendar.calendar.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by qhdhiman on 2015/3/18.
 *
 */
public class MomentsUtils {
	private static final String TAG = "MomentsUtils";
    /**
     * 取得特定ID的父VIew对象
     * @param resourceId 引用ID
     * @param child 当前View对象
     * @return 取得特定ID的父VIew对象
     */
    public static View getParent(int resourceId, View child) {
        if (child == null) {
            return null;
        }

        if (child.getId() == resourceId) {
            return child;
        } else {
            return getParent(resourceId, (ViewGroup)child.getParent());
        }
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
