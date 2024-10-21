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
#include <array>
#include <algorithm>
#include <iterator>
#include "ExtendedImageInfo.h"
#include "DTWAINRAII.h"

#ifdef USING_DTWAIN_LOADLIBRARY
    #ifndef API_INSTANCE
        #define API_INSTANCE DYNDTWAIN_API::
    #endif
#else
    #ifndef API_INSTANCE
        #define API_INSTANCE
    #endif
#endif

ExtendedImageInformation::ExtendedImageInformation(DTWAIN_SOURCE theSource) : m_theSource(theSource) 
{
    // Make sure we started the engine
    auto retValue = API_INSTANCE DTWAIN_InitExtImageInfo(m_theSource);
    if (retValue)
        API_INSTANCE DTWAIN_GetExtImageInfo(m_theSource);
    infoRetrieved = retValue ? true : false;
}

bool ExtendedImageInformation::FillBarcodeInfo()
{
    if (!infoRetrieved)
        return false;

    DTWAIN_ARRAY aCountX = {}, aCountY = {}, aType = {}, aText = {}, aRotation = {}, aConfidence = {}, aTextLength = {}, aCount{};
    std::array<DTWAINArray_RAII, 8> aVects = { aCountX, aCountY, aType, aText, aRotation, aConfidence, aTextLength, aCount };

    // Get the barcode count information
    LONG barCodeCount = 0;
    API_INSTANCE DTWAIN_GetExtImageInfoData(m_theSource, TWEI_BARCODECOUNT, &aCount);
    LONG nCount = API_INSTANCE DTWAIN_ArrayGetCount(aCount);
    if (nCount == 0)
        return true;
    bool retVal = API_INSTANCE DTWAIN_ArrayGetAtLong(aCount, 0, &barCodeCount);
    if (!retVal || barCodeCount == 0)
        return true;

    m_barcodeInfo.m_vBarInfos.resize(barCodeCount);

    // Get the barcode details
    API_INSTANCE DTWAIN_GetExtImageInfoData(m_theSource, TWEI_BARCODEX, &aCountX);
    API_INSTANCE DTWAIN_GetExtImageInfoData(m_theSource, TWEI_BARCODEY, &aCountY);
    API_INSTANCE DTWAIN_GetExtImageInfoData(m_theSource, TWEI_BARCODETYPE, &aType);
    API_INSTANCE DTWAIN_GetExtImageInfoData(m_theSource, TWEI_BARCODETEXT, &aText);
    API_INSTANCE DTWAIN_GetExtImageInfoData(m_theSource, TWEI_BARCODEROTATION, &aRotation);
    API_INSTANCE DTWAIN_GetExtImageInfoData(m_theSource, TWEI_BARCODECONFIDENCE, &aConfidence);
    API_INSTANCE DTWAIN_GetExtImageInfoData(m_theSource, TWEI_BARCODETEXTLENGTH, &aTextLength);

    int lastLen = 0;

    // Fill in the barcode texts
    for (int i = 0; i < barCodeCount; ++i)
    {
        char szBarText[256];
        DTWAIN_HANDLE sHandle;
        LONG length = 0;
        API_INSTANCE DTWAIN_ArrayGetAt(aText, i, &sHandle);
        API_INSTANCE DTWAIN_ArrayGetAtLong(aTextLength, i, &length);
        HandleRAII raii(sHandle);
        char* pText = (char *)raii.getData();
        if ( pText )
            memcpy(szBarText, pText + lastLen, length);
        szBarText[length] = 0;
        lastLen += length;
        strcpy(m_barcodeInfo.m_vBarInfos[i].text, szBarText);
    }

    // Fill in the other information
    std::array<LONG, 5> aCounts = { API_INSTANCE DTWAIN_ArrayGetCount(aCountX),
                                    API_INSTANCE DTWAIN_ArrayGetCount(aCountY),
                                    API_INSTANCE DTWAIN_ArrayGetCount(aType),
                                    API_INSTANCE DTWAIN_ArrayGetCount(aRotation),
                                    API_INSTANCE DTWAIN_ArrayGetCount(aConfidence) };

    std::array<std::vector<uint32_t>*, 5> aPtrVectors = { nullptr, nullptr, &m_vBarcodeType, &m_vBarcodeRotation, &m_vBarcodeConfidence };

    LONG* pBufferX = (LONG *)API_INSTANCE DTWAIN_ArrayGetBuffer(aCountX, 0);
    LONG* pBufferY = (LONG*)API_INSTANCE DTWAIN_ArrayGetBuffer(aCountY, 0);

    for (int i = 0; i < (std::min)(aCounts[0], aCounts[1]); ++i)
    {
        m_barcodeInfo.m_vBarInfos[i].xCoordinate = pBufferX[i];
        m_barcodeInfo.m_vBarInfos[i].yCoordinate = pBufferY[i];
    }

    LONG* pType = (LONG*)API_INSTANCE DTWAIN_ArrayGetBuffer(aType, 0);
    for (int i = 0; i < aCounts[2]; ++i)
        m_barcodeInfo.m_vBarInfos[i].type = pType[i];

    LONG* pRotation = (LONG*)API_INSTANCE DTWAIN_ArrayGetBuffer(aRotation, 0);
    for (int i = 0; i < aCounts[3]; ++i)
        m_barcodeInfo.m_vBarInfos[i].rotation = pRotation[i];

    LONG* pConfidence = (LONG*)API_INSTANCE DTWAIN_ArrayGetBuffer(aConfidence, 0);
    for (int i = 0; i < aCounts[3]; ++i)
        m_barcodeInfo.m_vBarInfos[i].confidence = pConfidence[i];

    return true;
}

