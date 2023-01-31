package com.dynarithmic.twain.highlevel.acquirecharacteristics;

import java.util.ArrayList;
import java.util.List;

import com.dynarithmic.twain.lowlevel.TwainConstants.*;

public class ImageTypeOptions
{
    private int bitDepth = defaultBitDepth;
    private boolean customHalfToneEnabled = false;

    private ICAP_PIXELTYPE pixelType = defaultPixelType;
    private ICAP_BITDEPTHREDUCTION bitDepthReduction = defaultBitDepthReduction;
    private ICAP_BITORDER bitOrder = defaultBitOrder;
    private String halfTone = defaultHalftone;
    private ICAP_PIXELFLAVOR pixelFlavor = defaultPixelFlavor;
    private double threshold = defaultThreshold;
    private boolean negateImage = false;
    private List<Byte> customHalfTone = new ArrayList<>();

    public static final int defaultBitDepth = 0;
    public static final ICAP_PIXELTYPE defaultPixelType = ICAP_PIXELTYPE.TWPT_DEFAULT;
    public static final ICAP_BITDEPTHREDUCTION defaultBitDepthReduction = ICAP_BITDEPTHREDUCTION.TWBR_DEFAULT;
    public static final ICAP_BITORDER defaultBitOrder = ICAP_BITORDER.TWBO_DEFAULT;
    public static final ICAP_PIXELFLAVOR defaultPixelFlavor = ICAP_PIXELFLAVOR.TWPF_DEFAULT;
    public static final double defaultThreshold = Double.MIN_VALUE;
    public static final String defaultHalftone = "";

    public int getBitDepth()
    {
        return bitDepth;
    }

    public ICAP_PIXELTYPE getPixelType()
    {
        return pixelType;
    }

    public ICAP_BITDEPTHREDUCTION getBitDepthReduction()
    {
        return bitDepthReduction;
    }

    public ICAP_BITORDER getBitOrder()
    {
        return bitOrder;
    }

    public String getHalfTone()
    {
        return halfTone;
    }

    public ICAP_PIXELFLAVOR getPixelFlavor()
    {
        return pixelFlavor;
    }

    public double getThreshold()
    {
        return threshold;
    }

    public boolean isNegateImage()
    {
        return negateImage;
    }

    public ImageTypeOptions setBitDepth(int bitDepth)
    {
        this.bitDepth = bitDepth;
        return this;
    }

    public ImageTypeOptions setPixelType(ICAP_PIXELTYPE pixelType)
    {
        this.pixelType = pixelType;
        return this;
    }

    public ImageTypeOptions setBitDepthReduction(ICAP_BITDEPTHREDUCTION bitDepthReduction)
    {
        this.bitDepthReduction = bitDepthReduction;
        return this;
    }

    public ImageTypeOptions setBitOrder(ICAP_BITORDER bitOrder)
    {
        this.bitOrder = bitOrder;
        return this;
    }

    public ImageTypeOptions setHalfTone(String halfTone)
    {
        this.halfTone = halfTone;
        return this;
    }

    public ImageTypeOptions setPixelFlavor(ICAP_PIXELFLAVOR pixelFlavor)
    {
        this.pixelFlavor = pixelFlavor;
        return this;
    }

    public ImageTypeOptions setThreshold(double threshold)
    {
        this.threshold = threshold;
        return this;
    }

    public ImageTypeOptions setNegateImage(boolean negateImage)
    {
        this.negateImage = negateImage;
        return this;
    }

    public List<Byte> getCustomHalfTone()
    {
        return customHalfTone;
    }

    public ImageTypeOptions setCustomHalfTone(List<Byte> customHalfTone)
    {
        this.customHalfTone = customHalfTone;
        return this;
    }

    public boolean isCustomHalfToneEnabled()
    {
        return this.customHalfToneEnabled;
    }

    public ImageTypeOptions enableCustomHalfTone(boolean enableCustomHalfTone)
    {
        this.customHalfToneEnabled = enableCustomHalfTone;
        return this;
    }

    static protected final int affectedCaps[] =
        {
                CAPS.ICAP_BITDEPTH,
                CAPS.ICAP_BITDEPTHREDUCTION,
                CAPS.ICAP_BITORDER,
                CAPS.ICAP_CUSTHALFTONE,
                CAPS.ICAP_HALFTONES,
                CAPS.ICAP_PIXELFLAVOR,
                CAPS.ICAP_PIXELTYPE,
                CAPS.ICAP_THRESHOLD
        };

    static public final int [] getAffectedCaps()
    {
        return affectedCaps;
    }



}
