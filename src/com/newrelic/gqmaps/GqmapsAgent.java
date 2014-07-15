/**
 * 
 */
package com.newrelic.gqmaps;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import com.newrelic.metrics.publish.Agent;
import com.newrelic.metrics.publish.configuration.ConfigurationException;

/**
 * @author parveen
 * Agent class for GQMAPS
 * 
 */

public class GqmapsAgent extends Agent {
	
	private static final String GUID = "com.newrelic.gqmaps";
    private static final String VERSION = "1.0.0";
    
    private String name;
    private String host;
    private int countIP;
    
    private Connection conn = null;
    private Statement st = null;
    private ResultSet rs = null;
    
	/**
     * Constructor for Gqmaps Agent.
     * @throws ConfigurationException if URL for Gqmaps metric service could not be built correctly from provided host
     */
    
    public GqmapsAgent(String name, String host) throws ConfigurationException {
        super(GUID, VERSION);
        
        try {
            this.name = name;
            this.host = host;
            getConnection(host);
        } 
        catch (Exception e) {
            throw new ConfigurationException("GQMAPS metric URL Connection Error", e);
        }
    }
    
    // Get a connection for mysql database. 
    private void getConnection(String host) throws ClassNotFoundException, SQLException {
    	Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://" + host + "/gqmaps?"
                + "user=gqmaps&password=Ch1ca803ear$");
		System.out.println("DB Connected....");
    }
    
    private Integer getAssetCount() {
    	try {
    		st = conn.createStatement();
    		rs = st.executeQuery("select ((Select  count(*) a FROM comp_snpsht cs where cs.run_id =(select max(run_id) from comp_snpsht))+ (Select  count(*) b FROM prntr_snpsht ps where ps.run_id =(select max(run_id) from comp_snpsht))+ (Select  count(*) c FROM nsrg_snpsht ns where ns.run_id =(select max(run_id) from comp_snpsht)))total_asset");
    	
    		while(rs.next()) {
    			countIP = rs.getInt(1);
    		}
    		
    		System.out.println("Count of ipaddresses:" + countIP);
    	}
    	catch(Exception e) {
    		System.out.println("Exception occurred:" + e);
    	}
    	return countIP;
    }
    
	@Override
	public String getComponentHumanLabel() {
		// to return display name for plugin
		 return name; 
	}

	@Override
	public void pollCycle() {
		// TODO Auto-generated method stub
		Integer numIPaddr = getAssetCount();
		System.out.println("Number of IP addresses:" + numIPaddr);
		if (numIPaddr != null) {
            reportMetric("ActiveAssets/Count", "assets", numIPaddr);
       } else {
           //TODO: log numArticles when null
    	   System.out.println("error");
       }
	}
}