bool ExtendedImageInformation::FillPageSourceInfo()
{
    if (!infoRetrieved)
        return false;
    
    DTWAIN_ARRAY aValues = {};
    DTWAINArray_RAII raii(aValues);
    std::array<int32_t, 2> stringItems = { TWEI_CAMERA, TWEI_BOOKNAME };
    std::array<TW_STR255*, 2> refStrings = { &m_pageSource.camera, &m_pageSource.bookname};

    for (size_t i = 0; i < stringItems.size(); ++i)
    {
        auto Ret = API_INSTANCE DTWAIN_GetExtImageInfoData(m_theSource, stringItems[i], &aValues);
        int nItems = API_INSTANCE DTWAIN_ArrayGetCount(aValues);
        if (nItems > 0)
            API_INSTANCE DTWAIN_ArrayGetAtANSIString(aValues, 0, *(refStrings[i]));
    }

    {
        std::array<int32_t, 4> intItems = { TWEI_CHAPTERNUMBER, TWEI_DOCUMENTNUMBER, TWEI_PAGENUMBER, TWEI_FRAMENUMBER };
        std::array<TW_UINT32*, 4> refInts = { &m_pageSource.chapterNumber, &m_pageSource.documentNumber, &m_pageSource.pageNumber, &m_pageSource.frameNumber };

        for (size_t i = 0; i < intItems.size(); ++i)
        {
            auto Ret = API_INSTANCE DTWAIN_GetExtImageInfoData(m_theSource, intItems[i], &aValues);
            int nItems = API_INSTANCE DTWAIN_ArrayGetCount(aValues);
            if (nItems > 0)
                API_INSTANCE DTWAIN_ArrayGetAtLong(aValues, 0, (LONG*)refInts[i]);
        }
    }

    {
        std::array<int32_t, 2> intItems = { TWEI_PAGESIDE, TWEI_PIXELFLAVOR };
        std::array<TW_UINT16*, 2> refInts = { &m_pageSource.pageSide, &m_pageSource.pixelFlavor };

        for (size_t i = 0; i < intItems.size(); ++i)
        {
            auto Ret = API_INSTANCE DTWAIN_GetExtImageInfoData(m_theSource, intItems[i], &aValues);
            int nItems = API_INSTANCE DTWAIN_ArrayGetCount(aValues);
            if (nItems > 0)
            {
                LONG lVal;
                API_INSTANCE DTWAIN_ArrayGetAtLong(aValues, 0, &lVal);
                *(refInts[i]) = lVal;
            }
        }
    }

    // Get the frame
    API_INSTANCE DTWAIN_GetExtImageInfoData(m_theSource, TWEI_FRAME, &aValues);
    int nItems = API_INSTANCE DTWAIN_ArrayGetCount(aValues); // This is a DTWAIN_FRAME
    if (nItems == 1) // There must be 1 item describing the frame
    {
        // Convert to TW_FIX32.  The resulting DTWAIN_ARRAY will consist of 4 TW_FIX32 values
        // describing left, top, right, and bottom of the frame
        DTWAIN_ARRAY aFix32 = API_INSTANCE DTWAIN_ArrayConvertFloatToFix32(aValues);
        DTWAINArray_RAII raii2(aFix32);
        LONG Whole, Frac;
        API_INSTANCE DTWAIN_ArrayFix32GetAt(aFix32, 0, &Whole, &Frac);
        m_pageSource.frame.Left.Whole = Whole;
        m_pageSource.frame.Left.Frac = Frac;
        API_INSTANCE DTWAIN_ArrayFix32GetAt(aFix32, 1, &Whole, &Frac);
        m_pageSource.frame.Top.Whole = Whole;
        m_pageSource.frame.Top.Frac = Frac;
        API_INSTANCE DTWAIN_ArrayFix32GetAt(aFix32, 2, &Whole, &Frac);
        m_pageSource.frame.Right.Whole = Whole;
        m_pageSource.frame.Right.Frac = Frac;
        API_INSTANCE DTWAIN_ArrayFix32GetAt(aFix32, 3, &Whole, &Frac);
        m_pageSource.frame.Bottom.Whole = Whole;
        m_pageSource.frame.Bottom.Frac = Frac;
    }
    return true;
}

