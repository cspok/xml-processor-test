package com.cspok.test.excel.processor.parser;

import org.apache.poi.ss.usermodel.Cell;

/**
 * Created by CS on 25/5/2015.
 */
public interface ExcelParsingHandler {

    boolean onSheetStart(String sheetName, int rowCount) throws Exception;

    boolean onRowStart(int rowNum, int cellsCount);

    boolean onCell(int cellNum, Cell cell);

    void onRowEnd(int j);

    void onSheetEnd(String sheetName);
}
