package com.dynarithmic.twain.highlevel;
import java.util.ArrayList;
import java.util.List;

public class TwainAcquisitionArray
{
    private List<TwainAcquisitionData> m_arrAcquisitions = new ArrayList<>();
    private int status;

    /**
     * @param allAcquisitions
     * A vector that on return, will be initialized to the page data that was acquired
     * for this acquisition.  This method need not be called by the DTWAIN Java program,
     * as this will be filled in automatically by the JNI native code.
     */
    public TwainAcquisitionArray(List<TwainAcquisitionData> allAcquisitions)
    {
        m_arrAcquisitions = allAcquisitions;
    }

    public List<TwainAcquisitionData> getAcquisitonArray()
    {
        return m_arrAcquisitions;
    }

    public TwainAcquisitionArray()
    {
        m_arrAcquisitions = new ArrayList<>();
        status = 0;
    }

    public void setAcquisitionArray(List<TwainAcquisitionData> allPages)
    {
        m_arrAcquisitions = allPages;
        status = 0;
    }

    public void addAcquisitionData(TwainAcquisitionData theData)
    {
        m_arrAcquisitions.add(theData);
    }
    /**
     * @return Number of pages acquired from TWAIN device during this acquisition
     */
    public int getNumAcquisitions()
    {
        return m_arrAcquisitions.size();
    }

    public TwainAcquisitionData get(int nWhichAcq)
    {
        if ( m_arrAcquisitions.isEmpty() )
            return null;
        if ( nWhichAcq < 0 || nWhichAcq >= m_arrAcquisitions.size() )
            return null;
        return m_arrAcquisitions.get(nWhichAcq);
    }

    public boolean anyImagesAcquired()
    {
        for (int i = 0; i < m_arrAcquisitions.size(); ++i)
        {
            if ( m_arrAcquisitions.get(i).getNumPages() > 0 )
                return true;
        }
        return false;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int theStatus)
    {
        status = theStatus;
    }
}
