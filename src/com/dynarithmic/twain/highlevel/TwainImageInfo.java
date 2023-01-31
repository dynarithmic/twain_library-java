package com.dynarithmic.twain.highlevel;

import com.dynarithmic.twain.lowlevel.TW_IMAGEINFO;

public class TwainImageInfo
{
    private TW_IMAGEINFO imageInfo = new TW_IMAGEINFO();

    public TwainImageInfo()
    {}

    public TW_IMAGEINFO getImageInfo()
    {
        return imageInfo;
    }

    public TwainImageInfo setImageInfo(TW_IMAGEINFO info)
    {
        if (info == null)
            throw new IllegalArgumentException();
        imageInfo = info;
        return this;
    }

    public boolean isValid()
    {
        return imageInfo.getBitsPerPixel().getValue() != 0;
    }
}