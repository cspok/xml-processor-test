package com.cspok.test.excel.processor.mapping.config.impl;

import com.cspok.test.excel.processor.mapping.config.SectionConfig;
import org.ini4j.Profile;

/**
 * Created by CS on 26/5/2015.
 */
public class WiniSectionConfig implements SectionConfig {

    private static final int COLUMN_TO_CELL_NUM_OFFSET = (int) 'A';
    private static final String START_ROW_KEY = "mapping.row.start.at";
    private static final String TABLE_NAME_COLUMN_KEY = "mapping.table.name.column";
    private static final String FIELD_NAME_COLUMN_KEY = "mapping.field.name.column";
    private static final String XPATH_COLUMN_KEY = "mapping.xpath.column";

    private int startRow = 0;
    private String tableNameColumn = null;
    private String fieldNameColumn = null;
    private String xPathColumn = null;
    private int minCellsCount;
    private int tableNameCellNum;
    private int fieldNameCellNum;
    private int xPathCellNum;

    public WiniSectionConfig(Profile.Section section) throws Exception {
        startRow = getIntRequired(section, START_ROW_KEY);
        tableNameColumn = getStringRequired(section, TABLE_NAME_COLUMN_KEY);
        fieldNameColumn = getStringRequired(section, FIELD_NAME_COLUMN_KEY);
        xPathColumn = getStringRequired(section, XPATH_COLUMN_KEY);

        setTableNameCellNum(columnToCellNum(tableNameColumn));
        setFieldNameCellNum(columnToCellNum(fieldNameColumn));
        setXPathCellNum(columnToCellNum(xPathColumn));
    }

    @Override
    public int getStartRow() {
        return startRow;
    }

    private void updateMinCellCount(int cellNum) {
        if (minCellsCount <= cellNum) {
            minCellsCount = cellNum + 1;
        }
    }

    @Override
    public int getMinCellsCount() {
        return minCellsCount;
    }

    @Override
    public String getTableNameColumn() {
        return tableNameColumn;
    }

    @Override
    public String getFieldNameColumn() {
        return fieldNameColumn;
    }

    @Override
    public String getXPathColumn() {
        return xPathColumn;
    }

    private void setTableNameCellNum(int tableNameCellNum) {
        this.tableNameCellNum = tableNameCellNum;
        updateMinCellCount(tableNameCellNum);
    }

    @Override
    public int getTableNameCellNum() {
        return tableNameCellNum;
    }

    private void setFieldNameCellNum(int fieldNameCellNum) {
        this.fieldNameCellNum = fieldNameCellNum;
        updateMinCellCount(fieldNameCellNum);
    }

    @Override
    public int getFieldNameCellNum() {
        return fieldNameCellNum;
    }

    private void setXPathCellNum(int xPathCellNum) {
        this.xPathCellNum = xPathCellNum;
        updateMinCellCount(xPathCellNum);
    }

    @Override
    public int getXPathCellNum() {
        return xPathCellNum;
    }

    private int getIntRequired(Profile.Section section, String key) throws Exception {
        Integer value = section.get(key, Integer.class);
        if (value != null) {
            return value.intValue();
        } else {
            throw new Exception("Config " + key + " not found for section " + section.getName() + "!");
        }
    }

    private String getStringRequired(Profile.Section section, String key) throws Exception {
        String value = section.get(key, String.class);
        if (value != null) {
            return value;
        } else {
            throw new Exception("Config " + key + " not found for section " + section.getName() + "!");
        }
    }

    private int columnToCellNum(String column) throws Exception {
        if (column.length() == 1) {
            return (int) column.charAt(0) - COLUMN_TO_CELL_NUM_OFFSET;
        } else {
            throw new Exception("Column with more than 1 character is not supported: " + column);
        }
    }

}
