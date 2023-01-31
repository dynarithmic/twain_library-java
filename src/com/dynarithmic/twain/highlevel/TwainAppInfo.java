 /**
 *
 * @version
 */
package com.dynarithmic.twain.highlevel;

import com.dynarithmic.twain.lowlevel.TW_IDENTITY;
import com.dynarithmic.twain.lowlevel.TwainConstants.*;

public class TwainAppInfo
{
    private final TW_IDENTITY appIdentity = new TW_IDENTITY();
    private CAP_LANGUAGE language = CAP_LANGUAGE.TWLG_ENGLISH;

    public TwainAppInfo setVersionInfo( String version )
    {
        appIdentity.getVersion().setInfo(version);
        return this;
    }

    public String getVersionInfo()
    {
        return appIdentity.getVersion().getInfo().getValue();
    }

    public String getManufacturer()
    {
        return appIdentity.getManufacturer().getValue();
    }

    public TwainAppInfo setManufacturer(String manu)
    {
        appIdentity.getManufacturer().setValue(manu);
        return this;
    }

    public String getProductFamily()
    {
        return appIdentity.getProductFamily().getValue();
    }

    public TwainAppInfo setProductFamily(String family)
    {
        appIdentity.getProductFamily().setValue(family);
        return this;
    }

    public String getProductName()
    {
        return appIdentity.getProductName().getValue();
    }

    public TwainAppInfo setProductName(String name)
    {
        appIdentity.getProductName().setValue(name);
        return this;
    }

    public TwainAppInfo setLanguage(CAP_LANGUAGE language)
    {
        this.language = language;
        return this;
    }

    public CAP_LANGUAGE getLanguage()
    {
        return this.language;
    }

    public TwainAppInfo()
    {
        super();
        appIdentity.getVersion().setInfo("<?>");
        appIdentity.getManufacturer().setValue("<?>");
        appIdentity.getProductFamily().setValue("<?>");
        appIdentity.getProductName().setValue("<?>");
    }

    public TwainAppInfo(String ver, String manu, String prodFamily, String prodName)
    {
        super();
        appIdentity.getVersion().setInfo(ver);
        appIdentity.getManufacturer().setValue(manu);
        appIdentity.getProductFamily().setValue(prodFamily);
        appIdentity.getProductName().setValue(prodName);
    }
}
