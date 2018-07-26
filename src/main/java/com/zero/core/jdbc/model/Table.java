package com.zero.core.jdbc.model;

import java.util.ArrayList;
import java.util.List;

/**
 * It represents the struecture of a table in a database
 * 
 * @author Louis
 * @date 2007-7-7
 */
public class Table {
    public String tableName;
    public List<Column> columns = new ArrayList<Column>();

    public Table(String tableName) {
        super();
        this.tableName = tableName;
    }

    public Table(String tableName, List<Column> columns) {
        super();
        this.tableName = tableName;
        this.columns = columns;
    }

    public void addColumn(Column column) {
        columns.add(column);
    }

    public void addColumn(String name, int type) {
        columns.add(new Column(name, type));
    }

    //isPrimary = true
    public void addColumn(String name, int type, boolean isPrimaryKey) {
        columns.add(new Column(name, type, isPrimaryKey));
    }

    public void addColumn(String name, int type, int size) {
        columns.add(new Column(name, type, size));
    }

    //isPrimaryKey = true
    public void addColumn(String name, int type, int size, boolean isPrimaryKey) {
        columns.add(new Column(name, type, size, isPrimaryKey));
    }

    //canNull = false
    public void addColumn(String name, int type, boolean canNull, int size) {
        columns.add(new Column(name, type, canNull, size));
    }

    public String toString() {
        return super.toString();
    }

    /**
     * Return all fields of this table in the format of "field1, field2 ...field-n;"
     */
    public String getColumnNames() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.columns.size(); i++) {
            String columnName = this.columns.get(i).name;
            if (i == 0)
                sb.append(columnName);
            else
                sb.append(",").append(columnName);
        }
        return sb.toString();
    }

    public String getColumnNamePlaceholders() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.columns.size(); i++) {
            if (i == 0)
                sb.append('?');
            else
                sb.append(",?");
        }
        return sb.toString();
    }
}
