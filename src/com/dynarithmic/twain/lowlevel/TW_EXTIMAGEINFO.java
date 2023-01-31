package com.dynarithmic.twain.lowlevel;

public class TW_EXTIMAGEINFO extends TwainLowLevel
{
    private final TW_UINT32   NumInfos = new TW_UINT32();
    private TW_INFO[]  Info = new TW_INFO[0];

    public TW_EXTIMAGEINFO() {}

    public TW_UINT32 getNumInfos()
    {
        return NumInfos;
    }

    public TW_INFO[] getInfo()
    {
        return Info;
    }

    public TW_INFO getOneInfo(int i)
    {
        return Info[i];
    }


    public TW_EXTIMAGEINFO setNumInfos(TW_UINT32 numInfos)
    {
        return setNumInfos(numInfos.getValue());
    }

    public TW_EXTIMAGEINFO setInfo(TW_INFO[] info)
    {
        Info = info;
        return this;
    }

    public TW_EXTIMAGEINFO setOneInfo(TW_INFO theInfo, int i )
    {
        Info[i] = theInfo;
        return this;
    }

    public TW_EXTIMAGEINFO setNumInfos(long numInfos)
    {
        UnsignedUtils.checkUnsigned32(numInfos);
        if ( numInfos != Info.length )
        {
            TW_INFO[] tempInfo = new TW_INFO[(int)numInfos];
            for (int i = 0; i < numInfos; ++i )
                tempInfo[i] = new TW_INFO();
            int minInfos = (int) Math.min(numInfos, Info.length);
            if (minInfos >= 0) System.arraycopy(Info, 0, tempInfo, 0, minInfos);
            Info = tempInfo;
        }
        NumInfos.setValue(numInfos);
        return this;
    }
}
