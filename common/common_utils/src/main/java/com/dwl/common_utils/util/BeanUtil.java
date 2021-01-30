package com.dwl.common_utils.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModel;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.ReflectionUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 对象操作类
 */
@ApiModel("对象操作类")
public class BeanUtil {

    /**
     * beanToMap
     *
     * @param bean
     * @return
     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (bean != null) {
            BeanWrapper beanWrapper = new BeanWrapperImpl(bean);
            PropertyDescriptor[] pd = beanWrapper.getPropertyDescriptors();
            for (int i = 0; i < pd.length; i++) {
                String key = pd[i].getName();
                Object value = beanWrapper.getPropertyValue(pd[i].getName());//获取属性值
                map.put(key, value);
            }
        }
        return map;
    }

    /**
     * @param map
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> clazz) {

        ObjectMapper om = new ObjectMapper();

        try {
            return om.readValue(om.writeValueAsString(map), clazz);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param mapList
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> mapsToList(List<Map> mapList, Class clazz) {
        List<T> list = new ArrayList<T>();
        for (Map map : mapList) {
            T bean = (T) mapToBean(map, clazz);
            list.add(bean);
        }
        return list;
    }

    /**
     * @param o
     * @return
     */
    public static boolean isNotEmpty(Object o) {
        return !isEmpty(o);
    }


    /**
     * 判断-是否为空
     *
     * @param o
     * @return
     */
    public static boolean isEmpty(Object o) {
        if (null == o || "".equals(o))
            return true;
        if (o instanceof Collection) {
            if (((Collection<?>) o).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断-是否为数字
     *
     * @param o
     * @return
     */
    public static boolean isNumber(Object o) {
        if (isEmpty(o)) {
            return false;
        }
        if (o instanceof Number) {
            return true;
        }
        if (o instanceof String) {
            try {
                Double.parseDouble((String) o);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * 判断-是否有效类
     * validClass
     *
     * @param className
     * @return
     */
    public static boolean isValidClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * 判断-是否继承
     *
     * @param clazz
     * @param parentClass
     * @return
     */
    public static boolean isInherit(Class<?> clazz, Class<?> parentClass) {
        return parentClass.isAssignableFrom(clazz);
    }

    /**
     * 获取非空属性map
     *
     * @param obj
     * @return
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Map<String, Object> getNotEmptyProperty(Object obj) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Map<String, Object> map = new HashMap<String, Object>();
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (!key.equals("class")) {  // 过滤class属性
                // 得到property对应的getter方法
                Method getter = property.getReadMethod();
                Object value = getter.invoke(obj);
                if (!isEmpty(value))
                    map.put(key, value);
            }
        }
        return map;
    }

    /**
     * 获取属性-根据属性名称
     *
     * @param bean
     * @param name
     * @return
     */
    public static String getProperty(Object bean, String name) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (key.equals(name)) {
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(bean);
                    return value.toString();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取简单属性-根据属性名称
     *
     * @param bean
     * @param name
     * @return
     */
    public static String getSimpleProperty(Object bean, String name) {
        return getProperty(bean, name);
    }

    /**
     * 获取列表属性-根据对象名称
     *
     * @param bean
     * @param name
     * @return
     * @throws Exception
     */
    public static List<?> getListProperty(Object bean, String name) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (key.equals(name)) {
                    Method getter = property.getReadMethod();
                    return (List<?>) getter.invoke(bean);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取数组属性-根据对象名称
     *
     * @param bean
     * @param name
     * @return
     */
    public static String[] getArrayProperty(Object bean, String name) {

        return null;

    }

    /**
     * @param e
     */
    private static void handleReflectionException(Exception e) {
        ReflectionUtils.handleReflectionException(e);
    }
}