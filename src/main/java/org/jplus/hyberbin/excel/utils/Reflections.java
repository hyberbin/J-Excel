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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * FileUtil说明.
 * @author Hyberbin
 *  2013-6-26 14:09:54
 */
public class Reflections {

    /** CGLIB */
    public static final String CGLIB_CLASS_SEPARATOR = "$$";

    private Reflections() {
    }

    /**
     * 调用Getter方法
     * @param obj
     * @param propertyName
     * @return 
     */
    public static Object invokeGetter(Object obj, String propertyName) {
        String getterMethodName = get(propertyName);
        return invokeMethod(obj, getterMethodName, new Class[]{}, new Object[]{});
    }

    /**
     * 调用Setter方法.使用value的Class来查找Setter方法.
     * @param obj
     * @param propertyName
     * @param value 
     */
    public static void invokeSetter(Object obj, String propertyName, Object value) {
        invokeSetter(obj, propertyName, value, null);
    }
    /**
     * 调用Setter方法.
     * @param obj
     * @param propertyName
     * @param value
     * @param propertyType  用于查找Setter方法,为空时使用value的Class替代.
     */
    public static void invokeSetter(Object obj, String propertyName, Object value, Class<?> propertyType) {
        Class<?> type = propertyType != null ? propertyType : value.getClass();
        String setterMethodName = set(propertyName);
        invokeMethod(obj, setterMethodName, new Class[]{type}, new Object[]{value});
    }

