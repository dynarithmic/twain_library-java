package com.dynarithmic.twain.highlevel.acquirecharacteristics;

import java.util.ArrayList;
import java.util.List;

import com.dynarithmic.twain.lowlevel.TwainConstants;
import com.dynarithmic.twain.lowlevel.TwainConstants.ICAP_MIRROR;
import com.dynarithmic.twain.lowlevel.TwainConstants.ICAP_ORIENTATION;

public class ImageParameterOptions
{
    private boolean thumbnailsEnabled = false;
    private boolean autoBrightEnabled = false;
    private double brightness = defaultBrightness;
    private double contrast = defaultContrast;
    private double highlight = defaultHighlight;
    private double shadow   = defaultShadow;
    private double XScaling = defaultXScaling;
    private double YScaling = defaultYScaling;
    private double rotation = defaultRotation;
    private List<Integer> imageDataSets = new ArrayList<>();
    private ICAP_MIRROR mirror = defaultMirror;
    private ICAP_ORIENTATION orientation = defaultOrientation;

    public static final double defaultBrightness = Double.MIN_VALUE;
    public static final double defaultContrast = Double.MIN_VALUE;
    public static final double defaultHighlight = Double.MIN_VALUE;
    public static final double defaultShadow = Double.MIN_VALUE;
    public static final double defaultXScaling = Double.MIN_VALUE;
    public static final double defaultYScaling = Double.MIN_VALUE;
    public static final double defaultRotation = Double.MIN_VALUE;
    public static final ICAP_MIRROR defaultMirror = ICAP_MIRROR.TWMR_DEFAULT;
    public static final ICAP_ORIENTATION defaultOrientation = ICAP_ORIENTATION.TWOR_DEFAULT;

    public boolean isThumbnailsEnabled()
    {
        return thumbnailsEnabled;
    }

    public boolean isAutoBrightEnabled()
    {
        return autoBrightEnabled;
    }

    public double getBrightness()
    {
        return brightness;
    }

    public double getContrast()
    {
        return contrast;
    }

    public double getHighlight()
    {
        return highlight;
    }

    public double getShadow()
    {
        return shadow;
    }

    public double getXScaling()
    {
        return XScaling;
    }

    public double getYScaling()
    {
        return YScaling;
    }

    public double getRotation()
    {
        return rotation;
    }

    public ICAP_MIRROR getMirror()
    {
        return mirror;
    }

    public ICAP_ORIENTATION getOrientation()
    {
        return orientation;
    }

    public ImageParameterOptions enableThumbnails(boolean thumbnailsEnabled)
    {
        this.thumbnailsEnabled = thumbnailsEnabled;
        return this;
    }

    public ImageParameterOptions enableAutoBright(boolean autoBrightEnabled)
    {
        this.autoBrightEnabled = autoBrightEnabled;
        return this;
    }

    public ImageParameterOptions setBrightness(double brightness)
    {
        this.brightness = brightness;
        return this;
    }

    public ImageParameterOptions setContrast(double contrast)
    {
        this.contrast = contrast;
        return this;
    }

    public ImageParameterOptions setHighlight(double highlight)
    {
        this.highlight = highlight;
        return this;
    }

    public ImageParameterOptions setShadow(double shadow)
    {
        this.shadow = shadow;
        return this;
    }

    public ImageParameterOptions setXScaling(double xScaling)
    {
        XScaling = xScaling;
        return this;
    }

    public ImageParameterOptions setYScaling(double yScaling)
    {
        YScaling = yScaling;
        return this;
    }

    public ImageParameterOptions setRotation(double rotation)
    {
        this.rotation = rotation;
        return this;
    }

    public ImageParameterOptions setMirror(ICAP_MIRROR mirror)
    {
        this.mirror = mirror;
        return this;
    }

    public ImageParameterOptions setOrientation(ICAP_ORIENTATION orientation)
    {
        this.orientation = orientation;
        return this;
    }

    public List<Integer> getImageDataSets()
    {
        return imageDataSets;
    }

    public ImageParameterOptions setImageDataSets(List<Integer> imageDataSets)
    {
        this.imageDataSets = imageDataSets;
        return this;
    }

    static protected final int affectedCaps[] = { TwainConstants.CAPS.CAP_THUMBNAILSENABLED,
                                                TwainConstants.CAPS.ICAP_AUTOBRIGHT,
                                                TwainConstants.CAPS.ICAP_BRIGHTNESS,
                                                TwainConstants.CAPS.ICAP_CONTRAST,
                                                TwainConstants.CAPS.ICAP_HIGHLIGHT,
                                                TwainConstants.CAPS.ICAP_MIRROR,
                                                TwainConstants.CAPS.ICAP_ORIENTATION,
                                                TwainConstants.CAPS.ICAP_ROTATION,
                                                TwainConstants.CAPS.ICAP_SHADOW,
                                                TwainConstants.CAPS.ICAP_XSCALING,
                                                TwainConstants.CAPS.ICAP_YSCALING,
                                                TwainConstants.CAPS.ICAP_IMAGEDATASET};


    static public final int [] getAffectedCaps()
    {
    return affectedCaps;
    }

}
