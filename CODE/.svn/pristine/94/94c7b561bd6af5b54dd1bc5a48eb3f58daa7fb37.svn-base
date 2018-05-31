/*
 * Copyright (C) 2010 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.common.db;

import com.viettel.common.bean.AuditLog;
import com.viettel.common.bean.ImportConfigBean;
import com.viettel.common.bean.MsgMail;
import com.viettel.common.bo.LogEmailMTA;
import com.viettel.common.bo.LogMTA;
import com.viettel.common.util.CommonUtils;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Cau hinh import file Excel.
 *
 * @author HuyenNV
 * @update thanhbv4
 * @since 1.0
 * @version 1.0
 */
public class QueryUtils {

    private static final Logger LOGGER = Logger.getLogger("DBProcess");

    public QueryUtils() {
    }
//    public static void main(String[] args){
//        QueryUtils queryUtils=new QueryUtils();
//        MsgMail msgMail = new MsgMail();
//        
//        queryUtils.insertMtaTransfer();
//    }

    public void insertMtaTransfer(List<MsgMail> dataList) throws Exception {
        System.out.println("  Start Insert ");
        StringBuilder queryHead = new StringBuilder(" INSERT INTO mta_transfer (msg_id,sender,subject,file_attact,sent_date,receiver) ");
        StringBuilder queryTail = new StringBuilder(" VALUES (?,?,?,?,?,?) ");
        String sqlInsertMTATrans = queryHead.toString() + queryTail.toString();
        Session session = (Session) baseHibernateDAO.openThreadSession();
        System.out.println(" baseHibernateDAO.beginTransaction() ");
        baseHibernateDAO.beginTransaction();
        PreparedStatement preparedStatement = session.connection().prepareStatement(sqlInsertMTATrans);
        int count_size = 0;
        System.out.println("  PreparedStatement preparedStatement = session.connection().prepareStatement(sql); ");
        for (MsgMail msg : dataList) {
            if (msg.isError() == false) {
                List<Address> receivers = (List<Address>) msg.getReciever();
                if (receivers != null && receivers.size() > 0) {
                    for (Address receiver : receivers) {
                        count_size++;
                        CommonUtils.filter(msg.getMsgId(), preparedStatement, 1);
                        CommonUtils.filter(msg.getSender(), preparedStatement, 2);
                        CommonUtils.filter(msg.getSubject(), preparedStatement, 3);
                        CommonUtils.filter(msg.getFileAttach(), preparedStatement, 4);
                        CommonUtils.filter(msg.getSentDate(), preparedStatement, 5);
                        CommonUtils.filter(((InternetAddress) receiver).getAddress(), preparedStatement, 6);
                        preparedStatement.addBatch();
//                            
                        if (count_size >= INSERT_FETCH_SIZE) {
                            try {
                                preparedStatement.setFetchSize(INSERT_FETCH_SIZE);
                                preparedStatement.executeBatch();
                                System.out.println(" ");
                                count_size = 0;
                            } catch (Exception e) {
                                preparedStatement.clearBatch();
                                System.out.println("Error " + e.getMessage());
                            }
                        }
                    }

                }
            }

        }
        System.out.println("COUNT : " + count_size);
        preparedStatement.executeBatch();
        System.out.println("  preparedStatement.executeBatch(); ");
        baseHibernateDAO.commitTransaction();
        System.out.println("   baseHibernateDAO.commitTransaction();");
        preparedStatement.close();
        System.out.println("begin insert mta trans history");
        insertMtaTransHistory1(dataList);

    }

    public void insertMtaTransHistory1(List<MsgMail> dataList) throws Exception {
        System.out.println("Insert in to History " + dataList.size());
        StringBuilder queryHeadMtaTransHistory = new StringBuilder(" INSERT INTO mta_trans_history (msg_id,status_msg,msg_name,insert_time) ");
        StringBuilder queryTailMtaTransHistory = new StringBuilder(" VALUES (?,?,?,?) ");
        String sql1 = queryHeadMtaTransHistory.toString() + queryTailMtaTransHistory.toString();
        Session session = (Session) baseHibernateDAO.openThreadSession();
        baseHibernateDAO.beginTransaction();
        PreparedStatement preparedStatement = session.connection().prepareStatement(sql1);
        for (MsgMail msg : dataList) {

            CommonUtils.filter(msg.getMsgId(), preparedStatement, 1);
            if (msg.isError() == true) {
                CommonUtils.filter(0, preparedStatement, 2);
            } else {
                CommonUtils.filter(1, preparedStatement, 2);
            }
            CommonUtils.filter(msg.getMsgName(), preparedStatement, 3);
            java.util.Date dt = new java.util.Date();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = sdf.format(dt);
            CommonUtils.filter(currentTime, preparedStatement, 4);

            preparedStatement.addBatch();
        }
        preparedStatement.setFetchSize(INSERT_FETCH_SIZE);
        preparedStatement.executeBatch();
        System.out.println(" Insert Thanh Cong insertMtaTransHistory");
        preparedStatement.close();
        baseHibernateDAO.commitTransaction();
    }

