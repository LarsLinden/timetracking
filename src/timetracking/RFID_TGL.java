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
    
    public RFID_TGL(JLabel jLabelClock)
    {
        this.jLabelClock = jLabelClock;

    }
    

    
    public void tagGained(TagGainEvent tagGainEvent)
    {
        clockStop = true;
        Font font = new Font("DialogInput",Font.PLAIN | Font.BOLD, 1);
        Font newFont = font.deriveFont(1F);
        jLabelClock.setFont(newFont);
        //jLabelClock.setFont (jLabelClock.getFont ().deriveFont (10.0f));  
        jLabelClock.setText("Übertragung läuft...");
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
                jLabelClock.setText("Keine Verbindung!");
            }
            DB.DBSelectName(tag);
            jLabelClock.setText("Willkomen" + DB.name);
            timerStart();
        }
        else{
            if (dateCheck.trim().substring(0,10).equals(dateNow.format(date))){
                
                DB.DBUpdateEnde(tag, time);
                boolean network = Timetracking.ethernet();
                if (network == true){
                    jLabelClock.setText("Keine Verbindung!");
                }
                DB.DBSelectName(tag);
                jLabelClock.setText("Schönen Feierabend" + DB.name);
                timerStart();
            }
            else{
                DB.DBInsertBegin(tag, time);
                boolean network = Timetracking.ethernet();
                if (network == true){
                    jLabelClock.setText("Keine Verbindung!");
                }
                DB.DBSelectName(tag);
                jLabelClock.setText("Willkomen" + DB.name);
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

}
