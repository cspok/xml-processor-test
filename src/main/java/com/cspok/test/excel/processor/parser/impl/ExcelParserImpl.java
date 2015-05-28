package com.cspok.test.excel.processor.parser.impl;

import com.cspok.test.excel.processor.parser.ExcelParser;
import com.cspok.test.excel.processor.parser.ExcelParsingHandler;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by CS on 25/5/2015.
 */
public class ExcelParserImpl implements ExcelParser {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ExcelParsingHandler handler = null;

    @Override
    public void setHandler(ExcelParsingHandler handler) {
        this.handler = handler;
    }

    public boolean parse(File excelFile) throws Exception {
        Workbook wb = null;
        try {
            logger.info("Processing excel file{} ", excelFile.getAbsolutePath());
            wb = WorkbookFactory.create(excelFile);
            int sheetsCount = wb.getNumberOfSheets();
            logger.debug("Sheets count: {}", sheetsCount);
            for (int i = 0; i < sheetsCount; i++) {
                Sheet sheet = wb.getSheetAt(i);
                int rowCount = sheet.getPhysicalNumberOfRows();
                if (handler.onSheetStart(sheet.getSheetName(), rowCount)) {
                    logger.info("Processing sheet: {}", sheet.getSheetName());
                    for (int j = 0; j < rowCount; j++) {
                        Row row = sheet.getRow(j);
                        int cellsCount = row.getPhysicalNumberOfCells();
                        if (handler.onRowStart(j, cellsCount)) {
                            logger.debug("Processing row {}", j + 1);
                            for (int k = 0; k < cellsCount; k++) {
                                Cell cell = row.getCell(k);
                                try {
                                    if (!handler.onCell(k, cell)) {
                                        logger.debug("Terminating row {} processing at cell {}.", j + 1, k + 1);
                                        break;
                                    }
                                } catch (Exception e) {
                                    logger.error("Error processing cell {} at row {}: {}", k + 1, j + 1, e.getMessage(), e);
                                }
                            }
                            handler.onRowEnd(j);
                        } else {
                            logger.debug("Skipping row {}", j + 1);
                        }
                    }
                } else {
                    logger.debug("Skipping sheet: {}", sheet.getSheetName());
                }
                handler.onSheetEnd(sheet.getSheetName());
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (InvalidFormatException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (wb != null) {
                try {
                    wb.close();
                } catch (IOException e) {
                }
            }
        }
        return false;
    }

}