    public void insertAuditLogProcess(List<AuditLog> dataList) throws Exception {
        System.out.println("Insert in to Audit Log Process: " + dataList.size());
        StringBuilder queryHeadAudit = new StringBuilder(" INSERT INTO audit_log_process (date_audit,email,oip,protocol) ");
        StringBuilder queryTailAudit = new StringBuilder(" VALUES (?,?,?,?) ");
        String sqlAudit = queryHeadAudit.toString() + queryTailAudit.toString();
        Session session = (Session) baseHibernateDAO.openThreadSession();
        baseHibernateDAO.beginTransaction();
        PreparedStatement preparedStatement = session.connection().prepareStatement(sqlAudit);
        for (AuditLog audit : dataList) {

            CommonUtils.filter(audit.getDateAudit(), preparedStatement, 1);
            CommonUtils.filter(audit.getEmail(), preparedStatement, 2);
            CommonUtils.filter(audit.getOip(), preparedStatement, 3);
            CommonUtils.filter(audit.getProtocol(), preparedStatement, 4);
            preparedStatement.addBatch();
        }
        preparedStatement.setFetchSize(INSERT_FETCH_SIZE);
        preparedStatement.executeBatch();
        System.out.println(" Insert Thanh Cong Audit Log Process");
        preparedStatement.close();
        baseHibernateDAO.commitTransaction();
    }
    public void insertLogMTAProcess(List<LogMTA> dataList) throws Exception {
        System.out.println("Insert in to LogMTA Process: " + dataList.size());
        StringBuilder queryHeadMtaTransHistory = new StringBuilder(" INSERT INTO mtaLog (time,inforAction,action,relay"
                + ",delay,delays,status,dsn,"
                + "nameEmail,result,listReceiveFWD) ");
        StringBuilder queryTailMtaTransHistory = new StringBuilder(" VALUES (?,?,?,?,"
                + "?,?,?,?,"
                + "?,?,?) ");
        String sql1 = queryHeadMtaTransHistory.toString() + queryTailMtaTransHistory.toString();
        Session session = (Session) baseHibernateDAO.openThreadSession();
        baseHibernateDAO.beginTransaction();
        PreparedStatement preparedStatement = session.connection().prepareStatement(sql1);
        for (LogMTA logMTA : dataList) {

            CommonUtils.filter(logMTA.getTime(), preparedStatement, 1);
            CommonUtils.filter(logMTA.getInforAction(), preparedStatement, 2);
            CommonUtils.filter(logMTA.getAction(), preparedStatement, 3);
            CommonUtils.filter(logMTA.getRelay(), preparedStatement, 4);
            CommonUtils.filter(logMTA.getDelay(), preparedStatement, 5);
            CommonUtils.filter(logMTA.getDelays(), preparedStatement, 6);
            CommonUtils.filter(logMTA.getStatus(), preparedStatement, 7);
            CommonUtils.filter(logMTA.getDsn(), preparedStatement, 8);
            CommonUtils.filter(logMTA.getNameEmail(), preparedStatement, 9);
            CommonUtils.filter(logMTA.getResult(), preparedStatement, 10);
            if(logMTA.getListReceiveFWD() != null){
                CommonUtils.filter(logMTA.getListReceiveFWD().toString(), preparedStatement, 11);
            }else{
                CommonUtils.filter("", preparedStatement, 11);
            }
            
            preparedStatement.addBatch();
        }
        preparedStatement.setFetchSize(INSERT_FETCH_SIZE);
        preparedStatement.executeBatch();
        System.out.println("Insert Thanh Cong insertLogMTAProcess");
        preparedStatement.close();
        baseHibernateDAO.commitTransaction();
    }