    /**
     * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
     * @param obj
     * @param fieldName
     * @return 
     */
    public static Object getFieldValue(final Object obj, final String fieldName) {
        Field field = getAccessibleField(obj, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        }

        Object result = null;
        try {
            result = field.get(obj);
        } catch (IllegalAccessException e) {
             FieldUtils.log.error(Reflections.class.getName() + "[不可访问的对象]", e);
        }
        return result;
    }

   
    /**
     * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
     * @param obj
     * @param fieldName
     * @param value 
     */
    public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
        Field field = getAccessibleField(obj, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        }
        try {
            field.set(obj, value);
        }  catch (IllegalAccessException ex) {
            FieldUtils.log.error(Reflections.class.getName() + "[不可访问的对象]", ex);
        }
    }

    /**
     * 对于被cglib AOP过的对象, 取得真实的Class类型.
     * @param clazz
     * @return 
     */
    public static Class<?> getUserClass(Class<?> clazz) {
        if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && !Object.class.equals(superClass)) {
                return superClass;
            }
        }
        return clazz;
    }

    /**
     * 直接调用对象方法, 无视private/protected修饰符. 用于一次性调用的情况.
     * @param obj
     * @param methodName
     * @param parameterTypes
     * @param args
     * @return 
     */
    public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes,
            final Object[] args) {
        Method method = getAccessibleMethod(obj, methodName, parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
        }

        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    /**
     * 循环向上转型, 获取对象的DeclaredField,	并强制设置为可访问.
     * <p></p>
     * 如向上转型到Object仍无法找到, 返回null.
     */
    public static Field getAccessibleField(final Object obj, final String fieldName) {
        for (Class<?> superClass = obj instanceof Class?(Class)obj:obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                Field field = superClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException e) {//NOSONAR
                // Field不在当前类定义,继续向上转型
            }
        }
        return null;
    }

    /**
     * 获取一个对象中的所有字段，包括父类中的字段信息
     * @param obj
     * @return
     */
    public static List<Field> getAllFields(Object obj) {
        return getAllFields(obj,Object.class);
    }

    /**
     * 获取一个对象中的所有字段，包括父类中的字段信息
     * @param obj
     * @return
     */
    public static List<Field> getAllFields(Object obj,Class theEnd) {
        List<Field> fields = new ArrayList<Field>();
        for (Class<?> superClass =obj instanceof Class?(Class)obj: obj.getClass(); superClass != theEnd; superClass = superClass.getSuperclass()) {
            Field[] field = superClass.getDeclaredFields();
            for (Field f : field) {
                f.setAccessible(true);
                fields.add(f);
            }
        }
        return fields;
    }

    /**
     * 获取一个对象中的所有字段，包括父类中的字段信息
     * @param obj
     * @return
     */
    public static List<String> getAllFieldNames(Object obj) {
        return getAllFieldNames(obj,Object.class);
    }

    /**
     * 获取一个对象中的所有字段，包括父类中的字段信息
     * @param obj
     * @return
     */
    public static List<String> getAllFieldNames(Object obj,Class theEnd) {
        List<String> fields = new ArrayList<String>();
        for (Class<?> superClass =obj instanceof Class?(Class)obj: obj.getClass(); superClass != theEnd; superClass = superClass.getSuperclass()) {
            Field[] field = superClass.getDeclaredFields();
            for (Field f : field) {
                f.setAccessible(true);
                fields.add(f.getName());
            }
        }
        return fields;
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问. 如向上转型到Object仍无法找到, 返回null.
     * <p></p>
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object...
     * args)
     */
    public static Method getAccessibleMethod(final Object obj, final String methodName,
            final Class<?>... parameterTypes) {
        for (Class<?> superClass =obj instanceof Class?(Class)obj:  obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                Method method = superClass.getDeclaredMethod(methodName, parameterTypes);
                method.setAccessible(true);
                return method;
            } catch (NoSuchMethodException e) {//NOSONAR
                // Method不在当前类定义,继续向上转型
            }
        }
        return null;
    }

    public static List<Method> getAllMethods(final Object obj) {
        List<Method> methods = new ArrayList<Method>();
        for (Class<?> superClass = obj instanceof Class ? (Class) obj : obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            Method[] method = superClass.getDeclaredMethods();
            for (Method method1 : method) {
                method1.setAccessible(true);
                methods.add(method1);
            }
        }
        return methods;
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class. eg.
     * <code>
     * public UserDao
     * extends HibernateDao&lt;User&gt;
     *</code>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or Object.class if cannot be
     * determined
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Class<T> getSuperClassGenricType(final Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
     * <p></p>
     * 如public UserDao extends HibernateDao&lt;User,Long&gt;
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be
     * determined
     */
    @SuppressWarnings("rawtypes")
    public static Class getSuperClassGenricType(final Class clazz, final int index) {

        Type genType = clazz.getGenericSuperclass();

        if (!(genType.getClass().isAssignableFrom(ParameterizedType.class))) {
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index].getClass().isAssignableFrom(Class.class))) {
            return Object.class;
        }

        return (Class) params[index];
    }

    /**
     * 执行反射执行
     *
     * @param className 类型
     * @return 类的实例
     */
    @SuppressWarnings("rawtypes")
    public static Object instance(String className) {
        try {
            Class dialectCls = Class.forName(className);
            return dialectCls.newInstance();
        } catch (ClassNotFoundException e) {
            FieldUtils.log.error(Reflections.class.getName() + "[无法找到方言类]", e);
            return null;
        } catch (InstantiationException e) {
            FieldUtils.log.error(Reflections.class.getName() + "[实例化方言错误]", e);
            return null;
        } catch (IllegalAccessException e) {
            FieldUtils.log.error(Reflections.class.getName() + "[实例化方言错误]", e);
            return null;
        }
    }

    /** 将反射时的checked exception转换为unchecked exception. */
    public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
        if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
                || e instanceof NoSuchMethodException) {
            return new IllegalArgumentException(e);
        } else if (e instanceof InvocationTargetException) {
            return new RuntimeException(((InvocationTargetException) e).getTargetException());
        } else if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        return new RuntimeException("Unexpected Checked Exception.", e);
    }

    /**
     * 实体的getXXX方法
     *
     * @param name 成员变量名
     * @return
     */
    public static String get(String name) {
        String get = "get" + (name.charAt(0) + "").toUpperCase() + name.substring(1);//get+变量名的第一个字母大写
        return get;
    }

    /**
     * 实体的setXXX方法
     *
     * @param name 成员变量名
     * @return
     */
    public static String set(String name) {
        return "set" + (name.charAt(0) + "").toUpperCase() + name.substring(1);//get+变量名的第一个字母大写
    }
}
