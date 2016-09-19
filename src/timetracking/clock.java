package timetracking;

import java.awt.Font;
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
    public int fontSizeToUse = 60;
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
                        
                        fontSize(time);
                        
                        jLabelClock.setText(time);
                        
                    }
                }
            }                    
        }.start();   
    }
    
    public void fontSize(String text){
        //Font clockFont = new Font("DialogInput",Font.BOLD, 60);
        //Font ClockFont = clockFont.deriveFont(fontSizeToUse);
        //jLabelClock.setFont(ClockFont); 
        
        Font labelFont = jLabelClock.getFont();
        String labelText = jLabelClock.getText();

        int stringWidth = jLabelClock.getFontMetrics(labelFont).stringWidth(labelText);
        int componentWidth = jLabelClock.getWidth();

        // Find out how much the font can grow in width.
        double widthRatio = (double)componentWidth / (double)stringWidth;

        int newFontSize = (int)(labelFont.getSize() * widthRatio);
        int componentHeight = jLabelClock.getHeight();

        // Pick a new font size so it will not be larger than the height of label.
        fontSizeToUse = Math.min(newFontSize, componentHeight);

        // Set the label's font size to the newly determined size.
        jLabelClock.setFont(new Font(labelFont.getName(), Font.BOLD, fontSizeToUse));
    }
}