bool ExtendedImageInformation::FillSkewInfo()
{
    if (!infoRetrieved)
        return false;

    DTWAIN_ARRAY aValues = {};
    DTWAINArray_RAII raii(aValues);
    std::array<int32_t, 12> intItems = { TWEI_DESKEWSTATUS, TWEI_SKEWORIGINALANGLE,
                                        TWEI_SKEWFINALANGLE, TWEI_SKEWCONFIDENCE,
                                        TWEI_SKEWWINDOWX1, TWEI_SKEWWINDOWY1,
                                        TWEI_SKEWWINDOWX2, TWEI_SKEWWINDOWY2,
                                        TWEI_SKEWWINDOWX3, TWEI_SKEWWINDOWY3,
                                        TWEI_SKEWWINDOWX4, TWEI_SKEWWINDOWY4 };

    std::array<TW_UINT32*, 12> refInts   = {&m_skewDetection.DeskewStatus, &m_skewDetection.OriginalAngle,
                                            &m_skewDetection.FinalAngle, &m_skewDetection.Confidence,
                                            &m_skewDetection.WindowX1, &m_skewDetection.WindowY1,
                                            &m_skewDetection.WindowX2, &m_skewDetection.WindowY2,
                                            &m_skewDetection.WindowX3, &m_skewDetection.WindowY3,
                                            &m_skewDetection.WindowX4, &m_skewDetection.WindowY4 };

    for (size_t i = 0; i < intItems.size(); ++i)
    {
        auto Ret = API_INSTANCE DTWAIN_GetExtImageInfoData(m_theSource, intItems[i], &aValues);
        int nItems = API_INSTANCE DTWAIN_ArrayGetCount(aValues);
        if (nItems > 0)
            API_INSTANCE DTWAIN_ArrayGetAtLong(aValues, 0, (LONG *)refInts[i]);
    }
    return true;
}

