package com.dynarithmic.twain.highlevel.acquirecharacteristics;

import com.dynarithmic.twain.lowlevel.TwainConstants;

public class ImageInformationOptions
{
    private String author = "";
    private String caption = "";
    private boolean extendedImageInfoAvailable = false;

    public ImageInformationOptions setAuthor(String author)
    {
        this.author = author;
        return this;
    }

    public ImageInformationOptions setCaption(String caption)
    {
        this.caption = caption;
        return this;
    }

    public ImageInformationOptions setExtendedImageInfoAvailable(boolean extendedImageInfoAvailable)
    {
        this.extendedImageInfoAvailable = extendedImageInfoAvailable;
        return this;
    }

    public String getAuthor()
    {
        return author;
    }

    public String getCaption()
    {
        return caption;
    }

    public boolean isExtendedImageInfoAvailable()
    {
        return extendedImageInfoAvailable;
    }

    static protected final int affectedCaps[] = { TwainConstants.CAPS.CAP_AUTHOR,
                                                  TwainConstants.CAPS.CAP_CAPTION,
                                                  TwainConstants.CAPS.ICAP_EXTIMAGEINFO};

    static public final int [] getAffectedCaps()
    {
        return affectedCaps;
    }
}
