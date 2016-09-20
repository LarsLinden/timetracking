package timetracking;

import com.phidgets.PhidgetException;
import com.phidgets.RFIDPhidget;
import com.phidgets.event.TagLossListener;
import com.phidgets.event.TagLossEvent;
/**
 *
 * @author LaLinden
 */
public class RFID_TLL implements TagLossListener {
    
    static public boolean clockGo;
    private RFIDPhidget rfid_reader;
    
    public RFID_TLL(RFIDPhidget rfid_reader)
    {
        this.rfid_reader = rfid_reader;
    }
    
    public void tagLost(TagLossEvent tagLossEvent)
    {
        clockGo = true;
        //LEDRedOff();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {System.out.println("Fehler Sleep" + ex);}
    }
    
            private void LEDRedOff() {                                        
        try
        {
            rfid_reader.setOutputState(1, false);
            
        }
        catch (PhidgetException ex)
        {
            System.out.println("LED " + ex);
        }
    } 
}
