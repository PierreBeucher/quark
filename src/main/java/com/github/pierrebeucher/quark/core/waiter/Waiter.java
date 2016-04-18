package com.github.pierrebeucher.quark.core.waiter;

import java.util.concurrent.Callable;

import com.github.pierrebeucher.quark.core.result.HelperResult;

/**
 * <p>The Waiter is used to perform an action periodically, until
 * an expected output is found or a timeout reached. </p>
 * <p>The main purpose of this class is to provide a try/retry/timeout pattern
 * for elements with which a real-time verification is not possible.  </p>
 * 
 * <p>The Waiter rely on the check() method to perform its verification actions. This
 * method will be called periodically, its returned result
 * defining the behavior of the Waiter: periodic calls will continue
 * until a successful call of check() is performed (i.e. the output isSuccess() call returns true)
 * or if the timeout is reached. </p>
 * 
 * <p><b>Example</b></p>
 * <p>We want to ensure our database contains a row matching certain conditions,
 * such as a column like "MyFile_.*[1-9].xml". This condition can be met at any moment.
 * We can use the Waiter to check periodically for this condition:
 * 
 * <pre>
 * {@code
 * Waiter myWaiter = new Waiter(){
 *    public String check() { 
 *      //call your code here to check if our database contains the expected data
 *      //it is possible to use the JdbcHelper or any other mechanism
 *      //TODO add corresponding code JdbcHelper
 *    }
 * };
 * </pre>
 *
 * <p>Then, use our waiter like this: </p>
 * 
 * <pre>
 *  //check every 5 seconds for 2 minutes
 *  myWaiter.waitForSuccess(120000, 5000)
 * </pre>
 * <p>waitForSuccess() will then return the found filename if success,
 * or null if nothing is found after 2 min. </p>
 * @author pierreb
 *
 */
public abstract class Waiter<E extends HelperResult<?>> implements Callable<E> {
	
	protected E latestResult;
	
	/**
	 * Create a new Waiter using the provide timeout (milliseconds) and period (milliseconds)
	 */
	public Waiter() {
		super();
		this.latestResult = null;
	}
	
	/**
	 * This method is called prior to any check() call. Does nothing unless overriden.
	 * On Waiter startup, null will be passed as parameter as there is not any result yet.
	 * @param latestResult the result returned from the latest check() call
	 */
	public void beforeCheck(E latestResult){
		
	}
	
	/**
	 * This method is called after every check() calls. Does nothing unless overriden.
	 * @param latestResult the result returned from the latest check() call
	 */
	public void afterCheck(E latestResult){
		
	}

	/**
	 * Perform the verification managed by this Waiter. The returned
	 * result define the success or failure of the performed
	 * verification.
	 * @return the verification result, either success or failure
	 * @throws Exception
	 */
	public abstract E performCheck(E latestResult) throws Exception;
	
	/**
	 * This method will call in the following order beforeCheck(), performCheck() and afterCheck().
	 * It should be used by the concrete implementation of call() to perform
	 * our check actions periodically. It can be overridden to behave differently. 
	 * @return 
	 * @throws Exception 
	 */
	protected E check() throws Exception{
		beforeCheck(latestResult);
		latestResult = performCheck(latestResult);
		afterCheck(latestResult);
		return latestResult;
	}
	
	/**
	 * Will call <i>check</i> every <i>period</i>, until a successful attempt is made or
	 * <i>timeout</i> is reached. On success, return the successful result. On failure, return
	 * the latest failed result. 
	 * @return the first successful action result if any, or the latest failed item
	 * @throws Exception 
	 */
	public abstract E call() throws Exception;
//	public E call() throws Exception {
//		long tStart = System.currentTimeMillis();
//		long tCurrent = System.currentTimeMillis();
//		long tEnd = tStart + timeout;
//		
//		E latestResult = null;
//		do {
//			beforeCheck(latestResult);
//			latestResult = check();
//			afterCheck(latestResult);
//			if(latestResult.isSuccess()){
//				return latestResult;
//			}
//			
//			Thread.sleep(period);
//			tCurrent = System.currentTimeMillis();
//		}while(tCurrent < tEnd);
//		
//		//after timeout, fail with the latest item
//		return latestResult;
//	}

}
