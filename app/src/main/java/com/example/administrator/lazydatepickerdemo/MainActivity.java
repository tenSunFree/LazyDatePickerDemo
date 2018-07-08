package com.example.administrator.lazydatepickerdemo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mikhaellopez.lazydatepicker.LazyDatePicker;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String DATE_FORMAT = "MM-dd-yyyy";
    private ScrollView root_scrollView;
    private LinearLayout root_linearLayout, root2_linearLayout, root3_linearLayout, root4_linearLayout;
    private Context context;
    private TextView meal_time_textview, lazydatepicker_textview;
    private LazyDatePicker lazydatepicker;
    private DisplayMetrics dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitializationRelated();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Rect outRect2 = new Rect();
                getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(outRect2);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) root_linearLayout.getLayoutParams();
                params.width = dm.widthPixels - (dip2px(context, 20) * 2);
                params.height = (outRect2.height() - (dip2px(context, 20) * 2)) / 3;
                root_linearLayout.setLayoutParams(params);
                root2_linearLayout.setLayoutParams(params);
                root3_linearLayout.setLayoutParams(params);
                root4_linearLayout.setVisibility(View.VISIBLE);
            }
        }, 500);

        /** 設定LazyDatePicker 可以輸入的時間範圍 */
        Date minDate = LazyDatePicker.stringToDate("07-08-2018", DATE_FORMAT);
        Date maxDate = LazyDatePicker.stringToDate("12-31-2028", DATE_FORMAT);
        lazydatepicker.setMinDate(minDate);
        lazydatepicker.setMaxDate(maxDate);

        /** 監聽LazyDatePicker */
        lazydatepicker.setOnDatePickListener(new LazyDatePicker.OnDatePickListener() {
            /** 當使用者輸入完正確數字後, 會隱藏數字軟鍵盤, 並且顯示相關文字 */
            @Override
            public void onDatePick(String date) {
                InputMethodManager imm = (InputMethodManager) getSystemService(context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(root_scrollView.getWindowToken(), 0);             // 強制隱藏鍵盤
                String dateString = date.toString();
                meal_time_textview.setText("就餐時間: "
                        + dateString.substring(4) + "年"
                        + dateString.substring(0, 2) + "月"
                        + dateString.substring(2, 4) + "日");
                meal_time_textview.setBackgroundResource(R.drawable.lazydatepicker_style);
            }

            /**
             * 當使用者點擊lazydatepicker時, 會將root_scrollView滑到底 已確保lazydatepicker不會被遮蔽
             * 並且再次模擬點擊lazydatepicker, 彈出數字軟鍵盤
             */
            @Override
            public void onShowKeyboard() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        root_scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        setMoveToRight2(1, lazydatepicker);
                    }
                }, 500);
            }
        });
    }

    private void InitializationRelated() {
        context = this;
        dm = new DisplayMetrics();                                                                  // 建立一個DisplayMetrics物件
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);                                 // 取得裝置的資訊
        meal_time_textview = findViewById(R.id.meal_time_textview);
        lazydatepicker_textview = findViewById(R.id.lazydatepicker_textview);
        root_scrollView = findViewById(R.id.root_scrollView);
        lazydatepicker = findViewById(R.id.lazydatepicker);
        root_linearLayout = findViewById(R.id.root_linearLayout);
        root2_linearLayout = findViewById(R.id.root2_linearLayout);
        root3_linearLayout = findViewById(R.id.root3_linearLayout);
        root4_linearLayout = findViewById(R.id.root4_linearLayout);
        root4_linearLayout.setVisibility(View.INVISIBLE);
    }

    /**
     * 主要是用來模擬點擊lazydatepicker
     */
    public void setMoveToRight2(int distance, View view) {
        view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                MotionEvent.ACTION_DOWN, 1, 1, 0));
        view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                MotionEvent.ACTION_MOVE, 1 + distance, 1, 0));
        view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                MotionEvent.ACTION_UP, 1 + distance, 1, 0));
    }

    /**
     * dp转为px
     *
     * @param context  上下文
     * @param dipValue dp值
     * @return
     */
    private int dip2px(Context context, float dipValue) {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
    }
}
