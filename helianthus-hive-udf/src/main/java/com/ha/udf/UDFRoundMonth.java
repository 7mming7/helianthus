package com.ha.udf;

import com.ha.util.DateUtils;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import java.text.ParseException;
import java.util.Calendar;

/**
 * 主要针对oracle中round(sysdate,'MONTH')的支持
 * User: shuiqing
 * DateTime: 16/5/26 下午5:03
 * Email: annuus.sq@gmail.om
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class UDFRoundMonth extends UDF {

    Text result = new Text();

    public Text evaluate(Text dateText) {
        System.out.println("UDFRoundMonth:-->" + dateText.toString());
        if (dateText == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        try {
            calendar = DateUtils.stringToCalendar(dateText.toString(),DateUtils.DATE_FORMAT_DAFAULT);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String day = dateText.toString().substring(6,8);
        Integer dayInt = Integer.parseInt(day);
        if (dayInt > 15) {
            calendar.set(Calendar.DAY_OF_MONTH,1);
            calendar.add(Calendar.MONTH,1);
        } else {
            calendar.set(Calendar.DAY_OF_MONTH,1);
        }
        result.set(DateUtils.formatCalendar(calendar, DateUtils.DATE_FORMAT_DAFAULT));
        return result;
    }

    /* public static void main(String[] args){
        UDFRoundMonth udfRoundMonth = new UDFRoundMonth();
        System.out.println(udfRoundMonth.evaluate(new Text("20150616")));
    }*/
}
