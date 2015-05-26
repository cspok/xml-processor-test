package com.cspok.test.xml.processor.parser.impl;

import com.cspok.test.xml.processor.parser.OccurrenceCounter;
import com.cspok.test.xml.processor.parser.XsdParser;
import com.cspok.test.xml.processor.parser.XsdParsingHandler;
import com.sun.xml.xsom.*;
import com.sun.xml.xsom.parser.SchemaDocument;
import com.sun.xml.xsom.parser.XSOMParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by CS on 4/5/2015.
 */
public class XsdParserImpl implements XsdParser {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ErrorHandler errorHandler;
    private XsdParsingHandler handler;

    @Override
    public void setHandler(XsdParsingHandler handler) {
        this.handler = handler;
    }

    @Override
    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public boolean parse(File schemaFile) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setValidating(false);

            XSOMParser parser = new XSOMParser(factory);
            if (errorHandler == null) {
                errorHandler = new DefaultErrorHandler();
            }
            if (handler == null) {
                handler = new DefaultXsdParsingHandler();
            }
            parser.setErrorHandler(errorHandler);

            parser.parse(schemaFile);
            XSSchemaSet result = parser.getResult();

            OccurrenceCounter occurCounter = new OccurrenceCounter();

            XSSchema rootSchema = result.getSchema(1);

            XSElementDecl rootElement = null;
            Iterator<XSElementDecl> itr = rootSchema.iterateElementDecls();
            if (itr.hasNext()) {
                rootElement = itr.next();
                handler.onStart(rootSchema);
                processElement(rootElement, new ArrayList<XSParticle>(), occurCounter);
                handler.onEnd();
                return true;
            } else {
                logger.error("Root element not found in schema: {}!", schemaFile.getAbsoluteFile());
                return false;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    private void processElement(XSElementDecl element, List<XSParticle> particlesChain,
                                OccurrenceCounter occurCounter) throws Exception {
        XSType type = element.getType();
        int occurCount = occurCounter.addOccurrence(type);
        if (occurCount > 3) {
            logger.error("Type: {} reoccurred more than 3 times for element: {}. Not going any deeper.", type.getName(), element.getName());
        } else {
            logger.debug("Element: {} is of type: {}.", element.getName(), type.getName());
            handler.onElementStart(element, particlesChain);
            if (type.isComplexType()) {
                XSComplexType complexType = type.asComplexType();
                for (XSAttributeUse attr : complexType.getAttributeUses()) {
                    XSAttributeDecl attrDecl = attr.getDecl();
                    handler.onAttribute(element, attrDecl);
                }
                XSContentType ct = complexType.getContentType();
                if (ct instanceof XSParticle) {
                    processParticle(ct.asParticle(), new ArrayList<XSParticle>(), occurCounter);
                } else if (ct instanceof XSSimpleType) {
                    handler.onSimpleType(ct.asSimpleType());
                }
            } else if (type.isSimpleType()) {
                handler.onSimpleType(type.asSimpleType());
            }
            handler.onElementEnd(element);
        }
        occurCounter.removeOccurrence(type);
    }

    private void processAttributes(XSElementDecl element, XSComplexType complexType) throws Exception {
    }

    private void processParticle(XSParticle particle, List<XSParticle> particlesChain,
                                 OccurrenceCounter occurCounter) throws Exception {
        particlesChain.add(particle);
        XSTerm term = particle.getTerm();
        if (term.isModelGroup()) {
            XSModelGroup modelGroup = term.asModelGroup();
            for (XSParticle childParticle : modelGroup.getChildren()) {
                processParticle(childParticle, particlesChain, occurCounter);
            }
        } else if (term.isElementDecl()) {
            processElement(term.asElementDecl(), particlesChain, occurCounter);
        }
        // Remove from the last
        particlesChain.remove(particlesChain.size() - 1);
    }

}
