/*
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2025 Dynarithmic Software.

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
package com.dynarithmic.twain.lowlevel;

public final class TwainConstants
{
    private TwainConstants() {}
    public static class CAPS
    {
        /****************************************************************************
         * Capabilities                                                             *
         ****************************************************************************/
        public static final int  CAP_CUSTOMBASE          = 0x8000; /* Base of custom capabilities */

        /* all data sources are REQUIRED to support these caps */
        public static final int  CAP_XFERCOUNT           = 0x0001;

        /* image data sources are REQUIRED to support these caps */
        public static final int  ICAP_COMPRESSION        = 0x0100;
        public static final int  ICAP_PIXELTYPE          = 0x0101;
        public static final int  ICAP_UNITS              = 0x0102;
        public static final int  ICAP_XFERMECH           = 0x0103;

        /* all data sources MAY support these caps */
        public static final int  CAP_AUTHOR                  = 0x1000;
        public static final int  CAP_CAPTION                 = 0x1001;
        public static final int  CAP_FEEDERENABLED           = 0x1002;
        public static final int  CAP_FEEDERLOADED            = 0x1003;
        public static final int  CAP_TIMEDATE                = 0x1004;
        public static final int  CAP_SUPPORTEDCAPS           = 0x1005;
        public static final int  CAP_EXTENDEDCAPS            = 0x1006;
        public static final int  CAP_AUTOFEED                = 0x1007;
        public static final int  CAP_CLEARPAGE               = 0x1008;
        public static final int  CAP_FEEDPAGE                = 0x1009;
        public static final int  CAP_REWINDPAGE              = 0x100a;
        public static final int  CAP_INDICATORS              = 0x100b;
        public static final int  CAP_PAPERDETECTABLE         = 0x100d;
        public static final int  CAP_UICONTROLLABLE          = 0x100e;
        public static final int  CAP_DEVICEONLINE            = 0x100f;
        public static final int  CAP_AUTOSCAN                = 0x1010;
        public static final int  CAP_THUMBNAILSENABLED       = 0x1011;
        public static final int  CAP_DUPLEX                  = 0x1012;
        public static final int  CAP_DUPLEXENABLED           = 0x1013;
        public static final int  CAP_ENABLEDSUIONLY          = 0x1014;
        public static final int  CAP_CUSTOMDSDATA            = 0x1015;
        public static final int  CAP_ENDORSER                = 0x1016;
        public static final int  CAP_JOBCONTROL              = 0x1017;
        public static final int  CAP_ALARMS                  = 0x1018;
        public static final int  CAP_ALARMVOLUME             = 0x1019;
        public static final int  CAP_AUTOMATICCAPTURE        = 0x101a;
        public static final int  CAP_TIMEBEFOREFIRSTCAPTURE  = 0x101b;
        public static final int  CAP_TIMEBETWEENCAPTURES     = 0x101c;
        public static final int  CAP_MAXBATCHBUFFERS         = 0x101e;
        public static final int  CAP_DEVICETIMEDATE          = 0x101f;
        public static final int  CAP_POWERSUPPLY             = 0x1020;
        public static final int  CAP_CAMERAPREVIEWUI         = 0x1021;
        public static final int  CAP_DEVICEEVENT             = 0x1022;
        public static final int  CAP_SERIALNUMBER            = 0x1024;
        public static final int  CAP_PRINTER                 = 0x1026;
        public static final int  CAP_PRINTERENABLED          = 0x1027;
        public static final int  CAP_PRINTERINDEX            = 0x1028;
        public static final int  CAP_PRINTERMODE             = 0x1029;
        public static final int  CAP_PRINTERSTRING           = 0x102a;
        public static final int  CAP_PRINTERSUFFIX           = 0x102b;
        public static final int  CAP_LANGUAGE                = 0x102c;
        public static final int  CAP_FEEDERALIGNMENT         = 0x102d;
        public static final int  CAP_FEEDERORDER             = 0x102e;
        public static final int  CAP_REACQUIREALLOWED        = 0x1030;
        public static final int  CAP_BATTERYMINUTES          = 0x1032;
        public static final int  CAP_BATTERYPERCENTAGE       = 0x1033;
        public static final int  CAP_CAMERASIDE              = 0x1034;
        public static final int  CAP_SEGMENTED               = 0x1035;
        public static final int  CAP_CAMERAENABLED           = 0x1036;
        public static final int  CAP_CAMERAORDER             = 0x1037;
        public static final int  CAP_MICRENABLED             = 0x1038;
        public static final int  CAP_FEEDERPREP              = 0x1039;
        public static final int  CAP_FEEDERPOCKET            = 0x103a;
        public static final int  CAP_AUTOMATICSENSEMEDIUM    = 0x103b;
        public static final int  CAP_CUSTOMINTERFACEGUID     = 0x103c;
        public static final int  CAP_SUPPORTEDCAPSSEGMENTUNIQUE    = 0x103d;
        public static final int  CAP_SUPPORTEDDATS           = 0x103e;
        public static final int  CAP_DOUBLEFEEDDETECTION     = 0x103f;
        public static final int  CAP_DOUBLEFEEDDETECTIONLENGTH = 0x1040;
        public static final int  CAP_DOUBLEFEEDDETECTIONSENSITIVITY = 0x1041;
        public static final int  CAP_DOUBLEFEEDDETECTIONRESPONSE = 0x1042;
        public static final int  CAP_PAPERHANDLING           = 0x1043;
        public static final int  CAP_INDICATORSMODE          = 0x1044;
        public static final int  CAP_PRINTERVERTICALOFFSET   = 0x1045;
        public static final int  CAP_POWERSAVETIME           = 0x1046;
        public static final int  CAP_PRINTERCHARROTATION     = 0x1047;
        public static final int  CAP_PRINTERFONTSTYLE        = 0x1048;
        public static final int  CAP_PRINTERINDEXLEADCHAR    = 0x1049;
        public static final int  CAP_PRINTERINDEXMAXVALUE    = 0x104A;
        public static final int  CAP_PRINTERINDEXNUMDIGITS   = 0x104B;
        public static final int  CAP_PRINTERINDEXSTEP        = 0x104C;
        public static final int  CAP_PRINTERINDEXTRIGGER     = 0x104D;
        public static final int  CAP_PRINTERSTRINGPREVIEW    = 0x104E;
        public static final int  CAP_SHEETCOUNT              = 0x104F;
        public static final int  CAP_IMAGEADDRESSENABLED     = 0x1050;
        public static final int  CAP_IAFIELDALEVEL          = 0x1051;
        public static final int  CAP_IAFIELDBLEVEL          = 0x1052;
        public static final int  CAP_IAFIELDCLEVEL          = 0x1053;
        public static final int  CAP_IAFIELDDLEVEL          = 0x1054;
        public static final int  CAP_IAFIELDELEVEL          = 0x1055;
        public static final int  CAP_IAFIELDAPRINTFORMAT    = 0x1056;
        public static final int  CAP_IAFIELDBPRINTFORMAT    = 0x1057;
        public static final int  CAP_IAFIELDCPRINTFORMAT    = 0x1058;
        public static final int  CAP_IAFIELDDPRINTFORMAT    = 0x1059;
        public static final int  CAP_IAFIELDEPRINTFORMAT    = 0x105A;
        public static final int  CAP_IAFIELDAVALUE          = 0x105B;
        public static final int  CAP_IAFIELDBVALUE          = 0x105C;
        public static final int  CAP_IAFIELDCVALUE          = 0x105D;
        public static final int  CAP_IAFIELDDVALUE          = 0x105E;
        public static final int  CAP_IAFIELDEVALUE          = 0x105F;
        public static final int  CAP_IAFIELDALASTPAGE       = 0x1060;
        public static final int  CAP_IAFIELDBLASTPAGE       = 0x1061;
        public static final int  CAP_IAFIELDCLASTPAGE       = 0x1062;
        public static final int  CAP_IAFIELDDLASTPAGE       = 0x1063;
        public static final int  CAP_IAFIELDELASTPAGE       = 0x1064;

        /* image data sources MAY support these caps */
        public static final int  ICAP_AUTOBRIGHT                   = 0x1100;
        public static final int  ICAP_BRIGHTNESS                   = 0x1101;
        public static final int  ICAP_CONTRAST                     = 0x1103;
        public static final int  ICAP_CUSTHALFTONE                 = 0x1104;
        public static final int  ICAP_EXPOSURETIME                 = 0x1105;
        public static final int  ICAP_FILTER                       = 0x1106;
        public static final int  ICAP_FLASHUSED                    = 0x1107;
        public static final int  ICAP_GAMMA                        = 0x1108;
        public static final int  ICAP_HALFTONES                    = 0x1109;
        public static final int  ICAP_HIGHLIGHT                    = 0x110a;
        public static final int  ICAP_IMAGEFILEFORMAT              = 0x110c;
        public static final int  ICAP_LAMPSTATE                    = 0x110d;
        public static final int  ICAP_LIGHTSOURCE                  = 0x110e;
        public static final int  ICAP_ORIENTATION                  = 0x1110;
        public static final int  ICAP_PHYSICALWIDTH                = 0x1111;
        public static final int  ICAP_PHYSICALHEIGHT               = 0x1112;
        public static final int  ICAP_SHADOW                       = 0x1113;
        public static final int  ICAP_FRAMES                       = 0x1114;
        public static final int  ICAP_XNATIVERESOLUTION            = 0x1116;
        public static final int  ICAP_YNATIVERESOLUTION            = 0x1117;
        public static final int  ICAP_XRESOLUTION                  = 0x1118;
        public static final int  ICAP_YRESOLUTION                  = 0x1119;
        public static final int  ICAP_MAXFRAMES                    = 0x111a;
        public static final int  ICAP_TILES                        = 0x111b;
        public static final int  ICAP_BITORDER                     = 0x111c;
        public static final int  ICAP_CCITTKFACTOR                 = 0x111d;
        public static final int  ICAP_LIGHTPATH                    = 0x111e;
        public static final int  ICAP_PIXELFLAVOR                  = 0x111f;
        public static final int  ICAP_PLANARCHUNKY                 = 0x1120;
        public static final int  ICAP_ROTATION                     = 0x1121;
        public static final int  ICAP_SUPPORTEDSIZES               = 0x1122;
        public static final int  ICAP_THRESHOLD                    = 0x1123;
        public static final int  ICAP_XSCALING                     = 0x1124;
        public static final int  ICAP_YSCALING                     = 0x1125;
        public static final int  ICAP_BITORDERCODES                = 0x1126;
        public static final int  ICAP_PIXELFLAVORCODES             = 0x1127;
        public static final int  ICAP_JPEGPIXELTYPE                = 0x1128;
        public static final int  ICAP_TIMEFILL                     = 0x112a;
        public static final int  ICAP_BITDEPTH                     = 0x112b;
        public static final int  ICAP_BITDEPTHREDUCTION            = 0x112c;
        public static final int  ICAP_UNDEFINEDIMAGESIZE           = 0x112d;
        public static final int  ICAP_IMAGEDATASET                 = 0x112e;
        public static final int  ICAP_EXTIMAGEINFO                 = 0x112f;
        public static final int  ICAP_MINIMUMHEIGHT                = 0x1130;
        public static final int  ICAP_MINIMUMWIDTH                 = 0x1131;
        public static final int  ICAP_AUTODISCARDBLANKPAGES        = 0x1134;
        public static final int  ICAP_FLIPROTATION                 = 0x1136;
        public static final int  ICAP_BARCODEDETECTIONENABLED      = 0x1137;
        public static final int  ICAP_SUPPORTEDBARCODETYPES        = 0x1138;
        public static final int  ICAP_BARCODEMAXSEARCHPRIORITIES   = 0x1139;
        public static final int  ICAP_BARCODESEARCHPRIORITIES      = 0x113a;
        public static final int  ICAP_BARCODESEARCHMODE            = 0x113b;
        public static final int  ICAP_BARCODEMAXRETRIES            = 0x113c;
        public static final int  ICAP_BARCODETIMEOUT               = 0x113d;
        public static final int  ICAP_ZOOMFACTOR                   = 0x113e;
        public static final int  ICAP_PATCHCODEDETECTIONENABLED    = 0x113f;
        public static final int  ICAP_SUPPORTEDPATCHCODETYPES      = 0x1140;
        public static final int  ICAP_PATCHCODEMAXSEARCHPRIORITIES = 0x1141;
        public static final int  ICAP_PATCHCODESEARCHPRIORITIES    = 0x1142;
        public static final int  ICAP_PATCHCODESEARCHMODE          = 0x1143;
        public static final int  ICAP_PATCHCODEMAXRETRIES          = 0x1144;
        public static final int  ICAP_PATCHCODETIMEOUT             = 0x1145;
        public static final int  ICAP_FLASHUSED2                   = 0x1146;
        public static final int  ICAP_IMAGEFILTER                  = 0x1147;
        public static final int  ICAP_NOISEFILTER                  = 0x1148;
        public static final int  ICAP_OVERSCAN                     = 0x1149;
        public static final int  ICAP_AUTOMATICBORDERDETECTION     = 0x1150;
        public static final int  ICAP_AUTOMATICDESKEW              = 0x1151;
        public static final int  ICAP_AUTOMATICROTATE              = 0x1152;
        public static final int  ICAP_JPEGQUALITY                  = 0x1153;
        public static final int  ICAP_FEEDERTYPE                   = 0x1154;
        public static final int  ICAP_ICCPROFILE                   = 0x1155;
        public static final int  ICAP_AUTOSIZE                     = 0x1156;
        public static final int  ICAP_AUTOMATICCROPUSESFRAME       = 0x1157;
        public static final int  ICAP_AUTOMATICLENGTHDETECTION     = 0x1158;
        public static final int  ICAP_AUTOMATICCOLORENABLED        = 0x1159;
        public static final int  ICAP_AUTOMATICCOLORNONCOLORPIXELTYPE = 0x115a;
        public static final int  ICAP_COLORMANAGEMENTENABLED       = 0x115b;
        public static final int  ICAP_IMAGEMERGE                   = 0x115c;
        public static final int  ICAP_IMAGEMERGEHEIGHTTHRESHOLD    = 0x115d;
        public static final int  ICAP_SUPPORTEDEXTIMAGEINFO        = 0x115e;
        public static final int  ICAP_FILMTYPE                     = 0x115f;
        public static final int  ICAP_MIRROR                       = 0x1160;
        public static final int  ICAP_JPEGSUBSAMPLING              = 0x1161;

        /* image data sources MAY support these audio caps */
        public static final int  ACAP_XFERMECH                     = 0x1202;

        public static final int  CAP_CLEARBUFFERS          = 0x101d;
        public static final int  CAP_SUPPORTEDCAPSEXT      = 0x100c;
        public static final int  CAP_PAGEMULTIPLEACQUIRE   = 0x1023;
        public static final int  CAP_PAPERBINDING          = 0x102f;
        public static final int  CAP_PASSTHRU              = 0x1031;
        public static final int  CAP_POWERDOWNTIME         = 0x1034;
        public static final int  ACAP_AUDIOFILEFORMAT      = 0x1201;

        private CAPS() {}
    }

    public static class TWON
    {
        public static final int TWON_ARRAY          = 3;
        public static final int TWON_ENUMERATION    = 4;
        public static final int TWON_ONEVALUE       = 5;
        public static final int TWON_RANGE          = 6;
        public static final int TWON_ICONID         = 962;
        public static final int TWON_DSMID          = 461;
        public static final int TWON_DSMCODEID      = 63;
        public static final int TWON_DONTCARE8      = 0xff;
        public static final int TWON_DONTCARE16     = 0xffff;
        public static final int TWON_DONTCARE32     = 0xffffffff;
        public static final int TWON_DEFAULT = 0;
        private TWON() {}
    }

    public static class TWMF
    {
        public static final int TWMF_APPOWNS     = 0x0001;
        public static final int TWMF_DSMOWNS     = 0x0002;
        public static final int TWMF_DSOWNS      = 0x0004;
        public static final int TWMF_POINTER     = 0x0008;
        public static final int TWMF_HANDLE      = 0x0010;
        private TWMF() {}
    }

    public static class TWTY
    {
        public static final int TWTY_INT8       = 0x0000;
        public static final int TWTY_INT16      = 0x0001;
        public static final int TWTY_INT32      = 0x0002;
        public static final int TWTY_UINT8       = 0x0003;
        public static final int TWTY_UINT16      = 0x0004;
        public static final int TWTY_UINT32      = 0x0005;
        public static final int TWTY_BOOL        = 0x0006;
        public static final int TWTY_FIX32       = 0x0007;
        public static final int TWTY_FRAME       = 0x0008;
        public static final int TWTY_STR32       = 0x0009;
        public static final int TWTY_STR64       = 0x000a;
        public static final int TWTY_STR128      = 0x000b;
        public static final int TWTY_STR255      = 0x000c;
        public static final int TWTY_HANDLE      = 0x000f;
        public static final int TWTY_STR1024     = 0x000d;
        public static final int TWTY_UNI512      = 0x000e;
        public static final int TWTY_DEFAULT     = -1;
        private TWTY() {}
    }

    public enum CAP_ALARMS
    {
        // Capability constants
        // CAP_ALARMS values
        TWAL_ALARM,
        TWAL_FEEDERERROR,
        TWAL_FEEDERWARNING,
        TWAL_BARCODE,
        TWAL_DOUBLEFEED,
        TWAL_JAM,
        TWAL_PATCHCODE,
        TWAL_POWER,
        TWAL_SKEW,
        TWAL_DEFAULT
    }

    public enum ICAP_AUTOSIZE
    {
        // ICAP_AUTOSIZE values
        TWAS_NONE,
        TWAS_AUTO,
        TWAS_CURRENT,
        TWAS_DEFAULT
    }

    public enum TWEI_BARCODEROTATION
    {
        // TWEI_BARCODEROTATION values
        TWBCOR_ROT0   ,
        TWBCOR_ROT90  ,
        TWBCOR_ROT180 ,
        TWBCOR_ROT270 ,
        TWBCOR_ROTX   ,
        TWBCOR_DEFAULT
    }

    public enum ICAP_BARCODESEARCHMODE
    {
        // ICAP_BARCODESEARCHMODE values
        TWBD_HORZ,
        TWBD_VERT,
        TWBD_HORZVERT,
        TWBD_VERTHORZ,
        TWBD_DEFAULT
    }

    public enum ICAP_PATCHCODESEARCHMODE
    {
        // ICAP_PATCHCODESEARCHMODE values
        TWBD_HORZ,
        TWBD_VERT,
        TWBD_HORZVERT,
        TWBD_VERTHORZ,
        TWBD_DEFAULT
    }

    public enum ICAP_BITORDER
    {
        TWBO_LSBFIRST,
        TWBO_MSBFIRST,
        TWBO_DEFAULT
    }

    public static class ICAP_AUTODISCARDBLANKPAGES
    {
        /* ICAP_AUTODISCARDBLANKPAGES values */
        public static final int  TWBP_DISABLE            = -2;
        public static final int  TWBP_AUTO               = -1;
        private ICAP_AUTODISCARDBLANKPAGES() {}
    }

    public enum ICAP_BITDEPTHREDUCTION
    {
        /* ICAP_BITDEPTHREDUCTION values */
        TWBR_THRESHOLD        ,
        TWBR_HALFTONE         ,
        TWBR_CUSTHALFTONE     ,
        TWBR_DIFFUSION        ,
        TWBR_DYNAMICTHRESHOLD ,
        TWBR_DEFAULT
    }

    public enum ICAP_SUPPORTEDBARCODETYPES
    {
        // ICAP_SUPPORTEDBARCODETYPES and TWEI_BARCODETYPE values
        TWBT_3OF9                 ,
        TWBT_2OF5INTERLEAVED      ,
        TWBT_2OF5NONINTERLEAVED   ,
        TWBT_CODE93               ,
        TWBT_CODE128              ,
        TWBT_UCC128               ,
        TWBT_CODABAR              ,
        TWBT_UPCA                 ,
        TWBT_UPCE                 ,
        TWBT_EAN8                 ,
        TWBT_EAN13                ,
        TWBT_POSTNET              ,
        TWBT_PDF417               ,
        TWBT_2OF5INDUSTRIAL       ,
        TWBT_2OF5MATRIX           ,
        TWBT_2OF5DATALOGIC        ,
        TWBT_2OF5IATA             ,
        TWBT_3OF9FULLASCII        ,
        TWBT_CODABARWITHSTARTSTOP ,
        TWBT_MAXICODE             ,
        TWBT_QRCODE,
        TWBT_DEFAULT
    }

    public enum ICAP_COMPRESSION
    {
        /* ICAP_COMPRESSION values*/
        TWCP_NONE              ,
        TWCP_PACKBITS          ,
        TWCP_GROUP31D          ,
        TWCP_GROUP31DEOL       ,
        TWCP_GROUP32D          ,
        TWCP_GROUP4            ,
        TWCP_JPEG              ,
        TWCP_LZW               ,
        TWCP_JBIG              ,
        TWCP_PNG               ,
        TWCP_RLE4              ,
        TWCP_RLE8              ,
        TWCP_BITFIELDS         ,
        TWCP_ZIP               ,
        TWCP_JPEG2000          ,
        TWCP_DEFAULT
    }

    public enum CAP_CAMERASIDE
    {
        TWCS_BOTH,
        TWCS_TOP,
        TWCS_BOTTOM,
    }

    public enum CAP_DEVICEEVENT
    {
        /* CAP_DEVICEEVENT values */
        TWDE_CHECKAUTOMATICCAPTURE ,
        TWDE_CHECKBATTERY          ,
        TWDE_CHECKDEVICEONLINE     ,
        TWDE_CHECKFLASH            ,
        TWDE_CHECKPOWERSUPPLY      ,
        TWDE_CHECKRESOLUTION       ,
        TWDE_DEVICEADDED           ,
        TWDE_DEVICEOFFLINE         ,
        TWDE_DEVICEREADY           ,
        TWDE_DEVICEREMOVED         ,
        TWDE_IMAGECAPTURED         ,
        TWDE_IMAGEDELETED          ,
        TWDE_PAPERDOUBLEFEED       ,
        TWDE_PAPERJAM              ,
        TWDE_LAMPFAILURE           ,
        TWDE_POWERSAVE             ,
        TWDE_POWERSAVENOTIFY       ,
        TWDE_DEFAULT
    }

    public static class CAP_DEVICEEVENT_CUSTOM
    {
        private CAP_DEVICEEVENT_CUSTOM() {}
        public static final int  TWDE_CUSTOMEVENTS  = 0x8000 ;
    }

    public enum TW_PASSTHRU
    {
        /* TW_PASSTHRU.Direction values. */
        TWDR_DEFAULT,
        TWDR_GET    ,
        TWDR_SET
    }

    public enum TWEI_DESKEWSTATUS
    {
        // TWEI_DESKEWSTATUS values.
        TWDSK_SUCCESS   ,
        TWDSK_REPORTONLY,
        TWDSK_FAIL      ,
        TWDSK_DISABLED  ,
        TWDSK_DEFAULT
    }

    public enum CAP_DUPLEX
    {
        /* CAP_DUPLEX values */
        TWDX_NONE       ,
        TWDX_1PASSDUPLEX,
        TWDX_2PASSDUPLEX,
        TWDX_DEFAULT
    }

    public enum CAP_FEEDERALIGNMENT
    {
        /* CAP_FEEDERALIGNMENT values */
        TWFA_NONE,
        TWFA_LEFT,
        TWFA_CENTER,
        TWFA_RIGHT,
        TWFA_DEFAULT
    }

    public enum ICAP_FEEDERTYPE
    {
        /* ICAP_FEEDERTYPE values*/
        TWFE_GENERAL,
        TWFE_PHOTO,
        TWFE_DEFAULT
    }

    public enum ICAP_IMAGEFILEFORMAT
    {
        /* ICAP_IMAGEFILEFORMAT values */
        TWFF_TIFF             ,
        TWFF_PICT             ,
        TWFF_BMP              ,
        TWFF_XBM              ,
        TWFF_JFIF             ,
        TWFF_FPX              ,
        TWFF_TIFFMULTI        ,
        TWFF_PNG              ,
        TWFF_SPIFF            ,
        TWFF_EXIF             ,
        TWFF_PDF              ,
        TWFF_JP2              ,
        TWFF_JPN              ,
        TWFF_JPX              ,
        TWFF_DEJAVU           ,
        TWFF_PDFA             ,
        TWFF_PDFA2            ,
        TWFF_PDFRASTER        ,
        TWFF_DEFAULT
    }

    public enum ICAP_FLASHUSED2
    {
        // ICAP_FLASHUSED2 values
        TWFL_NONE,
        TWFL_OFF,
        TWFL_ON,
        TWFL_AUTO,
        TWFL_REDEYE,
        TWFL_DEFAULT
    }

    public enum CAP_FEEDERORDER
    {
        /* CAP_FEEDERORDER values */
        TWFO_FIRSTPAGEFIRST,
        TWFO_LASTPAGEFIRST,
        TWFO_DEFAULT
    }

    public enum CAP_FEEDERPOCKET
    {
        TWFP_POCKETERROR       ,
        TWFP_POCKET1           ,
        TWFP_POCKET2           ,
        TWFP_POCKET3           ,
        TWFP_POCKET4           ,
        TWFP_POCKET5           ,
        TWFP_POCKET6           ,
        TWFP_POCKET7           ,
        TWFP_POCKET8           ,
        TWFP_POCKET9           ,
        TWFP_POCKET10          ,
        TWFP_POCKET11          ,
        TWFP_POCKET12          ,
        TWFP_POCKET13          ,
        TWFP_POCKET14          ,
        TWFP_POCKET15          ,
        TWFP_POCKET16          ,
        TWFP_DEFAULT
    }

    public enum ICAP_FLIPROTATION
    {
        // ICAP_FLIPROTATION values
        TWFR_BOOK,
        TWFR_FANFOLD
    }

    public enum ICAP_FILTER
    {
        // ICAP_FILTER values
        TWFT_RED               ,
        TWFT_GREEN             ,
        TWFT_BLUE              ,
        TWFT_NONE              ,
        TWFT_WHITE             ,
        TWFT_CYAN              ,
        TWFT_MAGENTA           ,
        TWFT_YELLOW            ,
        TWFT_BLACK             ,
        TWFT_DEFAULT
    }

    public static class TW_FILESYSTEM
    {
        // TW_FILESYSTEM.FileType values 
        public static final int TWFY_CAMERA            = 0;
        public static final int TWFY_CAMERATOP         = 1;
        public static final int TWFY_CAMERABOTTOM      = 2;
        public static final int TWFY_CAMERAPREVIEW     = 3;
        public static final int TWFY_DOMAIN            = 4;
        public static final int TWFY_HOST              = 5;
        public static final int TWFY_DIRECTORY         = 6;
        public static final int TWFY_IMAGE             = 7;
        public static final int TWFY_UNKNOWN           = 8;
        public static final int TWFY_DEFAULT           = 9999; 
    }

    public enum CAP_IAFIELD_LEVEL
    {
        // CAP_IAFIELD*_LEVEL values  
        TWIA_UNUSED,
        TWIA_FIXED ,
        TWIA_LEVEL1,
        TWIA_LEVEL2,
        TWIA_LEVEL3,
        TWIA_LEVEL4,
        TWIA_DEFAULT
    }

    public enum ICAP_ICCPROFILE
    {
        /* ICAP_ICCPROFILE values */
        TWIC_NONE,
        TWIC_LINK,
        TWIC_EMBED,
        TWIC_DEFAULT
    }

    public enum ICAP_IMAGEFILTER
    {
        // ICAP_IMAGEFILTER values
        TWIF_NONE,
        TWIF_AUTO,
        TWIF_LOWPASS,
        TWIF_BANDPASS,
        TWIF_HIGHPASS;
        public static final ICAP_IMAGEFILTER TWIF_TEXT = TWIF_BANDPASS;
        public static final ICAP_IMAGEFILTER TWIF_FINELINE = TWIF_HIGHPASS;
    }

    public enum ICAP_IMAGEMERGE
    {
        TWIM_NONE,
        TWIM_FRONTONTOP,
        TWIM_FRONTONBOTTOM,
        TWIM_FRONTONLEFT,
        TWIM_FRONTONRIGHT
    }

    public enum CAP_JOBCONTROL
    {
        /* CAP_JOBCONTROL values  */
        TWJC_NONE,
        TWJC_JSIC,
        TWJC_JSIS,
        TWJC_JSXC,
        TWJC_JSXS,
        TWJC_DEFAULT
    }

    public static class ICAP_JPEGQUALITY
    {
        /* ICAP_JPEGQUALITY values */
        public static final int  TWJQ_UNKNOWN           =-4;
        public static final int  TWJQ_LOW               =-3;
        public static final int  TWJQ_MEDIUM            =-2;
        public static final int  TWJQ_HIGH              =-1;
        private ICAP_JPEGQUALITY() {}
    }

    public enum ICAP_LIGHTPATH
    {
        // ICAP_LIGHTPATH values
        TWLP_REFLECTIVE,
        TWLP_TRANSMISSIVE
    }

    public enum ICAP_LIGHTSOURCE
    {
        /* ICAP_LIGHTSOURCE values */
        TWLS_RED,
        TWLS_GREEN,
        TWLS_BLUE,
        TWLS_NONE,
        TWLS_WHITE,
        TWLS_UV,
        TWLS_IR,
        TWLS_DEFAULT
    }

    public enum TWEI_MAGTYPE
    {
        /* TWEI_MAGTYPE values */
        TWMD_MICR              ,
        TWMD_RAW               ,
        TWMD_INVALID           ,
        TWMD_DEFAULT
    }

    public enum ICAP_NOISEFILTER
    {
        /* ICAP_NOISEFILTER values */
        TWNF_NONE,
        TWNF_AUTO,
        TWNF_LONEPIXEL,
        TWNF_MAJORITYRULE,
        TWNF_DEFAULT
    }

    public enum ICAP_ORIENTATION
    {
        /* ICAP_ORIENTATION values */
        TWOR_ROT0,
        TWOR_ROT90,
        TWOR_ROT180,
        TWOR_ROT270,
        TWOR_AUTO,
        TWOR_AUTOTEXT,
        TWOR_AUTOPICTURE,
        TWOR_DEFAULT;
        public static final ICAP_ORIENTATION TWOR_PORTRAIT  = TWOR_ROT0;
        public static final ICAP_ORIENTATION TWOR_LANDSCAPE = TWOR_ROT270;
    }

    public enum ICAP_OVERSCAN
    {
        /* ICAP_OVERSCAN values */
        TWOV_NONE,
        TWOV_AUTO,
        TWOV_TOPBOTTOM,
        TWOV_LEFTRIGHT,
        TWOV_ALL,
        TWOV_DEFAULT
    }

    public enum TW_PALETTE8
    {
        /* Palette types for TW_PALETTE8 */
        TWPA_RGB ,
        TWPA_GRAY,
        TWPA_CMY ,
        TWPA_DEFAULT
    }

    public enum ICAP_PLANARCHUNKY
    {
        /* ICAP_PLANARCHUNKY values */
        TWPC_CHUNKY,
        TWPC_PLANAR,
        TWPC_DEFAULT
    }

    public enum TWEI_PATCHCODE
    {
        /* TWEI_PATCHCODE values*/
        TWPCH_PATCH1,
        TWPCH_PATCH2,
        TWPCH_PATCH3,
        TWPCH_PATCH4,
        TWPCH_PATCH6,
        TWPCH_PATCHT,
        TWPCH_DEFAULT
    }

    public enum ICAP_SUPPORTEDPATCHCODETYPES
    {
        TWPCH_PATCH1,
        TWPCH_PATCH2,
        TWPCH_PATCH3,
        TWPCH_PATCH4,
        TWPCH_PATCH6,
        TWPCH_PATCHT,
        TWPCH_DEFAULT
    }

    public enum ICAP_PIXELFLAVOR
    {
        /* ICAP_PIXELFLAVOR values */
        TWPF_CHOCOLATE,
        TWPF_VANILLA,
        TWPF_DEFAULT
    }

    public enum CAP_PRINTERMODE
    {
        /* CAP_PRINTERMODE values */
        TWPM_SINGLESTRING       ,
        TWPM_MULTISTRING        ,
        TWPM_COMPOUNDSTRING     ,
        TWPM_DEFAULT
    }

    public enum CAP_PRINTER
    {
        /* CAP_PRINTER values */
        TWPR_IMPRINTERTOPBEFORE    ,
        TWPR_IMPRINTERTOPAFTER     ,
        TWPR_IMPRINTERBOTTOMBEFORE ,
        TWPR_IMPRINTERBOTTOMAFTER  ,
        TWPR_ENDORSERTOPBEFORE     ,
        TWPR_ENDORSERTOPAFTER      ,
        TWPR_ENDORSERBOTTOMBEFORE  ,
        TWPR_ENDORSERBOTTOMAFTER   ,
        TWPR_DEFAULT
    }

    public enum CAP_PRINTERFONTSTYLE
    {
        /* CAP_PRINTERFONTSTYLE Added 2.3 */
        TWPF_NORMAL    ,
        TWPF_BOLD      ,
        TWPF_ITALIC    ,
        TWPF_LARGESIZE ,
        TWPF_SMALLSIZE ,
        TWPF_DEFAULT
    }

    public enum CAP_PRINTERINDEXTRIGGER
    {
        /* CAP_PRINTERINDEXTRIGGER Added 2.3 */
        TWCT_PAGE    ,
        TWCT_PATCH1  ,
        TWCT_PATCH2  ,
        TWCT_PATCH3  ,
        TWCT_PATCH4  ,
        TWCT_PATCHT  ,
        TWCT_PATCH6  ,
        TWCT_DEFAULT
    }

    public enum CAP_POWERSUPPLY
    {
        /* CAP_POWERSUPPLY values */
        TWPS_EXTERNAL,
        TWPS_BATTERY ,
        TWPS_DEFAULT
    }

    public enum ICAP_PIXELTYPE
    {
        /* ICAP_PIXELTYPE values (PT_ means Pixel Type) */
        TWPT_BW,
        TWPT_GRAY,
        TWPT_RGB,
        TWPT_PALETTE,
        TWPT_CMY,
        TWPT_CMYK,
        TWPT_YUV,
        TWPT_YUVK,
        TWPT_CIEXYZ,
        TWPT_LAB,
        TWPT_SRGB,
        TWPT_SCRGB,
        TWPT_BGR,
        TWPT_CIELAB,
        TWPT_CIELUV,
        TWPT_YCBCR,
        TWPT_INFRARED,
        TWPT_DEFAULT
    }

    public enum ICAP_JPEGPIXELTYPE
    {
        TWPT_BW,
        TWPT_GRAY,
        TWPT_RGB,
        TWPT_PALETTE,
        TWPT_CMY,
        TWPT_CMYK,
        TWPT_YUV,
        TWPT_YUVK,
        TWPT_CIEXYZ,
        TWPT_DEFAULT
    }

    public enum CAP_SEGMENTED
    {
        /* CAP_SEGMENTED values */
        TWSG_NONE  ,
        TWSG_AUTO  ,
        TWSG_MANUAL,
        TWSG_DEFAULT
    }

    public enum ICAP_FILMTYPE
    {
        /* ICAP_FILMTYPE values */
        TWFM_POSITIVE,
        TWFM_NEGATIVE
    }

    public enum CAP_DOUBLEFEEDDETECTION
    {
        /* CAP_DOUBLEFEEDDETECTION */
        TWDF_ULTRASONIC,
        TWDF_BYLENGTH,
        TWDF_INFRARED,
        TWDF_DEFAULT
    }

    public enum CAP_DOUBLEFEEDDETECTIONSENSITIVITY
    {
        /* CAP_DOUBLEFEEDDETECTIONSENSITIVITY */
        TWUS_LOW   ,
        TWUS_MEDIUM,
        TWUS_HIGH  ,
        TWUS_DEFAULT
    }

    public enum CAP_DOUBLEFEEDDETECTIONRESPONSE
    {
        /* CAP_DOUBLEFEEDDETECTIONRESPONSE */
        TWDP_STOP        ,
        TWDP_STOPANDWAIT ,
        TWDP_SOUND       ,
        TWDP_DONOTIMPRINT,
        TWDP_DEFAULT
    }

    public enum ICAP_MIRROR
    {
        /* ICAP_MIRROR values */
        TWMR_NONE       ,
        TWMR_VERTICAL   ,
        TWMR_HORIZONTAL ,
        TWMR_DEFAULT
    }

    public enum ICAP_JPEGSUBSAMPLING
    {
        /* ICAP_JPEGSUBSAMPLING values */
        TWJS_444YCBCR            ,
        TWJS_444RGB              ,
        TWJS_422                 ,
        TWJS_421                 ,
        TWJS_411                 ,
        TWJS_420                 ,
        TWJS_410                 ,
        TWJS_311                 ,
        TWJS_DEFAULT
    }

    public enum CAP_PAPERHANDLING
    {
        /* CAP_PAPERHANDLING values */
        TWPH_NORMAL ,
        TWPH_FRAGILE,
        TWPH_THICK  ,
        TWPH_TRIFOLD ,
        TWPH_PHOTOGRAPH,
        TWPH_DEFAULT
    }

    public enum CAP_INDICATORSMODE
    {
        /* CAP_INDICATORSMODE values */
        TWCI_INFO     ,
        TWCI_WARNING  ,
        TWCI_ERROR    ,
        TWCI_WARMUP   ,
        TWCI_DEFAULT
    }

    public enum ICAP_SUPPORTEDSIZES
    {
        /* ICAP_SUPPORTEDSIZES values (SS_ means Supported Sizes) */
        TWSS_NONE              ,
        TWSS_A4                ,
        TWSS_JISB5             ,
        TWSS_USLETTER          ,
        TWSS_USLEGAL           ,
        TWSS_A5                ,
        TWSS_ISOB4             ,
        TWSS_ISOB6             ,
        REMOVED                ,
        TWSS_USLEDGER          ,
        TWSS_USEXECUTIVE       ,
        TWSS_A3                ,
        TWSS_ISOB3             ,
        TWSS_A6                ,
        TWSS_C4                ,
        TWSS_C5                ,
        TWSS_C6                ,
        TWSS_4A0               ,
        TWSS_2A0               ,
        TWSS_A0                ,
        TWSS_A1                ,
        TWSS_A2                ,
        TWSS_A7                ,
        TWSS_A8                ,
        TWSS_A9                ,
        TWSS_A10               ,
        TWSS_ISOB0             ,
        TWSS_ISOB1             ,
        TWSS_ISOB2             ,
        TWSS_ISOB5             ,
        TWSS_ISOB7             ,
        TWSS_ISOB8             ,
        TWSS_ISOB9             ,
        TWSS_ISOB10            ,
        TWSS_JISB0             ,
        TWSS_JISB1             ,
        TWSS_JISB2             ,
        TWSS_JISB3             ,
        TWSS_JISB4             ,
        TWSS_JISB6             ,
        TWSS_JISB7             ,
        TWSS_JISB8             ,
        TWSS_JISB9             ,
        TWSS_JISB10            ,
        TWSS_C0                ,
        TWSS_C1                ,
        TWSS_C2                ,
        TWSS_C3                ,
        TWSS_C7                ,
        TWSS_C8                ,
        TWSS_C9                ,
        TWSS_C10               ,
        TWSS_USSTATEMENT       ,
        TWSS_BUSINESSCARD      ,
        TWSS_MAXSIZE           ,
        TWSS_DEFAULT;

        public static final ICAP_SUPPORTEDSIZES TWSS_A4LETTER    = TWSS_A4;
        public static final ICAP_SUPPORTEDSIZES TWSS_B3          = TWSS_ISOB3;
        public static final ICAP_SUPPORTEDSIZES TWSS_B4          = TWSS_ISOB4;
        public static final ICAP_SUPPORTEDSIZES TWSS_B6          = TWSS_ISOB6;
        public static final ICAP_SUPPORTEDSIZES TWSS_B5LETTER    = TWSS_JISB5;
    }

    public enum ICAP_XFERMECH
    {
        /* ICAP_XFERMECH values (SX_ means Setup XFer) */
        TWSX_NATIVE,
        TWSX_FILE  ,
        TWSX_MEMORY,
        TWSX_UNKNOWN,
        TWSX_MEMFILE,
        TWSX_DEFAULT
    }

    public enum ACAP_XFERMECH
    {
        /* ACAP_XFERMECH values (SX_ means Setup XFer) */
        TWSX_NATIVE,
        TWSX_FILE  ,
        TWSX_DEFAULT
    }

    public enum ICAP_UNITS
    {
        /* ICAP_UNITS values (UN_ means UNits) */
        TWUN_INCHES,
        TWUN_CENTIMETERS,
        TWUN_PICAS      ,
        TWUN_POINTS     ,
        TWUN_TWIPS      ,
        TWUN_PIXELS     ,
        TWUN_MILLIMETERS,
        TWUN_DEFAULT
    }

    /****************************************************************************
     * Country Constants                                                        *
     ****************************************************************************/
    public static class TWCY
    {
        public static final int  TWCY_AFGHANISTAN   =1001;
        public static final int  TWCY_ALGERIA       = 213;
        public static final int  TWCY_AMERICANSAMOA = 684;
        public static final int  TWCY_ANDORRA       =  33;
        public static final int  TWCY_ANGOLA        =1002;
        public static final int  TWCY_ANGUILLA      =8090;
        public static final int  TWCY_ANTIGUA       =8091;
        public static final int  TWCY_ARGENTINA     =  54;
        public static final int  TWCY_ARUBA         = 297;
        public static final int  TWCY_ASCENSIONI    = 247;
        public static final int  TWCY_AUSTRALIA     =  61;
        public static final int  TWCY_AUSTRIA       =  43;
        public static final int  TWCY_BAHAMAS       =8092;
        public static final int  TWCY_BAHRAIN       = 973;
        public static final int  TWCY_BANGLADESH    = 880;
        public static final int  TWCY_BARBADOS      =8093;
        public static final int  TWCY_BELGIUM       =  32;
        public static final int  TWCY_BELIZE        = 501;
        public static final int  TWCY_BENIN         = 229;
        public static final int  TWCY_BERMUDA       =8094;
        public static final int  TWCY_BHUTAN        =1003;
        public static final int  TWCY_BOLIVIA       = 591;
        public static final int  TWCY_BOTSWANA      = 267;
        public static final int  TWCY_BRITAIN       =   6;
        public static final int  TWCY_BRITVIRGINIS  =8095;
        public static final int  TWCY_BRAZIL        =  55;
        public static final int  TWCY_BRUNEI        = 673;
        public static final int  TWCY_BULGARIA      = 359;
        public static final int  TWCY_BURKINAFASO   =1004;
        public static final int  TWCY_BURMA         =1005;
        public static final int  TWCY_BURUNDI       =1006;
        public static final int  TWCY_CAMAROON      = 237;
        public static final int  TWCY_CANADA        =   2;
        public static final int  TWCY_CAPEVERDEIS   = 238;
        public static final int  TWCY_CAYMANIS      =8096;
        public static final int  TWCY_CENTRALAFREP  =1007;
        public static final int  TWCY_CHAD          =1008;
        public static final int  TWCY_CHILE         =  56;
        public static final int  TWCY_CHINA         =  86;
        public static final int  TWCY_CHRISTMASIS   =1009;
        public static final int  TWCY_COCOSIS       =1009;
        public static final int  TWCY_COLOMBIA      =  57;
        public static final int  TWCY_COMOROS       =1010;
        public static final int  TWCY_CONGO         =1011;
        public static final int  TWCY_COOKIS        =1012;
        public static final int  TWCY_COSTARICA     = 506;
        public static final int  TWCY_CUBA          =   5;
        public static final int  TWCY_CYPRUS        = 357;
        public static final int  TWCY_CZECHOSLOVAKIA=  42;
        public static final int  TWCY_DENMARK       =  45;
        public static final int  TWCY_DJIBOUTI      =1013;
        public static final int  TWCY_DOMINICA      =8097;
        public static final int  TWCY_DOMINCANREP   =8098;
        public static final int  TWCY_EASTERIS      =1014;
        public static final int  TWCY_ECUADOR       = 593;
        public static final int  TWCY_EGYPT         =  20;
        public static final int  TWCY_ELSALVADOR    = 503;
        public static final int  TWCY_EQGUINEA      =1015;
        public static final int  TWCY_ETHIOPIA      = 251;
        public static final int  TWCY_FALKLANDIS    =1016;
        public static final int  TWCY_FAEROEIS      = 298;
        public static final int  TWCY_FIJIISLANDS   = 679;
        public static final int  TWCY_FINLAND       = 358;
        public static final int  TWCY_FRANCE        =  33;
        public static final int  TWCY_FRANTILLES    = 596;
        public static final int  TWCY_FRGUIANA      = 594;
        public static final int  TWCY_FRPOLYNEISA   = 689;
        public static final int  TWCY_FUTANAIS      =1043;
        public static final int  TWCY_GABON         = 241;
        public static final int  TWCY_GAMBIA        = 220;
        public static final int  TWCY_GERMANY       =  49;
        public static final int  TWCY_GHANA         = 233;
        public static final int  TWCY_GIBRALTER     = 350;
        public static final int  TWCY_GREECE        =  30;
        public static final int  TWCY_GREENLAND     = 299;
        public static final int  TWCY_GRENADA       =8099;
        public static final int  TWCY_GRENEDINES    =8015;
        public static final int  TWCY_GUADELOUPE    = 590;
        public static final int  TWCY_GUAM          = 671;
        public static final int  TWCY_GUANTANAMOBAY =5399;
        public static final int  TWCY_GUATEMALA     = 502;
        public static final int  TWCY_GUINEA        = 224;
        public static final int  TWCY_GUINEABISSAU  =1017;
        public static final int  TWCY_GUYANA        = 592;
        public static final int  TWCY_HAITI         = 509;
        public static final int  TWCY_HONDURAS      = 504;
        public static final int  TWCY_HONGKONG      = 852;
        public static final int  TWCY_HUNGARY       =  36;
        public static final int  TWCY_ICELAND       = 354;
        public static final int  TWCY_INDIA         =  91;
        public static final int  TWCY_INDONESIA     =  62;
        public static final int  TWCY_IRAN          =  98;
        public static final int  TWCY_IRAQ          = 964;
        public static final int  TWCY_IRELAND       = 353;
        public static final int  TWCY_ISRAEL        = 972;
        public static final int  TWCY_ITALY         =  39;
        public static final int  TWCY_IVORYCOAST    = 225;
        public static final int  TWCY_JAMAICA       =8010;
        public static final int  TWCY_JAPAN         =  81;
        public static final int  TWCY_JORDAN        = 962;
        public static final int  TWCY_KENYA         = 254;
        public static final int  TWCY_KIRIBATI      =1018;
        public static final int  TWCY_KOREA         =  82;
        public static final int  TWCY_KUWAIT        = 965;
        public static final int  TWCY_LAOS          =1019;
        public static final int  TWCY_LEBANON       =1020;
        public static final int  TWCY_LIBERIA       = 231;
        public static final int  TWCY_LIBYA         = 218;
        public static final int  TWCY_LIECHTENSTEIN =  41;
        public static final int  TWCY_LUXENBOURG    = 352;
        public static final int  TWCY_MACAO         = 853;
        public static final int  TWCY_MADAGASCAR    =1021;
        public static final int  TWCY_MALAWI        = 265;
        public static final int  TWCY_MALAYSIA      =  60;
        public static final int  TWCY_MALDIVES      = 960;
        public static final int  TWCY_MALI          =1022;
        public static final int  TWCY_MALTA         = 356;
        public static final int  TWCY_MARSHALLIS    = 692;
        public static final int  TWCY_MAURITANIA    =1023;
        public static final int  TWCY_MAURITIUS     = 230;
        public static final int  TWCY_MEXICO        =   3;
        public static final int  TWCY_MICRONESIA    = 691;
        public static final int  TWCY_MIQUELON      = 508;
        public static final int  TWCY_MONACO        =  33;
        public static final int  TWCY_MONGOLIA      =1024;
        public static final int  TWCY_MONTSERRAT    =8011;
        public static final int  TWCY_MOROCCO       = 212;
        public static final int  TWCY_MOZAMBIQUE    =1025;
        public static final int  TWCY_NAMIBIA       = 264;
        public static final int  TWCY_NAURU         =1026;
        public static final int  TWCY_NEPAL         = 977;
        public static final int  TWCY_NETHERLANDS   =  31;
        public static final int  TWCY_NETHANTILLES  = 599;
        public static final int  TWCY_NEVIS         =8012;
        public static final int  TWCY_NEWCALEDONIA  = 687;
        public static final int  TWCY_NEWZEALAND    =  64;
        public static final int  TWCY_NICARAGUA     = 505;
        public static final int  TWCY_NIGER         = 227;
        public static final int  TWCY_NIGERIA       = 234;
        public static final int  TWCY_NIUE          =1027;
        public static final int  TWCY_NORFOLKI      =1028;
        public static final int  TWCY_NORWAY        =  47;
        public static final int  TWCY_OMAN          = 968;
        public static final int  TWCY_PAKISTAN      =  92;
        public static final int  TWCY_PALAU         =1029;
        public static final int  TWCY_PANAMA        = 507;
        public static final int  TWCY_PARAGUAY      = 595;
        public static final int  TWCY_PERU          =  51;
        public static final int  TWCY_PHILLIPPINES  =  63;
        public static final int  TWCY_PITCAIRNIS    =1030;
        public static final int  TWCY_PNEWGUINEA    = 675;
        public static final int  TWCY_POLAND        =  48;
        public static final int  TWCY_PORTUGAL      = 351;
        public static final int  TWCY_QATAR         = 974;
        public static final int  TWCY_REUNIONI      =1031;
        public static final int  TWCY_ROMANIA       =  40;
        public static final int  TWCY_RWANDA        = 250;
        public static final int  TWCY_SAIPAN        = 670;
        public static final int  TWCY_SANMARINO     =  39;
        public static final int  TWCY_SAOTOME       =1033;
        public static final int  TWCY_SAUDIARABIA   = 966;
        public static final int  TWCY_SENEGAL       = 221;
        public static final int  TWCY_SEYCHELLESIS  =1034;
        public static final int  TWCY_SIERRALEONE   =1035;
        public static final int  TWCY_SINGAPORE     =  65;
        public static final int  TWCY_SOLOMONIS     =1036;
        public static final int  TWCY_SOMALI        =1037;
        public static final int  TWCY_SOUTHAFRICA   =  27;
        public static final int  TWCY_SPAIN         =  34;
        public static final int  TWCY_SRILANKA      =  94;
        public static final int  TWCY_STHELENA      =1032;
        public static final int  TWCY_STKITTS       =8013;
        public static final int  TWCY_STLUCIA       =8014;
        public static final int  TWCY_STPIERRE      = 508;
        public static final int  TWCY_STVINCENT     =8015;
        public static final int  TWCY_SUDAN         =1038;
        public static final int  TWCY_SURINAME      = 597;
        public static final int  TWCY_SWAZILAND     = 268;
        public static final int  TWCY_SWEDEN        =  46;
        public static final int  TWCY_SWITZERLAND   =  41;
        public static final int  TWCY_SYRIA         =1039;
        public static final int  TWCY_TAIWAN        = 886;
        public static final int  TWCY_TANZANIA      = 255;
        public static final int  TWCY_THAILAND      =  66;
        public static final int  TWCY_TOBAGO        =8016;
        public static final int  TWCY_TOGO          = 228;
        public static final int  TWCY_TONGAIS       = 676;
        public static final int  TWCY_TRINIDAD      =8016;
        public static final int  TWCY_TUNISIA       = 216;
        public static final int  TWCY_TURKEY        =  90;
        public static final int  TWCY_TURKSCAICOS   =8017;
        public static final int  TWCY_TUVALU        =1040;
        public static final int  TWCY_UGANDA        = 256;
        public static final int  TWCY_USSR          =   7;
        public static final int  TWCY_UAEMIRATES    = 971;
        public static final int  TWCY_UNITEDKINGDOM =  44;
        public static final int  TWCY_USA           =   1;
        public static final int  TWCY_URUGUAY       = 598;
        public static final int  TWCY_VANUATU       =1041;
        public static final int  TWCY_VATICANCITY   =  39;
        public static final int  TWCY_VENEZUELA     =  58;
        public static final int  TWCY_WAKE          =1042;
        public static final int  TWCY_WALLISIS      =1043;
        public static final int  TWCY_WESTERNSAHARA =1044;
        public static final int  TWCY_WESTERNSAMOA  =1045;
        public static final int  TWCY_YEMEN         =1046;
        public static final int  TWCY_YUGOSLAVIA    =  38;
        public static final int  TWCY_ZAIRE         = 243;
        public static final int  TWCY_ZAMBIA        = 260;
        public static final int  TWCY_ZIMBABWE      = 263;
        public static final int  TWCY_ALBANIA       = 355;
        public static final int  TWCY_ARMENIA       = 374;
        public static final int  TWCY_AZERBAIJAN    = 994;
        public static final int  TWCY_BELARUS       = 375;
        public static final int  TWCY_BOSNIAHERZGO  = 387;
        public static final int  TWCY_CAMBODIA      = 855;
        public static final int  TWCY_CROATIA       = 385;
        public static final int  TWCY_CZECHREPUBLIC = 420;
        public static final int  TWCY_DIEGOGARCIA   = 246;
        public static final int  TWCY_ERITREA       = 291;
        public static final int  TWCY_ESTONIA       = 372;
        public static final int  TWCY_GEORGIA       = 995;
        public static final int  TWCY_LATVIA        = 371;
        public static final int  TWCY_LESOTHO       = 266;
        public static final int  TWCY_LITHUANIA     = 370;
        public static final int  TWCY_MACEDONIA     = 389;
        public static final int  TWCY_MAYOTTEIS     = 269;
        public static final int  TWCY_MOLDOVA       = 373;
        public static final int  TWCY_MYANMAR       =  95;
        public static final int  TWCY_NORTHKOREA    = 850;
        public static final int  TWCY_PUERTORICO    = 787;
        public static final int  TWCY_RUSSIA        =   7;
        public static final int  TWCY_SERBIA        = 381;
        public static final int  TWCY_SLOVAKIA      = 421;
        public static final int  TWCY_SLOVENIA      = 386;
        public static final int  TWCY_SOUTHKOREA    =  82;
        public static final int  TWCY_UKRAINE       = 380;
        public static final int  TWCY_USVIRGINIS    = 340;
        public static final int  TWCY_VIETNAM       =  84;
        private TWCY() {}
    }

    public enum CAP_LANGUAGE
    {
        TWLG_DAN                     ,
        TWLG_DUT                     ,
        TWLG_ENG                     ,
        TWLG_FCF                     ,
        TWLG_FIN                          ,
        TWLG_FRN                          ,
        TWLG_GER                          ,
        TWLG_ICE                          ,
        TWLG_ITN                          ,
        TWLG_NOR                          ,
        TWLG_POR                          ,
        TWLG_SPA                          ,
        TWLG_SWE                          ,
        TWLG_USA                          ,
        TWLG_AFRIKAANS                    ,
        TWLG_ALBANIA                      ,
        TWLG_ARABIC                       ,
        TWLG_ARABIC_ALGERIA               ,
        TWLG_ARABIC_BAHRAIN               ,
        TWLG_ARABIC_EGYPT                 ,
        TWLG_ARABIC_IRAQ                  ,
        TWLG_ARABIC_JORDAN                ,
        TWLG_ARABIC_KUWAIT                ,
        TWLG_ARABIC_LEBANON               ,
        TWLG_ARABIC_LIBYA                 ,
        TWLG_ARABIC_MOROCCO               ,
        TWLG_ARABIC_OMAN                  ,
        TWLG_ARABIC_QATAR                 ,
        TWLG_ARABIC_SAUDIARABIA           ,
        TWLG_ARABIC_SYRIA                 ,
        TWLG_ARABIC_TUNISIA               ,
        TWLG_ARABIC_UAE                   ,
        TWLG_ARABIC_YEMEN                 ,
        TWLG_BASQUE                       ,
        TWLG_BYELORUSSIAN                 ,
        TWLG_BULGARIAN                    ,
        TWLG_CATALAN                      ,
        TWLG_CHINESE                      ,
        TWLG_CHINESE_HONGKONG             ,
        TWLG_CHINESE_PRC                  ,
        TWLG_CHINESE_SINGAPORE            ,
        TWLG_CHINESE_SIMPLIFIED           ,
        TWLG_CHINESE_TAIWAN               ,
        TWLG_CHINESE_TRADITIONAL          ,
        TWLG_CROATIA                      ,
        TWLG_CZECH                        ,
        TWLG_DUTCH_BELGIAN                ,
        TWLG_ENGLISH_AUSTRALIAN           ,
        TWLG_ENGLISH_CANADIAN             ,
        TWLG_ENGLISH_IRELAND              ,
        TWLG_ENGLISH_NEWZEALAND           ,
        TWLG_ENGLISH_SOUTHAFRICA          ,
        TWLG_ENGLISH_UK                   ,
        TWLG_ESTONIAN                     ,
        TWLG_FAEROESE                     ,
        TWLG_FARSI                        ,
        TWLG_FRENCH_BELGIAN               ,
        TWLG_FRENCH_LUXEMBOURG            ,
        TWLG_FRENCH_SWISS                 ,
        TWLG_GERMAN_AUSTRIAN              ,
        TWLG_GERMAN_LUXEMBOURG            ,
        TWLG_GERMAN_LIECHTENSTEIN         ,
        TWLG_GERMAN_SWISS                 ,
        TWLG_GREEK                        ,
        TWLG_HEBREW                       ,
        TWLG_HUNGARIAN                    ,
        TWLG_INDONESIAN                   ,
        TWLG_ITALIAN_SWISS                ,
        TWLG_JAPANESE                     ,
        TWLG_KOREAN                       ,
        TWLG_KOREAN_JOHAB                 ,
        TWLG_LATVIAN                      ,
        TWLG_LITHUANIAN                   ,
        TWLG_NORWEGIAN_BOKMAL             ,
        TWLG_NORWEGIAN_NYNORSK            ,
        TWLG_POLISH                       ,
        TWLG_PORTUGUESE_BRAZIL            ,
        TWLG_ROMANIAN                     ,
        TWLG_RUSSIAN                      ,
        TWLG_SERBIAN_LATIN                ,
        TWLG_SLOVAK                       ,
        TWLG_SLOVENIAN                    ,
        TWLG_SPANISH_MEXICAN              ,
        TWLG_SPANISH_MODERN               ,
        TWLG_THAI                         ,
        TWLG_TURKISH                      ,
        TWLG_UKRANIAN                     ,
        TWLG_ASSAMESE                     ,
        TWLG_BENGALI                      ,
        TWLG_BIHARI                       ,
        TWLG_BODO                         ,
        TWLG_DOGRI                        ,
        TWLG_GUJARATI                     ,
        TWLG_HARYANVI                     ,
        TWLG_HINDI                        ,
        TWLG_KANNADA                      ,
        TWLG_KASHMIRI                     ,
        TWLG_MALAYALAM                    ,
        TWLG_MARATHI                      ,
        TWLG_MARWARI                      ,
        TWLG_MEGHALAYAN                   ,
        TWLG_MIZO                         ,
        TWLG_NAGA                         ,
        TWLG_ORISSI                       ,
        TWLG_PUNJABI                      ,
        TWLG_PUSHTU                       ,
        TWLG_SERBIAN_CYRILLIC             ,
        TWLG_SIKKIMI                      ,
        TWLG_SWEDISH_FINLAND              ,
        TWLG_TAMIL                        ,
        TWLG_TELUGU                       ,
        TWLG_TRIPURI                      ,
        TWLG_URDU                         ,
        TWLG_VIETNAMESE                   ,
        TWLG_DEFAULT                      ;

        public static final CAP_LANGUAGE TWLG_DANISH = TWLG_DAN;
        public static final CAP_LANGUAGE TWLG_DUTCH = TWLG_DUT;
        public static final CAP_LANGUAGE TWLG_ENGLISH = TWLG_ENG;
        public static final CAP_LANGUAGE TWLG_ENGLISH_USA = TWLG_USA;
        public static final CAP_LANGUAGE TWLG_FINNISH = TWLG_FIN;
        public static final CAP_LANGUAGE TWLG_FRENCH = TWLG_FRN;
        public static final CAP_LANGUAGE TWLG_FRENCH_CANADIAN = TWLG_FCF;
        public static final CAP_LANGUAGE TWLG_GERMAN = TWLG_GER;
        public static final CAP_LANGUAGE TWLG_ICELANDIC = TWLG_ICE;
        public static final CAP_LANGUAGE TWLG_ITALIAN = TWLG_ITN;
        public static final CAP_LANGUAGE TWLG_NORWEGIAN = TWLG_NOR;
        public static final CAP_LANGUAGE TWLG_PORTUGUESE = TWLG_POR;
        public static final CAP_LANGUAGE TWLG_SPANISH = TWLG_SPA;
        public static final CAP_LANGUAGE TWLG_SWEDISH = TWLG_SWE;
    }

    public static class CAP_LANGUAGEUSERLOCALE
    {
        public static final int  TWLG_USERLOCALE           =-1;
        private CAP_LANGUAGEUSERLOCALE() {}
    }

    public static class DG
    {
        /****************************************************************************
         * Data Groups                                                              *
         ****************************************************************************/
        public static final int  DG_CONTROL         = 0x0001;
        public static final int  DG_IMAGE           = 0x0002;
        public static final int  DG_AUDIO           = 0x0004;
        public static final int  DG_MASK             = 0xFFFF;
        private DG() {}
    }

    public class DF extends DG
    {
        /* More Data Functionality may be added in the future.
         * These are for items that need to be determined before DS is opened.
         * NOTE: Supported Functionality constants must be powers of 2 as they are
         *       used as bitflags when Application asks DSM to present a list of DSs.
         *       to support backward capability the App and DS will not use the fields
         */
        public static final int  DF_DSM2             = 0x10000000;
        public static final int  DF_APP2             = 0x20000000;
        public static final int  DF_DS2              = 0x40000000;
        private DF() {}
    }

    public static class DAT
    {
        /****************************************************************************
         *                                                        *
         ****************************************************************************/
        public static final int  DAT_NULL            = 0x0000;
        public static final int  DAT_CUSTOMBASE      = 0x8000;

        /* Data Argument Types for the DG_CONTROL Data Group. */
        public static final int  DAT_CAPABILITY      = 0x0001;
        public static final int  DAT_EVENT           = 0x0002;
        public static final int  DAT_IDENTITY        = 0x0003;
        public static final int  DAT_PARENT          = 0x0004;
        public static final int  DAT_PENDINGXFERS    = 0x0005;
        public static final int  DAT_SETUPMEMXFER    = 0x0006;
        public static final int  DAT_SETUPFILEXFER   = 0x0007;
        public static final int  DAT_STATUS          = 0x0008;
        public static final int  DAT_USERINTERFACE   = 0x0009;
        public static final int  DAT_XFERGROUP       = 0x000a;
        public static final int  DAT_CUSTOMDSDATA    = 0x000c;
        public static final int  DAT_DEVICEEVENT     = 0x000d;
        public static final int  DAT_FILESYSTEM      = 0x000e;
        public static final int  DAT_PASSTHRU        = 0x000f;
        public static final int  DAT_CALLBACK        = 0x0010;
        public static final int  DAT_STATUSUTF8      = 0x0011;
        public static final int  DAT_CALLBACK2       = 0x0012;
        public static final int  DAT_METRICS         = 0x0013;
        public static final int  DAT_TWAINDIRECT     = 0x0014;

        /* Data Argument Types for the DG_IMAGE Data Group. */
        public static final int  DAT_IMAGEINFO       = 0x0101;
        public static final int  DAT_IMAGELAYOUT     = 0x0102;
        public static final int  DAT_IMAGEMEMXFER    = 0x0103;
        public static final int  DAT_IMAGENATIVEXFER = 0x0104;
        public static final int  DAT_IMAGEFILEXFER   = 0x0105;
        public static final int  DAT_CIECOLOR        = 0x0106;
        public static final int  DAT_GRAYRESPONSE    = 0x0107;
        public static final int  DAT_RGBRESPONSE     = 0x0108;
        public static final int  DAT_JPEGCOMPRESSION = 0x0109;
        public static final int  DAT_PALETTE8        = 0x010a;
        public static final int  DAT_EXTIMAGEINFO    = 0x010b;
        public static final int  DAT_FILTER          = 0x010c;

        /* Data Argument Types for the DG_AUDIO Data Group. */
        public static final int  DAT_AUDIOFILEXFER   = 0x0201;
        public static final int  DAT_AUDIOINFO       = 0x0202;
        public static final int  DAT_AUDIONATIVEXFER = 0x0203;

        /* misplaced */
        public static final int  DAT_ICCPROFILE        = 0x0401;
        public static final int  DAT_IMAGEMEMFILEXFER  = 0x0402;
        public static final int  DAT_ENTRYPOINT        = 0x0403;

        public static final int  DAT_TWUNKIDENTITY     = 0x000b;
        public static final int  DAT_SETUPFILEXFER2    = 0x0301;
        private DAT() {}
    }

    public static class MSG
    {

        /****************************************************************************
         * Messages                                                                 *
         ****************************************************************************/

        /* All message constants are unique.
         * Messages are grouped according to which DATs they are used with.*/

        public static final int  MSG_NULL            = 0x0000;
        public static final int  MSG_CUSTOMBASE      = 0x8000;

        /* Generic messages may be used with any of several DATs.                   */
        public static final int  MSG_GET             = 0x0001;
        public static final int  MSG_GETCURRENT      = 0x0002;
        public static final int  MSG_GETDEFAULT      = 0x0003;
        public static final int  MSG_GETFIRST        = 0x0004;
        public static final int  MSG_GETNEXT         = 0x0005;
        public static final int  MSG_SET             = 0x0006;
        public static final int  MSG_RESET           = 0x0007;
        public static final int  MSG_QUERYSUPPORT    = 0x0008;
        public static final int  MSG_GETHELP         = 0x0009;
        public static final int  MSG_GETLABEL        = 0x000a;
        public static final int  MSG_GETLABELENUM    = 0x000b;
        public static final int  MSG_SETCONSTRAINT   = 0x000c;

        /* Messages used with DAT_NULL                                              */
        public static final int  MSG_XFERREADY    = 0x0101;
        public static final int  MSG_CLOSEDSREQ   = 0x0102;
        public static final int  MSG_CLOSEDSOK    = 0x0103;
        public static final int  MSG_DEVICEEVENT  = 0X0104;

        /* Messages used with a pointer to DAT_PARENT data                          */
        public static final int  MSG_OPENDSM      = 0x0301;
        public static final int  MSG_CLOSEDSM     = 0x0302;

        /* Messages used with a pointer to a DAT_IDENTITY structure                 */
        public static final int  MSG_OPENDS       = 0x0401;
        public static final int  MSG_CLOSEDS      = 0x0402;
        public static final int  MSG_USERSELECT   = 0x0403;

        /* Messages used with a pointer to a DAT_USERINTERFACE structure            */
        public static final int  MSG_DISABLEDS    = 0x0501;
        public static final int  MSG_ENABLEDS     = 0x0502;
        public static final int  MSG_ENABLEDSUIONLY  = 0x0503;

        /* Messages used with a pointer to a DAT_EVENT structure                    */
        public static final int  MSG_PROCESSEVENT = 0x0601;

        /* Messages used with a pointer to a DAT_PENDINGXFERS structure             */
        public static final int  MSG_ENDXFER      = 0x0701;
        public static final int  MSG_STOPFEEDER   = 0x0702;

        /* Messages used with a pointer to a DAT_FILESYSTEM structure               */
        public static final int  MSG_CHANGEDIRECTORY  = 0x0801;
        public static final int  MSG_CREATEDIRECTORY  = 0x0802;
        public static final int  MSG_DELETE           = 0x0803;
        public static final int  MSG_FORMATMEDIA      = 0x0804;
        public static final int  MSG_GETCLOSE         = 0x0805;
        public static final int  MSG_GETFIRSTFILE     = 0x0806;
        public static final int  MSG_GETINFO          = 0x0807;
        public static final int  MSG_GETNEXTFILE      = 0x0808;
        public static final int  MSG_RENAME           = 0x0809;
        public static final int  MSG_COPY             = 0x080A;
        public static final int  MSG_AUTOMATICCAPTUREDIRECTORY = 0x080B;

        /* Messages used with a pointer to a DAT_PASSTHRU structure                 */
        public static final int  MSG_PASSTHRU         = 0x0901;

        /* used with DAT_CALLBACK */
        public static final int  MSG_REGISTER_CALLBACK = 0x0902;

        /* used with DAT_CAPABILITY */
        public static final int  MSG_RESETALL          = 0x0A01;

        /* used with DAT_TWAINDIRECT */
        public static final int  MSG_SETTASK           = 0x0B01;
        public static final int  MSG_CHECKSTATUS       = 0x0201;
        public static final int  MSG_INVOKE_CALLBACK   = 0x0903;
        private MSG() {}
    }

    public static class TWEI
    {
        /***************************************************************************
         *            Extended Image Info Attributes section  Added 1.7            *
         ***************************************************************************/
        public static final int  TWEI_BARCODEX               = 0x1200;
        public static final int  TWEI_BARCODEY               = 0x1201;
        public static final int  TWEI_BARCODETEXT            = 0x1202;
        public static final int  TWEI_BARCODETYPE            = 0x1203;
        public static final int  TWEI_DESHADETOP             = 0x1204;
        public static final int  TWEI_DESHADELEFT            = 0x1205;
        public static final int  TWEI_DESHADEHEIGHT          = 0x1206;
        public static final int  TWEI_DESHADEWIDTH           = 0x1207;
        public static final int  TWEI_DESHADESIZE            = 0x1208;
        public static final int  TWEI_SPECKLESREMOVED        = 0x1209;
        public static final int  TWEI_HORZLINEXCOORD         = 0x120A;
        public static final int  TWEI_HORZLINEYCOORD         = 0x120B;
        public static final int  TWEI_HORZLINELENGTH         = 0x120C;
        public static final int  TWEI_HORZLINETHICKNESS      = 0x120D;
        public static final int  TWEI_VERTLINEXCOORD         = 0x120E;
        public static final int  TWEI_VERTLINEYCOORD         = 0x120F;
        public static final int  TWEI_VERTLINELENGTH         = 0x1210;
        public static final int  TWEI_VERTLINETHICKNESS      = 0x1211;
        public static final int  TWEI_PATCHCODE              = 0x1212;
        public static final int  TWEI_ENDORSEDTEXT           = 0x1213;
        public static final int  TWEI_FORMCONFIDENCE         = 0x1214;
        public static final int  TWEI_FORMTEMPLATEMATCH      = 0x1215;
        public static final int  TWEI_FORMTEMPLATEPAGEMATCH  = 0x1216;
        public static final int  TWEI_FORMHORZDOCOFFSET      = 0x1217;
        public static final int  TWEI_FORMVERTDOCOFFSET      = 0x1218;
        public static final int  TWEI_BARCODECOUNT           = 0x1219;
        public static final int  TWEI_BARCODECONFIDENCE      = 0x121A;
        public static final int  TWEI_BARCODEROTATION        = 0x121B;
        public static final int  TWEI_BARCODETEXTLENGTH      = 0x121C;
        public static final int  TWEI_DESHADECOUNT           = 0x121D;
        public static final int  TWEI_DESHADEBLACKCOUNTOLD   = 0x121E;
        public static final int  TWEI_DESHADEBLACKCOUNTNEW   = 0x121F;
        public static final int  TWEI_DESHADEBLACKRLMIN      = 0x1220;
        public static final int  TWEI_DESHADEBLACKRLMAX      = 0x1221;
        public static final int  TWEI_DESHADEWHITECOUNTOLD   = 0x1222;
        public static final int  TWEI_DESHADEWHITECOUNTNEW   = 0x1223;
        public static final int  TWEI_DESHADEWHITERLMIN      = 0x1224;
        public static final int  TWEI_DESHADEWHITERLAVE      = 0x1225;
        public static final int  TWEI_DESHADEWHITERLMAX      = 0x1226;
        public static final int  TWEI_BLACKSPECKLESREMOVED   = 0x1227;
        public static final int  TWEI_WHITESPECKLESREMOVED   = 0x1228;
        public static final int  TWEI_HORZLINECOUNT          = 0x1229;
        public static final int  TWEI_VERTLINECOUNT          = 0x122A;
        public static final int  TWEI_DESKEWSTATUS           = 0x122B;
        public static final int  TWEI_SKEWORIGINALANGLE      = 0x122C;
        public static final int  TWEI_SKEWFINALANGLE         = 0x122D;
        public static final int  TWEI_SKEWCONFIDENCE         = 0x122E;
        public static final int  TWEI_SKEWWINDOWX1           = 0x122F;
        public static final int  TWEI_SKEWWINDOWY1           = 0x1230;
        public static final int  TWEI_SKEWWINDOWX2           = 0x1231;
        public static final int  TWEI_SKEWWINDOWY2           = 0x1232;
        public static final int  TWEI_SKEWWINDOWX3           = 0x1233;
        public static final int  TWEI_SKEWWINDOWY3           = 0x1234;
        public static final int  TWEI_SKEWWINDOWX4           = 0x1235;
        public static final int  TWEI_SKEWWINDOWY4           = 0x1236;
        public static final int  TWEI_BOOKNAME               = 0x1238;
        public static final int  TWEI_CHAPTERNUMBER          = 0x1239;
        public static final int  TWEI_DOCUMENTNUMBER         = 0x123A;
        public static final int  TWEI_PAGENUMBER             = 0x123B;
        public static final int  TWEI_CAMERA                 = 0x123C;
        public static final int  TWEI_FRAMENUMBER            = 0x123D;
        public static final int  TWEI_FRAME                  = 0x123E;
        public static final int  TWEI_PIXELFLAVOR            = 0x123F;
        public static final int  TWEI_ICCPROFILE             = 0x1240;
        public static final int  TWEI_LASTSEGMENT            = 0x1241;
        public static final int  TWEI_SEGMENTNUMBER          = 0x1242;
        public static final int  TWEI_MAGDATA                = 0x1243;
        public static final int  TWEI_MAGTYPE                = 0x1244;
        public static final int  TWEI_PAGESIDE               = 0x1245;
        public static final int  TWEI_FILESYSTEMSOURCE       = 0x1246;
        public static final int  TWEI_IMAGEMERGED            = 0x1247;
        public static final int  TWEI_MAGDATALENGTH          = 0x1248;
        public static final int  TWEI_PAPERCOUNT             = 0x1249;
        public static final int  TWEI_PRINTERTEXT            = 0x124A;
        public static final int  TWEI_TWAINDIRECTMETADATA    = 0x124B;
        public static final int  TWEI_IAFIELDA_VALUE         = 0x124C;
        public static final int  TWEI_IAFIELDB_VALUE         = 0x124D;
        public static final int  TWEI_IAFIELDC_VALUE         = 0x124E;
        public static final int  TWEI_IAFIELDD_VALUE         = 0x124F;
        public static final int  TWEI_IAFIELDE_VALUE         = 0x1250;
        public static final int  TWEI_IALEVEL                = 0x1251;
        public static final int  TWEI_PRINTER                = 0x1252;
        public static final int  TWEI_BARCODETEXT2           = 0x1253;
        private TWEI() {}
    }

    public static class TWEJ
    {
        public static final int  TWEJ_NONE                   = 0x0000;
        public static final int  TWEJ_MIDSEPARATOR           = 0x0001;
        public static final int  TWEJ_PATCH1                 = 0x0002;
        public static final int  TWEJ_PATCH2                 = 0x0003;
        public static final int  TWEJ_PATCH3                 = 0x0004;
        public static final int  TWEJ_PATCH4                 = 0x0005;
        public static final int  TWEJ_PATCH6                 = 0x0006;
        public static final int  TWEJ_PATCHT                 = 0x0007;
        private TWEJ() {}
    }

    public static class TWRC
    {
        /***************************************************************************
         *            Return Codes and Condition Codes section                     *
         ***************************************************************************/
        public static final int  TWRC_CUSTOMBASE     = 0x8000;

        public static final int  TWRC_SUCCESS          = 0;
        public static final int  TWRC_FAILURE          = 1;
        public static final int  TWRC_CHECKSTATUS      = 2;
        public static final int  TWRC_CANCEL           = 3;
        public static final int  TWRC_DSEVENT          = 4;
        public static final int  TWRC_NOTDSEVENT       = 5;
        public static final int  TWRC_XFERDONE         = 6;
        public static final int  TWRC_ENDOFLIST        = 7;
        public static final int  TWRC_INFONOTSUPPORTED = 8;
        public static final int  TWRC_DATANOTAVAILABLE = 9;
        public static final int  TWRC_BUSY             = 10;
        public static final int  TWRC_SCANNERLOCKED    = 11;
        /* Special code to indicate the wrong Java object was used in call to Data Source Manager */
        public static final int  TWRC_WRONGOBJECT      = 10000;
        private TWRC() {}
    }

    public static class TWCC
    {
        /* Condition Codes: Application gets these by doing DG_CONTROL DAT_STATUS MSG_GET.  */
        public static final int  TWCC_CUSTOMBASE       = 0x8000;

        public static final int  TWCC_SUCCESS            = 0;
        public static final int  TWCC_BUMMER             = 1;
        public static final int  TWCC_LOWMEMORY          = 2;
        public static final int  TWCC_NODS               = 3;
        public static final int  TWCC_MAXCONNECTIONS     = 4;
        public static final int  TWCC_OPERATIONERROR     = 5;
        public static final int  TWCC_BADCAP             = 6;
        public static final int  TWCC_BADPROTOCOL        = 9;
        public static final int  TWCC_BADVALUE           = 10;
        public static final int  TWCC_SEQERROR           = 11;
        public static final int  TWCC_BADDEST            = 12;
        public static final int  TWCC_CAPUNSUPPORTED     = 13;
        public static final int  TWCC_CAPBADOPERATION    = 14;
        public static final int  TWCC_CAPSEQERROR        = 15;
        public static final int  TWCC_DENIED             = 16;
        public static final int  TWCC_FILEEXISTS         = 17;
        public static final int  TWCC_FILENOTFOUND       = 18;
        public static final int  TWCC_NOTEMPTY           = 19;
        public static final int  TWCC_PAPERJAM           = 20;
        public static final int  TWCC_PAPERDOUBLEFEED    = 21;
        public static final int  TWCC_FILEWRITEERROR     = 22;
        public static final int  TWCC_CHECKDEVICEONLINE  = 23;
        public static final int  TWCC_INTERLOCK          = 24;
        public static final int  TWCC_DAMAGEDCORNER      = 25;
        public static final int  TWCC_FOCUSERROR         = 26;
        public static final int  TWCC_DOCTOOLIGHT        = 27;
        public static final int  TWCC_DOCTOODARK         = 28;
        public static final int  TWCC_NOMEDIA            = 29;
        private TWCC() {}
    }

    public static class TWQC
    {
        /* bit patterns: for query the operation that are supported by the data source on a capability */
        /* Application gets these through DG_CONTROL/DAT_CAPABILITY/MSG_QUERYSUPPORT */
        public static final int  TWQC_GET              = 0x0001;
        public static final int  TWQC_SET              = 0x0002;
        public static final int  TWQC_GETDEFAULT       = 0x0004;
        public static final int  TWQC_GETCURRENT       = 0x0008;
        public static final int  TWQC_RESET            = 0x0010;
        public static final int  TWQC_SETCONSTRAINT    = 0x0020;
        public static final int  TWQC_GETHELP          = 0x0100;
        public static final int  TWQC_GETLABEL         = 0x0200;
        public static final int  TWQC_GETLABELENUM     = 0x0400;
        public static final int  TWQC_CONSTRAINABLE    = 0x0040;
        private TWQC() {}
    }

    public static class CAP_FILESYSTEM
    {
        /* CAP_FILESYSTEM values (FS_ means file system) */
        public static final int  TWFS_FILESYSTEM       = 0;
        public static final int  TWFS_RECURSIVEDELETE  = 1;

        private CAP_FILESYSTEM() {}
    }


    public static class TWSX
    {
        public static final int  TWSX_FILE2            = 3;
        private TWSX() {}
    }

    public static class ACAP_AUDIOFILEFORMAT
    {

        /* ACAP_AUDIOFILEFORMAT values (AF_ means audio format).  Added 1.8 */
        public static final int  TWAF_WAV      = 0;
        public static final int  TWAF_AIFF     = 1;
        public static final int  TWAF_AU       = 3;
        public static final int  TWAF_SND      = 4;

        private ACAP_AUDIOFILEFORMAT() {}
    }

    public static class CAP_CLEARBUFFERS
    {
        private CAP_CLEARBUFFERS() {}
        /* CAP_CLEARBUFFERS values */
        public static final int  TWCB_AUTO                = 0;
        public static final int  TWCB_CLEAR               = 1;
        public static final int  TWCB_NOCLEAR             = 2;
    }
}

