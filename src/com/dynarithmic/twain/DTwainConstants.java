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
package com.dynarithmic.twain;

import java.util.Map;

import com.dynarithmic.twain.misc.OrdinalEnum;

public class DTwainConstants
{
    public static class JNIVersion
    {
        private JNIVersion() {}
        public static final int JNI_32 = 0;
        public static final int JNI_32U = 1;
        public static final int JNI_64 = 2;
        public static final int JNI_64U = 3;
        public static final int JNI_32D = 4;
        public static final int JNI_32UD = 5;
        public static final int JNI_64D = 6;
        public static final int JNI_64UD = 7;
    }

    public static class DTwainVersionFlags
    {
        private DTwainVersionFlags() {}
        public static final int DTWAIN_DEMODLL_VERSION = 0x00000001;
        public static final int DTWAIN_UNLICENSED_VERSION = 0x00000002;
        public static final int DTWAIN_COMPANY_VERSION = 0x00000004;
        public static final int DTWAIN_GENERAL_VERSION = 0x00000008;
        public static final int DTWAIN_DEVELOP_VERSION = 0x00000010;
        public static final int DTWAIN_JAVA_VERSION = 0x00000020;
        public static final int DTWAIN_TOOLKIT_VERSION = 0x00000040;
        public static final int DTWAIN_LIMITEDDLL_VERSION = 0x00000080;
        public static final int DTWAIN_STATICLIB_VERSION = 0x00000100;
        public static final int DTWAIN_STATICLIB_STDCALL_VERSION = 0x00000200;
        public static final int DTWAIN_PDF_VERSION = 0x00010000;
        public static final int DTWAIN_TWAINSAVE_VERSION = 0x00020000;
        public static final int DTWAIN_OCR_VERSION = 0x00040000;
        public static final int DTWAIN_BARCODE_VERSION = 0x00080000;
        public static final int DTWAIN_ACTIVEX_VERSION = 0x00100000;
        public static final int DTWAIN_32BIT_VERSION = 0x00200000;
        public static final int DTWAIN_64BIT_VERSION = 0x00400000;
        public static final int DTWAIN_UNICODE_VERSION = 0x00800000;
    }

    public enum SessionStartupMode
    {
        NONE,
        AUTOSTART
    }

    public enum DSMType implements OrdinalEnum
    {
        LEGACY (1),
        VERSION2 (2),
        LATESTVERSION (4);

        private final int enumValue;

        private DSMType(int version)
        {
            enumValue = version;
        }

        public int value( ) { return enumValue; }

        private static final Map<Integer, DSMType> map = OrdinalEnum.getValues(DSMType.class);
        public static DSMType from(int i)
        {
            return map.get(i);
        }
    }

    public enum DSFileType implements OrdinalEnum
    {
        TIFF (0),
        PICT (1),
        BMP (2),
        XBM (3),
        JFIF (4),
        FPX (5),
        TIFFMULTI (6),
        PNG (7),
        SPIFF (8),
        EXIF (9),
        PDF (10),
        JP2 (11),
        JPX (13),
        DEJAVU (14),
        PDFA (15),
        PDFA2 (16),
        PDFRASTER (17);

        private final int enumValue;

        DSFileType(int i)
        {
            enumValue = i;
        }

        public int value() { return enumValue; }

        private static final Map<Integer, DSFileType> map = OrdinalEnum.getValues(DSFileType.class);
        public static DSFileType from(int i)
        {
            return map.get(i);
        }

    }

    public enum CompressionType implements OrdinalEnum
    {
        NONE (0),
        PACKBITS (1),
        GROUP31D (2),
        GROUP31DEOL (3),
        GROUP32D  (4),
        GROUP4 (5),
        JPEG (6),
        LZW (7),
        JBIG (8),
        PNG (9),
        RLE4 (10),
        RLE8 (11),
        BITFIELDS (12),
        ZIP (13),
        JPEG2000  (14);

        private final int enumValue;

        CompressionType(int i)
        {
            enumValue = i;
        }

        public int value() { return enumValue; }

        private static final Map<Integer, CompressionType> map = OrdinalEnum.getValues(CompressionType.class);
        public static CompressionType from(int i)
        {
            return map.get(i);
        }
    }

    public enum PaperSize implements OrdinalEnum
    {
        NONE (0),
        A4LETTER (1),
        B5LETTER (2),
        USLETTER (3),
        USLEGAL (4),
        A5 (5),
        B4 (6),
        B6 (7),
        B (8),
        USLEDGER (9),
        USEXECUTIVE (10),
        A3 (11),
        B3 (12),
        A6 (13),
        C4 (14),
        C5 (15),
        C6 (16),
        FOURA0 (17),
        TWOA0 (18),
        A0 (19),
        A1 (20),
        A2 (21),
        A4 (1),
        A7 (22),
        A8 (23),
        A9 (24),
        A10 (25),
        ISOB0 (26),
        ISOB1 (27),
        ISOB2 (28),
        ISOB3 (12),
        ISOB4 (6),
        ISOB5 (29),
        ISOB6 (7),
        ISOB7 (30),
        ISOB8 (31),
        ISOB9 (32),
        ISOB10 (33),
        JISB0 (34),
        JISB1 (35),
        JISB2 (36),
        JISB3 (37),
        JISB4 (38),
        JISB5 (2),
        JISB6 (39),
        JISB7 (40),
        JISB8 (41),
        JISB9 (42),
        JISB10 (43),
        C0 (44),
        C1 (45),
        C2 (46),
        C3 (47),
        C7 (48),
        C8 (49),
        C9 (50),
        C10 (51),
        USSTATEMENT (52),
        BUSINESSCARD (53),
        VARIABLEPAGESIZE (512),  // For PDF only
        CUSTOMSIZE (1024);  // For PDF only


        private final int enumValue;

        PaperSize(int i)
        {
            enumValue = i;
        }

        public int value() { return enumValue; }

        private static final Map<Integer, PaperSize> map = OrdinalEnum.getValues(PaperSize.class);
        public static PaperSize from(int i)
        {
            return map.get(i);
        }

    }

    public enum FileType implements OrdinalEnum
    {
        BMP (100),
        JPEG (200),
        PDF (250),
        PDFMULTI (251),
        PCX (300),
        DCX (301),
        TGA (400),
        TIFFLZW (500),
        TIFFNONE (600),
        TIFFG3 (700),
        TIFFG4 (800),
        TIFFPACKBITS (801),
        TIFFDEFLATE (802),
        TIFFJPEG (803),
        TIFFJBIG (804),
        TIFFPIXARLOG (805),
        TIFFNONEMULTI (900),
        TIFFG3MULTI (901),
        TIFFG4MULTI (902),
        TIFFPACKBITSMULTI (903),
        TIFFDEFLATEMULTI (904),
        TIFFJPEGMULTI (905),
        TIFFLZWMULTI (906),
        TIFFJBIGMULTI (907),
        TIFFPIXARLOGMULTI (908),
        WMF (850),
        EMF (851),
        GIF (950),
        PNG (1000),
        PSD (2000),
        JPEG2000 (3000),
        POSTSCRIPT1 (4000),
        POSTSCRIPT2 (4001),
        POSTSCRIPT3 (4002),
        POSTSCRIPT1MULTI (4003),
        POSTSCRIPT2MULTI (4004),
        POSTSCRIPT3MULTI (4005),
        TEXT (6000),
        TEXTMULTI (6001),
        TIFFMULTI (7000),
        ICO (8000),
        ICO_VISTA (8001),
        WBMP (8500),
        GOOGLEWEBP(8501),
        PBM(10000),
        PPM(10000),
        BMP_SOURCE_MODE(2),
        TIFF_SOURCE_MODE(0),
        PICT_SOURCE_MODE(1),
        XBM_SOURCE_MODE(3),
        JFIF_SOURCE_MODE(4),
        FPX_SOURCE_MODE(5),
        TIFFMULTI_SOURCE_MODE(6),
        PNG_SOURCE_MODE(7),
        SPIFF_SOURCE_MODE(8),
        EXIF_SOURCE_MODE(9),
        PDF_SOURCE_MODE(10),
        JP2_SOURCE_MODE(11),
        JPX_SOURCE_MODE(13),
        DEJAVU_SOURCE_MODE(14),
        PDFA_SOURCE_MODE(15),
        PDFA2_SOURCE_MODE(16),
        PDFRASTER_SOURCE_MODE(17);


        private final int enumValue;

        FileType(int i)
        {
            enumValue = i;
        }
        public int value() { return enumValue; }

        private static final Map<Integer, FileType> map = OrdinalEnum.getValues(FileType.class);
        public static FileType from(int i)
        {
            return map.get(i);
        }
    }

    public enum FileTypeMultiPage implements OrdinalEnum
    {
        PCX (FileType.DCX.value()),
        PDF (FileType.PDF.value()),
        TIFFNOCOMPRESS (FileType.TIFFNONEMULTI.value()),
        TIFFG3 (FileType.TIFFG3MULTI.value()),
        TIFFG4 (FileType.TIFFG4MULTI.value()),
        TIFFPACKBITS (FileType.TIFFPACKBITSMULTI.value()),
        TIFFDEFLATE (FileType.TIFFDEFLATEMULTI.value()),
        TIFFJPEG (FileType.TIFFJPEGMULTI.value()),
        TIFFLZW (FileType.TIFFLZWMULTI.value()),
        POSTSCRIPT1 (FileType.POSTSCRIPT1MULTI.value()),
        POSTSCRIPT2 (FileType.POSTSCRIPT2MULTI.value()),
        POSTSCRIPT3 (FileType.POSTSCRIPT3MULTI.value()),
        TEXT  (FileType.TEXTMULTI.value()),
        NOTYPE (9999);

        private final int enumValue;

        FileTypeMultiPage(int i)
        {
            enumValue = i;
        }
        public int value() { return enumValue; }

        private static final Map<Integer, FileTypeMultiPage> map = OrdinalEnum.getValues(FileTypeMultiPage.class);
        public static FileTypeMultiPage from(int i)
        {
            return map.get(i);
        }
    }

    public enum PixelType implements OrdinalEnum
    {
        BW (0),
        GRAY (1),
        RGB (2),
        PALETTE (3),
        CMY (4),
        CMYK (5),
        YUV (6),
        YUVK (7),
        CIEXYZ (8),
        LAB (9),
        SRGB (10),
        SCRGB (11),
        SRGB64 (11),
        BGR (12),
        CIELAB (13),
        CIELUV (14),
        YCBCR (15),
        INFRARED (16),
        DEFAULT (1000);

        private final int enumValue;
        PixelType(int i)
        {
            enumValue = i;
        }

        public int value() { return enumValue; }

        private static final Map<Integer, PixelType> map = OrdinalEnum.getValues(PixelType.class);
        public static PixelType from(int i)
        {
            return map.get(i);
        }
    }

    public enum NotificationCode implements OrdinalEnum
    {
        ACQUIREDONE (1000),
        ACQUIREFAILED (1001),
        ACQUIRECANCELLED (1002),
        ACQUIRESTARTED (1003),
        PAGECONTINUE (1004),
        PAGEFAILED (1005),
        PAGECANCELLED (1006),
        TRANSFERREADY (1009),
        TRANSFERDONE (1010),
        UICLOSING (1011),
        UICLOSED (1012),
        UIOPENED (1013),
        UIOPENING (1055),
        UIOPENFAILURE (1060),
        CLIPTRANSFERDONE (1014),
        INVALIDIMAGEFORMAT (1015),
        ACQUIRETERMINATED (1021),
        TRANSFERSTRIPREADY (1022),
        TRANSFERSTRIPDONE (1023),
        TRANSFERSTRIPFAILED (1029),
        IMAGEINFOERROR (1024),
        TRANSFERCANCELLED (1030),
        FILESAVECANCELLED (1031),
        FILESAVEOK (1032),
        FILESAVEERROR (1033),
        FILEPAGESAVEOK (1034),
        FILEPAGESAVEERROR (1035),
        PROCESSEDDIB (1036),
        FEEDERLOADED (1037),
        GENERALERROR (1038),
        MANDUPFLIPPAGES (1040),
        MANDUPSIDE1DONE (1041),
        MANDUPSIDE2DONE (1042),
        MANDUPPAGECOUNTERROR (1043),
        MANDUPACQUIREDONE (1044),
        MANDUPSIDE1START (1045),
        MANDUPSIDE2START (1046),
        MANDUPMERGEERROR (1047),
        MANDUPMEMORYERROR (1048),
        MANDUPFILEERROR (1049),
        MANDUPFILESAVEERROR (1050),
        ENDOFJOBDETECTED (1051),
        EOJDETECTED (1051),
        EOJDETECTED_XFERDONE (1052),
        QUERYPAGEDISCARD (1053),
        PAGEDISCARDED (1054),
        PROCESSDIBACCEPTED (1055),
        PROCESSDIBFINALACCEPTED (1056),
        DEVICEEVENT (1100),
        TWAINPAGECANCELLED (1105),
        TWAINPAGEFAILED (1106),
        APPUPDATEDDIB (1107),
        FILEPAGESAVING (1110),
        EOJBEGINFILESAVE (1112),
        EOJENDFILESAVE (1113),
        CROPFAILED (1120),
        PROCESSEDDIBFINAL (1121),
        BLANKPAGEDETECTED1 (1130),
        BLANKPAGEDETECTED2 (1131),
        BLANKPAGEDETECTED3 (1132),
        BLANKPAGEDISCARDED1 (1133),
        BLANKPAGEDISCARDED2 (1134),
        OCRTEXTRETRIEVED (1140),
        QUERYOCRTEXT (1141),
        PDFOCRREADY (1142),
        PDFOCRDONE (1143),
        PDFOCRERROR (1144),
        SETCALLBACKINIT (1150),
        SETCALLBACK64INIT (1151),
        FILENAMECHANGING (1160),
        FILENAMECHANGED (1161),
        PROCESSEDAUDIOFINAL (1180),
        PROCESSAUDIOFINALACCEPTED (1181),
        PROCESSEDAUDIOFILE  (1182),
        TWAINTRIPLETBEGIN (1183),
        TWAINTRIPLETEND (1184),
        FEEDERNOTLOADED (1200);

