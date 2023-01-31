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
