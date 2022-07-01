package org.hibersap.generator.utils;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import java.io.File;
import java.util.Properties;

/**
 * Auxiliary class to parse command-line
 */
public class HibersapCommandLine {
    private static final String SAP_FUNCTION_MODULE = "sapFunctionModule";
    private static final String SAP_DESTINATION = "sapDestination";
    private final String[] args;

    private static final String[] MANDATORY_COMMAND_LINE = new String[]{
            SAP_DESTINATION,
            SAP_FUNCTION_MODULE
    };

    public HibersapCommandLine(String[] args) {
        this.args = args;
    }

    private static Options buildCommandLineOptions() {
        Options options = new Options();
        options.addOption(SAP_DESTINATION, true, "SAP destination (\"destination.jcoDestination\" must exist");
        options.addOption(SAP_FUNCTION_MODULE, true, "SAP function module to be parsed");
        return options;
    }

    public static Properties parseCommandLine(String[] args) throws Exception {
        CommandLine cmd = new DefaultParser().parse(buildCommandLineOptions(), args);
        Properties connectProperties = new Properties();
        for (String mandatoryOption : MANDATORY_COMMAND_LINE) {
            if (!cmd.hasOption(mandatoryOption)) {
                throw new Exception("Missing mandatory option -" + mandatoryOption);
            }
            connectProperties.setProperty(mandatoryOption, cmd.getOptionValue(mandatoryOption));
        }
        return connectProperties;
    }

    public String getSapFunctionModule() throws Exception {
        return parseCommandLine(args).getProperty(SAP_FUNCTION_MODULE);
    }

    public JCoDestination getJcoDestination() throws Exception {
        String jcoDestination = parseCommandLine(args).getProperty(SAP_DESTINATION);
        File jcoDestinationSettings = new File(jcoDestination + ".jcoDestination");
        if (!jcoDestinationSettings.exists()) {
            throw new Exception(jcoDestinationSettings + " does not exist at " + jcoDestinationSettings.getAbsolutePath());
        }
        return JCoDestinationManager.getDestination(jcoDestination);
    }

}
