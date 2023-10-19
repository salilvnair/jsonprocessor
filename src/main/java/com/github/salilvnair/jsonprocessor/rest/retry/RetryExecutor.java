package com.github.salilvnair.jsonprocessor.rest.retry;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class RetryExecutor {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());

    private int maxRetries = 0;

    private long delay = 0;
    
    private List<String> whiteListedExceptions;

    public RetryExecutor maxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
        return this;
    }
    
    public RetryExecutor delay(long delayInMillis) {
        this.delay = delayInMillis;
        return this;
    }
    
    public RetryExecutor maxRetries(String maxRetries) {
    	if(StringUtils.isNotEmpty(maxRetries)) {
    		maxRetries = maxRetries.trim();
    		if(NumberUtils.isNumber(maxRetries)) {
                this.maxRetries = Integer.parseInt(maxRetries);	
    		}
    	}    
        return this;
    }
    
    public RetryExecutor delay(String delay, TimeUnit timeUnit) {
    	if(StringUtils.isNotEmpty(delay)) {
    		delay = delay.trim();
            if(NumberUtils.isNumber(delay)) {
            	this.delay = timeUnit.toMillis(Integer.parseInt(delay));
            }
    	} 
        return this;
    }
    
    public RetryExecutor configure(String exception) {
    	if(whiteListedExceptions == null) {
    		whiteListedExceptions = new ArrayList<>();
    	}
    	whiteListedExceptions.add(exception);
    	return this;
    }
    
    public RetryExecutor configure(List<String> exceptions) {
    	if(whiteListedExceptions == null) {
    		whiteListedExceptions = new ArrayList<>();
    	}
    	if(CollectionUtils.isNotEmpty(exceptions)) {
            whiteListedExceptions.addAll(exceptions);
        }
    	return this;
    }
    
    public RetryExecutor delay(int delay, TimeUnit timeUnit) {
        this.delay = timeUnit.toMillis(delay);
        return this;
    }

    // Takes a function and executes it, if fails, passes the function to the retry command
    // retry command executes until it exceeds max retries
    public <T, R> T execute(Supplier<T> function) throws RetryExecutorException {
        try {
            return function.get();
        } 
        catch (Exception e) {
        	if(whiteListedExceptions!=null && !whiteListedExceptions.isEmpty()) {
        		boolean foundWhiteListedException = whiteListedExceptions.stream()
        		.anyMatch(exception -> e.getLocalizedMessage()!=null && e.getLocalizedMessage().contains(exception));
        		if(!foundWhiteListedException) {
        			String retryExecutorMsg = "FAILED will not be retried as the exception is not whitelisted";
        			logger.error(retryExecutorMsg+" ex:"+e.getLocalizedMessage());
        			throw new RetryExecutorException(e, retryExecutorMsg, e.getLocalizedMessage());
        		}
        	}
        	logger.error(e.getLocalizedMessage());        	
        	logger.error("FAILED will be retried " + maxRetries + " times after a delay of " + TimeUnit.MILLISECONDS.toSeconds(delay) + " seconds.");
            return retry(function);
        }
    }

    private <T> T retry(Supplier<T> function) throws RetryExecutorException {
    	Exception exception = null;
        int retryCounter = 0;
        while (retryCounter < maxRetries) {
            try {
            	if( delay > 0 ) {
            		Thread.sleep(delay);
            	}
                return function.get();
            } 
            catch (Exception ex) {
                retryCounter++;
                logger.error(ex.getLocalizedMessage());
                logger.error("FAILED on retry " + retryCounter + " of " + maxRetries);
                if (retryCounter >= maxRetries) {
                	logger.error("Max retries exceeded.");
                	exception = ex;
                    break;
                }
            }
        }
        if(exception==null) {
        	throw new RetryExecutorException("FAILED on all of " + maxRetries + " retries");
        }
        else {
        	throw new RetryExecutorException(exception, "FAILED on all of " + maxRetries + " retries", exception.getLocalizedMessage());
        }        
   }  
}