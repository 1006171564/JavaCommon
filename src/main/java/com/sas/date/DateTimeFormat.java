package com.sas.date;

/**
 * 时间格式字符串
 *
 * @author liuyongping
 * @version 1.0
 */
@SuppressWarnings("WeakerAccess")
public class DateTimeFormat {
    /**
     * 完整时间和日期格式，以破折号作为分隔符
     */
    public static final String DATE_TIME_PATTERN_1  = "yyyy-MM-dd HH:mm:ss";
    /**
     * 完整时间和日期格式，使用斜杠作为分隔符
     */
    public static final String DATE_TIME_PATTERN_2  = "dd/MM/yyyy HH:mm:ss";
    /**
     * 完整时间和日期格式，毫秒级别并且无分隔符
     */
    public static final String DATE_TIME_PATTERN_3  = "ddMMyyyyHHmmssSSS";
    /**
     * 只有日期格式，以破折号作为分隔符
     */
    public static final String DATE_PATTERN_1 = "yyyy-MM-dd";
    /**
     * 只有日期格式，使用斜杠作为分隔符
     */
    public static final String DATE_PATTERN_2 = "dd/MM/yyyy";
    /**
     * 完整时间格式
     */
    public static final String TIME_PATTERN_1 = "HH:mm:ss";
}
