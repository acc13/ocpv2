package io.yetanotherwhatever.ocpv2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Utils {

    //EST only
    static public String formatDateISO8601(Date d)
    {
        String nowAsISO = getISO8061DateFormat().format(d);

        return nowAsISO;
    }

    static public DateFormat getISO8061DateFormat()
    {
        String DEFAULT_TZ = "EST";
        String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm'" + DEFAULT_TZ + "'";
        TimeZone tz = TimeZone.getTimeZone(DEFAULT_TZ);
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        df.setTimeZone(tz);

        return df;
    }
}
