package com.cspok.test.xml.processor.parser.impl;

import com.cspok.test.xml.processor.parser.XsdParsingHandler;
import com.sun.xml.xsom.*;

import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

/**
 * Created by CS on 12/5/2015.
 */
public class DefaultXsdParsingHandler implements XsdParsingHandler {

    private Deque<String> currentPath = new ArrayDeque<String>();
    private String currentXPath;
    private PrintWriter writer;

    @Override
    public void onStart(XSSchema rootSchema) throws Exception {
        // Nothing to do
    }

    @Override
    public void onElementStart(XSElementDecl element, List<XSParticle> particlesChain) throws Exception {
        addToXPath(element, particlesChain);
    }

    @Override
    public void onElementEnd(XSElementDecl element) throws Exception {
        removeLastFromXPath();
    }

    @Override
    public void onSimpleType(XSSimpleType xsSimpleType) throws Exception {
        String xpath = generateSimpleTypeXPath(getCurrentXPath(), xsSimpleType);
        getWriter().println(xpath);
    }

    @Override
    public void onAttribute(XSElementDecl element, XSAttributeDecl attribute) throws Exception {
        String xpath = generateAttributeXPath(getCurrentXPath(), attribute.getName());
        getWriter().println(xpath);
    }

    @Override
    public void onEnd() {

    }

    public void setWriter(PrintWriter writer) {
        this.writer = writer;
    }

    protected PrintWriter getWriter() {
        if (this.writer == null) {
            this.writer = new PrintWriter(System.out);
        }
        return this.writer;
    }

    private boolean isArray(List<XSParticle> particlesChain) {
        for (XSParticle particle : particlesChain) {
            int maxOccurs = particle.getMaxOccurs().intValue();
            if (maxOccurs == XSParticle.UNBOUNDED || maxOccurs > 1) {
                return true;
            }
        }
        return false;
    }

    private static String getElementName(XSElementDecl element, boolean isArray) {
        StringBuilder sb = new StringBuilder(element.getName());
        if (isArray) {
            sb.append("[]");
        }
        return sb.toString();
    }

    private void addToXPath(XSElementDecl element, List<XSParticle> particlesChain) {
        this.currentPath.push(getElementName(element, isArray(particlesChain)));
        this.currentXPath = buildXPath(currentPath);
    }

    private void removeLastFromXPath() {
        this.currentPath.pop();
        this.currentXPath = null;
    }

    private static String buildXPath(Deque<String> currentPath) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> itr = currentPath.descendingIterator();
        while (itr.hasNext()) {
            sb.append("/");
            sb.append(itr.next());
        }
        return sb.toString();
    }

    private String getCurrentXPath() {
        return this.currentXPath;
    }

    private String generateAttributeXPath(String currentXPath, String attrName) {
        StringBuilder sb = new StringBuilder(currentXPath);
        sb.append("/@");
        sb.append(attrName);
        return sb.toString();
    }

    private String generateSimpleTypeXPath(String currentXPath, XSSimpleType simpleType) {
        return currentXPath;
    }

}
