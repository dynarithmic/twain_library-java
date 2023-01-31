package com.dynarithmic.twain.highlevel.acquirecharacteristics;

import com.dynarithmic.twain.lowlevel.TwainConstants.*;
public class LanguageOptions
{
    private CAP_LANGUAGE language = defaultLanguage;
    private boolean userLocaleEnabled = false;
    public static CAP_LANGUAGE defaultLanguage = CAP_LANGUAGE.TWLG_USA;

    public LanguageOptions setLanguage(CAP_LANGUAGE language)
    {
        this.language = language;
        return this;
    }

    public LanguageOptions enableUserLocale(boolean enabled)
    {
        this.userLocaleEnabled = enabled;
        return this;
    }

    public boolean isUserLocaleEnabled()
    {
        return this.userLocaleEnabled;
    }

    public CAP_LANGUAGE getLanguage()
    {
        return this.language;
    }

    static protected final int affectedCaps[] = {CAPS.CAP_LANGUAGE};

    static public final int [] getAffectedCaps()
    {
        return affectedCaps;
    }
}
