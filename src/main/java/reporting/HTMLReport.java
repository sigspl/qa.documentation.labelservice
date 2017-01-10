package reporting;

import org.fiware.qa.documentation.measurements.PlainMetricProtocol;

public class HTMLReport {

	
	private String begin="<html><head><title>Catalogue Compliance report</title></head><body>";
	private String end="</body></html>";
	private String header="<h1>Catalogue Compliance report</h1>";
	
	private String contents;
	
	public void generate()
	{
		String output = begin + header + contents + end;
	}

	public void setProtocol(PlainMetricProtocol protocol) {
		// TODO Auto-generated method stub
		
	}
	
	
}
