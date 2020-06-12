package adapters;

import android.util.Log;

public class DateInputAdapter {

    public static String toDateFormat(String dateToFormat){
        String[] date = dateToFormat.split("/");
        String dateFormatted = date[2] + "-" + date[1] + "-" + date[0];
        return dateFormatted;
    }

    public static String fromDateFormat(String dateToFormat){
        String[] date = dateToFormat.split("-");
        return date[2] + "/" + date[1] + "/" + date[0];
    }
}
