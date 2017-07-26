package com.expandablerecycleview.ui;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.expandablerecycleview.R;
import com.expandablerecycleview.adapter.ManageDaysAdapter;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Himangi on 26/7/17
 */

public class ManageTimeFragment extends Fragment {

    RecyclerView rVSelectWorkingDays;
    TextView tvOpeningHr;
    TextView tvClosingHr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_time, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rVSelectWorkingDays = (RecyclerView) view.findViewById(R.id.rVSelectWorkingDays);
        tvOpeningHr = (TextView) view.findViewById(R.id.tvOpeningHr);
        tvClosingHr = (TextView) view.findViewById(R.id.tvClosingHr);

        rVSelectWorkingDays.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        ManageDaysAdapter manageDaysAdapter = new ManageDaysAdapter(getActivity());
        rVSelectWorkingDays.setAdapter(manageDaysAdapter);

        tvOpeningHr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectOpeningTime();
            }
        });

        tvClosingHr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectClosingTime();
            }
        });
    }

    public void onSelectOpeningTime(){
        showTimerPickerDialog(tvOpeningHr);
    }

    public void onSelectClosingTime(){
        showTimerPickerDialog(tvClosingHr);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void showTimerPickerDialog(final View textView) {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        TextView tView = (TextView) textView;
//        String text = Utils.getText(tView);
//        if (!isEmpty(text)) {
//            List<String> splits = Arrays.asList(text.split(":"));
//            mHour = Integer.parseInt(splits.get(0));
//            mMinute = Integer.parseInt(splits.get(1));
//        }
        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                String am_pm = (hourOfDay < 12) ? " AM" : " PM";
//                String time = String.valueOf(hourOfDay).concat(":").concat(String.valueOf(minute)).concat(" ").concat(am_pm);
//                ((TextView) textView).setText(time);

                boolean isPM = (hourOfDay >= 12);
                ((TextView) textView).setText(String.format(Locale.ENGLISH,"%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"));

            }
        }, mHour, mMinute, false);
        timePickerDialog.show();
    }

}
