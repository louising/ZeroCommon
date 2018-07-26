package com.zero.core.jdbc.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.List;

import com.zero.core.util.BaseUtils;

/**
 * Represents a record from DB, it is disconnect from DB
 * 
 * @author Louis
 * @date 2007-4-5
 */
public class Record implements Serializable {
    private static final long serialVersionUID = 1L;

    public final String tableName;
    public final List<Column> columns; //Refer to Result.columns;
    public final List<Object> values;

    public Record(List<Column> columns, List<Object> values) {
        this("!TABLE_NAME!", columns, values);
    }

    public Record(String tableName, List<Column> columns, List<Object> values) {
        this.tableName = tableName;
        this.columns = columns;
        this.values = values;
    }

    public Column getColumn(int index) {
        if (index < 0 || index > (this.columns.size() - 1))
            return null;

        return this.columns.get(index);
    }

    public Column getColumn(String columnName) {
        return columns.get(indexOf(columnName));
    }

    /**
     * return column index to the column name
     */
    private int indexOf(String columnName) {
        for (int i = 0; i < this.columns.size(); i++)
            if (this.columns.get(i).name.equals(columnName))
                return i;

        return -1;
    }

    public Object getValue(int index) {
        Object value = null;
        if (index > -1 && index < this.columns.size())
            value = this.values.get(index);
        return value;
    }

    /*
     * Blob ==>byte[]
     * Clob ==>String 
     * Numberic, Decimal ==>BigDecimal 
     * Date ==>java.sql.Date
     * Timestamp==>java.sql.Timestamp
     */
    public Object getValue(String columnName) {
        Object value = null;
        int index = this.indexOf(columnName);
        if (index > -1)
            value = this.values.get(index);
        return value;
    }

    /**
     * If the column is Timesamp==>"yyyy:mm:dd hh:mm:ss.000000"
     * 
     * @param index
     * @return
     */
    public String getStringValue(int index) {
        Object value = getValue(index);
        if (value == null)
            return "";

        String str = "";
        int type = columns.get(index).type;
        if (type == Types.CHAR || type == Types.VARCHAR)
            str = value.toString().trim();
        else if (type == Types.TIMESTAMP) {
            if (value instanceof java.sql.Date)
                str = BaseUtils.format((java.sql.Date) value);
            else if (value instanceof java.sql.Timestamp)
                str = BaseUtils.format((java.sql.Timestamp) value);
            else
                str = BaseUtils.format((java.util.Date) value, false);
        } else if (type == Types.BLOB)
            str = BaseUtils.getObject((byte[]) value).toString();
        else
            str = value.toString();

        //else if (type == Types.CLOB)
        //str = "<![CDATA[" + value.toString() + "]]>";
        return str;
    }

    public String getStringValue(String columnName) {
        return this.getStringValue(indexOf(columnName));
    }

    public Long getLongValue(String columnName) {
        //in DB is Integer or BigInt or Number
        Long value = null;
        Object obj = this.getValue(columnName);
        if (obj != null) {
            int type = this.getColumn(columnName).type;
            if (type == Types.BIGINT)
                value = (Long) obj;
            else if (type == Types.NUMERIC || type == Types.DECIMAL)
                value = ((BigDecimal) obj).longValue();
            else if (type == Types.INTEGER)
                value = Long.parseLong(obj.toString());
        }
        return value;
    }

    public Integer getIntValue(String columnName) {
        Integer value = null;
        Object obj = this.getValue(columnName);
        if (obj != null) {
            int type = this.getColumn(columnName).type;
            if (type == Types.INTEGER)
                value = (Integer) obj;
        }
        return value;
    }

    public Double getDoubleValue(String columnName) {
        Double value = null;
        Object obj = this.getValue(columnName);
        if (obj != null) {
            int type = this.getColumn(columnName).type;
            if (type == Types.DOUBLE)
                value = (Double) obj;
            else if (type == Types.NUMERIC || type == Types.DECIMAL)
                value = ((BigDecimal) obj).doubleValue();
        }
        return value;
    }
    
    public String toString() {
        String str = "";
        for (int i = 0; i < columns.size(); i++) {
            str += columns.get(i).name + ": " + values.get(i) + "\n";
        }
        
        return str;
    }
}
