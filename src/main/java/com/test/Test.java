package com.test;

import com.service.MockClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/11/28
 */
@Service
public class Test implements InitializingBean {
    @Resource
    private MockClient mockClient;

    public static int findLengthOfLCIS(int[] nums) {
        int max = 0;
        int sum = 1 ;
        for(int i=1; i<nums.length; i++){
            if(nums[i]>nums[i-1]){
                sum++;
                continue;
            }
            max = Math.max(sum, max);
            sum = 1;
        }
        return Math.max(sum, max);
    }

    public static void main(String[] args) {
        findLengthOfLCIS(new int[]{1, 3,5,7});
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        mockClient.backClient("发消息给客户端！...");
    }
}