bool ExtendedImageInformation::FillShadedAreaInfo()
{
    if (!infoRetrieved)
        return false;

    DTWAIN_ARRAY aValues = {};
    DTWAINArray_RAII raii(aValues);

    // Get the count information
    LONG shadeCount = 0;
    API_INSTANCE DTWAIN_GetExtImageInfoData(m_theSource, TWEI_DESHADECOUNT, &aValues);
    LONG nCount = API_INSTANCE DTWAIN_ArrayGetCount(aValues);
    if (nCount == 0)
        return true;
    bool retVal = API_INSTANCE DTWAIN_ArrayGetAtLong(aValues, 0, &shadeCount);
    if (!retVal || shadeCount == 0)
        return true;

    m_shadedInfo.count = shadeCount;

    std::array<int32_t, 14> intItems = {TWEI_DESHADELEFT,
                                        TWEI_DESHADETOP,
                                        TWEI_DESHADEWIDTH,
                                        TWEI_DESHADEHEIGHT,
                                        TWEI_DESHADESIZE,
                                        TWEI_DESHADEBLACKCOUNTOLD,
                                        TWEI_DESHADEBLACKCOUNTNEW,
                                        TWEI_DESHADEBLACKRLMIN,
                                        TWEI_DESHADEBLACKRLMAX,
                                        TWEI_DESHADEWHITECOUNTOLD,
                                        TWEI_DESHADEWHITECOUNTNEW,
                                        TWEI_DESHADEWHITERLMIN,
                                        TWEI_DESHADEWHITERLMAX,
                                        TWEI_DESHADEWHITERLAVE };

    std::array<std::vector<TW_UINT32>*, 14> ptrVect = { &m_shadedInfo.leftV,
                                                        &m_shadedInfo.topV,
                                                        &m_shadedInfo.widthV,
                                                        &m_shadedInfo.heightV,
                                                        &m_shadedInfo.sizeV,
                                                        &m_shadedInfo.blackCountOldV,
                                                        &m_shadedInfo.blackCountNewV,
                                                        &m_shadedInfo.blackRLMinV,
                                                        &m_shadedInfo.blackRLMaxV,
                                                        &m_shadedInfo.whiteCountOldV,
                                                        &m_shadedInfo.whiteCountNewV,
                                                        &m_shadedInfo.whiteRLMinV,
                                                        &m_shadedInfo.whiteRLMaxV,
                                                        &m_shadedInfo.whiteRLAvgV };
    for (size_t i = 0; i < intItems.size(); ++i)
    {
        auto Ret = API_INSTANCE DTWAIN_GetExtImageInfoData(m_theSource, intItems[i], &aValues);
        auto count = API_INSTANCE DTWAIN_ArrayGetCount(aValues);
        for (LONG j = 0; j < count; ++j)
        {
            LONG lVal;
            API_INSTANCE DTWAIN_ArrayGetAtLong(aValues, j, &lVal);
            ptrVect[i]->push_back(lVal);
        }
    }
    std::for_each(ptrVect.begin(), ptrVect.end(), [&](auto* pVect) { pVect->resize(m_shadedInfo.count);  });
    return true;
}

bool ExtendedImageInformation::FillSpeckleRemovalInfo()
{
    if (!infoRetrieved)
        return false;

    DTWAIN_ARRAY aValues = {};
    DTWAINArray_RAII raii(aValues);

    std::array<int32_t, 3> intItems = { TWEI_SPECKLESREMOVED,
                                        TWEI_BLACKSPECKLESREMOVED,
                                        TWEI_WHITESPECKLESREMOVED };
    std::array<TW_UINT32*, 3> refInts = { &m_speckleRemoval.specklesRemoved,
                                          &m_speckleRemoval.blackSpecklesRemoved,
                                          &m_speckleRemoval.whiteSpecklesRemoved };
    for (size_t i = 0; i < intItems.size(); ++i)
    {
        auto Ret = API_INSTANCE DTWAIN_GetExtImageInfoData(m_theSource, intItems[i], &aValues);
        auto count = API_INSTANCE DTWAIN_ArrayGetCount(aValues);
        if (count > 0)
        {
            LONG lVal;
            API_INSTANCE DTWAIN_ArrayGetAtLong(aValues, 0, &lVal);
            *(refInts[i]) = lVal;
        }
    }
    return true;
}

