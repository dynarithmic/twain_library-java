package com.dynarithmic.twain.highlevel;

import com.dynarithmic.twain.lowlevel.TW_IMAGEMEMXFER;
import com.dynarithmic.twain.lowlevel.TW_UINT32;

public class BufferedTileInfo {
    private TW_IMAGEMEMXFER tileInfo = new TW_IMAGEMEMXFER();
    private byte [] tileData = null;
    private void setTileData(byte [] theData) { tileData = theData; }
    private void setInfo(TW_IMAGEMEMXFER xFer) 
    { 
        tileInfo = xFer; 
    }
    
    public BufferedTileInfo() {}
    public TW_IMAGEMEMXFER getInfo() { return tileInfo; }
    public byte [] getTileData() { return tileData; }
    public long getTileDataSize() { return tileInfo.getMemory().getLength().getValue(); }
}
