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
package com.dynarithmic.twain.highlevel.acquirecharacteristics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.dynarithmic.twain.DTwainConstants;
import com.dynarithmic.twain.DTwainConstants.MultipageSaveMode;

public class FileTransferOptions
{
    public class FileTransferFlags
    {
        boolean bUseName = true;
        boolean bUseSaveDialog = false;
        boolean bUseDeviceMode = false;
        boolean bUseCreateDirectory = true;

        public FileTransferFlags useName(boolean uName)
        {
            this.bUseName = uName;
            return this;
        }

        public FileTransferFlags useSaveDialog(boolean uSaveDialog)
        {
            this.bUseSaveDialog = uSaveDialog;
            return this;
        }

        public FileTransferFlags useDeviceTransfer(boolean uMode)
        {
            this.bUseDeviceMode = uMode;
            return this;
        }

        public FileTransferFlags autoCreateDirectory(boolean autoCreateDir)
        {
            this.bUseCreateDirectory = autoCreateDir;
            return this;
        }

        public boolean isDeviceTransfer()
        {
            return this.bUseDeviceMode;
        }

        public boolean isUseName()
        {
            return bUseName;
        }

        public boolean isUseSaveDialog()
        {
            return this.bUseSaveDialog;
        }

        public boolean isAutoCreateDirectory()
        {
            return this.bUseCreateDirectory;
        }
    }

    public class FilenameIncrementOptions
    {
        private boolean enabled = false;
        private int incrementValue = 1;
        private boolean resetCount = false;

        public FilenameIncrementOptions enable(boolean enabled)
        {
            this.enabled = enabled;
            return this;
        }

        public FilenameIncrementOptions setIncrementValue(int value)
        {
            this.incrementValue = value;
            return this;
        }

        public FilenameIncrementOptions useResetCount(boolean resetCount)
        {
            this.resetCount = resetCount;
            return this;
        }

        public boolean isEnabled()
        {
            return enabled;
        }

        public boolean isResetCount()
        {
            return resetCount;
        }

        public int getIncrementValue()
        {
            return incrementValue;
        }
    }

    public class MultipageSaveOptions
    {
        private MultipageSaveMode multiPageSaveMode = MultipageSaveMode.DEFAULT;
        private boolean saveIncomplete = false;

        public MultipageSaveOptions setSaveMode(MultipageSaveMode multiPageSaveMode)
        {
            this.multiPageSaveMode = multiPageSaveMode;
            return this;
        }

        public MultipageSaveOptions setSaveIncomplete(boolean saveIncomplete)
        {
            this.saveIncomplete = saveIncomplete;
            return this;
        }

        public MultipageSaveMode getSaveMode()
        {
            return multiPageSaveMode;
        }

        public boolean isSaveIncomplete()
        {
            return saveIncomplete;
        }
    }

    public static class FileTypeInfo
    {
        private FileTypeInfo() {}
        private static Map<DTwainConstants.FileType,
                       DTwainConstants.FileTypeMultiPage> multipage_map = new HashMap<>();
        private static Map<DTwainConstants.FileTypeMultiPage,
                       DTwainConstants.FileType> multipage_map2 = new HashMap<>();
        private static Set<DTwainConstants.FileType> alldevicesupport_set = new HashSet<>();
        private static Set<DTwainConstants.FileType> sourcedevicesupport_set = new HashSet<>();

