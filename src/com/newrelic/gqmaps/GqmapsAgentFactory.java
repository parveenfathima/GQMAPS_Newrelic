package com.newrelic.gqmaps;

import java.util.Map;

import com.newrelic.metrics.publish.Agent;
import com.newrelic.metrics.publish.AgentFactory;
import com.newrelic.metrics.publish.configuration.ConfigurationException;

/**
 * @author parveen
 * AgentFactory for Gqmaps Agent
 */

public class GqmapsAgentFactory extends AgentFactory{

	@Override
	public Agent createConfiguredAgent(Map<String, Object> properties)throws ConfigurationException {
		
		String name = (String) properties.get("name");
        String host = (String) properties.get("host");
        
        if (name == null || host == null) {
            throw new ConfigurationException("'name' and 'host' cannot be null. Do you have a 'config/plugin.json' file?");
        }
        
        return new GqmapsAgent(name, host);
	}
}
