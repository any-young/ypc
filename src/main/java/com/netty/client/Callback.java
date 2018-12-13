package com.netty.client;

import com.netty.message.Result;

/**
 * 
 *
 *
 */
public interface Callback {
		void putResult(Result result);
		Result getObject();
}
