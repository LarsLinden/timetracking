package timetracking;

import com.phidgets.event.TagGainListener;
import com.phidgets.event.TagGainEvent;

import javax.swing.JTextField;
/**
 *
 * @author LaLinden
 */
public class RFID_TGL implements TagGainListener {
    
    private JTextField tagTxt;
    
    public RFID_TGL(JTextField tagTxt)
    {
        this.tagTxt = tagTxt;
        
    }
    
    public void tagGained(TagGainEvent tagGainEvent)
    {
        tagTxt.setText(tagGainEvent.getValue());
    }
}
