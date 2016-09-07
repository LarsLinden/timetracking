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
    
    private JFrame app;
    private JTextField devNr;
    private JTextField name;
    
    public RFID_AL(JFrame app,JTextField devNr,JTextField name)
    {
        this.app = app;
        this.devNr = devNr;
        this.name = name;
    }
    
    public void attached(AttachEvent ae)
    {
        try
        {
            RFIDPhidget myRFID = (RFIDPhidget)ae.getSource();
            devNr.setText(myRFID.getDeviceType());
            name.setText(myRFID.getDeviceName());
            myRFID.setAntennaOn(true);
        }
        catch (PhidgetException ex)
        {
            System.out.println("Fehler");
        }
    }
    
}
