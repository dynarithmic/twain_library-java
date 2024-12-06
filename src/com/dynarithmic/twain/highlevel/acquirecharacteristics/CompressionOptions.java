/*
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2024 Dynarithmic Software.

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
import com.dynarithmic.twain.lowlevel.TwainConstants.*;

public class CompressionOptions
{
    private ICAP_BITORDER bitOrderValue = defaultBitOrderValue;
    private int ccittkFactor = defaultCCITTKFactor;
    private ICAP_COMPRESSION compression = defaultCompression;
    private ICAP_JPEGPIXELTYPE jpegPixelType = defaultJPEGPixelType;
    private int jpegQuality = defaultJPEGQuality;
    private ICAP_JPEGSUBSAMPLING jpegSubSampling = defaultJPEGSubSampling;
    private ICAP_PIXELFLAVOR pixelFlavorCodes = defaultPixelFlavorCodes;
    private int timeFill = defaultTimeFill;

    public static final ICAP_BITORDER defaultBitOrderValue = ICAP_BITORDER.TWBO_LSBFIRST;
    public static final int defaultCCITTKFactor = 4;
    public static final ICAP_COMPRESSION defaultCompression = ICAP_COMPRESSION.TWCP_NONE;
    public static final ICAP_JPEGPIXELTYPE defaultJPEGPixelType = ICAP_JPEGPIXELTYPE.TWPT_DEFAULT;
    public static final int defaultJPEGQuality = Integer.MAX_VALUE;
    public static final ICAP_JPEGSUBSAMPLING defaultJPEGSubSampling = ICAP_JPEGSUBSAMPLING.TWJS_DEFAULT;
    public static final int defaultTimeFill = Integer.MAX_VALUE;
    public static final ICAP_PIXELFLAVOR defaultPixelFlavorCodes = ICAP_PIXELFLAVOR.TWPF_DEFAULT;

    public ICAP_BITORDER getBitOrderValue()
    {
        return bitOrderValue;
    }

    public CompressionOptions setBitOrderValue(ICAP_BITORDER bitOrderValue)
    {
        this.bitOrderValue = bitOrderValue;
        return this;
    }

    public int getCCITTKFactor()
    {
        return ccittkFactor;
    }

    public CompressionOptions setCCITTKFactor(int ccittkFactor)
    {
        this.ccittkFactor = ccittkFactor;
        return this;
    }

    public ICAP_COMPRESSION getCompression()
    {
        return compression;
    }

    public CompressionOptions setCompression(ICAP_COMPRESSION compression)
    {
        this.compression = compression;
        return this;
    }

    public ICAP_JPEGPIXELTYPE getJpegPixelType()
    {
        return jpegPixelType;
    }

    public CompressionOptions setJpegPixelType(ICAP_JPEGPIXELTYPE jpegPixelType)
    {
        this.jpegPixelType = jpegPixelType;
        return this;
    }

    public int getJpegQuality()
    {
        return jpegQuality;
    }

    public CompressionOptions setJpegQuality(int jpegQuality)
    {
        this.jpegQuality = jpegQuality;
        return this;
    }

    public ICAP_JPEGSUBSAMPLING getJpegSubSampling()
    {
        return jpegSubSampling;
    }

    public CompressionOptions setJpegSubSampling(ICAP_JPEGSUBSAMPLING jpegSubSampling)
    {
        this.jpegSubSampling = jpegSubSampling;
        return this;
    }

    public ICAP_PIXELFLAVOR getPixelFlavorCodes()
    {
        return pixelFlavorCodes;
    }

    public CompressionOptions setPixelFlavorCodes(ICAP_PIXELFLAVOR pixelFlavor)
    {
        this.pixelFlavorCodes = pixelFlavor;
        return this;
    }

    public int getTimeFill()
    {
        return timeFill;
    }

    public CompressionOptions setTimeFill(int timeFill)
    {
        this.timeFill = timeFill;
        return this;
    }


    static protected final int[] affectedCaps = {
            CAPS.ICAP_BITORDERCODES,
            CAPS.ICAP_CCITTKFACTOR,
            CAPS.ICAP_COMPRESSION,
            CAPS.ICAP_JPEGPIXELTYPE,
            CAPS.ICAP_JPEGQUALITY,
            CAPS.ICAP_JPEGSUBSAMPLING,
            CAPS.ICAP_PIXELFLAVORCODES,
            CAPS.ICAP_TIMEFILL};

    static public final int [] getAffectedCaps()
    {
        return affectedCaps;
    }
}
