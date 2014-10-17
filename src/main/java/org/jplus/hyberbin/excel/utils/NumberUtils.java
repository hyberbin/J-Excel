/*
 * Copyright 2014 Hyberbin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * Email:hyberbin@qq.com
 */
package org.jplus.hyberbin.excel.utils;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Hyberbin
 * Date: 13-11-14
 * Time: 下午6:32
 */
public class NumberUtils {
    /**
     * 格式化数字
     * @param num  数字
     * @param scale 保留几位小数
     * @return
     */
    public static Float format(Object num, int scale) {
        BigDecimal b = new BigDecimal(num.toString());
        return b.setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public static int parseInt(Object o) {
        if (o == null) {
            return 0;
        }else if(o instanceof Integer){
           return (Integer)o;
        }
        try {
            return Integer.parseInt(o.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static Double parseDouble(Object o){
        if (o == null) {
            return 0d;
        }else if(o instanceof Double){
            return (Double)o;
        }
        try {
            return Double.parseDouble(o.toString());
        } catch (NumberFormatException e) {
            return 0d;
        }
    }

    public static Integer[] parseInts(String str){
        String[] split = str.split(",");
        Integer[] integers=new Integer[split.length];
        for(int i=0;i<split.length;i++){
            integers[i]=parseInt(split[i]);
        }
        return integers;
    }
}
