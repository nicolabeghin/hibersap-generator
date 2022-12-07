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

    private static final String[] DEFAULT_IMPORTS = new String[]{
            "org.hibersap.annotations.*",
            BapiConstants.TARGET_JAVA_PACKAGE_STRUCTURE,
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
        javaClass.addAnnotation(org.hibersap.annotations.Bapi.class).setStringValue(className);
        return javaClass;
    }

    /**
     * Create a base ABAP structure Java sourcecode
     */
    public static JavaClassSource createStructureClass(String className, BapiParser bapiParser) {
        final JavaClassSource javaClass = createBaseHibersapClass(className, bapiParser);
        javaClass.addAnnotation(org.hibersap.annotations.BapiStructure.class);
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
