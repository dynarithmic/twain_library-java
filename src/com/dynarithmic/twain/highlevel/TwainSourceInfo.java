package com.dynarithmic.twain.highlevel;

public class TwainSourceInfo extends TwainAppInfo
{
    public TwainSourceInfo()
    { super();  }

    public TwainSourceInfo(String ver, String manu, String prodFamily, String prodName, int major, int minor)
    {
        super(ver, manu, prodFamily, prodName);
        m_majorNum = major;
        m_minorNum = minor;
    }

    public int getMajorNum() { return m_majorNum; }
    public int getMinorNum() { return m_minorNum; }

    int m_majorNum;
    int m_minorNum;
}