        private final int enumValue;
        NotificationCode(int i)
        {
            enumValue = i;
        }

        public int value() { return enumValue; }

        private static final Map<Integer, NotificationCode> map = OrdinalEnum.getValues(NotificationCode.class);
        public static NotificationCode from(int i)
        {
            return map.get(i);
        }
    }

    public enum JobControl implements OrdinalEnum
    {
        NONE (0),
        JSIC (1),
        JSIS (2),
        JSXC (3),
        JSXS (4);

        private final int enumValue;
        JobControl(int i)
        {
            enumValue = i;
        }
        public int value() { return enumValue; }
        private static final Map<Integer, JobControl> map = OrdinalEnum.getValues(JobControl.class);
        public static JobControl from(int i)
        {
            return map.get(i);
        }
    }

    public enum ErrorCode implements OrdinalEnum
    {
        ERROR_NONE (0),
        ERROR_ACQUIRECANCELLED(1002),
        ERROR_FIRST (-1000),
        ERROR_BAD_HANDLE (-1001),
        ERROR_BAD_SOURCE (-1002),
        ERROR_BAD_ARRAY (-1003),
        ERROR_WRONG_ARRAY_TYPE (-1004),
        ERROR_INDEX_BOUNDS (-1005),
        ERROR_OUT_OF_MEMORY (-1006),
        ERROR_NULL_WINDOW (-1007),
        ERROR_BAD_PIXTYPE (-1008),
        ERROR_BAD_CONTAINER (-1009),
        ERROR_NO_SESSION (-1010),
        ERROR_BAD_ACQUIRE_NUM (-1011),
        ERROR_BAD_CAP (-1012),
        ERROR_CAP_NO_SUPPORT (-1013),
        ERROR_TWAIN (-1014),
        ERROR_HOOK_FAILED (-1015),
        ERROR_BAD_FILENAME (-1016),
        ERROR_EMPTY_ARRAY (-1017),
        ERROR_FILE_FORMAT (-1018),
        ERROR_BAD_DIB_PAGE (-1019),
        ERROR_SOURCE_ACQUIRING (-1020),
        ERROR_INVALID_PARAM (-1021),
        ERROR_INVALID_RANGE (-1022),
        ERROR_UI_ERROR (-1023),
        ERROR_BAD_UNIT (-1024),
        ERROR_LANGDLL_NOT_FOUND (-1025),
        ERROR_SOURCE_NOT_OPEN (-1026),
        ERROR_DEVICEEVENT_NOT_SUPPORTED (-1027),
        ERROR_UIONLY_NOT_SUPPORTED (-1028),
        ERROR_UI_ALREADY_OPENED (-1029),
        ERROR_CAPSET_NOSUPPORT (-1030),
        ERROR_NO_FILE_XFER (-1031),
        ERROR_INVALID_BITDEPTH (-1032),
        ERROR_NO_CAPS_DEFINED (-1033),
        ERROR_TILES_NOT_SUPPORTED (-1034),
        ERROR_INVALID_DTWAIN_FRAME (-1035),
        ERROR_LIMITED_VERSION (-1036),
        ERROR_NO_FEEDER (-1037),
        ERROR_NO_FEEDER_QUERY (-1038),
        ERROR_EXCEPTION_ERROR (-1039),
        ERROR_INVALID_STATE (-1040),
        ERROR_UNSUPPORTED_EXTINFO (-1041),
        ERROR_DLLRESOURCE_NOTFOUND (-1042),
        ERROR_NOT_INITIALIZED (-1043),
        ERROR_NO_SOURCES (-1044),
        ERROR_TWAIN_NOT_INSTALLED (-1045),
        ERROR_WRONG_THREAD (-1046),
        ERROR_BAD_CAPTYPE (-1047),
        ERROR_UNKNOWN_CAPDATATYPE (-1048),
        ERROR_DEMO_NOFILETYPE (-1049),
        ERROR_SOURCESELECTION_CANCELED (-1050),
        ERROR_RESOURCES_NOT_FOUND (-1051),
        ERROR_STRINGTYPE_MISMATCH (-1052),
        ERROR_ARRAYTYPE_MISMATCH (-1053),
        ERROR_SOURCENAME_NOTINSTALLED (-1054),

        TWAIN_ERROR_LOW_MEMORY (-1100),
        TWAIN_ERROR_FALSE_ALARM (-1101),
        TWAIN_ERROR_BUMMER (-1102),
        TWAIN_ERROR_NODATASOURCE (-1103),
        TWAIN_ERROR_MAXCONNECTIONS (-1104),
        TWAIN_ERROR_OPERATIONERROR (-1105),
        TWAIN_ERROR_BADCAPABILITY (-1106),
        TWAIN_ERROR_BADVALUE (-1107),
        TWAIN_ERROR_BADPROTOCOL (-1108),
        TWAIN_ERROR_SEQUENCEERROR (-1109),
        TWAIN_ERROR_BADDESTINATION (-1110),
        TWAIN_ERROR_CAPNOTSUPPORTED (-1111),
        TWAIN_ERROR_CAPBADOPERATION (-1112),
        TWAIN_ERROR_CAPSEQUENCEERROR (-1113),
        TWAIN_ERROR_FILEPROTECTEDERROR (-1114),
        TWAIN_ERROR_FILEEXISTERROR (-1115),
        TWAIN_ERROR_FILENOTFOUND (-1116),
        TWAIN_ERROR_DIRNOTEMPTY (-1117),
        TWAIN_ERROR_FEEDERJAMMED (-1118),
        TWAIN_ERROR_FEEDERMULTPAGES (-1119),
        TWAIN_ERROR_FEEDERWRITEERROR (-1120),
        TWAIN_ERROR_DEVICEOFFLINE (-1121),
        TWAIN_ERROR_NULL_CONTAINER (-1122),
        TWAIN_ERROR_INTERLOCK (-1123),
        TWAIN_ERROR_DAMAGEDCORNER (-1124),
        TWAIN_ERROR_FOCUSERROR (-1125),
        TWAIN_ERROR_DOCTOOLIGHT (-1126),
        TWAIN_ERROR_DOCTOODARK (-1127),
        TWAIN_ERROR_NOMEDIA (-1128),
        ERROR_FILEXFERSTART (-2000),
        ERROR_MEM (-2001),
        ERROR_FILEOPEN (-2002),
        ERROR_FILEREAD (-2003),
        ERROR_FILEWRITE (-2004),
        ERROR_BADPARAM (-2005),
        ERROR_INVALIDBMP (-2006),
        ERROR_BMPRLE (-2007),
        ERROR_RESERVED1 (-2008),
        ERROR_INVALIDJPG (-2009),
        ERROR_DC (-2010),
        ERROR_DIB (-2011),
        ERROR_RESERVED2 (-2012),
        ERROR_NORESOURCE (-2013),
        ERROR_CALLBACKCANCEL (-2014),
        ERROR_INVALIDPNG (-2015),
        ERROR_PNGCREATE (-2016),
        ERROR_INTERNAL (-2017),
        ERROR_FONT (-2018),
        ERROR_INTTIFF (-2019),
        ERROR_INVALIDTIFF (-2020),
        ERROR_NOTIFFLZW (-2021),
        ERROR_INVALIDPCX (-2022),
        ERROR_CREATEBMP (-2023),
        ERROR_NOLINES (-2024),
        ERROR_GETDIB (-2025),
        ERROR_NODEVOP (-2026),
        ERROR_INVALIDWMF (-2027),
        ERROR_DEPTHMISMATCH (-2028),
        ERROR_BITBLT (-2029),
        ERROR_BUFTOOSMALL (-2030),
        ERROR_TOOMANYCOLORS (-2031),
        ERROR_INVALIDTGA (-2032),
        ERROR_NOTGATHUMBNAIL (-2033),
        ERROR_RESERVED3 (-2034),
        ERROR_CREATEDIB (-2035),
        ERROR_NOLZW (-2036),
        ERROR_SELECTOBJ (-2037),
        ERROR_BADMANAGER (-2038),
        ERROR_OBSOLETE (-2039),
        ERROR_CREATEDIBSECTION (-2040),
        ERROR_SETWINMETAFILEBITS (-2041),
        ERROR_GETWINMETAFILEBITS (-2042),
        ERROR_PAXPWD (-2043),
        ERROR_INVALIDPAX (-2044),
        ERROR_NOSUPPORT (-2045),
        ERROR_INVALIDPSD (-2046),
        ERROR_PSDNOTSUPPORTED (-2047),
        ERROR_DECRYPT (-2048),
        ERROR_ENCRYPT (-2049),
        ERROR_COMPRESSION (-2050),
        ERROR_DECOMPRESSION (-2051),
        ERROR_INVALIDTLA (-2052),
        ERROR_INVALIDWBMP (-2053),
        ERROR_NOTIFFTAG (-2054),
        ERROR_NOLOCALSTORAGE (-2055),
        ERROR_INVALIDEXIF (-2056),
        ERROR_NOEXIFSTRING (-2057),
        ERROR_TIFFDLL32NOTFOUND (-2058),
        ERROR_TIFFDLL16NOTFOUND (-2059),
        ERROR_PNGDLL16NOTFOUND (-2060),
        ERROR_JPEGDLL16NOTFOUND (-2061),
        ERROR_BADBITSPERPIXEL (-2062),
        ERROR_TIFFDLL32INVALIDVER (-2063),
        ERROR_PDFDLL32NOTFOUND (-2064),
        ERROR_PDFDLL32INVALIDVER (-2065),
        ERROR_JPEGDLL32NOTFOUND (-2066),
        ERROR_JPEGDLL32INVALIDVER (-2067),
        ERROR_PNGDLL32NOTFOUND (-2068),
        ERROR_PNGDLL32INVALIDVER (-2069),
        ERROR_J2KDLL32NOTFOUND (-2070),
        ERROR_J2KDLL32INVALIDVER (-2071),
        ERROR_MANDUPLEX_UNAVAILABLE (-2072),
        ERROR_TIMEOUT (-2073),
        ERROR_INVALIDICONFORMAT (-2074),
        ERROR_TWAIN32DSMNOTFOUND (-2075),
        ERROR_TWAINOPENSOURCEDSMNOTFOUND (-2076),
        ERROR_TS_FIRST (-2080),
        ERROR_TS_NOFILENAME (-2081),
        ERROR_TS_NOTWAINSYS (-2082),
        ERROR_TS_DEVICEFAILURE (-2083),
        ERROR_TS_FILESAVEERROR (-2084),
        ERROR_TS_COMMANDILLEGAL (-2085),
        ERROR_TS_CANCELLED (-2086),
        ERROR_TS_ACQUISITIONERROR (-2087),
        ERROR_TS_INVALIDCOLORSPACE (-2088),
        ERROR_TS_PDFNOTSUPPORTED (-2089),
        ERROR_TS_NOTAVAILABLE (-2090),
        ERROR_OCR_FIRST (-2100),
        ERROR_OCR_INVALIDPAGENUM (-2101),
        ERROR_OCR_INVALIDENGINE (-2102),
        ERROR_OCR_NOTACTIVE (-2103),
        ERROR_OCR_INVALIDFILETYPE (-2104),
        ERROR_OCR_INVALIDPIXELTYPE (-2105),
        ERROR_OCR_INVALIDBITDEPTH (-2106),
        ERROR_OCR_RECOGNITIONERROR (-2107),
        ERROR_OCR_LAST (-2108);

        private final int enumValue;
        ErrorCode(int i)
        {
            enumValue = i;
        }
        public int value() { return enumValue; }
        private static final Map<Integer, ErrorCode> map = OrdinalEnum.getValues(ErrorCode.class);
        public static ErrorCode from(int i)
        {
            return map.get(i);
        }
    }

    public enum TwainDialogOptions implements OrdinalEnum
    {
        SORTNAMES (1),
        CENTER (2),
        CENTER_SCREEN (4),
        USETEMPLATE (8),
        CLEAR_PARAMS (16),
        HORIZONTALSCROLL (32),
        USEINCLUDENAMES (64),
        USEEXCLUDENAMES (128),
        USENAMEMAPPING (256),
        USEDEFAULTTITLE (512),
        TOPMOSTWINDOW (1024);

        private final int enumValue;
        TwainDialogOptions(int i)
        {
            enumValue = i;
        }
        public int value() { return enumValue; }
        private static final Map<Integer, TwainDialogOptions> map = OrdinalEnum.getValues(TwainDialogOptions.class);
        public static TwainDialogOptions from(int i)
        {
            return map.get(i);
        }
    }

    public enum MeasureUnit implements OrdinalEnum
    {
        INCHES (0),
        CENTIMETERS (1),
        PICAS (2),
        POINTS (3),
        TWIPS (4),
        PIXELS (5);

        private final int enumValue;
        MeasureUnit(int i)
        {
            enumValue = i;
        }
        public int value() { return enumValue; }
        private static final Map<Integer, MeasureUnit> map = OrdinalEnum.getValues(MeasureUnit.class);
        public static MeasureUnit from(int i)
        {
            return map.get(i);
        }
    }

