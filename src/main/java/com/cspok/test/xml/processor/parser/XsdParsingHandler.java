package com.cspok.test.xml.processor.parser;

import com.sun.xml.xsom.*;

import java.util.Deque;
import java.util.List;

/**
 * Created by CS on 4/5/2015.
 */
public interface XsdParsingHandler {

    void onStart(XSSchema rootSchema) throws Exception;

    void onElementStart(XSElementDecl element, List<XSParticle> particlesChain) throws Exception;

    void onElementEnd(XSElementDecl element) throws Exception;

    void onSimpleType(XSSimpleType xsSimpleType) throws Exception;

    void onAttribute(XSElementDecl element, XSAttributeDecl attribute) throws Exception;

    void onEnd();

}
