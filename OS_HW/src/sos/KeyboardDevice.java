package sos;

/**
 * This class simulates a simple, non-sharable read only device (keyboard).  
 *
 * @see Sim
 * @see CPU
 * @see SOS
 * @see Device
 */
public class KeyboardDevice implements Device
{
    private int m_id = -999;           // the OS assigned device ID

    /**
     * getId
     *
     * @return the device id of this device
     */
    public int getId()
    {
        return m_id;
    }
    
    /**
     * setId
     *
     * sets the device id of this device
     *
     * @param id the new id
     */
    public void setId(int id)
    {
        m_id = id;
    }
    
    /**
     * isSharable
     *
     * This device cannot be used by multiple processes simultaneously
     *
     * @return false
     */
    public boolean isSharable()
    {
        return false;
    }
    
    /**
     * isAvailable
     *
     * this device is available if no requests are currently being processed
     */
    public boolean isAvailable()
    {
        return true;
    }
    
    /**
     * isReadable
     *
     * @return whether this device can be read from (true/false)
     */
    public boolean isReadable()
    {
        return true;
    }
    
    /**
     * isWriteable
     *
     * @return whether this device can be written to (true/false)
     */
    public boolean isWriteable()
    {
        return false;
    }
     
    /**
     * read
     *
     * this method returns a generated number from the keyboard that
     * simulates a user pressing a key on the keyboard
     *
     * @return the value that was "read" from the keyboard
     */
    public int read(int addr)
    {
       return (int) (Math.random() * 1000); 
    }//read
    
    /**
     * write
     *
     * not implemented for this device
     */
    public void write(int addr /*not used*/, int data /*not used*/)
    {
        return;
    }
};//class KeyboardDevice
