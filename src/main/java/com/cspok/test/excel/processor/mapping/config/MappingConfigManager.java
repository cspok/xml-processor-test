package com.cspok.test.excel.processor.mapping.config;

import java.io.File;

/**
 * Created by CS on 26/5/2015.
 */
public interface MappingConfigManager {

    void loadConfig(File configFile) throws Exception;

    GlobalConfig getGlobalConfig();

    SectionConfig getSectionConfig(String sectionName);

}
