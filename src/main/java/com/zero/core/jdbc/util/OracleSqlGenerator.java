package com.zero.core.jdbc.util;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zero.core.jdbc.model.Column;
import com.zero.core.jdbc.model.Table;

public class OracleSqlGenerator {
    static Map<Integer, String> oracleTypeNameMap = new HashMap<Integer, String>(7);
    static {
        oracleTypeNameMap.put(Types.NUMERIC, "NUMBER");
        oracleTypeNameMap.put(Types.CHAR, "CHAR");
        oracleTypeNameMap.put(Types.VARCHAR, "VARCHAR2(4000)");
        oracleTypeNameMap.put(Types.DATE, "DATE");
        oracleTypeNameMap.put(Types.TIMESTAMP, "TIMESTAMP(6)");
        oracleTypeNameMap.put(Types.BLOB, "BLOB");
        oracleTypeNameMap.put(Types.CLOB, "CLOB");
    }
    
    private static String getSqlTypeName(int type) {
        return oracleTypeNameMap.get(type);
    }

    public static String buildSql(Table table) {
        String tableName = table.tableName;
        List<Column> fields = table.columns;

        String pks = ""; //primary key fields
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("DROP TABLE %s;\n", tableName));
        sb.append(String.format("CREATE TABLE %s (\n", tableName));
        for (int i = 0; i < fields.size(); i++) {
            Column field = fields.get(i);
            String canNULL = field.canNull ? "" : " NOT NULL";
            String type = getSqlTypeName(field.type);
            String size = field.size > 0 ? ("(" + field.size + ")") : "";
            if (i == fields.size() - 1)
                sb.append(String.format("  %s %s%s%s\n", field.name, type, size, canNULL));
            else
                sb.append(String.format("  %s %s%s%s,\n", field.name, type, size, canNULL));
            if (field.isPrimaryKey)
                pks = pks + field.name + ",";
        }
        sb.append(");\n");

        if (!pks.equals("")) {
            pks = pks.substring(0, pks.length() - 1);
            sb.append(String.format("ALTER TABLE %s ADD CONSTRAINT PK_%s PRIMARY KEY (%s);", tableName, tableName, pks));
        }
        return sb.toString();
    }

    public static Table createTestTable() {
        Table table = new Table("aTable");
        table.addColumn("aNumber", Types.NUMERIC, true);
        table.addColumn("aChar", Types.CHAR, 20);
        table.addColumn("aVarchar", Types.VARCHAR);
        table.addColumn("aDate", Types.DATE);
        table.addColumn("aTimestamp", Types.TIMESTAMP);
        table.addColumn("aBlob", Types.BLOB);
        table.addColumn("aClob", Types.CLOB);

        return table;
    }
}
