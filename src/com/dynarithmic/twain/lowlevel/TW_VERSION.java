package com.dynarithmic.twain.lowlevel;

public class TW_VERSION extends TwainLowLevel
{
    private TW_UINT16 MajorNum = new TW_UINT16();
    private TW_UINT16 MinorNum =  new TW_UINT16();
    private TW_UINT16 Language = new TW_UINT16();
    private TW_UINT16 Country = new TW_UINT16();
    private TW_STR32    Info = new TW_STR32();

    public TW_VERSION()
    {
    }

    public TW_UINT16 getMajorNum()
    {
        return MajorNum;
    }

    public TW_UINT16 getMinorNum()
    {
        return MinorNum;
    }

    public TW_UINT16 getLanguage()
    {
        return Language;
    }

    public TW_UINT16 getCountry()
    {
        return Country;
    }

    public TW_STR32 getInfo()
    {
        return Info;
    }

    public TW_VERSION setMajorNum(TW_UINT16 majorNum)
    {
        MajorNum = majorNum;
        return this;
    }

    public TW_VERSION setMinorNum(TW_UINT16 minorNum)
    {
        MinorNum = minorNum;
        return this;
    }

    public TW_VERSION setLanguage(TW_UINT16 language)
    {
        Language = language;
        return this;
    }

    public TW_VERSION setCountry(TW_UINT16 country)
    {
        Country = country;
        return this;
    }

    public TW_VERSION setInfo(TW_STR32 info)
    {
        Info = info;
        return this;
    }

    public TW_VERSION setMajorNum(int majorNum)
    {
        MajorNum.setValue(majorNum);
        return this;
    }

    public TW_VERSION setMinorNum(int minorNum)
    {
        MinorNum.setValue(minorNum);
        return this;
    }

    public TW_VERSION setLanguage(int language)
    {
        Language.setValue(language);
        return this;
    }

    public TW_VERSION setCountry(int country)
    {
        Country.setValue(country);
        return this;
    }

    public TW_VERSION setInfo(String info)
    {
        Info.setValue(info);
        return this;
    }
}