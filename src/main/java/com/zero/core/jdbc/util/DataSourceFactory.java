package com.zero.core.jdbc.util;

import javax.sql.DataSource;

/**
 * A factory for <code>DataSource</code> that connections to a physical data source. 
 * A DataSource is an alternative to the <code>DriverManager</code> facility, 
 * it is the preferred means of getting a connection. 
 * 
 * @author Louis
 * @date 2007-7-7
 */
public class DataSourceFactory {
    public static DataSource h2Datasource;
    public static DataSource hsqlDatasource;
    public static DataSource oracleDatasource;
    public static DataSource derbyDatasource;

    static {
        String driverClass = "org.h2.Driver";
        String url = "jdbc:h2:tcp://localhost/~/H2DB-SpringRestDemo";
        String user = "sa";
        String pwd = "sa";
    
        h2Datasource = new SimpleDataSource(driverClass, url, user, pwd);         
    }
    
    static {
        /*
        //Oracle        
        String driverClass = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@192.168.1.110:1521:xmldb";
        String user = "neohkdev";
        String pwd = "xml";
        oracleDatasource = new SimpleDataSource(driverClass, url, user, pwd); 
        */
    }
    
    static {
        /*
        //Derby datasource
        String driverClass = "org.apache.derby.jdbc.EmbeddedDriver";
        //String url = "jdbc:derby:C:/Alfresco/alf_data/derby_data/alfresco;create=false";
        //String url = "jdbc:derby:c:/tmp/derby_db;create=false";
        String url = "jdbc:derby:c:/tmp/toursdb";
        String user = ""; //alfresco
        String pwd = "";
        derbyDatasource = new SimpleDataSource(driverClass, url, user, pwd);
        */
    }

    static {
        /*
        //Hsql        
        String driverClass = "org.hsqldb.jdbcDriver";
        //String url = "jdbc:hsqldb:hsql://localhost/xdb";  //Hsqldb Server
        //String url = "jdbc:hsqldb:c:\db\test";            //In-Process (Standalone)
        String url = "jdbc:hsqldb:mem:xdb"; //Memory-Only
        //String url = "jdbc:hsqldb:file:E:/downloads/SmartClient_571_Evaluation/smartclientSDK/WEB-INF/db/hsqldb/isomorphic";

        String user = "sa";
        String pwd = "";
        hsqlDatasource = new SimpleDataSource(driverClass, url, user, pwd);
        */
    }
}
