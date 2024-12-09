/*
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2025 Dynarithmic Software.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    DYNARITHMIC SOFTWARE. DYNARITHMIC SOFTWARE DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS.

 */
package com.dynarithmic.twain.highlevel.acquirecharacteristics;

import com.dynarithmic.twain.lowlevel.TwainConstants;
import com.dynarithmic.twain.lowlevel.TwainConstants.*;
/**
 * @author Dynarithmic Software
 * <p>AutoAdjustOptions gets/sets the auto adjustment options described by the TWAIN 2.4 specification for
 * any TWAIN-enabled device.<br>
 * If the device does not support the auto adjust option(s), these settings are ignored during the acquisition process.<br>
 *              (see "Automatic Adjustments", Chapter 10-3 of the TWAIN 2.4 specification)
 */
public class AutoAdjustOptions
{
    private boolean senseMedium = false;
    private int discardBlankPages = ICAP_AUTODISCARDBLANKPAGES.TWBP_DISABLE;
    private boolean borderDetection = false;
    private boolean colorEnabled = false;
    private TwainConstants.ICAP_PIXELTYPE colorNonColorPixelType = defaultcolorNonColorPixelType;
    private boolean deskew = false;
    private boolean lengthDetection = false;
    private boolean rotate = false;
    private TwainConstants.ICAP_AUTOSIZE autoSize = defaultAutoSize;
    private TwainConstants.ICAP_FLIPROTATION flipRotation = defaultFlipRotation;
    private TwainConstants.ICAP_IMAGEMERGE imageMerge = defaultImageMerge;
    private double mergeHeightThreshold = defaultMergeHeightThreshold;

    public static final TwainConstants.ICAP_PIXELTYPE defaultcolorNonColorPixelType = TwainConstants.ICAP_PIXELTYPE.TWPT_BW;
    public static final TwainConstants.ICAP_FLIPROTATION defaultFlipRotation = TwainConstants.ICAP_FLIPROTATION.TWFR_BOOK;
    public static final TwainConstants.ICAP_IMAGEMERGE defaultImageMerge = TwainConstants.ICAP_IMAGEMERGE.TWIM_NONE;
    public static final double defaultMergeHeightThreshold = 0.0;
    public static final int defaultAutoDiscardBlankPages = ICAP_AUTODISCARDBLANKPAGES.TWBP_DISABLE;
    public static final TwainConstants.ICAP_AUTOSIZE defaultAutoSize = TwainConstants.ICAP_AUTOSIZE.TWAS_NONE;

    /**
     * @param senseMedium Turn on the sense medium.  Configures a Source to check for paper<br>
     * in the Automatic Document Feeder, and if it finds any, then automatically capture all<br>
     * of its images from the Feeder.  If the Feeder is empty when acquisition starts, then <br>
     * all images are automatically captured from the Flatbed.
     * @return The current object
     * @see #isSenseMediumEnabled()
     */
    public AutoAdjustOptions enableSenseMedium(boolean senseMedium)
    {
        this.senseMedium = senseMedium;
        return this;
    }

    /**
     * @param discardBlankPages Values can be one of the following:
     * <ul>
     * <li>ICAP_AUTODISCARDBLANKPAGES.TWBP_DISABLE (turn off auto discard)</li>
     * <li>ICAP_AUTODISCARDBLANKPAGES.TWBP_AUTO   (turn on auto discard)</li>
     * <li>A value from 0 to Integer.MAX_VALUE to denote the byte cutoff point to identify which images are to be discarded</li></ul>
     * @return The current object
     * @see #getDiscardBlankPages()
     */
    public AutoAdjustOptions setDiscardBlankPages(int discardBlankPages)
    {
        this.discardBlankPages = discardBlankPages;
        return this;
    }

    /**
     * @param borderDetection  Turn on/off automatic border detection
     * @return The current object
     * @see #isBorderDetectionEnabled()
     */
    public AutoAdjustOptions enableBorderDetection(boolean borderDetection)
    {
        this.borderDetection = borderDetection;
        return this;
    }

    /**
     * @param colorEnabled Turn on/off detection of the pixel type
     * @return  The current object
     * @see #isColorEnabled()
     */
    public AutoAdjustOptions enableColor(boolean colorEnabled)
    {
        this.colorEnabled = colorEnabled;
        return this;
    }