bool ExtendedImageInformation::GenericFillLineInfo(ExtendedImageInfo_LineDetectionNative& allInfo,
                                                    int32_t itemCountType, const std::array<int32_t, 4>& intItems)
{
    if (!infoRetrieved)
        return false;

    DTWAIN_ARRAY aValues = {};
    DTWAINArray_RAII raii(aValues);

    // Get the count information
    LONG horizCount = 0;
    API_INSTANCE DTWAIN_GetExtImageInfoData(m_theSource, itemCountType, &aValues);
    LONG nCount = API_INSTANCE DTWAIN_ArrayGetCount(aValues);
    if (nCount == 0)
        return true;
    bool retVal = API_INSTANCE DTWAIN_ArrayGetAtLong(aValues, 0, &horizCount);
    if (!retVal || horizCount == 0)
        return true;

    m_horizontalLineInfo.m_vLineInfo.resize(nCount);

    std::array<DTWAIN_ARRAY, 4> aAllValues;
    std::array<DTWAINArray_RAII, 4> aVects = { aAllValues[0], aAllValues[1], aAllValues[2], aAllValues[3] };
    std::array<LONG, 4> allCounts{};
    for (size_t i = 0; i < intItems.size(); ++i)
    {
        auto Ret = API_INSTANCE DTWAIN_GetExtImageInfoData(m_theSource, intItems[i], &aAllValues[i]);
        allCounts[i] = API_INSTANCE DTWAIN_ArrayGetCount(aAllValues[i]);
    }
    LONG maxCount = *std::min_element(allCounts.begin(), allCounts.end());
    for (LONG i = 0; i < maxCount; ++i)
    {
        ExtendedImageInfo_LineDetectionInfoNative oneLine{};
        LONG lVal;
        API_INSTANCE DTWAIN_ArrayGetAtLong(aAllValues[0], i, &lVal);
        oneLine.xCoordinate = lVal;
        API_INSTANCE DTWAIN_ArrayGetAtLong(aAllValues[1], i, &lVal);
        oneLine.yCoordinate = lVal;
        API_INSTANCE DTWAIN_ArrayGetAtLong(aAllValues[2], i, &lVal);
        oneLine.length = lVal;
        API_INSTANCE DTWAIN_ArrayGetAtLong(aAllValues[3], i, &lVal);
        oneLine.thickness = lVal;
        allInfo.m_vLineInfo.push_back(oneLine);
    }
    return true;
}

bool ExtendedImageInformation::FillHorizontalLineInfo()
{
    return GenericFillLineInfo(m_horizontalLineInfo, TWEI_HORZLINECOUNT, { TWEI_HORZLINEXCOORD,
                                                                           TWEI_HORZLINEYCOORD,
                                                                           TWEI_HORZLINELENGTH,
                                                                           TWEI_HORZLINETHICKNESS });
}

bool ExtendedImageInformation::FillVerticalLineInfo()
{
    return GenericFillLineInfo(m_verticalLineInfo, TWEI_VERTLINECOUNT, { TWEI_VERTLINEXCOORD,
                                                                           TWEI_VERTLINEYCOORD,
                                                                           TWEI_VERTLINELENGTH,
                                                                           TWEI_VERTLINETHICKNESS });
}

bool ExtendedImageInformation::FillFormsRecognitionInfo()
{
    if (!infoRetrieved)
        return false;

    DTWAIN_ARRAY aValues = {};
    DTWAINArray_RAII raii(aValues);

    // Get the template match information
    API_INSTANCE DTWAIN_GetExtImageInfoData(m_theSource, TWEI_FORMTEMPLATEMATCH, &aValues);
    LONG nCount = API_INSTANCE DTWAIN_ArrayGetCount(aValues);
    if (nCount > 0)
    {
        for (LONG i = 0; i < nCount; ++i)
        {
            char sz[256] = {};
            API_INSTANCE DTWAIN_ArrayGetAtANSIString(aValues, i, sz);
            m_formsRecognitionInfo.m_vTemplateMatch.push_back(sz);
        }
    }

    std::array<int32_t, 4> intTypes = { TWEI_FORMCONFIDENCE, TWEI_FORMTEMPLATEPAGEMATCH, TWEI_FORMHORZDOCOFFSET, TWEI_FORMVERTDOCOFFSET };
    std::array<std::vector<TW_UINT32>*, 4> ptrVects = { &m_formsRecognitionInfo.m_vConfidence, &m_formsRecognitionInfo.m_vTemplatePageMatch,
                                                        &m_formsRecognitionInfo.m_vHorizontalDocOffset, &m_formsRecognitionInfo.m_vVerticalDocOffset };
    for (size_t i = 0; i < intTypes.size(); ++i)
    {
        API_INSTANCE DTWAIN_GetExtImageInfoData(m_theSource, intTypes[i], &aValues);
        LONG nCount = API_INSTANCE DTWAIN_ArrayGetCount(aValues);
        if (nCount > 0)
        {
            LONG* pBuffer = (LONG*)API_INSTANCE DTWAIN_ArrayGetBuffer(aValues, 0);
            std::transform(pBuffer, pBuffer + nCount, std::back_inserter(*(ptrVects[i])), [&](LONG val) { return static_cast<TW_UINT32>(val); });
        }
    }
    return true;
}

