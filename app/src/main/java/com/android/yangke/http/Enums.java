package com.android.yangke.http;

import java.io.Serializable;
import java.lang.reflect.Method;

public enum Enums {
    ;

    /**
     * 获得code对应的enum
     */
    @SuppressWarnings("unchecked")
    public static <T extends ITypeCode> T getTypeOf(Class<T> c, int code) {
        try {
            Method m = c.getMethod("values", (Class<T>[]) null);
            T[] ret = (T[]) m.invoke(null);
            for (T type : ret) {
                int tv = type.getCode();
                if (tv == code) {
                    return type;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T extends ITypeCode> T optTypeOf(Class<T> c, int code, T fallback) {
        T ret = getTypeOf(c, code);
        return ret == null ? fallback : ret;
    }

    /**
     * 获得expr对应的第�?��enum(可能会有多个，只会找到第�?��)
     */
    @SuppressWarnings("unchecked")
    public static <T extends ITypeDesc> T getFirstTypeOf(Class<T> c, String desc) {
        try {
            Method m = c.getMethod("values", (Class<T>[]) null);
            T[] ret = (T[]) m.invoke(null);
            for (T type : ret) {
                String tv = type.getDesc();
                if (tv.equalsIgnoreCase(desc)) {
                    return type;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface ITypeCode extends Serializable {

        public int getCode();
    }

    public interface ITypeDesc extends Serializable {

        public String getDesc();
    }

    public interface IType extends ITypeCode, ITypeDesc {
    }
}
