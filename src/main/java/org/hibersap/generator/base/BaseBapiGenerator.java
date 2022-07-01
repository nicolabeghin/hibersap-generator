package org.hibersap.generator.base;

import org.hibersap.generator.parser.BapiParser;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Base Hibersap Java source code generator
 */
public class BaseBapiGenerator extends AbstractBaseGenerator {
    public static final String BAPI_ANNOTATION = "org.hibersap.annotations.Bapi";
    public static final String BAPI_STRUCTURE_ANNOTATION = "org.hibersap.annotations.BapiStructure";
    public static final String BAPI_IMPORT_ANNOTATION = "org.hibersap.annotations.Import";
    public static final String BAPI_EXPORT_ANNOTATION = "org.hibersap.annotations.Export";
    public static final String BAPI_TABLE_ANNOTATION = "org.hibersap.annotations.Table";
    public static final String BAPI_PARAMETER_ANNOTATION = "org.hibersap.annotations.Parameter";

    private static final String[] DEFAULT_IMPORTS = new String[]{
            "org.hibersap.annotations.*",
            "java.util.ArrayList",
            "java.util.List",
            "java.util.Date",
            "java.math.BigDecimal",
            "javax.validation.constraints.Required"
    };

    /**
     * Create a base ABAP BAPI Java sourcecode
     */
    public static JavaClassSource createBapiClass(String className, BapiParser bapiParser) {
        final JavaClassSource javaClass = createBaseHibersapClass(className, bapiParser);
        javaClass.addAnnotation(BAPI_ANNOTATION).setStringValue(className);
        return javaClass;
    }

    /**
     * Create a base ABAP structure Java sourcecode
     */
    public static JavaClassSource createStructureClass(String className, BapiParser bapiParser) {
        final JavaClassSource javaClass = createBaseHibersapClass(className, bapiParser);
        javaClass.addAnnotation(BAPI_STRUCTURE_ANNOTATION);
        return javaClass;
    }

    /**
     * Create a base Hibersap Java sourcecode
     */
    private static JavaClassSource createBaseHibersapClass(String className, BapiParser bapiParser) {
        final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
        javaClass.setPackage(BapiConstants.TARGET_JAVA_PACKAGE).setName(className);
        for (String defaultImport : DEFAULT_IMPORTS) {
            javaClass.addImport(defaultImport);
        }

        javaClass.addMethod().setConstructor(true).setPublic();
        javaClass.getJavaDoc()
                .addTagValue("@sapsystem", bapiParser.getjCoDestination().getDestinationName())
                .addTagValue("@date", new SimpleDateFormat("yyyy.MM.dd").format(new Date()))
                .setText("Automatically generated from SAP ERP Function Module " + bapiParser.getFUNCTION_MODULE());
        return javaClass;
    }

}
