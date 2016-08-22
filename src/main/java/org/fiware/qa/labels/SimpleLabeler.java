package org.fiware.qa.labels;

public class SimpleLabeler {

	
	public String produceLabel (double value)
	{
	
		
		if (value >= 0.96 && value < 1.0)
			return Label.A_PPP;
		
		
		else if (value >=0.9 && value <0.96 )
			return Label.A_PP;
					
		else if (value >=0.8 && value < 0.75 )
			return Label.A_P;
		
		else if (value < 0.75 && value >=0.65)
			return Label.A;
		
		else if (value >=0.5 && value < 0.65 )
			return Label.B;
		
		else if (value >=0.4 && value < 0.6 )
			return Label.C;
		
		else if (value >=0 && value < 0.4 )
			return Label.D;
		
					
		return "*ERROR*";
		
	}
	
	public String produceLinearLabel (double value)
	{
	
		
		if (value >= 0.9 && value < 1.0)
			return Label.A_PPP;
		
		
		else if (value >=0.8 && value <0.9 )
			return Label.A_PP;
					
		else if (value >=0.7 && value < 0.8 )
			return Label.A_P;
		
		else if (value < 0.6 && value >=0.7)
			return Label.A;
		
		else if (value >=0.5 && value < 0.6 )
			return Label.B;
		
		else if (value >=0.4 && value < 0.5 )
			return Label.C;
		
		else if (value >=0 && value < 0.4 )
			return Label.D;
		
					
		return "*ERROR*";
		
	}
	
	
}
