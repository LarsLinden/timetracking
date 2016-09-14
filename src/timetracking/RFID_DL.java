package timetracking;

import com.phidgets.RFIDPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.DetachListener;
import com.phidgets.event.DetachEvent;

import javax.swing.JFrame;
import javax.swing.JTextField;
/**
 *
 * @author LaLinden
 */
public class RFID_DL implements DetachListener {
    

    
    public RFID_DL()
    {

    }
    
    public void detached(DetachEvent ae)
    {
        try
        {
            RFIDPhidget myRFID = (RFIDPhidget)ae.getSource();

            myRFID.setAntennaOn(true);
        }
        catch (PhidgetException ex)
        {
            System.out.println("Fehler Detached" + ex);
        }
    }
    
}