        static
        {
            multipage_map.put(DTwainConstants.FileType.PCX, DTwainConstants.FileTypeMultiPage.PCX);
            multipage_map.put(DTwainConstants.FileType.PDF, DTwainConstants.FileTypeMultiPage.PDF );
            multipage_map.put(DTwainConstants.FileType.POSTSCRIPT1, DTwainConstants.FileTypeMultiPage.POSTSCRIPT1 );
            multipage_map.put(DTwainConstants.FileType.POSTSCRIPT2, DTwainConstants.FileTypeMultiPage.POSTSCRIPT2 );
            multipage_map.put(DTwainConstants.FileType.POSTSCRIPT3, DTwainConstants.FileTypeMultiPage.POSTSCRIPT3 );
            multipage_map.put(DTwainConstants.FileType.TIFFDEFLATE, DTwainConstants.FileTypeMultiPage.TIFFDEFLATE );
            multipage_map.put(DTwainConstants.FileType.TIFFG3, DTwainConstants.FileTypeMultiPage.TIFFG3 );
            multipage_map.put(DTwainConstants.FileType.TIFFG4, DTwainConstants.FileTypeMultiPage.TIFFG4 );
            multipage_map.put(DTwainConstants.FileType.TIFFJPEG, DTwainConstants.FileTypeMultiPage.TIFFJPEG );
            multipage_map.put(DTwainConstants.FileType.TIFFLZW, DTwainConstants.FileTypeMultiPage.TIFFLZW );
            multipage_map.put(DTwainConstants.FileType.TIFFNONE, DTwainConstants.FileTypeMultiPage.TIFFNOCOMPRESS );
            multipage_map.put(DTwainConstants.FileType.TIFFPACKBITS, DTwainConstants.FileTypeMultiPage.TIFFPACKBITS );
            multipage_map.put(DTwainConstants.FileType.BIGTIFFDEFLATE, DTwainConstants.FileTypeMultiPage.BIGTIFFDEFLATE );
            multipage_map.put(DTwainConstants.FileType.BIGTIFFG3, DTwainConstants.FileTypeMultiPage.BIGTIFFG3 );
            multipage_map.put(DTwainConstants.FileType.BIGTIFFG4, DTwainConstants.FileTypeMultiPage.BIGTIFFG4 );
            multipage_map.put(DTwainConstants.FileType.BIGTIFFJPEG, DTwainConstants.FileTypeMultiPage.BIGTIFFJPEG );
            multipage_map.put(DTwainConstants.FileType.BIGTIFFLZW, DTwainConstants.FileTypeMultiPage.BIGTIFFLZW );
            multipage_map.put(DTwainConstants.FileType.BIGTIFFNONE, DTwainConstants.FileTypeMultiPage.BIGTIFFNOCOMPRESS );
            multipage_map.put(DTwainConstants.FileType.BIGTIFFPACKBITS, DTwainConstants.FileTypeMultiPage.BIGTIFFPACKBITS );
            multipage_map.put(DTwainConstants.FileType.TEXT, DTwainConstants.FileTypeMultiPage.TEXT);

            multipage_map.forEach((k, v) -> multipage_map2.put(v,k));

            alldevicesupport_set.add(DTwainConstants.FileType.BMP);
            alldevicesupport_set.add(DTwainConstants.FileType.DCX);
            alldevicesupport_set.add(DTwainConstants.FileType.EMF);
            alldevicesupport_set.add(DTwainConstants.FileType.GIF);
            alldevicesupport_set.add(DTwainConstants.FileType.GOOGLEWEBP);
            alldevicesupport_set.add(DTwainConstants.FileType.JPEG);
            alldevicesupport_set.add(DTwainConstants.FileType.JPEG2000);
            alldevicesupport_set.add(DTwainConstants.FileType.PCX);
            alldevicesupport_set.add(DTwainConstants.FileType.PDF);
            alldevicesupport_set.add(DTwainConstants.FileType.PNG);
            alldevicesupport_set.add(DTwainConstants.FileType.POSTSCRIPT1);
            alldevicesupport_set.add(DTwainConstants.FileType.POSTSCRIPT2);
            alldevicesupport_set.add(DTwainConstants.FileType.POSTSCRIPT3);
            alldevicesupport_set.add(DTwainConstants.FileType.PSD);
            alldevicesupport_set.add(DTwainConstants.FileType.TGA);
            alldevicesupport_set.add(DTwainConstants.FileType.TEXT);
            alldevicesupport_set.add(DTwainConstants.FileType.TIFFDEFLATE);
            alldevicesupport_set.add(DTwainConstants.FileType.TIFFG3);
            alldevicesupport_set.add(DTwainConstants.FileType.TIFFG4);
            alldevicesupport_set.add(DTwainConstants.FileType.TIFFJPEG);
            alldevicesupport_set.add(DTwainConstants.FileType.TIFFLZW);
            alldevicesupport_set.add(DTwainConstants.FileType.TIFFNONE);
            alldevicesupport_set.add(DTwainConstants.FileType.TIFFPACKBITS);
            alldevicesupport_set.add(DTwainConstants.FileType.BIGTIFFDEFLATE);
            alldevicesupport_set.add(DTwainConstants.FileType.BIGTIFFG3);
            alldevicesupport_set.add(DTwainConstants.FileType.BIGTIFFG4);
            alldevicesupport_set.add(DTwainConstants.FileType.BIGTIFFJPEG);
            alldevicesupport_set.add(DTwainConstants.FileType.BIGTIFFLZW);
            alldevicesupport_set.add(DTwainConstants.FileType.BIGTIFFNONE);
            alldevicesupport_set.add(DTwainConstants.FileType.BIGTIFFPACKBITS);
            alldevicesupport_set.add(DTwainConstants.FileType.ICO);
            alldevicesupport_set.add(DTwainConstants.FileType.WMF);
            alldevicesupport_set.add(DTwainConstants.FileType.EMF);
            alldevicesupport_set.add(DTwainConstants.FileType.WBMP);
            alldevicesupport_set.add(DTwainConstants.FileType.GOOGLEWEBP);
            alldevicesupport_set.add(DTwainConstants.FileType.PPM);
            alldevicesupport_set.add(DTwainConstants.FileType.ICO_VISTA);
            alldevicesupport_set.add(DTwainConstants.FileType.TIFFNONEMULTI);
            alldevicesupport_set.add(DTwainConstants.FileType.TIFFG3MULTI);
            alldevicesupport_set.add(DTwainConstants.FileType.TIFFG4MULTI);
            alldevicesupport_set.add(DTwainConstants.FileType.TIFFPACKBITSMULTI);
            alldevicesupport_set.add(DTwainConstants.FileType.TIFFDEFLATEMULTI);
            alldevicesupport_set.add(DTwainConstants.FileType.TIFFJPEGMULTI);
            alldevicesupport_set.add(DTwainConstants.FileType.TIFFLZWMULTI);
            alldevicesupport_set.add(DTwainConstants.FileType.TIFFJBIGMULTI);
            alldevicesupport_set.add(DTwainConstants.FileType.BIGTIFFNONEMULTI);
            alldevicesupport_set.add(DTwainConstants.FileType.BIGTIFFG3MULTI);
            alldevicesupport_set.add(DTwainConstants.FileType.BIGTIFFG4MULTI);
            alldevicesupport_set.add(DTwainConstants.FileType.BIGTIFFPACKBITSMULTI);
            alldevicesupport_set.add(DTwainConstants.FileType.BIGTIFFDEFLATEMULTI);
            alldevicesupport_set.add(DTwainConstants.FileType.BIGTIFFJPEGMULTI);
            alldevicesupport_set.add(DTwainConstants.FileType.BIGTIFFLZWMULTI);
            alldevicesupport_set.add(DTwainConstants.FileType.POSTSCRIPT1MULTI);
            alldevicesupport_set.add(DTwainConstants.FileType.POSTSCRIPT2MULTI);
            alldevicesupport_set.add(DTwainConstants.FileType.POSTSCRIPT3MULTI);
            alldevicesupport_set.add(DTwainConstants.FileType.PDFMULTI);
            alldevicesupport_set.add(DTwainConstants.FileType.TEXTMULTI);
            alldevicesupport_set.add(DTwainConstants.FileType.BMP_RLE);
            alldevicesupport_set.add(DTwainConstants.FileType.TGA_RLE);

            sourcedevicesupport_set.add(DTwainConstants.FileType.BMP_SOURCE_MODE);
            sourcedevicesupport_set.add(DTwainConstants.FileType.TIFF_SOURCE_MODE);
            sourcedevicesupport_set.add(DTwainConstants.FileType.PICT_SOURCE_MODE);
            sourcedevicesupport_set.add(DTwainConstants.FileType.XBM_SOURCE_MODE);
            sourcedevicesupport_set.add(DTwainConstants.FileType.JFIF_SOURCE_MODE);
            sourcedevicesupport_set.add(DTwainConstants.FileType.FPX_SOURCE_MODE);
            sourcedevicesupport_set.add(DTwainConstants.FileType.TIFFMULTI_SOURCE_MODE);
            sourcedevicesupport_set.add(DTwainConstants.FileType.PNG_SOURCE_MODE);
            sourcedevicesupport_set.add(DTwainConstants.FileType.SPIFF_SOURCE_MODE);
            sourcedevicesupport_set.add(DTwainConstants.FileType.EXIF_SOURCE_MODE);
            sourcedevicesupport_set.add(DTwainConstants.FileType.PDF_SOURCE_MODE);
            sourcedevicesupport_set.add(DTwainConstants.FileType.JP2_SOURCE_MODE);
            sourcedevicesupport_set.add(DTwainConstants.FileType.JPX_SOURCE_MODE);
            sourcedevicesupport_set.add(DTwainConstants.FileType.DEJAVU_SOURCE_MODE);
            sourcedevicesupport_set.add(DTwainConstants.FileType.PDFA_SOURCE_MODE);
            sourcedevicesupport_set.add(DTwainConstants.FileType.PDFA2_SOURCE_MODE);
            sourcedevicesupport_set.add(DTwainConstants.FileType.PDFRASTER_SOURCE_MODE);
        }

