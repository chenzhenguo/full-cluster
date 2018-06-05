package org.jeecgframework.core.extend.datasource;

public class DataSourceContextHolder {
	private static final ThreadLocal<DataSourceType> contextHolder = new ThreadLocal<DataSourceType>();

	public static void setDataSourceType(DataSourceType dataSourceType) {
		contextHolder.set(dataSourceType);
	}

	public static DataSourceType getDataSourceType() {
		return (DataSourceType) contextHolder.get();
	}

	public static void clearDataSourceType() {
		contextHolder.remove();
	}
}
