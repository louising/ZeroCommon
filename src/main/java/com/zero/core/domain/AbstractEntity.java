package com.zero.core.domain;

import java.io.Serializable;
import java.util.Date;

import com.zero.core.util.BaseUtils;

/**
 * Super class for Domain Object(Entity), subclass should:
 * 1) override appendFields() to implement toString():
 *      appendField(sb, "customerID", customerID);
 *      appendField(sb, "itemID", itemID);
 * 2) override hashCode() like:
 *      return BaseUtils.hashCode(customerID, itemID, principal);
 * 
 * @author Louis
 */
public abstract class AbstractEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    public abstract Object getId();
    
    protected String format(String fieldName, Object fieldValue) {
        String format = "  %s = %s\n";
        
        if (fieldValue instanceof Date)
            fieldValue = BaseUtils.formatDate((Date) fieldValue);
        
        return String.format(format, fieldName, fieldValue);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();        
        appendFields(sb);        
        if (sb.length() == 0) {
            //subclass don't override appendFields()
            sb.append(getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()));
            //return super.toString();
        }            
        else {
            //subclass override appendFields()
            sb.insert(0, this.getClass().getSimpleName() + "(\n");
            sb.append(")");
        }            
        
        return sb.toString();
    }

    protected void appendFields(StringBuilder sb) {        
    }

    protected void appendField(StringBuilder sb, String fieldName, Object fieldValue) {
        sb.append(format(fieldName, fieldValue));
    }
}
