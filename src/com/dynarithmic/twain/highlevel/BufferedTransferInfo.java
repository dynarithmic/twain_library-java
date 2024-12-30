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
package com.dynarithmic.twain.highlevel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.dynarithmic.twain.DTwainConstants.CompressionType;
import com.dynarithmic.twain.DTwainConstants.ErrorCode;
import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;
import com.dynarithmic.twain.exceptions.DTwainRuntimeException;
import com.dynarithmic.twain.highlevel.capabilityinterface.CapabilityInterface;
import com.dynarithmic.twain.highlevel.capabilityinterface.CapabilityInterface.GetCapOperation;
import com.dynarithmic.twain.DTwainJavaAPI;
import com.dynarithmic.twain.DTwainConstants;
import com.dynarithmic.twain.lowlevel.TwainConstants.ICAP_COMPRESSION;

public class BufferedTransferInfo
{
    // There are two types of buffered transfer:
    // 1) Strips.  Either the user has a buffer, or let DTWAIN set up the buffer
    // 2) Tiles.   The user needs no setup, only to set up the TwainCallback to handle
    //    the tiles.
    private byte [] compressedStrip = null;
    private TwainSource mSource = null;
    private List<Integer> supportedCompressionTypes = new ArrayList<>();
    private CompressionType compressionType = DTwainConstants.CompressionType.NONE;
    private BufferedStripInfo m_StripInfo = new BufferedStripInfo();
    private boolean handleStrips = false;
    private boolean tileModeSupported = false;
    private boolean handleTiles = false;
    private class BestCompression
    {
        private CompressionType compressionType;
        public BestCompression(CompressionType ct)
        {
            this.compressionType = ct;
        }
    }

    private Map<DTwainConstants.DSFileType, List<BestCompression>> bestCompressionMap =
            new EnumMap<> (DTwainConstants.DSFileType.class);

    public BufferedTransferInfo()
    {
        List<BestCompression> arrayList =
                Arrays.asList(new BestCompression(DTwainConstants.CompressionType.LZW),
                              new BestCompression(DTwainConstants.CompressionType.PACKBITS),
                              new BestCompression(DTwainConstants.CompressionType.ZIP),
                              new BestCompression(DTwainConstants.CompressionType.JBIG),
                              new BestCompression(DTwainConstants.CompressionType.JPEG),
                              new BestCompression(DTwainConstants.CompressionType.GROUP4),
                              new BestCompression(DTwainConstants.CompressionType.GROUP32D),
                              new BestCompression(DTwainConstants.CompressionType.GROUP31D),
                              new BestCompression(DTwainConstants.CompressionType.GROUP31DEOL));
        bestCompressionMap.put(DTwainConstants.DSFileType.TIFF, arrayList);
        bestCompressionMap.put(DTwainConstants.DSFileType.TIFFMULTI, arrayList);

        List<BestCompression> arrayList2 = Arrays.asList(new BestCompression(DTwainConstants.CompressionType.PACKBITS));
        bestCompressionMap.put(DTwainConstants.DSFileType.PICT, arrayList2);

        List<BestCompression> arrayList3 = Arrays.asList(new BestCompression(DTwainConstants.CompressionType.RLE8),
                                                         new BestCompression(DTwainConstants.CompressionType.RLE4),
                                                         new BestCompression(DTwainConstants.CompressionType.BITFIELDS));
        bestCompressionMap.put(DTwainConstants.DSFileType.BMP, arrayList3);

    }

    public BufferedTransferInfo(TwainSource theSource) throws DTwainJavaAPIException, DTwainRuntimeException
    {
        this();
        attach(theSource);
    }
    
    public boolean isTileModeSupported()
    {
        return tileModeSupported;
    }

    private DTwainConstants.CompressionType getCompressionTypeFromMap(DTwainConstants.DSFileType ft)
    {
        List<BestCompression> best = bestCompressionMap.get(ft);
        for (BestCompression bt : best)
        {
            if (isCompressionTypeSupported(bt.compressionType))
                return bt.compressionType;
        }
        return DTwainConstants.CompressionType.NONE;
    }

    public BufferedStripInfo getStripInfo()
    {
        return this.m_StripInfo;
    }

    public boolean isHandleStrips()
    {
        return this.handleStrips;
    }

    public BufferedTransferInfo setHandleStrips(boolean handleStrips)
    {
        this.handleStrips = handleStrips;
        return this;
    }
    
