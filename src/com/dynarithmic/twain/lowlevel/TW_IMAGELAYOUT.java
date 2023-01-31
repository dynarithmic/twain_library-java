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
