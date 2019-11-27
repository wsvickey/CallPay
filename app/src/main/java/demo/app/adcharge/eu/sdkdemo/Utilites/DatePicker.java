package demo.app.adcharge.eu.sdkdemo.Utilites;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

public class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private OnDateSelectedListener listener;

    public static DatePicker newInstance(Date date, OnDateSelectedListener listener) {
        DatePicker frag = new DatePicker();
        frag.listener = listener;
        final Calendar c = Calendar.getInstance();
        c.setTime(date == null ? new Date() : date);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        Bundle args = new Bundle();

        args.putInt("year", year);
        args.putInt("month", month);
        args.putInt("day", day);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int year = getArguments().getInt("year");
        int month = getArguments().getInt("month");
        int day = getArguments().getInt("day");
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {
        final Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        listener.onDateSelected(c.getTime());
    }


    public interface OnDateSelectedListener {
        void onDateSelected(Date date);
    }
}
