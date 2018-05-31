/*
 * Copyright (C) 2010 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.common.util;


import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.mail.Address;
import javax.mail.internet.InternetAddress;


/**
 * Cac method dung chung cua he thong.
 *
 * @author HuyenNV
 * @ModifiedBy: HoangCH,PhucNH
 * @since 1.0
 * @version 1.0
 */
public class CommonUtils {
    
    public static String convertAddessesToString(List<Address> adds){
        if (adds == null) return "";
        String result="";
        for (Address add:adds){
            result+=((InternetAddress) add).getAddress()+",";
        }
        result = result.substring(0, result.length()-1);
        return result;
    }
    
    public static boolean isBlankOrNull(String str) {
        return (str == null || str.trim().isEmpty());
    }
    /**
     * Kiem tra lon hon hoac bang.
     *
     * @param obj So
     * @return void
     */
    public static void filterGe(Object obj, StringBuilder queryString, List<Object> paramList, String field) {
        if (obj != null) {
            queryString.append(" AND ").append(field).append(" >= ? ");
            paramList.add(obj);
        }
    }

    /**
     * Kiem tra nho hon hoac bang.
     *
     * @param obj So
     * @return void
     */
    public static void filterLe(Object obj, StringBuilder queryString, List<Object> paramList, String field) {
        if (obj != null) {
            queryString.append(" AND ").append(field).append(" <= ? ");
            paramList.add(obj);
        }
    }

    /**
     * filter for inserting preparedStatement
     *
     * @param value
     * @param field
     * @param preparedStatement
     * @param insertClause
     * @param valueClause
     * @param count
     * @throws Exception
     */
    public static void filter(String value, PreparedStatement preparedStatement, int index)
            throws Exception {

        if (value != null) {
            preparedStatement.setString(index, value.trim());
        } else {
            preparedStatement.setNull(index, java.sql.Types.NULL);
        }
    }
    public static void filterNString(String value, PreparedStatement preparedStatement, int index)
            throws Exception {

        if (value != null) {
            preparedStatement.setObject(index,  value.trim(),java.sql.Types.NVARCHAR);
        } else {
            preparedStatement.setNull(index, java.sql.Types.NULL);
        }
    }
    public static void filter(Double value, PreparedStatement preparedStatement, int index)
            throws Exception {

        if (value != null) {
            preparedStatement.setDouble(index, value);
        } else {
            preparedStatement.setNull(index, java.sql.Types.NULL);
        }
    }

    public static void filter(Long value, PreparedStatement preparedStatement, int index)
            throws Exception {

        if (value != null) {
            preparedStatement.setLong(index, value);
        } else {
            preparedStatement.setNull(index, java.sql.Types.NULL);
        }
    }

    public static void filter(Object value, PreparedStatement preparedStatement, int index)
            throws Exception {
        if (value != null) {
            if (value instanceof Date) {
                Date temp = (Date) value;
                preparedStatement.setObject(index, new java.sql.Timestamp(temp.getTime()));
            } else {
                preparedStatement.setObject(index, value);
            }

        } else {
            preparedStatement.setNull(index, java.sql.Types.NULL);
        }
    }

    public static void filter(java.sql.Date value, PreparedStatement preparedStatement, int index)
            throws Exception {

        if (value != null) {
            preparedStatement.setDate(index, value);
        } else {
            preparedStatement.setNull(index, java.sql.Types.NULL);
        }
    }

    /**
     * Neu s null thi tra ve xau rong, neu khong thi tra ve gia tri cua s.
     *
     * @param s Xau truyen vao
     * @return Neu s null thi tra ve xau rong, neu khong thi tra ve gia tri cua
     * s
     */
    public static String nvl(String s) {
        return s == null ? "" : s;
    }
    public static <T> List<List<T>> partition(List<T> list, int size) {

        if (list == null) {
            throw new NullPointerException(
                    "'list' must not be null");
        }
        if (!(size > 0)) {
            throw new IllegalArgumentException(
                    "'size' must be greater than 0");
        }

        return new Partition<T>(list, size);
    }
      /**
     * Chuyen string -> List Long
     *
     * @param inpuString
     * @param separator
     * @return
     */
    public static List<Long> string2ListLong(String inpuString, String separator) {
        List<Long> outPutList = new ArrayList<Long>();

        if (inpuString != null && !"".equals(inpuString.trim()) && separator != null && !"".equals(separator.trim())) {
            String[] idArr = inpuString.split(separator);
            for (int i = 0; i < idArr.length; i++) {
                if (idArr[i] != null && !"".equals(idArr[i].trim())) {
                    outPutList.add(Long.parseLong(idArr[i].trim()));
                }
            }
        }

        return outPutList;
    }
}
