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
