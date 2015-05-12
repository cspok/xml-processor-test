package com.cspok.test.xml.processor.parser;

import org.xml.sax.ErrorHandler;

import java.io.File;

/**
 * Created by CS on 4/5/2015.
 */
public interface XsdParser {

    void setHandler(XsdParsingHandler handler);

    void setErrorHandler(ErrorHandler errorHandler);

    boolean parse(File schemaFile);

}
