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
#ifndef EXTENDEDIMAGEINFO_TYPES_H
#define EXTENDEDIMAGEINFO_TYPES_H
#include <windows.h>
#include <twain.h>
#include <vector>

struct ExtendedImageInfo_BarcodeInfoNative
{
    TW_UINT32 confidence = 0;
    TW_UINT32 rotation = 0;
    TW_UINT32 length = 0;
    TW_UINT32 xCoordinate = 0;
    TW_UINT32 yCoordinate = 0;
    TW_UINT32 type = 0;
    TW_STR255 text{};
};

struct ExtendedImageInfo_BarcodeNative
{
    std::vector<ExtendedImageInfo_BarcodeInfoNative> m_vBarInfos;
};

struct ExtendedImageInfo_PageSourceInfoNative
{
    TW_STR255 bookname{};
    TW_UINT32 chapterNumber = 0;
    TW_UINT32 documentNumber = 0;
    TW_UINT32 pageNumber = 0;
    TW_STR255 camera{};
    TW_UINT32 frameNumber = 0;
    TW_FRAME frame{};
    TW_UINT16 pixelFlavor = 0;
    TW_UINT16 pageSide = 0;
};

struct ExtendedImageInfo_SkewDetectionInfoNative
{
    TW_UINT32 DeskewStatus{};
    TW_UINT32 OriginalAngle{};
    TW_UINT32 FinalAngle{};
    TW_UINT32 Confidence{};
    TW_UINT32 WindowX1{};
    TW_UINT32 WindowX2{};
    TW_UINT32 WindowX3{};
    TW_UINT32 WindowX4{};
    TW_UINT32 WindowY1{};
    TW_UINT32 WindowY2{};
    TW_UINT32 WindowY3{};
    TW_UINT32 WindowY4{};
};

struct ExtendedImageInfo_ShadedAreaDetectionInfoNative
{
    TW_UINT32 top = 0;
    TW_UINT32 left = 0;
    TW_UINT32 height = 0;
    TW_UINT32 width = 0;
    TW_UINT32 size = 0;
    TW_UINT32 blackCountOld = 0;
    TW_UINT32 blackCountNew = 0;
    TW_UINT32 blackRLMin = 0;
    TW_UINT32 blackRLMax = 0;
    TW_UINT32 whiteCountOld = 0;
    TW_UINT32 whiteCountNew = 0;
    TW_UINT32 whiteRLMin = 0;
    TW_UINT32 whiteRLMax = 0;
    TW_UINT32 whiteRLAvg = 0;
};

struct ExtendedImageInfo_ShadedAreaDetectionInfoNativeV
{
    TW_UINT32 count = 0;
    std::vector<TW_UINT32> topV;
    std::vector<TW_UINT32> leftV;
    std::vector<TW_UINT32> heightV;
    std::vector<TW_UINT32> widthV;
    std::vector<TW_UINT32> sizeV;
    std::vector<TW_UINT32> blackCountOldV;
    std::vector<TW_UINT32> blackCountNewV;
    std::vector<TW_UINT32> blackRLMinV;
    std::vector<TW_UINT32> blackRLMaxV;
    std::vector<TW_UINT32> whiteCountOldV;
    std::vector<TW_UINT32> whiteCountNewV;
    std::vector<TW_UINT32> whiteRLMinV;
    std::vector<TW_UINT32> whiteRLMaxV;
    std::vector<TW_UINT32> whiteRLAvgV;
};

struct ExtendedImageInfo_ShadedAreaDectionNative
{
    std::vector<ExtendedImageInfo_ShadedAreaDetectionInfoNative> m_vShadeInfos;
};

struct ExtendedImageInfo_SpeckleRemovalInfoNative
{
    TW_UINT32 specklesRemoved = 0;
    TW_UINT32 whiteSpecklesRemoved = 0;
    TW_UINT32 blackSpecklesRemoved = 0;
};

struct ExtendedImageInfo_LineDetectionInfoNative
{
    TW_UINT32 xCoordinate = 0;
    TW_UINT32 yCoordinate = 0;
    TW_UINT32 length = 0;
    TW_UINT32 thickness = 0;
};

struct ExtendedImageInfo_LineDetectionNative
{
    std::vector<ExtendedImageInfo_LineDetectionInfoNative> m_vLineInfo;
};

struct ExtendedImageInfo_FormsRecognitionNative
{
    std::vector<TW_UINT32> m_vConfidence;
    std::vector<std::string> m_vTemplateMatch;
    std::vector<TW_UINT32> m_vTemplatePageMatch;
    std::vector<TW_UINT32> m_vHorizontalDocOffset;
    std::vector<TW_UINT32> m_vVerticalDocOffset; 
};

struct ExtendedImageInfo_ImageSegmentationInfoNative
{
    TW_STR255 m_sICCProfile = {};
    TW_BOOL m_bLastSegment = {};
    TW_UINT32 m_segmentNumber = {};
};

struct ExtendedImageInfo_EndorsedTextInfoNative
{
    TW_STR255 m_sEndorsedText = {};
};

struct ExtendedImageInfo_ExtendedImageInfo20Native
{
    TW_UINT16 m_magType = {};
};

struct ExtendedImageInfo_ExtendedImageInfo21Native
{
    TW_STR255 m_fileSystemSource = {};
    TW_BOOL m_imageMerged = {};
    std::vector<char> m_magData;
    TW_UINT32 m_magDataLength = {};
    TW_UINT16 m_pageSide = {};
};

struct ExtendedImageInfo_ExtendedImageInfo22Native
{
    TW_UINT32 m_PaperCount = {};
};

struct ExtendedImageInfo_ExtendedImageInfo23Native
{
    TW_STR255 m_PrinterText = {};
};

struct ExtendedImageInfo_ExtendedImageInfo24Native
{
    std::string m_twainDirectMetaData;
};

struct ExtendedImageInfo_ExtendedImageInfo25Native
{
    struct ImageAddressing
    {
        std::array<TW_STR32, 5> m_AddressInfo = {};
        TW_UINT16 m_iaLevel = {};
    };
    TW_UINT16 m_printer = {};
    std::vector<std::string> m_barcodeText;
    ImageAddressing m_ImageAddressing;
};

struct ExtendedImageInfo_PatchCodeNative
{
    TW_UINT32 m_patchCode = {};
};
#endif