    public BufferedTransferInfo setHandleTiles(boolean handleTiles)
    {
        if ( tileModeSupported )
            this.handleTiles = handleTiles;
        return this;
    }
    
    public boolean isUsingStripTransfer() { return !this.handleTiles; }
    public boolean isUsingTileTransfer() { return this.handleTiles; }
    
    public BufferedTransferInfo attach(TwainSource theSource) throws DTwainJavaAPIException
    {
        mSource = theSource;
        CapabilityInterface ci = mSource.getCapabilityInterface();
        GetCapOperation gc = ci.get();
        supportedCompressionTypes = ci.getCompression(gc);
        tileModeSupported = ci.isTilesSupported();
        Collections.sort(supportedCompressionTypes);
        try
        {
            setCompressionType(compressionType);  // always set the default type
        }
        catch (DTwainJavaAPIException e)
        {
            throw e;
        }
        return this;
    }

    public boolean isCompressionTypeSupported(ICAP_COMPRESSION Compression)
    {
        return isCompressionTypeSupported(Compression.ordinal());
    }

    public boolean isCompressionTypeSupported(CompressionType Compression)
    {
        return isCompressionTypeSupported(Compression.ordinal());
    }

    public boolean isCompressionTypeSupported(int Compression)
    {
        for (int cmp : supportedCompressionTypes)
        {
            if (cmp == Compression)
                return true;
        }
        return false;
    }

    public boolean isTIFFCompressionSupported()
    {
        return isCompressionTypeSupported(CompressionType.PACKBITS) ||
               isCompressionTypeSupported(CompressionType.GROUP31D) ||
               isCompressionTypeSupported(CompressionType.GROUP31DEOL) ||
               isCompressionTypeSupported(CompressionType.GROUP32D) ||
               isCompressionTypeSupported(CompressionType.GROUP4) ||
               isCompressionTypeSupported(CompressionType.JPEG) ||
               isCompressionTypeSupported(CompressionType.LZW) ||
               isCompressionTypeSupported(CompressionType.JBIG);
    }

    public boolean isJPEGCompressionSupported()
    {
        return isCompressionTypeSupported(CompressionType.JPEG);
    }

    public boolean isPNGCompressionSupported()
    {
        return isCompressionTypeSupported(CompressionType.PNG);
    }

    public boolean isBMPCompressionSupported()
    {
        return isCompressionTypeSupported(CompressionType.RLE4) ||
               isCompressionTypeSupported(CompressionType.RLE8) ||
               isCompressionTypeSupported(CompressionType.BITFIELDS);
    }

    public boolean isJBIGCompressionSupported()
    {
        return isCompressionTypeSupported(CompressionType.JBIG);
    }

    public boolean isLZWCompressionSupported()
    {
        return isCompressionTypeSupported(CompressionType.LZW);
    }

    public boolean isJP2CompressionSupported()
    {
        return isCompressionTypeSupported(CompressionType.JPEG2000);
    }

    public boolean isJPXCompressionSupported()
    {
        return isCompressionTypeSupported(CompressionType.JPEG2000);
    }

    public boolean isCompressionSupported()
    {
        int nCompressions = supportedCompressionTypes.size();
        if ( isCompressionTypeSupported(CompressionType.NONE) )
            return nCompressions != 1;
        return nCompressions > 1;
    }

    public BufferedTransferInfo setJPEGQuality(int quality) throws DTwainJavaAPIException
    {
        CapabilityInterface ci = mSource.getCapabilityInterface();
        ci.setJpegQuality(Arrays.asList(quality), ci.set());
        return this;
    }

    public CompressionType getBestCompressionType(DTwainConstants.DSFileType ft, boolean isMono)
    {
        return CompressionType.values()[getBestCompressionTypeImpl(ft, isMono).ordinal()];
    }

