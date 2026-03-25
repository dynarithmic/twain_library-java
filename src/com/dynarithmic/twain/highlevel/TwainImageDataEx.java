/*
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2026 Dynarithmic Software.

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
package com.dynarithmic.twain.highlevel;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.*;

public class TwainImageDataEx
{
    public class BitmapHeader
    {
        int headerSize;
        int width;
        int height;
        short planes;
        short bpp;
        int compression;
        int colorsUsed;
        
        void setHeader(ByteBuffer buf)
        {
            buf.order(ByteOrder.LITTLE_ENDIAN);
            headerSize = buf.getInt(0);
            width = buf.getInt(4);
            height = buf.getInt(8);
            planes = buf.getShort(12);
            bpp = buf.getShort(14);
            compression = buf.getInt(16);
            colorsUsed = buf.getInt(32);
        }

        public int getHeaderSize() { return headerSize; }
        public int getWidth() { return width; }
        public int getHeight() { return height; }
        public short getPlanes() { return planes; }
        public short getBitsPerPixel() { return bpp; }
        public int getCompression() { return compression; }
        public int getColorsUsed() { return colorsUsed; }
    }

    private class DataBufferByteBuffer extends DataBuffer
    {
        private final ByteBuffer buffer;

        public DataBufferByteBuffer(ByteBuffer buffer, int size)
        {
            super(DataBuffer.TYPE_BYTE, size);
            this.buffer = buffer;
        }

        @Override
        public int getElem(int bank, int i)
        {
            return buffer.get(i) & 0xFF;
        }

        @Override
        public void setElem(int bank, int i, int val)
        {
            buffer.put(i, (byte)val);
        }
    }
    
    private ByteBuffer dibData = null;
    private BitmapHeader bitmapHeader = null;
    private byte [] imagePixelBuffer;
    
    public TwainImageDataEx()
    {}

    private void setImageData(ByteBuffer dibData)
    {
        this.dibData = dibData;
        bitmapHeader = new BitmapHeader();
        bitmapHeader.setHeader(dibData);
    }

    public ByteBuffer getImageData()
    {
        return this.dibData;
    }
    
    public byte [] getImageDataAsPixels()
    {
        imagePixelBuffer = new byte[dibData.remaining()];
        dibData.order(ByteOrder.LITTLE_ENDIAN);
        dibData.get(imagePixelBuffer);
        return imagePixelBuffer;
    }
    
    public BitmapHeader getHeader() { return bitmapHeader; }
    
    public BufferedImage BufferedImage()
    {
        dibData.order(ByteOrder.LITTLE_ENDIAN);

        int headerSize = bitmapHeader.getHeaderSize();
        int width = bitmapHeader.getWidth();
        int height = bitmapHeader.getHeight();
        short planes = bitmapHeader.getPlanes();
        short bpp = bitmapHeader.getBitsPerPixel();
        int compression = bitmapHeader.getCompression();
        int clrUsed = bitmapHeader.getColorsUsed();

        boolean bottomUp = height > 0;
        height = Math.abs(height);

        int stride = ((width * bpp + 31) / 32) * 4;

        int paletteEntries = 0;
        if (bpp <= 8)
            paletteEntries = (clrUsed != 0) ? clrUsed : (1 << bpp);

        int paletteOffset = headerSize;
        int pixelOffset = headerSize + paletteEntries * 4;

        ByteBuffer pixelBuf = dibData.duplicate();
        pixelBuf.position(pixelOffset);
        pixelBuf = pixelBuf.slice();

        DataBuffer db = new DataBufferByteBuffer(pixelBuf, stride * height);

        SampleModel sm;
        ColorModel cm;

        switch (bpp)
        {
            case 1:
            {
                sm = new MultiPixelPackedSampleModel(
                        DataBuffer.TYPE_BYTE,
                        width,
                        height,
                        1,
                        stride,
                        0);

                byte[] r = new byte[paletteEntries];
                byte[] g = new byte[paletteEntries];
                byte[] b = new byte[paletteEntries];

                for (int i = 0; i < paletteEntries; i++)
                {
                    int base = paletteOffset + i * 4;
                    b[i] = dibData.get(base);
                    g[i] = dibData.get(base + 1);
                    r[i] = dibData.get(base + 2);
                }

                cm = new IndexColorModel(1, paletteEntries, r, g, b);
                break;
            }

            case 8:
            {
                sm = new PixelInterleavedSampleModel(
                        DataBuffer.TYPE_BYTE,
                        width,
                        height,
                        1,
                        stride,
                        new int[]{0});

                byte[] r = new byte[paletteEntries];
                byte[] g = new byte[paletteEntries];
                byte[] b = new byte[paletteEntries];

                for (int i = 0; i < paletteEntries; i++)
                {
                    int base = paletteOffset + i * 4;
                    b[i] = dibData.get(base);
                    g[i] = dibData.get(base + 1);
                    r[i] = dibData.get(base + 2);
                }

                cm = new IndexColorModel(8, paletteEntries, r, g, b);
                break;
            }

            case 24:
            {
                sm = new PixelInterleavedSampleModel(
                        DataBuffer.TYPE_BYTE,
                        width,
                        height,
                        3,
                        stride,
                        new int[]{2,1,0}); // BGR → RGB

                cm = new ComponentColorModel(
                        ColorSpace.getInstance(ColorSpace.CS_sRGB),
                        false,
                        false,
                        Transparency.OPAQUE,
                        DataBuffer.TYPE_BYTE);
                break;
            }

            case 32:
            {
                sm = new PixelInterleavedSampleModel(
                        DataBuffer.TYPE_BYTE,
                        width,
                        height,
                        4,
                        stride,
                        new int[]{2,1,0,3}); // BGRA → RGBA

                cm = new ComponentColorModel(
                        ColorSpace.getInstance(ColorSpace.CS_sRGB),
                        true,
                        false,
                        Transparency.TRANSLUCENT,
                        DataBuffer.TYPE_BYTE);
                break;
            }

            default:
                throw new IllegalArgumentException("Unsupported bit depth: " + bpp);
        }

        WritableRaster raster = Raster.createWritableRaster(sm, db, null);

        BufferedImage img = new BufferedImage(cm, raster, false, null);

        if (bottomUp)
            img = flipVertical(img);

        return img;
    }
    
    private BufferedImage flipVertical(BufferedImage img)
    {
        int w = img.getWidth();
        int h = img.getHeight();

        BufferedImage flipped =
            new BufferedImage(w, h, img.getType());

        for (int y = 0; y < h; y++)
            flipped.getRaster().setRect(
                0, y,
                img.getRaster().createChild(
                    0, h - y - 1, w, 1, 0, 0, null));

        return flipped;
    }
}