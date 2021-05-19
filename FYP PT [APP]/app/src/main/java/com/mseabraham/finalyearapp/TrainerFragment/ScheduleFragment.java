package com.mseabraham.finalyearapp.TrainerFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mseabraham.finalyearapp.R;
import com.mseabraham.finalyearapp.TrainerFragment.BookingFragment;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ScheduleFragment extends Fragment {
    //TextView link;
    private CalendarView calendar;
    private TextView txtDate, txtYear, txtDay;
    private Date srcDate;
    private Calendar cal;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_schedule,container,false);

        calendar = (CalendarView) view.findViewById(R.id.calendarView);
        txtDate = (TextView) view.findViewById(R.id.date_display_date);
        txtYear = (TextView) view.findViewById(R.id.date_display_year);
        txtDay = (TextView) view.findViewById(R.id.date_display_day);
        Button todayDate = (Button) view.findViewById(R.id.date_display_today);
        Button btnSrc = (Button) view.findViewById(R.id.btnSrcDate);

        calendar.setMinDate(System. currentTimeMillis() - 1000);

        setTodaysDate();

        btnSrc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                BookingFragment frag = new BookingFragment();


                //TIME AND DATE SETTING
                bundle.putSerializable("date", srcDate);
                frag.setArguments(bundle);
                getFragmentManager().beginTransaction().add(R.id.frame_layout, frag).commit();
            }
        });

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                //GET THE DAY OF THE WEEK
                cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                cal.set(year,month,dayOfMonth);
                srcDate = cal.getTime();
                int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                //GET THE DATES IN WORD FORMAT
                String curDate = String.valueOf(dayOfMonth);
                String dDate = new DateFormatSymbols().getShortMonths()[month];
                String dDay = new DateFormatSymbols().getWeekdays()[dayOfWeek];
                //SET TEXT VIEWS TO NEWLY SELECTED DATES
                txtYear.setText(String.valueOf(year));
                txtDate.setText(curDate+ " "+ dDate);
                txtDay.setText(dDay);

            }
        });

        todayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTodaysDate();
            }
        });

        return view;
    }

    @SuppressLint("SimpleDateFormat")
    private void setTodaysDate(){
        Date date = new Date();
        calendar.setDate(date.getTime());
        srcDate = date;
        //TEXT VIEW DATE FORMATS
        SimpleDateFormat simpYear = new SimpleDateFormat("YYYY");
        SimpleDateFormat simpDate = new SimpleDateFormat("dd MMM");
        //SET TEXTS VIEWS TO CURRENT DATE
        txtYear.setText(simpYear.format(date.getTime()));
        txtDate.setText(simpDate.format(date.getTime()));
        txtDay.setText(new SimpleDateFormat("EEEE").format(date.getTime()));
    }
}
