
package com.dynarithmic.twain.lowlevel;

public class TW_STR1024 extends TW_STRING
{
   public TW_STR1024()
   {
       super(1026, 1024);
   }

   public TW_STR1024(int val1, int val2)
   {
       this();
   }

    public String getValue()
    {
        return super.getValue();
    }

    public TW_STR1024 setValue(String s)
    {
        super.setValue(s);
        return this;
    }

    public TW_STR1024 setValue(TW_STR1024 s)
    {
        super.setValue(s.getValue());
        return this;
    }

}
