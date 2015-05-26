package com.cspok.test.xml.processor.parser.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Created by CS on 4/5/2015.
 */
public class DefaultErrorHandler implements ErrorHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void warning(SAXParseException e) throws SAXException {
        logger.warn(e.getMessage(), e);
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        logger.error(e.getMessage(), e);
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        logger.error(e.getMessage(), e);
    }
}
