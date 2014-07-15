/**
 * 
 */
package com.newrelic.gqmaps;


import com.newrelic.metrics.publish.Runner;
import com.newrelic.metrics.publish.configuration.ConfigurationException;

/**
 * @author parveen
 * Main class for gqmaps Agent
 */

public class Main {

	public static void main(String[] args) {
		try {
            Runner runner = new Runner();
            runner.add(new GqmapsAgentFactory());
            runner.setupAndRun(); // Never returns
        } catch (ConfigurationException e) {
            System.err.println("ERROR: " + e.getMessage());
            System.exit(-1);
        }
	}
}
