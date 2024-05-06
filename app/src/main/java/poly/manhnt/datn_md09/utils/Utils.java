package poly.manhnt.datn_md09.utils;

import android.os.Handler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Utils {
    public static boolean compare(String str1, String str2) {
        return (Objects.equals(str1, str2));
    }

    public static void delay(int millis) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Code to be executed after delay
            }
        }, millis);
    }

    public static String dateFormatter(String inputDate) {
        try {

            // Create a SimpleDateFormat instance for parsing the input date in ISO 8601 format
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

            // Create another SimpleDateFormat instance for formatting the date in the desired format
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd/MM/yyyy");

            // Parse the string to Date object
            Date date = parser.parse(inputDate);

            // Format the Date object into the new pattern
            String formattedDate = formatter.format(date);

            return formattedDate;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
