package com.cspok.test.xml.processor.parser;

import com.sun.xml.xsom.XSType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by CS on 4/5/2015.
 */
public class OccurrenceCounter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Map<String, Map<String, AtomicInteger>> occurrenceMap = new HashMap<String, Map<String, AtomicInteger>>();

    public int addOccurrence(XSType type) {
        AtomicInteger occurrence = getOccurrence(type);
        int count = occurrence.incrementAndGet();
        logger.debug("Occurrence added for {}:{}. Count: {}", type.getTargetNamespace(), type.getName(), count);
        return count;
    }

    public int removeOccurrence(XSType type) {
        AtomicInteger occurrence = getOccurrence(type);
        int count = occurrence.decrementAndGet();
        logger.debug("Occurrence removed for {}:{}. Count: {}", type.getTargetNamespace(), type.getName(), count);
        return count;
    }

    private AtomicInteger getOccurrence(XSType type) {
        Map<String, AtomicInteger> map = getMapForNamespace(type.getTargetNamespace());
        return getOccurrenceForTypeName(map, type.getName());
    }

    private AtomicInteger getOccurrenceForTypeName(Map<String, AtomicInteger> map, String typeName) {
        AtomicInteger occurrence = map.get(typeName);
        if (occurrence == null) {
            occurrence = new AtomicInteger();
            map.put(typeName, occurrence);
        }
        return occurrence;
    }

    private Map<String, AtomicInteger> getMapForNamespace(String ns) {
        Map<String, AtomicInteger> map = occurrenceMap.get(ns);
        if (map == null) {
            map = new HashMap<String, AtomicInteger>();
            occurrenceMap.put(ns, map);
        }
        return map;
    }
}
