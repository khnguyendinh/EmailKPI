/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mtatrace;

import com.viettel.common.bo.LogEmailMTA;
import com.viettel.common.util.ActionThread;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import static mtatrace.LogEmailProcess.formatter;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author thanhbv4
 */
public class LogMTAProcess extends ActionThread {

    

    private LinkedBlockingQueue<LogEmailMTA> queueLogMTA;
    private String host = "zimbra.viettel.com.vn";

//    private String folderDirectory = "D:\\sourcecode\\MtaTrace\\0\\";
    private String filePathMailLog = "F:\\SOURCE CODE\\khoand\\DOCUMENT\\ReadLog\\mail\\m1\\logM1.txt";
//    private String folderDirectory = "/home/app/msg/";
//    private String errorDirectory = "D:\\sourcecode\\MtaTrace\\error\\";
    private String errorDirectory = "F:\\SOURCE CODE\\khoand\\CODE\\LogEmail\\SAVE DB\\log\\error\\";
//    private String errorDirectory = "/home/app/MtaTrace/error/";
    private static final String SEND_EMAIL = "from=<";
    private static final String RECIVE_EMAIL = "to=<";
    private static final String FWD_EMAIL = "fwd from";
    private static final String DELIVERY_OK = "delivery ok";
    private static final String QUEUED_AS = "queued as";
    File dirDes = new File(errorDirectory);
    private static Logger logger;
    private long lastKnownPosition = 0;
    //10%
    private int percentLogRead = 100;
    // number log read in each 100 logs
    private int numberLogRead = 0;
    private int NUMBER_LINE_READ_ONE_TIME_MTA = 0;
    private long timeStart;
    private int SLEEP_TIME = 0;
    private Date lastTime;
    //gia tri date Cao nhat moi lan chay
    private Date maxTime;
    private long timeRun = 0;
         private String config = "../etc/config.properties";
         private String configLog4j = "../etc/log4j.conf";
//    private String config = "F:\\SOURCE CODE\\khoand\\CODE\\LogEmail\\CODE\\etc\\config.properties";
//     private String configLog4j = "F:\\SOURCE CODE\\khoand\\CODE\\LogEmail\\SAVE DB\\MtaTrace\\etc\\log4j.conf";

    public LogMTAProcess(String name, LinkedBlockingQueue<LogEmailMTA> queueMsg) {
        this.setName(name);
        this.queueLogMTA = queueMsg;
        this.logger = Logger.getLogger("mtalog");
//        PropertyConfigurator.configure("D:\\sourcecode\\MtaTrace\\MtaTrace\\etc\\log4j.conf");
//        PropertyConfigurator.configure("F:\\SOURCE CODE\\khoand\\CODE\\LogEmail\\SAVE DB\\MtaTrace\\etc\\log4j.conf");
//        PropertyConfigurator.configure("/home/app/MtaTrace/etc/log4j.conf");
        PropertyConfigurator.configure(configLog4j);
        logger.info("MTA Log Process Start");
        System.out.println("MTA Log Process Start");
        numberLogRead = percentLogRead;
        timeStart = System.currentTimeMillis();
    }