        public static Map<DTwainConstants.FileType, DTwainConstants.FileTypeMultiPage> getMultiPageMap()
        {
            return multipage_map;
        }

        public static Set<DTwainConstants.FileType> getUniversalSupportedFileTypes()
        {
            return alldevicesupport_set;
        }

        public static DTwainConstants.FileTypeMultiPage getMultipageType(DTwainConstants.FileType ft)
        {
            if ( multipage_map.containsKey(ft))
                return multipage_map.get(ft);
            return DTwainConstants.FileTypeMultiPage.NOTYPE;
        }

        public static boolean isUniversalSupport(DTwainConstants.FileType ft)
        {
            return alldevicesupport_set.contains(ft);
        }

        public static boolean isSourceSupportRequired(DTwainConstants.FileType ft)
        {
            return sourcedevicesupport_set.contains(ft);
        }
    }

    private String fileName = "temp.bmp";
    private DTwainConstants.FileType fileType = DTwainConstants.FileType.BMP;
    private boolean multiPage = false;
    private boolean autocreateDirectory = false;
    private FilenameIncrementOptions nameIncrementOptions = new FilenameIncrementOptions();
    private MultipageSaveOptions multipageSaveOptions = new MultipageSaveOptions();
    private FileTransferFlags fileTransferFlags = new FileTransferFlags();