bool ExtendedImageInformation::FillImageSegmentationInfo()
{
    if (!infoRetrieved)
        return false;

    DTWAIN_ARRAY aValues = {};
    DTWAINArray_RAII raii(aValues);

    API_INSTANCE DTWAIN_GetExtImageInfoData(m_theSource, TWEI_ICCPROFILE, &aValues);
    LONG nCount = API_INSTANCE DTWAIN_ArrayGetCount(aValues);
    if (nCount > 0)
        API_INSTANCE DTWAIN_ArrayGetAtANSIString(m_theSource, 0, m_imageSementationInfo.m_sICCProfile);

    API_INSTANCE DTWAIN_GetExtImageInfoData(m_theSource, TWEI_LASTSEGMENT, &aValues);
    nCount = API_INSTANCE DTWAIN_ArrayGetCount(aValues);
    if (nCount > 0)
    {
        LONG lVal = 0;
        API_INSTANCE DTWAIN_ArrayGetAtLong(aValues, 0, &lVal);
        m_imageSementationInfo.m_bLastSegment = lVal;
    }

    API_INSTANCE DTWAIN_GetExtImageInfoData(m_theSource, TWEI_SEGMENTNUMBER, &aValues);
    nCount = API_INSTANCE DTWAIN_ArrayGetCount(aValues);
    if (nCount > 0)
    {
        LONG lVal = 0;
        API_INSTANCE DTWAIN_ArrayGetAtLong(aValues, 0, &lVal);
        m_imageSementationInfo.m_segmentNumber = lVal;
    }
    return true;
}

bool ExtendedImageInformation::FillEndorsedTextInfo()
{
    if (!infoRetrieved)
        return false;

    DTWAIN_ARRAY aValues = {};
    DTWAINArray_RAII raii(aValues);
    API_INSTANCE DTWAIN_GetExtImageInfoData(m_theSource, TWEI_ENDORSEDTEXT, &aValues);
    LONG nCount = API_INSTANCE DTWAIN_ArrayGetCount(aValues);
    if (nCount > 0)
        API_INSTANCE DTWAIN_ArrayGetAtANSIString(m_theSource, 0, m_endorsedTextInfo.m_sEndorsedText);
    return true;
}

