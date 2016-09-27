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
    public Timer timer3;
    public TimerTask task;
    public TimerTask task2;
    public TimerTask task3;
    public boolean redLED;
    public boolean greenLED;
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
                DB.DBInsertBegin(tag, time, dateNow.toString());
                String tagCheck = DB.DBSelectCheck(tag);
                
                if (tagCheck.equals(tag)){
                    DB.DBSelectName(tag);
                    welcome = "<html><body><font size=\"35\"><span style=\"font-family:Arial\"><center>Willkommen<p>" + DB.name + "</center></span></font></body></html>";
                    jLabelClock.setText(welcome);
                    greenLED = true;
                    LEDGreen();
                    timerStartGreenPiep();
                }
                else{
                    jLabelClock.setText("<html><body><font size=\"35\"><span style=\"font-family:Arial;font-size:13px;\"><center>Unbekannter Tag<p>" + tag + "</center></span></font></body></html>");
                    redLED = true;
                    LEDRedOnManual();
                    timerStartLED();
                }
            }
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
                greenLED = true;
                LEDGreen();
                timerStartGreenPiep();
                }
                timerStart();
            }
            else{
                DB.DBInsertBegin(tag, time, dateNow.toString());
                boolean network = Timetracking.ethernet();
                if (network == true){
                    jLabelClock.setText(fail);
                    LEDRedOn();
                }
                else{
                DB.DBSelectName(tag);
                welcome = "<html><body><font size=\"35\"><span style=\"font-family:Arial\"><center>Willkommen<p>" + DB.name + "</center></span></font></body></html>";
                jLabelClock.setText(welcome);
                greenLED = true;
                LEDGreen();
                timerStartGreenPiep();
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
                redLED = false;
                LEDRedOnManual();
                timer2.cancel();
                task2.cancel();
                task2=null;
                timer2=null;            
            }   
        };
        timer2.schedule(task2, 1000);
    }
    
        public void timerStartGreenPiep(){
        timer3 = new Timer();
        task3 = new TimerTask(){
        
            public void run(){
                greenLED = false;
                LEDGreen();
                timer3.cancel();
                task3.cancel();
                task3=null;
                timer3=null;            
            }   
        };
        timer3.schedule(task3, 500);
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
    
    private void LEDGreen() {
        try
        {
            //Wieder auf 0 setzten!!! Ansonsten kein Grpünes Licht und kein Signalton! Nur für die Entwicklungszeit umgestelt!
            rfid_reader.setOutputState(1, greenLED);
            
        }
        catch (PhidgetException ex)
        {
            System.out.println("LED " + ex);
        }
    }
}
