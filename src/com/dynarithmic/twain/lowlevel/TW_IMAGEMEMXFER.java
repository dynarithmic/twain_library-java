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

public class TW_IMAGEMEMXFER extends TwainLowLevel
{
    private TW_UINT16 Compression = new TW_UINT16();
    private TW_UINT32 BytesPerRow = new TW_UINT32();
    private TW_UINT32 Columns = new TW_UINT32();
    private TW_UINT32 Rows = new TW_UINT32();
    private TW_UINT32 XOffset = new TW_UINT32();
    private TW_UINT32 YOffset = new TW_UINT32();
    private TW_UINT32 BytesWritten = new TW_UINT32();
    private TW_MEMORY Memory = new TW_MEMORY();

    public TW_IMAGEMEMXFER()
    {
    }

    public TW_UINT16 getCompression()
    {
        return Compression;
    }

    public TW_UINT32 getBytesPerRow()
    {
        return BytesPerRow;
    }

    public TW_UINT32 getColumns()
    {
        return Columns;
    }

    public TW_UINT32 getRows()
    {
        return Rows;
    }

    public TW_UINT32 getXOffset()
    {
        return XOffset;
    }

    public TW_UINT32 getYOffset()
    {
        return YOffset;
    }

    public TW_UINT32 getBytesWritten()
    {
        return BytesWritten;
    }

    public TW_MEMORY getMemory()
    {
        return Memory;
    }

    public TW_IMAGEMEMXFER setCompression(TW_UINT16 compression)
    {
        Compression = compression;
        return this;
    }

    public TW_IMAGEMEMXFER setBytesPerRow(TW_UINT32 bytesPerRow)
    {
        BytesPerRow = bytesPerRow;
        return this;
    }

    public TW_IMAGEMEMXFER setColumns(TW_UINT32 columns)
    {
        Columns = columns;
        return this;
    }

    public TW_IMAGEMEMXFER setRows(TW_UINT32 rows)
    {
        Rows = rows;
        return this;
    }

    public TW_IMAGEMEMXFER setXOffset(TW_UINT32 xOffset)
    {
        XOffset = xOffset;
        return this;
    }

    public TW_IMAGEMEMXFER setYOffset(TW_UINT32 yOffset)
    {
        YOffset = yOffset;
        return this;
    }

    public TW_IMAGEMEMXFER setBytesWritten(TW_UINT32 bytesWritten)
    {
        BytesWritten = bytesWritten;
        return this;
    }

    public TW_IMAGEMEMXFER setMemory(TW_MEMORY memory)
    {
        Memory = memory;
        return this;
    }

    public TW_IMAGEMEMXFER setCompression(int compression)
    {
        Compression.setValue(compression);
        return this;
    }

    public TW_IMAGEMEMXFER setBytesPerRow(long bytesPerRow)
    {
        BytesPerRow.setValue(bytesPerRow);
        return this;
    }

    public TW_IMAGEMEMXFER setColumns(long columns)
    {
        Columns.setValue(columns);
        return this;
    }

    public TW_IMAGEMEMXFER setRows(long rows)
    {
        Rows.setValue(rows);
        return this;
    }

    public TW_IMAGEMEMXFER setXOffset(long xOffset)
    {
        XOffset.setValue(xOffset);
        return this;
    }

    public TW_IMAGEMEMXFER setYOffset(long yOffset)
    {
        YOffset.setValue(yOffset);
        return this;
    }

    public TW_IMAGEMEMXFER setBytesWritten(long bytesWritten)
    {
        BytesWritten.setValue(bytesWritten);
        return this;
    }
}
