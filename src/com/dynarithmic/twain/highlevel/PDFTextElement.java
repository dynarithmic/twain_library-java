package com.dynarithmic.twain.highlevel;

import com.dynarithmic.twain.DTwainConstants.TextPageDisplayOptions;
import com.dynarithmic.twain.DTwainConstants.TextRenderMode;

public class PDFTextElement
{
    String fontName;
    String text;
    int XPosition;
    int YPosition;
    double fontSize;
    double scaling;
    double charSpacing;
    double wordSpacing;
    int strokeWidth;
    RGBColor RGBColor = new RGBColor();
    TextRenderMode renderMode = TextRenderMode.FILL;
    TextPageDisplayOptions pageDisplayOptions = TextPageDisplayOptions.ALLPAGES;

    public PDFTextElement()
    {
        XPosition = 0;
        YPosition = 0;
        fontSize = 8.0;
        scaling = 100.0;
        fontName = "Helvetica";
        charSpacing = 0.0;
        wordSpacing = 0.0;
        strokeWidth = 2;
        text = "";
    }

    public String getText()
    {
        return this.text;
    }

    public String getFontName()
    {
        return fontName;
    }

    public PDFTextElement setFontName(String fontName)
    {
        this.fontName = fontName;
        return this;
    }

    public int getXPosition()
    {
        return XPosition;
    }

    public PDFTextElement setXPosition(int xPosition)
    {
        XPosition = xPosition;
        return this;
    }

    public int getYPosition()
    {
        return YPosition;
    }

    public PDFTextElement setYPosition(int yPosition)
    {
        YPosition = yPosition;
        return this;
    }

    public PDFTextElement setText(String text)
    {
        this.text = text;
        return this;
    }

    public double getFontSize()
    {
        return fontSize;
    }

    public PDFTextElement setFontSize(double fontSize)
    {
        this.fontSize = fontSize;
        return this;
    }

    public double getScaling()
    {
        return scaling;
    }
    public PDFTextElement setScaling(double scaling)
    {
        this.scaling = scaling;
        return this;
    }
    public double getCharSpacing()
    {
        return charSpacing;
    }
    public PDFTextElement setCharSpacing(double charSpacing)
    {
        this.charSpacing = charSpacing;
        return this;
    }
    public double getWordSpacing()
    {
        return wordSpacing;
    }
    public PDFTextElement setWordSpacing(double wordSpacing)
    {
        this.wordSpacing = wordSpacing;
        return this;
    }
    public int getStrokeWidth()
    {
        return strokeWidth;
    }
    public PDFTextElement setStrokeWidth(int strokeWidth)
    {
        this.strokeWidth = strokeWidth;
        return this;
    }
    public RGBColor getRGBColor()
    {
        return RGBColor;
    }
    public PDFTextElement setRGBColor(RGBColor rGBColor)
    {
        RGBColor = rGBColor;
        return this;
    }
    public TextRenderMode getRenderMode()
    {
        return renderMode;
    }

    private int getRenderModeAsInt()
    {
        return renderMode.value();
    }

    public PDFTextElement setRenderMode(TextRenderMode renderMode)
    {
        this.renderMode = renderMode;
        return this;
    }
    public TextPageDisplayOptions getPageDisplayOptions()
    {
        return pageDisplayOptions;
    }

    private int getPageDisplayOptionsAsInt()
    {
        return pageDisplayOptions.value();
    }

    public PDFTextElement setPageDisplayOptions(TextPageDisplayOptions pageDisplayOptions)
    {
        this.pageDisplayOptions = pageDisplayOptions;
        return this;
    }
}
