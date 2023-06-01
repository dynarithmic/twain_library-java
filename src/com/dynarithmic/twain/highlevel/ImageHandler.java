/*
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2023 Dynarithmic Software.

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

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;

import com.dynarithmic.twain.exceptions.DTwainRuntimeException;

public class ImageHandler
{
    TwainAcquisitionArray acquisitionArray = null;
    public ImageHandler()
    {}

    public ImageHandler(TwainAcquisitionArray acquisitionArray)
    {
        this.acquisitionArray = acquisitionArray;
    }

    public TwainAcquisitionArray getAcquisitionArray()
    {
        return this.acquisitionArray;
    }

    public int getNumAcquisitions()
    {
        if ( this.acquisitionArray != null)
            return this.acquisitionArray.getNumAcquisitions();
        return 0;
    }

    public int getNumImages(int acquisitionNumber)
    {
        int numAcquisitions = getNumAcquisitions();
        if ( numAcquisitions > 0 )
        {
            if ( acquisitionNumber >= 0 && acquisitionNumber < numAcquisitions)
                return this.acquisitionArray.get(acquisitionNumber).getNumPages();
        }
        return 0;
    }

    public byte [] getImageData(int acquisitionNumber, int page) throws DTwainRuntimeException
    {
        int numImages = getNumImages(acquisitionNumber);
        if ( numImages > 0 )
        {
            if ( page >= 0 && page < numImages )
                return this.acquisitionArray.get(acquisitionNumber).getImageData(page);
        }
        return new byte[0];
    }

    public BufferedImage getImage(int acquisitionNumber, int page) throws DTwainRuntimeException, IOException, ImageReadException
    {
        byte [] imageData = getImageData(acquisitionNumber, page);
        if ( imageData.length > 0 )
            return Imaging.getBufferedImage(imageData);
        return null;
    }

}
