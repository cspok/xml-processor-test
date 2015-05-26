package com.cspok.test.excel.processor.parser;

import java.io.File;

/**
 * Created by CS on 25/5/2015.
 */
public interface ExcelParser {

    void setHandler(ExcelParsingHandler handler);

    boolean parse(File excelFile) throws Exception;

}
