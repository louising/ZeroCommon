package com.zero.core.jdbc.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.zero.core.jdbc.model.Column;
import com.zero.core.jdbc.model.Record;
import com.zero.core.jdbc.model.Result;
import com.zero.core.jdbc.model.Table;
import com.zero.core.util.BaseUtils;

/**
 * Generic utility methods for working with JDBC.
 * 
 * //Note: ABCD is just support on Oracle 10 DatabaseMetaData mm = con.getMetaData(); //dbMeta int A = mm.getJDBCMajorVersion();
 * //10 unable in Oracle-JDBC_9.0.1.1.0 int B = mm.getJDBCMinorVersion(); //2 unable in Oracle-JDBC_9.0.1.1.0 int C =
 * mm.getDatabaseMajorVersion(); //10 unable in Oracle-JDBC_9.0.1.1.0 int D = mm.getDatabaseMinorVersion(); //2 unable in
 * Oracle-JDBC_9.0.1.1.0
 * 
 * String E = mm.getDatabaseProductName(); //Oracle //Oracle Database 10g Enterprise Edition Release 10.2.0.1.0 - Production With
 * the Partitioning, OLAP and Data Mining options String F = mm.getDatabaseProductVersion(); String G = mm.getDriverName(); //Oracle
 * JDBC driver String H = mm.getDriverVersion(); //10.2.0.2.0 //or old: //9.0.1.1.0 int I = mm.getDriverMajorVersion(); //10 int J =
 * mm.getDriverMinorVersion(); //2
 * 
 * System.out.println(A + "," + B + "," + C + "," + D + "," + E + "," + F + "," + G + "," + H + "," + I + "," + J);
 * 
 * @author Louis
 * @date 2007-4-5
 */
public class JdbcUtils {
    private static boolean isDebug = false;

    /**
     * Retrieve a JDBC column value from a ResultSet
     * <pre>
     * Blob: byte[]
     * Clob: String
     * BigInt
     * Bit, SmallInt, Integer
     * Double, Real
     * Numeric, Decimal: BigDecimal
     * Date, Timestamp
     * Char, Varchar
     * Blob, Clob (Oracle)
     * Other (HSql)
     * </pre>
     * @param rs is the ResultSet holding the data
     * @param columnIndex column index based on 1;
     * @return the FieldValue object that hold String value and Object value
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object getResultSetFieldValue(ResultSet rs, int columnIndex) throws SQLException, IOException,
            ClassNotFoundException {
        Object fieldValue = rs.getObject(columnIndex);
        //System.out.println(rs.getMetaData().getColumnName(columnIndex) + ": " + rs.getObject(columnIndex));
        /*        if (fieldValue != null)
                    System.out.println(rs.getMetaData().getColumnName(columnIndex) + ":" + fieldValue.getClass());
                else
                    System.out.println("[DEBUG] ResultSet.FieldValue: null");*/

        if (fieldValue == null)
            return null;

        int type = rs.getMetaData().getColumnType(columnIndex);
        if (type == Types.TIMESTAMP || type == Types.DATE || type == -100) {
            //For Oracle 9, when type is Types.TIMESTAMP(93), return -100
            String columnClassName = rs.getMetaData().getColumnClassName(columnIndex);
            if (columnClassName.equals("java.sql.Date") || columnClassName.equals("oracle.sql.DATE")
                    || rs.getMetaData().getColumnTypeName(columnIndex).equals("DATE")
                    || rs.getObject(columnIndex).getClass().getName().equals("java.sql.Date"))
                fieldValue = rs.getDate(columnIndex);
            else
                fieldValue = rs.getTimestamp(columnIndex);
        } else if (type == Types.BLOB) {
            //rs.getObject(i); //oracle.sql.BLOB; a pointer, be avaiable even in other Connection to the same DB             
            Blob blob = rs.getBlob(columnIndex);
            if (blob != null) {
                byte[] bytes = null;
                BufferedInputStream in = new BufferedInputStream(blob.getBinaryStream());
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int b;
                while ((b = in.read()) != -1)
                    out.write(b);
                bytes = out.toByteArray();
                in.close();
                out.close();

                fieldValue = bytes;

                /* method(2) - just fit Oracle 10g, when convert to Object, a error occurs (invalid head): 
                  byte[] bytes = rs.getBytes(index);
                  java.io.StreamCorruptedException: invalid stream header                              
                 */
            }
        } else if (type == Types.CLOB) {
            Clob clob = rs.getClob(columnIndex);
            if (clob != null) {
                BufferedReader in = new BufferedReader(clob.getCharacterStream());
                int len = (int) clob.length();
                char[] buffer = new char[len];
                in.read(buffer);
                in.close();
                fieldValue = new String(buffer);
            }
        } else if (type == Types.BIGINT) {
            fieldValue = rs.getLong(columnIndex);
        } else if (type == Types.INTEGER) {
            fieldValue = rs.getInt(columnIndex);
        } else if (type == Types.DOUBLE) {
            fieldValue = rs.getDouble(columnIndex);
        } else if (type == Types.NUMERIC || type == Types.DECIMAL) {
            fieldValue = rs.getBigDecimal(columnIndex);
        }

