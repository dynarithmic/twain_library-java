package com.dynarithmic.twain.lowlevel;

public class TW_PALETTE8 extends TwainLowLevel
{
    private TW_UINT16 NumColors = new TW_UINT16();
    private TW_UINT16 PaletteType = new TW_UINT16();
    private TW_ELEMENT8[] Colors = new TW_ELEMENT8[256];

    public TW_PALETTE8()
    {
        for (int i = 0; i < 256; ++i)
            Colors[i] = new TW_ELEMENT8();
    }

    public TW_UINT16 getNumColors()
    {
        return NumColors;
    }

    public TW_UINT16 getPaletteType()
    {
        return PaletteType;
    }

    public TW_ELEMENT8[] getColors()
    {
        return Colors;
    }

    public TW_ELEMENT8 getColorValue(int which)
    {
        return Colors[which];
    }

    public TW_PALETTE8 setNumColors(TW_UINT16 numColors)
    {
        NumColors = numColors;
        return this;
    }

    public TW_PALETTE8 setPaletteType(TW_UINT16 paletteType)
    {
        PaletteType = paletteType;
        return this;
    }

    public TW_PALETTE8 setColors(TW_ELEMENT8[] colors)
    {
        Colors = colors;
        return this;
    }

    public TW_PALETTE8 setPaletteType(int paletteType)
    {
        PaletteType.setValue(paletteType);
        return this;
    }

    public TW_PALETTE8 setNumColors(int numColors)
    {
        NumColors.setValue(numColors);
        return this;
    }

    public TW_PALETTE8 setColorValue(TW_ELEMENT8 val, int which)
    {
        Colors[which] = val;
        return this;
    }

}
