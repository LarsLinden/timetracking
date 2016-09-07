package timetracking;

import com.phidgets.event.TagLossListener;
import com.phidgets.event.TagLossEvent;

import javax.swing.JTextField;
/**
 *
 * @author LaLinden
 */
public class RFID_TLL implements TagLossListener {
    
    private JTextField tagTxt;
    
    public RFID_TLL(JTextField tagTxt)
    {
        this.tagTxt = tagTxt;
    }
    
    public void tagLost(TagLossEvent tagLossEvent)
    {
        tagTxt.setText("Bitte RFID scannen");
    }
    
}
