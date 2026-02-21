/*
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2026 Dynarithmic Software.

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

    public static class DTwainConstantToString
    {
        public static final int DTWAIN_CONSTANT_TWPT     = 0 ;
        public static final int DTWAIN_CONSTANT_TWUN     = 1 ;
        public static final int DTWAIN_CONSTANT_TWCY     = 2 ;
        public static final int DTWAIN_CONSTANT_TWAL     = 3 ;
        public static final int DTWAIN_CONSTANT_TWAS     = 4 ;
        public static final int DTWAIN_CONSTANT_TWBCOR   = 5 ;
        public static final int DTWAIN_CONSTANT_TWBD     = 6 ;
        public static final int DTWAIN_CONSTANT_TWBO     = 7 ;
        public static final int DTWAIN_CONSTANT_TWBP     = 8 ;
        public static final int DTWAIN_CONSTANT_TWBR     = 9 ;
        public static final int DTWAIN_CONSTANT_TWBT     = 10;
        public static final int DTWAIN_CONSTANT_TWCP     = 11;
        public static final int DTWAIN_CONSTANT_TWCS     = 12;
        public static final int DTWAIN_CONSTANT_TWDE     = 13;
        public static final int DTWAIN_CONSTANT_TWDR     = 14;
        public static final int DTWAIN_CONSTANT_TWDSK    = 15;
        public static final int DTWAIN_CONSTANT_TWDX     = 16;
        public static final int DTWAIN_CONSTANT_TWFA     = 17;   
        public static final int DTWAIN_CONSTANT_TWFE     = 18;   
        public static final int DTWAIN_CONSTANT_TWFF     = 19;
        public static final int DTWAIN_CONSTANT_TWFL     = 20;   
        public static final int DTWAIN_CONSTANT_TWFO     = 21;  
        public static final int DTWAIN_CONSTANT_TWFP     = 22;   
        public static final int DTWAIN_CONSTANT_TWFR     = 23;   
        public static final int DTWAIN_CONSTANT_TWFT     = 24;   
        public static final int DTWAIN_CONSTANT_TWFY     = 22;   
        public static final int DTWAIN_CONSTANT_TWIA     = 23;   
        public static final int DTWAIN_CONSTANT_TWIC     = 27;   
        public static final int DTWAIN_CONSTANT_TWIF     = 28;   
        public static final int DTWAIN_CONSTANT_TWIM     = 29;   
        public static final int DTWAIN_CONSTANT_TWJC     = 30;   
        public static final int DTWAIN_CONSTANT_TWJQ     = 31;   
        public static final int DTWAIN_CONSTANT_TWLP     = 32;   
        public static final int DTWAIN_CONSTANT_TWLS     = 33;   
        public static final int DTWAIN_CONSTANT_TWMD     = 34;  
        public static final int DTWAIN_CONSTANT_TWNF     = 35;  
        public static final int DTWAIN_CONSTANT_TWOR     = 36;  
        public static final int DTWAIN_CONSTANT_TWOV     = 37;  
        public static final int DTWAIN_CONSTANT_TWPA     = 38;  
        public static final int DTWAIN_CONSTANT_TWPC     = 39;  
        public static final int DTWAIN_CONSTANT_TWPCH    = 40;  
        public static final int DTWAIN_CONSTANT_TWPF     = 41;  
        public static final int DTWAIN_CONSTANT_TWPM     = 42;  
        public static final int DTWAIN_CONSTANT_TWPR     = 43;  
        public static final int DTWAIN_CONSTANT_TWPF2    = 44;  
        public static final int DTWAIN_CONSTANT_TWCT     = 45;  
        public static final int DTWAIN_CONSTANT_TWPS     = 46;  
        public static final int DTWAIN_CONSTANT_TWSS     = 47;  
        public static final int DTWAIN_CONSTANT_TWPH     = 48;
        public static final int DTWAIN_CONSTANT_TWCI     = 49;
        public static final int DTWAIN_CONSTANT_FONTNAME = 50;
        public static final int DTWAIN_CONSTANT_TWEI     = 51;
        public static final int DTWAIN_CONSTANT_TWEJ     = 52;
        public static final int DTWAIN_CONSTANT_TWCC     = 53;
        public static final int DTWAIN_CONSTANT_TWQC     = 54;
        public static final int DTWAIN_CONSTANT_TWRC     = 55;
        public static final int DTWAIN_CONSTANT_MSG      = 56;
        public static final int DTWAIN_CONSTANT_TWLG     = 57;
        public static final int DTWAIN_CONSTANT_DLLINFO  = 58;
        public static final int DTWAIN_CONSTANT_DG       = 59;
        public static final int DTWAIN_CONSTANT_DAT      = 60;
        public static final int DTWAIN_CONSTANT_DF       = 61;
        public static final int DTWAIN_CONSTANT_TWTY     = 62;
        public static final int DTWAIN_CONSTANT_TWCB     = 63;
        public static final int DTWAIN_CONSTANT_TWAF     = 64;
        public static final int DTWAIN_CONSTANT_TWFS     = 65;
        public static final int DTWAIN_CONSTANT_TWJS     = 66;
        public static final int DTWAIN_CONSTANT_TWMR     = 67;
        public static final int DTWAIN_CONSTANT_TWDP     = 68;
        public static final int DTWAIN_CONSTANT_TWUS     = 69;
        public static final int DTWAIN_CONSTANT_TWDF     = 70;
        public static final int DTWAIN_CONSTANT_TWFM     = 71;
        public static final int DTWAIN_CONSTANT_TWSG     = 72;
        public static final int DTWAIN_CONSTANT_DTWAIN_TN = 73;
        public static final int DTWAIN_CONSTANT_TWON     = 74;
        public static final int DTWAIN_CONSTANT_TWMF     = 75;
        public static final int DTWAIN_CONSTANT_TWSX     = 76;
        public static final int DTWAIN_CONSTANT_CAP      = 77;
        public static final int DTWAIN_CONSTANT_ICAP     = 78;
        public static final int DTWAIN_CONSTANT_DTWAIN_CONT = 79;
        public static final int DTWAIN_CONSTANT_CAPCODE_MAP = 80;
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
        ICO_RESIZED (8002),
        WBMP (8500),
        GOOGLEWEBP(8501),
        PBM(10000),
        PPM(10000),
        WBMP_RESIZED(11000),
        TGA_RLE(11001),
        BMP_RLE(11002),
        BIGTIFFLZW          (11003),
        BIGTIFFLZWMULTI     (11004),
        BIGTIFFNONE         (11005),
        BIGTIFFNONEMULTI    (11006),
        BIGTIFFPACKBITS     (11007),
        BIGTIFFPACKBITSMULTI(11008),
        BIGTIFFDEFLATE      (11009),
        BIGTIFFDEFLATEMULTI (11010),
        BIGTIFFG3           (11011),
        BIGTIFFG3MULTI      (11012),
        BIGTIFFG4           (11013),
        BIGTIFFG4MULTI      (11014),
        BIGTIFFJPEG         (11015),
        BIGTIFFJPEGMULTI    (11016),
        JPEGXR              (12000),
        SVG                 (13000),
        SVGZ                (13001),
                
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
        BIGTIFFNOCOMPRESS (FileType.TIFFNONEMULTI.value()),
        BIGTIFFG3 (FileType.BIGTIFFG3MULTI.value()),
        BIGTIFFG4 (FileType.BIGTIFFG4MULTI.value()),
        BIGTIFFPACKBITS (FileType.BIGTIFFPACKBITSMULTI.value()),
        BIGTIFFDEFLATE (FileType.BIGTIFFDEFLATEMULTI.value()),
        BIGTIFFJPEG (FileType.BIGTIFFJPEGMULTI.value()),
        BIGTIFFLZW (FileType.BIGTIFFLZWMULTI.value()),
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
        UICLOSING (3000),
        UICLOSED (3001),
        UIOPENED (3002),
        UIOPENING (3003),
        UIOPENFAILURE (3004),
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
        CLOSEDIBFAILED  (1057),
        INVALID_TWAINDSM2_BITMAP (1058),
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
        FEEDERNOTLOADED (1200),
        
        FEEDERTIMEOUT(1202),
        FEEDERNOTENABLED(1203),
        FEEDERNOTSUPPORTED(1204),
        FEEDERTOFLATBED(1205),
        PREACQUIRESTART(1206),
        FILECOMPRESSTYPEMISMATCH(1302),
        
        TRANSFERTILEREADY (1300),
        TRANSFERTILEDONE (1301);

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
        ERROR_NO_MEMFILE_XFER       (-1055),
        ERROR_AREA_ARRAY_TOO_SMALL  (-1056),
        ERROR_LOG_CREATE_ERROR  (-1057),
        ERROR_FILESYSTEM_NOT_SUPPORTED (-1058),
        ERROR_TILEMODE_NOTSET (-1059),
        ERROR_INI32_NOT_FOUND (-1060),
        ERROR_INI64_NOT_FOUND (-1061),
        ERROR_CRC_CHECK (-1062),
        ERROR_RESOURCES_BAD_VERSION (-1063),
        ERROR_WIN32_ERROR (-1064),
        ERROR_STRINGID_NOTFOUND (-1065),
        ERROR_RESOURCES_DUPLICATEID_FOUND (-1066),
        ERROR_UNAVAILABLE_EXTINFO (-1067),
        ERROR_TWAINDSM2_BADBITMAP (-1068),
        ERROR_ACQUISITION_CANCELED (-1069),
        ERROR_IMAGE_RESAMPLED (-1070),
        ERROR_UNKNOWN_TWAIN_RC (-1071),
        ERROR_UNKNOWN_TWAIN_CC (-1072),
        ERROR_RESOURCES_DATA_EXCEPTION (-1073),
        ERROR_AUDIO_TRANSFER_NOTSUPPORTED (-1074),
        ERROR_FEEDER_COMPLIANCY (-1075),
        ERROR_SUPPORTEDCAPS_COMPLIANCY1 (-1076),
        ERROR_SUPPORTEDCAPS_COMPLIANCY2 (-1077),
        ERROR_ICAPPIXELTYPE_COMPLIANCY1 (-1078),
        ERROR_ICAPPIXELTYPE_COMPLIANCY2 (-1079),
        ERROR_ICAPBITDEPTH_COMPLIANCY1 (-1080),
        ERROR_XFERMECH_COMPLIANCY      (-1081),
        ERROR_STANDARDCAPS_COMPLIANCY  (-1082),
        ERROR_EXTIMAGEINFO_DATATYPE_MISMATCH (-1083),
        ERROR_EXTIMAGEINFO_RETRIEVAL (-1084),
        ERROR_RANGE_OUTOFBOUNDS      (-1085),
        ERROR_RANGE_STEPISZERO       (-1086),
        ERROR_BLANKNAMEDETECTED   (-1087),
        
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
        ERROR_INVALID_DIRECTORY (-2077),
        ERROR_CREATE_DIRECTORY (-2078),
        ERROR_OCRLIBRARY_NOTFOUND (-2079),
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
        ERROR_OCR_LAST (-2108),
        ERROR_SOURCE_COULD_NOT_OPEN   (-2500),
        ERROR_SOURCE_COULD_NOT_CLOSE  (-2501),
        ERROR_IMAGEINFO_INVALID       (-2502),
        ERROR_WRITEDATA_TOFILE        (-2503),
        ERROR_OPERATION_NOTSUPPORTED  (-2504);

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
        TOPMOSTWINDOW (1024),
        HIGHLIGHTFIRST (8192),
        SAVELASTSCREENPOS (16384),
        CENTER_CURRENT_MONITOR (32768);

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
    
    public enum TextTransformOptions implements OrdinalEnum
    {
        SCALE_ROTATE_SKEW (0),
        SCALE_SKEW_ROTATE (1),
        SKEW_SCALE_ROTATE (2),
        SKEW_ROTATE_SCALE (3),
        ROTATE_SCALE_SKEW (4),
        ROTATE_SKEW_SCALE (5);

        private final int enumValue;

        TextTransformOptions(int i)
        {
            enumValue = i;
        }

        public int value() { return enumValue; }
        private static final Map<Integer, TextTransformOptions> map = OrdinalEnum.getValues(TextTransformOptions.class);
        public static TextTransformOptions from(int i)
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
        ONMANUALSAVE(4),
        ONINCOMPLETESAVE(128);

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
}
