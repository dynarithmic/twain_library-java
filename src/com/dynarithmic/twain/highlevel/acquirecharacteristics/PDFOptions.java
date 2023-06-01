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
package com.dynarithmic.twain.highlevel.acquirecharacteristics;

import java.util.ArrayList;
import java.util.List;
import java.security.SecureRandom;

import com.dynarithmic.twain.DTwainConstants.PDFFileOptions;
import com.dynarithmic.twain.DTwainConstants.PageOrientation;
import com.dynarithmic.twain.DTwainConstants.PaperSize;

public class PDFOptions
{
    public class PDFEncryption
    {
        private String userPassword = "";
        private String ownerPassword = "";
        private boolean enabled = false;
        private boolean strongEncryptionEnabled = false;
        private boolean autoPasswordGenerationEnabled = false;
        private int permissions = 0;
        private String range = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&()_+=-{}[];:'\\\",<.>/?`~";

        public String generateRandomPassword()
        {
            SecureRandom random = new SecureRandom();
            StringBuilder retString = new StringBuilder();
            int len = range.length();
            for (int i = 0; i < 32; ++i)
                retString.append(range.charAt(random.nextInt(len-1)));
            return retString.toString();
        }

        private PDFEncryption setPassword(StringBuilder pwdtoset, String pwdString, int nWhich)
        {
            if (this.autoPasswordGenerationEnabled && nWhich == 0)
                pwdtoset.append(generateRandomPassword());
            else
                pwdtoset.append(pwdString);
            return this;
        }


        public PDFEncryption setPermissions(List<PDFFileOptions.Protection> permissions)
        {
            this.permissions = 0;
            for (PDFFileOptions.Protection p : PDFFileOptions.Protection.values())
            {
                if (permissions.contains(p))
                    this.permissions += p.value();
            }
            return this;
        }

        public PDFEncryption setPermissions(int permissions)
        {
            this.permissions = permissions;
            return this;
        }

        public int getPermissionsAsInteger()
        {
            return this.permissions;
        }

        public List<PDFFileOptions.Protection> getPermissions()
        {
            List<PDFFileOptions.Protection> retList = new ArrayList<>();
            for (PDFFileOptions.Protection p : PDFFileOptions.Protection.values())
            {
                if (isPermissionSet(p))
                    retList.add(p);
            }
            return retList;
        }

        public boolean isPermissionSet(PDFFileOptions.Protection permission)
        {
            return (this.permissions & permission.value()) > 0;
        }

        public String getUserPassword()
        {
            return userPassword;
        }

        public String getOwnerPassword()
        {
            return ownerPassword;
        }

        public boolean isEnabled()
        {
            return enabled;
        }

        public boolean isStrongEncryptionEnabled()
        {
            return strongEncryptionEnabled;
        }


        public boolean isAutoPasswordGenerationEnabled()
        {
            return autoPasswordGenerationEnabled;
        }

        public PDFEncryption setUserPassword(String userPassword)
        {
            StringBuilder sBuild = new StringBuilder();
            setPassword(sBuild, userPassword, 0);
            this.userPassword = sBuild.toString();
            return this;
        }

        public PDFEncryption setOwnerPassword(String ownerPassword)
        {
            StringBuilder sBuild = new StringBuilder();
            setPassword(sBuild, ownerPassword, 1);
            this.ownerPassword = sBuild.toString();
            return this;
        }

        public PDFEncryption enable(boolean enabled)
        {
            this.enabled = enabled;
            return this;
        }

        public PDFEncryption enableStrongEncryption(boolean enableStrongEncryption)
        {
            this.strongEncryptionEnabled = enableStrongEncryption;
            return this;
        }

        public PDFEncryption enableAutoPasswordGeneration(boolean enableAutoPasswordGeneration)
        {
            this.autoPasswordGenerationEnabled = enableAutoPasswordGeneration;
            return this;
        }
    }

    public class PDFPaperSizeOptions
    {
        private PaperSize paperSize = PaperSize.USLETTER;
        private int customWidth = Integer.MAX_VALUE;
        private int customHeight = Integer.MAX_VALUE;
        private boolean customSizeEnabled = false;

        public PaperSize getPaperSize()
        {
            return paperSize;
        }

        public PDFPaperSizeOptions setPaperSize(PaperSize paperSize)
        {
            this.paperSize = paperSize;
            return this;
        }

        public PDFPaperSizeOptions setCustomSize(int customWidth, int customHeight)
        {
            this.customHeight = customHeight;
            this.customWidth = customWidth;
            return this;
        }

        public int getCustomWidth()
        {
            return this.customWidth;
        }

        public int getCustomHeight()
        {
            return this.customHeight;
        }

        public boolean isCustomSizeEnabled()
        {
            return customSizeEnabled;
        }

        public PDFPaperSizeOptions enableCustomSize(boolean customSizeEnabled)
        {
            this.customSizeEnabled = customSizeEnabled;
            return this;
        }