    /**
     * Chen vao CSDL.
     *
     * @param tableName Bang chi tiet
     * @param columnConfig Cau hinh cot Excel
     * @param a Mang doi tuong du lieu
     * @param inImportMasterId ID bang master
     */
    public void insertIntoDatabase(List<Object[]> dataList) throws Exception {

        StringBuilder queryHead = new StringBuilder(" INSERT INTO " + tableName + "( ");
        StringBuilder queryTail = new StringBuilder(" VALUES ( ");
        for (int col = 0; col < columnConfig.length; col++) {
            if (!columnConfig[col].getDatabaseColumn().isEmpty()) {
                if (col == 1) {
                    queryHead.append(columnConfig[col].getDatabaseColumn());
                    queryTail.append(" ? ");
                } else {
                    if (col < 6 || col > 50) {
                        queryHead.append(" , ").append(columnConfig[col].getDatabaseColumn());
                        queryTail.append(" , ? ");
                    }
                }
            }
        }
        String sql = queryHead + " ) " + queryTail + " ) ";
        Session session = (Session) BaseHibernateDAO.threadLocal.get();
        PreparedStatement preparedStatement = session.connection().prepareStatement(sql);
        int count;
        for (Object[] a : dataList) {
            count = 1;
            for (int col = 0; col < columnConfig.length; col++) {
                if (col < 6 || col > 50) {
                    if (!columnConfig[col].getDatabaseColumn().isEmpty()) {
                        CommonUtils.filter(a[col], preparedStatement, count++);
                    }
                }
            }
            preparedStatement.addBatch();
        }
        preparedStatement.setFetchSize(INSERT_FETCH_SIZE);
        preparedStatement.executeBatch();
        preparedStatement.close();
    }

    /**
     * Chen vao CSDL.
     *
     * @param tableName Bang chi tiet
     * @param columnConfig Cau hinh cot Excel
     * @param a Mang doi tuong du lieu
     * @param inImportMasterId ID bang master
     */
    public void insertIntoDatabase(Object[] a) throws Exception {

        try {
            // <editor-fold defaultstate="collapsed" desc="Thanh lap cau query">
            StringBuilder strHead = new StringBuilder(" INSERT INTO " + tableName + "(" + tableName + "_id");
            StringBuffer strTail = new StringBuffer(") VALUES (" + tableName + "_SEQ.nextval");
            for (int col = 0; col < columnConfig.length; col++) {
                if (!columnConfig[col].getDatabaseColumn().isEmpty() && (a[col] != null)) {
                    strHead.append(", ").append(columnConfig[col].getDatabaseColumn());
                    strTail.append(", ?");
                }
            }
            strHead.append(strTail).append(") ");
            SQLQuery query = baseHibernateDAO.createSQLQuery(strHead.toString());
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Day vao CSDL">
            int count = 0;
            for (int col = 0; col < columnConfig.length; col++) {
                if (!columnConfig[col].getDatabaseColumn().isEmpty() && (a[col] != null)) {
                    query.setParameter(count, a[col]);
                    count++;
                }
            }
            query.executeUpdate();
            // </editor-fold>
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

        }
    }

    /**
     * Cap nhat CSDL da co san va tra ve danh sach id cac ban ghi da duoc cap
     * nhat
     *
     * @param tableName Bang chi tiet
     * @param columnConfig Cau hinh cot Excel
     * @param a Mang doi tuong du lieu
     * @param inImportMasterId ID bang master
     */
    public List<Long> updateDBAndReturnList(List<Object[]> dataList) throws Exception {

        ArrayList<Long> idList = new ArrayList();
        for (Object[] a : dataList) {
            try {
                // <editor-fold defaultstate="collapsed" desc="Thanh lap cau query">
                StringBuilder sqlStr = new StringBuilder(" UPDATE " + tableName + " SET ");
                for (int col = 1; col < columnConfig.length; col++) {
                    if (!columnConfig[col].getDatabaseColumn().isEmpty() && (a[col] != null)) {
                        sqlStr.append(columnConfig[col].getDatabaseColumn());
                        sqlStr.append(" = ? ,");
                    }
                }
                sqlStr.replace(sqlStr.length() - 1, sqlStr.length(), " ");
                sqlStr.append(" WHERE ");
                sqlStr.append(tableName);
                sqlStr.append("_ID = ? ");
                SQLQuery query = baseHibernateDAO.createSQLQuery(sqlStr.toString());
                // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="Day vao CSDL">
                int count = 0;
                for (int col = 1; col < columnConfig.length; col++) {
                    if (!columnConfig[col].getDatabaseColumn().isEmpty() && (a[col] != null)) {
                        query.setParameter(count, a[col]);
                        count++;
                    }
                }
                query.setParameter(count, a[0]);
                query.executeUpdate();
                idList.add((Long) a[0]);
                // </editor-fold>
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage());
            }
        }
        return idList;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    //Cau hinh
    //Ten bang day vao CSDL
    private String tableName;
    //Doi tuong DAO
    private BaseHibernateDAO baseHibernateDAO = new BaseHibernateDAO();
    //Danh sach cac dong co du lieu
    private List<Integer> rowList = new LinkedList<Integer>();
    /**
     * Constants for DB information
     */
    //Cau hinh
    private ImportConfigBean[] columnConfig;
    private static final Integer INSERT_FETCH_SIZE = 100;

