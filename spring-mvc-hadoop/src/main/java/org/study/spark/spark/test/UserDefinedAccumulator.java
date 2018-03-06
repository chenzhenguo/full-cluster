package org.study.spark.spark.test;

import org.apache.spark.AccumulatorParam;

public class UserDefinedAccumulator implements AccumulatorParam<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String addInPlace(String v1, String v2) {
		System.out.println("addInPlace: "+ v1+" "+v2);
		if("A".equals(v1)){
			return v2;
		}else{
			return v1 + "+" +v2;
		}
	}

	@Override
	public String zero(String v1) {
		System.out.println("zero:" + v1);
		return v1;
	}

	@Override
	public String addAccumulator(String v1, String v2) {
		System.out.println("addAccumulator:" + v1+" "+v2);
		return v1 + "+" +v2;
	}

}
