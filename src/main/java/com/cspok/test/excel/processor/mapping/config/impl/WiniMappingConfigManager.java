package com.cspok.test.excel.processor.mapping.config.impl;

import com.cspok.test.excel.processor.mapping.config.GlobalConfig;
import com.cspok.test.excel.processor.mapping.config.MappingConfigManager;
import com.cspok.test.excel.processor.mapping.config.SectionConfig;
import org.ini4j.Profile;
import org.ini4j.Wini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by CS on 26/5/2015.
 */
public class WiniMappingConfigManager implements MappingConfigManager {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private GlobalConfig commonConfig;
    private Map<String, SectionConfig> sectionConfigMap = new HashMap<String, SectionConfig>();

    @Override
    public void loadConfig(File configFile) throws Exception {
        Wini ini = new Wini(configFile);
        for (String sectionName : ini.keySet()) {
            Profile.Section section = ini.get(sectionName);
            if (sectionName.equals("?")) {
                logger.info("Found global section in config.");
                commonConfig = new WiniGlobalConfig(section);
            } else {
                logger.info("Found section in config: {}.", sectionName);
                SectionConfig sc = new WiniSectionConfig(section);
                sectionConfigMap.put(sectionName, sc);
            }
        }
    }

    @Override
    public GlobalConfig getGlobalConfig() {
        return null;
    }

    @Override
    public SectionConfig getSectionConfig(String sectionName) {
        return sectionConfigMap.get(sectionName);
    }
}
