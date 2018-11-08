package io.yetanotherwhatever.ocpv2;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Utils {

    static final private String DEFAULT_TZ = "EST";
    static final private String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm'" + DEFAULT_TZ + "'";

    //EST only
    static public String formatDateISO8601(Date d)
    {
        String nowAsISO = getISO8061DateFormat().format(d);

        return nowAsISO;
    }

    static public DateFormat getISO8061DateFormat()
    {
        TimeZone tz = TimeZone.getTimeZone(DEFAULT_TZ);
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        df.setTimeZone(tz);

        return df;
    }

    static public Calendar unformatDateISO8601(String date) throws ParseException
    {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);

        Calendar cal = Calendar.getInstance();
        cal.setTime(df.parse(date));

        return cal;
    }
}
