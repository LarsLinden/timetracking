package timetracking;

import com.phidgets.event.TagGainListener;
import com.phidgets.event.TagGainEvent;
import com.phidgets.RFIDPhidget;
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
    String fail = "<html><body><font size=\"60\"><span style=\"font-family:Arial\"><center>Keine<p>Verbindung!</center></span></font></body></html>";
    String connection = "<html><body><font size=\"60\"><span style=\"font-family:Arial\"><center>Übertragung<p>läuft...</center></span></font></body></html>";
    String welcome;
    
    public RFID_TGL(JLabel jLabelClock)
    {
        this.jLabelClock = jLabelClock;
    }

    public void tagGained(TagGainEvent tagGainEvent)
    {
        clockStop = true;
        
        
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
                jLabelClock.setText(fail);
            }
            else{
            DB.DBSelectName(tag);
            
            welcome = "<html><body><font size=\"35\"><span style=\"font-family:Arial;font-size:13px;\"><center>Willkommen<p>" + DB.name + "</center></span></font></body></html>";
            jLabelClock.setText(welcome);
            }
            timerStart();
        }
        else{
            if (dateCheck.trim().substring(0,10).equals(dateNow.format(date))){
                
                DB.DBUpdateEnde(tag, time);
                boolean network = Timetracking.ethernet();
                if (network == true){
                    jLabelClock.setText(fail);
                }
                else{
                DB.DBSelectName(tag);
                String bye = "<html><body><font size=\"35\"><span style=\"font-family:Arial\"><center>Schönen Feierabend<p>" + DB.name + "</center></span></font></body></hrml>";
                jLabelClock.setText(bye);
                }
                timerStart();
            }
            else{
                DB.DBInsertBegin(tag, time);
                boolean network = Timetracking.ethernet();
                if (network == true){
                    jLabelClock.setText(fail);
                }
                else{
                DB.DBSelectName(tag);
                welcome = "<html><body><font size=\"35\"><span style=\"font-family:Arial\"><center>Willkommen<p>" + DB.name + "</center></span></font></body></html>";
                jLabelClock.setText(welcome);
                }
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
