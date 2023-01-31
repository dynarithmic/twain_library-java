package com.dynarithmic.twain.lowlevel;

public class TW_STR255 extends TW_STRING
{
   public TW_STR255()
   {
       super(256, 255);
   }

   public TW_STR255(int val1, int val2)
   {
       this();
   }

    public String getValue()
    {
        return super.getValue();
    }

    public TW_STR255 setValue(String s)
    {
        super.setValue(s);
        return this;
    }

    public TW_STR255 setValue(TW_STR255 s)
    {
        super.setValue(s.getValue());
        return this;
    }
}