    /**
     * @param colorNonColorPixelType The pixel type to set if setColorEnabled is enabled
     * @return The current object
     * @see #getColorNonColorPixelType()
     */
    public AutoAdjustOptions setColorNonColorPixelType(TwainConstants.ICAP_PIXELTYPE colorNonColorPixelType)
    {
        this.colorNonColorPixelType = colorNonColorPixelType;
        return this;
    }

    /**
     * @param deskew Turn on/off auto deskew
     * @return The current object
     * @see #isDeskewEnabled()
     */
    public AutoAdjustOptions enableDeskew(boolean deskew)
    {
        this.deskew = deskew;
        return this;
    }

    /**
     * @param lengthDetection Turn on/off automatic length detection of the document
     * @return The current object
     * @see #isLengthDetectionEnabled()
     */
    public AutoAdjustOptions enableLengthDetection(boolean lengthDetection)
    {
        this.lengthDetection = lengthDetection;
        return this;
    }

    /**
     * @param rotate Turn on/off automatic rotation of the document
     * @return The current object
     * @see #isRotateEnabled()
     */
    public AutoAdjustOptions enableRotate(boolean rotate)
    {
        this.rotate = rotate;
        return this;
    }

    /**
     * @param autoSize Automatically size the output image to a size supported by the device.  Value can be one of the following:
     * <ul style=�list-style-type:disc�>
     * <li>ICAP_AUTOSIZE.TWAS_NONE (turn off auto size)</li>
     * <li>ICAP_AUTOSIZE.TWAS_AUTO (automatically size image to a supported size)</li>
     * <li>ICAP_AUTOSIZE.TWAS_CURRENT (automatically size image to the current size setting of the device)</li></ul>
     * @return The current object
     * @see #getAutoSize()
     */
    public AutoAdjustOptions setAutoSize(TwainConstants.ICAP_AUTOSIZE autoSize)
    {
        this.autoSize = autoSize;
        return this;
    }

    /**
     * @param flipRotation Orient images that flip orientation every other image.  Value can be one of the following:
     * <ul style=�list-style-type:disc�>
     * <li>ICAP_FLIPROTATION.TWFR_BOOK</li>
     * <li>ICAP_FLIPROTATION.TWFR_FANFOLD</li></ul>
     * @return The current object
     * @see #getFlipRotation()
     */
    public AutoAdjustOptions setFlipRotation(TwainConstants.ICAP_FLIPROTATION flipRotation)
    {
        this.flipRotation = flipRotation;
        return this;
    }

    /**
     * @param imageMerge Merges the front and rear image of a document in one of four orientations.  Value can be one of the following:
     * <ul style=�list-style-type:disc�>
     * <li>ICAP_IMAGEMERGE.TWIM_NONE</li>
     * <li>ICAP_IMAGEMERGE.TWIM_FRONTONTOP</li>
     * <li>ICAP_IMAGEMERGE.TWIM_FRONTONBOTTOM</li>
     * <li>ICAP_IMAGEMERGE.TWIM_FRONTONLEFT</li>
     * <li>ICAP_IMAGEMERGE.TWIM_FRONTONRIGHT</li></ul>
     * @return The current object
     * @see #getImageMerge()
     */
    public AutoAdjustOptions setImageMerge(TwainConstants.ICAP_IMAGEMERGE imageMerge)
    {
        this.imageMerge = imageMerge;
        return this;
    }

    /**
     * @param mergeHeightThreshold A value between 0.0 and the physical height, in ICAP_UNITS units.
     * <p>Front and rear images less than or equal to this value are merged according to the settings of getImageMerge().<br>
     * If either the front or the rear image is greater than this value, they are not merged.
     * @return The current object
     * @see #getMergeHeightThreshold()
     * @see com.dynarithmic.twain.highlevel.capabilityinterface.CapabilityInterface#getPhysicalHeight(com.dynarithmic.twain.highlevel.capabilityinterface.CapabilityInterface.GetCapOperation) getPhysicalHeight()
     * @see com.dynarithmic.twain.highlevel.capabilityinterface.CapabilityInterface#getUnits(com.dynarithmic.twain.highlevel.capabilityinterface.CapabilityInterface.GetCapOperation) getUnits()
     */
    public AutoAdjustOptions setMergeHeightThreshold(double mergeHeightThreshold)
    {
        this.mergeHeightThreshold = mergeHeightThreshold;
        return this;
    }

