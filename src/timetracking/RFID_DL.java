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
    
    private JFrame app;
    private JTextField devNr;
    private JTextField name;
    
    public RFID_DL(JFrame app,JTextField devNr,JTextField name)
    {
        this.app = app;
        this.devNr = devNr;
        this.name = name;
    }
    
    public void detached(DetachEvent ae)
    {
        try
        {
            RFIDPhidget myRFID = (RFIDPhidget)ae.getSource();
            devNr.setText("Device not found");
            name.setText("Device not found");
            myRFID.setAntennaOn(true);
        }
        catch (PhidgetException ex)
        {
            System.out.println("Fehler");
        }
    }
    
}
