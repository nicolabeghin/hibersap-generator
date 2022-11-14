package org.hibersap.generator.jco;

import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoMetaData;
import com.sap.conn.jco.JCoRecord;
import org.hibersap.generator.base.AbstractBaseGenerator;

/**
 * Mapping ABAP types to Java ones
 */
public final class AbapToJavaTypeMapper extends AbstractBaseGenerator {

    public static String getJavaTypeFromAbap(JCoField jCoField) throws Exception {
        if (isComplexType(jCoField)) {
            return String.format("List<%s>", cleanupClassName(jCoField));
        }
        switch (jCoField.getType()) {
            case JCoMetaData.TYPE_CHAR:
            case JCoMetaData.TYPE_STRING:
            case JCoMetaData.TYPE_NUM:
                return "String";
            case JCoMetaData.TYPE_INT:
            case JCoMetaData.TYPE_INT1:
            case JCoMetaData.TYPE_INT2:
            case JCoMetaData.TYPE_INT8:
                return "int";
            case JCoMetaData.TYPE_DATE:
            case JCoMetaData.TYPE_TIME:
                return "Date";
            case JCoMetaData.TYPE_DECF16:
            case JCoMetaData.TYPE_DECF34:
            case JCoMetaData.TYPE_BCD:
                return "BigDecimal";
            case JCoMetaData.TYPE_XSTRING:
                return "byte[]";
            case JCoMetaData.TYPE_BYTE:
                return "byte";
            case JCoMetaData.TYPE_FLOAT:
                return "Double";
            default:
                throw new Exception("Unhandled type " + jCoField.getTypeAsString() + "(code " + jCoField.getType() + ")");
        }
    }

    private static String cleanupClassName(JCoField jCoField) {
        return cleanupClassName(jCoField.getRecordMetaData().getName());
    }

    public static String cleanupClassName(JCoRecord jCoField) {
        return cleanupClassName(jCoField.getMetaData().getName());
    }

    private static String cleanupClassName(String className) {
        String originalClassName = className;
        if (className.startsWith("/")) { // remove leading slash
            className = className.substring(1);
        }
        if (className.contains("/")) { // remove slashes in class name
            className = className.replaceAll("/", "_");
            LOG.warning("Renamed ABAP class " + originalClassName + " to " + className);
        }
        return className;
    }

    public static boolean isComplexType(JCoField jCoField) {
        switch (jCoField.getType()) {
            case JCoMetaData.TYPE_STRUCTURE:
            case JCoMetaData.TYPE_TABLE:
                return true;
            default:
                return false;
        }
    }
}
