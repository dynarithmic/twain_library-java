package com.dynarithmic.twain.lowlevel;

public class TW_STRING extends TwainLowLevel
{
    protected char[] data = null;
    protected int actualsize = 0;
    public TW_STRING()
    {
        data = new char[0];
    }

    public TW_STRING(int allocSize, int actual) throws IllegalArgumentException
    {
        if ( actual > allocSize )
            throw new IllegalArgumentException();
        data = new char [allocSize];
        actualsize = actual;
    }

    protected void setSize(int allocSize, int actual) throws IllegalArgumentException
    {
        if ( actual > allocSize )
            throw new IllegalArgumentException();
        data = new char [allocSize];
        actualsize = actual;
    }

    public String getValue()
    {
        if ( data != null )
        {
            String s = new String(data);
            int nullIndex = s.indexOf(0);
            if ( nullIndex != -1 )
                return s.substring(0, nullIndex);
            return s;
        }
        return "";
    }

    public TW_STRING setValue(String s)
    {
        if ( data != null)
        {
            int minToCopy = Math.min(actualsize,  s.length());
            for (int i = 0; i < minToCopy; ++i)
                data[i] = s.charAt(i);
            data[minToCopy] = '\0';
        }
        return this;
    }

    public char[] getData()
    {
        return data;
    }

    public int getSize()
    {
        return actualsize;
    }
}