    private void getConfig() {
        Properties properties = new Properties();
        FileInputStream fileInputStream = null;
        logger.info("get Config");
        System.out.println("get config MTA");
        try {
            fileInputStream = new FileInputStream(config);
            properties.load(fileInputStream);
            this.filePathMailLog = properties.getProperty("folderDirectory_MTA");
            this.errorDirectory = properties.getProperty("errorDirectory");
            this.numberLogRead = Integer.parseInt(properties.getProperty("percentLogRead_MTA"));
            this.NUMBER_LINE_READ_ONE_TIME_MTA = Integer.parseInt(properties.getProperty("NUMBER_LINE_READ_ONE_TIME_MTA"));
            this.SLEEP_TIME = Integer.parseInt(properties.getProperty("SleepTime_MTA"));
            logger.info("NUMBER_LINE_READ_ONE_TIME_MTA "+NUMBER_LINE_READ_ONE_TIME_MTA);
            logger.info("folderDirectory_MTA "+filePathMailLog);
            logger.info("SLEEP_TIME "+SLEEP_TIME);
        } catch (IOException ex) {
            logger.info(ex.getMessage());

        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException ex) {
                    logger.info(ex);

                }
            }
        }

    }

    private void readFileMTALog() throws IOException, FileNotFoundException {
        try {
             File file = new File(filePathMailLog);
            String sCurrentLine = "";
            int counter = 0;
            ReversedLinesFileReader object = new ReversedLinesFileReader(file);
            long l = file.length();
            System.out.println("lenght "+l+" lastKnownPosition "+lastKnownPosition);
            if (l > this.lastKnownPosition) {
                this.lastKnownPosition = l;

                while (!(sCurrentLine = object.readLine()).isEmpty() && counter < NUMBER_LINE_READ_ONE_TIME_MTA && checkTimeIsAfterLasTime(sCurrentLine,counter)) {
                    counter++;
                    System.out.println("counter MTA " + counter);
                    LogEmailMTA logMTA = null;
                    switch (getTypeLine(sCurrentLine)) {
                        case 0:
//                            logMTA = getInforSendEmail(sCurrentLine);
                            logMTA = getInforReceiveEmail(sCurrentLine);
                            break;
//                        case 1:
//                            logMTA = getInforReceiveEmail(sCurrentLine);
//                            break;
//                        case 2:
//                            logMTA = getInforFWDEmail(sCurrentLine);
//                            break;

                        default:
                            break;

                    }
                    if (logMTA != null) {
                        logMTA.setType_log("MTA");
//                    if(queueLogMTA.size() > 1950){
//                        queueLogMTA.clear();
//                    }
                        queueLogMTA.add(logMTA);
                        System.out.println("Add queue MTA");
                    }
                }
            }else{
                System.out.println("No Have any new Line ===");
                logger.info("No Have any new Line ===");
            }
            lastTime = maxTime;
        } catch (FileNotFoundException ex) {
            this.logger.error((Object) ("Error : " + ex.getMessage()));
        } catch (IOException ex) {
            ex.printStackTrace();
            this.logger.error((Object) ("Error : " + ex.getMessage()));
        }

    }

    @Override
    protected void action() throws Exception {
        getConfig();
        readFileMTALog();
        timeRun++;
    }

    @Override
    protected void onExecuting() throws Exception {

    }

    @Override
    protected void onKilling() {

    }

    @Override
    protected void onException(Exception e) {

    }

    @Override
    protected long sleeptime() throws Exception {

        return SLEEP_TIME*60*1000;
    }


    private static LogEmailMTA getInforReceiveEmail(String sCurrentLine) {
        
        LogEmailMTA logMTA = null;
        String time = getDateTime(sCurrentLine);
        
        sCurrentLine = sCurrentLine.substring(21);
        
        String inforAction = sCurrentLine.substring(0, sCurrentLine.indexOf("to=<"));
        String relay = StringUtils.substringBetween(sCurrentLine, "relay=", ",");
        String delay = StringUtils.substringBetween(sCurrentLine, "delay=", ",");
        float numDelay = 0;
        if(delay != null){
            delay.replace(" ", "");
            try {
                numDelay = Float.parseFloat(delay);

            } catch (Exception e) {
            }
        }
        String delays = StringUtils.substringBetween(sCurrentLine, "delays=", ",");
        String dsn = StringUtils.substringBetween(sCurrentLine, "dsn=", ",");
        String nameEmail = StringUtils.substringBetween(sCurrentLine, "to=<", ">,");
        String status = StringUtils.substringBetween(sCurrentLine, "status=", " ");
         //sub string start
        if(inforAction  != null && inforAction.length() > 199){
            inforAction = sCurrentLine.substring(0, 199);
        }
        if(relay  != null && relay.length() > 150){
            relay = relay.substring(0, 149);
        }
        if(delays  != null && delays.length() > 150){
            delays = delays.substring(0, 149);
        }
        if(dsn != null && dsn.length() > 45){
            dsn = dsn.substring(0, 44);
        }
        if(status != null && status.length() > 45){
            status = status.substring(0, 44);
        }
        if(nameEmail  != null && nameEmail.length() > 199){
            nameEmail = nameEmail.substring(0, 199);
        }
        //sub string end
//        System.out.println("dsn "+status);
        if (time != null) {
            logMTA = new LogEmailMTA();
            logMTA.setTime(time);
        }
        if (logMTA != null) {

            if (inforAction != null) {
                logMTA.setInforAction(inforAction);
            }
            if (relay != null) {
                logMTA.setRelay(relay);
            }
            if (numDelay > 0) {
                logMTA.setDelay(numDelay);
            }
            if (delays != null) {
                logMTA.setDelays(delays);
            }
            if (dsn != null) {
                logMTA.setDsn(dsn);
            }
            if (nameEmail != null) {
                logMTA.setName_email(nameEmail);
            }
            if (sCurrentLine.toLowerCase().contains(DELIVERY_OK)) {
                logMTA.setResult("DELIVERY_OK");
            }
            if (sCurrentLine.toLowerCase().contains(QUEUED_AS)) {
                logMTA.setResult("ADD QUEUED");
            }
            if (status != null) {
                logMTA.setStatus(status);
            }

            logMTA.setAction("SEND_EMAIL");
        }
        return logMTA;
    }

    private static String getDateTime(String sCurrentLine) {
        try {
            int yearCurrent = Calendar.getInstance().get(Calendar.YEAR);
        String month = sCurrentLine.substring(0,3);
        month = getMonth(month);
        String dateCurerrent = StringUtils.substringBetween(sCurrentLine," ", " ");
        if(dateCurerrent.length() == 1){
            dateCurerrent = "0"+dateCurerrent;
        }
        String time = sCurrentLine.substring(7,15);
        time = time.replace(" ", "");
        return yearCurrent+"-"+month+"-"+dateCurerrent+ " "+time;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        
    }

    
    private static LogEmailMTA getInforSendEmail(String sCurrentLine) {
        LogEmailMTA logMTA = null;
        String time = getDateTime(sCurrentLine);
        
        sCurrentLine = sCurrentLine.substring(21);
        String inforAction = "";
        try {
            inforAction = sCurrentLine.substring(0, sCurrentLine.indexOf("from=<"));
        } catch (Exception e) {
            logger.error("cant get inforAction");
        }

        if( inforAction  != null && inforAction.length() > 199){
            inforAction = sCurrentLine.substring(0, sCurrentLine.indexOf("from"));
        }
        if(inforAction  != null && inforAction.length() > 199){
            inforAction = sCurrentLine.substring(0, sCurrentLine.indexOf(": "));
        }
        
        
        String relay = StringUtils.substringBetween(sCurrentLine, "relay=", ",");
        
        String delay = StringUtils.substringBetween(sCurrentLine, "delay=", ",");
        float numDelay = 0;
        if(delay != null){
            delay.replace(" ", "");
            try {
                numDelay = Float.parseFloat(delay);

            } catch (Exception e) {
            }
        }
       
        String delays = StringUtils.substringBetween(sCurrentLine, "delays=", ",");
        
        String dsn = StringUtils.substringBetween(sCurrentLine, "dsn=", ",");
        
        String nameEmail = StringUtils.substringBetween(sCurrentLine, "from=<", ">,");
        String status = StringUtils.substringBetween(sCurrentLine, "status=", " ");
        //sub string start
        if(inforAction  != null && inforAction.length() > 199){
            inforAction = sCurrentLine.substring(0, 199);
        }
        if(relay  != null && relay.length() > 150){
            relay = relay.substring(0, 149);
        }
        if(delays  != null && delays.length() > 150){
            delays = delays.substring(0, 149);
        }
        if(dsn != null && dsn.length() > 45){
            dsn = dsn.substring(0, 44);
        }
        if(status != null && status.length() > 45){
            status = status.substring(0, 44);
        }
        if (nameEmail != null && nameEmail.length() > 200) {
            nameEmail = nameEmail.substring(0, 199);
        }
        //sub string end
        
        if (time != null) {
            logMTA = new LogEmailMTA();
            logMTA.setTime(time);
        }
        if (logMTA != null) {

            if (inforAction != null) {
                logMTA.setInforAction(inforAction);
            }
            if (relay != null) {
                logMTA.setRelay(relay);
            }
            if (numDelay > 0) {
                logMTA.setDelay(numDelay);
            }
            if (delays != null) {
                logMTA.setDelays(delays);
            }
            if (dsn != null) {
                logMTA.setDsn(dsn);
            }
            if (nameEmail != null) {
                logMTA.setName_email(nameEmail);
            }
            if (sCurrentLine.toLowerCase().contains(DELIVERY_OK)) {
                logMTA.setResult("DELIVERY_OK");
            }
            if (sCurrentLine.toLowerCase().contains(QUEUED_AS)) {
                logMTA.setResult("ADD QUEUED");
            }
            if (status != null) {
                logMTA.setStatus(status);
            }

            logMTA.setAction("SEND_EMAIL");
        }
        return logMTA;
    }

    private static int getTypeLine(String sCurrentLine) {
        if (sCurrentLine.length() == 0) {
            return -1;
        }
        if (sCurrentLine.toLowerCase().contains(DELIVERY_OK)) {
            return 0;
        }
//        if (sCurrentLine.toLowerCase().contains(RECIVE_EMAIL)) {
//            return 1;
//        }
//        if (sCurrentLine.toLowerCase().contains(FWD_EMAIL)) {
//            return 2;
//        }
        return -1;
    }

    public static int countLines(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }
    private static String getMonth(String month) {
        if(month.toLowerCase().contains("jan")){
            return "01";
        }
        if(month.toLowerCase().contains("feb")){
            return "02";
        }
        if(month.toLowerCase().contains("mar")){
            return "03";
        }
        if(month.toLowerCase().contains("apr")){
            return "04";
        }
        if(month.toLowerCase().contains("may")){
            return "05";
        }
        if(month.toLowerCase().contains("jun")){
            return "06";
        }
        if(month.toLowerCase().contains("jul")){
            return "07";
        }
        if(month.toLowerCase().contains("aug")){
            return "08";
        }
        if(month.toLowerCase().contains("sep")){
            return "09";
        }
        if(month.toLowerCase().contains("oct")){
            return "10";
        }
        if(month.toLowerCase().contains("nov")){
            return "11";
        }
        if(month.toLowerCase().contains("dec")){
            return "12";
        }
        return "";
    }

    private LogEmailMTA getInforFWDEmail(String sCurrentLine) {
        LogEmailMTA logMTA = null;
        String time = getDateTime(sCurrentLine);
        
        sCurrentLine = sCurrentLine.substring(21);
        
        String inforAction = sCurrentLine.substring(0, sCurrentLine.indexOf("FWD"));
       
        String relay = StringUtils.substringBetween(sCurrentLine, "relay=", ",");
       
        String delay = StringUtils.substringBetween(sCurrentLine, "delay=", ",");
        float numDelay = 0;
        if(delay != null){
            delay.replace(" ", "");
            try {
                numDelay = Float.parseFloat(delay);

            } catch (Exception e) {
            }
        }
        String delays = StringUtils.substringBetween(sCurrentLine, "delays=", ",");
        
        String dsn = StringUtils.substringBetween(sCurrentLine, "dsn=", ",");
        
        String nameEmail = StringUtils.substringBetween(sCurrentLine, "from <", "> ->");
       
        String status = StringUtils.substringBetween(sCurrentLine, "status=", " ");
        
        String listEmailReceive = StringUtils.substringBetween(sCurrentLine, "->", ",BODY");
        if(listEmailReceive != null){
            listEmailReceive = listEmailReceive.replaceAll("<", "").replaceAll(">","");
        }
         //sub string start
        if(inforAction  != null && inforAction.length() > 199){
            inforAction = sCurrentLine.substring(0, 199);
        }
        if(relay  != null && relay.length() > 150){
            relay = relay.substring(0, 149);
        }
        if(delays  != null && delays.length() > 150){
            delays = delays.substring(0, 149);
        }
        if(dsn != null && dsn.length() > 45){
            dsn = dsn.substring(0, 44);
        }
        if(status != null && status.length() > 45){
            status = status.substring(0, 44);
        }
        if (nameEmail != null && nameEmail.length() > 200) {
            nameEmail = nameEmail.substring(0, 199);
        }
        //sub string end
        System.out.println("listEmailReceive "+listEmailReceive);
        if (time != null) {
            logMTA = new LogEmailMTA();
            logMTA.setTime(time);
        }
        if (logMTA != null) {

            if (inforAction != null) {
                logMTA.setInforAction(inforAction);
            }
            if (relay != null) {
                logMTA.setRelay(relay);
            }
            if (numDelay > 0) {
                logMTA.setDelay(numDelay);
            }
            if (delays != null) {
                logMTA.setDelays(delays);
            }
            if (dsn != null) {
                logMTA.setDsn(dsn);
            }
            if (nameEmail != null) {
                logMTA.setName_email(nameEmail);
            }
            if (sCurrentLine.toLowerCase().contains(DELIVERY_OK)) {
                logMTA.setResult("DELIVERY_OK");
            }
            if (sCurrentLine.toLowerCase().contains(QUEUED_AS)) {
                logMTA.setResult("ADD QUEUED");
            }
            if (status != null) {
                logMTA.setStatus(status);
            }
            if (listEmailReceive != null) {
                String[] arrstring = listEmailReceive.split(",");
                List<String> list = Arrays.asList(arrstring);
                logMTA.setListReceiveFWD(list);
            }

            logMTA.setAction("FWD_EMAIL");
        }
        return logMTA;
    }

    private boolean checkTimeIsAfterLasTime(String sCurrentLine, int counter) {
       if(sCurrentLine.equals(null)){
            return true;
        }
        if(sCurrentLine.length() > 20){
            String dateInString = sCurrentLine.substring(0, 19);
            
            try {
                Date nowTime = formatter.parse(dateInString);
                if(lastTime == null){
                    System.out.println("Set lastTime ==="+counter);
                        logger.info("Set lastTime ===");
                    lastTime = nowTime;
                    return true;
                }
                if (counter == 0) {
                    if (nowTime.before(lastTime)) {
                        System.out.println("Not after Last Time ===");
                        logger.info("Not after Last Time ===");
                        return false;
                    }else{
                        maxTime = nowTime;
                    }
                }else if(timeRun > 0){
                    if (nowTime.before(lastTime)) {
                        System.out.println("Not after Last Time ===");
                        logger.info("Not after Last Time ===");
                        return false;
                    } 
                }
            } catch (ParseException ex) {
                return true;
            }
            
        }
        return true;
    }

}