    private DTwainConstants.CompressionType getBestCompressionTypeImpl(DTwainConstants.DSFileType ft, boolean isMono)
    {
        // returns the best compression for a particular file type
        if (!isCompressionSupported())
            return DTwainConstants.CompressionType.NONE;
        switch (ft)
        {
            case TIFF:
            case TIFFMULTI:
            {
                if (!isTIFFCompressionSupported() )
                    return DTwainConstants.CompressionType.NONE;
                return this.getCompressionTypeFromMap(ft);
            }
            case PICT:
            {
                return this.getCompressionTypeFromMap(ft);
            }

            case BMP:
            {
                if (!isBMPCompressionSupported())
                    return DTwainConstants.CompressionType.NONE;
                return this.getCompressionTypeFromMap(ft);
            }

            case JFIF:
            {
                if (isJPEGCompressionSupported())
                    return DTwainConstants.CompressionType.JPEG;
            }
            break;

            case PNG:
            {
                if (isCompressionTypeSupported(DTwainConstants.CompressionType.PNG))
                    return DTwainConstants.CompressionType.PNG;
            }
            break;

            case SPIFF:
            {
                if (isJPEGCompressionSupported())
                    return DTwainConstants.CompressionType.JPEG;
                if (isJBIGCompressionSupported())
                    return DTwainConstants.CompressionType.JBIG;

                if (isCompressionTypeSupported(DTwainConstants.CompressionType.PNG))
                    return DTwainConstants.CompressionType.PNG;
            }
            break;

            case JP2:
            {
                if (isJP2CompressionSupported())
                    return DTwainConstants.CompressionType.JPEG2000;
            }
            break;

            case JPX:
            {
                if (isJPXCompressionSupported())
                    return DTwainConstants.CompressionType.JPEG2000;
            }
            break;

            default:
                break;
        }
        return DTwainConstants.CompressionType.NONE;
    }

    public BufferedTransferInfo setStripSize(int stripSize)
    {
        this.m_StripInfo.setBufferSize(stripSize);
        return this;
    }

    public int getStripSize()
    {
        return this.m_StripInfo.getBufferSize();
    }

    public boolean initTransfer(CompressionType compression) throws DTwainJavaAPIException
    {
        if (mSource == null)
            return false;
        DTwainJavaAPI handle = mSource.getTwainSession().getAPIHandle();

        // Test to see if the compression is supported
        CapabilityInterface ci = mSource.getCapabilityInterface();
        if (!ci.isCompressionValueSupported(compression.value()))
            return false;
        
        // If this is tiled, we need to call with a BufferedTileInfo to tell DTWAIN
        // to setup this transfer for tiles
        if ( this.handleTiles )
            handle.DTWAIN_SetBufferedTransferInfo(mSource.getSourceHandle(), new BufferedTileInfo());
        else
        {
            // This is a stripped transfer
            int bufferSize = m_StripInfo.getBufferSize();
            if ( bufferSize > 0)
            {
                if ( bufferSize < m_StripInfo.getMinimumSize() || bufferSize > this.m_StripInfo.getMaximumSize())
                    return false;
            }
            m_StripInfo.setAppAllocatesBuffer(handleStrips);
            handle.DTWAIN_SetBufferedTransferInfo(mSource.getSourceHandle(), m_StripInfo);
        }
        
        return true;
    }

    public BufferedTransferInfo setCompressionType(CompressionType compressionType2)
            throws DTwainJavaAPIException, DTwainRuntimeException
    {
        if ( !isCompressionTypeSupported(compressionType2))
            throw new DTwainRuntimeException(ErrorCode.ERROR_COMPRESSION);
        try
        {
            CapabilityInterface ci = mSource.getCapabilityInterface();
            ci.setCompression(Arrays.asList(compressionType2.ordinal()), ci.set());
            if ( ci.getLastCapError().getErrorCode() == 0 )
            {
                // set the max and minimum sizes
                DTwainJavaAPI theInterface = mSource.getTwainSession().getAPIHandle();
                m_StripInfo = theInterface.DTWAIN_CreateBufferedStripInfo(mSource.getSourceHandle());
                compressionType = compressionType2;
            }
            return this;
        }
        catch (DTwainJavaAPIException e)
        {
            throw e;
        }
    }

    public DTwainConstants.CompressionType getCompressionType()
    {
        return compressionType;
    }

    public int getMinimumBufferSize()
    {
        return this.m_StripInfo.getMinimumSize();
    }

    public int getMaximumBufferSize()
    {
        return this.m_StripInfo.getMaximumSize();
    }

    public int getPreferredSize()
    {
        return this.m_StripInfo.getPreferredSize();
    }

    public BufferedTransferInfo setBufferSize(int size)
    {
        this.m_StripInfo.setBufferSize(size);
        return this;
    }

    public int getBufferSize()
    {
        return compressedStrip.length;

    }
    public byte [] getStrip()
    {
        return this.m_StripInfo.getBufferedStripData();
    }
}