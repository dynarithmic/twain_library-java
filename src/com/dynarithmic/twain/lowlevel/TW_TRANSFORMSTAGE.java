package com.dynarithmic.twain.lowlevel;

public class TW_TRANSFORMSTAGE extends TwainLowLevel
{
    private TW_DECODEFUNCTION[] Decode = new TW_DECODEFUNCTION[3];
    private TW_FIX32[][] Mix = new TW_FIX32[3][3];

    public TW_TRANSFORMSTAGE()
    {
        for (int i = 0; i < 3; ++i)
            Decode[i] = new TW_DECODEFUNCTION();
        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 3; ++j)
                Mix[i][j] = new TW_FIX32();
        }
    }

    public TW_DECODEFUNCTION[] getDecode()
    {
        return Decode;
    }

    public TW_DECODEFUNCTION getDecodeValue(int i)
    {
        return Decode[i];
    }

    public TW_FIX32[] getMixRow(int i)
    {
        return Mix[i];
    }

    public TW_FIX32 getMixValue(int i, int j)
    {
        return Mix[i][j];
    }

    public TW_FIX32[][] getMix()
    {
        return Mix;
    }

    public TW_TRANSFORMSTAGE setDecode(TW_DECODEFUNCTION[] decode)
    {
        Decode = decode;
        return this;
    }

    public TW_TRANSFORMSTAGE setDecodeValue(TW_DECODEFUNCTION decode, int i)
    {
        Decode[i] = decode;
        return this;
    }

    public TW_TRANSFORMSTAGE setMix(TW_FIX32[][] mix)
    {
        Mix = mix;
        return this;
    }

    public TW_TRANSFORMSTAGE setMixValue(TW_FIX32 value, int i, int j)
    {
        Mix[i][j] = value;
        return this;
    }
}
