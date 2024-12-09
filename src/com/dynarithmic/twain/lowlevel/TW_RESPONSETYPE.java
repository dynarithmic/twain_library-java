/*
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2025 Dynarithmic Software.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    DYNARITHMIC SOFTWARE. DYNARITHMIC SOFTWARE DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS.

 */
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
