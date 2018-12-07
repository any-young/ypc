package com.test;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/11/28
 */
public class Test {

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
}
