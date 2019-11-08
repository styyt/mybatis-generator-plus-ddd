package ${cfg.infPackage}.consts.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * author: yyt
 * date: 2019-11-8 14:45:11
 * dto和domain简单转换工具类
 */
public class DomainDtoTransUtils {

    private final Logger LOG =LoggerFactory.getLogger(this.getClass());

    private DomainDtoTransUtils(){}


    public static Object CloneAttribute(Object clone,Object beCloned) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Field[] fieldClone = null;
        Field[] fieldBeCloned = null;
        Map<String,Field> map = new HashMap<String,Field>();
        try {
            Class<?> classClone = clone.getClass();
            Class<?> classBecloned = beCloned.getClass();

            fieldClone = classClone.getDeclaredFields();
            fieldBeCloned = classBecloned.getDeclaredFields();

            for(int t =0;t<fieldBeCloned.length;t++){
                map.put(fieldBeCloned[t].getName(), fieldBeCloned[t]);
            }

            for(int i=0;i<fieldClone.length;i++){
                String fieldCloneName = fieldClone[i].getName();
                Field fie = map.get(fieldCloneName);
                if(fie!=null){
                    //如果两边都存在的同样属性，则更新，如果缺少get/set方法,会报错
                    Method method1 = classClone.getMethod(getMethodName(fieldCloneName));
                    Method method2 = classBecloned.getMethod(setMethodName(fieldCloneName),fie.getType());
                    method2.invoke(beCloned,method1.invoke(clone));
                }
            }
        } catch (Exception e) {
            throw e;
        }finally{
            fieldClone = null;
            fieldBeCloned = null;
            map.clear();
        }
        return beCloned;
    }

    private static String getMethodName(String fieldName){
        String head = fieldName.substring(0, 1).toUpperCase();
        String tail = fieldName.substring(1);
        return "get"+head+tail;
    }

    private static String setMethodName(String fieldName){
        String head = fieldName.substring(0, 1).toUpperCase();
        String tail = fieldName.substring(1);
        return "set"+head+tail;
    }

}
