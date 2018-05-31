/*
 * Copyright (C) 2010 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.mail.db;

import com.viettel.common.bo.BasicBO;
import com.viettel.common.util.CommonUtils;
import com.viettel.common.util.Encryption;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.CallableStatement;
import java.sql.Types;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Order;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.type.Type;

/**
 * Lop co ban de thao tac voi CSDL.
 *
 * @author HoangCH, HuyenNV
 * @version 1.0
 * @since 1.0
 */
public class BaseHibernateDAO {

    static final Logger LOGGER = Logger.getLogger(BaseHibernateDAO.class);
    /**
     * Ngon ngu su dung trong sap xep
     */
    private static final String SORTING_LANGUAGE = "Vietnamese";
    //<editor-fold defaultstate="collapsed" desc="Hang, bien, phuong thuc static">
    public static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
    public static final String configFile = "hibernate.cfg.xml";
    public static final String encryptedFile = "hibernate.cfg";
    public static final Configuration configuration = new Configuration();
    private static SessionFactory sessionFactory;

    static {
        try {
            configuration.configure(configFile);
            //loadEncryptedDBConfig(configuration);
            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }

    private static void loadEncryptedDBConfig(Configuration config) throws Exception {
        URL file = Thread.currentThread().getContextClassLoader().getResource(encryptedFile);
        String decryptString = Encryption.decryptFile(URLDecoder.decode(file.getPath()));
        //System.out.println(decryptString);
        String[] properties = decryptString.split("\r\n");
        for (String s : properties) {
            //System.out.println(s);
            String temp[] = s.split("=", 2);
            if (temp.length == 2) {
                //config.setProperty(temp[0], temp[1]);
            }
        }
    }
    //</editor-fold>

    /**
     * @return the sessionFactory
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    //<editor-fold defaultstate="collapsed" desc="Cac phuong thuc co ban">
    private Session getSession() throws HibernateException {
        return (Session) threadLocal.get();
    }

    public void beginTransaction() {
        getSession().beginTransaction();
    }

    public void commitTransaction() {
        Session session = getSession();
        if (session != null && session.isOpen() && session.getTransaction().isActive()) {
            session.getTransaction().commit();
        }
    }

    public void rollbackTransaction() {
        // Rollback when error occurs
        Session session = getSession();
        if (session != null && session.isOpen() && session.getTransaction().isActive()) {
            session.getTransaction().rollback();
            session.clear();
        }

        // No matter what close session too..
        closeSession();
    }

    public Session openThreadSession() throws Exception {
        Session session = (Session) threadLocal.get();
        if (session != null) {
            if (session.isOpen()) {
                session.close();
            }
            threadLocal.remove();
        }
        if (session == null || !session.isOpen()) {
            if (getSessionFactory() == null) {
                rebuildSessionFactory();
            }
            session = getSessionFactory() == null ? null : ((Session) (getSessionFactory().openSession()));
            threadLocal.set(session);
        }
        return session;
    }

    public void rebuildSessionFactory() {
        configuration.configure(configFile);
        sessionFactory = configuration.buildSessionFactory();
    }

    public void closeSession() throws HibernateException {
        Session session = (Session) threadLocal.get();
        threadLocal.set(null);
        if (session != null && session.isOpen()) {
            session.close();
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Tim kiem">
    public <T> List<T> getAll(Class<T> tableName, String orderColumn) {
        Session session = getSession();
        String hql = " FROM " + tableName.getName() + " ORDER BY " + orderColumn;
        Query query = session.createQuery(hql);
        return query.list();
    }

    public <T> List<T> findByProperty(Class<T> tableName, String propertyName, Object value, String orderClause) {
        String hql = " FROM " + tableName.getName() + " t WHERE t." + propertyName + " = ? ";
        if (!CommonUtils.isBlankOrNull(orderClause)) {
            hql += " ORDER BY " + orderClause;
        }
        Query query = getSession().createQuery(hql);
        query.setParameter(0, value);
        return query.list();
    }
    
    /**
     * Tìm theo các khóa ngoại
     * @param <T>
     * @param tableName
     * @param pairs
     * @return Trả về danh sách bản ghi trong theo hai khóa ngoại trở lên
     */
    public <T> List<T> findByForeignKey(Class<T> tableName, Object ... pairs) {
        String hql = " FROM " + tableName.getName() + " t WHERE 1 = 1 ";
               
        if(pairs != null && pairs.length % 2 == 0) {
            int index = 0;
            for(Object obj : pairs) {
                if(index % 2 == 0) {
                    hql += " AND t." + (String)obj + " = ";
                } else {
                    hql += (Long)obj;
                }
                index++;
            }
        }        
        Query query = getSession().createQuery(hql);        
        return query.list();
    }

    public <T> List<T> findByIds(Class<T> tableName, String idColumn, String ids, String orderColumn) {
        StringBuilder hql = new StringBuilder(" FROM " + tableName.getName() + " t ");
        if (!ids.isEmpty()) {
            hql.append(" WHERE t.").append(idColumn).append(" IN (:ids) ");
        }
        if (!CommonUtils.isBlankOrNull(orderColumn)) {
            hql.append(" ORDER BY ").append("t.").append(orderColumn);
        }
        Query query = getSession().createQuery(hql.toString());
        if (!ids.isEmpty()) {
            query.setParameterList("ids", CommonUtils.string2ListLong(ids, ","));
        }
        return query.list();
    }

    public <T> List<T> findByIds(Class<T> tableName, String idColumn, List ids, String... orderColumn) {
        StringBuilder hql = new StringBuilder(" FROM " + tableName.getName() + " t ");
        List<List> parList = null;
        int parSize = 0;
        if (!ids.isEmpty()) {
            parList = CommonUtils.partition(ids, 999);
            parSize = parList.size();
            hql.append(" WHERE 1=1 ");
            for (int i = 0; i < parSize; i++) {
                hql.append(" AND t.").append(idColumn).append(" IN (:ids_").append(i).append(") ");
            }
        }
        int length = orderColumn.length;
        if (length > 0) {
            hql.append(" ORDER BY ");
            for (int i = 0; i < length; i++) {
                if (!CommonUtils.isBlankOrNull(orderColumn[i])) {
                    hql.append(i > 0 ? ", " : "").append("t.").append(orderColumn[i]);
                }
            }
        }
        Query query = getSession().createQuery(hql.toString());
        if (!ids.isEmpty()) {
            for (int i = 0; i < parSize; i++) {
                query.setParameterList("ids_" + i, parList.get(i));
            }
        }
        return query.list();
    }

    public <T> T get(Class<T> entityClass, Serializable id) throws Exception {
        return get(entityClass, id, null);
    }

    /**
     * Can bo di (NEED DELETE)
     */
    public <T> T get(final Class<T> entityClass, final Serializable id, final LockMode lockMode) {
        if (lockMode != null) {
            return (T) getSession().get(entityClass, id, lockMode);
        } else {
            return (T) getSession().get(entityClass, id);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Luu vao DB">
    public void save(BasicBO objectToSave) {
        getSession().save(objectToSave);
    }

    public void update(Object entity) {
        getSession().update(entity);
    }

    public void saveOrUpdate(final Object entity) {
        getSession().saveOrUpdate(entity);

    }

    /**
     * Can bo di (NEED DELETE)
     */
    public void saveOrUpdateAll(final Collection entities) {
        for (Object entity : entities) {
            getSession().saveOrUpdate(entity);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Delete">
    public void delete(Object entity) {
        getSession().delete(entity);
    }

    public void deleteByIds(List<Long> arrId, Class className, String idColumn) {
        if ((arrId != null) && !arrId.isEmpty()) {
            StringBuilder hql = new StringBuilder(" DELETE FROM " + className.getName() + " t WHERE 1 = 1 ");
            List<List<Long>> parList = CommonUtils.partition(arrId, 999);
            int parSize = parList.size();
            if (parSize > 0) {
                for (int i = 0; i < parSize; i++) {
                    hql.append(" AND t.").append(idColumn).append(" IN (:ids_").append(i).append(") ");
                }
                Query query = createQuery(hql.toString());
                for (int i = 0; i < parSize; i++) {
                    query.setParameterList("ids_" + i, parList.get(i));
                }
                query.executeUpdate();
            }
        }
    }

    public void deleteByIds(Long[] arrId, Class className, String idColumn) {
        if ((arrId != null) && (arrId.length > 0)) {
            String hql = " DELETE FROM " + className.getName() + " t WHERE t." + idColumn + " IN (:arrId) ";
            Query query = createQuery(hql);
            query.setParameterList("arrId", arrId);
            query.executeUpdate();
        }
    }

    public void deleteById(Long id, Class className, String idColumn) {
        String hql = " DELETE FROM " + className.getName() + " t WHERE t." + idColumn + " = ? ";
        Query query = createQuery(hql);
        query.setParameter(0, id);
        query.executeUpdate();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Cac phuong thuc chung">
    public void flushSession() {
        getSession().flush();
        getSession().clear();
    }

    public Query createQuery(String hql) {
        return getSession().createQuery(hql);
    }

    public SQLQuery createSQLQuery(String sql) {
        return getSession().createSQLQuery(sql);
    }
    
    public static SQLQuery createStaticSQLQuery(String sql) {
        return ((Session) threadLocal.get()).createSQLQuery(sql);
    }

    public Criteria createCriteria(Class type) {
        return getSession().createCriteria(type);
    }

    public void setReadOnly(Object o, boolean flag) {
        getSession().setReadOnly(o, flag);
    }

    public CallableStatement prepareCall(String sql) throws Exception {
        return getSession().connection().prepareCall(sql);
    }

    public long getSequence(String sequenceName) throws Exception {
        String sql = "SELECT " + sequenceName + " .NextVal FROM Dual";
        Query query = getSession().createSQLQuery(sql);
        BigDecimal bigDecimal = (BigDecimal) query.uniqueResult();
        return bigDecimal.longValue();
    }

    public boolean duplicate(Class className, String idColumn, Long idValue, String codeColumn, String codeValue) {
        String hql = " SELECT COUNT(*) FROM " + className.getName() + " t WHERE LOWER(t." + codeColumn + ") = ? ";
        if (idValue != null) {
            hql += " AND t." + idColumn + " != ? ";
        }
        Query query = createQuery(hql);
        query.setParameter(0, codeValue.trim().toLowerCase());
        if (idValue != null) {
            query.setParameter(1, idValue);
        }
        query.setMaxResults(1);
        Long count = (Long) query.uniqueResult();
        return count > 0;
    }

    public boolean hasConstraint(Class className, String idColumn, Long idValue) {
        String hql = " SELECT COUNT(*) FROM " + className.getName() + " t WHERE t." + idColumn + " = ? ";
        Query query = createQuery(hql);
        query.setParameter(0, idValue);
        query.setMaxResults(1);
        Long count = (Long) query.uniqueResult();
        return count > 0;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Cac ham tien ich dung khi Order theo Unicode">
    public static String buildOrderString(String... columns) {
        StringBuilder fragment = new StringBuilder();
        for (int i = 0; i < columns.length; i++) {
            if (SORTING_LANGUAGE.equals("")) {
                fragment.append(columns[i]);
            } else {
                fragment.append(" nlssort(").append(columns[i]).append(",'NLS_SORT=").append(SORTING_LANGUAGE).append("') ");
            }
            fragment.append(" asc");
            if (i < columns.length - 1) {
                fragment.append(", ");
            }
        }

        return fragment.toString();
    }

    public Order asc(String propertyName) {
        return new CustomOrder(propertyName, true);
    }

    public Order ascNullsFirst(String propertyName) {
        return new CustomOrder(propertyName, true, true);
    }

    public Order ascNullsLast(String propertyName) {
        return new CustomOrder(propertyName, true, false);
    }

    public Order desc(String propertyName) {
        return new CustomOrder(propertyName, false);
    }

    public Order descNullsFirst(String propertyName) {
        return new CustomOrder(propertyName, false, true);
    }

    public Order descNullsLast(String propertyName) {
        return new CustomOrder(propertyName, false, false);
    }

    public class CustomOrder extends Order implements Serializable {

        private boolean ascending;
        private boolean ignoreCase;
        private String propertyName;
        private boolean nulls = false; //nulls first nulls last

        @Override
        public String toString() {
            return propertyName + ' ' + (ascending ? "asc" : "desc");
        }

        @Override
        public CustomOrder ignoreCase() {
            ignoreCase = true;
            return this;
        }

        /**
         * Constructor for CustomOrder.
         */
        protected CustomOrder(String propertyName, boolean ascending) {
            super(propertyName, ascending);
            this.propertyName = propertyName;
            this.ascending = ascending;
        }

        protected CustomOrder(String propertyName, boolean ascending, boolean nulls) {
            super(propertyName, ascending);
            this.propertyName = propertyName;
            this.ascending = ascending;
            this.nulls = nulls;
        }

        /**
         * Render the SQL fragment
         *
         */
        @Override
        public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery)
                throws HibernateException {
            String[] columns = criteriaQuery.getColumnsUsingProjection(criteria, propertyName);
            Type type = criteriaQuery.getTypeUsingProjection(criteria, propertyName);
            StringBuilder fragment = new StringBuilder();
            for (int i = 0; i < columns.length; i++) {
                SessionFactoryImplementor factory = criteriaQuery.getFactory();
                boolean lower = ignoreCase && type.sqlTypes(factory)[i] == Types.VARCHAR;

                if (lower) {
                    fragment.append(factory.getDialect().getLowercaseFunction()).append('(');
                }

                if (SORTING_LANGUAGE.equals("")) {
                    fragment.append(columns[i]);
                    fragment.append(ascending ? " asc" : " desc");
                } else {
                    fragment.append(" nlssort(").append(columns[i]).append(",'NLS_SORT=").append(SORTING_LANGUAGE).append("')");
                    fragment.append(ascending ? " asc" : " desc");
                    fragment.append(" nulls ").append(this.nulls == false ? "first" : "last");
                }

                if (lower) {
                    fragment.append(')');
                }

                if (i < columns.length - 1) {
                    fragment.append(", ");
                }
            }
            return fragment.toString();
        }
    }
    //</editor-fold>
}