    public FileTransferFlags getTransferFlags()
    {
        return this.fileTransferFlags;
    }

    public FileTransferOptions setType(DTwainConstants.FileType ft)
    {
        this.fileType = ft;
        return this;
    }

    public FileTransferOptions setName(String name)
    {
        this.fileName = name;
        return this;
    }

    public FileTransferOptions enableMultiPage(boolean multiPage)
    {
        this.multiPage = multiPage;
        return this;
    }

    public FileTransferOptions enableAutoCreateDirectory(boolean bEnable)
    {
        this.autocreateDirectory = bEnable;
        return this;
    }

    boolean isAutoCreateDirectoryEnabled()
    {
        return this.autocreateDirectory;
    }

    public FilenameIncrementOptions getFilenameIncrementOptions()
    {
        return nameIncrementOptions;
    }

    public MultipageSaveOptions getMultipageSaveOptions()
    {
        return this.multipageSaveOptions;
    }

    public DTwainConstants.FileType getType()
    {
        return this.fileType;
    }

    public String getName()
    {
        return this.fileName;
    }

    public boolean canMultiPage()
    {
        return multiPage &&
                (FileTypeInfo.getMultipageType(fileType) != DTwainConstants.FileTypeMultiPage.NOTYPE);
    }

    protected static final int [] affectedCaps = {};

    public static final int [] getAffectedCaps()
    {
        return affectedCaps;
    }
}
