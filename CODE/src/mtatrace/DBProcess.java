/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mtatrace;

import com.viettel.common.bo.LogEmailMTA;
import com.viettel.common.db.QueryUtils;
import com.viettel.common.util.ActionThread;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 *
 */
public class DBProcess extends ActionThread {

    private LinkedBlockingQueue<LogEmailMTA> queueEmail;
    private LinkedBlockingQueue<LogEmailMTA> queueMTA;
    private List<LogEmailMTA> lsEmail = new ArrayList<>();
    private List<LogEmailMTA> lsMTA = new ArrayList<>();
//    private List<LogMTA> lsMTA = new ArrayList<>();
    QueryUtils queryUtils = new QueryUtils();
    private int size = 200;
    long time = System.currentTimeMillis();
    private static Logger logger;
         private String config = "../etc/config.properties";
     private String configLog4j = "../etc/log4j.conf";
//    private String config = "F:\\SOURCE CODE\\khoand\\CODE\\LogEmail\\CODE\\etc\\config.properties";
//     private String configLog4j = "F:\\SOURCE CODE\\khoand\\CODE\\LogEmail\\CODE\\etc\\log4j.conf";
//     private String config = "F:\\SOURCE CODE\\khoand\\DOCUMENT\\ReadLog\\config2.properties";

    public DBProcess(String name, LinkedBlockingQueue<LogEmailMTA> queueEmail,LinkedBlockingQueue<LogEmailMTA> queueMTA) {
        System.out.println("BAT DAU VAO DBProcess Start");
        this.setName(name);
        this.queueEmail = queueEmail;
        this.queueMTA = queueMTA;
        time = System.currentTimeMillis();
        this.logger = Logger.getLogger("DBProcess");
        PropertyConfigurator.configure(configLog4j);
         logger.info("DBProcess Start");
         System.out.println("DBProcess Start");
        
    }

    private void getConfigDB() {
        Properties properties = new Properties();
        FileInputStream fileInputStream = null;
        try {
            System.out.println("DBProcess get Config");
            fileInputStream = new FileInputStream(config);
            logger.info("DBProcess get Config");
            properties.load(fileInputStream);
            this.size = Integer.parseInt(properties.getProperty("Size"));
            System.out.println("Size = "+size);
        } catch (IOException ex) {
            logger.info(ex.getMessage());
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    logger.info(ex);
                }
            }
        }

    }

    @Override
    protected void action() throws Exception {
        System.out.println("VAO action CUA dbPROCESS ");
        getConfigDB();
        while (queueEmail.size() > 0) {
            LogEmailMTA msg = queueEmail.poll();
            if (msg != null) {
                lsEmail.add(msg);
            }
        }
            
            long timeCurrentEmail = System.currentTimeMillis() - time;
//            System.out.println("timcurrent" + timeCurrent);
//        if (lsEmail.size() > size || (timeCurrentEmail > 3000 && lsEmail.size() > 0)) {
        if (lsEmail.size() >= size) {
            System.out.println("Size queueEmail : " + queueEmail.size());
            System.out.println("Size queue : " + queueEmail.size());
            System.out.println("lsEmail size :  " + lsEmail.size());
            queryUtils.insertMailLog(lsEmail);
            lsEmail = new ArrayList<>();
            time = System.currentTimeMillis();
        }
        while (queueMTA.size() > 0) {
            LogEmailMTA msg = queueMTA.poll();
            if (msg != null) {
                lsMTA.add(msg);
            }
        }

        long timeCurrentMTA = System.currentTimeMillis() - time;
//            System.out.println("timcurrent" + timeCurrent);
//        System.err.println("Size queueMTA: " + queueMTA.size());
//        if (lsMTA.size() > size || (timeCurrentMTA > 3000 && lsMTA.size() > 0)) {
        if (lsMTA.size() >= size) {
            System.out.println("Size queueMTA : " + queueMTA.size());
            System.out.println("lsMTA size :  " + lsMTA.size());
            queryUtils.insertMailLog(lsMTA);
            lsMTA = new ArrayList<>();
            time = System.currentTimeMillis();
        }
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
        return 100;
    }

}
