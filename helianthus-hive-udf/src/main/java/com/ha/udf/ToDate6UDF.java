package com.ha.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

public class ToDate6UDF extends UDF {

	public String evaluate(String datetime, String type) {
        try {
			if (null != datetime && !"".equals(datetime)) {
                datetime = datetime.trim();
				if (datetime.contains("-")) {
					String date = datetime.trim().split(" ")[0];
					int len = date.split("-").length;
					String year = date.split("-")[0];
					String month = date.split("-")[1];
					if (month.length() == 1) {
						month = "0" + month;
					}

					datetime = year + month;
					if (len > 2) {
						String day = date.split("-")[2];
						if (day.length() == 1) {
							day = "0" + day;
						}
						datetime = datetime + day;
					}
				}
				String date = datetime.replaceAll("-", "");
				if (date.length() >= 6) {
					String year = date.substring(0, 4);
					String month = date.substring(4, 6);
					String day = "";
					if (date.length() > 6) {
						day = date.substring(6, 8);
						if (day.length() == 1) {
							day = "0" + day;
						}
					}

					if (month.length() == 1) {
						month = "0" + month;
					}

					String data = "";
					if ("yyyymm".equalsIgnoreCase(type)) {
						data = year + month;
					} else if ("yyyymmdd".equalsIgnoreCase(type)) {
						data = year + month + day;
					} else if ("yyyy".equalsIgnoreCase(type)) {
						data = year;
					}else if("mm".equalsIgnoreCase(type) && date.length() > 6){
						data = month;
					}else if("dd".equalsIgnoreCase(type) && date.length() > 6){
						data = day;
					}else if ("yyyy-mm-dd".equalsIgnoreCase(type)) {
						data = year + "-" + month + "-" + day;
					}else if ("yyyy-mm".equalsIgnoreCase(type)) {
						data = year + "-" + month;
					}else if ("ddmmyy".equalsIgnoreCase(type)) {
                        data = day + month + year;
                    }
					return data;
				} else {
					return datetime;
				}

			} else {
				return null;
			}

		} catch (Exception e) {

			return null;

		}

	}

	public static void main(String args[]) {
        ToDate6UDF toDate6UDF = new ToDate6UDF();
		String datetime = "201501";
		String type = "YYYY-MM-dd";
        System.out.print(toDate6UDF.evaluate("   2   ", type));
        System.out.print("asfadf");
    }
}
