package com.dynarithmic.twain.lowlevel;

import java.math.BigInteger;

public class TW_UINTPTR extends TwainLowLevel
{
    protected BigInteger value = BigInteger.valueOf(0);

    public TW_UINTPTR()
    {}

    public TW_UINTPTR(long val)
    {
        setValue(val);
    }

    public TW_UINTPTR(String val)
    {
        setValue(val);
    }

    public TW_UINTPTR(BigInteger val)
    {
        setValue(val);
    }

    public TW_UINTPTR setValue(BigInteger val)
    {
        UnsignedUtils.checkUnsigned64(val);
        value = new BigInteger(val.toString());
        return this;
    }

    public TW_UINTPTR setValue(long val)
    {
        UnsignedUtils.checkUnsigned64(val);
        value = new BigInteger(val + "");
        return this;
    }

    public TW_UINTPTR setValue(String s)
    {
        value = new BigInteger(s);
        return this;
    }

    public long getValue()
    {
        return value.longValue();
    }

    public BigInteger getValueAsUnsigned()
    {
        return value;
    }

    public String getValueAsString()
    {
        return value.toString();
    }
}