    public void insertMailLog(List<LogEmailMTA> lsMsg) {
        try {
            System.out.println("insertMailLog start "+lsMsg.size());
            LOGGER.info("insertMailLog start "+lsMsg.size());
            StringBuilder queryHeadMtaTransHistory = new StringBuilder(" INSERT INTO mailmtalog (type_log,time,miliSecond,inforAction,action,"
                    + "status,name_email,ip,oIp,ua,"
                    + "elapsed,result,relay,delay,delays,"
                    + "dsn,listReceiveFWD) ");
            StringBuilder queryTailMtaTransHistory = new StringBuilder(" VALUES (?,?,?,?,?,"
                    + "?,?,?,?,?,"
                    + "?,?,?,?,?,"
                    + "?,?) ");
            String sql1 = queryHeadMtaTransHistory.toString() + queryTailMtaTransHistory.toString();
            Session session = (Session) baseHibernateDAO.openThreadSession();
            baseHibernateDAO.beginTransaction();
            PreparedStatement preparedStatement = session.connection().prepareStatement(sql1);
            for (LogEmailMTA msg : lsMsg) {

                CommonUtils.filter(msg.getType_log(), preparedStatement, 1);
                CommonUtils.filter(msg.getTime(), preparedStatement, 2);
                CommonUtils.filter(msg.getMiliSecond(), preparedStatement, 3);
                CommonUtils.filter(msg.getInforAction(), preparedStatement, 4);
                CommonUtils.filter(msg.getAction(), preparedStatement, 5);
                CommonUtils.filter(msg.getStatus(), preparedStatement, 6);
                CommonUtils.filter(msg.getName_email(), preparedStatement, 7);
                CommonUtils.filter(msg.getIp(), preparedStatement, 8);
                CommonUtils.filter(msg.getoIp(), preparedStatement, 9);
                CommonUtils.filter(msg.getUa(), preparedStatement, 10);
                CommonUtils.filter(msg.getElapsed(), preparedStatement, 11);
                CommonUtils.filter(msg.getResult(), preparedStatement, 12);
                CommonUtils.filter(msg.getRelay(), preparedStatement, 13);
                CommonUtils.filter(msg.getDelay(), preparedStatement, 14);
                if (msg.getDelays() != null) {
                    CommonUtils.filter(msg.getDelays(), preparedStatement, 15);
                } else {
                    CommonUtils.filter("", preparedStatement, 15);
                }
                CommonUtils.filter(msg.getDsn(), preparedStatement, 16);
                if(msg.getListReceiveFWD() != null && msg.getListReceiveFWD().size() > 0){
                    CommonUtils.filter(msg.getListReceiveFWD().toString(), preparedStatement, 17);
                }else{
                    CommonUtils.filter("", preparedStatement, 17);
                }
                preparedStatement.addBatch();
            }
            preparedStatement.setFetchSize(INSERT_FETCH_SIZE);
            preparedStatement.executeBatch();
            preparedStatement.close();
            baseHibernateDAO.commitTransaction();
            System.out.println("Insert Thanh Cong insertMailLog");
            LOGGER.info("Insert Thanh Cong insertMailLog");
            

        } catch (Exception ex) {
            
            ex.printStackTrace();
            LOGGER.error(ex.getMessage());
        }
    }

}
