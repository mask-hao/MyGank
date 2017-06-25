package com.zhanghao.gankio.util;

import java.util.Random;

/**
 * Created by zhanghao on 2017/6/23.
 */
public class RandomUtil {
    private  static Random random = new Random();

    public static int getRandomFromRange(int max,int min){
        return random.nextInt(max)%(max-min+1)+min;
    }
}
