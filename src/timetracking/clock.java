package timetracking;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JLabel;

/**
 *
 * @author LaLinden
 */
public class clock extends javax.swing.JFrame 
{
    public int timeRun;
    
    private JLabel jLabelClock;
    
    public clock(JLabel jLabelClock)
    {
        this.jLabelClock = jLabelClock;
                
        new Thread()
        {
            public void run()
            {
                while (timeRun == 0)
                {
                    Calendar cal = new GregorianCalendar();
                    
                    int hour = cal.get(Calendar.HOUR_OF_DAY);
                    int min = cal.get(Calendar.MINUTE);
                    int sec = cal.get(Calendar.SECOND);
                    
                    DecimalFormat fmt = new DecimalFormat("00");
                    
                    String time = fmt.format(hour) + ":" + fmt.format(min) + ":" + fmt.format(sec);

                    boolean DisplayTimeBool = Timetracking.getClockGo();
                    if (DisplayTimeBool == false){
                        jLabelClock.setText(time);
                    }
                }
            }                    
        }.start();   
    }
}
