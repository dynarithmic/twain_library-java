package com.dynarithmic.twain.highlevel;

import com.dynarithmic.twain.DTwainConstants;
import com.dynarithmic.twain.DTwainConstants.DSFileType;

public class BufferedStripInfo {
    private int preferredSize;
    private int minimumSize;
    private int maximumSize;
    private int bufferSize;
    private DSFileType compressionType;
    private long bufferHandle;
    private byte[] bufferedStripData;
    private boolean appAllocatesBuffer;

    private int columnsinBuffer;
    private int rowsInBuffer;
    private int xOffsetInImage;
    private int yOffsetInImage;
    private int bytesWritten;
    private int bytesPerRow;

    private TwainImageInfo imageInfo;

    public int getPreferredSize() {
        return preferredSize;
    }
    public void setPreferredSize(int preferredSize) {
        this.preferredSize = preferredSize;
    }
    public int getMinimumSize() {
        return minimumSize;
    }
    public void setMinimumSize(int minimumSize) {
        this.minimumSize = minimumSize;
    }
    public int getMaximumSize() {
        return maximumSize;
    }
    public void setMaximumSize(int maximumSize) {
        this.maximumSize = maximumSize;
    }

    public BufferedStripInfo()
    { preferredSize = minimumSize = maximumSize = -1; bufferHandle = 0; bufferSize = 0;
      compressionType = DTwainConstants.DSFileType.BMP; this.appAllocatesBuffer = false;
      bufferedStripData = new byte[0];}

    public BufferedStripInfo(int pref, int mini, int maxi)
    {
        preferredSize = pref;
        minimumSize = mini;
        maximumSize = maxi;
        bufferSize = 0;
        bufferHandle = 0;
        bufferedStripData = new byte[0];
        compressionType = DTwainConstants.DSFileType.BMP;
    }
    public BufferedStripInfo setAppAllocatesBuffer(boolean appAllocatesBuffer)
    {
        this.appAllocatesBuffer = appAllocatesBuffer;
        return this;
    }

    public boolean isAppAllocatesBuffer()
    {
        return this.appAllocatesBuffer;
    }

    public int getBufferSize() {
        return bufferSize;
    }
    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }
    public long getBufferHandle() {
        return bufferHandle;
    }
    public void setBufferHandle(long bufferHandle) {
        this.bufferHandle = bufferHandle;
    }
    public DTwainConstants.DSFileType getCompressionType() {
        return compressionType;
    }
    private int getCompressionTypeI() {
        return compressionType.value();
    }

    public void setCompressionType(DTwainConstants.DSFileType compressionType) {
        this.compressionType = compressionType;
    }

    private void setCompressionType(int compressionTypeI)   {
        this.compressionType = compressionType.values()[compressionTypeI];
    }

    public byte[] getBufferedStripData() {
        return bufferedStripData;
    }
    public void setBufferedStripData(byte[] bufferedStripData) {
        this.bufferedStripData = bufferedStripData;
    }
    public TwainImageInfo getImageInfo() {
        return imageInfo;
    }
    public void setImageInfo(TwainImageInfo imageInfo) {
        this.imageInfo = imageInfo;
    }
    public int getColumnsInBuffer() {
        return columnsinBuffer;
    }
    public void setColumnsInBuffer(int columnsinBuffer) {
        this.columnsinBuffer = columnsinBuffer;
    }
    public int getRowsInBuffer() {
        return rowsInBuffer;
    }
    public void setRowsInBuffer(int rowsInBuffer) {
        this.rowsInBuffer = rowsInBuffer;
    }
    public int getxOffsetInImage() {
        return xOffsetInImage;
    }
    public void setxOffsetInImage(int xOffsetInImage) {
        this.xOffsetInImage = xOffsetInImage;
    }
    public int getyOffsetInImage() {
        return yOffsetInImage;
    }
    public void setyOffsetInImage(int yOffsetInImage) {
        this.yOffsetInImage = yOffsetInImage;
    }
    public int getBytesWritten() {
        return bytesWritten;
    }
    public void setBytesWritten(int bytesWritten) {
        this.bytesWritten = bytesWritten;
    }
    public int getBytesPerRow() {
        return bytesPerRow;
    }
    public void setBytesPerRow(int bytesPerRow) {
        this.bytesPerRow = bytesPerRow;
    }

}
