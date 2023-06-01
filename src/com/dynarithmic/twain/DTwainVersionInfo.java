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
package com.dynarithmic.twain;

import com.dynarithmic.twain.DTwainConstants.DTwainVersionFlags;

public class DTwainVersionInfo
{
    private int majorVersion;
    private int minorVersion;
    private int patchVersion;
    private int versionType;
    private String executionPath;
    private String longVersionName;
    private String shortVersionName;
    private String versionCopyright;

    public String toString() { return longVersionName; }

    public DTwainVersionInfo()
    {
        setMajorVersion(-1);
        setMinorVersion(-1);
        setPatchVersion(-1);
        setVersionType(0);
        executionPath = "";
    }

    public DTwainVersionInfo(int majorV, int minorV, int patchV, int versionV, String exePath)
    {
        setMajorVersion(majorV);
        setMinorVersion(minorV);
        setPatchVersion(patchV);
        setVersionType(versionV);
        executionPath = exePath;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    private void setMajorVersion(int majorVersion) {
        this.majorVersion = majorVersion;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    private void setMinorVersion(int minorVersion) {
        this.minorVersion = minorVersion;
    }

    @SuppressWarnings("unused")
    private void setExecutionPath(String exePath)
    {
        this.executionPath = exePath;
    }

    public String getExecutionPath()
    {
        return this.executionPath;
    }

    public int getPatchVersion() {
        return patchVersion;
    }

    private void setPatchVersion(int patchVersion) {
        this.patchVersion = patchVersion;
    }

    public int getVersionType() {
        return versionType;
    }

    private void setVersionType(int versionType) {
        this.versionType = versionType;
    }

    private void setVersionCopyright(String sCopyright)
    {
        this.versionCopyright = sCopyright;
    }
    
    public String getVersionCopyright()
    {
        return this.versionCopyright;
    }
    
    public boolean is32Bit()
    {
        return this.versionType != -1 &&
                (versionType | DTwainVersionFlags.DTWAIN_32BIT_VERSION) != 0;
    }

    public boolean is64Bit()
    {
        return this.versionType != -1 &&
                (this.versionType | DTwainVersionFlags.DTWAIN_64BIT_VERSION) != 0;
    }

    public boolean isUnicode()
    {
        return this.versionType != -1 &&
                (this.versionType | DTwainVersionFlags.DTWAIN_UNICODE_VERSION) != 0;
    }

    public boolean isAnsi()
    {
        if (this.versionType == -1)
            return false;
        return !isUnicode();
    }

    public boolean isDebug()
    {
        return this.versionType != -1 &&
                (this.versionType | DTwainVersionFlags.DTWAIN_DEVELOP_VERSION) != 0;
    }

    public String getLongVersionName()
    {
        return longVersionName;
    }

    public String getShortVersionName()
    {
        return shortVersionName;
    }

    public void setLongVersionName(String longVersionName)
    {
        this.longVersionName = longVersionName;
    }

    public void setShortVersionName(String shortVersionName)
    {
        this.shortVersionName = shortVersionName;
    }
}
