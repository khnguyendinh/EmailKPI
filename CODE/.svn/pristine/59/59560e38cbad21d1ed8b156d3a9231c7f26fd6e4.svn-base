/*
 * Copyright (C) 2010 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.mail.db;

import com.viettel.common.bean.ImportConfigBean;
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

    private static final Logger LOGGER = Logger.getLogger(QueryUtils.class);

    public QueryUtils() {
    }

    public void insertMtaIntoDatabase(List<Object[]> dataList) throws Exception {

        StringBuilder queryHead = new StringBuilder(" INSERT INTO mta_transfer (msg_id,sender,subject,file_attact,sent_date,receiver) ");
        StringBuilder queryTail = new StringBuilder(" VALUES (?,?,?,?,?,?) ");
        String sql = queryHead.toString() + queryTail.toString();
        Session session = (Session) baseHibernateDAO.openThreadSession();
        baseHibernateDAO.beginTransaction();
        PreparedStatement preparedStatement = session.connection().prepareStatement(sql);

        for (Object[] a : dataList) {
            try {
                if (a[5].equals(1)) {
                    List<Address> receivers = (List<Address>) a[7];
                    if (receivers != null && receivers.size() > 0) {
                        for (Address receiver : receivers) {
                            CommonUtils.filter(a[0], preparedStatement, 1);
                            CommonUtils.filter(a[1], preparedStatement, 2);
                            CommonUtils.filter(a[2], preparedStatement, 3);
                            CommonUtils.filter(a[3], preparedStatement, 4);
                            CommonUtils.filter(a[4], preparedStatement, 5);
                            CommonUtils.filter(((InternetAddress) receiver).getAddress(), preparedStatement, 6);
                            preparedStatement.addBatch();
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("--------------------------------");
                System.out.println(a[7]);
                List<Address> receivers = (List<Address>) a[7];
                System.out.println(receivers.size());
            }

        }
        preparedStatement.setFetchSize(INSERT_FETCH_SIZE);
        preparedStatement.executeBatch();
        preparedStatement.close();
        baseHibernateDAO.commitTransaction();
    }

    public void insertMtaTransHistory(List<Object[]> dataList) throws Exception {
        StringBuilder queryHeadMtaTransHistory = new StringBuilder(" INSERT INTO mta_trans_history (msg_id,status_msg,msg_name) ");
        StringBuilder queryTailMtaTransHistory = new StringBuilder(" VALUES (?,?,?) ");
        String sql1 = queryHeadMtaTransHistory.toString() + queryTailMtaTransHistory.toString();
        Session session = (Session) baseHibernateDAO.openThreadSession();
        baseHibernateDAO.beginTransaction();
        PreparedStatement preparedStatement = session.connection().prepareStatement(sql1);
        for (Object[] a : dataList) {
            CommonUtils.filter(a[0], preparedStatement, 1);
            CommonUtils.filter(a[5], preparedStatement, 2);
            CommonUtils.filter(a[6], preparedStatement, 3);
            preparedStatement.addBatch();
        }
        preparedStatement.setFetchSize(INSERT_FETCH_SIZE);
        preparedStatement.executeBatch();
        preparedStatement.close();
        baseHibernateDAO.commitTransaction();
    }

    public void insertMtaTransDetail(List<Object[]> dataList) throws Exception {
        StringBuilder queryHeadMtaTransDetail = new StringBuilder(" INSERT INTO mta_trans_detail (receiver,mta_trans_id) ");
        StringBuilder queryTailMtaTransDetail = new StringBuilder(" VALUES (?,?) ");
        String sql1 = queryHeadMtaTransDetail.toString() + queryTailMtaTransDetail.toString();
        Session session = (Session) baseHibernateDAO.openThreadSession();
        baseHibernateDAO.beginTransaction();
        PreparedStatement preparedStatement = session.connection().prepareStatement(sql1);
        for (Object[] a : dataList) {
            CommonUtils.filter(a[7], preparedStatement, 1);
            CommonUtils.filter(a[8], preparedStatement, 2);
            preparedStatement.addBatch();
        }
        preparedStatement.setFetchSize(INSERT_FETCH_SIZE);
        preparedStatement.executeBatch();
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
    private static final Integer INSERT_FETCH_SIZE = 1000;

}
