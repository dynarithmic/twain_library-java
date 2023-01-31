package com.dynarithmic.twain.lowlevel;

import com.dynarithmic.twain.lowlevel.TwainConstants.DF;

public class SupportedGroupsHandler
{
    private static final TwainConstantMapper<DF> s_map = new TwainConstantMapper<>(TwainConstants.DF.class);
    private SupportedGroupsHandler()
    {}

    public static String asString(int val)
    {
        StringBuilder sOut = new StringBuilder();
        long numBits = 32;
        boolean foundGroup = false;
        for (int i = 0; i < numBits; ++i)
        {
            int curGroup = 1 << i;
            if ( (val & curGroup) != 0)
            {
                if ( foundGroup )
                    sOut.append(",");
                try
                {
                    sOut.append(" ").append(s_map.toString(curGroup));
                }
                catch (IllegalArgumentException | IllegalAccessException e)
                {
                    sOut.append(" Unknown(").append(curGroup).append(")");
                }
                foundGroup = true;
            }
        }
        return sOut.toString();
    }
}
