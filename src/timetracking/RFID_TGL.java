package timetracking;

import com.phidgets.PhidgetException;
import com.phidgets.event.TagGainListener;
import com.phidgets.event.TagGainEvent;
import com.phidgets.RFIDPhidget;
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
    private RFIDPhidget rfid_reader;
    public Timer timer;
    public Timer timer2;
    public TimerTask task;
    public TimerTask task2;
    public boolean redLED;
    String fail = "<html><body><font size=\"80\"><span style=\"font-family:Arial\"><center>Keine<p>Verbindung!</center></span></font></body></html>";
    String connection = "<html><body><font size=\"80\"><span style=\"font-family:Arial\"><center>Übertragung<p>läuft...</center></span></font></body></html>";
    String welcome;
    
    public RFID_TGL(JLabel jLabelClock, RFIDPhidget rfid_reader)
    {
        this.jLabelClock = jLabelClock;
        this.rfid_reader = rfid_reader;
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
        
        LEDRedOn();
        
        if (dateCheck == null){          
            
            boolean network = Timetracking.ethernet();
            if (network == true){
                jLabelClock.setText(fail);
                LEDRedOn();
            }
            else{
                jLabelClock.setText("<html><body><font size=\"35\"><span style=\"font-family:Arial;font-size:13px;\"><center>Unbekannter Tag<p>" + tag + "</center></span></font></body></html>");
                timerStartLED();
            }
            redLED = false;
            LEDRedOnManual();
            timerStart();
        }
        else{
            if (dateCheck.trim().substring(0,10).equals(dateNow.format(date))){
                
                DB.DBUpdateEnde(tag, time);
                boolean network = Timetracking.ethernet();
                if (network == true){
                    jLabelClock.setText(fail);
                    LEDRedOn();
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
                    LEDRedOn();
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
    
    public void timerStartLED(){
        timer2 = new Timer();
        task2 = new TimerTask(){
        
            public void run(){
                redLED = true;
                LEDRedOnManual();
                timer2.cancel();
                task2.cancel();
                task2=null;
                timer2=null;            
            }   
        };
        timer2.schedule(task2, 1000);
    }
    
    private void LEDRedOn() {                                        
        try
        {
            rfid_reader.setOutputState(1, Timetracking.ethernet());
            
        }
        catch (PhidgetException ex)
        {
            System.out.println("LED " + ex);
        }
    }
        
    private void LEDRedOnManual() {
        try
        {
            rfid_reader.setOutputState(1, redLED);
            
        }
        catch (PhidgetException ex)
        {
            System.out.println("LED " + ex);
        }
    } 
}
