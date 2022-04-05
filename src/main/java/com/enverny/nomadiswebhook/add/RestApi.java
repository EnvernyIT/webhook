package com.enverny.nomadiswebhook.add;


public class RestApi {
	private static String reportLogicAPI = null;

	public static String getReportLogicAPI() {
		return reportLogicAPI;
	}

	public static void setReportLogicAPI(String url) {
		RestApi.reportLogicAPI = url;
	}

}
