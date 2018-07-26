package com.zero.core.jdbc.model;

/**
 * It represents a column of a table
 * Support type
 * <pre>
 * 
 * </pre>
 * 
 * @author Louis
 * @date 2007-7-8
 */
public class Column {
    //mandatory
    public String name; //column's name
    public int type; //column's SQL type.

    public String typeName; //column's database-specific type name.       
    public String columnClassName; //the fully-qualified class name of object return by ResultSet.getObject

    //public static String pattern = "%-12s %-8s %-10s %s";

    //optional
    public int size = 0;
    public boolean canNull = true;
    public boolean isPrimaryKey = false;

    private static int maxColumnNameLength = 4;
    private static int maxTypeNameLength = 9;

    //Create table
    public Column(String name, int type) {
        super();
        this.name = name;
        this.type = type;

        int length = name.length();
        if (length > maxColumnNameLength)
            maxColumnNameLength = length;
    }

    //Search from DB
    public Column(String name, int type, String typeName, String columnClassName) {
        this(name, type);
        this.typeName = typeName;
        this.columnClassName = columnClassName;

        int length = typeName.length();
        if (length > maxTypeNameLength)
            maxTypeNameLength = length;
    }

    //isPrimary = true
    public Column(String name, int type, boolean isPrimaryKey) {
        this(name, type);

        this.isPrimaryKey = isPrimaryKey;
        this.canNull = !isPrimaryKey;
    }

    public Column(String name, int type, int size) {
        this(name, type);
        this.size = size;
    }

    //isPrimaryKey = true
    public Column(String name, int type, int size, boolean isPrimaryKey) {
        this(name, type, size);

        this.isPrimaryKey = isPrimaryKey;
        this.canNull = !isPrimaryKey;
    }

    //canNull = false
    public Column(String name, int type, boolean canNull, int size) {
        this(name, type, size);
        this.canNull = canNull;
    }

    public String toString() {
        return String.format("%s %s %s %s", name, type, typeName, columnClassName);
    }
}
