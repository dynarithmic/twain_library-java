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
import java.util.Arrays;
import java.util.List;

import com.dynarithmic.twain.DTwainConstants;
import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;
import com.dynarithmic.twain.lowlevel.TW_BOOL;
import com.dynarithmic.twain.lowlevel.TW_FRAME;
import com.dynarithmic.twain.lowlevel.TW_STR255;
import com.dynarithmic.twain.lowlevel.TW_STR32;
import com.dynarithmic.twain.lowlevel.TW_UINT16;
import com.dynarithmic.twain.lowlevel.TW_UINT32;

public class ExtendedImageInfo {
    public class BarcodeInfo
    {
        TW_UINT32 count = new TW_UINT32();

        public class BarcodeSingleInfo
        {
            TW_UINT32 confidence =  new TW_UINT32();
            TW_UINT32 rotation =  new TW_UINT32();
            TW_UINT32 length =  new TW_UINT32();
            TW_UINT32 xCoordinate = new TW_UINT32();
            TW_UINT32 yCoordinate = new TW_UINT32();
            TW_UINT32 type  = new TW_UINT32();
            TW_STR255 text;

            private BarcodeSingleInfo setConfidence(TW_UINT32 confidence) {
                this.confidence = confidence;
                return this;
            }
            private BarcodeSingleInfo setRotation(TW_UINT32 rotation) {
                this.rotation = rotation;
                return this;
            }
            private BarcodeSingleInfo setTextLength(TW_UINT32 textLength) {
                this.length = textLength;
                return this;
            }
            private BarcodeSingleInfo setString(TW_STR255 textStr) {
                this.text = textStr;
                return this;
            }

            private BarcodeSingleInfo setXCoordinate(TW_UINT32 xCoord) {
                this.xCoordinate = xCoord;
                return this;
            }

            private BarcodeSingleInfo setYCoordinate(TW_UINT32 yCoord) {
                this.yCoordinate = yCoord;
                return this;
            }

            private BarcodeSingleInfo setType(TW_UINT32 type) {
                this.type = type;
                return this;
            }

            public TW_UINT32 getConfidence() {
                return confidence;
            }
            public TW_UINT32 getRotation() {
                return rotation;
            }
            public TW_UINT32 getLength() {
                return length;
            }
            public TW_UINT32 getXCoordinate() {
                return xCoordinate;
            }
            public TW_UINT32 getYCoordinate() {
                return yCoordinate;
            }
            public TW_UINT32 getType() {
                return type;
            }
            
            public String getTypeName(TwainSession session) throws DTwainJavaAPIException
            {
                return 
                    session.getAPIHandle().DTWAIN_GetTwainNameFromConstant(DTwainConstants.DTwainConstantToString.DTWAIN_CONSTANT_TWBT,(int)type.getValue());
            }
            
            public String getText() {
                return text.getValue();
            }
        }

        List<BarcodeSingleInfo> barcodeInfos = new ArrayList<>();

        public BarcodeInfo() {}