    /**
     * @return true if the automatic sense medium setting is set, otherwise false
     * @see #enableSenseMedium(boolean)
     */
    public boolean isSenseMediumEnabled()
    {
        return this.senseMedium;
    }

    /**
     * @return The discard blank page setting.
     * @see #setDiscardBlankPages(int)
     */
    public int getDiscardBlankPages()
    {
        return this.discardBlankPages;
    }

    /**
     * @return true if the border detection is enabled, otherwise false
     * @see #enableBorderDetection(boolean)
     */
    public boolean isBorderDetectionEnabled()
    {
        return this.borderDetection;
    }

    /**
     * @return true if the color-enabled setting is enabled, otherwise false
     * @see #enableColor(boolean)
     */
    public boolean isColorEnabled()
    {
        return this.colorEnabled;
    }

    /**
     * @return The color-noncolor pixel type.
     * @see #setColorNonColorPixelType(TwainConstants.ICAP_PIXELTYPE)
     */
    public TwainConstants.ICAP_PIXELTYPE getColorNonColorPixelType()
    {
        return this.colorNonColorPixelType;
    }

    /**
     * @return true if the auto-deskew options is enabled, otherwise false
     * @see #enableDeskew(boolean)
     */
    public boolean isDeskewEnabled()
    {
        return this.deskew;
    }

    /**
     * @return true if the auto length detection is enabled, otherwise false
     * @see #enableLengthDetection(boolean)
     */
    public boolean isLengthDetectionEnabled()
    {
        return this.lengthDetection;
    }

    /**
     * @return true if auto rotate is enabled, otherwise false.
     * @see #enableRotate(boolean)
     */
    public boolean isRotateEnabled()
    {
        return this.rotate;
    }

    /**
     * @return the auto-size setting
     * @see #setAutoSize(TwainConstants.ICAP_AUTOSIZE)
     */
    public ICAP_AUTOSIZE getAutoSize()
    {
        return this.autoSize;
    }

    /**
     * @return the flip rotation setting
     * @see #setFlipRotation(TwainConstants.ICAP_FLIPROTATION)
     */
    public TwainConstants.ICAP_FLIPROTATION getFlipRotation()
    {
        return this.flipRotation;
    }

    /**
     * @return the image merge setting
     * @see #setImageMerge(TwainConstants.ICAP_IMAGEMERGE)
     */
    public TwainConstants.ICAP_IMAGEMERGE getImageMerge()
    {
        return this.imageMerge;
    }

    /**
     * @return the merge height threshold setting
     * @see #setMergeHeightThreshold(double)
     */
    public double getMergeHeightThreshold()
    {
        return this.mergeHeightThreshold;
    }

    static protected final int affectedCaps[] = { CAPS.CAP_AUTOMATICSENSEMEDIUM,
                                                  CAPS.ICAP_AUTODISCARDBLANKPAGES,
                                                  CAPS.ICAP_AUTOMATICBORDERDETECTION,
                                                  CAPS.ICAP_AUTOMATICCOLORNONCOLORPIXELTYPE,
                                                  CAPS.ICAP_AUTOMATICCOLORENABLED,
                                                  CAPS.ICAP_AUTOMATICDESKEW,
                                                  CAPS.ICAP_AUTOMATICLENGTHDETECTION,
                                                  CAPS.ICAP_AUTOMATICROTATE,
                                                  CAPS.ICAP_AUTOSIZE,
                                                  CAPS.ICAP_FLIPROTATION,
                                                  CAPS.ICAP_IMAGEMERGE,
                                                  CAPS.ICAP_IMAGEMERGEHEIGHTTHRESHOLD};


    /**
     * @return an array of the TWAIN capabilities that the AutoAdjustOptions will affect<br>
     *         when TwainSource.acquire() is called.
     */
    static public final int [] getAffectedCaps()
    {
        return affectedCaps;
    }
}
