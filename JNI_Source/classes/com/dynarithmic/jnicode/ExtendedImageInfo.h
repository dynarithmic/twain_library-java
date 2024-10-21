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
#ifndef EXTENDEDIMAGEINFO_H
#define EXTENDEDIMAGEINFO_H
#include <vector>
#include <string>
#include "ExtendedImageInfo_Types.h"

#ifdef USING_DTWAIN_LOADLIBRARY
    #include "dtwainx2.h"
#else
    #include "dtwain.h"
#endif

class ExtendedImageInformation
{
    private:
        bool GenericFillLineInfo(ExtendedImageInfo_LineDetectionNative& allInfo, int32_t itemCountType, const std::array<int32_t, 4>& itemsToGet);

    public:
        // For barcode information
        DTWAIN_SOURCE m_theSource = nullptr;
        std::vector<std::string> m_vBarcodeText;
        std::vector<std::pair<uint32_t, uint32_t>> m_vBarcodePosition;
        std::vector<uint32_t> m_vBarcodeRotation;
        std::vector<uint32_t> m_vBarcodeType;
        std::vector<uint32_t> m_vBarcodeConfidence;
        bool infoRetrieved = false;
        int m_actualCount = 0;

        // For page source information
        std::string m_CameraInfo;
        std::string m_Bookname;
        LONG m_ChapterNumber = 0;
        LONG m_DocumentNumber = 0;
        LONG m_PageNumber = 0;
        LONG m_FrameNumber = 0;
        LONG m_PageSide = 0;

        ExtendedImageInfo_SkewDetectionInfoNative m_skewDetection;
        ExtendedImageInfo_PageSourceInfoNative m_pageSource;
        ExtendedImageInfo_BarcodeNative m_barcodeInfo;
        ExtendedImageInfo_ShadedAreaDetectionInfoNativeV m_shadedInfo;
        ExtendedImageInfo_SpeckleRemovalInfoNative m_speckleRemoval;
        ExtendedImageInfo_LineDetectionNative m_horizontalLineInfo;
        ExtendedImageInfo_LineDetectionNative m_verticalLineInfo;
        ExtendedImageInfo_FormsRecognitionNative m_formsRecognitionInfo;
        ExtendedImageInfo_ImageSegmentationInfoNative m_imageSementationInfo;
        ExtendedImageInfo_EndorsedTextInfoNative m_endorsedTextInfo;
        ExtendedImageInfo_ExtendedImageInfo21Native m_extendedImageInfo21;
        ExtendedImageInformation(DTWAIN_SOURCE theSource);

        bool IsInfoRetrieved() const { return infoRetrieved; }
        bool FillBarcodeInfo();
        bool FillPageSourceInfo();
        bool FillSkewInfo();
        bool FillShadedAreaInfo();
        bool FillSpeckleRemovalInfo();
        bool FillHorizontalLineInfo();
        bool FillVerticalLineInfo();
        bool FillFormsRecognitionInfo();
        bool FillImageSegmentationInfo();
        bool FillEndorsedTextInfo();
        bool FillExtendedImageInfo21();
};
#endif