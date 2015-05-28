package com.cspok.test.excel.processor.parser.impl;

import com.cspok.test.excel.processor.mapping.MappingItem;
import com.cspok.test.excel.processor.mapping.config.MappingConfigManager;
import com.cspok.test.excel.processor.mapping.config.SectionConfig;
import com.cspok.test.excel.processor.parser.ExcelParsingHandler;
import org.apache.poi.ss.usermodel.Cell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CS on 26/5/2015.
 */
public class DefaultExcelParsingHandler implements ExcelParsingHandler {

    private MappingConfigManager configManager = null;
    private SectionConfig currentSectionConfig;

    private List<MappingItem> mappingList = new ArrayList<MappingItem>();
    private MappingItem currentMapping = null;
    private String previousTableName = null;

    public DefaultExcelParsingHandler(MappingConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public boolean onSheetStart(String sheetName, int rowCount) throws Exception {
        currentSectionConfig = configManager.getSectionConfig(sheetName);
        if (currentSectionConfig != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onRowStart(int rowNum, int cellsCount) {
        if (rowNum >= currentSectionConfig.getStartRow() - 1 && cellsCount >= currentSectionConfig.getMinCellsCount()) {
            currentMapping = new MappingItem();
            return true;
        }
        return false;
    }

    @Override
    public boolean onCell(int cellNum, Cell cell) {
        if (cellNum == currentSectionConfig.getTableNameCellNum()) {
            String tableName = null;
            if (cell != null) {
                tableName = cell.getStringCellValue().trim();
                previousTableName = tableName;
            } else {
                tableName = previousTableName;
            }
            if (tableName != null && tableName.length() > 0) {
                currentMapping.setTableName(tableName);
                return true;
            } else {
                return false;
            }
        } else if (cellNum == currentSectionConfig.getFieldNameCellNum()) {
            if (cell != null) {
                String fieldName = cell.getStringCellValue().trim();
                currentMapping.setFieldName(fieldName);
                return true;
            } else {
                return false;
            }
        } else if (cellNum == currentSectionConfig.getXPathCellNum()) {
            if (cell != null) {
                String xpath = cell.getStringCellValue().trim();
                if (xpath.startsWith("/")) {
                    currentMapping.setXPath(xpath.replaceAll(" ", ""));
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        if (currentMapping.isValid()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRowEnd(int j) {
        if (currentMapping != null && currentMapping.isValid()) {
            mappingList.add(currentMapping);
        }
        currentMapping = null;
    }

    @Override
    public void onSheetEnd(String sheetName) {
        previousTableName = null;
    }

    @Override
    public List<MappingItem> getMappingList() {
        return mappingList;
    }

}
