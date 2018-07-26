package com.zero.core.util;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;

public class ClassViewer {

    /**
     * "java.lang.reflect.Method", "java.util.List"
     * @param qualifiedClassName
     */
    @SuppressWarnings("rawtypes")
    public static void prtClassOnReflection(String qualifiedClassName) {
        if ((qualifiedClassName == null) || (qualifiedClassName.trim().equals("")))
            return;

        try {
            final String space4 = "    ";
            Class c = Class.forName(qualifiedClassName.trim());

            StringBuffer sb = new StringBuffer();
            sb.append(Modifier.toString(c.getModifiers()) + " ");
            if (!c.isInterface())
                sb.append("class ");

            sb.append(c.getName());
            if (c.getSuperclass() != null && c.getSuperclass() != Object.class)
                sb.append(" extends " + c.getSuperclass().getName() + " {" + "\n");
            else
                sb.append(" {" + "\n");

            Field[] fields = c.getDeclaredFields();
            for (int i = 0; i < fields.length; i++)
                sb.append(space4 + StripQualifiers.strip(fields[i].toString()) + ";" + "\n");

            if (fields.length > 0)
                sb.append("" + "\n");
            Constructor[] ctor = c.getDeclaredConstructors();
            for (int i = 0; i < ctor.length; i++)
                sb.append(space4 + StripQualifiers.strip(ctor[i].toString()) + ";" + "\n");

            if (ctor.length > 0)
                sb.append("" + "\n");
            Method[] m = c.getDeclaredMethods();
            //Arrays.sort(m, new MethodReturnTypeComparator());
            Arrays.sort(m, new MethodModifyComparator());
            for (int i = 0; i < m.length; i++)
                sb.append(space4 + StripQualifiers.strip(m[i].toString()) + ";" + "\n");

            sb.append("}" + "\n");
            print(sb);
            print("method amount:" + m.length);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void print(Object obj) {
        System.out.println(obj);
    }
}

class StripQualifiers {
    private StreamTokenizer st;

    private StripQualifiers(String qualified) {
        st = new StreamTokenizer(new StringReader(qualified));
        st.ordinaryChar(' '); // Keep the spaces
    }

    private String getNext() {
        String s = null;
        try {
            int token = st.nextToken();
            if (token != StreamTokenizer.TT_EOF) {
                switch (st.ttype) {
                case StreamTokenizer.TT_EOL:
                    s = null;
                    break;
                case StreamTokenizer.TT_NUMBER:
                    s = Double.toString(st.nval);
                    break;
                case StreamTokenizer.TT_WORD:
                    s = new String(st.sval);
                    break;
                default: // single character in ttype
                    s = String.valueOf((char) st.ttype);
                }
            }
        } catch (IOException e) {
            System.err.println("Error fetching token");
        }
        return s;
    }

    public static String strip(String qualified) {
        StripQualifiers sq = new StripQualifiers(qualified);
        String s = "", si;
        while ((si = sq.getNext()) != null) {
            int lastDot = si.lastIndexOf('.');
            if (lastDot != -1)
                si = si.substring(lastDot + 1);
            s += si;
        }
        return s;
    }
}

class MethodModifyComparator implements Comparator<Method> {
    public int compare(Method obj1, Method obj2) {
        return obj1.getModifiers() - obj2.getModifiers();
    }
}

class MethodReturnTypeComparator implements Comparator<Method> {
    public int compare(Method obj1, Method obj2) {
        return obj1.getReturnType().getName().compareTo(obj2.getReturnType().getName());
    }
}

class MethodNameComparator implements Comparator<Method> {
    public int compare(Method obj1, Method obj2) {
        return obj1.getName().compareTo(obj2.getName());
    }
}