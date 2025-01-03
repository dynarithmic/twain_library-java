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
package com.dtwain.demos.fulldemo;
import com.dynarithmic.twain.DTwainConstants.FileType;
import com.dynarithmic.twain.highlevel.TwainAcquisitionArray;
import com.dynarithmic.twain.highlevel.TwainAcquisitionData;
import com.dynarithmic.twain.highlevel.TwainImageData;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.imaging.Imaging;

/* This class will display image data in a dialog */
public class DTwainImageDisplayDialog extends JDialog {

    class ImagePanel extends JPanel {

        /**
         * Image for the inner class
         */
        protected BufferedImage mPanelBufferedImage;

        /**
         * Panel to display the image
         */
        public ImagePanel() {
            super();
            // no op
        }

        /**
         * Sets the buffered image into the class
         *
         * @param bi BufferedImage
         */
        public void setBufferedImage(final BufferedImage bi) {
            if (bi == null) {
                return;
            }
            mPanelBufferedImage = bi;
            final Dimension d = new Dimension(mPanelBufferedImage.getWidth(this),
                    mPanelBufferedImage.getHeight(this));
            setPreferredSize(d);
            revalidate();
            repaint();
        }

        /**
         * Paints the component.
         *
         * @param g Graphics object used for the painting
         */
        public void paintComponent(final Graphics g) {
            super.paintComponent(g);
            final Dimension d = getSize();
            g.setColor(getBackground());
            g.fillRect(0, 0, d.width, d.height);
            if (mPanelBufferedImage != null) {
                g.drawImage(mPanelBufferedImage, 0, 0, this);
            }
        }
    }
    protected ImagePanel imagePanel;

    private JComboBox comboBox;
    private TwainAcquisitionArray allAcquisitions = null;
    private TwainImageData mOneImageData = null;
    protected BufferedImage mBufferedImage;
    protected RenderedImage mRenderedImage;
    private int curAcquisition = 0;
    private int curPage = 0;
    private FileType imageType;
    private JButton prevButton = null;
    private JButton nextButton = null;
    private JLabel curPageLabel = null;
    private JLabel curTotalPagesLabel = null;
    private JLabel pageLabel = null;
    private JLabel ofLabel = null;
    private DTwainImageDisplayDialog theDialog = null;
    private JLabel acquisitionLabel = null;
    private JScrollPane scrollPane = null;
    private boolean okPressed = false;
    /**
     * Launch the application
     *
     * @param args
     */

    protected void reset() {
        if (mBufferedImage != null) {
            imagePanel.setBufferedImage(mBufferedImage);
        }
    }

    public static GraphicsConfiguration getDefaultConfiguration() {
        final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final GraphicsDevice gd = ge.getDefaultScreenDevice();
        return gd.getDefaultConfiguration();
    }

    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    protected void retrieveImage(TwainImageData imgData)
    {
        try
        {
            // retrieve the image data as a byte array
            final byte[] image = imgData.getImageData();
            mBufferedImage = Imaging.getBufferedImage(imgData.getImageData());
            if (mBufferedImage == null)
            {
                setTitle("Acquired Images " + getMemTypeString(imageType) + " -- No image decoder found --");
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                nextButton.setEnabled(false);
                prevButton.setEnabled(false);
                return;
            }
            Image scaledImage = mBufferedImage.getScaledInstance(scrollPane.getWidth(), scrollPane.getHeight(), Image.SCALE_SMOOTH);
            mBufferedImage = toBufferedImage(scaledImage);
            // enable/disable next/prev buttons
            reset();
        }
        catch (final Exception e) {
           System.out.println(e.getMessage());
        }
    }

    protected void displayImageHelper()
    {
        if ( mOneImageData != null )
            displayOneImage();
        else
            displayImage();
    }

    public boolean isOkPressed() { return okPressed; }

