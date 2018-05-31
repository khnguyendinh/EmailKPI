/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mtatrace;

import com.viettel.common.bean.MsgMail;
import com.viettel.common.bo.LogEmailMTA;
import com.viettel.common.util.ActionThread;
import com.viettel.common.util.CommonUtils;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class TestProccess extends ActionThread {

    private LinkedBlockingQueue<LogEmailMTA> queueLogEmail;
    private String host = "zimbra.viettel.com.vn";

//    private String folderDirectory = "D:\\sourcecode\\MtaTrace\\0\\";
    private String filePathMailLog = "F:\\SOURCE CODE\\khoand\\CODE\\LogEmail\\SAVE DB\\log\\logEmail.txt";
//    private String folderDirectory = "/home/app/msg/";
//    private String errorDirectory = "D:\\sourcecode\\MtaTrace\\error\\";
    private String errorDirectory = "F:\\SOURCE CODE\\khoand\\CODE\\LogEmail\\SAVE DB\\log\\error\\";
//    private String errorDirectory = "/home/app/MtaTrace/error/";
    private static final String Read_Email = "GetMsgRequest";
    private static final String Delete_Email = "MsgActionRequest";
    private static final String Search_Email = "SearchRequest";
    File dirDes = new File(errorDirectory);
    private static Logger logger;
    private long lastKnownPosition = 0;
    //10%
    private int percentLogRead = 100;
    // number log read in each 100 logs
    private int numberLogRead = 0;
    private int BLOCK_TIME = 0;
    private long timeStart;

    public TestProccess(String name, LinkedBlockingQueue<LogEmailMTA> queueMsg) {
        this.setName(name);
        this.queueLogEmail = queueMsg;
        this.logger = Logger.getLogger("maillog");
//        PropertyConfigurator.configure("D:\\sourcecode\\MtaTrace\\MtaTrace\\etc\\log4j.conf");
        PropertyConfigurator.configure("F:\\SOURCE CODE\\khoand\\CODE\\LogEmail\\SAVE DB\\MtaTrace\\etc\\log4j.conf");
//        PropertyConfigurator.configure("/home/app/MtaTrace/etc/log4j.conf");
        logger.info("Mail Log Process Start");
        System.out.println("Mail Log Process Start");
        numberLogRead = percentLogRead;
        timeStart = System.currentTimeMillis();
    }

    private void getConfig() {
        Properties properties = new Properties();
        FileInputStream fileInputStream = null;
        logger.info("get Config");
//        System.out.println("get config ");
        try {
//            fileInputStream = new FileInputStream("../etc/config.properties");
            fileInputStream = new FileInputStream("F:\\SOURCE CODE\\khoand\\DOCUMENT\\ReadLog\\config.properties");
            properties.load(fileInputStream);
            this.filePathMailLog = properties.getProperty("folderDirectory_MailLog");
            this.errorDirectory = properties.getProperty("errorDirectory");
            this.numberLogRead = Integer.parseInt(properties.getProperty("percentLogRead_MailLog"));
            this.BLOCK_TIME = Integer.parseInt(properties.getProperty("BlockTime_Mail"));
        } catch (IOException ex) {
            logger.info(ex.getMessage());
            System.out.println(ex.getMessage());

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

    private void readFileMailLog() throws IOException, FileNotFoundException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePathMailLog));
            String sCurrentLine = "";
            long tmpTime;
            System.out.println("DATE "+new Date().toString());
            if (countLines(filePathMailLog) > lastKnownPosition) {
//                System.out.println("lastKnownPosition MailLog " + lastKnownPosition);
                for (int i = 0; i < lastKnownPosition; i++) {
                    reader.readLine();
                }
                while ((sCurrentLine = reader.readLine()) != null) {
                    tmpTime = System.currentTimeMillis() - timeStart;
//                    if (tmpTime <= BLOCK_TIME * 60 * 1000) {
//                        System.out.println("RUN_EMAIL "+BLOCK_TIME);
//                        System.out.println("DATE "+new Date().toString()+ " LINE __"+lastKnownPosition);
                        lastKnownPosition++;
                        LogEmailMTA logEmail = null;
//                        if (sCurrentLine.length() > 0 && Character.isDigit(sCurrentLine.charAt(0)) && lastKnownPosition % 100 <= numberLogRead) {
//                            switch (getTypeLine(sCurrentLine)) {
//                                case 0:
//                                    logEmail = getInforReadEmail(sCurrentLine);
//                                    break;
//                                case 1:
//                                    logEmail = getInforDeleteEmail(sCurrentLine);
//                                    break;
//                                case 2:
//                                    logEmail = getInforSearchEmail(sCurrentLine);
//                                    break;
//
//                                default:
//                                    break;
//                            }
//
//                        }
//                        logEmail.setType_log("MAIL");
//                        queueLogEmail.add(logEmail);
                        sCurrentLine = "";
//                    }else {
//                        timeStart = System.currentTimeMillis();
////                        System.out.println("STOP_EMAIL");
//                    }
                }
                reader.close();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    protected void action() throws Exception {
        getConfig();
        readFileMailLog();
//        while(true){
//           System.out.println("RUN "+new Date().toString());
//        }
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
        System.out.println("SLEEP");
        return 10 * 1000;
    }

    private static LogEmailMTA getInforSearchEmail(String sCurrentLine) {
        String input = sCurrentLine;
        LogEmailMTA logEmail = null;
        String time = input.substring(0, 19);
        String miliSecond = input.substring(20, 23);
        input = input.substring(24);
        String status = input.split("  ")[0];
        String ip = StringUtils.substringBetween(input, "ip=", ";");
        String oIp = StringUtils.substringBetween(input, "oip=", ";");
        String inforAction = input.substring(input.indexOf("[qtp"), input.indexOf("]"));

        String ua = StringUtils.substringBetween(input, "ua=", "]");
        String elapsed = "";
        if (input.split("elapsed=").length > 1) {
            elapsed = input.split("elapsed=")[1];
        }
        String result = "FAIL";
        int num_elapse = -1;
        try {
            num_elapse = Integer.parseInt(elapsed);

            if (num_elapse > 0) {
                result = "SUCCESS";
            }
        } catch (Exception e) {
        }
        String emailName = StringUtils.substringBetween(input, "name=", ";");
        if (time != null) {
            logEmail = new LogEmailMTA();
            logEmail.setTime(time);
        }
        if (logEmail != null) {

            if (status != null) {
                logEmail.setStatus(status);
            }
            if (ip != null) {
                logEmail.setIp(ip);
            }
            if (oIp != null) {
                logEmail.setoIp(oIp);
            }
            if (ua != null) {
                logEmail.setUa(ua);
            }
            
            logEmail.setElapsed(num_elapse);
            
            if (result != null) {
                logEmail.setResult(result);
            }
            long num = -1;
            if (miliSecond != null) {
                num = Long.parseLong(miliSecond);
                if (num > 0) {
                    logEmail.setMiliSecond(num);
                }

            }
            if (inforAction != null) {
                logEmail.setInforAction(inforAction);
            }
            if (emailName != null) {
                logEmail.setName_email(emailName);
            }
            logEmail.setAction("SEARCH_EMAIL");
        }
        return logEmail;
    }

    private static LogEmailMTA getInforDeleteEmail(String sCurrentLine) {
        String input = sCurrentLine;
        LogEmailMTA logEmail = null;
        String time = input.substring(0, 19);
        String miliSecond = input.substring(20, 23);
        input = input.substring(24);
        String status = input.split("  ")[0];
        String ip = StringUtils.substringBetween(input, "ip=", ";");
        String oIp = StringUtils.substringBetween(input, "oip=", ";");
        String inforAction = input.substring(input.indexOf("[qtp"), input.indexOf("]"));

        String ua = StringUtils.substringBetween(input, "ua=", "]");
        String elapsed = "";
        if (input.split("elapsed=").length > 1) {
            elapsed = input.split("elapsed=")[1];
        }
        String result = "FAIL";
        int num_elapse = -1;
        try {
            num_elapse = Integer.parseInt(elapsed);

            if (num_elapse > 0) {
                result = "SUCCESS";
            }
        } catch (Exception e) {
        }
        String emailName = StringUtils.substringBetween(input, "name=", ";");
        if (time != null) {
            logEmail = new LogEmailMTA();
            logEmail.setTime(time);
        }
        if (logEmail != null) {

            if (status != null) {
                logEmail.setStatus(status);
            }
            if (ip != null) {
                logEmail.setIp(ip);
            }
            if (oIp != null) {
                logEmail.setoIp(oIp);
            }
            if (ua != null) {
                logEmail.setUa(ua);
            }
            
            logEmail.setElapsed(num_elapse);
            
            if (result != null) {
                logEmail.setResult(result);
            }
            long num = -1;
            if (miliSecond != null) {
                num = Long.parseLong(miliSecond);
                if (num > 0) {
                    logEmail.setMiliSecond(num);
                }
            }
            if (inforAction != null) {
                logEmail.setInforAction(inforAction);
            }
            if (emailName != null) {
                logEmail.setName_email(emailName);
            }

            logEmail.setAction("DELETE_EMAIL");
        }
        return logEmail;
    }

    private static LogEmailMTA getInforReadEmail(String sCurrentLine) {
        String input = sCurrentLine;
        LogEmailMTA logEmail = null;
        String time = input.substring(0, 19);
        String miliSecond = input.substring(20, 23);
        input = input.substring(24);
        String status = input.split("  ")[0];
        String ip = StringUtils.substringBetween(input, "ip=", ";");
        String oIp = StringUtils.substringBetween(input, "oip=", ";");
        String inforAction = input.substring(input.indexOf("[qtp"), input.indexOf("]"));

        String ua = StringUtils.substringBetween(input, "ua=", "]");
        String elapsed = "";
        if (input.split("elapsed=").length > 1) {
            elapsed = input.split("elapsed=")[1];
        }
        String result = "FAIL";
        int num_elapse = -1;
        try {
            num_elapse = Integer.parseInt(elapsed);

            if (num_elapse > 0) {
                result = "SUCCESS";
            }
        } catch (Exception e) {
        }
        String emailName = StringUtils.substringBetween(input, "name=", ";");
        if (time != null) {
            logEmail = new LogEmailMTA();
            logEmail.setTime(time);
        }
        if (logEmail != null) {

            if (status != null) {
                logEmail.setStatus(status);
            }
            if (ip != null) {
                logEmail.setIp(ip);
            }
            if (oIp != null) {
                logEmail.setoIp(oIp);
            }
            if (ua != null) {
                logEmail.setUa(ua);
            }
            
            logEmail.setElapsed(num_elapse);
            
            if (result != null) {
                logEmail.setResult(result);
            }
            long num = -1;
            if (miliSecond != null) {
                num = Long.parseLong(miliSecond);
                if (num > 0) {
                    logEmail.setMiliSecond(num);
                }
            }
            if (inforAction != null) {
                logEmail.setInforAction(inforAction);
            }
            if (emailName != null) {
                logEmail.setName_email(emailName);
            }
            logEmail.setAction("READ_EMAIL");
        }
        return logEmail;
    }

    private static int getTypeLine(String sCurrentLine) {
        if (sCurrentLine.length() == 0) {
            return -1;
        }
        if (sCurrentLine.contains(Read_Email)) {
            return 0;
        }
        if (sCurrentLine.contains(Delete_Email)) {
            return 1;
        }
        if (sCurrentLine.contains(Search_Email)) {
            return 2;
        }
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

}
