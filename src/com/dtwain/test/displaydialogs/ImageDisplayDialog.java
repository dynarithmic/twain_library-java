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
package com.dtwain.test.displaydialogs;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.imaging.Imaging;

import com.dynarithmic.twain.DTwainConstants;
import com.dynarithmic.twain.DTwainConstants.FileType;
import com.dynarithmic.twain.highlevel.TwainAcquisitionData;

public class ImageDisplayDialog extends JDialog {

    class ImagePanel extends JPanel {
        /** Image for the inner class
             */
            protected BufferedImage mPanelBufferedImage;

            /** Pnale to diaply the image
             */
            public ImagePanel() {
                super();
                // no op
            }

            /** Sets the bufferedimage into the class
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

            /** Paints the component.
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
    private ArrayList<TwainAcquisitionData> allAcquisitions=null;
    protected BufferedImage mBufferedImage;
    protected RenderedImage mRenderedImage;
    private int curAcquisition = 0;
    private int curPage = 0;
    private FileType imageType;
    private JButton prevButton=null;
    private JButton nextButton=null;
    private JLabel curPageLabel=null;
    private JLabel curTotalPagesLabel=null;
    private ImageDisplayDialog theDialog=null;
    private boolean exitOnClose=false;

    public boolean isExitOnClose() { return exitOnClose; }

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

    protected void displayImage()
    {
        if ( allAcquisitions == null || allAcquisitions.isEmpty() )
            return;
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        final TwainAcquisitionData AcqData = allAcquisitions.get(curAcquisition);

            // assume that we know how to handle single page file types through ImageIO
            try {
                if ( AcqData.getNumPages() > 0 )
                {
                    mBufferedImage = Imaging.getBufferedImage(AcqData.getImageData(curPage));
                    if ( mBufferedImage == null )
                    {
                        setTitle("Acquired Images " + getMemTypeString(imageType) + " -- No image decoder found --");
                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                        nextButton.setEnabled(false);
                        prevButton.setEnabled(false);
                        return;
                    }
                    // enable/disable next/prev buttons
                    final int nPages = AcqData.getNumPages();
                    prevButton.setEnabled(curPage > 0);
                    nextButton.setEnabled(curPage < (nPages-1) && nPages > 1 );

                    // set current page number
                    curPageLabel.setText("" + (curPage + 1));
                    curTotalPagesLabel.setText("" + AcqData.getNumPages());
                    reset();
                }
                else {
                    prevButton.setEnabled(false);
                    nextButton.setEnabled(false);
                    curPageLabel.setText("" + 0);
                    curTotalPagesLabel.setText("" + 0);
                    reset();
                }

                setCursor(Cursor.getPredefinedCursor(  Cursor.DEFAULT_CURSOR));
            }
            catch (final Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    /**
     * Create the dialog
     */
    private String getMemTypeString(final DTwainConstants.FileType memType)
    {
        switch (memType)
        {
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

    public ImageDisplayDialog(final List<TwainAcquisitionData> allAcq, final FileType memType, boolean exitOnClose )
    {
        super();
        imageType = memType;
        theDialog = this;
        this.exitOnClose = exitOnClose;
        getContentPane().setLayout(null);
        setResizable(false);
        allAcquisitions = (ArrayList<TwainAcquisitionData>) allAcq;
        setTitle("Acquired Images " + getMemTypeString(memType));
        setModal(true);

        setBounds(100, 100, 724, 722);

        this.addWindowListener(new WindowAdapter() {
              public void windowClosing(WindowEvent we) {
                  if ( theDialog.isExitOnClose() )
                      System.exit(0);
                    theDialog.setVisible(false);
              }
            });

        final JLabel acquisitionLabel = new JLabel();
        acquisitionLabel.setBounds(138, 658, 73, 15);
        acquisitionLabel.setText("Acquisition:");
        getContentPane().add(acquisitionLabel);

        final Integer [] itemnumbers = new Integer [allAcquisitions.size()];
        for (int i = 0; i < allAcquisitions.size(); ++i )
            itemnumbers[i] = i + 1;
        comboBox = new JComboBox(itemnumbers);
        comboBox.setBounds(209, 655, 63, 24);
        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final Integer sSelect = (Integer)comboBox.getSelectedItem();
                curAcquisition = sSelect.intValue()-1;
                curPage = 0;
                displayImage();
            }
        });
        getContentPane().add(comboBox);

        final JButton okButton = new JButton();
        okButton.setMnemonic(KeyEvent.VK_O);
        okButton.setBounds(626, 11, 85, 35);
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                if (theDialog.isExitOnClose())
                    System.exit(0);
                theDialog.setVisible(false);
                theDialog.dispose();
            }
        });
        okButton.setText("Ok");
        getContentPane().add(okButton);

        final JButton cancelButton = new JButton();
        cancelButton.setMnemonic(KeyEvent.VK_C);
        cancelButton.setBounds(626, 45, 85, 40);
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                if (theDialog.isExitOnClose())
                    System.exit(0);
                theDialog.setVisible(false);
                theDialog.dispose();
            }
        });
        cancelButton.setText("Cancel");
        getContentPane().add(cancelButton);

        prevButton = new JButton();
        prevButton.setMnemonic(KeyEvent.VK_P);
        prevButton.setBounds(331, 655, 78, 30);
        prevButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                curPage--;
                displayImage();
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
                displayImage();
            }
        });
        nextButton.setText("Next");
        getContentPane().add(nextButton);

        final JLabel pageLabel = new JLabel();
        pageLabel.setBounds(495, 656, 30, 30);
        pageLabel.setText("Page");
        getContentPane().add(pageLabel);

        curPageLabel =  new JLabel();
        curPageLabel.setBounds(532, 656, 27, 30);
        curPageLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        curPageLabel.setText("1");
        getContentPane().add(curPageLabel);

        final JLabel ofLabel = new JLabel();
        ofLabel.setBounds(562, 664, 19, 15);
        getContentPane().add(ofLabel);
        ofLabel.setText("of");

        curTotalPagesLabel = new JLabel();
        curTotalPagesLabel.setBounds(586, 656, 30, 30);
        curTotalPagesLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        getContentPane().add(curTotalPagesLabel);
        //

        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(5, 5, 612, 637);
        getContentPane().add(scrollPane);

        final ImagePanel panel_1 = new ImagePanel();
        imagePanel = panel_1;
        scrollPane.setViewportView(panel_1);

        setLocationRelativeTo(null);
        displayImage();
    }
}
