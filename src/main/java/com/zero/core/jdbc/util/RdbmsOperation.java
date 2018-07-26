package com.zero.core.jdbc.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.zero.core.jdbc.model.Result;
import com.zero.core.util.BaseUtils;

/**
 * Execute a update sql
 * 
 * <pre>
 * RdbmsOperation dao = new RdbmsOperation(datasource);
 * dao.setSql(&quot;update table1 set name=? where id=?&quot;);
 * dao.addParameter(Types.VARCHAR, &quot;Mike&quot;);
 * dao.addParameter(Types.INTEGER, 1001);
 * dao.executeUpdate();
 * 
 * RdbmsOperation dao = new RdbmsOperation(datasource); 
 * dao.executeUpdate(&quot;INSERT INTO table1(id, name) VALUES (1, &quot;OK&quot;)&quot;);
 * </pre>
 * 
 * @author Louis
 * @date 2007-4-23
 */
public class RdbmsOperation {
    private final DataSource datasource;

    private List<Parameter> parameters = new ArrayList<Parameter>();

    private String sql;

    public RdbmsOperation(DataSource datasource) {
        this.datasource = datasource;
    }

    @SuppressWarnings("rawtypes")
    private void setParameters(PreparedStatement preStmt) throws SQLException, IOException {
        for (int i = 0; i < this.parameters.size(); i++) {
            Parameter p = this.parameters.get(i);
            if (p.type == Types.INTEGER)
                preStmt.setInt(i + 1, (Integer) p.value);
            else if (p.type == Types.BIGINT)
                preStmt.setLong(i + 1, (Long) p.value);
            else if (p.type == Types.DOUBLE || p.type == Types.DECIMAL || p.type == Types.NUMERIC)
                preStmt.setDouble(i + 1, (Double) p.value);
            else if (p.type == Types.BOOLEAN)
                preStmt.setBoolean(i + 1, (Boolean) p.value);
            else if (p.type == Types.TIMESTAMP)
                preStmt.setTimestamp(i + 1, (Timestamp) p.value);
            else if (p.type == Types.DATE)
                preStmt.setDate(i + 1, (java.sql.Date) p.value);
            else if (p.type == Types.OTHER) { // For HSql.Other --> Object
                if (p.value instanceof java.io.Serializable)
                    preStmt.setObject(i + 1, p.value);
                else
                    throw new IOException("The object must be serializable !");
            } else if (p.type == Types.BLOB) {
                byte[] bytes = null;
                Class cls = p.value.getClass();
                if (cls.isArray() && cls.getComponentType().getSimpleName().equals("byte"))
                    bytes = (byte[]) p.value;
                else if (p.value instanceof java.io.Serializable)
                    bytes = BaseUtils.getBytes((java.io.Serializable) p.value);
                else
                    throw new IOException("The object must be serializable !");

                preStmt.setBytes(i + 1, bytes);
            } else if (p.type == Types.CLOB || p.type == Types.VARCHAR || p.type == Types.CHAR)
                preStmt.setString(i + 1, (String) p.value);
        }
    }

    /**
     * Execute a update sql that with the parameters that set by
     * addParameter(int, Object);
     * 
     * @throws SQLException
     * @throws IOException
     */
    public void executeUpdate() throws SQLException, IOException {
        executeUpdate0(sql, true);
    }

    /**
     * Execute one or multiple sql without parameters separated by ';'
     * 
     * @param sql
     * @throws SQLException
     * @throws IOException
     */
    public void executeUpdate(String sql) throws SQLException, IOException {
        executeUpdate0(sql, false);
    }

    public void executeUpdate(List<String> sqls) {
        if (sqls == null || sqls.size() == 0)
            return;
        String sql = "";

        for (String aSql : sqls)
            sql += aSql + ";\n";

        executeUpdate0(sql, false);
    }

