package com.cspok.test.excel.processor.parser.impl;

import com.cspok.test.excel.processor.mapping.config.MappingConfigManager;
import com.cspok.test.excel.processor.mapping.config.SectionConfig;
import com.cspok.test.excel.processor.parser.ExcelParsingHandler;
import org.apache.poi.ss.usermodel.Cell;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by CS on 26/5/2015.
 */
public class DefaultExcelParsingHandler implements ExcelParsingHandler {

    private MappingConfigManager configManager = null;
    private SectionConfig currentSectionConfig;
    private Map<String, Map<String, String>> mappingMap = new LinkedHashMap<String, Map<String, String>>();
    private Map<String, String> currentMapping = null;

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
        if (rowNum >= currentSectionConfig.getStartRow() && cellsCount >= currentSectionConfig.getMinCellsCount()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onCell(int cellNum, Cell cell) {
        if (cellNum == currentSectionConfig.getTableNameCellNum()) {

            return true;
        }
        return false;
    }

    @Override
    public void onRowEnd(int j) {

    }

    @Override
    public void onSheetEnd(String sheetName) {

    }

}
