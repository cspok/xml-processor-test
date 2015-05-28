package com.cspok.test.excel.processor.mapping;

/**
 * Created by CS on 27/5/2015.
 */
public class MappingItem {

    private String tableName;
    private String fieldName;
    private String xPath;

    public boolean isValid() {
        if (tableName != null && tableName.length() > 0
                && fieldName != null && fieldName.length() > 0
                && xPath != null && xPath.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setXPath(String XPath) {
        this.xPath = XPath;
    }

    public String getXPath() {
        return xPath;
    }
}
