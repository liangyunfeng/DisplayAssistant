package com.lyf.display.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by lyf on 2018/2/25.
 */

public class DeviceInfo {

    public static String getSystemProperties() {
        StringBuilder result = new StringBuilder();
        try {
            Class cls = Class.forName("android.os.SystemProperties");
            Method method = cls.getMethod("get", new Class[]{String.class, String.class});
            Object obj = cls.newInstance();

            result.append("ro.product.model = ");
            result.append(((String) method.invoke(obj, new Object[]{"ro.product.model", "Unknown"})).trim());
            result.append("\n");
            result.append("ro.product.board = ");
            result.append(((String) method.invoke(obj, new Object[]{"ro.product.board", "Unknown"})).trim());
            result.append("\n");
            result.append("ro.product.device = ");
            result.append(((String) method.invoke(obj, new Object[]{"ro.product.device", "Unknown"})).trim());
            result.append("\n");
            result.append("ro.product.name = ");
            result.append(((String) method.invoke(obj, new Object[]{"ro.product.name", "Unknown"})).trim());
            result.append("\n");
            result.append("ro.product.brand = ");
            result.append(((String) method.invoke(obj, new Object[]{"ro.product.brand", "Unknown"})).trim());
            result.append("\n");
            result.append("ro.build.scafe.shot = ");
            result.append(((String) method.invoke(obj, new Object[]{"ro.build.scafe.shot", "Unknown"})).trim());
            result.append("\n");
            result.append("ro.build.scafe.cream = ");
            result.append(((String) method.invoke(obj, new Object[]{"ro.build.scafe.cream", "Unknown"})).trim());
            result.append("\n");
            result.append("ro.build.scafe.size = ");
            result.append(((String) method.invoke(obj, new Object[]{"ro.build.scafe.size", "Unknown"})).trim());
            result.append("\n");
            result.append("ro.build.product = ");
            result.append(((String) method.invoke(obj, new Object[]{"ro.build.product", "Unknown"})).trim());
            result.append("\n");
            result.append("ro.board.platform = ");
            result.append(((String) method.invoke(obj, new Object[]{"ro.board.platform", "Unknown"})).trim());
            result.append("\n");
            result.append("ro.csc.country_code = ");
            result.append(((String) method.invoke(obj, new Object[]{"ro.csc.country_code", "Unknown"})).trim());
            result.append("\n");
            result.append("ro.csc.countryiso_code = ");
            result.append(((String) method.invoke(obj, new Object[]{"ro.csc.countryiso_code", "Unknown"})).trim());
            result.append("\n");
            result.append("ro.csc.sales_code = ");
            result.append(((String) method.invoke(obj, new Object[]{"ro.csc.sales_code", "Unknown"})).trim());
            result.append("\n");
            result.append("persist.log.seclevel = ");
            result.append(((String) method.invoke(obj, new Object[]{"persist.log.seclevel", "Unknown"})).trim());

            return result.toString();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static String getSystemProperties(String className, String methodName, Object... paramNames) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("ro.product.model = ");
            Class myClass = Class.forName(className);
            Method myMethod = myClass.getMethod(methodName, java.lang.Object.class, java.lang.Object.class);
            sb.append(((String) myMethod.invoke(myClass.newInstance(), "ro.product.model", "Unknown")).trim());
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static String getSystemProperties(String attribution) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("ro.product.model = ");
            Class myClass = Class.forName("android.os.SystemProperties");
            Method myMethod = myClass.getMethod("get", java.lang.String.class, java.lang.String.class);
            sb.append(((String) myMethod.invoke(myClass.newInstance(), attribution, "Unknown")).trim());
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return sb.toString();
    }
}
