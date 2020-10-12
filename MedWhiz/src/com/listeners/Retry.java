package com.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry implements IRetryAnalyzer{

	private int retryCount = 0;
	private int maxRetryCount = 1;

	@Override
	public boolean retry(ITestResult result) {

		if (retryCount < maxRetryCount) {
			System.out.println("Retrying test " + result.getName() + " with status "
					+ getResultStatusName(result.getStatus()) + " for the " + (retryCount+1) + " time(s).");
			System.out.println("error: "+result.getThrowable());
			retryCount++;
			return true;
		}
		return false;
	}

	private String getResultStatusName(int status) {
		// TODO Auto-generated method stub
		String resultName = null;
		if(status==1)
			resultName = "SUCCESS";
		if(status==2)
			resultName = "FAILURE";
		if(status==3)
			resultName = "SKIP";
		return resultName;

	}


}
