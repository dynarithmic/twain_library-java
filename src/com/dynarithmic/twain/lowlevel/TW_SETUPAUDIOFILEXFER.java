package com.dynarithmic.twain.lowlevel;

public class TW_SETUPAUDIOFILEXFER extends TwainLowLevel
{
    private TW_STR255 FileName = new TW_STR255();
    private TW_UINT16 Format = new TW_UINT16();
    private TW_INT16 VRefNum = new TW_INT16();

    public TW_SETUPAUDIOFILEXFER()
    {
    }

    public TW_STR255 getFileName()
    {
        return FileName;
    }

    public TW_UINT16 getFormat()
    {
        return Format;
    }

    public TW_INT16 getVRefNum()
    {
        return VRefNum;
    }

    public TW_SETUPAUDIOFILEXFER setFileName(TW_STR255 fileName)
    {
        FileName = fileName;
        return this;
    }

    public TW_SETUPAUDIOFILEXFER setFormat(TW_UINT16 format)
    {
        Format = format;
        return this;
    }

    public TW_SETUPAUDIOFILEXFER setVRefNum(TW_INT16 vRefNum)
    {
        VRefNum = vRefNum;
        return this;
    }

    public TW_SETUPAUDIOFILEXFER setFileName(String fileName)
    {
        FileName.setValue(fileName);
        return this;
    }

    public TW_SETUPAUDIOFILEXFER setFormat(int format)
    {
        Format.setValue(format);
        return this;
    }

    public TW_SETUPAUDIOFILEXFER setVRefNum(short vRefNum)
    {
        VRefNum.setValue(vRefNum);
        return this;
    }

}
