package org.hibersap.generator.jco;

import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;

import java.util.Properties;

/**
 * Class to dynamically inject a JCO destination without a jcoDestination file
 */
@Deprecated
public class CustomDestinationDataProvider implements DestinationDataProvider {
    private DestinationDataEventListener eL;
    private final Properties connectProperties;

    public CustomDestinationDataProvider(Properties connectProperties) {
        this.connectProperties = connectProperties;
    }

    @Override
    public Properties getDestinationProperties(String destinationName) {
        return connectProperties;
    }

    @Override
    public void setDestinationDataEventListener(DestinationDataEventListener eventListener) {
        this.eL = eventListener;
    }

    @Override
    public boolean supportsEvents() {
        return true;
    }

}