package com.dynarithmic.twain.lowlevel;

public class TW_STR32 extends TW_STRING
{
   public TW_STR32()
   {
       super(34, 32);
   }

   public TW_STR32(int val1, int val2)
   {
       this();
   }

   public String getValue()
   {
      return super.getValue();
   }

   public TW_STR32 setValue(String s)
   {
        super.setValue(s);
        return this;
    }

   public TW_STR32 setValue(TW_STR32 s)
   {
       super.setValue(s.getValue());
       return this;
   }
}
