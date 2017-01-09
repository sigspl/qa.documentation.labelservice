package org.fiware.qa.documentation.measurements.models;

public class EnablerDescription {

	public String name="";
	public String overview="";
	public String creating_instances="";
	public String documentation="";
	public String downloads="";
	public String instances="";
	public String terms_conditions="";
	public String text="";
	public String meta="";
	public String url="";
	
	public boolean corruptMeta=false;
	
	
	public String toString()
	{
		return name + " / " + url; 
				
	}
	
	public void report()
	{
		System.out.println(name);
		System.out.println(url);
		System.out.println("overview: " + overview);
		System.out.println("creating instances: " + creating_instances);
		System.out.println("documentation: " + documentation);
		System.out.println("downloads: " + downloads);
		System.out.println("instances: " + instances);
		System.out.println("terms&conditions: " + terms_conditions);
		System.out.println("accumulated text length: " +  text.length());
		
	}
	
}