bool ExtendedImageInformation::FillExtendedImageInfo21()
{
    if (!infoRetrieved)
        return false;
    DTWAIN_ARRAY aValues = {};
    DTWAINArray_RAII raii(aValues);

    API_INSTANCE DTWAIN_GetExtImageInfoData(m_theSource, TWEI_MAGDATALENGTH, &aValues);
    LONG nCount = API_INSTANCE DTWAIN_ArrayGetCount(aValues);
    LONG magDataLength = 0;
    if (nCount > 0)
    {
        API_INSTANCE DTWAIN_ArrayGetAtLong(aValues, 0, &magDataLength);

        // Get the mag data
        API_INSTANCE DTWAIN_GetExtImageInfoData(m_theSource, TWEI_MAGDATA, &aValues);
        LONG nCount = API_INSTANCE DTWAIN_ArrayGetCount(aValues);
        DTWAIN_HANDLE sHandle = NULL;
        if (nCount > 0)
        {
            // Need to determine the array type
            auto arrayType = API_INSTANCE DTWAIN_ArrayGetType(aValues);
            if (arrayType == DTWAIN_ARRAYANSISTRING)
            {
                // Copy the string to the vector
                TW_STR255 szData;
                API_INSTANCE DTWAIN_ArrayGetAtANSIString(aValues, 0, szData);
                m_extendedImageInfo21.m_magData = std::vector<char>(szData, szData + magDataLength);
            }
            else
            {
                // The data is a handle, so maybe GlobalLock it?
                LONG length;
                API_INSTANCE DTWAIN_ArrayGetAt(aValues, 0, &sHandle);
                HandleRAII raii(sHandle);
                char* pText = (char*)raii.getData();
                if (pText)
                    m_extendedImageInfo21.m_magData = std::vector<char>(pText, pText + magDataLength);
            }
            if (sHandle)
                GlobalFree(sHandle);
        }
    }
    API_INSTANCE DTWAIN_GetExtImageInfoData(m_theSource, TWEI_FILESYSTEMSOURCE, &aValues);
    nCount = API_INSTANCE DTWAIN_ArrayGetCount(aValues);
    if (nCount > 0)
        API_INSTANCE DTWAIN_ArrayGetAtANSIString(aValues, 0, m_extendedImageInfo21.m_fileSystemSource);
    API_INSTANCE DTWAIN_GetExtImageInfoData(m_theSource, TWEI_PAGESIDE, &aValues);
    nCount = API_INSTANCE DTWAIN_ArrayGetCount(aValues);
    if (nCount > 0)
    {
        LONG lVal = 0;
        API_INSTANCE DTWAIN_ArrayGetAtLong(aValues, 0, &lVal);
        m_extendedImageInfo21.m_pageSide = lVal;
    }
    API_INSTANCE DTWAIN_GetExtImageInfoData(m_theSource, TWEI_IMAGEMERGED, &aValues);
    nCount = API_INSTANCE DTWAIN_ArrayGetCount(aValues);
    if (nCount > 0)
    {
        LONG lVal = 0;
        API_INSTANCE DTWAIN_ArrayGetAtLong(aValues, 0, &lVal);
        m_extendedImageInfo21.m_imageMerged = lVal;
    }
    return true;
}

#if 0
    if (!infoRetrieved)
        return false;

    DTWAIN_ARRAY aValues = {};
    DTWAINArray_RAII raii(aValues);

    // Get the count information
    LONG horizCount = 0;
    API_INSTANCE DTWAIN_GetExtImageInfoData(m_theSource, TWEI_HORZLINECOUNT, &aValues);
    LONG nCount = API_INSTANCE DTWAIN_ArrayGetCount(aValues);
    if (nCount == 0)
        return true;
    bool retVal = API_INSTANCE DTWAIN_ArrayGetAtLong(aValues, 0, &horizCount);
    if (!retVal || horizCount == 0)
        return true;
    
    m_horizontalLineInfo.m_vLineInfo.resize(nCount);

    std::array<int32_t, 4> intItems = { TWEI_HORZLINEXCOORD,
                                        TWEI_HORZLINEYCOORD,
                                        TWEI_HORZLINELENGTH,
                                        TWEI_HORZLINETHICKNESS };

    std::array<DTWAIN_ARRAY, 4> aAllValues;
    std::array<DTWAINArray_RAII, 4> aVects = { aAllValues[0], aAllValues[1], aAllValues[2], aAllValues[3] };
    std::array<LONG, 4> allCounts {};
    for (size_t i = 0; i < intItems.size(); ++i)
    {
        auto Ret = API_INSTANCE DTWAIN_GetExtImageInfoData(m_theSource, intItems[i], &aAllValues[i]);
        allCounts[i] = API_INSTANCE DTWAIN_ArrayGetCount(aAllValues[i]);
    }
    LONG maxCount = *std::min_element(allCounts.begin(), allCounts.end());
    for (LONG i = 0; i < maxCount; ++i)
    {
        ExtendedImageInfo_LineDetectionInfoNative oneLine{};
        LONG lVal;
        API_INSTANCE DTWAIN_ArrayGetAtLong(aAllValues[0], i, &lVal);
        oneLine.xCoordinate = lVal;
        API_INSTANCE DTWAIN_ArrayGetAtLong(aAllValues[1], i, &lVal);
        oneLine.yCoordinate = lVal;
        API_INSTANCE DTWAIN_ArrayGetAtLong(aAllValues[2], i, &lVal);
        oneLine.length = lVal;
        API_INSTANCE DTWAIN_ArrayGetAtLong(aAllValues[3], i, &lVal);
        oneLine.thickness = lVal;
        m_horizontalLineInfo.m_vLineInfo.push_back(oneLine);
    }
    return true;
}
#endif