package com.cspok.test.excel.processor.mapping.config.impl;

import com.cspok.test.excel.processor.mapping.config.SectionConfig;
import org.ini4j.Profile;

/**
 * Created by CS on 26/5/2015.
 */
public class WiniSectionConfig implements SectionConfig {

    public static final String START_ROW_KEY = "mapping.row.start.at";
    public static final String MIN_CELLS_COUNT_KEY = "mapping.min.cells.count";
    public static final String TABLE_NAME_CELL_NUM_KEY = "mapping.table.name.cell.num";
    public static final String FIELD_NAME_CELL_NUM_KEY = "mapping.field.name.cell.num";
    public static final String XPATH_CELL_NUM_KEY = "mapping.xpath.cell.num";

    private int startRow;
    private int minCellsCount;
    private int tableNameCellNum;
    private int fieldNameCellNum;
    private int xPathCellNum;

    public WiniSectionConfig(Profile.Section section) throws Exception {
        startRow = getIntRequired(section, START_ROW_KEY);
        minCellsCount = getIntRequired(section, MIN_CELLS_COUNT_KEY);
        tableNameCellNum = getIntRequired(section, TABLE_NAME_CELL_NUM_KEY);
        fieldNameCellNum = getIntRequired(section, FIELD_NAME_CELL_NUM_KEY);
        xPathCellNum = getIntRequired(section, XPATH_CELL_NUM_KEY);
    }

    @Override
    public int getStartRow() {
        return startRow;
    }

    @Override
    public int getMinCellsCount() {
        return minCellsCount;
    }

    @Override
    public int getTableNameCellNum() {
        return tableNameCellNum;
    }

    @Override
    public int getFieldNameCellNum() {
        return fieldNameCellNum;
    }

    @Override
    public int getXPathCellNum() {
        return xPathCellNum;
    }

    private int getIntRequired(Profile.Section section, String key) throws Exception {
        Integer iStartRow = section.get(key, Integer.class);
        if (iStartRow != null) {
            return iStartRow.intValue();
        } else {
            throw new Exception("Config " + key + " not found for section " + section.getName() + "!");
        }
    }

}
