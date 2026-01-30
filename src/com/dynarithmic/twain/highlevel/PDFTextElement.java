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
package com.dynarithmic.twain.highlevel;

import com.dynarithmic.twain.DTwainConstants.TextPageDisplayOptions;
import com.dynarithmic.twain.DTwainConstants.TextRenderMode;
import com.dynarithmic.twain.DTwainConstants.TextTransformOptions;

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
    double strokeWidth;
    double rotationAngle;
    double skewAngleX;
    double skewAngleY;
    double scalingX;
    double scalingY;
    
    RGBColor RGBColor = new RGBColor();
    TextRenderMode renderMode = TextRenderMode.FILL;
    TextTransformOptions textTransformOptions = TextTransformOptions.SCALE_ROTATE_SKEW;

    public PDFTextElement()
    {
        XPosition = 0;
        YPosition = 0;
        fontSize = 8.0;
        scaling = 100.0;
        fontName = "Helvetica";
        charSpacing = 0.0;
        wordSpacing = 0.0;
        strokeWidth = 2.0;
        text = "";
        rotationAngle = 0.0;
        skewAngleX = skewAngleY = 0.0;
        scalingX = scalingY = 1.0;
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
    public double getStrokeWidth()
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

    public double getRotationAngle() {
        return rotationAngle;
    }

    public PDFTextElement setRotationAngle(double rotationAngle) {
        this.rotationAngle = rotationAngle;
        return this;
    }

    public double getSkewAngleX() {
        return skewAngleX;
    }

    public PDFTextElement setSkewAngleX(double skewAngleX) {
        this.skewAngleX = skewAngleX;
        return this;
    }

    public double getSkewAngleY() {
        return skewAngleY;
    }

    public PDFTextElement setSkewAngleY(double skewAngleY) {
        this.skewAngleY = skewAngleY;
        return this;
    }

    public double getScalingX() {
        return scalingX;
    }

    public PDFTextElement setScalingX(double scalingX) {
        this.scalingX = scalingX;
        return this;
    }

    public double getScalingY() {
        return scalingY;
    }

    public PDFTextElement setScalingY(double scalingY) {
        this.scalingY = scalingY;
        return this;
    }

    public TextTransformOptions getTextTransform() {
        return textTransformOptions;
    }

    private int getTextTransformAsInt() {
        return textTransformOptions.value();
    }
    
    public PDFTextElement setTextTransform(TextTransformOptions textTransform) {
        this.textTransformOptions = textTransform;
        return this;
    }

    public PDFTextElement setStrokeWidth(double strokeWidth) {
        this.strokeWidth = strokeWidth;
        return this;
    }
}
