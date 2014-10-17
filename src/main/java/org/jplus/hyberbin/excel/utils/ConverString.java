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
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 将表单的内容放到POJO类中
 * @author hyberbin
 * @version 1.0
 */
public class ConverString {

    /**
     * 将String转换成BigDecimal
     * @param str String
     * @return BigDecimal
     */
    public static BigDecimal asBigDecimal(String str) {
        return asBigDecimal(str, new BigDecimal(BigInteger.ZERO));
    }

    /**
     * 将String转换成BigDecimal
     * @param str String
     * @param defaultValue 默认值
     * @return String
     */
    public static BigDecimal asBigDecimal(String str, BigDecimal defaultValue) {
        try {
            return new BigDecimal(str.trim());
        } catch (NullPointerException nullpointerexception) {
            return defaultValue;
        } catch (NumberFormatException numberformatexception) {
            return defaultValue;
        } catch (RuntimeException runtimeexception) {
            return defaultValue;
        }
    }

    /**
     * 将String转换成BigInteger
     * @param str String
     * @return BigInteger
     */
    public static BigInteger asBigInteger(String str) {
        return asBigInteger(str, BigInteger.ZERO);
    }

    /**
     * 将String转换成BigInteger
     * @param str String
     * @param defaultValue 默认值
     * @return BigInteger
     */
    public static BigInteger asBigInteger(String str, BigInteger defaultValue) {
        try {
            return new BigInteger(str.trim());
        } catch (NullPointerException nullpointerexception) {
            return defaultValue;
        } catch (NumberFormatException numberformatexception) {
            return defaultValue;
        }
    }

    /**
     * 将 String 解码为 boolean
     * @param str String
     * @return boolean
     */
    public static boolean asBoolean(String str) {
        return asBoolean(str, false);
    }

    /**
     * 将 String 解码为 Boolean
     * @param str String
     * @param defaultValue 默认值
     * @return Boolean
     */
    public static Boolean asBoolean(String str, Boolean defaultValue) {
        try {
            str = str.trim();
            return Integer.decode(str).intValue() == 0 ? Boolean.FALSE : Boolean.TRUE;
        } catch (NullPointerException nullpointerexception) {
            return defaultValue;
        } catch (NumberFormatException numberformatexception) {
        }
        if (str.equals("")) {
            return defaultValue;
        }
        for (int i = 0; i < FALSE_STRINGS.length; i++) {
            if (str.equalsIgnoreCase(FALSE_STRINGS[i])) {
                return Boolean.FALSE;
            }
        }

        return Boolean.TRUE;
    }

