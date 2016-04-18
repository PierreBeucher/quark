package com.github.pierrebeucher.quark.core.waiter;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.core.result.HelperResult;
import com.github.pierrebeucher.quark.core.result.IntegerHelperResult;
import com.github.pierrebeucher.quark.core.result.ResultBuilder;
import com.github.pierrebeucher.quark.core.result.StringHelperResult;
import com.github.pierrebeucher.quark.core.waiter.SimpleWaiter;
import com.github.pierrebeucher.quark.core.waiter.Waiter;

/**
 * Test various context of use of the Waiter
 * @author Pierre Beucher
 *
 */
public class WaiterTest {

	/**
	 * Ensure the waiter is properly called periodically using the
	 * defined period. We use a Helper with a period which will
	 * return success after a certain call count, and ensure
	 * @throws Exception 
	 */
	@Test
	public void callPeriod() throws Exception{
		
		//timeout, period, total number of check and expected total time
		long timeout = 2000;
		int period = 300;
		final int totalCheckCount = 4;
		int expectedTime = (totalCheckCount-1)*period;
		
		//margin when calculating our period is respected
		int margin = 100;
		
		Waiter<HelperResult<?>> waiter = new SimpleWaiter<HelperResult<?>>(timeout, period){
			
			Integer checkCount = 0;
			
			@Override
			public HelperResult<?> performCheck(HelperResult<?> latestResult) throws Exception {
				checkCount++;
				return new IntegerHelperResult(checkCount >= totalCheckCount, checkCount);
			}
		};
		
		// launch the Waiter and register begin / end time
		// our waiter should run with a period of 300ms and stop after 4 checks
		// with a total time around 900ms 
		// we can reasonnably assume that the total time is contained within our margins
		long beginTime = System.currentTimeMillis();
		callWaiterSecureTimeout(waiter, 10000);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - beginTime;
		
		long lowerBound = expectedTime - margin;
		long higherBound = expectedTime + margin;
		Assert.assertEquals(totalTime > lowerBound && totalTime <= higherBound, true,
				"The Waiter with a " + period + "ms period stopping after " + totalCheckCount + " calls"
				+ " does not seem to run between " + lowerBound + " and " + higherBound + "ms."
				+ " Something is probably wrong with the periodicity mechanism."
				+ " Actual time is " + totalTime + "ms.");
	}
	
	/**
	 * Test the timeout is respected
	 * @throws Exception 
	 */
	@Test
	public void callTimeout() throws Exception{
		
		final HelperResult<?> failure = new StringHelperResult(false, "fail");
		Waiter<HelperResult<?>> waiter = new SimpleWaiter<HelperResult<?>>(1000, 100){
			@Override
			public HelperResult<?> performCheck(HelperResult<?> latestResult) throws Exception {
				return failure;
			}
		};
		
		//not throwing an exception indicates that the timeout worked
		HelperResult<?> result = callWaiterSecureTimeout(waiter, 10000);
		Assert.assertEquals(result, failure, "The Waiter should return the expected failure after timeout");
	}
	
	/**
	 * Test the result returned by the Waiter is the one expected
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * @throws TimeoutException 
	 */
	@Test
	public void callSuccess() throws TimeoutException, InterruptedException, ExecutionException{
		
		final HelperResult<?> failure = ResultBuilder.failure("fail");
		final HelperResult<?> success = ResultBuilder.success("success");
		
		//first call will always return failure, second call returns success
		Waiter<HelperResult<?>> waiter = new SimpleWaiter<HelperResult<?>>(1000, 100){
			boolean shouldSuccess = false;
			@Override
			public HelperResult<?> performCheck(HelperResult<?> latestResult) throws Exception {
				if(!shouldSuccess){
					shouldSuccess = true;
					return failure;
				} else {
					return success;
				}
			}
		};
		
		HelperResult<?> result = callWaiterSecureTimeout(waiter, 10000);
		Assert.assertEquals(result, success, "Waiter did not return expected result after success");
	}

	/**
	 * Call a Waiter inside a Thread killed after the given timeout.
	 * As Waiter are being tested, it is possible their timeout mechanism is broken
	 * so we want to make sure they are finished even if the code is broken.
	 * This function will throw an exception if the Timeout defined occur.
	 * @param waiter
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	private HelperResult<?> callWaiterSecureTimeout(final Waiter<HelperResult<?>> waiter, long timeout) throws TimeoutException, InterruptedException, ExecutionException{

		ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<HelperResult<?>> future = executor.submit(new Callable<HelperResult<?>>(){
			@Override
			public HelperResult<?> call() throws Exception {
				return waiter.call();
			}
        });
        
	    return future.get(timeout, TimeUnit.MILLISECONDS);
	}
}
