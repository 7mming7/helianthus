package com.ha.udf;

import com.ha.util.DateUtils;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.hive.ql.udf.UDFType;
import org.apache.hadoop.io.Text;
import org.apache.hive.pdk.HivePdkUnitTest;
import org.apache.hive.pdk.HivePdkUnitTests;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 月初日期函数(获取参数时间的月的第一天日期)
 * User: shuiqing
 * DateTime: 16/5/13 下午3:34
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@UDFType(deterministic = false)
@Description(name = "first_day",
        value = "_FUNC_(dateString) -  returns the first day of the month based " +
                "on a date string with yyyy-MM-dd HH:mm:ss pattern.",
        extended = "Example:\n"
                +"> SELECT first_day('2016-03-15 01:22:33') FROM src LIMIT 1;"
                +"2003-03-01 00:00:00\n"
)
@HivePdkUnitTests(
        setup = "", cleanup = "",
        cases = {
                @HivePdkUnitTest(
                        query = "SELECT first_day('2003-03-15 01:22:33') FROM dual;",
                        result = "2003-03-01 00:00:00"
                ),
                @HivePdkUnitTest(
                        query = "SELECT first_day('2011-07-21 09:21:00') FROM dual;",
                        result = "2011-07-01 00:00:00"
                )
        }
)
public class UDFFirstDay extends UDF {
    private SimpleDateFormat standardFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Calendar calendar = Calendar.getInstance();

    public UDFFirstDay() {
        standardFormatter.setLenient(false);
    }

    Text result = new Text();

    public Text evaluate(Text dateText){
        if (dateText == null) {
            return null;
        }

        try {
            calendar.setTime(standardFormatter.parse(dateText.toString()));
            int lastDate = calendar.getActualMinimum(Calendar.DATE);
            calendar.set(Calendar.DATE, lastDate);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date newDate = calendar.getTime();
            result.set(standardFormatter.format(newDate));
            return result;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Text evaluate(Text dateText, Text patternText) {
        if (dateText == null) {
            return null;
        } else if (patternText == null) {
            return evaluate(dateText);
        }

        try{
        	//将YYYY-mm-dd中的mm转化为MM
			String oldPattern=patternText.toString();
			String newPattern=oldPattern.replace('m', 'M');
			
			String dateStr=dateText.toString();
			//将日期转成yyyy-MM-dd的格式
			if("yyyy-MM-dd".equals(newPattern)&&!dateStr.contains("-")){
				dateStr=dateStr.substring(0, 4)+"-"+dateStr.substring(4, 6)+"-"+dateStr.substring(6, 8);
			}
			//将日期转成yyyyMMdd的格式 
			if("yyyyMMdd".equals(newPattern)&&dateStr.contains("-")){
				dateStr=dateStr.substring(0, 10).replace("-", "");
			}
        	
        	
            standardFormatter = new SimpleDateFormat(newPattern);
            calendar = DateUtils.stringToCalendar(dateStr,newPattern);
            int lastDate = calendar.getActualMinimum(Calendar.DATE);
            calendar.set(Calendar.DATE, lastDate);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            result.set(DateUtils.formatCalendar(calendar,newPattern));
            return result;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*public static void main(String[] args){
        UDFFirstDay udfFirstDay = new UDFFirstDay();
        Text dateText = new Text("2015-05-13");
        Text parttenText = new Text("yyyyMMdd");
        System.out.println(udfFirstDay.evaluate(dateText,parttenText));
    }*/
}
