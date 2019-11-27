package demo.app.adcharge.eu.sdkdemo.Utilites;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
public class DatePickerFragment  extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private final Calendar c = Calendar.getInstance();
    private DatePickerListener listener;

    public DatePickerFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int customStyle = -1;

        Bundle arguments = getArguments();
        if (arguments != null)
            customStyle = arguments.getInt("customStyle", -1);

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        if (customStyle != -1)
            return new DatePickerDialog(getActivity(), customStyle, this, year, month, day);
        else
            return new DatePickerDialog(getActivity(), this, year, month, day);
    }



    public void setDatePickerListener(DatePickerListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        if (listener != null) {

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String data = sdf.format(c.getTime());

            listener.onDatePicked(data);
        }
    }

    public interface DatePickerListener {
        void onDatePicked(String date);
    }
}