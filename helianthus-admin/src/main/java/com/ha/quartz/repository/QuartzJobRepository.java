package com.ha.quartz.repository;

import com.ha.quartz.base.QuartzConstants;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * quartz repository
 * User: shuiqing
 * DateTime: 17/4/20 下午3:21
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class QuartzJobRepository{

    private DataSource dataSource;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Map<String, Object>> getQrtzTriggers() {
        List<Map<String, Object>> results = getJdbcTemplate().queryForList(
                "select * from QRTZ_TRIGGERS order by start_time");
        long val = 0;
        String temp = null;
        for (Map<String, Object> map : results) {
            temp = MapUtils.getString(map, QuartzConstants.TRIGGERNAME);
            if (StringUtils.indexOf(temp, "&") != -1) {
                map.put(QuartzConstants.DISPLAYNAME, StringUtils.substringBefore(temp, "&"));
            } else {
                map.put(QuartzConstants.DISPLAYNAME, temp);
            }

            val = MapUtils.getLongValue(map, QuartzConstants.NEXTFIRETIME);
            if (val > 0) {
                map.put(QuartzConstants.NEXTFIRETIME, DateFormatUtils.format(val, "yyyy-MM-dd HH:mm:ss"));
            }

            val = MapUtils.getLongValue(map, QuartzConstants.PREVFIRETIME);
            if (val > 0) {
                map.put(QuartzConstants.PREVFIRETIME, DateFormatUtils.format(val, "yyyy-MM-dd HH:mm:ss"));
            }

            val = MapUtils.getLongValue(map, QuartzConstants.STARTTIME);
            if (val > 0) {
                map.put(QuartzConstants.STARTTIME, DateFormatUtils.format(val, "yyyy-MM-dd HH:mm:ss"));
            }

            val = MapUtils.getLongValue(map, QuartzConstants.ENDTIME);
            if (val > 0) {
                map.put(QuartzConstants.ENDTIME, DateFormatUtils.format(val, "yyyy-MM-dd HH:mm:ss"));
            }

            map.put("status", QuartzConstants.status.get(MapUtils.getString(map, QuartzConstants.TRIGGERSTATE)));
        }

        return results;
    }

    private JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(this.dataSource);
    }
}
