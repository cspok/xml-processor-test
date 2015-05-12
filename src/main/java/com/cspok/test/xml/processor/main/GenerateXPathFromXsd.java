package com.cspok.test.xml.processor.main;

import com.cspok.test.xml.processor.parser.XsdParser;
import com.cspok.test.xml.processor.parser.impl.DefaultXsdParsingHandler;
import com.cspok.test.xml.processor.parser.impl.XsdParserImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.io.PrintWriter;

/**
 * Created by CS on 28/4/2015.
 */
public class GenerateXPathFromXsd {

    private static Logger logger = LoggerFactory.getLogger(GenerateXPathFromXsd.class);

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            logger.error("Insufficient arguments! The following arguments are required:");
            logger.error("      arg1: Path to a single root schema file or schema files directory (scan dir for process).");
            logger.error("      arg2: [Optional] Path to output files directory.");
            System.exit(-1);
        }
        String rootSchemaFilePath = args[0];
        File fileOrDir = new File(rootSchemaFilePath);
        if (!fileOrDir.exists()) {
            logger.error("Root schema file: " + fileOrDir.getAbsolutePath() + " does not exist!");
            System.exit(-1);
        }
        if (fileOrDir.isFile() && !fileOrDir.getName().endsWith(".xsd")) {
            logger.error(fileOrDir.getAbsolutePath() + " is not an XSD file or directory!");
            System.exit(-1);
        }

        String outputDir = null;
        if (args.length >= 2) {
            outputDir = args[1];
        }
        if (fileOrDir.isDirectory()) {
            File[] files = fileOrDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".xsd");
                }
            });
            for (File file : files) {
                processSchemaFile(file, outputDir);
            }
        } else {
            processSchemaFile(fileOrDir, outputDir);
        }
    }

    private static void processSchemaFile(File schemaFile, String outputDir) throws Exception {
        XsdParser parser = new XsdParserImpl();
        File outputFile = null;
        String outputFilename = schemaFile.getName() + ".out";
        if (outputDir == null) {
            outputFile = new File(schemaFile.getParent(), outputFilename);
        } else {
            outputFile = new File(outputDir, outputFilename);
        }
        if (outputFile.exists()) {
            logger.info("Output file: {} already exists. Deleting it.", outputFile.getAbsoluteFile());
            outputFile.delete();
        }
        PrintWriter xpathOut = new PrintWriter(outputFile);
        DefaultXsdParsingHandler handler = new DefaultXsdParsingHandler();
        handler.setWriter(xpathOut);

        parser.setHandler(handler);
        boolean success = parser.parse(schemaFile);
        xpathOut.close();

        if (success) {
            logger.info("XPath for schema {} generated.", schemaFile.getAbsoluteFile());
        } else {
            logger.error("No XPath to be generated for schema {}.", schemaFile.getAbsoluteFile());
            outputFile.delete();
        }
    }

}
