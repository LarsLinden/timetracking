package timetracking;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.*;


class DBVerbindung{
    
    static boolean ethernet;
    Connection conn;  
    String dateBegin;
    String last_name;
    String first_name;
    String gender;
    String name;
    String tagCheck;
    
    boolean WelcomeOrGo;
    
    public DBVerbindung(){
        
        try{  
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setUser("larsremote");
            dataSource.setPassword("Citrix170890!");
            dataSource.setServerName("cherryfactory.de");
            dataSource.setDatabaseName("timetracking");
            conn = dataSource.getConnection();
            ethernet = false;
        }catch(Exception e){
            ethernet = true;
            System.out.println("SQL connection Fehler");
        }  
    }  
    
    public void DBInsertBegin(String tag, String time, String date){
        
        
        try(Statement stmt = (Statement) conn.createStatement()) {          
            stmt.executeUpdate("INSERT INTO worktime(tag, begin, date) VALUES (" + tag + ", " + time + ", " + date + ")");            
        }catch(Exception e){System.out.println("SQL Insert Fehler" + e);}
        WelcomeOrGo = true;
    }
    
    public void DBUpdateEnde(String tag, String time, String date){
        
        try(Statement stmt = (Statement) conn.createStatement()) {          
            stmt.executeUpdate("UPDATE worktime set ende = (" + time + ") WHERE (tag  = " + tag + ") AND (date = " + date + ")");            
        }catch(Exception e){System.out.println("SQL UPDATE Fehler" + e);}
        WelcomeOrGo = false;
    }
    
    public String DBSelect (String tag){
        
        try{
            Statement stmt = (Statement) conn.createStatement();          
            ResultSet rs = stmt.executeQuery("SELECT begin FROM worktime WHERE id=(SELECT max(id) FROM worktime WHERE tag = " + tag + ")");
                              
            while (rs.next())
            {   
                dateBegin = rs.getString(1);
            }
            
        }catch(Exception e){System.out.println("SQL SELECT Fehler" + e);}
        
        return dateBegin;
    }
    
    public String DBSelectName (String tag){
        
        try{
            Statement stmt = (Statement) conn.createStatement();          
            ResultSet rs = stmt.executeQuery("SELECT first_name, last_name, gender FROM personal WHERE tag = " + tag );
                             
            while (rs.next())
                {   
                    first_name = rs.getString(1);
                    last_name = rs.getString(2);
                    gender = rs.getString(3);
                    
                    name = " " + gender + " " + first_name + " " + last_name;
                }
            
        }catch(Exception e){System.out.println("SQL SELECT name Fehler" + e);}
        
        return name;
    }
    
    public String DBSelectCheck (String tag){
        
        try{
            Statement stmt = (Statement) conn.createStatement();          
            ResultSet rs = stmt.executeQuery("SELECT tag FROM personal WHERE tag = " + tag );
                             
            while (rs.next())
                {   
                    tagCheck = rs.getString(1);
                }
            
        }catch(Exception e){System.out.println("SQL SELECT check Fehler" + e);}
        
        return tagCheck;
    }

}
