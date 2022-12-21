package org.hibersap.generator.parser;

import com.sap.conn.jco.*;
import org.hibersap.generator.base.AbstractBaseGenerator;
import org.hibersap.generator.base.BaseBapiGenerator;
import org.hibersap.generator.jco.AbapToJavaTypeMapper;
import org.hibersap.generator.jco.JCoDirection;
import org.hibersap.generator.writer.BapiFileWriter;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.PropertySource;

public class BapiParser extends AbstractBaseGenerator {
    private final String FUNCTION_MODULE;
    private final JCoDestination jCoDestination;

    public BapiParser(String FUNCTION_MODULE, JCoDestination jCoDestination) {
        this.FUNCTION_MODULE = FUNCTION_MODULE;
        this.jCoDestination = jCoDestination;
        setupFunctionModuleFileLogger(FUNCTION_MODULE);
    }

    /**
     * Entrpoint method
     */
    public void parse() throws Exception {
        LOG.info("Parsing SAP RFC " + FUNCTION_MODULE);
        JCoFunction functionModule = jCoDestination.getRepository().getFunction(FUNCTION_MODULE);
        LOG.info("Successfully retrieved RFC");
        JavaClassSource javaClassSource = BaseBapiGenerator.createBapiClass(FUNCTION_MODULE, this);
        parseParameterList(functionModule.getImportParameterList(), JCoDirection.IMPORT, javaClassSource);
        parseParameterList(functionModule.getExportParameterList(), JCoDirection.EXPORT, javaClassSource);
        parseParameterList(functionModule.getTableParameterList(), JCoDirection.TABLE, javaClassSource);
        BapiFileWriter.write(FUNCTION_MODULE, javaClassSource);
    }

    /**
     * Generic method to handle JCO parameter list
     */
    private void parseParameterList(JCoParameterList parameters, JCoDirection jCoDirection, JavaClassSource javaClassSource) throws Exception {
        if (parameters == null) {
            LOG.warning(String.format("No %s parameter", jCoDirection.name()));
            return;
        }
        LOG.info(String.format("Found %d %s parameters", parameters.getFieldCount(), jCoDirection.name()));
        for (JCoField p : parameters) {
            LOG.info(String.format(jCoDirection.name() + " [%s]\t\ttype %s", p.getName(), p.getTypeAsString()));
            handleJCoField(p, jCoDirection, javaClassSource);
            if (p.isStructure()) {
                handleComplexType(parameters.getStructure(p.getName()));
            }
            if (p.isTable()) {
                handleComplexType(parameters.getTable(p.getName()));
            }
        }
    }

    /**
     * Handling JCO field independently of type and direction
     */
    private PropertySource handleJCoField(JCoField jCoField, JCoDirection jCoDirection, JavaClassSource javaClassSource) throws Exception {

        // create basic Java property mapping from ABAP type
        PropertySource propertySource = javaClassSource.addProperty(AbapToJavaTypeMapper.getJavaTypeFromAbap(jCoField), jCoField.getName());

        // add description from ABAP comment
        if (jCoField.getDescription() != null) {
            propertySource.getField().getJavaDoc().setText(jCoField.getDescription());
        } else {
            LOG.warning("No description for field "+jCoField.getName());
        }

        FieldSource field = propertySource.getField();

        // initialize field if complex type
        switch (jCoField.getType()) {
            case JCoMetaData.TYPE_TABLE:
                field.setLiteralInitializer(String.format("new ArrayList<%s>()", jCoField.getRecordMetaData().getName()));
                break;
            case JCoMetaData.TYPE_STRUCTURE:
                field.setLiteralInitializer(String.format("new %s()", AbapToJavaTypeMapper.cleanupClassName(jCoField)));
                break;
        }

        // add @Required annotation if not optional
        if (jCoField instanceof JCoParameterField && !((JCoParameterField) jCoField).isOptional()) {
            field.addAnnotation("javax.validation.constraints.Required");
        }

        // add Hibersap annotation for import/export/table
        if (jCoDirection != null) {
            switch (jCoDirection) {
                case IMPORT:
                    field.addAnnotation(org.hibersap.annotations.Import.class);
                    break;
                case EXPORT:
                    field.addAnnotation(org.hibersap.annotations.Export.class);
                    break;
                case TABLE:
                    field.addAnnotation(org.hibersap.annotations.Table.class);
                    break;
                default:
                    break;
            }
        }

        // add original ABAP field reference
        if (jCoField.getType() == JCoMetaData.TYPE_STRUCTURE) {
            field.addAnnotation(org.hibersap.annotations.Parameter.class).setStringValue("value", jCoField.getName()).setEnumValue("type", org.hibersap.annotations.ParameterType.STRUCTURE);
        } else {
            field.addAnnotation(org.hibersap.annotations.Parameter.class).setStringValue(jCoField.getName());
        }
        return propertySource;
    }

    /**
     * Recursive function to handle ABAP complex types (STRUCTURE and TABLE)
     */
    private void handleComplexType(JCoRecord jCoRecord) throws Exception {
        String name = AbapToJavaTypeMapper.cleanupClassName(jCoRecord);
        LOG.info("Handling complex ABAP type " + name);
        JavaClassSource javaClassSource = BaseBapiGenerator.createStructureClass(name, this);
        for (JCoField jCoField : jCoRecord) {
            handleJCoField(jCoField, null, javaClassSource);
        }
        BapiFileWriter.write(name, javaClassSource, true);
    }

    public String getFUNCTION_MODULE() {
        return FUNCTION_MODULE;
    }

    public JCoDestination getjCoDestination() {
        return jCoDestination;
    }
}
