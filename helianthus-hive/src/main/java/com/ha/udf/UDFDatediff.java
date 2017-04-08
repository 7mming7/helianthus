package com.ha.udf;

import com.ha.util.DateUtils;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import java.text.ParseException;
import java.util.Calendar;

/**
 * 日期间相减函数
 * User: shuiqing
 * DateTime: 16/7/21 下午4:28
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class UDFDatediff extends UDF{

    private final String standardFormatter = "yyyy-MM-dd HH:mm:ss";

    public Text evaluate(Text firstDate, Text secondDate) {
        if (null == firstDate || null == secondDate) {
            return null;
        }

        StringBuffer first = new StringBuffer();
        first.append(firstDate.toString());
        StringBuffer second = new StringBuffer();
        second.append(secondDate.toString());

        if (firstDate.getLength() < 19) {
            first.append(" 00:00:00");
        }
        if (secondDate.getLength() < 19) {
            second.append(" 00:00:00");
        }

        Text returnValue = new Text();

        Calendar firstCal = Calendar.getInstance();
        Calendar secondCal = Calendar.getInstance();
        try {
            firstCal = DateUtils.stringToCalendar(first.toString(), standardFormatter);
            secondCal = DateUtils.stringToCalendar(second.toString(), standardFormatter);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Float floatDate = DateUtils.dayFloatValueBetTwoCal(firstCal, secondCal);
        returnValue.set(floatDate.toString());
        return returnValue;
    }

    public static void main(String[] args) {
        UDFDatediff udfDatediff =  new UDFDatediff();
        Text first = new Text("2016-12-15");
        Text second = new Text("2016-12-15");
        udfDatediff.evaluate(first,second);
    }
}
