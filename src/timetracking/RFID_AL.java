package timetracking;

import com.phidgets.RFIDPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.AttachListener;
import com.phidgets.event.AttachEvent;

import javax.swing.JFrame;
import javax.swing.JTextField;
/**
 *
 * @author LaLinden
 */
public class RFID_AL implements AttachListener {
    

    
    public RFID_AL()
    {

    }
    
    public void attached(AttachEvent ae)
    {
        try
        {
            RFIDPhidget myRFID = (RFIDPhidget)ae.getSource();

            myRFID.setAntennaOn(true);
        }
        catch (PhidgetException ex)
        {
            System.out.println("Fehler Attach" + ex);
        }
    }
    
}
