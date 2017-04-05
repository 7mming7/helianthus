package com.ha.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.ha.base.DateType;

/**
 * 日期工具类
 * User: shuiqing
 * DateTime: 17/4/5 下午6:21
 * Email: helianthus301@163.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class DateUtils {
    public static final String START_DATA="20160101";		//日期转换的起始日期
    //使用ThreadLocal创建线程安全Calendar
    private static ThreadLocal<Calendar> localCalendar = new ThreadLocal<Calendar>(){
        @Override
        protected Calendar initialValue() {
            return Calendar.getInstance();
        }
    };

    /**
     * 功能：获取Calendar对象，初始化设置每周以周一开始，每年的第一周必须大于或等于3天，否则就算上一年的最后一周
     * @return	返回Calendar对象
     */
    public static synchronized Calendar getCalendar(){
        //从ThreadLocal中获取Calendar对象，保证线程安全
        Calendar cal = localCalendar.get();
        cal.clear();

        //赋值初始化
        cal.setFirstDayOfWeek(Calendar.MONDAY);//每周以周一开始
        cal.setMinimalDaysInFirstWeek(3);//每年的第一周必须大于或等于3天，否则就算上一年的最后一周
        return cal;
    }

    /**
     * 功能：根据时间类型返回时间
     * @param timeType  时间类型
     * @param date      日期YYYYMMDD
     * @return 时间
     */
    public static String getTime(int timeType, String date) {
        String time = null;

        if (timeType == DateType.DAY.getValue()) {
            time = date;
        }
        if (timeType == DateType.WEEK.getValue()) {
            time = DateUtils.getWeekByDay(date);
        }
        if (timeType == DateType.MONTH.getValue()) {
            time = DateUtils.getMonthByDay(date);
        }
        if (timeType == DateType.YEAR.getValue()) {
            time = DateUtils.getYearByDay(date);
        }

        return time;
    }
    //=======================================================================小时段======================================================================
    /**
     * 功能：获取小时段值
     * @param millis	时间
     * @return	返回  0 - 23
     */
    public static int getTime(long millis) {
        Calendar cal = getCalendar();
        cal.setTimeInMillis(millis);
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    //=======================================================================天======================================================================
    /**
     * 功能：获取今天0点时间值（Long）
     * @return	时间
     */
    public static long getTodayZeroHour(){
        Calendar cal = getCalendar();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static long getStartHourByDay(String date){
        Calendar cal = getCalendar();
        cal.set(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(4, 6)) - 1, Integer.parseInt(date.substring(6, 8))); //月是从0开始的， 0 - 11
        cal.getTimeInMillis();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static long getTimeByDay(String date){
        Calendar cal = getCalendar();
        cal.set(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(4, 6)) - 1, Integer.parseInt(date.substring(6, 8))); //月是从0开始的， 0 - 11
        cal.getTimeInMillis();
        return cal.getTimeInMillis();
    }

    /**
     * 功能：获取天
     * @param millis	时间
     * @return	返回YYYYMMDD
     */
    public static String getDay(long millis) {
        Calendar cal = getCalendar();
        cal.setTimeInMillis(millis);
        return getDay(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * 功能：根据起始时间获取日期（天）集合
     * @param startDate {YYYYMMDD}
     * @param endDate {YYYYMMDD}
     * @return
     */
    public static List<String> getDays(String startDate, String endDate){
        List<String> days = new ArrayList<String>();
        //获取结束时间
        Calendar cal = getCalendar();
        cal.set(Integer.parseInt(endDate.substring(0, 4)), Integer.parseInt(endDate.substring(4, 6)) - 1, Integer.parseInt(endDate.substring(6, 8))); //月是从0开始的， 0 - 11
        long endTime = cal.getTimeInMillis();

        //获取开始时间
        cal.set(Integer.parseInt(startDate.substring(0, 4)), Integer.parseInt(startDate.substring(4,6)) - 1, Integer.parseInt(startDate.substring(6,8))); //月是从0开始的， 0 - 11
        long startTime = cal.getTimeInMillis();

        //获取起始时间中包含的天数
        while(startTime <= endTime){
            days.add(getDay(startTime));
            startTime += 24*3600*1000L;
        }
        return days;
    }

    /**
     * 功能：获取周内的所有日期集合
     * @param weekStr 周（yyyyww）
     * @return 日期集合
     */
    public static List<String> getDaysByWeek(String weekStr) {
        Calendar cal = getCalendar();
        cal.set(Calendar.YEAR, Integer.parseInt(weekStr.substring(0, 4)));
        cal.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(weekStr.substring(4)));
        //星期一
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        long startTime = cal.getTimeInMillis();

        //星期日
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        long endTime = cal.getTimeInMillis();

        List<String> days = new ArrayList<String>();
        //获取起始时间中包含的天数
        while(startTime <= endTime){
            days.add(getDay(startTime));
            startTime += 24*3600*1000L;
        }
        return days;
    }

    /**
     * 功能：获取一个月中的天集合
     * @param time 月(yyyyMM)
     * @return list<yyyyMMdd>
     */
    public static List<String> getDaysByMonth(String time) {
        int year = Integer.parseInt(time.substring(0, 4));
        int month = Integer.parseInt(time.substring(4));
        return getDays(time + "01", time + getDaysOfMonth(year, month));
    }

    /**
     * 功能：获取一年中的天集合
     * @param time 年（yyyy）
     * @return list<yyyyMMdd>
     */
    public static List<String> getDaysByYear(String time) {
        return getDays(time + "0101", time + "12" + getDaysOfMonth(Integer.parseInt(time), 12));
    }

    /**
     * 功能：获取下一天
     * @param date
     * @return
     */
    public static String getNextDay(String date){
        Calendar cal = getCalendar();
        cal.set(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(4, 6)) - 1, Integer.parseInt(date.substring(6, 8))); //月是从0开始的， 0 - 11
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return getDay(cal.getTimeInMillis());
    }

    /**
     * 功能：获取上一天
     * @param date
     * @return
     */
    public static String getLastDay(String date){
        Calendar cal = getCalendar();
        cal.set(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(4, 6)) - 1, Integer.parseInt(date.substring(6, 8))); //月是从0开始的， 0 - 11
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return getDay(cal.getTimeInMillis());
    }

    //=======================================================================周======================================================================
    /**
     * 功能：获取指定天所属周的星期一
     * @param date YYYYMMDD
     * @return
     */
    public static String getFirstDayOfWeek(String date){
        Calendar cal = getCalendar();
        cal.set(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(4,6)) - 1, Integer.parseInt(date.substring(6,8))); //月是从0开始的， 0 - 11
        cal.getTimeInMillis();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return getDay(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * 功能：获取指定天所属周的星期日
     * @param date YYYYMMDD
     * @return
     */
    public static String getEndDayOfWeek(String date){
        Calendar cal = getCalendar();
        cal.set(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(4,6)) - 1, Integer.parseInt(date.substring(6,8))); //月是从0开始的， 0 - 11
        cal.getTimeInMillis();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return getDay(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * 功能：获取指定日期所属的周
     * @param day	日期YYYYMMDD
     * @return	周
     */
    public static String getWeekByDay(String day){
        Calendar cal = getCalendar();
        cal.set(Integer.parseInt(day.substring(0, 4)), Integer.parseInt(day.substring(4,6)) - 1, Integer.parseInt(day.substring(6,8))); //月是从0开始的， 0 - 11
        cal.getTimeInMillis();
        int year = cal.get(Calendar.YEAR);			//获取日期所在年
        int week = cal.get(Calendar.WEEK_OF_YEAR);  //获得日期所在周
        cal.add(Calendar.DAY_OF_MONTH, -7);
        if( week < cal.get(Calendar.WEEK_OF_YEAR) && year == cal.get(Calendar.YEAR)){
            year+=1;
        }
        return getWeek(year, week);
    }

    /**
     * 根据起始时间获取周集合
     * @param startDate {YYYYMMDD}
     * @param endDate {YYYYMMDD}
     * @return {YYYYWW}
     */
    public static List<String> getWeeks(String startDate, String endDate){
        Set<String> weeks = new HashSet<String>();
        List<String> days = getDays(startDate, endDate);
        for (String day : days) {
            weeks.add(getWeekByDay(day));
        }
        return Lists.newArrayList(weeks);
    }

    /**
     * 功能：获取一个月中的所有周的集合
     * @param month 月(yyyyMM)
     * @return set<周(yyyyww)>
     */
    public static Set<String> getWeeksByMonth(String month) {
        Set<String> weeks = new HashSet<String>();

        List<String> dates = getDaysByMonth(month);
        for (String date : dates) {
            //获得日期对应的周
            weeks.add(getWeekByDay(date));
        }

        return weeks;
    }

    /**
     * 功能：获取一年中的所有周的集合
     * @param year 年(yyyy)
     * @return set<周(yyyyww)>
     */
    public static Set<String> getWeeksByYear(String year) {
        Set<String> weeks = new HashSet<String>();

        for (int month = 1; month <= 12 ; month++) {
            //获取与月相关的周的集合并添加
            weeks.addAll(getWeeksByMonth(getMonth(Integer.parseInt(year), month)));
        }
        return weeks;
    }

    //=======================================================================月======================================================================

    /**
     * 功能：获取指定日期所属的月
     * @param date	日期YYYYMMDD
     * @return
     */
    public static String getMonthByDay(String date){
        return date.substring(0, 6);
    }

    /**
     * 功能：获取指定天所属月的第一天
     * @param date	YYYYMMDD
     * @return
     */
    public static String getFirstDayOfMonth(String date){
        String month = getMonthByDay(date);
        return month + "01";
    }

    /**
     * 功能：获取指定天所属月的最后一天
     * @param date	YYYYMMDD
     * @return
     */
    public static String getEndDayOfMonth(String date){
        int daysOfMonth = getDaysOfMonth(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(4, 6)));
        String month = getMonthByDay(date);
        return month + daysOfMonth;
    }

    /**
     * 根据起始时间获取月集合
     * @param startDate {YYYYMMDD}
     * @param endDate {YYYYMMDD}
     * @return {YYYYMM}
     */
    public static synchronized List<String> getMonths(String startDate, String endDate){
        List<String> months = new ArrayList<String>();

        Calendar endCal = getCalendar();
        endCal.set(Calendar.YEAR, Integer.parseInt(endDate.substring(0, 4)));
        endCal.set(Calendar.MONTH, Integer.parseInt(endDate.substring(4,6)) - 1);//月是从0开始的， 0 - 11
        long endTime = endCal.getTimeInMillis();

        Calendar cal = getCalendar();
        cal.set(Calendar.YEAR, Integer.parseInt(startDate.substring(0, 4)));
        cal.set(Calendar.MONTH, Integer.parseInt(startDate.substring(4,6)) - 1);//月是从0开始的， 0 - 11


        while(endTime >= cal.getTimeInMillis()){
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            months.add(year + StringUtil.fillLeftData(String.valueOf(month), 2, '0'));
            cal.setTimeInMillis(cal.getTimeInMillis() + getDaysOfMonth(year, month) * 24 * 3600 * 1000L);
        }
        return months;
    }

    //=======================================================================年======================================================================
    /**
     * 功能：获取指定天所属年的第一天
     * @param date	YYYYMMDD
     * @return
     */
    public static String getFirstDayOfYear(String date){
        String year = date.substring(0, 4);
        return year +	"0101";
    }

    /**
     * 功能：获取指定天所属年的最后一天
     * @param date	YYYYMMDD
     * @return
     */
    public static String getEndDayOfYear(String date){
        String year = date.substring(0, 4);
        return year + "12" + getDaysOfMonth(Integer.parseInt(year), 12);
    }

    /**
     * 功能：根据天获取对应的年份
     * @param date	时间
     * @return	返回YYYY
     */
    public static String getYearByDay(String date){
        return date.substring(0, 4);
    }

    /**
     * 功能：根据日期（天、周、月）获取年
     * @param time	时间
     * @return	返回YYYY
     */
    public static String getYearByTime(String time) {
        return time.substring(0, 4);
    }

    /**
     * 根据起始时间获取月集合
     * @param startDate {YYYYMMDD}
     * @param endDate {YYYYMMDD}
     * @return {YYYY}
     */
    public static synchronized List<String> getYears(String startDate, String endDate){
        List<String> years = new ArrayList<String>();
        int startYear = Integer.parseInt(startDate.substring(0, 4));
        int endYear = Integer.parseInt(endDate.substring(0, 4));

        while(endYear >= startYear){
            years.add(String.valueOf(startYear));
            startYear++;
        }
        return years;
    }

    /**
     * 功能：获取对应月份的天数
     * @param year		年
     * @param month		月
     * @return	月中天数
     */
    public static int getDaysOfMonth(int year, int month) {
        switch (month) {
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                if (isLeapYear(year)) {
                    return 29;
                } else {
                    return 28;
                }
            default:
                return 31;
        }
    }

    /**
     * 功能：判断是否是闰年
     * @param year
     * @return	true：是，false：不是
     */
    public static boolean isLeapYear(int year) {
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            return true;
        }
        return false;
    }

    /**
     * 功能：构造日描述
     * @param year		年
     * @param month		月
     * @param day		日
     * @return	返回YYYYMMDD
     */
    public static String getDay(int year, int month, int day){
        return year + StringUtil.fillLeftData(month+"", 2, '0') + StringUtil.fillLeftData(day+"" , 2 ,'0');
    }

    /**
     * 功能：构造周描述
     * @param year		年
     * @param week		周
     * @return	返回YYYYWW
     */
    public static String getWeek(int year, int week){
        return year + StringUtil.fillLeftData(week+"", 2, '0');
    }

    /**
     * 功能：构造月描述
     * @param year		年
     * @param month		月
     * @return	返回YYYYMM
     */
    public static String getMonth(int year, int month){
        return year + StringUtil.fillLeftData(month+"", 2, '0');
    }

    /**
     * 功能：构造年描述
     * @param year 年
     * @return	返回YYYY
     */
    public static String getYear(int year){
        return String.valueOf(year);
    }

    public static String getToday() {
        return getToday("yyyyMMdd");
    }

    public static String getToday(String format) {
        Calendar calendar = getCalendar();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return new SimpleDateFormat(format).format(calendar.getTime());
    }

    public static String getYesterday() {
        Calendar calendar = getCalendar();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DATE, -1);
        return new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
    }

    /**
     * 功能：获取long型时间
     * @param date yyyyMMdd
     * @return
     * @throws ParseException
     */
    public static long getTime(String date) throws ParseException {
        return new SimpleDateFormat("yyyyMMdd HH:mm:ss").parse(date + " 00:00:00").getTime();
    }

    public static class TimeEntity{
        public String startTime;
        public String endTime;

        public TimeEntity(String startTime, String endTime){
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public String toString(){
            return "[" + startTime + "," + endTime + "]";
        }

        public String getStartTime() {
            return startTime;
        }

        public String getEndTime() {
            return endTime;
        }
    }

    /**
     * 函数名：convertDate
     * 功能描述：日期转换，将具体日期转换为距离20160101的天数
     * @param date 格式为yyyyMMdd
     * @return 距离20160101的天数
     */
    public static long convertDate(String date){
        DateFormat fmtDateTime = new SimpleDateFormat("yyyyMMdd");
        Date date1=null;
        Date date2=null;
        try {
            date1 = fmtDateTime.parse(START_DATA.toString());
            date2 = fmtDateTime.parse(date.toString());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(date1==null||date2==null) return 0;
        Calendar calendar = getCalendar();
        calendar.setTime(date1);
        long time1=calendar.getTimeInMillis();
        calendar.setTime(date2);
        long todayTime=calendar.getTimeInMillis();
        long days = (todayTime - time1) / (1000 * 60 * 60 * 24);
        return days;
    }
    /**
     * 函数名：UnConvertDate
     * 功能描述：日期反转换，将距离20160101的天数转换为日期
     * @param days 距离20160101的天数
     * @return date 格式为yyyyMMdd
     */
    public static String UnConvertDate(long days){
        DateFormat fmtDateTime = new SimpleDateFormat("yyyyMMdd");
        Date date1=null;
        try {
            date1 = fmtDateTime.parse(START_DATA.toString());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Calendar calendar = getCalendar();
        calendar.setTime(date1);
        calendar.add(Calendar.DATE, (int)days);

        return new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
    }
    /**
     * 函数名：CompressDate
     * 功能描述：日期压缩，将具体日期转64进制的数值
     * @param date 格式为yyyyMMdd
     * @return 64进制的数值
     */
    public static String CompressDate(String date){
        return NumericUtil.CompressNumber(DateUtils.convertDate(date));
    }
    /**
     * 功能：时间压缩，将具体时间压缩为64进制的数值
     * @param time
     * @return
     */
    public static String compressTime(long time){
        return NumericUtil.CompressNumber(getTimeGap(time));
    }
    /**
     * 函数名：uncompressDate
     * 功能描述：日期解压缩，将64进制的数值转为日期
     * @param value 64进制的数值
     * @return date 格式为yyyyMMdd
     */
    public static String UnCompressDate(String value){
        return DateUtils.UnConvertDate(NumericUtil.UnCompressNumber(value));
    }


    /**
     * 功能：计算time与当天凌晨的时间差
     * @param time
     * @return 秒
     */
    public static long getTimeGap(long time){
        Calendar cal = getCalendar();
        cal.setTimeInMillis(time);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long zeroTime = cal.getTimeInMillis();
        return (time - zeroTime) / 1000;
    }

    /**
     * 获取昨天早上四点的时间
     * @return long time
     */
    public static String getYesterdayTime(){
        Calendar calendar = getCalendar();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DATE,-1);
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new SimpleDateFormat("yyyyMMddHH").format(calendar.getTime());
    }
    /**
     * 函数名：convertTimeToLong
     * 功能描述：将日期转为时间戳
     * @param date yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Long convertTimeToLong(String date){
        if(date.trim().length()==0) return null;
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date).getTime();
        } catch (ParseException e) {
            return null;
        }
    }

    public static void main(String[] args) {
//		System.out.println(getTime(System.currentTimeMillis() - 60 * 60 * 1000));
//		System.out.println(getTime(System.currentTimeMillis() - 120 * 60 * 1000));
//
//		System.out.println(getWeeks("20141223", "20141229"));
//		System.out.println(getMonths("201310", "201406"));
//		System.out.println(getYears("20150101", "20150303"));

//		System.out.println(getTimeByDay(System.currentTimeMillis()));
//		System.out.println(getDay(1410234278000L));

//		System.out.println(getNextDay());
//		System.out.println(System.currentTimeMillis());



//		System.out.println(getWeekByDay("20140101"));
        //System.out.println(getWeeksByMonth("201412"));
        //System.out.println(getWeeksByYear("2014"));

        //System.out.println(getDaysByWeek("201427"));
        //System.out.println(getDaysByMonth("201402"));
        //System.out.println(getDaysByYear("2014"));

//		System.out.println(getLastDay("20140201"));
//		System.out.println(getFirstDayOfWeek("20150304"));
//		System.out.println(getWeekByDay("20140102"));
//		System.out.println(getEndDayOfWeek("20140102"));
//		String days=CompressDate("20160506");
//		System.out.println(days);
//		System.out.println(compressTime(System.currentTimeMillis()));
//		System.out.println(getTimeGap(System.currentTimeMillis()));
//		System.out.println("2016033105".compareTo(getYesterdayTime()));
    }
}
