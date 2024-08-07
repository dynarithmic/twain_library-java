/*
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2024 Dynarithmic Software.

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
package com.dynarithmic.twain.highlevel;

import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;
import com.dynarithmic.twain.lowlevel.TwainConstantMapper;
import com.dynarithmic.twain.lowlevel.TwainConstants;
import com.dynarithmic.twain.lowlevel.TwainConstants.CAPS;

/**
 * Contains the TWAIN Source capability constants.
 *
 */
public class TwainCapabilityHandler
{
    private TwainCapabilityHandler( ) {}

    private static final TwainConstantMapper<CAPS> capToStringMap =
                    new TwainConstantMapper<>(TwainConstants.CAPS.class);

    /**
     * Get the string name of the capability.  If the capability is a custom capability, the
     * returned string is "CAP_CUSTOMBASE + xxxx", where xxxx is an integer constant, and CAP_CUSTOMBASE is
     * equal to 0x8000.
     * @param nCapName
     * Capability value.
     * @return
     * String name of capability.
     */
    public static String toString(int nCapName)
    {
        if ( nCapName < TwainConstants.CAPS.CAP_CUSTOMBASE )
        {
           try
           {
               return capToStringMap.toString(nCapName);
           }
           catch (IllegalArgumentException | IllegalAccessException e)
           {
              return "";
           }
        }
        else
        {
            String sCapName;
            try
            {
                sCapName = capToStringMap.toString(TwainConstants.CAPS.CAP_CUSTOMBASE);
                sCapName += " + ";
                sCapName= sCapName + (nCapName - TwainConstants.CAPS.CAP_CUSTOMBASE);
                return sCapName;
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                return "";
            }
        }
    }

    /**
     * Get the integer value of the string capability name.  If the capability
     * is a custom capability, <i>sCapName</i> must be of the form<p>
     * "CAP_CUSTOMBASE + xxxx", where xxxx is some integer (base 10) value.
     * @param sCapName
     * The capability string name.
     * @return
     * The integer value of the capability.
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws DTwainJavaAPIException
     */
    public static int toInt(String sCapName) throws IllegalArgumentException, IllegalAccessException, DTwainJavaAPIException
    {
      // trim the whitespace from the name
      sCapName= sCapName.trim();

      // convert to upper case
      sCapName= sCapName.toUpperCase();

      // check if it's a custom cap
      if ( sCapName.length() >= 14 && sCapName.startsWith("CAP_CUSTOMBASE"))
      {
          // find the '+' character
          int nIndex= sCapName.indexOf('+');
          if ( nIndex != -1 )
          {
              String sSuffix= sCapName.substring(nIndex+1);
              sSuffix= sSuffix.trim();
              return TwainConstants.CAPS.CAP_CUSTOMBASE + Integer.parseInt(sSuffix);
          }
          return 0;
      }
      return capToStringMap.toInt(sCapName);
    }
}