        return fieldValue;
    }

    /**
     * Convert ResultSet into Result; never null
     * 
     * @param rs
     * @return
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Result getResult(ResultSet rs) throws SQLException, IOException, ClassNotFoundException {
        ResultSetMetaData rsm = rs.getMetaData();
        //get columns
        List<Column> columns = new ArrayList<Column>();
        for (int i = 1; i <= rsm.getColumnCount(); i++) {

            String name = rsm.getColumnName(i);
            //System.out.println(name + ": " + rsm.getColumnDisplaySize(i));

            int type = rsm.getColumnType(i);
            String typeName = rsm.getColumnTypeName(i);
            String className = rsm.getColumnClassName(i);

            if (type == Types.TIMESTAMP || type == Types.DATE || type == -100 || className.startsWith("oracle.sql.TIMESTAMP")
                    || className.startsWith("oracle.sql.DATE"))
                type = Types.TIMESTAMP;
            columns.add(new Column(name, type, typeName, className));
        }

        //get records;
        List<Record> records = new ArrayList<Record>();

        while (rs.next()) {
            List<Object> fieldValues = new ArrayList<Object>();
            for (int i = 1; i <= rsm.getColumnCount(); i++)
                fieldValues.add(getResultSetFieldValue(rs, i));

            records.add(new Record(columns, fieldValues));
        }

        return new Result(columns, records);
    }

    public static void insertTestData(Table table, int count, DataSource ds) {
        Connection con = null;
        try {
            con = ds.getConnection();
            if (isDebug)
                System.out.println("AutoCommit: " + con.getAutoCommit());

            if (con.getAutoCommit())
                con.setAutoCommit(false);

            PreparedStatement pstmt = con.prepareStatement("delete from " + table.tableName);
            pstmt.execute();
            pstmt.close();

            String sql = "INSERT INTO %s (%s) VALUES (%s)";
            sql = String.format(sql, table.tableName, table.getColumnNames(), table.getColumnNamePlaceholders());

            pstmt = con.prepareStatement(sql);
            int init = 100;
            for (int i = 0; i < count; i++) {
                Timestamp t = new Timestamp(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * i);
                Date d = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * i);

                for (int j = 1; j <= table.columns.size(); j++) {
                    int type = table.columns.get(j - 1).type;
                    if (type == Types.BIGINT)
                        pstmt.setLong(j, init * 10 + i);
                    else if (type == Types.INTEGER || type == Types.SMALLINT || type == Types.TINYINT)
                        pstmt.setInt(j, init + i);
                    else if (type == Types.CHAR || type == Types.VARCHAR)
                        pstmt.setString(j, BaseUtils.nextString(5));
                    else if (type == Types.DOUBLE || type == Types.REAL)
                        pstmt.setDouble(j, 0.618);
                    else if (type == Types.DECIMAL || type == Types.NUMERIC)
                        pstmt.setBigDecimal(j, BigDecimal.valueOf(init + i));
                    else if (type == Types.DATE)
                        pstmt.setDate(j, d);
                    else if (type == Types.TIMESTAMP)
                        pstmt.setTimestamp(j, t);
                    else if (type == Types.BOOLEAN)
                        pstmt.setBoolean(j, (i % 2 == 0) ? true : false);
                    else if (type == Types.OTHER)
                        pstmt.setObject(j, new String("Z" + i));
                    else if (type == Types.CLOB) {
                        pstmt.setString(j, "Dear Developer Tools Group customers, partners and fans,");
                    } else if (type == Types.BLOB) {
                        byte[] bytes = BaseUtils.getBytes(new String("Z" + 100 * i));
                        pstmt.setBytes(j, bytes);
                    }
                }
                pstmt.execute();
            }
            pstmt.close();
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(con);
        }
    }

    static void prtColumnInfo(ResultSet rs, int columIndex) throws SQLException {
        Object obj = rs.getObject(columIndex);
        String str = String.format("%-14s getObject(i).class: %s", rs.getMetaData().getColumnName(columIndex), obj.getClass());
        System.out.println(str);
    }

    // In Oracle, Statement can just execute a sql without ';' or to use Statement.addBatch(sql)
    public static boolean executeUpdate(String sql, DataSource datasource) {
        if (isDebug)
            System.out.println("Sql: \n" + sql);

        boolean result = true;
        Connection con = null;
        try {
            con = datasource.getConnection();
            if (!con.getAutoCommit())
                con.setAutoCommit(true);

            Statement stmt = con.createStatement();
            String[] sqls = sql.split(";");
            for (String aSql : sqls)
                stmt.addBatch(aSql);
            stmt.executeBatch();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
        } finally {
            close(con);
        }

        return result;
    }

    public static void close(Connection con) {
        if (con != null)
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    /** Specifial for Hsql DB, for memory DB, need shutdown to save data */
    public static void shutdown(Connection con) {
        try {
            if (con != null && !con.isClosed()) {
                //for Hsql (jdbc:hsqldb)
                Statement stat = con.createStatement();
                stat.execute("SHUTDOWN");
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}