    public enum PageOrientation implements OrdinalEnum
    {
        ROT0 (0),
        ROT90 (1),
        ROT180 (2),
        ROT270 (3),
        PORTRAIT (ROT0),
        LANDSCAPE (ROT270);
        private final int enumValue;
        PageOrientation(int i)
        {
            enumValue = i;
        }
        PageOrientation(PageOrientation po)
        {
            enumValue = po.value();
        }

        public int value() { return enumValue; }
        private static final Map<Integer, PageOrientation> map = OrdinalEnum.getValues(PageOrientation.class);
        public static  PageOrientation from(int i)
        {
            return map.get(i);
        }
    }

    public enum PDFFileOptions implements OrdinalEnum
    {
        ASCII85COMPRESSION (2048);

        public enum Scaling implements OrdinalEnum
        {
            NOSCALING (128),
            FITPAGE (256),
            CUSTOMSCALE (4096),
            PIXELSPERMETERSIZE (8192);
            private final int enumValue;
            Scaling(int i)
            {
                enumValue = i;
            }
            public int value() { return enumValue; }
            private static final Map<Integer, Scaling> map = OrdinalEnum.getValues(Scaling.class);
            public static Scaling from(int i)
            {
                return map.get(i);
            }
        }

        public enum Protection implements OrdinalEnum
        {
            ALLOWPRINTING (2052),
            ALLOWMOD (8),
            ALLOWCOPY (16),
            ALLOWMODANNOTATIONS (32),
            ALLOWFILLIN (256),
            ALLOWEXTRACTION (512),
            ALLOWASSEMBLY (1024),
            ALLOWDEGRADEDPRINTING (4);

            private final int enumValue;
            Protection(int i)
            {
                enumValue = i;
            }
            public int value() { return enumValue; }
            private static final Map<Integer, Protection> map = OrdinalEnum.getValues(Protection.class);
            public static Protection from(int i)
            {
                return map.get(i);
            }
        }

        private final int enumValue;
        PDFFileOptions(int i)
        {
            enumValue = i;
        }
        public int value() { return enumValue; }
        private static final Map<Integer, PDFFileOptions> map = OrdinalEnum.getValues(PDFFileOptions.class);
        public static PDFFileOptions from(int i)
        {
            return map.get(i);
        }
    }

    public enum CapabilityOption implements OrdinalEnum
    {
        GET (1),
        GETCURRENT (2),
        GETDEFAULT (3),
        SET (6),
        RESET (7),
        RESETALL (8),
        SETCONSTRAINT (9),
        GETHELP (9),
        GETLABEL (10),
        GETLABELENUM (11),
        SETAVAILABLE (8),
        SETCURRENT (16);

        private final int enumValue;
        CapabilityOption(int i)
        {
            enumValue = i;
        }
        public int value() { return enumValue; }
        private static final Map<Integer, CapabilityOption> map = OrdinalEnum.getValues(CapabilityOption.class);
        public static CapabilityOption from(int i)
        {
            return map.get(i);
        }
    }

    public enum SourceStateAfterAcquire
    {
        CLOSED,
        OPENED
    }

    public enum RoundingRule implements OrdinalEnum
    {
        ROUNDNEAREST (0),
        ROUNDUP (1),
        ROUNDDOWN (2);

        public enum DeltaValue
        {
            DELTA (+1.0e-8);
            private final double enumValue;
            DeltaValue(double d)
            {
                enumValue = d;
            }
            public double value() { return enumValue; }
        }

        private final int enumValue;

        RoundingRule(int i)
        {
            enumValue = i;
        }

        public int value() { return enumValue; }
        private static final Map<Integer, RoundingRule> map = OrdinalEnum.getValues(RoundingRule.class);
        public static RoundingRule from(int i)
        {
            return map.get(i);
        }

    }

    public enum ManualDuplexSetup implements OrdinalEnum
    {
        FACEUPTOPPAGE (0),
        FACEUPBOTTOMPAGE (1),
        FACEDOWNTOPPAGE (2),
        FACEDOWNBOTTOMPAGE (3);

        private final int enumValue;

        ManualDuplexSetup(int i)
        {
            enumValue = i;
        }

        public int value() { return enumValue; }
        private static final Map<Integer, ManualDuplexSetup> map = OrdinalEnum.getValues(ManualDuplexSetup.class);
        public static ManualDuplexSetup from(int i)
        {
            return map.get(i);
        }
    }

    public enum ArrayType implements OrdinalEnum
    {
        ARRAYANY (1),
        ARRAYLONG (2),
        ARRAYFLOAT (3),
        ARRAYHANDLE (4),
        ARRAYSOURCE (5),
        ARRAYSTRING (6),
        ARRAYFRAME (7),
        ARRAYBOOL (ARRAYLONG.value()),
        ARRAYLONGSTRING (8),
        ARRAYUNICODESTRING (9),
        ARRAYLONG64 (10),
        ARRAYANSISTRING (11),
        ARRAYWIDESTRING (12),
        ARRAYTWFIX32 (200),
        ARRAYINT16 (100),
        ARRAYUINT16 (110),
        ARRAYUINT32 (120),
        ARRAYINT32 (130),
        ARRAYINT64 (140),
        RANGELONG (ARRAYLONG.value()),
        RANGEFLOAT (ARRAYFLOAT.value());

        private final int enumValue;

        ArrayType(int i)
        {
            enumValue = i;
        }

        public int value() { return enumValue; }
        private static final Map<Integer, ArrayType> map = OrdinalEnum.getValues(ArrayType.class);
        public static ArrayType from(int i)
        {
            return map.get(i);
        }
    }

    public enum ContainerType implements OrdinalEnum
    {
        ARRAY (8),
        ENUMERATION (16),
        ONEVALUE (32),
        RANGE (64),
        INVALID(9999),
        DEFAULT (0);
        private final int enumValue;

        ContainerType(int i)
        {
            enumValue = i;
        }

        public int value() { return enumValue; }
        private static final Map<Integer, ContainerType> map = OrdinalEnum.getValues(ContainerType.class);
        public static ContainerType from(int i)
        {
            return map.get(i);
        }
    }

    public enum AcquireFileOptions implements OrdinalEnum
    {
        USENATIVE (1),
        USEBUFFERED (2),
        USECOMPRESSION (4),
        USEMEMFILE (8),
        USENAME (16),
        USEPROMPT (32),
        USELONGNAME (64),
        USESOURCEMODE (128),
        USELIST (256),
        CREATEDIRECTORY (512);

        private final int enumValue;

        AcquireFileOptions(int i)
        {
            enumValue = i;
        }

        public int value() { return enumValue; }
        private static final Map<Integer, AcquireFileOptions> map = OrdinalEnum.getValues(AcquireFileOptions.class);
        public static AcquireFileOptions from(int i)
        {
            return map.get(i);
        }
    }

    public enum RangeOptions implements OrdinalEnum
    {
        RANGEMIN (0),
        RANGEMAX (1),
        RANGESTEP (2),
        RANGEDEFAULT (3),
        RANGECURRENT (4);

        private final int enumValue;

        RangeOptions(int i)
        {
            enumValue = i;
        }

        public int value() { return enumValue; }
        private static final Map<Integer, RangeOptions> map = OrdinalEnum.getValues(RangeOptions.class);
        public static RangeOptions from(int i)
        {
            return map.get(i);
        }
    }

    public enum LoggingOptions implements OrdinalEnum
    {
        DECODE_SOURCE      (0x00000001),
        DECODE_DEST        (0x00000002),
        DECODE_TWMEMREF    (0x00000040),
        DECODE_TWEVENT     (0x00000008),
        SHOW_CALLSTACK     (0x00000010),
        SHOW_ISTWAINMSG    (0x00000020),
        SHOW_INITFAILURE   (0x00000040),
        SHOW_LOWLEVELTWAIN (0x00000080),
        DECODE_BITMAP      (0x00000100),
        SHOW_NOTIFICATIONS (0x00000200),
        SHOW_MISCELLANEOUS (0x00000400),
        SHOW_DTWAINERRORS  (0x00000800),
        LOGALL             (0x0000FFFF),
        USE_FILE           (0x00010000),
        SHOW_EXCEPTIONS    (0x00020000),
        SHOW_ERRORMSGBOX   (0x00040000),
        USE_BUFFER         (0x00080000),
        FILE_APPEND        (0x00100000),
        USE_CALLBACK       (0x00200000),
        USE_CRLF           (0x00400000),
        USE_CONSOLE        (0x00800000),
        USE_DEBUGMONITOR   (0x01000000),
        USE_WINDOW         (0x02000000);

        private final int enumValue;

        LoggingOptions(int i)
        {
            enumValue = i;
        }

        public int value() { return enumValue; }
        private static final Map<Integer, LoggingOptions> map = OrdinalEnum.getValues(LoggingOptions.class);
        public static LoggingOptions from(int i)
        {
            return map.get(i);
        }
    }

    public enum AcquireType implements OrdinalEnum
    {
        ACQUIREINVALID (-1),
        NATIVE (1),
        BUFFERED (2),
        DEVICEFILE (3),
        NATIVEFILE (AcquireFileOptions.USENATIVE.value()),
        BUFFEREDFILE(AcquireFileOptions.USEBUFFERED.value()),
        NATIVECLIPBOARD (6),
        BUFFEREDCLIPBOARD (7);

        private final int enumValue;

        AcquireType(int i)
        {
            enumValue = i;
        }

        public int value() { return enumValue; }
        private static final Map<Integer, AcquireType> map = OrdinalEnum.getValues(AcquireType.class);
        public static AcquireType from(int i)
        {
            return map.get(i);
        }
    }

    public enum BlankPageDiscardOption implements OrdinalEnum
    {
        AUTODISCARD_NONE (0),
        AUTODISCARD_ORIGINAL (1),
        AUTODISCARD_ADJUSTED (2),
        AUTODISCARD_ANY (0xFFFF);
        private final int enumValue;

        BlankPageDiscardOption(int i)
        {
            enumValue = i;
        }

        public int value() { return enumValue; }
        private static final Map<Integer, BlankPageDiscardOption> map = OrdinalEnum.getValues(BlankPageDiscardOption.class);
        public static BlankPageDiscardOption from(int i)
        {
            return map.get(i);
        }
    }

    public enum BlankPageDetectionOption implements OrdinalEnum
    {
        DETECT_ORIGINAL (1),
        DETECT_ADJUSTED (2),
        DETECT_BOTH (3);

        private final int enumValue;

        BlankPageDetectionOption(int i)
        {
            enumValue = i;
        }

        public int value() { return enumValue; }
        private static final Map<Integer, BlankPageDetectionOption> map = OrdinalEnum.getValues(BlankPageDetectionOption.class);
        public static BlankPageDetectionOption from(int i)
        {
            return map.get(i);
        }
    }

    public enum TwainTripletType implements OrdinalEnum
    {
        DGNAME  (0),
        DATNAME (1),
        MSGNAME (2);
        private final int enumValue;

        TwainTripletType(int i)
        {
            enumValue = i;
        }

        public int value() { return enumValue; }
        private static final Map<Integer, TwainTripletType> map = OrdinalEnum.getValues(TwainTripletType.class);
        public static TwainTripletType from(int i)
        {
            return map.get(i);
        }
    }

    public enum TextRenderMode implements OrdinalEnum
    {
        FILL (0),
        STROKE (1),
        FILLANDSTROKE (2),
        INVISIBLE (3);

        private final int enumValue;

        TextRenderMode(int i)
        {
            enumValue = i;
        }

        public int value() { return enumValue; }
        private static final Map<Integer, TextRenderMode> map = OrdinalEnum.getValues(TextRenderMode.class);
        public static TextRenderMode from(int i)
        {
            return map.get(i);
        }
    }

    public enum AcquireAreaOptions implements OrdinalEnum
    {
        SET (CapabilityOption.SET.value()),
        RESET (CapabilityOption.RESET.value());
        private final int enumValue;

        AcquireAreaOptions(int i)
        {
            enumValue = i;
        }
        public int value() { return enumValue; }
        private static final Map<Integer, AcquireAreaOptions> map = OrdinalEnum.getValues(AcquireAreaOptions.class);
        public static AcquireAreaOptions from(int i)
        {
            return map.get(i);
        }
    }

    public enum TextPageDisplayOptions implements OrdinalEnum
    {
        ALLPAGES (1),
        EVENPAGES (2),
        ODDPAGES (4),
        FIRSTPAGE (8),
        LASTPAGE (16),
        CURRENTPAGE (32),
        DISABLED (64);

        private final int enumValue;

        TextPageDisplayOptions(int i)
        {
            enumValue = i;
        }

        public int value() { return enumValue; }
        private static final Map<Integer, TextPageDisplayOptions> map = OrdinalEnum.getValues(TextPageDisplayOptions.class);
        public static TextPageDisplayOptions from(int i)
        {
            return map.get(i);
        }
    }

    public enum MultipageSaveMode implements OrdinalEnum
    {
        DEFAULT(0),
        ONUICLODE(1),
        ONSOURCECLOSE(2),
        ONENDACQUIRE(3),
        ONMANUALSAVE(4);

        private final int enumValue;

        MultipageSaveMode(int i)
        {
            enumValue = i;
        }

        public int value() { return enumValue; }
        private static final Map<Integer, MultipageSaveMode> map = OrdinalEnum.getValues(MultipageSaveMode.class);
        public static MultipageSaveMode from(int i)
        {
            return map.get(i);
        }
    }


    public static final int DTWAIN_AREASET = CapabilityOption.SET.value();
    public static final int DTWAIN_AREARESET = CapabilityOption.RESET.value();
    public static final int DTWAIN_AREACURRENT = CapabilityOption.GETCURRENT.value();
    public static final int DTWAIN_AREADEFAULT = CapabilityOption.GETDEFAULT.value();

    public static final int DTWAIN_ACQUIREALL = (-1);
    public static final int DTWAIN_MAXACQUIRE = (-1);
    public static final int DTWAIN_ANYSUPPORT = (-1);

