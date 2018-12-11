package com.netty.client;

import com.netty.message.Result;

/**
 * 
 * @author 17070680
 *
 */
public interface Callback {
		void putResult(Result result);
		Result getObject();
}
