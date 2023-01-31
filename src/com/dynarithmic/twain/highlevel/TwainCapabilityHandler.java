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