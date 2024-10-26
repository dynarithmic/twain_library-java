/*
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2024 Dynarithmic Software.

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
import java.util.List;
import java.util.stream.Collectors;

import com.dynarithmic.twain.DTwainConstants;
import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;
import com.dynarithmic.twain.lowlevel.TW_FRAME;
import com.dynarithmic.twain.lowlevel.TW_STR255;
import com.dynarithmic.twain.lowlevel.TW_UINT32;

// The private functions and variables are used by the internals of these classes
// and the JNI layer.  
@SuppressWarnings("unused")
public class ExtendedImageInfo {
    public class BarcodeInfo
    {
        long count;

        public class BarcodeSingleInfo
        {
            long confidence;
            long rotation;
            long length;
            long xCoordinate;
            long yCoordinate;
            long type;
            String text;

            private BarcodeSingleInfo setConfidence(long confidence) {
                this.confidence = confidence;
                return this;
            }
            private BarcodeSingleInfo setRotation(long rotation) {
                this.rotation = rotation;
                return this;
            }
            private BarcodeSingleInfo setTextLength(long textLength) {
                this.length = textLength;
                return this;
            }
            private BarcodeSingleInfo setText(String textStr) {
                this.text = textStr;
                return this;
            }

            private BarcodeSingleInfo setXCoordinate(long xCoord) {
                this.xCoordinate = xCoord;
                return this;
            }

            private BarcodeSingleInfo setYCoordinate(long yCoord) {
                this.yCoordinate = yCoord;
                return this;
            }

            private BarcodeSingleInfo setType(long type) {
                this.type = type;
                return this;
            }

            public long getConfidence() {
                return confidence;
            }
            public long getRotation() {
                return rotation;
            }
            public long getLength() {
                return length;
            }
            public long getXCoordinate() {
                return xCoordinate;
            }
            public long getYCoordinate() {
                return yCoordinate;
            }
            public long getType() {
                return type;
            }
            
            public String getTypeName(TwainSession session) throws DTwainJavaAPIException
            {
                return 
                    session.getAPIHandle().DTWAIN_GetTwainNameFromConstant(DTwainConstants.DTwainConstantToString.DTWAIN_CONSTANT_TWBT,(int)type);
            }
            
            public String getText() {
                return text;
            }
        }

        List<BarcodeSingleInfo> barcodeInfos = new ArrayList<>();

        public BarcodeInfo() {}

        private BarcodeInfo setCount(long nCount)
        {
            this.count = nCount;
            barcodeInfos.clear();
            for (int i = 0; i < this.count; ++i)
                barcodeInfos.add(new BarcodeSingleInfo());
            return this;
        }

        private BarcodeInfo setSingleInfo(BarcodeSingleInfo info, int nWhich)
        {
            this.barcodeInfos.set(nWhich,  info);
            return this;
        }

        public List<BarcodeSingleInfo> getAllBarcodeInfo()
        {
            return this.barcodeInfos;
        }

        public long getCount()
        {
            return this.count;
        }

        public BarcodeSingleInfo getSingleInfo(int nWhich) throws DTwainJavaAPIException
        {
            if ( nWhich >= this.barcodeInfos.size() || nWhich < 0)
                throw new DTwainJavaAPIException("Out of bounds");
            return this.barcodeInfos.get(nWhich);
        }
    }

    public class ShadedAreaDetectionInfo
    {
        long count;
        class ShadedAreaSingleInfo
        {
            long top;
            long left;
            long height;
            long width;
            long size;
            long blackCountOld;
            long blackCountNew;
            long blackRLMin;
            long blackRLMax;
            long whiteCountOld;
            long whiteCountNew;
            long whiteRLMin;
            long whiteRLMax;
            long whiteRLAverage;

            private ShadedAreaSingleInfo setTop(long top) {
                this.top = top; return this;
            }
            private ShadedAreaSingleInfo setLeft(long left) {
                this.left = left; return this;
            }
            private ShadedAreaSingleInfo setHeight(long height) {
                this.height = height; return this;
            }
            private ShadedAreaSingleInfo setWidth(long width) {
                this.width = width; return this;
            }
            private ShadedAreaSingleInfo setSize(long size) {
                this.size = size; return this;
            }
            private ShadedAreaSingleInfo setBlackCountOld(long blackCountOld) {
                this.blackCountOld = blackCountOld;
                return this;
            }
            private ShadedAreaSingleInfo setBlackCountNew(long blackCountNew) {
                this.blackCountNew = blackCountNew;
                return this;
            }
            private ShadedAreaSingleInfo setBlackRLMin(long blackRLMin) {
                this.blackRLMin = blackRLMin;
                return this;
            }
            private ShadedAreaSingleInfo setBlackRLMax(long blackRLMax) {
                this.blackRLMax = blackRLMax;
                return this;
            }
            private ShadedAreaSingleInfo setWhiteCountOld(long whiteCountOld) {
                this.whiteCountOld = whiteCountOld;
                return this;
            }
            private ShadedAreaSingleInfo setWhiteCountNew(long whiteCountNew) {
                this.whiteCountNew = whiteCountNew;
                return this;
            }
            private ShadedAreaSingleInfo setWhiteRLMin(long whiteRLMin) {
                this.whiteRLMin = whiteRLMin;
                return this;
            }
            private ShadedAreaSingleInfo setWhiteRLMax(long whiteRLMax) {
                this.whiteRLMax = whiteRLMax;
                return this;
            }
            private ShadedAreaSingleInfo setWhiteRLAverage(long whilteRLAverage) {
                this.whiteRLAverage = whilteRLAverage;
                return this;
            }

            public long getTop() {
                return top;
            }

            public long getLeft() {
                return left;
            }

            public long getHeight() {
                return height;
            }

            public long getWidth() {
                return width;
            }

            public long getSize() {
                return size;
            }

            public long getBlackCountOld() {
                return blackCountOld;
            }

            public long getBlackCountNew() {
                return blackCountNew;
            }

            public long getBlackRLMin() {
                return blackRLMin;
            }

            public long getBlackRLMax() {
                return blackRLMax;
            }

            public long getWhiteCountOld() {
                return whiteCountOld;
            }

            public long getWhiteCountNew() {
                return whiteCountNew;
            }

            public long getWhiteRLMin() {
                return whiteRLMin;
            }

            public long getWhiteRLMax() {
                return whiteRLMax;
            }

            public long getWhiteRLAverage() {
                return whiteRLAverage;
            }
        }

        List<ShadedAreaSingleInfo> shadedAreaInfos = new ArrayList<>();

        ShadedAreaDetectionInfo() {}

        private ShadedAreaDetectionInfo setCount(long count) {
            this.count = count;
            shadedAreaInfos.clear();
            for (int i = 0; i < this.count; ++i)
                shadedAreaInfos.add(new ShadedAreaSingleInfo());
            return this;
        }

        private ShadedAreaDetectionInfo setSingleInfo(ShadedAreaSingleInfo info, int nWhich)
        {
            this.shadedAreaInfos.set(nWhich,  info);
            return this;
        }

        public long getCount() {
            return count;
        }

        public ShadedAreaSingleInfo getSingleInfo(int nWhich) throws DTwainJavaAPIException
        {
            if ( nWhich >= this.shadedAreaInfos.size() || nWhich < 0)
                throw new DTwainJavaAPIException("Out of bounds");
            return this.shadedAreaInfos.get(nWhich);
        }
    }

    public class SpeckleRemovalInfo
    {
        long specklesRemoved;
        long blackSpecklesRemoved;
        long whiteSpecklesRemoved;

        public SpeckleRemovalInfo() {}

        private SpeckleRemovalInfo setSpecklesRemoved(long specklesRemoved) {
            this.specklesRemoved = specklesRemoved;
            return this;
        }
        private SpeckleRemovalInfo setBlackSpecklesRemoved(long blackSpecklesRemoved) {
            this.blackSpecklesRemoved = blackSpecklesRemoved;
            return this;
        }
        private SpeckleRemovalInfo setWhiteSpecklesRemoved(long whiteSpecklesRemoved) {
            this.whiteSpecklesRemoved = whiteSpecklesRemoved;
            return this;
        }

        public long getSpecklesRemoved() {
            return specklesRemoved;
        }

        public long getBlackSpecklesRemoved() {
            return blackSpecklesRemoved;
        }

        public long getWhiteSpecklesRemoved() {
            return whiteSpecklesRemoved;
        }
    }

    public class LineDetectionInfo
    {
        long count;

        public class LineDetectionSingleInfo
        {
            long xCoordinate;
            long yCoordinate;
            long length;
            long thickness;

            private LineDetectionSingleInfo  setXCoordinate(long xCoordinate) {
                this.xCoordinate = xCoordinate;
                return this;
            }

            private LineDetectionSingleInfo  setYCoordinate(long yCoordinate) {
                this.yCoordinate = yCoordinate;
                return this;
            }

            private  LineDetectionSingleInfo setLength(long length) {
                this.length = length;
                return this;
            }

            private  LineDetectionSingleInfo setThickness(long thickness) {
                this.thickness = thickness;
                return this;
            }

            public long getXCoordinate() {
                return xCoordinate;
            }

            public long getYCoordinate() {
                return yCoordinate;
            }

            public long getLength() {
                return length;
            }

            public long getThickness() {
                return thickness;
            }
        }

        List<LineDetectionSingleInfo> lineInfos = new ArrayList<>();

        public LineDetectionInfo() {}
        
        private LineDetectionInfo setCount(long count) 
        {
            this.count = count;
            lineInfos.clear();
            for (int i = 0; i < this.count; ++i)
                lineInfos.add(new LineDetectionSingleInfo());
            return this;
        }

        private LineDetectionInfo setSingleInfo(LineDetectionSingleInfo info, int nWhich)
        {
            this.lineInfos.set(nWhich, info);
            return this;
        }

        public LineDetectionSingleInfo getSingleInfo(int nWhich) throws DTwainJavaAPIException
        {
            if ( nWhich >= this.lineInfos.size() || nWhich < 0)
                throw new DTwainJavaAPIException("Out of bounds");
            return this.lineInfos.get(nWhich);
        }
        
        public long getCount() {
            return count;
        }
    }

    public class PatchcodeDetectionInfo
    {
        long patchcode;

        public PatchcodeDetectionInfo() {}

        private PatchcodeDetectionInfo setPatchcode(long patchcode) {
            this.patchcode = patchcode;
            return this;
        }

        public long getPatchcode() {
            return patchcode;
        }
    }
    
    public class SkewDetectionInfo
    {
        long deskewStatus;
        long originalAngle;
        long finalAngle;
        long confidence;
        long windowX1;
        long windowX2;
        long windowX3;
        long windowX4;
        long windowY1;
        long windowY2;
        long windowY3;
        long windowY4;

        public SkewDetectionInfo() {}

        SkewDetectionInfo setDeskewStatus(long deskewStatus) {
            this.deskewStatus = deskewStatus; return this;
        }

        SkewDetectionInfo setOriginalAngle(long originalAngle) {
            this.originalAngle = originalAngle; return this;
        }

        SkewDetectionInfo setFinalAngle(long finalAngle) {
            this.finalAngle = finalAngle; return this;
        }

        SkewDetectionInfo setConfidence(long confidence) {
            this.confidence = confidence; return this;
        }

        SkewDetectionInfo setWindowX1(long windowX1) {
            this.windowX1 = windowX1; return this;
        }

        SkewDetectionInfo setWindowX2(long windowX2) {
            this.windowX2 = windowX2; return this;
        }

        SkewDetectionInfo setWindowX3(long windowX3) {
            this.windowX3 = windowX3; return this;
        }

        SkewDetectionInfo setWindowX4(long windowX4) {
            this.windowX4 = windowX4; return this;
        }

        SkewDetectionInfo setWindowY1(long windowY1) {
            this.windowY1 = windowY1; return this;
        }

        SkewDetectionInfo setWindowY2(long windowY2) {
            this.windowY2 = windowY2; return this;
        }

        SkewDetectionInfo setWindowY3(long windowY3) {
            this.windowY3 = windowY3; return this;
        }

        SkewDetectionInfo setWindowY4(long windowY4) {
            this.windowY4 = windowY4; return this;
        }

        public long getDeskewStatus() {
            return deskewStatus;
        }

        public long getOriginalAngle() {
            return originalAngle;
        }

        public long getFinalAngle() {
            return finalAngle;
        }

        public long getConfidence() {
            return confidence;
        }

        public long getWindowX1() {
            return windowX1;
        }

        public long getWindowX2() {
            return windowX2;
        }

        public long getWindowX3() {
            return windowX3;
        }

        public long getWindowX4() {
            return windowX4;
        }

        public long getWindowY1() {
            return windowY1;
        }

        public long getWindowY2() {
            return windowY2;
        }

        public long getWindowY3() {
            return windowY3;
        }

        public long getWindowY4() {
            return windowY4;
        }
    }
    
    public class EndorsedTextInfo
    {
        String text;
        public EndorsedTextInfo() {}
        private EndorsedTextInfo setText(String text)
        {
            this.text = text;
            return this;
        }

        public String getText()
        {
            return this.text;
        }
    }
    
    public class FormsRecognitionInfo
    {
        List<Long> confidenceI = new ArrayList<>();
        List<String> templateMatchS = new ArrayList<>();
        List<Long> templatePageMatchI = new ArrayList<>();
        List<Long> horizontalDocOffsetI = new ArrayList<>();
        List<Long> verticalDocOffsetI = new ArrayList<>();
        
        public FormsRecognitionInfo() {}

        private FormsRecognitionInfo setConfidence(List<TW_UINT32> confidence) 
        {
            this.confidenceI = confidence.stream()
                    .map(TW_UINT32::getValue)
                    .collect(Collectors.toList());
            return this;
        }
        
        private FormsRecognitionInfo setTemplateMatch(List<TW_STR255> templateMatch) 
        {
            this.templateMatchS = templateMatch.stream()
                    .map(TW_STR255::getValue)
                    .collect(Collectors.toList());
            return this;
        }
        
        private FormsRecognitionInfo setTemplatePageMatch(List<TW_UINT32> templatePageMatch) 
        {
            this.templatePageMatchI = templatePageMatch.stream()
                    .map(TW_UINT32::getValue)
                    .collect(Collectors.toList());
            return this;
        }
        
        private FormsRecognitionInfo setHorizontalDocOffset(List<TW_UINT32> horizontalDocOffset) 
        {
            this.horizontalDocOffsetI = horizontalDocOffset.stream()
                    .map(TW_UINT32::getValue)
                    .collect(Collectors.toList());
            return this;
        }
        
        private FormsRecognitionInfo setVerticalDocOffset(List<TW_UINT32> verticalDocOffset) 
        {
            this.verticalDocOffsetI = verticalDocOffset.stream()
                    .map(TW_UINT32::getValue)
                    .collect(Collectors.toList());
            return this;
        }

        public List<Long> getConfidence() {
            return confidenceI;
        }

        public List<String> getTemplateMatch() {
            return templateMatchS;
        }

        public List<Long> getTemplatePageMatch() {
            return templatePageMatchI;
        }

        public List<Long> getHorizontalDocOffset() {
            return horizontalDocOffsetI;
        }

        public List<Long> getVerticalDocOffset() {
            return verticalDocOffsetI;
        }
    }
    
    public class PageSourceInfo
    {
        String bookname;
        String camera;
        long chapterNumber;
        long documentNumber;
        long pageNumber;
        long frameNumber;
        TwainFrameDouble frameD = new TwainFrameDouble();
        int pageside;
        int pixelFlavor;

        public PageSourceInfo() {}

        private PageSourceInfo setPageSide(int pageSide) {
            this.pageside = pageSide;
            return this;
        }
        
        private PageSourceInfo setBookname(String bookname) {
            this.bookname = bookname; return this;
        }
        private PageSourceInfo setChapterNumber(long chapterNumber) {
            this.chapterNumber = chapterNumber; return this;
        }
        private PageSourceInfo setDocumentNumber(long documentNumber) {
            this.documentNumber = documentNumber; return this;
        }
        private PageSourceInfo setPageNumber(long pageNumber) {
            this.pageNumber = pageNumber; return this;
        }
        private PageSourceInfo setCamera(String camera) {
            this.camera = camera; return this;
        }
        private PageSourceInfo setFrameNumber(long frameNumber) {
            this.frameNumber = frameNumber; return this;
        }
        private PageSourceInfo setFrame(TW_FRAME frame) {
            frameD.setAll(frame);
            return this;
        }
        private PageSourceInfo setPixelFlavor(int pixelFlavor) {
            this.pixelFlavor = pixelFlavor; return this;
        }

        public int getPageSide()
        {
            return this.pageside;
        }
        
        public String getPageSideName(TwainSession session) throws DTwainJavaAPIException 
        {
            return
            session.getAPIHandle().DTWAIN_GetTwainNameFromConstant(DTwainConstants.DTwainConstantToString.DTWAIN_CONSTANT_TWCS,pageside);
        }
        
        public String getBookname() {
            return bookname;
        }

        public long getChapterNumber() {
            return chapterNumber;
        }

        public long getDocumentNumber() {
            return documentNumber;
        }

        public long getPageNumber() {
            return pageNumber;
        }

        public String getCamera() {
            return camera;
        }

        public long getFrameNumber() {
            return frameNumber;
        }

        public TwainFrameDouble getFrame() {
            return frameD;
        }

        public int getPixelFlavor() {
            return pixelFlavor;
        }
    }
    
    public class ImageSegmentationInfo
    {
        String ICCProfile;
        boolean lastSegment;
        long segmentNumber;

        public ImageSegmentationInfo() {}

        private ImageSegmentationInfo setICCProfile(String iCCProfile) {
            ICCProfile = iCCProfile; return this;
        }

        private ImageSegmentationInfo setLastSegment(boolean lastSegment) {
            this.lastSegment = lastSegment; return this;
        }

        private ImageSegmentationInfo setSegmentNumber(long segmentNumber) {
            this.segmentNumber = segmentNumber; return this;
        }

        public String getICCProfile() {
            return ICCProfile;
        }

        public boolean isLastSegment() {
            return lastSegment;
        }

        public long getSegmentNumber() {
            return segmentNumber;
        }
    }
    
    public class ExtendedImageInfo20
    {
        int magtype;

        public ExtendedImageInfo20() {}

        private ExtendedImageInfo20 setMagType(int magtype) {
            this.magtype = magtype; return this;
        }

        public int getMagType() {
            return magtype;
        }
    }
    
    public class ExtendedImageInfo21
    {
        String fileSystemSource;
        boolean imageMerged;
        byte [] magData = new byte [0];
        long magDataLength;
        int pageSide;
        
        public ExtendedImageInfo21() {}
        
        private ExtendedImageInfo21 setFileSystemSource(String fileSystemSource)
        {
            this.fileSystemSource = fileSystemSource;
            return this;
        }
        
        private ExtendedImageInfo21 setMagData(byte [] magData)
        {
            this.magData = magData;
            return this;
        }
        
        private ExtendedImageInfo21 setImageMerged(boolean imageMerged)
        {
            this.imageMerged = imageMerged;
            return this;
        }
        
        private ExtendedImageInfo21 setMagDataLength(long magDataLength)
        {
            this.magDataLength = magDataLength;
            return this;
        }
        
        private ExtendedImageInfo21 setPageSide(int pageSide)
        {
            this.pageSide = pageSide;
            return this;
        }
        
        public String getFileSystemSource()
        {
            return this.fileSystemSource;
        }
        
        public byte [] getMagData()
        {
            return this.magData;
        }
        
        public boolean getImageMerged()
        {
            return this.imageMerged;
        }
        
        public long getMagDataLength()
        {
            return this.magDataLength;
        }
        
        public int getPageSide()
        {
            return this.pageSide;
        }
    }

    public class ExtendedImageInfo22
    {
        long paperCount;
        public ExtendedImageInfo22() {}
        
        private ExtendedImageInfo22 setPaperCount(long paperCount) 
        {
            this.paperCount = paperCount;
            return this;
        }
        
        public long getPaperCount()
        {
            return this.paperCount;
        }
    }

    public class ExtendedImageInfo23
    {
        String printerText;
        public ExtendedImageInfo23() {}
        private ExtendedImageInfo23 setPrinterText(String printerText) 
        {
            this.printerText = printerText;
            return this;
        }
        
        public String getPrinterText()
        {
            return this.printerText;
        }
    }

    public class ExtendedImageInfo24
    {
        String twainDirectMetaData = "";
        public ExtendedImageInfo24() {}
        private ExtendedImageInfo24 setTwainDirectMetaData(String twainDirectMetaData) 
        {
            this.twainDirectMetaData = twainDirectMetaData;
            return this;
        }
        
        public String getTwainDirectMetaData()
        {
            return this.twainDirectMetaData;
        }
    }

    public class ExtendedImageInfo25
    {
        private final String[] iAField = new String[]{"", "", "", "", ""};
        private int iALevel;
        private int printer;
        private final List<String> barcodeText = new ArrayList<>();
        
        private void setIAField(String val, int nWhich)
        {
            iAField[nWhich] = val;
        }
        private String getIAField(int nWhich)
        {
            return iAField[nWhich];
        }
        private ExtendedImageInfo25 setIAFieldValueA(String val)
        {
            setIAField(val, 0);
            return this;
        }
        private ExtendedImageInfo25 setIAFieldValueB(String val)
        {
            setIAField(val, 1);
            return this;
        }
        private ExtendedImageInfo25 setIAFieldValueC(String val)
        {
            setIAField(val, 2);
            return this;
        }
        private ExtendedImageInfo25 setIAFieldValueD(String val)
        {
            setIAField(val, 3);
            return this;
        }
        private ExtendedImageInfo25 setIAFieldValueE(String val)
        {
            setIAField(val, 4);
            return this;
        }
        private ExtendedImageInfo25 setIALevel(int val)
        {
            this.iALevel = val;
            return this;
        }
        private ExtendedImageInfo25 setPrinter(int val)
        {
            this.printer = val;
            return this;
        }
        private ExtendedImageInfo25 addBarcodeText(String text)
        {
            barcodeText.add(text);
            return this;
        }
        public String getIAFieldValueA()
        {
            return this.getIAField(0);
        }
        public String getIAFieldValueB()
        {
            return this.getIAField(1);
        }
        public String getIAFieldValueC()
        {
            return this.getIAField(2);
        }
        public String getIAFieldValueD()
        {
            return this.getIAField(3);
        }
        public String getIAFieldValueE()
        {
            return this.getIAField(4);
        }
        public int getIALevel()
        {
            return this.iALevel;
        }
        public int getPrinter()
        {
            return this.printer;
        }
        public int getNumBarCodes() { return this.barcodeText.size(); }
        public String getBarCode(int i) 
        {
            String s = "";
            if ( i < 0 || i >= getNumBarCodes() )
                return s;
            return this.barcodeText.get(i);
        }
    }

    public enum LineDetection {HORIZONTAL, VERTICAL};
    
    int [] supportedExtImageInfo = new int[0];
    BarcodeInfo barcodeInfo = new BarcodeInfo();
    ShadedAreaDetectionInfo shadedAreaDetectionInfo = new ShadedAreaDetectionInfo();
    SpeckleRemovalInfo speckleRemovalInfo = new SpeckleRemovalInfo();
    LineDetectionInfo [] lineDeetectionInfo = new LineDetectionInfo[] {new LineDetectionInfo(), new LineDetectionInfo()};
    PatchcodeDetectionInfo patchcodeDetectionInfo = new PatchcodeDetectionInfo();
    SkewDetectionInfo skewDetectionInfo = new SkewDetectionInfo();
    EndorsedTextInfo endorsedTextInfo = new EndorsedTextInfo();
    FormsRecognitionInfo formsRecognitionInfo = new FormsRecognitionInfo();
    PageSourceInfo pageSourceInfo = new PageSourceInfo();
    ImageSegmentationInfo imageSegmentationInfo = new ImageSegmentationInfo();
    ExtendedImageInfo20 extimageInfo20 = new ExtendedImageInfo20();
    ExtendedImageInfo21 extImageInfo21 = new ExtendedImageInfo21();
    ExtendedImageInfo22 extImageInfo22 = new ExtendedImageInfo22();
    ExtendedImageInfo23 extImageInfo23 = new ExtendedImageInfo23();
    ExtendedImageInfo24 extImageInfo24 = new ExtendedImageInfo24();
    ExtendedImageInfo25 extImageInfo25 = new ExtendedImageInfo25();

    public ExtendedImageInfo()
    {}

    private ExtendedImageInfo setSupportedExtendedImageInfo(int [] supportedExtImageInfo )
    {
        this.supportedExtImageInfo = supportedExtImageInfo;
        return this;
    }
    
    private ExtendedImageInfo setBarcodeInfo(BarcodeInfo barcodeInfo) {
        this.barcodeInfo = barcodeInfo; return this;
    }

    private ExtendedImageInfo setShadedAreaDetectionInfo(ShadedAreaDetectionInfo shadedAreaDetectionInfo) {
        this.shadedAreaDetectionInfo = shadedAreaDetectionInfo; return this;
    }

    private ExtendedImageInfo setSpeckleRemovalInfo(SpeckleRemovalInfo speckleRemoveInfo) {
        this.speckleRemovalInfo = speckleRemoveInfo; return this;
    }

    private ExtendedImageInfo setLineDetectionInfo(LineDetectionInfo detectInfo, LineDetection nWhich)
    {
        this.lineDeetectionInfo[nWhich.ordinal()] = detectInfo;
        return this;
    }
    
    private ExtendedImageInfo setHorizontalLineDetectionInfo(LineDetectionInfo horizontalLineDetectionInfo) {
        this.lineDeetectionInfo[LineDetection.HORIZONTAL.ordinal()] = horizontalLineDetectionInfo; return this;
    }

    private ExtendedImageInfo setVerticalLineDetectionInfo(LineDetectionInfo verticalLineDetectionInfo) {
        this.lineDeetectionInfo[LineDetection.VERTICAL.ordinal()] = verticalLineDetectionInfo; return this;
    }

    private ExtendedImageInfo setPatchcodeDetectionInfo(PatchcodeDetectionInfo patchcodeDetectionInfo) {
        this.patchcodeDetectionInfo = patchcodeDetectionInfo; return this;
    }

    private ExtendedImageInfo setSkewDetectionInfo(SkewDetectionInfo skewDetectionInfo) {
        this.skewDetectionInfo = skewDetectionInfo; return this;
    }

    private ExtendedImageInfo setEndorsedTextInfo(EndorsedTextInfo endorsedTextInfo) {
        this.endorsedTextInfo = endorsedTextInfo; return this;
    }

    private ExtendedImageInfo setFormsRecognitionInfo(FormsRecognitionInfo formsRecognitionInfo) {
        this.formsRecognitionInfo = formsRecognitionInfo; return this;
    }

    private ExtendedImageInfo setPageSourceInfo(PageSourceInfo pageSourceInfo) {
        this.pageSourceInfo = pageSourceInfo; return this;
    }

    private ExtendedImageInfo setImageSegmentationInfo(ImageSegmentationInfo imageSegmentationInfo) {
        this.imageSegmentationInfo = imageSegmentationInfo; return this;
    }

    private ExtendedImageInfo setExtendedImageInfo20(ExtendedImageInfo20 extimageInfo20) {
        this.extimageInfo20 = extimageInfo20; return this;
    }

    private ExtendedImageInfo setExtendedImageInfo21(ExtendedImageInfo21 extImageInfo21)
    {
        this.extImageInfo21 = extImageInfo21;
        return this;
    }

    private ExtendedImageInfo setExtendedImageInfo22(ExtendedImageInfo22 extImageInfo22)
    {
        this.extImageInfo22 = extImageInfo22;
        return this;
    }
    private ExtendedImageInfo setExtendedImageInfo23(ExtendedImageInfo23 extImageInfo23)
    {
        this.extImageInfo23 = extImageInfo23;
        return this;
    }

    private ExtendedImageInfo setExtendedImageInfo24(ExtendedImageInfo24 extImageInfo24)
    {
        this.extImageInfo24 = extImageInfo24;
        return this;
    }

    private ExtendedImageInfo setExtendedImageInfo25(ExtendedImageInfo25 extImageInfo25)
    {
        this.extImageInfo25 = extImageInfo25;
        return this;
    }
    
    public BarcodeInfo getBarcodeInfo() {
        return barcodeInfo;
    }

    public ShadedAreaDetectionInfo getShadedAreaDetectionInfo() {
        return shadedAreaDetectionInfo;
    }

    public SpeckleRemovalInfo getSpeckleRemovalInfo() {
        return speckleRemovalInfo;
    }

    public LineDetectionInfo getHorizontalLineDetectionInfo() {
        return this.lineDeetectionInfo[LineDetection.HORIZONTAL.ordinal()]; // horizontalLineDetectionInfo;
    }

    public LineDetectionInfo getVerticalLineDetectionInfo() {
        return this.lineDeetectionInfo[LineDetection.VERTICAL.ordinal()]; 
    }

    public PatchcodeDetectionInfo getPatchcodeDetectionInfo() {
        return patchcodeDetectionInfo;
    }

    public SkewDetectionInfo getSkewDetectionInfo() {
        return skewDetectionInfo;
    }

    public EndorsedTextInfo getEndorsedTextInfo() {
        return endorsedTextInfo;
    }

    public FormsRecognitionInfo getFormsRecognitionInfo() {
        return formsRecognitionInfo;
    }

    public PageSourceInfo getPageSourceInfo() {
        return pageSourceInfo;
    }

    public ImageSegmentationInfo getImageSegmentationInfo() {
        return imageSegmentationInfo;
    }

    public ExtendedImageInfo20 getExtendedImageInfo20() {
        return extimageInfo20;
    }
    
    public ExtendedImageInfo21 getExtendedImageInfo21() {
        return this.extImageInfo21;
    }
    
    public ExtendedImageInfo22 getExtendedImageInfo22() {
        return this.extImageInfo22;
    }
    public ExtendedImageInfo23 getExtendedImageInfo23() {
        return this.extImageInfo23;
    }
    public ExtendedImageInfo24 getExtendedImageInfo24() {
        return this.extImageInfo24;
    }
    public ExtendedImageInfo25 getExtendedImageInfo25() {
        return this.extImageInfo25;
    }
    public int[] getSupportedExtendedImageInfo()
    {
        return this.supportedExtImageInfo;
    }
    
    public static String getTypeName(TwainSession session, int value) throws DTwainJavaAPIException
    {
        return 
            session.getAPIHandle().DTWAIN_GetTwainNameFromConstant(DTwainConstants.DTwainConstantToString.DTWAIN_CONSTANT_TWEI, value);
    }
}
