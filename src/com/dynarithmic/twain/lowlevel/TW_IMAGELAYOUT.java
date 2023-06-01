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
package com.dynarithmic.twain.lowlevel;

public class TW_IMAGELAYOUT extends TwainLowLevel
{
    private TW_FRAME   Frame = new TW_FRAME();
    private TW_UINT32  DocumentNumber = new TW_UINT32();
    private TW_UINT32  PageNumber = new TW_UINT32();
    private TW_UINT32  FrameNumber = new TW_UINT32();

    public TW_IMAGELAYOUT() {}

    public TW_FRAME getFrame()
    {
        return Frame;
    }

    public TW_UINT32 getDocumentNumber()
    {
        return DocumentNumber;
    }

    public TW_UINT32 getPageNumber()
    {
        return PageNumber;
    }

    public TW_UINT32 getFrameNumber()
    {
        return FrameNumber;
    }

    public TW_IMAGELAYOUT setFrame(TW_FRAME frame)
    {
        Frame = frame;
        return this;
    }

    public TW_IMAGELAYOUT setDocumentNumber(TW_UINT32 documentNumber)
    {
        DocumentNumber = documentNumber;
        return this;
    }

    public TW_IMAGELAYOUT setPageNumber(TW_UINT32 pageNumber)
    {
        PageNumber = pageNumber;
        return this;
    }

    public TW_IMAGELAYOUT setFrameNumber(TW_UINT32 frameNumber)
    {
        FrameNumber = frameNumber;
        return this;
    }

    public TW_IMAGELAYOUT setDocumentNumber(long documentNumber)
    {
        DocumentNumber.setValue(documentNumber);
        return this;
    }

    public TW_IMAGELAYOUT setPageNumber(long pageNumber)
    {
        PageNumber.setValue(pageNumber);
        return this;
    }

    public TW_IMAGELAYOUT setFrameNumber(long frameNumber)
    {
        FrameNumber.setValue(frameNumber);
        return this;
    }

}
