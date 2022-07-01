package org.hibersap.generator;

import org.hibersap.generator.parser.BapiParser;
import org.hibersap.generator.utils.HibersapCommandLine;

public class Main {

    public static void main(String[] args) {
        try {
            HibersapCommandLine hibersapCommandLine = new HibersapCommandLine(args);
            BapiParser bapiParser = new BapiParser(hibersapCommandLine.getSapFunctionModule(), hibersapCommandLine.getJcoDestination());
            bapiParser.parse();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

}