        public PDFPaperSizeOptions reset()
        {
            this.paperSize = PaperSize.USLETTER;
            this.customHeight = Integer.MAX_VALUE;
            this.customWidth = Integer.MAX_VALUE;
            this.customSizeEnabled = false;
            return this;
        }

        public PDFPaperSizeOptions setCustomWidth(int customWidth)
        {
            this.customWidth = customWidth;
            return this;
        }

        public PDFPaperSizeOptions setCustomHeight(int customHeight)
        {
            this.customHeight = customHeight;
            return this;
        }
    }

    public class PDFOrientation
    {
        private PageOrientation orientation = PageOrientation.PORTRAIT;

        public PageOrientation getOrientation()
        {
            return orientation;
        }

        public PDFOrientation setOrientation(PageOrientation pdfOrientation)
        {
            this.orientation = pdfOrientation;
            return this;
        }
    }

    public class PDFPageScaleOptions
    {
        private PDFFileOptions.Scaling scaling = PDFFileOptions.Scaling.NOSCALING;
        private boolean customScaleEnabled = false;
        private double customScaleX = Double.MIN_VALUE;
        private double customScaleY = Double.MIN_VALUE;

        public PDFFileOptions.Scaling getScaling()
        {
            return scaling;
        }

        public PDFPageScaleOptions setScaling(PDFFileOptions.Scaling scaling)
        {
            this.scaling = scaling;
            return this;
        }

        public double getCustomScaleX()
        {
            return customScaleX;
        }

        public double getCustomScaleY()
        {
            return customScaleY;
        }

        public PDFPageScaleOptions setCustomScaleX(double customScaleX)
        {
            this.customScaleX = customScaleX;
            return this;
        }

        public PDFPageScaleOptions setCustomScaleY(double customScaleY)
        {
            this.customScaleY = customScaleY;
            return this;
        }

        public boolean isCustomScaleEnabled()
        {
            return customScaleEnabled;
        }

        public PDFPageScaleOptions enableCustomScale(boolean customScaleEnabled)
        {
            this.customScaleEnabled = customScaleEnabled;
            return this;
        }

        public PDFPageScaleOptions reset()
        {
            this.customScaleEnabled = false;
            this.customScaleX = Double.MIN_VALUE;
            this.customScaleY = Double.MIN_VALUE;
            this.scaling = PDFFileOptions.Scaling.NOSCALING;
            return this;
        }
    }

    private String author = "";
    private String producer = "";
    private String creator = "";
    private String title = "";
    private String subject = "";
    private String keywords = "";
    private int JPEGQuality = defaultJPEGQuality;
    private boolean ASCII85Enabled = false;

    private PDFOrientation pdfOrientation = new PDFOrientation();
    private PDFPageScaleOptions pdfPageScale = new PDFPageScaleOptions();
    private PDFPaperSizeOptions paperSizeOptions = new PDFPaperSizeOptions();
    private PDFEncryption pdfEncryption = new PDFEncryption();

    public static final int defaultJPEGQuality = 75;

    public String getAuthor()
    {
        return author;
    }

    public String getProducer()
    {
        return producer;
    }

    public String getCreator()
    {
        return creator;
    }

    public String getTitle()
    {
        return title;
    }

    public String getSubject()
    {
        return subject;
    }

    public String getKeywords()
    {
        return keywords;
    }

    public int getJPEGQuality()
    {
        return JPEGQuality;
    }

    public boolean isASCII85Enabled()
    {
        return ASCII85Enabled;
    }

    public PDFOrientation getPDFOrientation()
    {
        return pdfOrientation;
    }

    public PDFPageScaleOptions getPDFPageScaleOptions()
    {
        return pdfPageScale;
    }

    public PDFPaperSizeOptions getPaperSizeOptions()
    {
        return paperSizeOptions;
    }

    public PDFEncryption getPDFEncryption()
    {
        return pdfEncryption;
    }

    public static int getDefaultjpegquality()
    {
        return defaultJPEGQuality;
    }

    public PDFOptions setAuthor(String author)
    {
        this.author = author;
        return this;
    }

    public PDFOptions setProducer(String producer)
    {
        this.producer = producer;
        return this;
    }

    public PDFOptions setCreator(String creator)
    {
        this.creator = creator;
        return this;
    }

    public PDFOptions setTitle(String title)
    {
        this.title = title;
        return this;
    }

    public PDFOptions setSubject(String subject)
    {
        this.subject = subject;
        return this;
    }

    public PDFOptions setKeywords(String keywords)
    {
        this.keywords = keywords;
        return this;
    }

    public PDFOptions setJPEGQuality(int jPEGQuality)
    {
        this.JPEGQuality = jPEGQuality;
        return this;
    }

    public PDFOptions enableASCII85(boolean aSCII85Enabled)
    {
        this.ASCII85Enabled = aSCII85Enabled;
        return this;
    }
}