    private void executeUpdate0(String sql, boolean hasParameter) {
        Connection con = null;
        try {
            con = datasource.getConnection();

            if (hasParameter) {
                PreparedStatement preStmt = con.prepareStatement(sql);
                setParameters(preStmt);
                preStmt.executeUpdate();
                preStmt.close();
            } else {
                Statement stmt = con.createStatement();
                String[] sqls = sql.split(";");
                for (String aSql : sqls)
                    stmt.addBatch(aSql);
                stmt.executeBatch();
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // JdbcUtils.close(con);
        }
    }

    /**
     * Execute a sql query that no parameters
     * 
     * @param sql
     * @return
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Result executeQuery(String sql) throws SQLException, IOException, ClassNotFoundException {
        return executeQuery0(sql, false);
    }

    /**
     * Execute a sql query with parameters that set by addParameter(int,
     * Object);
     * 
     * @return
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Result executeQuery() throws SQLException, IOException, ClassNotFoundException {
        return executeQuery0(sql, true);
    }

    /**
     * Get Tables
     * @param types Typical types are 
     * "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM"
     * new String[] { "TABLE", "VIEW", "SYNONYM", "ALIAS"}
     */
    public Result getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) {
        Connection con = null;
        Result result = null;
        try {
            con = datasource.getConnection();

            ResultSet rs = con.getMetaData().getTables(catalog, schemaPattern, tableNamePattern, types);
            result = JdbcUtils.getResult(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(con);
        }

        return result;
    }

    private Result executeQuery0(String sql, boolean hasParameter) {
        Connection con = null;
        Result result = null;
        try {
            con = datasource.getConnection();
            // prtDrvInfo(con);
            PreparedStatement preStmt = con.prepareStatement(sql);
            if (hasParameter)
                setParameters(preStmt);

            ResultSet rs = preStmt.executeQuery(); // oracle.jdbc.driver.OracleResultSetImpl
            result = JdbcUtils.getResult(rs);
            preStmt.close(); // It result in ResultSet is also closed
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(con);
        }

        return result;
    }

    void prtDrvInfo(Connection con) throws SQLException {

        DatabaseMetaData mm = con.getMetaData(); //
        int A = mm.getJDBCMajorVersion(); //10 unable in Oracle-JDBC_9.0.1.1.0 
        int B = mm.getJDBCMinorVersion(); //2 unable in Oracle-JDBC_9.0.1.1.0 
        int C = mm.getDatabaseMajorVersion(); //10 unable in Oracle-JDBC_9.0.1.1.0
        int D = mm.getDatabaseMinorVersion(); //2 unable in Oracle-JDBC_9.0.1.1.0

        String E = mm.getDatabaseProductName(); //Oracle 
        //Oracle Database 10g Enterprise Edition Release 10.2.0.1.0 - Production With the Partitioning, OLAP and Data Mining options
        String F = mm.getDatabaseProductVersion();

        String G = mm.getDriverName(); //Oracle JDBC driver 
        String H = mm.getDriverVersion(); //10.2.0.2.0 //or old: //9.0.1.1.0 
        int I = mm.getDriverMajorVersion(); //10 
        int J = mm.getDriverMinorVersion(); //2

        //String info = String.format("%s,%s,%s,%s,  %s,%s,%s,%s,  %s,%s", A, B, C, D, E, F, G, H, I, J); 
        String pattern = "%s,%s,%s,%s,\n%s,%s,\n%s,%s,%s,%s\n";
        pattern = pattern.replaceAll(",", "\n");
        String info = String.format(pattern, A, B, C, D, E, F, G, H, I, J);
        System.out.println(info);

        // for oracle 9
        //DatabaseMetaData mm = con.getMetaData(); // dbMeta
        E = mm.getDatabaseProductName(); // Oracle
        F = mm.getDatabaseProductVersion(); // Oracle Database 10g
        // Enterprise Edition Release 10.2.0.1.0 - Production With the Partitioning, OLAP and Data Mining options

        G = mm.getDriverName(); // Oracle JDBC driver
        H = mm.getDriverVersion(); // 10.2.0.2.0 //or old: //9.0.1.1.0
        I = mm.getDriverMajorVersion(); // 10
        J = mm.getDriverMinorVersion(); // 2

        info = String.format("%s,%s,%s,%s,  %s,%s,%s,%s,  %s,%s", A, B, C, D, E, F, G, H, I, J);
        pattern = "%s,%s,\n%s,%s,%s,%s\n";
        pattern = pattern.replaceAll(",", "\n");
        info = String.format(pattern, E, F, G, H, I, J);
        System.out.println(info);
    }

    /**
     * add parameter for the sql
     * 
     * @param sql
     * @param parameterTypes
     * supported types:
     * <pre>
     *      0) Types.BIGINT             --Long
     *      1) Types.INTEGER
     *      2) Types.CHAR, VARCHAR      --String
     *      3) Types.Boolean
     *      4) Types.DATE, TIMESTAMP    --java.sql.Date, java.sql.Timestamp
     *      5) Types.DOUBLE
     *      6) Types.Decimal, Numeric   --BigDecimal
     *      7) Types.Other              --Object (HSQL)
     *      8) Types.CLOB               --String
     *      9) Types.BLOB               --byte[], java.io.Serializable
     * </pre>
     */
    public void addParameter(int type, Object obj) {
        this.parameters.add(new Parameter(type, obj));
    }

    public void setSql(String sql) {
        this.sql = sql;
        this.parameters.clear();
    }

    public DataSource getDatasource() {
        return datasource;
    }

    private static class Parameter {
        final int type;
        final Object value;

        // type is constant in java.sql.Types, such as Types.VARCHAR.
        public Parameter(int type, Object value) {
            this.type = type;
            this.value = value;
        }
    }
}
