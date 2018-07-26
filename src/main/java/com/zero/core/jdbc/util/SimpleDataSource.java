package com.zero.core.jdbc.util;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

/*
//Oracle        
String driverClass = "oracle.jdbc.driver.OracleDriver";
String url = "jdbc:oracle:thin:@192.168.1.110:1521:xmldb";
String user = "neohkdev";
String pwd = "xml";
oracleDatasource = new SimpleDataSource(driverClass, url, user, pwd); 
*/

/*
//Derby datasource
String driverClass = "org.apache.derby.jdbc.EmbeddedDriver";
String url = "jdbc:derby:C:/Alfresco/alf_data/derby_data/alfresco;create=false";
String url = "jdbc:derby:c:/tmp/derby_db;create=false";
String url = "jdbc:derby:c:/tmp/toursdb";
String user = ""; //alfresco
String pwd = "";
derbyDatasource = new SimpleDataSource(driverClass, url, user, pwd);
*/

/*
//Hsql        
String driverClass = "org.hsqldb.jdbcDriver";
String url = "jdbc:hsqldb:hsql://localhost/xdb";  //Hsqldb Server
String url = "jdbc:hsqldb:c:\db\test";            //In-Process (Standalone)
String url = "jdbc:hsqldb:mem:xdb"; //Memory-Only
String url = "jdbc:hsqldb:file:E:/downloads/SmartClient_571_Evaluation/smartclientSDK/WEB-INF/db/hsqldb/isomorphic";

String user = "sa";
String pwd = "";
hsqlDatasource = new SimpleDataSource(driverClass, url, user, pwd);
*/

/**
 * Simple implementation of the standard JDBC <code>DataSource</code> interface,  
 * configuring a plain old JDBC Driver via bean properties, and returning a new <code>Connection</code>
 * for every <code>getConnection()</code> call.
 *
 * @author Louis
 * @date 2007-4-5
 */
public class SimpleDataSource implements DataSource {
    private String driverClassName;
    private String url;
    private String username;
    private String password;

    public SimpleDataSource(String driverClassName, String url, String username, String password) {
        setDriverClassName(driverClassName);
        setUrl(url);
        setUsername(username);
        setPassword(password);
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public Connection getConnection(String username, String password) throws SQLException {
        return DriverManager.getConnection(this.url, username, password);
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName.trim();

        this.driverClassName = driverClassName.trim();
        try {
            Class.forName(this.driverClassName);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    public void setLogWriter(PrintWriter out) throws SQLException {
    }

    public void setLoginTimeout(int seconds) throws SQLException {
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
