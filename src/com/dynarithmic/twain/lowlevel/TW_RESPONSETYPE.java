package com.dynarithmic.twain.lowlevel;

public class TW_RESPONSETYPE extends TwainLowLevel
{
   private TW_ELEMENT8[] Response = new TW_ELEMENT8[0];

   public TW_RESPONSETYPE() {}

   public int getNumItems()
   {
       return Response.length;
   }

   public TW_ELEMENT8[] getResponse()
   {
       return Response;
   }

   public TW_ELEMENT8 getResponseValue(int i)
   {
       return Response[i];
   }

   public TW_RESPONSETYPE setNumItems(int val)
   {
       if ( val != Response.length )
       {
           TW_ELEMENT8[] temp = new TW_ELEMENT8[val];
           int minElements = Math.min(val, Response.length);
           for (int i = 0; i < val; ++i)
               temp[i] = new TW_ELEMENT8();
           System.arraycopy(Response, 0, temp, 0, minElements);
           Response = temp;
       }
       return this;
   }

   public TW_RESPONSETYPE setResponse(TW_ELEMENT8[] val)
   {
       Response = val;
       return this;
   }

   public TW_RESPONSETYPE setResponseValue(TW_ELEMENT8 val, int i)
   {
       Response[i] = val;
       return this;
   }
}
