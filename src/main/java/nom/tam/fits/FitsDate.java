package nom.tam.fits;

/*
 * #%L
 * nom.tam FITS library
 * %%
 * Copyright (C) 1996 - 2024 nom-tam-fits
 * %%
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 * #L%
 */
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ISO timestamp support for FITS headers. Such timestamps are used with <code>DATE</code> style header keywords, such
 * as <code>DATE-OBS</code> or <code>DATE-END</code>.
 */
public class FitsDate implements Comparable<FitsDate> {

    /**
     * logger to log to.
     */
    private static final int FIRST_THREE_CHARACTER_VALUE = 100;

    private static final int FIRST_TWO_CHARACTER_VALUE = 10;

    private static final int FITS_DATE_STRING_SIZE = 23;

    private static final TimeZone UTC = TimeZone.getTimeZone("UTC");

    private static final int NEW_FORMAT_DAY_OF_MONTH_GROUP = 4;

    private static final int NEW_FORMAT_HOUR_GROUP = 6;

    private static final int NEW_FORMAT_MILLISECOND_GROUP = 10;

    private static final int NEW_FORMAT_MINUTE_GROUP = 7;

    private static final int NEW_FORMAT_MONTH_GROUP = 3;

    private static final int NEW_FORMAT_SECOND_GROUP = 8;

    private static final int NEW_FORMAT_YEAR_GROUP = 2;

    private static final Pattern NORMAL_REGEX = Pattern.compile("\\s*(([0-9][0-9][0-9][0-9])-([0-9][0-9])-([0-9][0-9]))(T([0-9][0-9]):([0-9][0-9]):([0-9][0-9])(\\.([0-9]+))?)?\\s*");

    private static final int OLD_FORMAT_DAY_OF_MONTH_GROUP = 1;

    private static final int OLD_FORMAT_MONTH_GROUP = 2;

    private static final int OLD_FORMAT_YEAR_GROUP = 3;

    private static final Pattern OLD_REGEX = Pattern.compile("\\s*([0-9][0-9])/([0-9][0-9])/([0-9][0-9])\\s*");

    private static final int YEAR_OFFSET = 1900;

    private static final int NB_DIGITS_MILLIS = 3;

    private static final int POW_TEN = 10;

    /**
     * Returns the FITS date string for the current date and time.
     *
     * @return the current date in FITS date format
     *
     * @see    #getFitsDateString(Date)
     */
    public static String getFitsDateString() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the FITS date string for a specific date and time
     *
     * @return       a created FITS format date string Java Date object.
     *
     * @param  epoch The epoch to be converted to FITS format.
     *
     * @see          #getFitsDateString(Date, boolean)
     * @see          #getFitsDateString()
     */
    public static String getFitsDateString(Date epoch) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the FITS date string, with or without the time component, for a specific date and time.
     *
     * @return           a created FITS format date string. Note that the date is not rounded.
     *
     * @param  epoch     The epoch to be converted to FITS format.
     * @param  timeOfDay Whether the time of day information shouldd be included
     *
     * @see              #getFitsDateString(Date)
     * @see              #getFitsDateString()
     */
    public static String getFitsDateString(Date epoch, boolean timeOfDay) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    private int hour = -1;

    private int mday = -1;

    private int millisecond = -1;

    private int minute = -1;

    private int month = -1;

    private int second = -1;

    private int year = -1;

    /**
     * Convert a FITS date string to a Java <CODE>Date</CODE> object.
     *
     * @param  dStr          the FITS date
     *
     * @throws FitsException if <CODE>dStr</CODE> does not contain a valid FITS date.
     */
    public FitsDate(String dStr) throws FitsException {
        // if the date string is null, we are done
        if (dStr == null || dStr.isEmpty()) {
            return;
        }
        Matcher match = FitsDate.NORMAL_REGEX.matcher(dStr);
        if (match.matches()) {
            year = getInt(match, FitsDate.NEW_FORMAT_YEAR_GROUP);
            month = getInt(match, FitsDate.NEW_FORMAT_MONTH_GROUP);
            mday = getInt(match, FitsDate.NEW_FORMAT_DAY_OF_MONTH_GROUP);
            hour = getInt(match, FitsDate.NEW_FORMAT_HOUR_GROUP);
            minute = getInt(match, FitsDate.NEW_FORMAT_MINUTE_GROUP);
            second = getInt(match, FitsDate.NEW_FORMAT_SECOND_GROUP);
            millisecond = getMilliseconds(match, FitsDate.NEW_FORMAT_MILLISECOND_GROUP);
        } else {
            match = FitsDate.OLD_REGEX.matcher(dStr);
            if (!match.matches()) {
                if (dStr.trim().isEmpty()) {
                    return;
                }
                throw new FitsException("Bad FITS date string \"" + dStr + '"');
            }
            year = getInt(match, FitsDate.OLD_FORMAT_YEAR_GROUP) + FitsDate.YEAR_OFFSET;
            month = getInt(match, FitsDate.OLD_FORMAT_MONTH_GROUP);
            mday = getInt(match, FitsDate.OLD_FORMAT_DAY_OF_MONTH_GROUP);
        }
    }

    private static int getInt(Matcher match, int groupIndex) {
        String value = match.group(groupIndex);
        if (value != null) {
            return Integer.parseInt(value);
        }
        return -1;
    }

    private static int getMilliseconds(Matcher match, int groupIndex) {
        String value = match.group(groupIndex);
        if (value != null) {
            value = String.format("%-3s", value).replace(' ', '0');
            int num = Integer.parseInt(value);
            if (value.length() > NB_DIGITS_MILLIS) {
                num = (int) Math.round(num / Math.pow(POW_TEN, value.length() - NB_DIGITS_MILLIS));
            }
            return num;
        }
        return -1;
    }

    /**
     * Get a Java Date object corresponding to this FITS date.
     *
     * @return The Java Date object.
     */
    public Date toDate() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public int compareTo(FitsDate fitsDate) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    private void appendThreeDigitValue(StringBuilder buf, int value) {
        if (value < FitsDate.FIRST_THREE_CHARACTER_VALUE) {
            buf.append('0');
        }
        appendTwoDigitValue(buf, value);
    }

    private void appendTwoDigitValue(StringBuilder buf, int value) {
        if (value < FitsDate.FIRST_TWO_CHARACTER_VALUE) {
            buf.append('0');
        }
        buf.append(value);
    }
}
