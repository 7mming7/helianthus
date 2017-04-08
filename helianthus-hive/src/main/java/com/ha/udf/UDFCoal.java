package com.ha.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.io.Text;

/**
 * coal
 * 该函数主要针对oracle的coalcese函数的处理，解决hive中空字符串和null的支持
 * coal(null,'','    ',null,'23',null)  -> 23
 */
public class UDFCoal extends UDF {

	public Text evaluate(Text[] arguments) throws HiveException {

		Text returnValue = null;
		for (int i=0;i<arguments.length;i++) {
			Text deferredObject = arguments[i];
			if (deferredObject != null
					&& !deferredObject.toString().trim().equals("")
					&& !deferredObject.toString().trim().equalsIgnoreCase("null")){
				return deferredObject;
			}
		}

		return returnValue;
	}
}
