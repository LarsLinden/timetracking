package timetracking;

import com.phidgets.event.TagLossListener;
import com.phidgets.event.TagLossEvent;
/**
 *
 * @author LaLinden
 */
public class RFID_TLL implements TagLossListener {
    
    static public boolean clockGo;    
    
    public RFID_TLL()
    {       
    }
    
    public void tagLost(TagLossEvent tagLossEvent)
    {
        clockGo = true;
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {System.out.println("Fehler Sleep" + ex);}
    }
    
}