    public static final int DTWAIN_USENATIVE = 1;
    public static final int DTWAIN_USEBUFFERED = 2;
    public static final int DTWAIN_USECOMPRESSION = 4;
    public static final int DTWAIN_USEMEMFILE = 8;
    public static final int DTWAIN_USENAME = 16;
    public static final int DTWAIN_USEPROMPT = 32;
    public static final int DTWAIN_USELONGNAME = 64;
    public static final int DTWAIN_USESOURCEMODE = 128;
    public static final int DTWAIN_USELIST = 256;
    public static final int DTWAIN_CREATEDIRECTORY = 512;


/*  public static final int DTWAIN_FRAMELEFT = 0;
    public static final int DTWAIN_FRAMETOP = 1;
    public static final int DTWAIN_FRAMERIGHT = 2;
    public static final int DTWAIN_FRAMEBOTTOM = 3;
    public static final int DTWAIN_FIX32WHOLE = 0;
    public static final int DTWAIN_FIX32FRAC = 1;
    public static final int DTWAIN_CAPDATATYPE_UNKNOWN = (-10);
    public static final int DTWAIN_JCBP_JSIC = 5;
    public static final int DTWAIN_JCBP_JSIS = 6;
    public static final int DTWAIN_JCBP_JSXC = 7;
    public static final int DTWAIN_JCBP_JSXS = 8;
    public static final int DTWAIN_FEEDPAGEON = 1;
    public static final int DTWAIN_CLEARPAGEON = 2;
    public static final int DTWAIN_REWINDPAGEON = 4;
    public static final int DTWAIN_AppOwnsDib = 1;
    public static final int DTWAIN_SourceOwnsDib = 2;
    public static final int DTWAIN_AREASET = DTWAIN_CAPSET;
    public static final int DTWAIN_AREARESET = DTWAIN_CAPRESET;
    public static final int DTWAIN_AREACURRENT = DTWAIN_CAPGETCURRENT;
    public static final int DTWAIN_AREADEFAULT = DTWAIN_CAPGETDEFAULT;
    public static final int DTWAIN_VER15 = 0;
    public static final int DTWAIN_VER16 = 1;
    public static final int DTWAIN_VER17 = 2;
    public static final int DTWAIN_VER18 = 3;
    public static final int DTWAIN_VER20 = 4;
    public static final int DTWAIN_VER21 = 5;
    public static final int DTWAIN_VER22 = 6;
    public static final int DTWAIN_ACQUIREALL = (-1);
    public static final int DTWAIN_MAXACQUIRE = (-1);
    public static final int DTWAIN_DX_NONE = 0;
    public static final int DTWAIN_DX_1PASSDUPLEX = 1;
    public static final int DTWAIN_DX_2PASSDUPLEX = 2;
    public static final int DTWAIN_CURRENT = (-2);
    public static final int DTWAIN_DEFAULT = (-1);
    public static final double DTWAIN_FLOATDEFAULT = (-9999.0);
    public static final int DTWAIN_CallbackERROR = 1;
    public static final int DTWAIN_CallbackMESSAGE = 2;
    public static final int DTWAIN_USENATIVE = 1;
    public static final int DTWAIN_USEBUFFERED = 2;
    public static final int DTWAIN_USECOMPRESSION = 4;
    public static final int DTWAIN_FAILURE1 = (-1);
    public static final int DTWAIN_FAILURE2 = (-2);
    public static final int DTWAIN_DELETEALL = (-1);
    public static final int DTWAIN_PDFOCR_CLEANTEXT1 = 1;
    public static final int DTWAIN_PDFOCR_CLEANTEXT2 = 2;
    public static final int DTWAIN_MODAL = 0;
    public static final int DTWAIN_MODELESS = 1;
    public static final int DTWAIN_UIModeCLOSE = 0;
    public static final int DTWAIN_UIModeOPEN = 1;
    public static final int DTWAIN_REOPEN_SOURCE = 2;
    public static final double DTWAIN_FLOATDELTA = (+1.0e-8);
    public static final int DTWAIN_OR_ANYROTATION = (-1);
    public static final int DTWAIN_CO_GET = 0x0001;
    public static final int DTWAIN_CO_SET = 0x0002;
    public static final int DTWAIN_CO_GETDEFAULT = 0x0004;
    public static final int DTWAIN_CO_GETCURRENT = 0x0008;
    public static final int DTWAIN_CO_RESET = 0x0010;
    public static final int DTWAIN_CO_SETCONSTRAINT = 0x0020;
    public static final int DTWAIN_CO_CONSTRAINABLE = 0x0040;
    public static final int DTWAIN_CO_GETHELP = 0x0100;
    public static final int DTWAIN_CO_GETLABEL = 0x0200;
    public static final int DTWAIN_CO_GETLABELENUM = 0x0400;
    public static final int DTWAIN_CNTYAFGHANISTAN = 1001;
    public static final int DTWAIN_CNTYALGERIA = 213;
    public static final int DTWAIN_CNTYAMERICANSAMOA = 684;
    public static final int DTWAIN_CNTYANDORRA = 033;
    public static final int DTWAIN_CNTYANGOLA = 1002;
    public static final int DTWAIN_CNTYANGUILLA = 8090;
    public static final int DTWAIN_CNTYANTIGUA = 8091;
    public static final int DTWAIN_CNTYARGENTINA = 54;
    public static final int DTWAIN_CNTYARUBA = 297;
    public static final int DTWAIN_CNTYASCENSIONI = 247;
    public static final int DTWAIN_CNTYAUSTRALIA = 61;
    public static final int DTWAIN_CNTYAUSTRIA = 43;
    public static final int DTWAIN_CNTYBAHAMAS = 8092;
    public static final int DTWAIN_CNTYBAHRAIN = 973;
    public static final int DTWAIN_CNTYBANGLADESH = 880;
    public static final int DTWAIN_CNTYBARBADOS = 8093;
    public static final int DTWAIN_CNTYBELGIUM = 32;
    public static final int DTWAIN_CNTYBELIZE = 501;
    public static final int DTWAIN_CNTYBENIN = 229;
    public static final int DTWAIN_CNTYBERMUDA = 8094;
    public static final int DTWAIN_CNTYBHUTAN = 1003;
    public static final int DTWAIN_CNTYBOLIVIA = 591;
    public static final int DTWAIN_CNTYBOTSWANA = 267;
    public static final int DTWAIN_CNTYBRITAIN = 6;
    public static final int DTWAIN_CNTYBRITVIRGINIS = 8095;
    public static final int DTWAIN_CNTYBRAZIL = 55;
    public static final int DTWAIN_CNTYBRUNEI = 673;
    public static final int DTWAIN_CNTYBULGARIA = 359;
    public static final int DTWAIN_CNTYBURKINAFASO = 1004;
    public static final int DTWAIN_CNTYBURMA = 1005;
    public static final int DTWAIN_CNTYBURUNDI = 1006;
    public static final int DTWAIN_CNTYCAMAROON = 237;
    public static final int DTWAIN_CNTYCANADA = 2;
    public static final int DTWAIN_CNTYCAPEVERDEIS = 238;
    public static final int DTWAIN_CNTYCAYMANIS = 8096;
    public static final int DTWAIN_CNTYCENTRALAFREP = 1007;
    public static final int DTWAIN_CNTYCHAD = 1008;
    public static final int DTWAIN_CNTYCHILE = 56;
    public static final int DTWAIN_CNTYCHINA = 86;
    public static final int DTWAIN_CNTYCHRISTMASIS = 1009;
    public static final int DTWAIN_CNTYCOCOSIS = 1009;
    public static final int DTWAIN_CNTYCOLOMBIA = 57;
    public static final int DTWAIN_CNTYCOMOROS = 1010;
    public static final int DTWAIN_CNTYCONGO = 1011;
    public static final int DTWAIN_CNTYCOOKIS = 1012;
    public static final int DTWAIN_CNTYCOSTARICA = 506;
    public static final int DTWAIN_CNTYCUBA = 005;
    public static final int DTWAIN_CNTYCYPRUS = 357;
    public static final int DTWAIN_CNTYCZECHOSLOVAKIA = 42;
    public static final int DTWAIN_CNTYDENMARK = 45;
    public static final int DTWAIN_CNTYDJIBOUTI = 1013;
    public static final int DTWAIN_CNTYDOMINICA = 8097;
    public static final int DTWAIN_CNTYDOMINCANREP = 8098;
    public static final int DTWAIN_CNTYEASTERIS = 1014;
    public static final int DTWAIN_CNTYECUADOR = 593;
    public static final int DTWAIN_CNTYEGYPT = 20;
    public static final int DTWAIN_CNTYELSALVADOR = 503;
    public static final int DTWAIN_CNTYEQGUINEA = 1015;
    public static final int DTWAIN_CNTYETHIOPIA = 251;
    public static final int DTWAIN_CNTYFALKLANDIS = 1016;
    public static final int DTWAIN_CNTYFAEROEIS = 298;
    public static final int DTWAIN_CNTYFIJIISLANDS = 679;
    public static final int DTWAIN_CNTYFINLAND = 358;
    public static final int DTWAIN_CNTYFRANCE = 33;
    public static final int DTWAIN_CNTYFRANTILLES = 596;
    public static final int DTWAIN_CNTYFRGUIANA = 594;
    public static final int DTWAIN_CNTYFRPOLYNEISA = 689;
    public static final int DTWAIN_CNTYFUTANAIS = 1043;
    public static final int DTWAIN_CNTYGABON = 241;
    public static final int DTWAIN_CNTYGAMBIA = 220;
    public static final int DTWAIN_CNTYGERMANY = 49;
    public static final int DTWAIN_CNTYGHANA = 233;
    public static final int DTWAIN_CNTYGIBRALTER = 350;
    public static final int DTWAIN_CNTYGREECE = 30;
    public static final int DTWAIN_CNTYGREENLAND = 299;
    public static final int DTWAIN_CNTYGRENADA = 8099;
    public static final int DTWAIN_CNTYGRENEDINES = 8015;
    public static final int DTWAIN_CNTYGUADELOUPE = 590;
    public static final int DTWAIN_CNTYGUAM = 671;
    public static final int DTWAIN_CNTYGUANTANAMOBAY = 5399;
    public static final int DTWAIN_CNTYGUATEMALA = 502;
    public static final int DTWAIN_CNTYGUINEA = 224;
    public static final int DTWAIN_CNTYGUINEABISSAU = 1017;
    public static final int DTWAIN_CNTYGUYANA = 592;
    public static final int DTWAIN_CNTYHAITI = 509;
    public static final int DTWAIN_CNTYHONDURAS = 504;
    public static final int DTWAIN_CNTYHONGKONG = 852;
    public static final int DTWAIN_CNTYHUNGARY = 36;
    public static final int DTWAIN_CNTYICELAND = 354;
    public static final int DTWAIN_CNTYINDIA = 91;
    public static final int DTWAIN_CNTYINDONESIA = 62;
    public static final int DTWAIN_CNTYIRAN = 98;
    public static final int DTWAIN_CNTYIRAQ = 964;
    public static final int DTWAIN_CNTYIRELAND = 353;
    public static final int DTWAIN_CNTYISRAEL = 972;
    public static final int DTWAIN_CNTYITALY = 39;
    public static final int DTWAIN_CNTYIVORYCOAST = 225;
    public static final int DTWAIN_CNTYJAMAICA = 8010;
    public static final int DTWAIN_CNTYJAPAN = 81;
    public static final int DTWAIN_CNTYJORDAN = 962;
    public static final int DTWAIN_CNTYKENYA = 254;
    public static final int DTWAIN_CNTYKIRIBATI = 1018;
    public static final int DTWAIN_CNTYKOREA = 82;
    public static final int DTWAIN_CNTYKUWAIT = 965;
    public static final int DTWAIN_CNTYLAOS = 1019;
    public static final int DTWAIN_CNTYLEBANON = 1020;
    public static final int DTWAIN_CNTYLIBERIA = 231;
    public static final int DTWAIN_CNTYLIBYA = 218;
    public static final int DTWAIN_CNTYLIECHTENSTEIN = 41;
    public static final int DTWAIN_CNTYLUXENBOURG = 352;
    public static final int DTWAIN_CNTYMACAO = 853;
    public static final int DTWAIN_CNTYMADAGASCAR = 1021;
    public static final int DTWAIN_CNTYMALAWI = 265;
    public static final int DTWAIN_CNTYMALAYSIA = 60;
    public static final int DTWAIN_CNTYMALDIVES = 960;
    public static final int DTWAIN_CNTYMALI = 1022;
    public static final int DTWAIN_CNTYMALTA = 356;
    public static final int DTWAIN_CNTYMARSHALLIS = 692;
    public static final int DTWAIN_CNTYMAURITANIA = 1023;
    public static final int DTWAIN_CNTYMAURITIUS = 230;
    public static final int DTWAIN_CNTYMEXICO = 3;
    public static final int DTWAIN_CNTYMICRONESIA = 691;
    public static final int DTWAIN_CNTYMIQUELON = 508;
    public static final int DTWAIN_CNTYMONACO = 33;
    public static final int DTWAIN_CNTYMONGOLIA = 1024;
    public static final int DTWAIN_CNTYMONTSERRAT = 8011;
    public static final int DTWAIN_CNTYMOROCCO = 212;
    public static final int DTWAIN_CNTYMOZAMBIQUE = 1025;
    public static final int DTWAIN_CNTYNAMIBIA = 264;
    public static final int DTWAIN_CNTYNAURU = 1026;
    public static final int DTWAIN_CNTYNEPAL = 977;
    public static final int DTWAIN_CNTYNETHERLANDS = 31;
    public static final int DTWAIN_CNTYNETHANTILLES = 599;
    public static final int DTWAIN_CNTYNEVIS = 8012;
    public static final int DTWAIN_CNTYNEWCALEDONIA = 687;
    public static final int DTWAIN_CNTYNEWZEALAND = 64;
    public static final int DTWAIN_CNTYNICARAGUA = 505;
    public static final int DTWAIN_CNTYNIGER = 227;
    public static final int DTWAIN_CNTYNIGERIA = 234;
    public static final int DTWAIN_CNTYNIUE = 1027;
    public static final int DTWAIN_CNTYNORFOLKI = 1028;
    public static final int DTWAIN_CNTYNORWAY = 47;
    public static final int DTWAIN_CNTYOMAN = 968;
    public static final int DTWAIN_CNTYPAKISTAN = 92;
    public static final int DTWAIN_CNTYPALAU = 1029;
    public static final int DTWAIN_CNTYPANAMA = 507;
    public static final int DTWAIN_CNTYPARAGUAY = 595;
    public static final int DTWAIN_CNTYPERU = 51;
    public static final int DTWAIN_CNTYPHILLIPPINES = 63;
    public static final int DTWAIN_CNTYPITCAIRNIS = 1030;
    public static final int DTWAIN_CNTYPNEWGUINEA = 675;
    public static final int DTWAIN_CNTYPOLAND = 48;
    public static final int DTWAIN_CNTYPORTUGAL = 351;
    public static final int DTWAIN_CNTYQATAR = 974;
    public static final int DTWAIN_CNTYREUNIONI = 1031;
    public static final int DTWAIN_CNTYROMANIA = 40;
    public static final int DTWAIN_CNTYRWANDA = 250;
    public static final int DTWAIN_CNTYSAIPAN = 670;
    public static final int DTWAIN_CNTYSANMARINO = 39;
    public static final int DTWAIN_CNTYSAOTOME = 1033;
    public static final int DTWAIN_CNTYSAUDIARABIA = 966;
    public static final int DTWAIN_CNTYSENEGAL = 221;
    public static final int DTWAIN_CNTYSEYCHELLESIS = 1034;
    public static final int DTWAIN_CNTYSIERRALEONE = 1035;
    public static final int DTWAIN_CNTYSINGAPORE = 65;
    public static final int DTWAIN_CNTYSOLOMONIS = 1036;
    public static final int DTWAIN_CNTYSOMALI = 1037;
    public static final int DTWAIN_CNTYSOUTHAFRICA = 27;
    public static final int DTWAIN_CNTYSPAIN = 34;
    public static final int DTWAIN_CNTYSRILANKA = 94;
    public static final int DTWAIN_CNTYSTHELENA = 1032;
    public static final int DTWAIN_CNTYSTKITTS = 8013;
    public static final int DTWAIN_CNTYSTLUCIA = 8014;
    public static final int DTWAIN_CNTYSTPIERRE = 508;
    public static final int DTWAIN_CNTYSTVINCENT = 8015;
    public static final int DTWAIN_CNTYSUDAN = 1038;
    public static final int DTWAIN_CNTYSURINAME = 597;
    public static final int DTWAIN_CNTYSWAZILAND = 268;
    public static final int DTWAIN_CNTYSWEDEN = 46;
    public static final int DTWAIN_CNTYSWITZERLAND = 41;
    public static final int DTWAIN_CNTYSYRIA = 1039;
    public static final int DTWAIN_CNTYTAIWAN = 886;
    public static final int DTWAIN_CNTYTANZANIA = 255;
    public static final int DTWAIN_CNTYTHAILAND = 66;
    public static final int DTWAIN_CNTYTOBAGO = 8016;
    public static final int DTWAIN_CNTYTOGO = 228;
    public static final int DTWAIN_CNTYTONGAIS = 676;
    public static final int DTWAIN_CNTYTRINIDAD = 8016;
    public static final int DTWAIN_CNTYTUNISIA = 216;
    public static final int DTWAIN_CNTYTURKEY = 90;
    public static final int DTWAIN_CNTYTURKSCAICOS = 8017;
    public static final int DTWAIN_CNTYTUVALU = 1040;
    public static final int DTWAIN_CNTYUGANDA = 256;
    public static final int DTWAIN_CNTYUSSR = 7;
    public static final int DTWAIN_CNTYUAEMIRATES = 971;
    public static final int DTWAIN_CNTYUNITEDKINGDOM = 44;
    public static final int DTWAIN_CNTYUSA = 1;
    public static final int DTWAIN_CNTYURUGUAY = 598;
    public static final int DTWAIN_CNTYVANUATU = 1041;
    public static final int DTWAIN_CNTYVATICANCITY = 39;
    public static final int DTWAIN_CNTYVENEZUELA = 58;
    public static final int DTWAIN_CNTYWAKE = 1042;
    public static final int DTWAIN_CNTYWALLISIS = 1043;
    public static final int DTWAIN_CNTYWESTERNSAHARA = 1044;
    public static final int DTWAIN_CNTYWESTERNSAMOA = 1045;
    public static final int DTWAIN_CNTYYEMEN = 1046;
    public static final int DTWAIN_CNTYYUGOSLAVIA = 38;
    public static final int DTWAIN_CNTYZAIRE = 243;
    public static final int DTWAIN_CNTYZAMBIA = 260;
    public static final int DTWAIN_CNTYZIMBABWE = 263;
    public static final int DTWAIN_LANGDANISH = 0;
    public static final int DTWAIN_LANGDUTCH = 1;
    public static final int DTWAIN_LANGINTERNATIONALENGLISH = 2;
    public static final int DTWAIN_LANGFRENCHCANADIAN = 3;
    public static final int DTWAIN_LANGFINNISH = 4;
    public static final int DTWAIN_LANGFRENCH = 5;
    public static final int DTWAIN_LANGGERMAN = 6;
    public static final int DTWAIN_LANGICELANDIC = 7;
    public static final int DTWAIN_LANGITALIAN = 8;
    public static final int DTWAIN_LANGNORWEGIAN = 9;
    public static final int DTWAIN_LANGPORTUGUESE = 10;
    public static final int DTWAIN_LANGSPANISH = 11;
    public static final int DTWAIN_LANGSWEDISH = 12;
    public static final int DTWAIN_LANGUSAENGLISH = 13;
    public static final int DTWAIN_NO_ERROR = (0);
    public static final int DTWAIN_DE_CHKAUTOCAPTURE = 1;
    public static final int DTWAIN_DE_CHKBATTERY = 2;
    public static final int DTWAIN_DE_CHKDEVICEONLINE = 4;
    public static final int DTWAIN_DE_CHKFLASH = 8;
    public static final int DTWAIN_DE_CHKPOWERSUPPLY = 16;
    public static final int DTWAIN_DE_CHKRESOLUTION = 32;
    public static final int DTWAIN_DE_DEVICEADDED = 64;
    public static final int DTWAIN_DE_DEVICEOFFLINE = 128;
    public static final int DTWAIN_DE_DEVICEREADY = 256;
    public static final int DTWAIN_DE_DEVICEREMOVED = 512;
    public static final int DTWAIN_DE_IMAGECAPTURED = 1024;
    public static final int DTWAIN_DE_IMAGEDELETED = 2048;
    public static final int DTWAIN_DE_PAPERDOUBLEFEED = 4096;
    public static final int DTWAIN_DE_PAPERJAM = 8192;
    public static final int DTWAIN_DE_LAMPFAILURE = 16384;
    public static final int DTWAIN_DE_POWERSAVE = 32768;
    public static final int DTWAIN_DE_POWERSAVENOTIFY = 65536;
    public static final int DTWAIN_DE_CUSTOMEVENTS = 0x8000;
    public static final int DTWAIN_GETDE_EVENT = 0;
    public static final int DTWAIN_GETDE_DEVNAME = 1;
    public static final int DTWAIN_GETDE_BATTERYMINUTES = 2;
    public static final int DTWAIN_GETDE_BATTERYPCT = 3;
    public static final int DTWAIN_GETDE_XRESOLUTION = 4;
    public static final int DTWAIN_GETDE_YRESOLUTION = 5;
    public static final int DTWAIN_GETDE_FLASHUSED = 6;
    public static final int DTWAIN_GETDE_AUTOCAPTURE = 7;
    public static final int DTWAIN_GETDE_TIMEBEFORECAPTURE = 8;
    public static final int DTWAIN_GETDE_TIMEBETWEENCAPTURES = 9;
    public static final int DTWAIN_GETDE_POWERSUPPLY = 10;
    public static final int DTWAIN_IMPRINTERTOPBEFORE = 1;
    public static final int DTWAIN_IMPRINTERTOPAFTER = 2;
    public static final int DTWAIN_IMPRINTERBOTTOMBEFORE = 4;
    public static final int DTWAIN_IMPRINTERBOTTOMAFTER = 8;
    public static final int DTWAIN_ENDORSERTOPBEFORE = 16;
    public static final int DTWAIN_ENDORSERTOPAFTER = 32;
    public static final int DTWAIN_ENDORSERBOTTOMBEFORE = 64;
    public static final int DTWAIN_ENDORSERBOTTOMAFTER = 128;
    public static final int DTWAIN_PM_SINGLESTRING = 0;
    public static final int DTWAIN_PM_MULTISTRING = 1;
    public static final int DTWAIN_PM_COMPOUNDSTRING = 2;
    public static final int DTWAIN_TWTY_INT8 = 0x0000;
    public static final int DTWAIN_TWTY_INT16 = 0x0001;
    public static final int DTWAIN_TWTY_INT32 = 0x0002;
    public static final int DTWAIN_TWTY_UINT8 = 0x0003;
    public static final int DTWAIN_TWTY_UINT16 = 0x0004;
    public static final int DTWAIN_TWTY_UINT32 = 0x0005;
    public static final int DTWAIN_TWTY_BOOL = 0x0006;
    public static final int DTWAIN_TWTY_FIX32 = 0x0007;
    public static final int DTWAIN_TWTY_FRAME = 0x0008;
    public static final int DTWAIN_TWTY_STR32 = 0x0009;
    public static final int DTWAIN_TWTY_STR64 = 0x000A;
    public static final int DTWAIN_TWTY_STR128 = 0x000B;
    public static final int DTWAIN_TWTY_STR255 = 0x000C;
    public static final int DTWAIN_TWTY_STR1024 = 0x000D;
    public static final int DTWAIN_TWTY_UNI512 = 0x000E;
    public static final int DTWAIN_EI_BARCODEX = 0x1200;
    public static final int DTWAIN_EI_BARCODEY = 0x1201;
    public static final int DTWAIN_EI_BARCODETEXT = 0x1202;
    public static final int DTWAIN_EI_BARCODETYPE = 0x1203;
    public static final int DTWAIN_EI_DESHADETOP = 0x1204;
    public static final int DTWAIN_EI_DESHADELEFT = 0x1205;
    public static final int DTWAIN_EI_DESHADEHEIGHT = 0x1206;
    public static final int DTWAIN_EI_DESHADEWIDTH = 0x1207;
    public static final int DTWAIN_EI_DESHADESIZE = 0x1208;
    public static final int DTWAIN_EI_SPECKLESREMOVED = 0x1209;
    public static final int DTWAIN_EI_HORZLINEXCOORD = 0x120A;
    public static final int DTWAIN_EI_HORZLINEYCOORD = 0x120B;
    public static final int DTWAIN_EI_HORZLINELENGTH = 0x120C;
    public static final int DTWAIN_EI_HORZLINETHICKNESS = 0x120D;
    public static final int DTWAIN_EI_VERTLINEXCOORD = 0x120E;
    public static final int DTWAIN_EI_VERTLINEYCOORD = 0x120F;
    public static final int DTWAIN_EI_VERTLINELENGTH = 0x1210;
    public static final int DTWAIN_EI_VERTLINETHICKNESS = 0x1211;
    public static final int DTWAIN_EI_PATCHCODE = 0x1212;
    public static final int DTWAIN_EI_ENDORSEDTEXT = 0x1213;
    public static final int DTWAIN_EI_FORMCONFIDENCE = 0x1214;
    public static final int DTWAIN_EI_FORMTEMPLATEMATCH = 0x1215;
    public static final int DTWAIN_EI_FORMTEMPLATEPAGEMATCH = 0x1216;
    public static final int DTWAIN_EI_FORMHORZDOCOFFSET = 0x1217;
    public static final int DTWAIN_EI_FORMVERTDOCOFFSET = 0x1218;
    public static final int DTWAIN_EI_BARCODECOUNT = 0x1219;
    public static final int DTWAIN_EI_BARCODECONFIDENCE = 0x121A;
    public static final int DTWAIN_EI_BARCODEROTATION = 0x121B;
    public static final int DTWAIN_EI_BARCODETEXTLENGTH = 0x121C;
    public static final int DTWAIN_EI_DESHADECOUNT = 0x121D;
    public static final int DTWAIN_EI_DESHADEBLACKCOUNTOLD = 0x121E;
    public static final int DTWAIN_EI_DESHADEBLACKCOUNTNEW = 0x121F;
    public static final int DTWAIN_EI_DESHADEBLACKRLMIN = 0x1220;
    public static final int DTWAIN_EI_DESHADEBLACKRLMAX = 0x1221;
    public static final int DTWAIN_EI_DESHADEWHITECOUNTOLD = 0x1222;
    public static final int DTWAIN_EI_DESHADEWHITECOUNTNEW = 0x1223;
    public static final int DTWAIN_EI_DESHADEWHITERLMIN = 0x1224;
    public static final int DTWAIN_EI_DESHADEWHITERLAVE = 0x1225;
    public static final int DTWAIN_EI_DESHADEWHITERLMAX = 0x1226;
    public static final int DTWAIN_EI_BLACKSPECKLESREMOVED = 0x1227;
    public static final int DTWAIN_EI_WHITESPECKLESREMOVED = 0x1228;
    public static final int DTWAIN_EI_HORZLINECOUNT = 0x1229;
    public static final int DTWAIN_EI_VERTLINECOUNT = 0x122A;
    public static final int DTWAIN_EI_DESKEWSTATUS = 0x122B;
    public static final int DTWAIN_EI_SKEWORIGINALANGLE = 0x122C;
    public static final int DTWAIN_EI_SKEWFINALANGLE = 0x122D;
    public static final int DTWAIN_EI_SKEWCONFIDENCE = 0x122E;
    public static final int DTWAIN_EI_SKEWWINDOWX1 = 0x122F;
    public static final int DTWAIN_EI_SKEWWINDOWY1 = 0x1230;
    public static final int DTWAIN_EI_SKEWWINDOWX2 = 0x1231;
    public static final int DTWAIN_EI_SKEWWINDOWY2 = 0x1232;
    public static final int DTWAIN_EI_SKEWWINDOWX3 = 0x1233;
    public static final int DTWAIN_EI_SKEWWINDOWY3 = 0x1234;
    public static final int DTWAIN_EI_SKEWWINDOWX4 = 0x1235;
    public static final int DTWAIN_EI_SKEWWINDOWY4 = 0x1236;
    public static final int DTWAIN_EI_BOOKNAME = 0x1238;
    public static final int DTWAIN_EI_CHAPTERNUMBER = 0x1239;
    public static final int DTWAIN_EI_DOCUMENTNUMBER = 0x123A;
    public static final int DTWAIN_EI_PAGENUMBER = 0x123B;
    public static final int DTWAIN_EI_CAMERA = 0x123C;
    public static final int DTWAIN_EI_FRAMENUMBER = 0x123D;
    public static final int DTWAIN_EI_FRAME = 0x123E;
    public static final int DTWAIN_EI_PIXELFLAVOR = 0x123F;
    public static final int DTWAINGCD_RETURNHANDLE = 1;
    public static final int DTWAINGCD_COPYDATA = 2;
    public static final int DTWAIN_BYPOSITION = 0;
    public static final int DTWAIN_BYID = 1;
    public static final int DTWAINSCD_USEHANDLE = 1;
    public static final int DTWAINSCD_USEDATA = 2;
    public static final int DTWAIN_PAGEFAIL_RETRY = 1;
    public static final int DTWAIN_PAGEFAIL_TERMINATE = 2;
    public static final int DTWAIN_MAXRETRY_ATTEMPTS = 3;
    public static final int DTWAIN_RETRY_FOREVER = (-1);
    public static final int DTWAIN_PS_REGULAR = 0;
    public static final int DTWAIN_PS_ENCAPSULATED = 1;
    public static final int DTWAIN_BP_AUTODISCARD_NONE = 0;
    public static final int DTWAIN_BP_AUTODISCARD_IMMEDIATE = 1;
    public static final int DTWAIN_BP_AUTODISCARD_AFTERPROCESS = 2;
    public static final int DTWAIN_BP_AUTODISCARD_ANY = 0xFFFF;
    public static final int DTWAIN_LP_REFLECTIVE = 0;
    public static final int DTWAIN_LP_TRANSMISSIVE = 1;
    public static final int DTWAIN_LS_RED = 0;
    public static final int DTWAIN_LS_GREEN = 1;
    public static final int DTWAIN_LS_BLUE = 2;
    public static final int DTWAIN_LS_NONE = 3;
    public static final int DTWAIN_LS_WHITE = 4;
    public static final int DTWAIN_LS_UV = 5;
    public static final int DTWAIN_LS_IR = 6;


    public static final int DTWAIN_RES_ENGLISH = 0;
    public static final int DTWAIN_RES_FRENCH = 1;
    public static final int DTWAIN_RES_SPANISH = 2;
    public static final int DTWAIN_RES_DUTCH = 3;
    public static final int DTWAIN_RES_GERMAN = 4;
    public static final int DTWAIN_RES_ITALIAN = 5;
    public static final int DTWAIN_AL_ALARM = 0;
    public static final int DTWAIN_AL_FEEDERERROR = 1;
    public static final int DTWAIN_AL_FEEDERWARNING = 2;
    public static final int DTWAIN_AL_BARCODE = 3;
    public static final int DTWAIN_AL_DOUBLEFEED = 4;
    public static final int DTWAIN_AL_JAM = 5;
    public static final int DTWAIN_AL_PATCHCODE = 6;
    public static final int DTWAIN_AL_POWER = 7;
    public static final int DTWAIN_AL_SKEW = 8;
    public static final int DTWAIN_FT_CAMERA = 0;
    public static final int DTWAIN_FT_CAMERATOP = 1;
    public static final int DTWAIN_FT_CAMERABOTTOM = 2;
    public static final int DTWAIN_FT_CAMERAPREVIEW = 3;
    public static final int DTWAIN_FT_DOMAIN = 4;
    public static final int DTWAIN_FT_HOST = 5;
    public static final int DTWAIN_FT_DIRECTORY = 6;
    public static final int DTWAIN_FT_IMAGE = 7;
    public static final int DTWAIN_FT_UNKNOWN = 8;
    public static final int DTWAIN_NF_NONE = 0;
    public static final int DTWAIN_NF_AUTO = 1;
    public static final int DTWAIN_NF_LONEPIXEL = 2;
    public static final int DTWAIN_NF_MAJORITYRULE = 3;
    public static final int DTWAIN_CB_AUTO = 0;
    public static final int DTWAIN_CB_CLEAR = 1;
    public static final int DTWAIN_CB_NOCLEAR = 2;
    public static final int DTWAIN_FA_NONE = 0;
    public static final int DTWAIN_FA_LEFT = 1;
    public static final int DTWAIN_FA_CENTER = 2;
    public static final int DTWAIN_FA_RIGHT = 3;
    public static final int DTWAIN_PF_CHOCOLATE = 0;
    public static final int DTWAIN_PF_VANILLA = 1;
    public static final int DTWAIN_FO_FIRSTPAGEFIRST = 0;
    public static final int DTWAIN_FO_LASTPAGEFIRST = 1;
    public static final int DTWAIN_INCREMENT_STATIC = 0;
    public static final int DTWAIN_INCREMENT_DYNAMIC = 1;
    public static final int DTWAIN_INCREMENT_DEFAULT = -1;
    public static final int DTWAIN_MANDUP_SCANOK = 1;
    public static final int DTWAIN_MANDUP_SIDE1RESCAN = 2;
    public static final int DTWAIN_MANDUP_SIDE2RESCAN = 3;
    public static final int DTWAIN_MANDUP_RESCANALL = 4;
    public static final int DTWAIN_MANDUP_PAGEMISSING = 5;
    public static final int DTWAIN_DEMODLL_VERSION = 0x00000001;
    public static final int DTWAIN_UNLICENSED_VERSION = 0x00000002;
    public static final int DTWAIN_COMPANY_VERSION = 0x00000004;
    public static final int DTWAIN_GENERAL_VERSION = 0x00000008;
    public static final int DTWAIN_DEVELOP_VERSION = 0x00000010;
    public static final int DTWAIN_JAVA_VERSION = 0x00000020;
    public static final int DTWAIN_TOOLKIT_VERSION = 0x00000040;
    public static final int DTWAIN_LIMITEDDLL_VERSION = 0x00000080;
    public static final int DTWAIN_STATICLIB_VERSION = 0x00000100;
    public static final int DTWAIN_STATICLIB_STDCALL_VERSION = 0x00000200;
    public static final int DTWAIN_PDF_VERSION = 0x00010000;
    public static final int DTWAIN_TWAINSAVE_VERSION = 0x00020000;
    public static final int DTWAIN_OCR_VERSION = 0x00040000;
    public static final int DTWAIN_BARCODE_VERSION = 0x00080000;
    public static final int DTWAIN_ACTIVEX_VERSION = 0x00100000;
    public static final int DTWAIN_32BIT_VERSION = 0x00200000;
    public static final int DTWAIN_64BIT_VERSION = 0x00400000;
    public static final int DTWAIN_UNICODE_VERSION = 0x00800000;
    public static final int DTWAINOCR_RETURNHANDLE = 1;
    public static final int DTWAINOCR_COPYDATA = 2;
    public static final int DTWAIN_OCRINFO_CHAR = 0;
    public static final int DTWAIN_OCRINFO_CHARXPOS = 1;
    public static final int DTWAIN_OCRINFO_CHARYPOS = 2;
    public static final int DTWAIN_OCRINFO_CHARXWIDTH = 3;
    public static final int DTWAIN_OCRINFO_CHARYWIDTH = 4;
    public static final int DTWAIN_OCRINFO_CHARCONFIDENCE = 5;
    public static final int DTWAIN_OCRINFO_PAGENUM = 6;
    public static final int DTWAIN_OCRINFO_OCRENGINE = 7;
    public static final int DTWAIN_OCRINFO_TEXTLENGTH = 8;
    public static final int DTWAIN_PDFPAGETYPE_COLOR = 0;
    public static final int DTWAIN_PDFPAGETYPE_BW = 1;
    public static final int DTWAIN_TWAINDSMSEARCH_NOTFOUND = (-1);
    public static final int DTWAIN_TWAINDSMSEARCH_WSO = 0;
    public static final int DTWAIN_TWAINDSMSEARCH_WOS = 1;
    public static final int DTWAIN_TWAINDSMSEARCH_SWO = 2;
    public static final int DTWAIN_TWAINDSMSEARCH_SOW = 3;
    public static final int DTWAIN_TWAINDSMSEARCH_OWS = 4;
    public static final int DTWAIN_TWAINDSMSEARCH_OSW = 5;
    public static final int DTWAIN_TWAINDSMSEARCH_W = 6;
    public static final int DTWAIN_TWAINDSMSEARCH_S = 7;
    public static final int DTWAIN_TWAINDSMSEARCH_O = 8;
    public static final int DTWAIN_TWAINDSMSEARCH_WS = 9;
    public static final int DTWAIN_TWAINDSMSEARCH_WO = 10;
    public static final int DTWAIN_TWAINDSMSEARCH_SW = 11;
    public static final int DTWAIN_TWAINDSMSEARCH_SO = 12;
    public static final int DTWAIN_TWAINDSMSEARCH_OW = 13;
    public static final int DTWAIN_TWAINDSMSEARCH_OS = 14;
    public static final int DTWAIN_PDFPOLARITY_POSITIVE = 1;
    public static final int DTWAIN_PDFPOLARITY_NEGATIVE = 2;
    public static final int DTWAIN_TWPF_NORMAL = 0;
    public static final int DTWAIN_TWPF_BOLD = 1;
    public static final int DTWAIN_TWPF_ITALIC = 2;
    public static final int DTWAIN_TWPF_LARGESIZE = 3;
    public static final int DTWAIN_TWPF_SMALLSIZE = 4;
    public static final int DTWAIN_TWCT_PAGE = 0;
    public static final int DTWAIN_TWCT_PATCH1 = 1;
    public static final int DTWAIN_TWCT_PATCH2 = 2;
    public static final int DTWAIN_TWCT_PATCH3 = 3;
    public static final int DTWAIN_TWCT_PATCH4 = 4;
    public static final int DTWAIN_TWCT_PATCHT = 5;
    public static final int DTWAIN_TWCT_PATCH6 = 6;

    public static final int DTWAIN_CV_CAPCUSTOMBASE = 0x8000;
    public static final int DTWAIN_CV_CAPXFERCOUNT = 0x0001;
    public static final int DTWAIN_CV_ICAPCOMPRESSION = 0x0100;
    public static final int DTWAIN_CV_ICAPPIXELTYPE = 0x0101;
    public static final int DTWAIN_CV_ICAPUNITS = 0x0102;
    public static final int DTWAIN_CV_ICAPXFERMECH = 0x0103;
    public static final int DTWAIN_CV_CAPAUTHOR = 0x1000;
    public static final int DTWAIN_CV_CAPCAPTION = 0x1001;
    public static final int DTWAIN_CV_CAPFEEDERENABLED = 0x1002;
    public static final int DTWAIN_CV_CAPFEEDERLOADED = 0x1003;
    public static final int DTWAIN_CV_CAPTIMEDATE = 0x1004;
    public static final int DTWAIN_CV_CAPSUPPORTEDCAPS = 0x1005;
    public static final int DTWAIN_CV_CAPEXTENDEDCAPS = 0x1006;
    public static final int DTWAIN_CV_CAPAUTOFEED = 0x1007;
    public static final int DTWAIN_CV_CAPCLEARPAGE = 0x1008;
    public static final int DTWAIN_CV_CAPFEEDPAGE = 0x1009;
    public static final int DTWAIN_CV_CAPREWINDPAGE = 0x100a;
    public static final int DTWAIN_CV_CAPINDICATORS = 0x100b;
    public static final int DTWAIN_CV_CAPSUPPORTEDCAPSEXT = 0x100c;
    public static final int DTWAIN_CV_CAPPAPERDETECTABLE = 0x100d;
    public static final int DTWAIN_CV_CAPUICONTROLLABLE = 0x100e;
    public static final int DTWAIN_CV_CAPDEVICEONLINE = 0x100f;
    public static final int DTWAIN_CV_CAPAUTOSCAN = 0x1010;
    public static final int DTWAIN_CV_CAPTHUMBNAILSENABLED = 0x1011;
    public static final int DTWAIN_CV_CAPDUPLEX = 0x1012;
    public static final int DTWAIN_CV_CAPDUPLEXENABLED = 0x1013;
    public static final int DTWAIN_CV_CAPENABLEDSUIONLY = 0x1014;
    public static final int DTWAIN_CV_CAPCUSTOMDSDATA = 0x1015;
    public static final int DTWAIN_CV_CAPENDORSER = 0x1016;
    public static final int DTWAIN_CV_CAPJOBCONTROL = 0x1017;
    public static final int DTWAIN_CV_CAPALARMS = 0x1018;
    public static final int DTWAIN_CV_CAPALARMVOLUME = 0x1019;
    public static final int DTWAIN_CV_CAPAUTOMATICCAPTURE = 0x101a;
    public static final int DTWAIN_CV_CAPTIMEBEFOREFIRSTCAPTURE = 0x101b;
    public static final int DTWAIN_CV_CAPTIMEBETWEENCAPTURES = 0x101c;
    public static final int DTWAIN_CV_CAPCLEARBUFFERS = 0x101d;
    public static final int DTWAIN_CV_CAPMAXBATCHBUFFERS = 0x101e;
    public static final int DTWAIN_CV_CAPDEVICETIMEDATE = 0x101f;
    public static final int DTWAIN_CV_CAPPOWERSUPPLY = 0x1020;
    public static final int DTWAIN_CV_CAPCAMERAPREVIEWUI = 0x1021;
    public static final int DTWAIN_CV_CAPDEVICEEVENT = 0x1022;
    public static final int DTWAIN_CV_CAPPAGEMULTIPLEACQUIRE = 0x1023;
    public static final int DTWAIN_CV_CAPSERIALNUMBER = 0x1024;
    public static final int DTWAIN_CV_CAPFILESYSTEM = 0x1025;
    public static final int DTWAIN_CV_CAPPRINTER = 0x1026;
    public static final int DTWAIN_CV_CAPPRINTERENABLED = 0x1027;
    public static final int DTWAIN_CV_CAPPRINTERINDEX = 0x1028;
    public static final int DTWAIN_CV_CAPPRINTERMODE = 0x1029;
    public static final int DTWAIN_CV_CAPPRINTERSTRING = 0x102a;
    public static final int DTWAIN_CV_CAPPRINTERSUFFIX = 0x102b;
    public static final int DTWAIN_CV_CAPLANGUAGE = 0x102c;
    public static final int DTWAIN_CV_CAPFEEDERALIGNMENT = 0x102d;
    public static final int DTWAIN_CV_CAPFEEDERORDER = 0x102e;
    public static final int DTWAIN_CV_CAPPAPERBINDING = 0x102f;
    public static final int DTWAIN_CV_CAPREACQUIREALLOWED = 0x1030;
    public static final int DTWAIN_CV_CAPPASSTHRU = 0x1031;
    public static final int DTWAIN_CV_CAPBATTERYMINUTES = 0x1032;
    public static final int DTWAIN_CV_CAPBATTERYPERCENTAGE = 0x1033;
    public static final int DTWAIN_CV_CAPPOWERDOWNTIME = 0x1034;
    public static final int DTWAIN_CV_CAPSEGMENTED = 0x1035;
    public static final int DTWAIN_CV_CAPCAMERAENABLED = 0x1036;
    public static final int DTWAIN_CV_CAPCAMERAORDER = 0x1037;
    public static final int DTWAIN_CV_CAPMICRENABLED = 0x1038;
    public static final int DTWAIN_CV_CAPFEEDERPREP = 0x1039;
    public static final int DTWAIN_CV_CAPFEEDERPOCKET = 0x103a;
    public static final int DTWAIN_CV_CAPAUTOMATICSENSEMEDIUM = 0x103b;
    public static final int DTWAIN_CV_CAPCUSTOMINTERFACEGUID = 0x103c;
    public static final int DTWAIN_CV_CAPSUPPORTEDCAPSSEGMENTUNIQUE = 0x103d;
    public static final int DTWAIN_CV_CAPSUPPORTEDDATS = 0x103e;
    public static final int DTWAIN_CV_CAPDOUBLEFEEDDETECTION = 0x103f;
    public static final int DTWAIN_CV_CAPDOUBLEFEEDDETECTIONLENGTH = 0x1040;
    public static final int DTWAIN_CV_CAPDOUBLEFEEDDETECTIONSENSITIVITY = 0x1041;
    public static final int DTWAIN_CV_CAPDOUBLEFEEDDETECTIONRESPONSE = 0x1042;
    public static final int DTWAIN_CV_CAPPAPERHANDLING  = 0x1043;
    public static final int DTWAIN_CV_CAPINDICATORSMODE = 0x1044;
    public static final int DTWAIN_CV_CAPPRINTERVERTICALOFFSET  = 0x1045;
    public static final int DTWAIN_CV_CAPPOWERSAVETIME  = 0x1046;
    public static final int DTWAIN_CV_CAPPRINTERCHARROTATION = 0x1047;
    public static final int DTWAIN_CV_CAPPRINTERFONTSTYLE = 0x1048;
    public static final int DTWAIN_CV_CAPPRINTERINDEXLEADCHAR = 0x1049;
    public static final int DTWAIN_CV_CAPPRINTERINDEXMAXVALUE = 0x104A;
    public static final int DTWAIN_CV_CAPPRINTERINDEXNUMDIGITS = 0x104B;
    public static final int DTWAIN_CV_CAPPRINTERINDEXSTEP = 0x104C;
    public static final int DTWAIN_CV_CAPPRINTERINDEXTRIGGER = 0x104D;
    public static final int DTWAIN_CV_CAPPRINTERSTRINGPREVIEW = 0x104E;
    public static final int DTWAIN_CV_ICAPAUTOBRIGHT = 0x1100;
    public static final int DTWAIN_CV_ICAPBRIGHTNESS = 0x1101;
    public static final int DTWAIN_CV_ICAPCONTRAST = 0x1103;
    public static final int DTWAIN_CV_ICAPCUSTHALFTONE = 0x1104;
    public static final int DTWAIN_CV_ICAPEXPOSURETIME = 0x1105;
    public static final int DTWAIN_CV_ICAPFILTER = 0x1106;
    public static final int DTWAIN_CV_ICAPFLASHUSED = 0x1107;
    public static final int DTWAIN_CV_ICAPGAMMA = 0x1108;
    public static final int DTWAIN_CV_ICAPHALFTONES = 0x1109;
    public static final int DTWAIN_CV_ICAPHIGHLIGHT = 0x110a;
    public static final int DTWAIN_CV_ICAPIMAGEFILEFORMAT = 0x110c;
    public static final int DTWAIN_CV_ICAPLAMPSTATE = 0x110d;
    public static final int DTWAIN_CV_ICAPLIGHTSOURCE = 0x110e;
    public static final int DTWAIN_CV_ICAPORIENTATION = 0x1110;
    public static final int DTWAIN_CV_ICAPPHYSICALWIDTH = 0x1111;
    public static final int DTWAIN_CV_ICAPPHYSICALHEIGHT = 0x1112;
    public static final int DTWAIN_CV_ICAPSHADOW = 0x1113;
    public static final int DTWAIN_CV_ICAPFRAMES = 0x1114;
    public static final int DTWAIN_CV_ICAPXNATIVERESOLUTION = 0x1116;
    public static final int DTWAIN_CV_ICAPYNATIVERESOLUTION = 0x1117;
    public static final int DTWAIN_CV_ICAPXRESOLUTION = 0x1118;
    public static final int DTWAIN_CV_ICAPYRESOLUTION = 0x1119;
    public static final int DTWAIN_CV_ICAPMAXFRAMES = 0x111a;
    public static final int DTWAIN_CV_ICAPTILES = 0x111b;
    public static final int DTWAIN_CV_ICAPBITORDER = 0x111c;
    public static final int DTWAIN_CV_ICAPCCITTKFACTOR = 0x111d;
    public static final int DTWAIN_CV_ICAPLIGHTPATH = 0x111e;
    public static final int DTWAIN_CV_ICAPPIXELFLAVOR = 0x111f;
    public static final int DTWAIN_CV_ICAPPLANARCHUNKY = 0x1120;
    public static final int DTWAIN_CV_ICAPROTATION = 0x1121;
    public static final int DTWAIN_CV_ICAPSUPPORTEDSIZES = 0x1122;
    public static final int DTWAIN_CV_ICAPTHRESHOLD = 0x1123;
    public static final int DTWAIN_CV_ICAPXSCALING = 0x1124;
    public static final int DTWAIN_CV_ICAPYSCALING = 0x1125;
    public static final int DTWAIN_CV_ICAPBITORDERCODES = 0x1126;
    public static final int DTWAIN_CV_ICAPPIXELFLAVORCODES = 0x1127;
    public static final int DTWAIN_CV_ICAPJPEGPIXELTYPE = 0x1128;
    public static final int DTWAIN_CV_ICAPTIMEFILL = 0x112a;
    public static final int DTWAIN_CV_ICAPBITDEPTH = 0x112b;
    public static final int DTWAIN_CV_ICAPBITDEPTHREDUCTION = 0x112c;
    public static final int DTWAIN_CV_ICAPUNDEFINEDIMAGESIZE = 0x112d;
    public static final int DTWAIN_CV_ICAPIMAGEDATASET = 0x112e;
    public static final int DTWAIN_CV_ICAPEXTIMAGEINFO = 0x112f;
    public static final int DTWAIN_CV_ICAPMINIMUMHEIGHT = 0x1130;
    public static final int DTWAIN_CV_ICAPMINIMUMWIDTH = 0x1131;
    public static final int DTWAIN_CV_ICAPAUTOBORDERDETECTION = 0x1132;
    public static final int DTWAIN_CV_ICAPAUTODESKEW = 0x1133;
    public static final int DTWAIN_CV_ICAPAUTODISCARDBLANKPAGES = 0x1134;
    public static final int DTWAIN_CV_ICAPAUTOROTATE = 0x1135;
    public static final int DTWAIN_CV_ICAPFLIPROTATION = 0x1136;
    public static final int DTWAIN_CV_ICAPBARCODEDETECTIONENABLED = 0x1137;
    public static final int DTWAIN_CV_ICAPSUPPORTEDBARCODETYPES = 0x1138;
    public static final int DTWAIN_CV_ICAPBARCODEMAXSEARCHPRIORITIES = 0x1139;
    public static final int DTWAIN_CV_ICAPBARCODESEARCHPRIORITIES = 0x113a;
    public static final int DTWAIN_CV_ICAPBARCODESEARCHMODE = 0x113b;
    public static final int DTWAIN_CV_ICAPBARCODEMAXRETRIES = 0x113c;
    public static final int DTWAIN_CV_ICAPBARCODETIMEOUT = 0x113d;
    public static final int DTWAIN_CV_ICAPZOOMFACTOR = 0x113e;
    public static final int DTWAIN_CV_ICAPPATCHCODEDETECTIONENABLED = 0x113f;
    public static final int DTWAIN_CV_ICAPSUPPORTEDPATCHCODETYPES = 0x1140;
    public static final int DTWAIN_CV_ICAPPATCHCODEMAXSEARCHPRIORITIES = 0x1141;
    public static final int DTWAIN_CV_ICAPPATCHCODESEARCHPRIORITIES = 0x1142;
    public static final int DTWAIN_CV_ICAPPATCHCODESEARCHMODE = 0x1143;
    public static final int DTWAIN_CV_ICAPPATCHCODEMAXRETRIES = 0x1144;
    public static final int DTWAIN_CV_ICAPPATCHCODETIMEOUT = 0x1145;
    public static final int DTWAIN_CV_ICAPFLASHUSED2 = 0x1146;
    public static final int DTWAIN_CV_ICAPIMAGEFILTER = 0x1147;
    public static final int DTWAIN_CV_ICAPNOISEFILTER = 0x1148;
    public static final int DTWAIN_CV_ICAPOVERSCAN = 0x1149;
    public static final int DTWAIN_CV_ICAPAUTOMATICBORDERDETECTION = 0x1150;
    public static final int DTWAIN_CV_ICAPAUTOMATICDESKEW = 0x1151;
    public static final int DTWAIN_CV_ICAPAUTOMATICROTATE = 0x1152;
    public static final int DTWAIN_CV_ICAPJPEGQUALITY = 0x1153;
    public static final int DTWAIN_CV_ICAPFEEDERTYPE = 0x1154;
    public static final int DTWAIN_CV_ICAPICCPROFILE = 0x1155;
    public static final int DTWAIN_CV_ICAPAUTOSIZE = 0x1156;
    public static final int DTWAIN_CV_ICAPAUTOMATICCROPUSESFRAME = 0x1157;
    public static final int DTWAIN_CV_ICAPAUTOMATICLENGTHDETECTION = 0x1158;
    public static final int DTWAIN_CV_ICAPAUTOMATICCOLORENABLED = 0x1159;
    public static final int DTWAIN_CV_ICAPAUTOMATICCOLORNONCOLORPIXELTYPE = 0x115a;
    public static final int DTWAIN_CV_ICAPCOLORMANAGEMENTENABLED = 0x115b;
    public static final int DTWAIN_CV_ICAPIMAGEMERGE = 0x115c;
    public static final int DTWAIN_CV_ICAPIMAGEMERGEHEIGHTTHRESHOLD = 0x115d;
    public static final int DTWAIN_CV_ICAPSUPPORTEDEXTIMAGEINFO = 0x115e;
    public static final int DTWAIN_CV_ICAPFILMTYPE = 0x115f;
    public static final int DTWAIN_CV_ICAPMIRROR = 0x1160;
    public static final int DTWAIN_CV_ICAPJPEGSUBSAMPLING = 0x1161;
    public static final int DTWAIN_CV_ACAPAUDIOFILEFORMAT = 0x1201;
    public static final int DTWAIN_CV_ACAPXFERMECH = 0x1202;


    public static final int DTWAIN_CFMCV_CAPCFMSTART = 2048;
    public static final int DTWAIN_CFMCV_CAPDUPLEXSCANNER = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+10);
    public static final int DTWAIN_CFMCV_CAPDUPLEXENABLE = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+11);
    public static final int DTWAIN_CFMCV_CAPSCANNERNAME = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+12);
    public static final int DTWAIN_CFMCV_CAPSINGLEPASS = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+13);
    public static final int DTWAIN_CFMCV_CAPERRHANDLING = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+20);
    public static final int DTWAIN_CFMCV_CAPFEEDERSTATUS = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+21);
    public static final int DTWAIN_CFMCV_CAPFEEDMEDIUMWAIT = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+22);
    public static final int DTWAIN_CFMCV_CAPFEEDWAITTIME = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+23);
    public static final int DTWAIN_CFMCV_ICAPWHITEBALANCE = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+24);
    public static final int DTWAIN_CFMCV_ICAPAUTOBINARY = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+25);
    public static final int DTWAIN_CFMCV_ICAPIMAGESEPARATION = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+26);
    public static final int DTWAIN_CFMCV_ICAPHARDWARECOMPRESSION = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+27);
    public static final int DTWAIN_CFMCV_ICAPIMAGEEMPHASIS = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+28);
    public static final int DTWAIN_CFMCV_ICAPOUTLINING = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+29);
    public static final int DTWAIN_CFMCV_ICAPDYNTHRESHOLD = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+30);
    public static final int DTWAIN_CFMCV_ICAPVARIANCE = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+31);
    public static final int DTWAIN_CFMCV_CAPENDORSERAVAILABLE = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+32);
    public static final int DTWAIN_CFMCV_CAPENDORSERENABLE = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+33);
    public static final int DTWAIN_CFMCV_CAPENDORSERCHARSET = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+34);
    public static final int DTWAIN_CFMCV_CAPENDORSERSTRINGLENGTH = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+35);
    public static final int DTWAIN_CFMCV_CAPENDORSERSTRING = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+36);
    public static final int DTWAIN_CFMCV_ICAPDYNTHRESHOLDCURVE = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+48);
    public static final int DTWAIN_CFMCV_ICAPSMOOTHINGMODE = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+49);
    public static final int DTWAIN_CFMCV_ICAPFILTERMODE = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+50);
    public static final int DTWAIN_CFMCV_ICAPGRADATION = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+51);
    public static final int DTWAIN_CFMCV_ICAPMIRROR = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+52);
    public static final int DTWAIN_CFMCV_ICAPEASYSCANMODE = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+53);
    public static final int DTWAIN_CFMCV_ICAPSOFTWAREINTERPOLATION = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+54);
    public static final int DTWAIN_CFMCV_ICAPIMAGESEPARATIONEX = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+55);
    public static final int DTWAIN_CFMCV_CAPDUPLEXPAGE = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+56);
    public static final int DTWAIN_CFMCV_ICAPINVERTIMAGE = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+57);
    public static final int DTWAIN_CFMCV_ICAPSPECKLEREMOVE = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+58);
    public static final int DTWAIN_CFMCV_ICAPUSMFILTER = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+59);
    public static final int DTWAIN_CFMCV_ICAPNOISEFILTERCFM = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+60);
    public static final int DTWAIN_CFMCV_ICAPDESCREENING = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+61);
    public static final int DTWAIN_CFMCV_ICAPQUALITYFILTER = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+62);
    public static final int DTWAIN_CFMCV_ICAPBINARYFILTER = (DTWAIN_CV_CAPCUSTOMBASE+DTWAIN_CFMCV_CAPCFMSTART+63);
    public static final int DTWAIN_OCRCV_IMAGEFILEFORMAT = 0x1000;
    public static final int DTWAIN_OCRCV_DESKEW = 0x1001;
    public static final int DTWAIN_OCRCV_DESHADE = 0x1002;
    public static final int DTWAIN_OCRCV_ORIENTATION = 0x1003;
    public static final int DTWAIN_OCRCV_NOISEREMOVE = 0x1004;
    public static final int DTWAIN_OCRCV_LINEREMOVE = 0x1005;
    public static final int DTWAIN_OCRCV_INVERTPAGE = 0x1006;
    public static final int DTWAIN_OCRCV_INVERTZONES = 0x1007;
    public static final int DTWAIN_OCRCV_LINEREJECT = 0x1008;
    public static final int DTWAIN_OCRCV_CHARACTERREJECT = 0x1009;
    public static final int DTWAIN_OCRCV_ERRORREPORTMODE = 0x1010;
    public static final int DTWAIN_OCRCV_ERRORREPORTFILE = 0x1011;
    public static final int DTWAIN_OCRCV_PIXELTYPE = 0x1012;
    public static final int DTWAIN_OCRCV_BITDEPTH = 0x1013;
    public static final int DTWAIN_OCRCV_RETURNCHARINFO = 0x1014;
    public static final int DTWAIN_OCRCV_NATIVEFILEFORMAT = 0x1015;
    public static final int DTWAIN_OCRCV_MPNATIVEFILEFORMAT = 0x1016;
    public static final int DTWAIN_OCRCV_SUPPORTEDCAPS = 0x1017;
    public static final int DTWAIN_OCRCV_DISABLECHARACTERS = 0x1018;
    public static final int DTWAIN_OCRCV_REMOVECONTROLCHARS = 0x1019;
    public static final int DTWAIN_OCRORIENT_OFF = 0;
    public static final int DTWAIN_OCRORIENT_AUTO = 1;
    public static final int DTWAIN_OCRORIENT_90 = 2;
    public static final int DTWAIN_OCRORIENT_180 = 3;
    public static final int DTWAIN_OCRORIENT_270 = 4;
    public static final int DTWAIN_OCRIMAGEFORMAT_AUTO = 10000;
    public static final int DTWAIN_OCRERROR_MODENONE = 0;
    public static final int DTWAIN_OCRERROR_SHOWMSGBOX = 1;
    public static final int DTWAIN_OCRERROR_WRITEFILE = 2;
    public static final int DTWAIN_PDFTEXT_ALLPAGES = 0x00000001;
    public static final int DTWAIN_PDFTEXT_EVENPAGES = 0x00000002;
    public static final int DTWAIN_PDFTEXT_ODDPAGES = 0x00000004;
    public static final int DTWAIN_PDFTEXT_FIRSTPAGE = 0x00000008;
    public static final int DTWAIN_PDFTEXT_LASTPAGE = 0x00000010;
    public static final int DTWAIN_PDFTEXT_CURRENTPAGE = 0x00000020;
    public static final int DTWAIN_PDFTEXT_DISABLED = 0x00000040;
    public static final int DTWAIN_PDFTEXT_TOPLEFT = 0x00000100;
    public static final int DTWAIN_PDFTEXT_TOPRIGHT = 0x00000200;
    public static final int DTWAIN_PDFTEXT_HORIZCENTER = 0x00000400;
    public static final int DTWAIN_PDFTEXT_VERTCENTER = 0x00000800;
    public static final int DTWAIN_PDFTEXT_BOTTOMLEFT = 0x00001000;
    public static final int DTWAIN_PDFTEXT_BOTTOMRIGHT = 0x00002000;
    public static final int DTWAIN_PDFTEXT_BOTTOMCENTER = 0x00004000;
    public static final int DTWAIN_PDFTEXT_TOPCENTER = 0x00008000;
    public static final int DTWAIN_PDFTEXT_XCENTER = 0x00010000;
    public static final int DTWAIN_PDFTEXT_YCENTER = 0x00020000;
    public static final int DTWAIN_PDFTEXT_NOSCALING = 0x00100000;
    public static final int DTWAIN_PDFTEXT_NOCHARSPACING = 0x00200000;
    public static final int DTWAIN_PDFTEXT_NOWORDSPACING = 0x00400000;
    public static final int DTWAIN_PDFTEXT_NOSTROKEWIDTH = 0x00800000;
    public static final int DTWAIN_PDFTEXT_NORENDERMODE = 0x01000000;
    public static final int DTWAIN_PDFTEXT_NORGBCOLOR = 0x02000000;
    public static final int DTWAIN_PDFTEXT_NOFONTSIZE = 0x04000000;
    public static final int DTWAIN_PDFTEXT_NOABSPOSITION = 0x08000000;
    public static final int DTWAIN_PDFTEXT_IGNOREALL = 0xFFF00000;
    public static final int DTWAIN_FONT_COURIER = 0;
    public static final int DTWAIN_FONT_COURIERBOLD = 1;
    public static final int DTWAIN_FONT_COURIERBOLDOBLIQUE = 2;
    public static final int DTWAIN_FONT_COURIEROBLIQUE = 3;
    public static final int DTWAIN_FONT_HELVETICA = 4;
    public static final int DTWAIN_FONT_HELVETICABOLD = 5;
    public static final int DTWAIN_FONT_HELVETICABOLDOBLIQUE = 6;
    public static final int DTWAIN_FONT_HELVETICAOBLIQUE = 7;
    public static final int DTWAIN_FONT_TIMESBOLD = 8;
    public static final int DTWAIN_FONT_TIMESBOLDITALIC = 9;
    public static final int DTWAIN_FONT_TIMESROMAN = 10;
    public static final int DTWAIN_FONT_TIMESITALIC = 11;
    public static final int DTWAIN_FONT_SYMBOL = 12;
    public static final int DTWAIN_FONT_ZAPFDINGBATS = 13;
    public static final int DTWAIN_PDFRENDER_FILL = 0;
    public static final int DTWAIN_PDFRENDER_STROKE = 1;
    public static final int DTWAIN_PDFRENDER_FILLSTROKE = 2;
    public static final int DTWAIN_PDFRENDER_INVISIBLE = 3;
    public static final int DTWAIN_PDFTEXTELEMENT_SCALINGXY = 0;
    public static final int DTWAIN_PDFTEXTELEMENT_FONTHEIGHT = 1;
    public static final int DTWAIN_PDFTEXTELEMENT_WORDSPACING = 2;
    public static final int DTWAIN_PDFTEXTELEMENT_POSITION = 3;
    public static final int DTWAIN_PDFTEXTELEMENT_COLOR = 4;
    public static final int DTWAIN_PDFTEXTELEMENT_STROKEWIDTH = 5;
    public static final int DTWAIN_PDFTEXTELEMENT_DISPLAYFLAGS = 6;
    public static final int DTWAIN_PDFTEXTELEMENT_FONTNAME = 7;
    public static final int DTWAIN_PDFTEXTELEMENT_TEXT = 8;
    public static final int DTWAIN_PDFTEXTELEMENT_RENDERMODE = 9;
    public static final int DTWAIN_PDFTEXTELEMENT_CHARSPACING = 10;
    public static final int DTWAIN_PDFTEXTELEMENT_ROTATIONANGLE = 11;
    public static final int DTWAIN_PDFTEXTELEMENT_LEADING = 12;
    public static final int DTWAIN_PDFTEXTELEMENT_SCALING = 13;
    public static final int DTWAIN_PDFTEXTELEMENT_TEXTLENGTH = 14;
    public static final int DTWAIN_PDFTEXTELEMENT_SKEWANGLES = 15;
    public static final int DTWAIN_PDFTEXTELEMENT_TRANSFORMORDER = 16;
    public static final int DTWAIN_PDFTEXTTRANSFORM_TSRK = 0;
    public static final int DTWAIN_PDFTEXTTRANSFORM_TSKR = 1;
    public static final int DTWAIN_PDFTEXTTRANSFORM_TKSR = 2;
    public static final int DTWAIN_PDFTEXTTRANSFORM_TKRS = 3;
    public static final int DTWAIN_PDFTEXTTRANSFORM_TRSK = 4;
    public static final int DTWAIN_PDFTEXTTRANSFORM_TRKS = 5;
    public static final int DTWAIN_PDFTEXTTRANSFORM_STRK = 6;
    public static final int DTWAIN_PDFTEXTTRANSFORM_STKR = 7;
    public static final int DTWAIN_PDFTEXTTRANSFORM_SKTR = 8;
    public static final int DTWAIN_PDFTEXTTRANSFORM_SKRT = 9;
    public static final int DTWAIN_PDFTEXTTRANSFORM_SRTK = 10;
    public static final int DTWAIN_PDFTEXTTRANSFORM_SRKT = 11;
    public static final int DTWAIN_PDFTEXTTRANSFORM_RSTK = 12;
    public static final int DTWAIN_PDFTEXTTRANSFORM_RSKT = 13;
    public static final int DTWAIN_PDFTEXTTRANSFORM_RTSK = 14;
    public static final int DTWAIN_PDFTEXTTRANSFORM_RTKT = 15;
    public static final int DTWAIN_PDFTEXTTRANSFORM_RKST = 16;
    public static final int DTWAIN_PDFTEXTTRANSFORM_RKTS = 17;
    public static final int DTWAIN_PDFTEXTTRANSFORM_KSTR = 18;
    public static final int DTWAIN_PDFTEXTTRANSFORM_KSRT = 19;
    public static final int DTWAIN_PDFTEXTTRANSFORM_KRST = 20;
    public static final int DTWAIN_PDFTEXTTRANSFORM_KRTS = 21;
    public static final int DTWAIN_PDFTEXTTRANSFORM_KTSR = 22;
    public static final int DTWAIN_PDFTEXTTRANSFORM_KTRS = 23;
    public static final int DTWAIN_PDFTEXTTRANFORM_LAST = DTWAIN_PDFTEXTTRANSFORM_KTRS;

    public static final int DTWAIN_DGNAME  = 0;
    public static final int DTWAIN_DATNAME = 1;
    public static final int DTWAIN_MSGNAME = 2;*/

}
