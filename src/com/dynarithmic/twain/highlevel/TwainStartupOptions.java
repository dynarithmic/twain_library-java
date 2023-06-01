/*
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2023 Dynarithmic Software.

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

import com.dynarithmic.twain.DTwainConstants;
import com.dynarithmic.twain.DTwainConstants.DSMType;

public class TwainStartupOptions
{
    private String sResourcePath;
    private String sDSMSearchOrder;
    private String sDSMUserDir;
    private String sTemporaryDir;
    private boolean bAutoStartSession;
    private DSMType iDSM;
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
        iDSM = DTwainConstants.DSMType.LEGACY;
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
        return iDSM;
    }

    public TwainStartupOptions setDSM(DSMType dsm)
    {
        iDSM = dsm;
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
