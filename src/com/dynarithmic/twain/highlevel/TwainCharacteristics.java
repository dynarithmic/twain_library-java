package com.dynarithmic.twain.highlevel;

public class TwainCharacteristics extends TwainStartupOptions
{
    private TwainSourceDialog twainDialog = new TwainSourceDialog();
    private boolean openSourcesOnSelectEnabled = true;

    public TwainCharacteristics()
    {
        super();
    }

    public TwainSourceDialog getTwainDialog()
    {
        return twainDialog;
    }

    public boolean isOpenSourcesOnSelectEnabled()
    {
        return this.openSourcesOnSelectEnabled;
    }

    public TwainCharacteristics setOpenSourcesOnSelect(boolean bSet)
    {
        this.openSourcesOnSelectEnabled = bSet;
        return this;
    }
}
