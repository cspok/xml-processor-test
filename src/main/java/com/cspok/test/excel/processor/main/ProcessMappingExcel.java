package com.cspok.test.excel.processor.main;

import com.cspok.test.excel.processor.mapping.MappingItem;
import com.cspok.test.excel.processor.mapping.config.MappingConfigManager;
import com.cspok.test.excel.processor.mapping.config.impl.WiniMappingConfigManager;
import com.cspok.test.excel.processor.parser.ExcelParser;
import com.cspok.test.excel.processor.parser.ExcelParsingHandler;
import com.cspok.test.excel.processor.parser.impl.DefaultExcelParsingHandler;
import com.cspok.test.excel.processor.parser.impl.ExcelParserImpl;
import org.ini4j.Wini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * Created by CS on 26/5/2015.
 */
public class ProcessMappingExcel {

    private static Logger logger = LoggerFactory.getLogger(ProcessMappingExcel.class);

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            logger.error("Insufficient arguments! The following arguments are required:");
            logger.error("      arg1: Path to the mapping excel file.");
            logger.error("      arg2: Path to the mapping config file.");
            System.exit(-1);
        }
        String mappingExcelFile = args[0];
        File excelFile = new File(mappingExcelFile);
        if (!excelFile.exists() || excelFile.isDirectory()) {
            logger.error("Mapping Excel file: " + excelFile.getAbsolutePath() + " does not exist or is a directory!");
            System.exit(-1);
        }
        if (!excelFile.getName().endsWith(".xls") && !excelFile.getName().endsWith(".xlsx")) {
            logger.error(excelFile.getAbsolutePath() + " is not an Excel file (based on file extension)!");
            System.exit(-1);
        }
        String mappingConfigFile = args[1];
        File configFile = new File(mappingConfigFile);
        if (!configFile.exists() || configFile.isDirectory()) {
            logger.error("Mapping config file: " + configFile.getAbsolutePath() + " does not exist or is a directory!");
            System.exit(-1);
        }
        if (!configFile.getName().endsWith(".ini")) {
            logger.error(configFile.getAbsolutePath() + " is not an ini file (based on file extension)!");
            System.exit(-1);
        }

        MappingConfigManager configManager = new WiniMappingConfigManager();
        configManager.loadConfig(configFile);

        ExcelParsingHandler handler = new DefaultExcelParsingHandler(configManager);

        ExcelParser parser = new ExcelParserImpl();
        parser.setHandler(handler);

        parser.parse(excelFile);

        List<MappingItem> mappingList = handler.getMappingList();

        logger.info("Processed {} mappings.", mappingList.size());

        for (MappingItem item : mappingList) {
            logger.info("{}, {}, {}", item.getTableName(), item.getFieldName(), item.getXPath());
        }
    }

}
