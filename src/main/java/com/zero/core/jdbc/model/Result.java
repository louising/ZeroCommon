package com.zero.core.jdbc.model;

import java.io.Serializable;
import java.util.List;

import com.zero.core.util.BaseUtils;
import com.zero.core.util.Log;

/**
 * Result: It represent a result set from a Query;
 * 
 * @author Louis
 * @date 2007-4-5
 */
public class Result implements Serializable {
    private static final long serialVersionUID = 1L;

    public final String tableName;
    public final List<Column> columns;
    public final List<Record> records;

    private int[] maxFieldWidths;

    public Result(List<Column> columns, List<Record> records) {
        this("!TABLE_NAME!", columns, records);
    }

    public Result(String tableName, List<Column> columns, List<Record> records) {
        super();
        this.tableName = tableName;
        this.columns = columns;
        this.records = records;
    }

    public String toString() {
        //1) Record count
        int recordCount = records.size();
        Log.prt("Record count: " + recordCount + "\n");

        //2) Header
        int digit = String.valueOf(recordCount).length();
        digit = (digit < 3) ? 3 : digit; //3 is the length of 'No.'

        StringBuilder result = new StringBuilder();
        StringBuilder line = new StringBuilder();

        String format = " %" + digit + "s | "; //first column: ' %digit | '        
        line.append("-" + BaseUtils.getString('-', digit) + "-|-"); //' %digit | ': '-' refer to space

        for (int i = 0; i < columns.size(); i++) {
            int width = getMaxColumnWidth(i);
            format += "%-" + width + "s | ";
            line.append(BaseUtils.getString('-', width) + "-|-"); //'-|-': '-' refer to space
        }

        line.append('\n');
        format += "%n";

        //header
        Object[] headers = new Object[columns.size() + 1];
        headers[0] = "No.";
        for (int i = 1; i <= this.columns.size(); i++)
            headers[i] = this.columns.get(i - 1).name;
        String headerStr = String.format(format, headers);
        result.append(headerStr);

        //3) separatrix (between header and records)
        result.append(line);

        //4) Records
        for (int i = 0; i < this.records.size(); i++) {
            Record record = this.records.get(i);
            Object[] values = new String[this.columns.size() + 1];
            values[0] = (i + 1) + "";
            for (int j = 1; j <= this.columns.size(); j++)
                values[j] = record.getStringValue(j - 1);
            String recordStr = String.format(format, values);
            result.append(recordStr);
        }
        if (records.size() > 0)
            result.append(line.toString());

        prtColumnInfo();
        return result.toString();
    }

    //get the max width of the column according to the content
    protected int getMaxFieldWidth(int columnIndex) {
        if (maxFieldWidths == null)
            getMaxFieldWidths();

        return maxFieldWidths[columnIndex];
    }

    private void getMaxFieldWidths() {
        maxFieldWidths = new int[columns.size()];

        for (Record record : this.records) {
            for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
                String str = record.getStringValue(columnIndex);
                int length = (str != null) ? str.length() : 0;
                if (length > maxFieldWidths[columnIndex]) {
                    maxFieldWidths[columnIndex] = length;
                }
            }
        }
    }

    //get the the width of the special column's header
    protected int getHeaderWidth(int columnIndex) {
        return this.columns.get(columnIndex).name.length();
    }

    //index based on 0
    protected int getMaxColumnWidth(int columnIndex) {
        return Math.max(getHeaderWidth(columnIndex), getMaxFieldWidth(columnIndex));
    }

    void prtColumnInfo() {
        StringBuilder sb = new StringBuilder();        
        //Format
        int maxColumnNameLength = 4;
        int maxTypeNameLength = 9;
        int maxColumnClassNameLength = 0;
        for (Column c : columns) {
            int nameLength = c.name.length();
            if (nameLength > maxColumnNameLength)
                maxColumnNameLength = nameLength;

            int typeNameLength = c.typeName.length();
            if (typeNameLength > maxTypeNameLength)
                maxTypeNameLength = typeNameLength;
            
            int columnClassNameLength = c.columnClassName.length();
            if (columnClassNameLength > maxColumnClassNameLength)
                maxColumnClassNameLength = columnClassNameLength;
        }
        
        String format = " %3s | %-" + maxColumnNameLength + "s | %-4s | %-" + maxTypeNameLength + "s | %s";
        String line = "-----|-" + BaseUtils.getString('-', maxColumnNameLength) + "-|-" + "-----|-" +
        BaseUtils.getString('-', maxTypeNameLength) + "-|-" + BaseUtils.getString('-', maxColumnClassNameLength);
        
        //Column Label
        String columnLabels = String.format(format, "No.", "Name", "Type", "Type Name", "Column Class Name");
        sb.append(columnLabels).append('\n');
        
        //Line
        sb.append(line).append('\n');

        //Column
        for (int i = 0; i < columns.size(); i++) {
            Column c = columns.get(i);
            String columnStr = String.format(format, i + "", c.name, c.type, c.typeName, c.columnClassName);
            sb.append(columnStr).append('\n');
        }
        
        sb.append(line).append('\n');
        
        Log.prt("Columns::");
        Log.prt(sb);
    }

    /*public String getColumnPattern() {
        //Name Type Type-Name Column-Class-Name
        int maxColumnNameLength = 4;
        int maxTypeNameLength = 9;
        for (Column c : columns) {
            int nameLength = c.name.length();
            if (nameLength > maxColumnNameLength)
                maxColumnNameLength = nameLength;

            int typeNameLength = c.typeName.length();
            if (typeNameLength > maxTypeNameLength)
                maxTypeNameLength = typeNameLength;
        }

        return " %3s | %-" + maxColumnNameLength + "s %-4s %-" + maxTypeNameLength + "s %s";
    }

    public StringBuilder getColumnLabels() {
        StringBuilder sb = new StringBuilder();
        String columnLabels = String.format(getColumnPattern(), "Name", "Type", "Type Name", "Column Class Name");
        sb.append(columnLabels);
        return sb;
    }*/
}
