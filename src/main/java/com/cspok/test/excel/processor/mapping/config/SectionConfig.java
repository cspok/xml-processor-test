package com.cspok.test.excel.processor.mapping.config;

/**
 * Created by CS on 26/5/2015.
 */
public interface SectionConfig {

    int getStartRow();

    int getMinCellsCount();

    String getTableNameColumn();

    String getFieldNameColumn();

    String getXPathColumn();

    int getTableNameCellNum();

    int getFieldNameCellNum();

    int getXPathCellNum();

}
