package com.dynarithmic.twain.lowlevel;

public class TW_IMAGEINFO extends TwainLowLevel
{
    private TW_FIX32 XResolution = new TW_FIX32();
    private TW_FIX32 YResolution = new TW_FIX32();
    private TW_INT32 ImageWidth = new TW_INT32();
    private TW_INT32 ImageLength = new TW_INT32();
    private TW_INT16 SamplesPerPixel = new TW_INT16();
    private TW_INT16[] BitsPerSample = new TW_INT16[8];
    private TW_INT16 BitsPerPixel = new TW_INT16();
    private TW_INT16 Planar = new TW_INT16();
    private TW_INT16 PixelType = new TW_INT16();
    private TW_INT16 Compression = new TW_INT16();

    public TW_IMAGEINFO()
    {
        for (int i = 0; i < 8; ++i)
            BitsPerSample[i] = new TW_INT16();
    }

    public TW_FIX32 getXResolution()
    {
        return XResolution;
    }
    public TW_FIX32 getYResolution()
    {
        return YResolution;
    }
    public TW_INT32 getImageWidth()
    {
        return ImageWidth;
    }
    public TW_INT32 getImageLength()
    {
        return ImageLength;
    }
    public TW_INT16 getSamplesPerPixel()
    {
        return SamplesPerPixel;
    }
    public TW_INT16[] getBitsPerSample()
    {
        return BitsPerSample;
    }
    public TW_INT16 getBitsPerPixel()
    {
        return BitsPerPixel;
    }
    public TW_INT16 getPlanar()
    {
        return Planar;
    }
    public TW_INT16 getPixelType()
    {
        return PixelType;
    }
    public TW_INT16 getCompression()
    {
        return Compression;
    }
    public TW_INT16 getBitsPerSampleValue(int i)
    {
        if ( i > 7 || i < 0)
            throw new IllegalArgumentException();
        return BitsPerSample[i];
    }

    public TW_IMAGEINFO setXResolution(TW_FIX32 xResolution)
    {
        XResolution = xResolution;
        return this;
    }
    public TW_IMAGEINFO setYResolution(TW_FIX32 yResolution)
    {
        YResolution = yResolution;
        return this;
    }
    public TW_IMAGEINFO setImageWidth(TW_INT32 imageWidth)
    {
        ImageWidth = imageWidth;
        return this;
    }
    public TW_IMAGEINFO setImageLength(TW_INT32 imageLength)
    {
        ImageLength = imageLength;
        return this;
    }
    public TW_IMAGEINFO setSamplesPerPixel(TW_INT16 samplesPerPixel)
    {
        SamplesPerPixel = samplesPerPixel;
        return this;
    }
    public TW_IMAGEINFO setBitsPerSample(TW_INT16[] bitsPerSample)
    {
        if ( bitsPerSample.length != 8 )
            throw new IllegalArgumentException();
        BitsPerSample = bitsPerSample;
        return this;
    }

    public TW_IMAGEINFO setBitsPerPixel(TW_INT16 bitsPerPixel)
    {
        BitsPerPixel = bitsPerPixel;
        return this;
    }
    public TW_IMAGEINFO setPlanar(TW_INT16 planar)
    {
        Planar = planar;
        return this;
    }
    public TW_IMAGEINFO setPixelType(TW_INT16 pixelType)
    {
        PixelType = pixelType;
        return this;
    }
    public TW_IMAGEINFO setCompression(TW_INT16 compression)
    {
        Compression = compression;
        return this;
    }

    public TW_IMAGEINFO setXResolution(double xResolution)
    {
        XResolution.setValue(xResolution);
        return this;
    }

    public TW_IMAGEINFO setYResolution(double yResolution)
    {
        YResolution.setValue(yResolution);
        return this;
    }

    public TW_IMAGEINFO setImageWidth(int imageWidth)
    {
        ImageWidth.setValue(imageWidth);
        return this;
    }

    public TW_IMAGEINFO setImageLength(int imageLength)
    {
        ImageLength.setValue(imageLength);
        return this;
    }

    public TW_IMAGEINFO setSamplesPerPixel(short samplesPerPixel)
    {
        SamplesPerPixel.setValue(samplesPerPixel);
        return this;
    }

    public TW_IMAGEINFO setBitsPerSample(short[] bitsPerSample)
    {
        if ( bitsPerSample.length != 8 )
            throw new IllegalArgumentException();
        for (int i = 0; i < 8; ++i)
            BitsPerSample[i].setValue(bitsPerSample[i]);
        return this;
    }

    public TW_IMAGEINFO setBitsPerSampleValue(short bitSample, int nWhich)
    {
        if ( nWhich > 7)
            throw new IllegalArgumentException();
        BitsPerSample[nWhich].setValue(bitSample);
        return this;
    }

    public TW_IMAGEINFO setBitsPerPixel(short bitsPerPixel)
    {
        BitsPerPixel.setValue(bitsPerPixel);
        return this;
    }

    public TW_IMAGEINFO setPlanar(short planar)
    {
        Planar.setValue(planar);
        return this;
    }

    public TW_IMAGEINFO setPixelType(short pixelType)
    {
        PixelType.setValue(pixelType);
        return this;
    }

    public TW_IMAGEINFO setCompression(short compression)
    {
        Compression.setValue(compression);
        return this;
    }
    public TW_IMAGEINFO setBitsPerSampleValue(TW_INT16 val, int i)
    {
        if ( i < 0 || i >= 8 )
            throw new IllegalArgumentException();
        BitsPerSample[i] = val;
        return this;
    }

}
