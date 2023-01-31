package com.dynarithmic.twain.lowlevel;

public class TW_METRICS extends TwainLowLevel
{
    private TW_UINT32 SizeOf = new TW_UINT32();
    private TW_UINT32 ImageCount = new TW_UINT32();
    private TW_UINT32 SheetCount = new TW_UINT32();

    public TW_METRICS()
    {
    }

    public TW_UINT32 getSizeOf()
    {
        return SizeOf;
    }

    public TW_UINT32 getImageCount()
    {
        return ImageCount;
    }

    public TW_UINT32 getSheetCount()
    {
        return SheetCount;
    }

    public TW_METRICS setSizeOf(TW_UINT32 sizeOf)
    {
        SizeOf = sizeOf;
        return this;
    }

    public TW_METRICS setImageCount(TW_UINT32 imageCount)
    {
        ImageCount = imageCount;
        return this;
    }

    public TW_METRICS setSheetCount(TW_UINT32 sheetCount)
    {
        SheetCount = sheetCount;
        return this;
    }

    public TW_METRICS setSizeOf(long sizeOf)
    {
        SizeOf.setValue(sizeOf);
        return this;
    }

    public TW_METRICS setImageCount(long imageCount)
    {
        ImageCount.setValue(imageCount);
        return this;
    }

    public TW_METRICS setSheetCount(long sheetCount)
    {
        SheetCount.setValue(sheetCount);
        return this;
    }
}
