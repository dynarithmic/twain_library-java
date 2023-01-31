package com.dynarithmic.twain.highlevel;

import com.dynarithmic.twain.DTwainConstants;
import com.dynarithmic.twain.DTwainConstants.DSMType;

public class TwainStartupOptions
{
    private String sResourcePath;
    private String sDSMSearchOrder;
    private String sDSMUserDir;
    private String sTemporaryDir;
    private boolean bAutoStartSession;
    private DSMType iDSMToUse;
    private String sResourceLanguage = "english";
    private TwainAppInfo appInfo = new TwainAppInfo();
    public static final String DEFAULT_SEARCH = "CWSOU";

    public TwainStartupOptions()
    {
        sResourcePath = "";
        sDSMSearchOrder = DEFAULT_SEARCH;
        sDSMUserDir = "";
        sTemporaryDir = "";
        bAutoStartSession = true;
        iDSMToUse = DTwainConstants.DSMType.LEGACY;
    }

    public TwainStartupOptions setAutoStartSession(boolean bStart)
    {
        bAutoStartSession = bStart;
        return this;
    }

    public TwainStartupOptions setTemporaryDirectory(String dir)
    {
        sTemporaryDir = dir;
        return this;
    }

    public String getTemporaryDirectory()
    {
        return sTemporaryDir;
    }

    public boolean isAutoStartSession()
    {
        return bAutoStartSession;
    }

    public TwainStartupOptions setResourcePath(String value)
    {
        sResourcePath = value;
        return this;
    }

    public TwainStartupOptions setDSMSearchOrder(String searchOrder, String userDir)
    {
        sDSMSearchOrder = searchOrder;
        sDSMUserDir = userDir;
        return this;
    }

    public TwainStartupOptions setDSMSearchOrder(String searchOrder)
    {
        sResourcePath = searchOrder;
        return this;
    }

    public TwainStartupOptions setDSMUserDirectory(String userDir)
    {
        sDSMUserDir = userDir;
        return this;
    }

    public String getDSMSearchOrder()
    {
        return sDSMSearchOrder;
    }

    public String getDSMUserDirectory()
    {
        return sDSMUserDir;
    }

    public String getResourcePath()
    {
        return sResourcePath;
    }

    public DSMType getDSMToUse()
    {
        return iDSMToUse;
    }

    public TwainStartupOptions setDSMToUse(DSMType dsm)
    {
        iDSMToUse = dsm;
        return this;
    }

    public TwainAppInfo getAppInfo()
    {
        return appInfo;
    }

    public TwainStartupOptions setAppInfo(TwainAppInfo info)
    {
        this.appInfo = info;
        return this;
    }

    public String getResourceLanguage()
    {
        return sResourceLanguage;
    }

    public TwainStartupOptions setResourceLanguage(String sResourceLanguage)
    {
        this.sResourceLanguage = sResourceLanguage;
        return this;
    }


}
