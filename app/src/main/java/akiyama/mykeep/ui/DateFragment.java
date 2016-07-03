package akiyama.mykeep.ui;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;


import akiyama.mykeep.R;
import akiyama.mykeep.util.DimUtil;

/**
 * Created by Administrator on 2016/1/16.
 */
public class DateFragment extends DialogFragment implements View.OnClickListener{
    private TabLayout mTabLayout;
    private ViewPager mVp;
    private View mView;
    private DatePagerAdapter mDatePagerAdapter;
    private OnTimeSetListener mOnTimeSetListener;
    private TimePicker mTimePicker;
    private DatePicker mDatePicker;
    private Button mCancelBtn;
    private Button mOkBtn;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.cancel_btn:
                this.dismiss();
                break;
            case R.id.ok_btn:
                setDateTime();
                break;
        }
    }


    private void setDateTime(){
        if(mOnTimeSetListener!=null && mDatePicker!=null && mTimePicker!=null){
            mOnTimeSetListener.onDateTimeSet(mDatePicker.getYear(),mDatePicker.getMonth(),mDatePicker.getDayOfMonth(),
                    mTimePicker.getCurrentHour(),mTimePicker.getCurrentMinute());
        }
        this.dismiss();
    }

    public void setOnTimeSetListener(OnTimeSetListener onTimeSetListener) {
        this.mOnTimeSetListener = onTimeSetListener;
    }

    public interface OnTimeSetListener {
        public void onInitDateTimeSet(DatePicker datePicker,TimePicker timePicker);
        public void onDateTimeSet(int year, int month, int day, int hourOfDay, int minute);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.layout_pick_dialog, container);
        mTabLayout = (TabLayout) mView.findViewById(R.id.date_tbl);
        mVp = (ViewPager) mView.findViewById(R.id.date_content_vp);
        mCancelBtn = (Button) mView.findViewById(R.id.cancel_btn);
        mOkBtn = (Button) mView.findViewById(R.id.ok_btn);
        initView();
        return mView;
    }

    private void initView() {
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.date_corners_bg);
        getDialog().setTitle("设置时间");
        setOnClick();
        mDatePagerAdapter = new DatePagerAdapter();
        mVp.setAdapter(mDatePagerAdapter);
        mTabLayout.setupWithViewPager(mVp);
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = ViewGroup.LayoutParams.WRAP_CONTENT;
        int height = DimUtil.getScreenHeight()*5/6;
        getDialog().getWindow().setLayout(width, height);
    }

    private void setOnClick(){
        mCancelBtn.setOnClickListener(this);
        mOkBtn.setOnClickListener(this);
    }

    private class DatePagerAdapter extends PagerAdapter {
        private View mView;
        public int getCount() {
            return 2;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            LayoutInflater inflater = (LayoutInflater) collection.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            int resId = 0;
            switch (position) {
                case 0:
                    resId = R.layout.layout_date;
                    mView = inflater.inflate(resId, null);
                    mDatePicker = (DatePicker) mView.findViewById(R.id.date_dp);
                    break;
                case 1:
                    resId = R.layout.layout_time;
                    mView = inflater.inflate(resId, null);
                    mTimePicker = (TimePicker) mView.findViewById(R.id.time_dp);
                    mTimePicker.setIs24HourView(true);
                    mOnTimeSetListener.onInitDateTimeSet(mDatePicker,mTimePicker);
                    break;
            }

            ((ViewPager) collection).addView(mView, 0);
            return mView;
        }

        @Override
        public void destroyItem(ViewGroup arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);

        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == ((View) arg1);

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position==0){
                return "日期";
            }else {
                return "时间";
            }
        }

    }


}
