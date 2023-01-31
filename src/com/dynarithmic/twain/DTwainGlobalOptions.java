package com.dynarithmic.twain;

import com.dynarithmic.twain.DTwainConstants.JNIVersion;
import com.dynarithmic.twain.highlevel.TwainSession;

public class DTwainGlobalOptions
{
    static private int jniVersion = JNIVersion.JNI_32U;
    public static int getJNIVersion() { return jniVersion; }
    public static String getJNIVersionAsString()
    {
        String s;
        try
        {
            s = TwainSession.getJNIVersionAsString(jniVersion);
            if ( s == null || s.isEmpty() )
                return "";
            return s;
        }
        catch (Exception e)
        {
            return "";
        }
    }

    public static void setJNIVersion(int version)
    {
        String s;
        try
        {
            s = TwainSession.getJNIVersionAsString(version);
            if ( s == null || s.isEmpty() )
                jniVersion = JNIVersion.JNI_32U;
            else
                jniVersion = version;
        }
        catch (Exception e)
        {
            jniVersion = JNIVersion.JNI_32U;
        }
    }

    public static void setJNIVersion(String version)
    {
        try
        {
            jniVersion = TwainSession.getJNIVersionAsInt(version);
        }
        catch (Exception e)
        {
            jniVersion = JNIVersion.JNI_32U;
        }
    }
}