    protected void displayOneImage()
    {
        comboBox.setVisible(false);
        prevButton.setVisible(false);
        nextButton.setVisible(false);
        curPageLabel.setVisible(false);
        curTotalPagesLabel.setVisible(false);
        pageLabel.setVisible(false);
        ofLabel.setVisible(false);
        acquisitionLabel.setVisible(false);

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        retrieveImage(mOneImageData);
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    protected void displayImage()
    {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        // this gets the array of pages that were acquired
        final TwainAcquisitionData AcqData = allAcquisitions.get(curAcquisition);

        // assume that we know how to handle single page file types through ImageIO
        try
        {
            if (AcqData.getNumPages() > 0)
            {
                retrieveImage(AcqData.getImageDataObject(curPage));
                // enable/disable next/prev buttons
                final int nPages = AcqData.getNumPages();
                prevButton.setEnabled(curPage > 0);
                nextButton.setEnabled(curPage < (nPages - 1) && nPages > 1);

                // set current page number
                curPageLabel.setText("" + (curPage + 1));
                curTotalPagesLabel.setText("" + AcqData.getNumPages());
            }
            else
            {
                prevButton.setEnabled(false);
                nextButton.setEnabled(false);
                curPageLabel.setText("" + 0);
                curTotalPagesLabel.setText("" + 0);
                reset();
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } catch (final Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Create the dialog
     */
    private String getMemTypeString(final FileType imageType2) {
        switch (imageType2) {
            case JPEG:
                return "(JPEG Image)";
            case PNG:
                return "(PNG Image)";
            case GIF:
                return "(GIF Image)";
            case BMP:
                return "(BMP Image)";
            case TIFFNONE:
                return "(Uncompressed TIFF Image)";
            case TIFFG3:
                return "(Group 3 Compressed TIFF Image)";
            case TIFFG4:
                return "(Group 4 Compressed TIFF Image)";
        }
        return "(Unknown image type)";
    }

    protected void setControls(FileType bmp)
    {
        imageType = bmp;
        theDialog = this;
        getContentPane().setLayout(null);
        setResizable(false);
        setTitle("Acquired Images " + getMemTypeString(bmp));
        setModal(true);

        setBounds(100, 100, 724, 722);

        acquisitionLabel = new JLabel();
        acquisitionLabel.setBounds(138, 658, 73, 15);
        acquisitionLabel.setText("Acquisition:");
        getContentPane().add(acquisitionLabel);


        if ( mOneImageData == null )
        {
            final Integer[] itemnumbers = new Integer[allAcquisitions.getNumAcquisitions()];
            for (int i = 0; i < allAcquisitions.getNumAcquisitions(); ++i) {
                itemnumbers[i] = new Integer(i + 1);
            }
            comboBox = new JComboBox(itemnumbers);
            theDialog.setTitle("Image Displayer");
        }
        else
        {
            comboBox = new JComboBox();
            theDialog.setTitle("Keep or Discard Image?");
        }
        comboBox.setBounds(209, 655, 63, 24);
        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final Integer sSelect = (Integer) comboBox.getSelectedItem();
                curAcquisition = sSelect.intValue() - 1;
                curPage = 0;
                displayImageHelper();
            }
        });
        getContentPane().add(comboBox);

        final JButton okButton = new JButton();
        okButton.setMnemonic(KeyEvent.VK_O);
        okButton.setBounds(626, 11, 85, 35);
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                theDialog.setVisible(false);
                okPressed = true;
            }
        });
        okButton.setText("Ok");
        if ( mOneImageData != null )
            okButton.setText("Keep Page");
        getContentPane().add(okButton);

        final JButton cancelButton = new JButton();
        cancelButton.setMnemonic(KeyEvent.VK_C);
        cancelButton.setBounds(626, 45, 85, 40);
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                theDialog.setVisible(false);

            }
        });
        cancelButton.setText("Cancel");
        if ( mOneImageData != null )
            cancelButton.setText("Discard Page");
        getContentPane().add(cancelButton);

        prevButton = new JButton();
        prevButton.setMnemonic(KeyEvent.VK_P);
        prevButton.setBounds(331, 655, 78, 30);
        prevButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                curPage--;
                displayImageHelper();
            }
        });
        prevButton.setText("Prev");
        getContentPane().add(prevButton);

        nextButton = new JButton();
        nextButton.setMnemonic(KeyEvent.VK_N);
        nextButton.setBounds(413, 655, 78, 30);
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                curPage++;
                displayImageHelper();
            }
        });
        nextButton.setText("Next");
        getContentPane().add(nextButton);

        pageLabel = new JLabel();
        pageLabel.setBounds(495, 656, 30, 30);
        pageLabel.setText("Page");
        getContentPane().add(pageLabel);

        curPageLabel = new JLabel();
        curPageLabel.setBounds(532, 656, 27, 30);
        curPageLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        curPageLabel.setText("1");
        getContentPane().add(curPageLabel);

        ofLabel = new JLabel();
        ofLabel.setBounds(562, 664, 19, 15);
        getContentPane().add(ofLabel);
        ofLabel.setText("of");

        curTotalPagesLabel = new JLabel();
        curTotalPagesLabel.setBounds(586, 656, 30, 30);
        curTotalPagesLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        getContentPane().add(curTotalPagesLabel);
        //

        scrollPane = new JScrollPane();
        scrollPane.setBounds(5, 5, 612, 637);
        getContentPane().add(scrollPane);

        final ImagePanel panel_1 = new ImagePanel();
        imagePanel = panel_1;
        scrollPane.setViewportView(panel_1);

        setLocationRelativeTo(null);
        displayImageHelper();
    }

    public DTwainImageDisplayDialog(final TwainImageData iData, final FileType bmp)
    {
        super();
        mOneImageData = iData;
        setControls(bmp);
    }

    public DTwainImageDisplayDialog(final TwainAcquisitionArray allAcq, final FileType memType)
    {
        super();
        allAcquisitions = allAcq;
        setControls(memType);
    }
}
