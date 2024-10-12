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
        LONG length;
        API_INSTANCE DTWAIN_ArrayGetAt(aText, i, &sHandle);
        API_INSTANCE DTWAIN_ArrayGetAtLong(aTextLength, i, &length);
        char* pText = (char*)GlobalLock(sHandle);
        memcpy(szBarText, pText + lastLen, length);
        szBarText[length] = 0;
        lastLen += length;
        GlobalUnlock(sHandle);

        m_vBarcodeText.push_back(szBarText);
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
    LONG* pType = (LONG*)API_INSTANCE DTWAIN_ArrayGetBuffer(aType, 0);
    LONG* pRotation = (LONG*)API_INSTANCE DTWAIN_ArrayGetBuffer(aRotation, 0);
    LONG* pConfidence = (LONG*)API_INSTANCE DTWAIN_ArrayGetBuffer(aConfidence, 0);

    std::array<LONG *, 5> aArrays = { pBufferX, pBufferY, pType, pRotation, pConfidence };

    for (int i = 0; i < (std::min)(aCounts[0], aCounts[1]); ++i)
        m_vBarcodePosition.push_back({ pBufferX[i], pBufferY[i] });
    for (int i = 2; i < aCounts.size(); ++i)
    {
        auto vectToUse = aPtrVectors[i];
        std::transform(aArrays[i], aArrays[i] + aCounts[i], std::back_inserter(*vectToUse), [&](LONG val){ return val; });
    }
    m_actualCount = barCodeCount;
    return true;
}

bool ExtendedImageInformation::FillCameraInfo()
{
    if (!infoRetrieved)
        return false;
    
    DTWAIN_ARRAY cameras = {};
    DTWAINArray_RAII raii(cameras);
    auto Ret = API_INSTANCE DTWAIN_GetExtImageInfoData(m_theSource, TWEI_CAMERA, &cameras);
    int nCameras = API_INSTANCE DTWAIN_ArrayGetCount(cameras);
    if (nCameras > 0)
    {
        char szCamera[355] = {};
        API_INSTANCE DTWAIN_ArrayGetAtANSIString(cameras, 0, szCamera);
        m_CameraInfo = szCamera;
    }
    return true;
}