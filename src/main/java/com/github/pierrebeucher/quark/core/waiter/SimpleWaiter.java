package com.github.pierrebeucher.quark.core.waiter;

import java.util.concurrent.TimeUnit;

import com.github.pierrebeucher.quark.core.result.HelperResult;

/**
 * Waiter implementation running on the calling Thread.
 * @author Pierre Beucher
 *
 * @param <E>
 */
public abstract class SimpleWaiter<E extends HelperResult<?>> extends Waiter<E> {

	private long timeout;
	
	private long period;
	
	private TimeUnit timeUnit;
	
	/**
	 * Create a SimpleWaiter using the given timeout and period and specified TimeUnit for each.
	 * @param timeout timeout time
	 * @param period period time
	 * @param timeUnit time unit used for period and timeout
	 */
	public SimpleWaiter(long timeout, long period, TimeUnit timeUnit) {
		super();
		this.timeout = timeout;
		this.period = period;
		this.timeUnit = timeUnit;
	}
	
	/**
	 * Create a SimpleWaiter using the given timeout and period in milliseconds.
	 * @param timeout timeout in ms
	 * @param period period in ms
	 */
	public SimpleWaiter(long timeout, long period) {
		this(timeout, period, TimeUnit.MILLISECONDS);
	}

	@Override
	public E call() throws InterruptedException {
		long tCurrent = currentTime();
		long tStart = tCurrent;
		long tEnd = tStart + timeout;
		
		do {
			E result = check();
			if(result.isSuccess()){
				return result;
			}
			
			timeUnit.sleep(period);
			tCurrent = currentTime();
		}while(tCurrent < tEnd);
		
		//after timeout, fail with the latest item
		return latestResult;
	}
	
	/**
	 * @return the current time using the configured TimeUnit
	 */
	private long currentTime(){
		return timeUnit.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
	}

}
