package com.dynarithmic.twain.lowlevel;

public class TW_PASSTHRU extends TwainLowLevel
{
    private TW_MEMREF  pCommand = new TW_MEMREF();
    private TW_UINT32  CommandBytes = new TW_UINT32();
    private TW_INT32   Direction = new TW_INT32();
    private TW_MEMREF  pData = new TW_MEMREF();
    private TW_UINT32  DataBytes = new TW_UINT32();
    private TW_UINT32  DataBytesXfered = new TW_UINT32();

    public TW_PASSTHRU() {}

    public TW_MEMREF getpCommand()
    {
        return pCommand;
    }

    public TW_UINT32 getCommandBytes()
    {
        return CommandBytes;
    }

    public TW_INT32 getDirection()
    {
        return Direction;
    }

    public TW_MEMREF getpData()
    {
        return pData;
    }

    public TW_UINT32 getDataBytes()
    {
        return DataBytes;
    }

    public TW_UINT32 getDataBytesXfered()
    {
        return DataBytesXfered;
    }

    public TW_PASSTHRU setpCommand(TW_MEMREF pCommand)
    {
        this.pCommand = pCommand;
        return this;
    }

    public TW_PASSTHRU setCommandBytes(TW_UINT32 commandBytes)
    {
        CommandBytes = commandBytes;
        return this;
    }

    public TW_PASSTHRU setDirection(TW_INT32 direction)
    {
        Direction = direction;
        return this;
    }

    public TW_PASSTHRU setpData(TW_MEMREF pData)
    {
        this.pData = pData;
        return this;
    }

    public TW_PASSTHRU setDataBytes(TW_UINT32 dataBytes)
    {
        DataBytes = dataBytes;
        return this;
    }

    public TW_PASSTHRU setDataBytesXfered(TW_UINT32 dataBytesXfered)
    {
        DataBytesXfered = dataBytesXfered;
        return this;
    }

    public TW_PASSTHRU setCommandBytes(long commandBytes)
    {
        CommandBytes.setValue(commandBytes);
        return this;
    }

    public TW_PASSTHRU setDirection(int direction)
    {
        Direction.setValue(direction);
        return this;
    }

    public TW_PASSTHRU setDataBytes(long dataBytes)
    {
        DataBytes.setValue(dataBytes);
        return this;
    }

    public TW_PASSTHRU setDataBytesXfered(long dataBytesXfered)
    {
        DataBytesXfered.setValue(dataBytesXfered);
        return this;
    }

}
