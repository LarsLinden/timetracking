package timetracking;

import com.phidgets.event.TagGainListener;
import com.phidgets.event.TagGainEvent;
import java.awt.Font;
import java.util.*;
import java.text.*;
import javax.swing.JLabel;
/**
 *
 * @author LaLinden
 */
public class RFID_TGL implements TagGainListener {
    
    String name;
    String dateCheck = null;
    SimpleDateFormat dateNow = null;
    static public boolean clockStop;
    static public boolean ethernet;
    private JLabel jLabelClock;
    public Timer timer;
    public TimerTask task;
    public int fontSizeToUse = 60;
    String fail = "Keine Verbindung!";
    String welcome;
    
    public RFID_TGL(JLabel jLabelClock)
    {
        this.jLabelClock = jLabelClock;

    }
    

    
    public void tagGained(TagGainEvent tagGainEvent)
    {
        clockStop = true;
        
//        Font font = new Font("DialogInput",Font.PLAIN | Font.BOLD, 1);
//        Font newFont = font.deriveFont(10F);
//        jLabelClock.setFont(newFont); 

Font labelFont = jLabelClock.getFont();
String labelText = jLabelClock.getText();

int stringWidth = jLabelClock.getFontMetrics(labelFont).stringWidth(labelText);
int componentWidth = jLabelClock.getWidth();

// Find out how much the font can grow in width.
double widthRatio = (double)componentWidth / (double)stringWidth;

int newFontSize = (int)(labelFont.getSize() * widthRatio);
int componentHeight = jLabelClock.getHeight();

// Pick a new font size so it will not be larger than the height of label.
int fontSizeToUse = Math.min(newFontSize, componentHeight);

// Set the label's font size to the newly determined size.
jLabelClock.setFont(new Font(labelFont.getName(), Font.PLAIN, fontSizeToUse));

        String connection = "Übertragung öäuft...";
        fontSize(connection);
        jLabelClock.setText(connection);
        Date date = new Date();
        SimpleDateFormat dateBegin = new SimpleDateFormat ("YYYY-MM-dd HH:mm:ss");
        
        String tag = "'"+ tagGainEvent.getValue() + "'";
        String time = "'" + dateBegin.format(date) + "'";
        try{
        DBVerbindung DB = new DBVerbindung();
        
        DB.DBSelect(tag);
        
        dateCheck = DB.dateBegin; 
        dateNow = new SimpleDateFormat ("YYYY-MM-dd");
        
        if (dateCheck == null){
            DB.DBInsertBegin(tag, time);
            boolean network = Timetracking.ethernet();
            if (network == true){
                fontSize(fail);
                jLabelClock.setText(fail);
            }
            DB.DBSelectName(tag);
            
            welcome = "Willkommen" + DB.name;
            fontSize(welcome);
            jLabelClock.setText(welcome);
            timerStart();
        }
        else{
            if (dateCheck.trim().substring(0,10).equals(dateNow.format(date))){
                
                DB.DBUpdateEnde(tag, time);
                boolean network = Timetracking.ethernet();
                if (network == true){
                    fontSize(fail);
                    jLabelClock.setText(fail);
                }
                DB.DBSelectName(tag);
                jLabelClock.setText("Schönen Feierabend" + DB.name);
                timerStart();
            }
            else{
                DB.DBInsertBegin(tag, time);
                boolean network = Timetracking.ethernet();
                if (network == true){
                    fontSize(fail);
                    jLabelClock.setText(fail);
                }
                DB.DBSelectName(tag);
                welcome = "Willkommen" + DB.name;
                fontSize(welcome);
                jLabelClock.setText(welcome);
                timerStart();
            }
        }

        }
        catch (Exception e){System.out.println("Fatal Error");}
    }
    
    public void timerStart(){
        timer = new Timer();
        task = new TimerTask(){
        
            public void run(){
                clockStop = false; 
                timer.cancel();
                task.cancel();
                task=null;
                timer=null;            
            }   
        };
        timer.schedule(task, 2000);
    }
    
    public void fontSize(String text){
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
