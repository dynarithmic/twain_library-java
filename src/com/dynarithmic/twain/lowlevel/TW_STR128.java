package com.dynarithmic.twain.lowlevel;

public class TW_STR128 extends TW_STRING
{
   public TW_STR128()
   {
       super(130, 128);
   }

   public TW_STR128(int val1, int val2)
   {
       this();
   }

    public String getValue()
    {
        return super.getValue();
    }

    public TW_STR128 setValue(String s)
    {
        super.setValue(s);
        return this;
    }

    public TW_STR128 setValue(TW_STR128 s)
    {
        super.setValue(s.getValue());
        return this;
    }
}
