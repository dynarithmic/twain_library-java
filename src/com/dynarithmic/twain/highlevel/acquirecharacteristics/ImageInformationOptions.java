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