        private BarcodeInfo setCount(TW_UINT32 nCount)
        {
            this.count = nCount;
            barcodeInfos.clear();
            for (int i = 0; i < this.count.getValue(); ++i)
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

        public TW_UINT32 getCount()
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
        TW_UINT32 count = new TW_UINT32();
        class ShadedAreaSingleInfo
        {
            TW_UINT32 top = new TW_UINT32();
            TW_UINT32 left = new TW_UINT32();
            TW_UINT32 height = new TW_UINT32();
            TW_UINT32 width = new TW_UINT32();
            TW_UINT32 size = new TW_UINT32();
            TW_UINT32 blackCountOld = new TW_UINT32();
            TW_UINT32 blackCountNew = new TW_UINT32();
            TW_UINT32 blackRLMin = new TW_UINT32();
            TW_UINT32 blackRLMax = new TW_UINT32();
            TW_UINT32 whiteCountOld = new TW_UINT32();
            TW_UINT32 whiteCountNew = new TW_UINT32();
            TW_UINT32 whiteRLMin = new TW_UINT32();
            TW_UINT32 whiteRLMax = new TW_UINT32();
            TW_UINT32 whiteRLAverage = new TW_UINT32();

            private ShadedAreaSingleInfo setTop(TW_UINT32 top) {
                this.top = top; return this;
            }
            private ShadedAreaSingleInfo setLeft(TW_UINT32 left) {
                this.left = left; return this;
            }
            private ShadedAreaSingleInfo setHeight(TW_UINT32 height) {
                this.height = height; return this;
            }
            private ShadedAreaSingleInfo setWidth(TW_UINT32 width) {
                this.width = width; return this;
            }
            private ShadedAreaSingleInfo setSize(TW_UINT32 size) {
                this.size = size; return this;
            }
            private ShadedAreaSingleInfo setBlackCountOld(TW_UINT32 blackCountOld) {
                this.blackCountOld = blackCountOld;
                return this;
            }
            private ShadedAreaSingleInfo setBlackCountNew(TW_UINT32 blackCountNew) {
                this.blackCountNew = blackCountNew;
                return this;
            }
            private ShadedAreaSingleInfo setBlackRLMin(TW_UINT32 blackRLMin) {
                this.blackRLMin = blackRLMin;
                return this;
            }
            private ShadedAreaSingleInfo setBlackRLMax(TW_UINT32 blackRLMax) {
                this.blackRLMax = blackRLMax;
                return this;
            }
            private ShadedAreaSingleInfo setWhiteCountOld(TW_UINT32 whiteCountOld) {
                this.whiteCountOld = whiteCountOld;
                return this;
            }
            private ShadedAreaSingleInfo setWhiteCountNew(TW_UINT32 whiteCountNew) {
                this.whiteCountNew = whiteCountNew;
                return this;
            }
            private ShadedAreaSingleInfo setWhiteRLMin(TW_UINT32 whiteRLMin) {
                this.whiteRLMin = whiteRLMin;
                return this;
            }
            private ShadedAreaSingleInfo setWhiteRLMax(TW_UINT32 whiteRLMax) {
                this.whiteRLMax = whiteRLMax;
                return this;
            }
            private ShadedAreaSingleInfo setWhiteRLAverage(TW_UINT32 whilteRLAverage) {
                this.whiteRLAverage = whilteRLAverage;
                return this;
            }

            public TW_UINT32 getTop() {
                return top;
            }

            public TW_UINT32 getLeft() {
                return left;
            }

            public TW_UINT32 getHeight() {
                return height;
            }

            public TW_UINT32 getWidth() {
                return width;
            }

            public TW_UINT32 getSize() {
                return size;
            }

            public TW_UINT32 getBlackCountOld() {
                return blackCountOld;
            }

            public TW_UINT32 getBlackCountNew() {
                return blackCountNew;
            }

            public TW_UINT32 getBlackRLMin() {
                return blackRLMin;
            }

            public TW_UINT32 getBlackRLMax() {
                return blackRLMax;
            }

            public TW_UINT32 getWhiteCountOld() {
                return whiteCountOld;
            }

            public TW_UINT32 getWhiteCountNew() {
                return whiteCountNew;
            }

            public TW_UINT32 getWhiteRLMin() {
                return whiteRLMin;
            }

            public TW_UINT32 getWhiteRLMax() {
                return whiteRLMax;
            }

            public TW_UINT32 getWhiteRLAverage() {
                return whiteRLAverage;
            }
        }

        List<ShadedAreaSingleInfo> shadedAreaInfos = new ArrayList<>();

        ShadedAreaDetectionInfo() {}

        private ShadedAreaDetectionInfo setCount(TW_UINT32 count) {
            this.count = count;
            shadedAreaInfos.clear();
            for (int i = 0; i < this.count.getValue(); ++i)
                shadedAreaInfos.add(new ShadedAreaSingleInfo());
            return this;
        }

        private ShadedAreaDetectionInfo setSingleInfo(ShadedAreaSingleInfo info, int nWhich)
        {
            this.shadedAreaInfos.set(nWhich,  info);
            return this;
        }

        public TW_UINT32 getCount() {
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
        TW_UINT32 specklesRemoved = new TW_UINT32();
        TW_UINT32 blackSpecklesRemoved = new TW_UINT32();
        TW_UINT32 whiteSpecklesRemoved = new TW_UINT32();

        public SpeckleRemovalInfo() {}

        private SpeckleRemovalInfo setSpecklesRemoved(TW_UINT32 specklesRemoved) {
            this.specklesRemoved = specklesRemoved;
            return this;
        }
        private SpeckleRemovalInfo setBlackSpecklesRemoved(TW_UINT32 blackSpecklesRemoved) {
            this.blackSpecklesRemoved = blackSpecklesRemoved;
            return this;
        }
        private SpeckleRemovalInfo setWhiteSpecklesRemoved(TW_UINT32 whiteSpecklesRemoved) {
            this.whiteSpecklesRemoved = whiteSpecklesRemoved;
            return this;
        }

        public TW_UINT32 getSpecklesRemoved() {
            return specklesRemoved;
        }

        public TW_UINT32 getBlackSpecklesRemoved() {
            return blackSpecklesRemoved;
        }

        public TW_UINT32 getWhiteSpecklesRemoved() {
            return whiteSpecklesRemoved;
        }
    }

    public class HorizontalLineDetectionInfo
    {
        TW_UINT32 count = new TW_UINT32();

        public class HorizontalLineDetectionSingleInfo
        {
            TW_UINT32 xCoordinate = new TW_UINT32();
            TW_UINT32 yCoordinate = new TW_UINT32();
            TW_UINT32 length    = new TW_UINT32();
            TW_UINT32 thickness = new TW_UINT32();

            private HorizontalLineDetectionSingleInfo  setXCoordinate(TW_UINT32 xCoordinate) {
                this.xCoordinate = xCoordinate;
                return this;
            }

            private HorizontalLineDetectionSingleInfo  setYCoordinate(TW_UINT32 yCoordinate) {
                this.yCoordinate = yCoordinate;
                return this;
            }

            private  HorizontalLineDetectionSingleInfo setLength(TW_UINT32 length) {
                this.length = length;
                return this;
            }

            private  HorizontalLineDetectionSingleInfo setThickness(TW_UINT32 thickness) {
                this.thickness = thickness;
                return this;
            }

            public TW_UINT32 getXCoordinate() {
                return xCoordinate;
            }

            public TW_UINT32 getYCoordinate() {
                return yCoordinate;
            }

            public TW_UINT32 getLength() {
                return length;
            }

            public TW_UINT32 getThickness() {
                return thickness;
            }
        }

        List<HorizontalLineDetectionSingleInfo> horizontalLineInfos = new ArrayList<>();

        public HorizontalLineDetectionInfo() {}
        
        private HorizontalLineDetectionInfo setCount(TW_UINT32 count) 
        {
            this.count = count;
            horizontalLineInfos.clear();
            for (int i = 0; i < this.count.getValue(); ++i)
                horizontalLineInfos.add(new HorizontalLineDetectionSingleInfo());
            return this;
        }

        private HorizontalLineDetectionInfo setSingleInfo(HorizontalLineDetectionSingleInfo info, int nWhich)
        {
            this.horizontalLineInfos.set(nWhich, info);
            return this;
        }

        public HorizontalLineDetectionSingleInfo getSingleInfo(int nWhich) throws DTwainJavaAPIException
        {
            if ( nWhich >= this.horizontalLineInfos.size() || nWhich < 0)
                throw new DTwainJavaAPIException("Out of bounds");
            return this.horizontalLineInfos.get(nWhich);
        }
        
        public TW_UINT32 getCount() {
            return count;
        }

    }
    
    public class VerticalLineDetectionInfo
    {
        TW_UINT32 count = new TW_UINT32();

        public class VerticalLineDetectionSingleInfo
        {
            TW_UINT32 xCoordinate = new TW_UINT32();
            TW_UINT32 yCoordinate = new TW_UINT32();
            TW_UINT32 length    = new TW_UINT32();
            TW_UINT32 thickness = new TW_UINT32();

            private VerticalLineDetectionSingleInfo  setXCoordinate(TW_UINT32 xCoordinate) {
                this.xCoordinate = xCoordinate;
                return this;
            }

            private VerticalLineDetectionSingleInfo  setYCoordinate(TW_UINT32 yCoordinate) {
                this.yCoordinate = yCoordinate;
                return this;
            }

            private  VerticalLineDetectionSingleInfo setLength(TW_UINT32 length) {
                this.length = length;
                return this;
            }

            private  VerticalLineDetectionSingleInfo setThickness(TW_UINT32 thickness) {
                this.thickness = thickness;
                return this;
            }

            public TW_UINT32 getXCoordinate() {
                return xCoordinate;
            }

            public TW_UINT32 getYCoordinate() {
                return yCoordinate;
            }

            public TW_UINT32 getLength() {
                return length;
            }

            public TW_UINT32 getThickness() {
                return thickness;
            }
        }

        List<VerticalLineDetectionSingleInfo> VerticalLineInfos = new ArrayList<>();

        public VerticalLineDetectionInfo() {}
        
        private VerticalLineDetectionInfo setCount(TW_UINT32 count) 
        {
            this.count = count;
            VerticalLineInfos.clear();
            for (int i = 0; i < this.count.getValue(); ++i)
                VerticalLineInfos.add(new VerticalLineDetectionSingleInfo());
            return this;
        }

        private VerticalLineDetectionInfo setSingleInfo(VerticalLineDetectionSingleInfo info, int nWhich)
        {
            this.VerticalLineInfos.set(nWhich, info);
            return this;
        }

        public VerticalLineDetectionSingleInfo getSingleInfo(int nWhich) throws DTwainJavaAPIException
        {
            if ( nWhich >= this.VerticalLineInfos.size() || nWhich < 0)
                throw new DTwainJavaAPIException("Out of bounds");
            return this.VerticalLineInfos.get(nWhich);
        }
        
        public TW_UINT32 getCount() {
            return count;
        }
    }

    public class PatchcodeDetectionInfo
    {
        TW_UINT32 patchcode = new TW_UINT32();

        public PatchcodeDetectionInfo() {}

        private PatchcodeDetectionInfo setPatchcode(TW_UINT32 patchcode) {
            this.patchcode = patchcode;
            return this;
        }

        public TW_UINT32 getPatchcode() {
            return patchcode;
        }
    }
    public class SkewDetectionInfo
    {
        TW_UINT32 deskewStatus = new TW_UINT32();
        TW_UINT32 originalAngle = new TW_UINT32();
        TW_UINT32 finalAngle = new TW_UINT32();
        TW_UINT32 confidence = new TW_UINT32();
        TW_UINT32 windowX1 = new TW_UINT32();
        TW_UINT32 windowX2 = new TW_UINT32();
        TW_UINT32 windowX3 = new TW_UINT32();
        TW_UINT32 windowX4 = new TW_UINT32();
        TW_UINT32 windowY1 = new TW_UINT32();
        TW_UINT32 windowY2 = new TW_UINT32();
        TW_UINT32 windowY3 = new TW_UINT32();
        TW_UINT32 windowY4 = new TW_UINT32();

        public SkewDetectionInfo() {}

        SkewDetectionInfo setDeskewStatus(TW_UINT32 deskewStatus) {
            this.deskewStatus = deskewStatus; return this;
        }

        SkewDetectionInfo setOriginalAngle(TW_UINT32 originalAngle) {
            this.originalAngle = originalAngle; return this;
        }

        SkewDetectionInfo setFinalAngle(TW_UINT32 finalAngle) {
            this.finalAngle = finalAngle; return this;
        }

        SkewDetectionInfo setConfidence(TW_UINT32 confidence) {
            this.confidence = confidence; return this;
        }

        SkewDetectionInfo setWindowX1(TW_UINT32 windowX1) {
            this.windowX1 = windowX1; return this;
        }

        SkewDetectionInfo setWindowX2(TW_UINT32 windowX2) {
            this.windowX2 = windowX2; return this;
        }

        SkewDetectionInfo setWindowX3(TW_UINT32 windowX3) {
            this.windowX3 = windowX3; return this;
        }

        SkewDetectionInfo setWindowX4(TW_UINT32 windowX4) {
            this.windowX4 = windowX4; return this;
        }

        SkewDetectionInfo setWindowY1(TW_UINT32 windowY1) {
            this.windowY1 = windowY1; return this;
        }

        SkewDetectionInfo setWindowY2(TW_UINT32 windowY2) {
            this.windowY2 = windowY2; return this;
        }

        SkewDetectionInfo setWindowY3(TW_UINT32 windowY3) {
            this.windowY3 = windowY3; return this;
        }

        SkewDetectionInfo setWindowY4(TW_UINT32 windowY4) {
            this.windowY4 = windowY4; return this;
        }

        public TW_UINT32 getDeskewStatus() {
            return deskewStatus;
        }

        public TW_UINT32 getOriginalAngle() {
            return originalAngle;
        }

        public TW_UINT32 getFinalAngle() {
            return finalAngle;
        }

        public TW_UINT32 getConfidence() {
            return confidence;
        }

        public TW_UINT32 getWindowX1() {
            return windowX1;
        }

        public TW_UINT32 getWindowX2() {
            return windowX2;
        }

        public TW_UINT32 getWindowX3() {
            return windowX3;
        }

        public TW_UINT32 getWindowX4() {
            return windowX4;
        }

        public TW_UINT32 getWindowY1() {
            return windowY1;
        }

        public TW_UINT32 getWindowY2() {
            return windowY2;
        }

        public TW_UINT32 getWindowY3() {
            return windowY3;
        }

        public TW_UINT32 getWindowY4() {
            return windowY4;
        }

    }
    public class EndorsedTextInfo
    {
        TW_STR255 text = new TW_STR255();
        public EndorsedTextInfo() {}
        private EndorsedTextInfo setText(TW_STR255 text)
        {
            this.text = text;
            return this;
        }

        public String getText()
        {
            return this.text.getValue();
        }
    }
    public class FormsRecognitionInfo
    {
        List<TW_UINT32> confidence = new ArrayList<>();
        List<TW_STR255> templateMatch = new ArrayList<>();
        List<TW_UINT32> templatePageMatch = new ArrayList<>();
        List<TW_UINT32> horizontalDocOffset = new ArrayList<>();
        List<TW_UINT32> verticalDocOffset = new ArrayList<>();
        public FormsRecognitionInfo() {}

        private FormsRecognitionInfo setConfidence(List<TW_UINT32> confidence) {
            this.confidence = confidence; return this;
        }
        private FormsRecognitionInfo setTemplateMatch(List<TW_STR255> templateMatch) {
            this.templateMatch = templateMatch; return this;
        }
        private FormsRecognitionInfo setTemplatePageMatch(List<TW_UINT32> templatePageMatch) {
            this.templatePageMatch = templatePageMatch; return this;
        }
        private FormsRecognitionInfo setHorizontalDocOffset(List<TW_UINT32> horizontalDocOffset) {
            this.horizontalDocOffset = horizontalDocOffset; return this;
        }
        private FormsRecognitionInfo setVerticalDocOffset(List<TW_UINT32> verticalDocOffset) {
            this.verticalDocOffset = verticalDocOffset; return this;
        }

        public List<TW_UINT32> getConfidence() {
            return confidence;
        }

        public List<TW_STR255> getTemplateMatch() {
            return templateMatch;
        }

        public List<TW_UINT32> getTemplatePageMatch() {
            return templatePageMatch;
        }

        public List<TW_UINT32> getHorizontalDocOffset() {
            return horizontalDocOffset;
        }

        public List<TW_UINT32> getVerticalDocOffset() {
            return verticalDocOffset;
        }
    }
    public class PageSourceInfo
    {
        TW_STR255 bookname = new TW_STR255();
        TW_UINT32 chapterNumber = new TW_UINT32();
        TW_UINT32 documentNumber = new TW_UINT32();
        TW_UINT32 pageNumber = new TW_UINT32();
        TW_STR255 camera    = new TW_STR255();
        TW_UINT32 frameNumber = new TW_UINT32();
        TW_FRAME frame  = new TW_FRAME();
        TW_UINT16 pageside = new TW_UINT16();
        TW_UINT16 pixelFlavor = new TW_UINT16();

        public PageSourceInfo() {}

        private PageSourceInfo setPageSide(TW_UINT16 pageSide) {
            this.pageside = pageSide;
            return this;
        }
        
        private PageSourceInfo setBookname(TW_STR255 bookname) {
            this.bookname = bookname; return this;
        }
        private PageSourceInfo setChapterNumber(TW_UINT32 chapterNumber) {
            this.chapterNumber = chapterNumber; return this;
        }
        private PageSourceInfo setDocumentNumber(TW_UINT32 documentNumber) {
            this.documentNumber = documentNumber; return this;
        }
        private PageSourceInfo setPageNumber(TW_UINT32 pageNumber) {
            this.pageNumber = pageNumber; return this;
        }
        private PageSourceInfo setCamera(TW_STR255 camera) {
            this.camera = camera; return this;
        }
        private PageSourceInfo setFrameNumber(TW_UINT32 frameNumber) {
            this.frameNumber = frameNumber; return this;
        }
        private PageSourceInfo setFrame(TW_FRAME frame) {
            this.frame = frame; return this;
        }
        private PageSourceInfo setPixelFlavor(TW_UINT16 pixelFlavor) {
            this.pixelFlavor = pixelFlavor; return this;
        }

        public TW_UINT16 getPageSide()
        {
            return this.pageside;
        }
        
        public String getPageSideName(TwainSession session) throws DTwainJavaAPIException 
        {
            return
            session.getAPIHandle().DTWAIN_GetTwainNameFromConstant(DTwainConstants.DTwainConstantToString.DTWAIN_CONSTANT_TWCS,(int)pageside.getValue());
        }
        public String getBookname() {
            return bookname.getValue();
        }

        public TW_UINT32 getChapterNumber() {
            return chapterNumber;
        }

        public TW_UINT32 getDocumentNumber() {
            return documentNumber;
        }

        public TW_UINT32 getPageNumber() {
            return pageNumber;
        }

        public String getCamera() {
            return camera.getValue();
        }

        public TW_UINT32 getFrameNumber() {
            return frameNumber;
        }

        public TW_FRAME getFrame() {
            return frame;
        }

        public TW_UINT16 getPixelFlavor() {
            return pixelFlavor;
        }
    }
    public class ImageSegmentationInfo
    {
        TW_STR255 ICCProfile = new TW_STR255();
        TW_BOOL lastSegment = new TW_BOOL();
        TW_UINT32 segmentNumber = new TW_UINT32();

        public ImageSegmentationInfo() {}

        private ImageSegmentationInfo setICCProfile(TW_STR255 iCCProfile) {
            ICCProfile = iCCProfile; return this;
        }

        private ImageSegmentationInfo setLastSegment(TW_BOOL lastSegment) {
            this.lastSegment = lastSegment; return this;
        }

        private ImageSegmentationInfo setSegmentNumber(TW_UINT32 segmentNumber) {
            this.segmentNumber = segmentNumber; return this;
        }

        public String getICCProfile() {
            return ICCProfile.getValue();
        }

        public TW_BOOL isLastSegment() {
            return lastSegment;
        }

        public TW_UINT32 getSegmentNumber() {
            return segmentNumber;
        }
    }
    public class ExtendedImageInfo20
    {
        TW_UINT16 magtype = new TW_UINT16();

        public ExtendedImageInfo20() {}

        private ExtendedImageInfo20 setMagType(TW_UINT16 magtype) {
            this.magtype = magtype; return this;
        }

        public TW_UINT16 getMagType() {
            return magtype;
        }
    }
    
    public class ExtendedImageInfo21
    {
        TW_STR255 fileSystemSource = new TW_STR255();
        TW_BOOL   imageMerged = new TW_BOOL();
        byte [] magData = new byte [0];
        TW_UINT32 magDataLength = new TW_UINT32();
        TW_UINT16 pageSide = new TW_UINT16();
        
        public ExtendedImageInfo21() {}
        
        private ExtendedImageInfo21 setFileSystemSource(TW_STR255 fileSystemSource)
        {
            this.fileSystemSource = fileSystemSource;
            return this;
        }
        
        private ExtendedImageInfo21 setMagData(byte [] magData)
        {
            this.magData = magData;
            return this;
        }
        
        private ExtendedImageInfo21 setImageMerged(TW_BOOL imageMerged)
        {
            this.imageMerged = imageMerged;
            return this;
        }
        
        private ExtendedImageInfo21 setMagDataLength(TW_UINT32 magDataLength)
        {
            this.magDataLength = magDataLength;
            return this;
        }
        
        private ExtendedImageInfo21 setPageSide(TW_UINT16 pageSide)
        {
            this.pageSide = pageSide;
            return this;
        }
        
        public String getFileSystemSource()
        {
            return this.fileSystemSource.getValue();
        }
        
        public byte [] getMagData()
        {
            return this.magData;
        }
        
        public TW_BOOL getImageMerged()
        {
            return this.imageMerged;
        }
        
        public TW_UINT32 getMagDataLength()
        {
            return this.magDataLength;
        }
        
        public TW_UINT16 getPageSide()
        {
            return this.pageSide;
        }
    }

    public class ExtendedImageInfo22
    {
        TW_UINT32 paperCount = new TW_UINT32();
        public ExtendedImageInfo22() {}
        
        private ExtendedImageInfo22 setPaperCount(TW_UINT32 paperCount) 
        {
            this.paperCount = paperCount;
            return this;
        }
        
        public TW_UINT32 getPaperCount()
        {
            return this.paperCount;
        }
    }

    public class ExtendedImageInfo23
    {
        TW_STR255 printerText = new TW_STR255();
        public ExtendedImageInfo23() {}
        private ExtendedImageInfo23 setPrinterText(TW_STR255 printerText) 
        {
            this.printerText = printerText;
            return this;
        }
        
        public String getPrinterText()
        {
            return this.printerText.getValue();
        }
    }

    public class ExtendedImageInfo24
    {
        String twainDirectMetaData = new String();
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
        private TW_STR32[] iAField = new TW_STR32[] 
                {new TW_STR32(), new TW_STR32(), new TW_STR32(), new TW_STR32(), new TW_STR32()};
        private TW_UINT16 iALevel = new TW_UINT16();
        private TW_UINT16 printer = new TW_UINT16();
        private List<String> barcodeText = new ArrayList<>();
        
        private void setIAField(TW_STR32 val, int nWhich)
        {
            iAField[nWhich] = val;
        }
        private String getIAField(int nWhich)
        {
            return iAField[nWhich].getValue();
        }
        private ExtendedImageInfo25 setIAFieldValueA(TW_STR32 val)
        {
            setIAField(val, 0);
            return this;
        }
        private ExtendedImageInfo25 setIAFieldValueB(TW_STR32 val)
        {
            setIAField(val, 1);
            return this;
        }
        private ExtendedImageInfo25 setIAFieldValueC(TW_STR32 val)
        {
            setIAField(val, 2);
            return this;
        }
        private ExtendedImageInfo25 setIAFieldValueD(TW_STR32 val)
        {
            setIAField(val, 3);
            return this;
        }
        private ExtendedImageInfo25 setIAFieldValueE(TW_STR32 val)
        {
            setIAField(val, 4);
            return this;
        }
        private ExtendedImageInfo25 setIALevel(TW_UINT16 val)
        {
            this.iALevel = val;
            return this;
        }
        private ExtendedImageInfo25 setPrinter(TW_UINT16 val)
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
        public TW_UINT16 getIALevel()
        {
            return this.iALevel;
        }
        public TW_UINT16 getPrinter()
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

    int [] supportedExtImageInfo = new int[0];
    BarcodeInfo barcodeInfo = new BarcodeInfo();
    ShadedAreaDetectionInfo shadedAreaDetectionInfo = new ShadedAreaDetectionInfo();
    SpeckleRemovalInfo speckleRemovalInfo = new SpeckleRemovalInfo();
    HorizontalLineDetectionInfo horizontalLineDetectionInfo = new HorizontalLineDetectionInfo();
    VerticalLineDetectionInfo verticalLineDetectionInfo = new VerticalLineDetectionInfo();
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

    private ExtendedImageInfo setHorizontalLineDetectionInfo(HorizontalLineDetectionInfo horizontalLineDetectionInfo) {
        this.horizontalLineDetectionInfo = horizontalLineDetectionInfo; return this;
    }

    private ExtendedImageInfo setVerticalLineDetectionInfo(VerticalLineDetectionInfo verticalLineDetectionInfo) {
        this.verticalLineDetectionInfo = verticalLineDetectionInfo; return this;
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

    public HorizontalLineDetectionInfo getHorizontalLineDetectionInfo() {
        return horizontalLineDetectionInfo;
    }

    public VerticalLineDetectionInfo getVerticalLineDetectionInfo() {
        return verticalLineDetectionInfo;
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