    /**
     * 将 String 解码为 boolean
     * @param str String
     * @param defaultValue 默认值
     * @return boolean
     */
    public static boolean asBoolean(String str, boolean defaultValue) {
        try {
            str = str.trim();
            return Integer.decode(str).intValue() != 0;
        } catch (NullPointerException nullpointerexception) {
            return defaultValue;
        } catch (NumberFormatException numberformatexception) {
        }
        if (str.equals("")) {
            return defaultValue;
        }
        for (int i = 0; i < FALSE_STRINGS.length; i++) {
            if (str.equalsIgnoreCase(FALSE_STRINGS[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * 将 String 解码为 byte
     * @param str String
     * @return byte
     */
    public static byte asByte(String str) {
        return asByte(str, (byte) 0);
    }

    /**
     * 将 String 解码为 Byte
     * @param str String
     * @param defaultValue 默认值
     * @return Byte
     */
    public static Byte asByte(String str, Byte defaultValue) {
        try {
            return Byte.decode(str.trim());
        } catch (NullPointerException nullpointerexception) {
            return defaultValue;
        } catch (NumberFormatException numberformatexception) {
            return defaultValue;
        }
    }

    /**
     * 将 String 解码为 byte
     * @param str String
     * @param defaultValue 默认值
     * @return byte
     */
    public static byte asByte(String str, byte defaultValue) {
        try {
            return Byte.decode(str.trim()).byteValue();
        } catch (NullPointerException nullpointerexception) {
            return defaultValue;
        } catch (NumberFormatException numberformatexception) {
            return defaultValue;
        }
    }

    /**
     * 将 String 解码为 char
     * @param str String
     * @return char
     */
    public static char asCharacter(String str) {
        return asCharacter(str, '\0');
    }

    /**
     * 将 String 解码为 Character
     * @param str String
     * @param defaultValue 默认值
     * @return Character
     */
    public static Character asCharacter(String str, Character defaultValue) {
        try {
            return new Character(str.trim().charAt(0));
        } catch (NullPointerException nullpointerexception) {
            return defaultValue;
        } catch (IndexOutOfBoundsException indexoutofboundsexception) {
            return defaultValue;
        }
    }

    /**
     * 将 String 解码为 char
     * @param str String
     * @param defaultValue 默认值
     * @return char
     */
    public static char asCharacter(String str, char defaultValue) {
        try {
            return str.trim().charAt(0);
        } catch (NullPointerException nullpointerexception) {
            return defaultValue;
        } catch (IndexOutOfBoundsException indexoutofboundsexception) {
            return defaultValue;
        }
    }

    /**
     * 将 String 解码为 double
     * @param str String
     * @return double
     */
    public static double asDouble(String str) {
        return asDouble(str, 0.0D);
    }

    /**
     * 将 String 解码为 Double
     * @param str String
     * @param defaultValue 默认值
     * @return Double
     */
    public static Double asDouble(String str, Double defaultValue) {
        try {
            return new Double(str.trim());
        } catch (NullPointerException nullpointerexception) {
            return defaultValue;
        } catch (NumberFormatException numberformatexception) {
            return defaultValue;
        }
    }

    /**
     * 将 String 解码为 double
     * @param str String
     * @param defaultValue 默认值
     * @return double
     */
    public static double asDouble(String str, double defaultValue) {
        try {
            return (new Double(str.trim())).doubleValue();
        } catch (NullPointerException nullpointerexception) {
            return defaultValue;
        } catch (NumberFormatException numberformatexception) {
            return defaultValue;
        }
    }

    /**
     * 将 String 解码为 float
     * @param str String
     * @return float
     */
    public static float asFloat(String str) {
        return asFloat(str, 0.0F);
    }

    /**
     * 将 String 解码为 Float
     * @param str String
     * @param defaultValue 默认值
     * @return Float
     */
    public static Float asFloat(String str, Float defaultValue) {
        try {
            return Double.valueOf(str.trim()).floatValue();
        } catch (NullPointerException nullpointerexception) {
            return defaultValue;
        } catch (NumberFormatException numberformatexception) {
            return defaultValue;
        }
    }

    /**
     * 将 String 解码为 float
     * @param str String
     * @param defaultValue 默认值
     * @return float
     */
    public static float asFloat(String str, float defaultValue) {
        try {
            return Double.valueOf(str.trim()).floatValue();
        } catch (NullPointerException nullpointerexception) {
            return defaultValue;
        } catch (NumberFormatException numberformatexception) {
            return defaultValue;
        }
    }

    /**
     * 将 String 解码为 int
     * @param str String
     * @return int
     */
    public static int asInteger(String str) {
        return asInteger(str, 0);
    }

    /**
     * 将 String 解码为 Integer
     * @param str String
     * @param defaultValue 默认值
     * @return Integer
     */
    public static Integer asInteger(String str, Integer defaultValue) {
        try {
            return Double.valueOf(str.trim()).intValue();
        } catch (NullPointerException nullpointerexception) {
            return defaultValue;
        } catch (NumberFormatException numberformatexception) {
            return defaultValue;
        }
    }

    /**
     * 将 String 解码为 int
     * @param str String
     * @param defaultValue 默认值
     * @return int
     */
    public static int asInteger(String str, int defaultValue) {
        try {
            return Double.valueOf(str.trim()).intValue();
        } catch (NullPointerException nullpointerexception) {
            return defaultValue;
        } catch (NumberFormatException numberformatexception) {
            return defaultValue;
        }
    }

    /**
     * 将 String 解码为 long
     * @param str String
     * @return long
     */
    public static long asLong(String str) {
        return asLong(str, 0L);
    }

    /**
     * 将 String 解码为 Long
     * @param str String
     * @param defaultValue 默认值
     * @return Long
     */
    public static Long asLong(String str, Long defaultValue) {
        try {
            return Long.decode(str.trim());
        } catch (NullPointerException nullpointerexception) {
            return defaultValue;
        } catch (NumberFormatException numberformatexception) {
            return defaultValue;
        }
    }

    /**
     * 将 String 解码为 long
     * @param str String
     * @param defaultValue 默认值
     * @return long
     */
    public static long asLong(String str, long defaultValue) {
        try {
            return Double.valueOf(str.trim()).longValue();
        } catch (NullPointerException nullpointerexception) {
            return defaultValue;
        } catch (NumberFormatException numberformatexception) {
            return defaultValue;
        }
    }

    /**
     * 将 String 解码为 short
     * @param str String
     * @return short
     */
    public static short asShort(String str) {
        return asShort(str, (short) 0);
    }

    /**
     * 将 String 解码为 Short
     * @param str String
     * @param defaultValue 默认值
     * @return Short
     */
    public static Short asShort(String str, Short defaultValue) {
        try {
            return Double.valueOf(str.trim()).shortValue();
        } catch (NullPointerException nullpointerexception) {
            return defaultValue;
        } catch (NumberFormatException numberformatexception) {
            return defaultValue;
        }
    }

    /**
     * 将 String 解码为 short
     * @param str String
     * @param defaultValue 默认值
     * @return short
     */
    public static short asShort(String str, short defaultValue) {
        try {
            return Short.decode(str.trim()).shortValue();
        } catch (NullPointerException nullpointerexception) {
            return defaultValue;
        } catch (NumberFormatException numberformatexception) {
            return defaultValue;
        }
    }

    /**
     * 将 String 解码为 String
     * @param str String
     * @return String
     */
    public static String asString(String str) {
        return asString(str, "", "");
    }

    /**
     * 将 String 解码为 String
     * @param str String
     * @param defaultValue 默认值
     * @return String
     */
    public static String asString(String str, String defaultValue) {
        return asString(str, defaultValue, defaultValue);
    }

    /**
     * 将 String 解码为 String
     * @param str String
     * @param defaultValue 默认值
     * @param emptyStringValue ""对应的值
     * @return String
     */
    public static String asString(String str, String defaultValue, String emptyStringValue) {
        try {
            return str.equals("") ? emptyStringValue : str;
        } catch (NullPointerException nullpointerexception) {
            return defaultValue;
        }
    }

    /**
     * 将 String 解码为 Date
     * @param str String
     * @return Date
     */
    public static Date asDate(String str) {
        return asDate(str, new Date(), null);
    }

    /**
     * 将 String 解码为 Date
     * @param str String
     * @param defaultValue 默认值
     * @return Date
     */
    public static Date asDate(String str, Date defaultValue) {
        return asDate(str, defaultValue, null);
    }

    /**
     * 将 String 解码为 Date
     * @param str String
     * @param defaultValue 默认值
     * @param pattern String
     * @return Date
     */
    public static Date asDate(String str, Date defaultValue, String pattern) {
        DateFormat formatter = new SimpleDateFormat(pattern != null ? pattern : DEFAULT_DATE_PATTTERN);
        try {
            return formatter.parse(str);
        } catch (ParseException parseexception) {
            return defaultValue;
        } catch (NullPointerException nullpointerexception) {
            return defaultValue;
        }
    }

    /**
     * 将String按指定的type进行解码
     * @param type 指定类型 （基本类型对象或Date）
     * @param str String
     * @return Object
     */
    public static Object asType(Class type, String str) {
        if (type.isAssignableFrom(java.lang.String.class)) {
            return asString(str, "", "");
        }
        if (type.isAssignableFrom(java.lang.Integer.class) || type.equals(Integer.TYPE)) {
            return asInteger(str, new Integer(0));
        }
        if (type.isAssignableFrom(java.lang.Double.class) || type.equals(Double.TYPE)) {
            return asDouble(str, new Double(0.0D));
        }
        if (type.isAssignableFrom(java.lang.Boolean.class) || type.equals(Boolean.TYPE)) {
            return asBoolean(str, Boolean.FALSE);
        }
        if (type.isAssignableFrom(java.lang.Float.class) || type.equals(Float.TYPE)) {
            return asFloat(str, new Float(0.0F));
        }
        if (type.isAssignableFrom(java.lang.Long.class) || type.equals(Long.TYPE)) {
            return asLong(str, new Long(0L));
        }
        if (type.isAssignableFrom(java.lang.Short.class) || type.equals(Short.TYPE)) {
            return asShort(str, new Short((short) 0));
        }
        if (type.isAssignableFrom(java.lang.Byte.class) || type.equals(Byte.TYPE)) {
            return asByte(str, new Byte((byte) 0));
        }
        if (type.isAssignableFrom(java.lang.Character.class) || type.equals(Character.TYPE)) {
            return asCharacter(str, new Character('\0'));
        }
        if (type.isAssignableFrom(java.math.BigDecimal.class)) {
            return asBigDecimal(str, new BigDecimal(BigInteger.ZERO));
        }
        if (type.isAssignableFrom(java.math.BigInteger.class)) {
            return asBigInteger(str, BigInteger.ZERO);
        }
        if (type.isAssignableFrom(java.sql.Date.class)) {
            return java.sql.Date.valueOf(str);
        }
        if (type.isAssignableFrom(java.sql.Timestamp.class)) {
            return java.sql.Timestamp.valueOf(str);
        }
        if (type.isAssignableFrom(java.util.Date.class)) {
            return asDate(str, new Date(), null);
        } else {
            return null;
        }
    }

    /**
     * 将String按指定的type进行解码
     * @param type 指定类型 （基本类型对象或Date）
     * @param str String
     * @param defaultValue 默认值 （str为null或""时的值）
     * @return Object
     */
    public static Object asType(Class type, String str, Object defaultValue) {
        if (type.isAssignableFrom(java.lang.String.class)) {
            return asString(str, (String) defaultValue);
        }
        if (type.isAssignableFrom(java.lang.Integer.class) || type.equals(Integer.TYPE)) {
            return asInteger(str, (Integer) defaultValue);
        }
        if (type.isAssignableFrom(java.lang.Double.class) || type.equals(Double.TYPE)) {
            return asDouble(str, (Double) defaultValue);
        }
        if (type.isAssignableFrom(java.lang.Boolean.class) || type.equals(Boolean.TYPE)) {
            return asBoolean(str, (Boolean) defaultValue);
        }
        if (type.isAssignableFrom(java.lang.Float.class) || type.equals(Float.TYPE)) {
            return asFloat(str, (Float) defaultValue);
        }
        if (type.isAssignableFrom(java.lang.Long.class) || type.equals(Long.TYPE)) {
            return asLong(str, (Long) defaultValue);
        }
        if (type.isAssignableFrom(java.lang.Short.class) || type.equals(Short.TYPE)) {
            return asShort(str, (Short) defaultValue);
        }
        if (type.isAssignableFrom(java.lang.Byte.class) || type.equals(Byte.TYPE)) {
            return asByte(str, (Byte) defaultValue);
        }
        if (type.isAssignableFrom(java.lang.Character.class) || type.equals(Character.TYPE)) {
            return asCharacter(str, (Character) defaultValue);
        }
        if (type.isAssignableFrom(java.math.BigDecimal.class)) {
            return asBigDecimal(str, (BigDecimal) defaultValue);
        }
        if (type.isAssignableFrom(java.math.BigInteger.class)) {
            return asBigInteger(str, (BigInteger) defaultValue);
        }
        if (type.isAssignableFrom(java.sql.Date.class)) {
            return java.sql.Date.valueOf(str);
        }
        if (type.isAssignableFrom(java.sql.Timestamp.class)) {
            return java.sql.Timestamp.valueOf(str);
        }
        if (type.isAssignableFrom(java.util.Date.class)) {
            return asDate(str, (Date) defaultValue);
        } else {
            return null;
        }
    }

    /**
     * 将Object按指定的type进行解码
     * @param type 指定类型
     * @param obj Object
     * @return Object
     */
    public static Object asType(Class type, Object obj) {
        if (!type.equals(java.lang.String.class) && type.isInstance(obj)) {
            return obj;
        }
        if (obj == null || (obj instanceof String)) {
            return asType(type, (String) obj);
        }
        if ((obj instanceof Date) && (java.lang.String.class).isAssignableFrom(type)) {
            return (new SimpleDateFormat(DEFAULT_DATE_PATTTERN)).format((Date) obj);
        }
        if ((obj instanceof Number) && (java.lang.Number.class).isAssignableFrom(type)) {
            Number num = (Number) obj;
            if (type.isAssignableFrom(java.lang.Number.class)) {
                return num;
            }
            if (type.isAssignableFrom(java.lang.Integer.class)) {
                return new Integer(num.intValue());
            }
            if (type.isAssignableFrom(java.lang.Double.class)) {
                return new Double(num.doubleValue());
            }
            if (type.isAssignableFrom(java.lang.Float.class)) {
                return new Float(num.floatValue());
            }
            if (type.isAssignableFrom(java.lang.Long.class)) {
                return new Long(num.longValue());
            }
            if (type.isAssignableFrom(java.lang.Short.class)) {
                return new Short(num.shortValue());
            }
            if (type.isAssignableFrom(java.lang.Byte.class)) {
                return new Byte(num.byteValue());
            }
            if (type.isAssignableFrom(java.math.BigInteger.class)) {
                return (new BigDecimal(num.toString())).toBigInteger();
            }
            if (type.isAssignableFrom(java.math.BigDecimal.class)) {
                return new BigDecimal(num.toString());
            }
        }
        return asType(type, obj.toString());
    }

    /**
     * 将Object按指定的type进行解码
     * @param type 指定类型
     * @param obj Object
     * @param defaultValue 默认值
     * @return Object
     */
    public static Object asType(Class type, Object obj, Object defaultValue) {
        if (!type.equals(java.lang.String.class) && type.isInstance(obj)) {
            return obj;
        }
        if (obj == null || (obj instanceof String)) {
            return asType(type, (String) obj, defaultValue);
        }
        if ((obj instanceof Date) && (java.lang.String.class).isAssignableFrom(type)) {
            return (new SimpleDateFormat(DEFAULT_DATE_PATTTERN)).format((Date) obj);
        }
        if ((obj instanceof Number) && (java.lang.Number.class).isAssignableFrom(type)) {
            Number num = (Number) obj;
            if (type.isAssignableFrom(java.lang.Number.class)) {
                return num;
            }
            if (type.isAssignableFrom(java.lang.Integer.class)) {
                return new Integer(num.intValue());
            }
            if (type.isAssignableFrom(java.lang.Double.class)) {
                return new Double(num.doubleValue());
            }
            if (type.isAssignableFrom(java.lang.Float.class)) {
                return new Float(num.floatValue());
            }
            if (type.isAssignableFrom(java.lang.Long.class)) {
                return new Long(num.longValue());
            }
            if (type.isAssignableFrom(java.lang.Short.class)) {
                return new Short(num.shortValue());
            }
            if (type.isAssignableFrom(java.lang.Byte.class)) {
                return new Byte(num.byteValue());
            }
            if (type.isAssignableFrom(java.math.BigInteger.class)) {
                return (new BigDecimal(num.toString())).toBigInteger();
            }
            if (type.isAssignableFrom(java.math.BigDecimal.class)) {
                return new BigDecimal(num.toString());
            }
        }
        return asType(type, obj.toString(), defaultValue);
    }

    /**
     * 获取className
     * @param cls Class
     * @return String
     */
    public static String getClassName(Class cls) {
        return getClassName(cls.getName());
    }

    /**
     * 根据给定的应用全名获取类名
     * @param fullName 应用全名
     * @return String
     */
    public static String getClassName(String fullName) {
        if (fullName == null) {
            return null;
        } else {
            fullName = fullName.trim();
            String className = fullName.substring(fullName.lastIndexOf('.') + 1).replace('$', '.').trim();
            return className.equals("") ? null : className;
        }
    }
    /**
     * 指定解析为false的字符串
     */
    private static String FALSE_STRINGS[] = {"false", "null", "nul", "off", "no", "n"};
    /**
     * 默认日期模式
     */
    private static String DEFAULT_DATE_PATTTERN = "yyyy-MM-dd";

    /**
     * 将字符串转换成整数
     * @param str String
     * @return int
     */
    public static int ConvertToInt(String str) {
        int num;
        try {
            num = Integer.parseInt(str);
        } catch (Exception e) {
            num = 0;
        }
        return num;
    }

    /**
     * 将字符串转换成整数对象
     * @param str String
     * @return Integer
     */
    public static Integer ConvertToInteger(String str) {
        Integer num;
        try {
            num = Integer.parseInt(str);
        } catch (Exception e) {
            num = 0;
        }
        return num;
    }

    /**
     * 将字符串转换成长整数
     * @param str String
     * @return long
     */
    public static long ConvertToLong(String str) {
        long num;
        try {
            num = Long.parseLong(str);
        } catch (Exception e) {
            num = 0L;
        }
        return num;
    }

    /**
     * 将字符串转换成长整数对象
     * @param str String
     * @return Long
     */
    public static Long ConvertToLonger(String str) {
        Long num;
        try {
            num = Long.parseLong(str);
        } catch (Exception e) {
            num = 0L;
        }
        return num;
    }
}
