package com.dynarithmic.twain.lowlevel;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.reflect.FieldUtils;

import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;

public class TwainConstantMapper<T>
{
    protected final Map<Integer, String> intToStringMap = new TreeMap<>();
    protected final Map<String, Integer> stringToIntMap = new TreeMap<>();
    private final Class<T> constantClass;
    private boolean isInitialized = false;

    public static class StringModifier
    {
        private StringModifier() {}
        public static final int MAKEUPPER = 1;
        public static final int TRIMLEFT = 2;
        public static final int TRIMRIGHT = 4;
        public static final int TRIMALL = TRIMLEFT | TRIMRIGHT;
        public static final int APPLYALL = MAKEUPPER | TRIMALL;
    }

    public TwainConstantMapper(Class<T> theClass)
    {
        constantClass = theClass;
        try
        {
            initialize();
        }
        catch (IllegalArgumentException | IllegalAccessException e)
        {
        	System.err.println("Cannot map TWAIN constant to string name");
        	System.err.println("Possible exceptions may be thrown by application if accessing Twain constants");
        }
    }

    public void initialize() throws IllegalArgumentException, IllegalAccessException
    {
        if (!isInitialized)
        {

            Field[] declaredFields = FieldUtils.getAllFields(constantClass);
            for (Field field : declaredFields)
            {
                if (java.lang.reflect.Modifier.isStatic(field.getModifiers()))
                {
                    intToStringMap.put((Integer) field.get(null), field.getName());
                    stringToIntMap.put(field.getName(), (Integer) field.get(null));
                }
            }
        }
        isInitialized = true;
    }

    public String toString(int nName) throws IllegalArgumentException, IllegalAccessException
    {
        if (!isInitialized)
            initialize();
        String s = intToStringMap.get(nName);
        return s == null ? "" : s;
    }

    public int toInt(String sName, int modifications) throws IllegalArgumentException, IllegalAccessException, DTwainJavaAPIException
    {
        if (!isInitialized)
            initialize();
        String origName = sName;
        if ((modifications & StringModifier.TRIMRIGHT) == StringModifier.TRIMRIGHT)
            sName = StringUtils.stripEnd(sName, null);
        if ((modifications & StringModifier.TRIMLEFT) == StringModifier.TRIMLEFT)
            sName = StringUtils.stripStart(sName, null);
        if ((modifications & StringModifier.MAKEUPPER) == StringModifier.MAKEUPPER)
            sName = StringUtils.upperCase(sName);
        if ( sName == null )
            throw new DTwainJavaAPIException("The string name \"" + origName + "\" using modifications " + modifications +
                                            " is not mapped to an integer");
        try
        {
            return stringToIntMap.get(sName);
        }
        catch(Exception e)
        {
            throw new DTwainJavaAPIException("The string name \"" + origName + "\" using modifications " + modifications +
                    " is not mapped to an integer\nAdditional Information:\n" + e);
        }
    }

    public int toInt(String sName) throws IllegalArgumentException, IllegalAccessException, DTwainJavaAPIException
    {
        return toInt(sName, StringModifier.APPLYALL);
    }
}