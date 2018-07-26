package com.zero.core.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Generic utility methods for working with reflection
 *
 * @author Louis
 * @date 2007-4-17
 */
@SuppressWarnings("rawtypes")
public class ReflectionUtils {
    private ReflectionUtils() {
        //Prevent from creating instance of this Class and extends from it
    }

    private static class MemberComparator implements Comparator<Member> {
        public int compare(Member o1, Member o2) {
            String name1 = Modifier.toString(o1.getModifiers());
            String name2 = Modifier.toString(o2.getModifiers());
            return name1.compareTo(name2);
        }
    }

    public static String toString(Class<?> cls) {        
        
        StringBuilder sb = new StringBuilder();
        Class<?> superCls = cls.getSuperclass();
        if (cls.isInterface())
            sb.append(Modifier.toString(cls.getModifiers()) + " " + cls.getName());
        else
            sb.append(Modifier.toString(cls.getModifiers()) + " class " + cls.getName());
        if (superCls != null && superCls != Object.class)
            sb.append(" extends " + superCls.getName());

        Class<?>[] interfaces = cls.getInterfaces();
        if (interfaces.length > 0) {
            sb.append(" implements " + interfaces[0].getSimpleName());
            for (int i = 1; i < interfaces.length; i++) {
                Class<?> inter = interfaces[i];
                sb.append(", " + inter.getSimpleName());
            }
        }
        sb.append(" {\n");

        getFields(cls, sb);
        getConstructors(cls, sb);
        getMethods(cls, sb);
        sb.append("}");
        return sb.toString();
    }

    /**
     * Prints all constructors of a class
     */    
    private static void getConstructors(Class cl, StringBuilder sb) {
        sb.append("   //Constructors\n");

        Constructor[] constructors = cl.getDeclaredConstructors();
        Arrays.sort(constructors, new MemberComparator());
        for (Constructor c : constructors) {
            String name = c.getName();
            sb.append("   " + Modifier.toString(c.getModifiers()));
            sb.append(" " + name + "(");

            // print parameter types
            Class[] paramTypes = c.getParameterTypes();
            for (int j = 0; j < paramTypes.length; j++) {
                if (j > 0)
                    sb.append(", ");
                sb.append(paramTypes[j].getSimpleName());
            }
            sb.append(");\n");
            //sb.append("   " + c.toString() + ";\n");
        }

        sb.append("\n");
    }

    /**
     * Prints all methods of a class
     */
    private static void getMethods(Class cl, StringBuilder sb) {
        sb.append("   //Methods\n");

        Method[] methods = cl.getDeclaredMethods();
        Arrays.sort(methods, new MemberComparator());
        sortMethodByReturnTypeName(methods);

        for (Method m : methods) {
            Class retType = m.getReturnType();
            String name = m.getName();
            sb.append("   " + Modifier.toString(m.getModifiers()));
            sb.append(" " + retType.getSimpleName() + " " + name + "(");

            // print parameter types
            Class[] paramTypes = m.getParameterTypes();
            for (int j = 0; j < paramTypes.length; j++) {
                if (j > 0)
                    sb.append(", ");
                sb.append(paramTypes[j].getSimpleName());
            }
            sb.append(");\n");
        }
    }

    //Based on the order of modifiers, sort by Return-Type.SimpleName
    public static void sortMethodByReturnTypeName(Method[] methods) {
        class MethodReturnTypeNameComparator implements Comparator<Method> {
            public int compare(Method o1, Method o2) {
                return o1.getReturnType().getSimpleName().compareTo(o2.getReturnType().getSimpleName());
            }
        }

        if (methods == null || methods.length < 3)
            return;

        MethodReturnTypeNameComparator comparator = new MethodReturnTypeNameComparator();
        int first = 0, end = 1;
        int modifiers1 = methods[0].getModifiers();
        for (int i = 1; i < methods.length; i++) {
            int modifiers2 = methods[i].getModifiers();
            if (modifiers2 == modifiers1)
                end++;
            else {
                Arrays.sort(methods, first, end, comparator);

                modifiers1 = modifiers2;
                first = end++;
            }
        }
        Arrays.sort(methods, first, end, comparator);
    }

    //Based on the order of modifiers, sort by Field-Type.SimpleName
    public static void sortFieldByTypeName(Field[] fields) {
        class FieldTypeNameComparator implements Comparator<Field> {
            public int compare(Field o1, Field o2) {
                return o1.getType().getSimpleName().compareTo(o2.getType().getSimpleName());
            }
        }

        if (fields == null || fields.length < 3)
            return;

        FieldTypeNameComparator comparator = new FieldTypeNameComparator();
        int first = 0, end = 1;
        int modifiers1 = fields[0].getModifiers();
        for (int i = 1; i < fields.length; i++) {
            int modifiers2 = fields[i].getModifiers();
            if (modifiers2 == modifiers1)
                end++;
            else {
                Arrays.sort(fields, first, end, comparator);

                modifiers1 = modifiers2;
                first = end++;
            }
        }
        Arrays.sort(fields, first, end, comparator);
    }

    /**
     * Prints all fields of a class
     */
    private static void getFields(Class cl, StringBuilder sb) {
        sb.append("   //Fields\n");
        Field[] fields = cl.getDeclaredFields();
        Arrays.sort(fields, new MemberComparator());
        sortFieldByTypeName(fields);

        for (Field f : fields) {
            Class type = f.getType();
            String name = f.getName();
            sb.append("   " + Modifier.toString(f.getModifiers()));
            sb.append(" " + type.getSimpleName() + " " + name + ";\n");
        }
        sb.append("\n");
    }

    /**
     * Get field's value by reflect
     * 
     * @param fieldName
     * @param object
     * @return
     */
    public static Object getFieldValue(String fieldName, Object object) {
        Object obj = null;
        try {
            Field f = object.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            obj = f.get(object);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /** 
     * invoke method by reflect
     * 
     * @param methodName doIt
     * @param argsTypes new Class[] { int.class, String.class }
     * @param args new Object[] { 23, "Perfect" }
     */
    public static Object invoke(String methodName, Object object, Class[] argsTypes, Object[] args) {
        Object result = null;

        try {
            Method m = object.getClass().getDeclaredMethod(methodName, argsTypes);
            m.setAccessible(true);
            result = m.invoke(object, args);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public static Method getMethod(String methodName, Class<?> cls, Class<?>...args) {
        Method m1 = null;
        try {
            m1 = cls.getDeclaredMethod(methodName, args);            
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        
        return m1;
    }
}
