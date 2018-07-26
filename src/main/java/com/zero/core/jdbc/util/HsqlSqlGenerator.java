package com.zero.core.jdbc.util;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zero.core.jdbc.model.Column;
import com.zero.core.jdbc.model.Table;

public class HsqlSqlGenerator {
    static Map<Integer, String> hsqlTypeNameMap = new HashMap<Integer, String>(14);
    static {
        hsqlTypeNameMap.put(Types.TINYINT, "TINYINT");
        hsqlTypeNameMap.put(Types.SMALLINT, "SMALLINT");
        hsqlTypeNameMap.put(Types.INTEGER, "INTEGER");
        hsqlTypeNameMap.put(Types.BIGINT, "BIGINT");
        
        hsqlTypeNameMap.put(Types.DOUBLE, "DOUBLE");
        hsqlTypeNameMap.put(Types.REAL, "REAL");
        
        hsqlTypeNameMap.put(Types.NUMERIC, "NUMERIC");
        hsqlTypeNameMap.put(Types.DECIMAL, "DECIMAL");
        
        hsqlTypeNameMap.put(Types.CHAR, "CHAR");
        hsqlTypeNameMap.put(Types.VARCHAR, "VARCHAR");
        
        hsqlTypeNameMap.put(Types.DATE, "DATE");
        hsqlTypeNameMap.put(Types.TIMESTAMP, "TIMESTAMP");
        
        hsqlTypeNameMap.put(Types.OTHER, "OTHER");
        hsqlTypeNameMap.put(Types.BOOLEAN, "BOOLEAN");
    }
    
    private static String getSqlTypeName(int type) {
        return hsqlTypeNameMap.get(type);
    }
    
    public static String buildSql(Table table) {
        String tableName = table.tableName;
        List<Column> fields = table.columns;

        String pks = ""; //primary key fields
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("DROP TABLE %s IF EXISTS;\n", tableName));
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
        table.addColumn("aBigInt", Types.BIGINT, true);
        table.addColumn("aInt", Types.INTEGER);
        table.addColumn("aBoolean", Types.BOOLEAN);

        table.addColumn("aChar", Types.CHAR, 20);
        table.addColumn("aVarchar", Types.VARCHAR);

        table.addColumn("aDouble", Types.DOUBLE);
        table.addColumn("aNumeric", Types.NUMERIC);

        table.addColumn("aDate", Types.DATE);
        table.addColumn("aTimestamp", Types.TIMESTAMP);

        table.addColumn("aOther", Types.OTHER);

        return table;
    }
}
