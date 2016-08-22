
public class SimpleLabeler {

	
	public String produceLabel (String name, double value)
	{
	
		if (value >= 0.98 && value < 1.0)
			return Label.A_PPP;
		
		
		else if (value >=0.95 && value <0.98 )
			return Label.A_PP;
					
		else if (value >=0.9 && value < 0.95 )
			return Label.A_P;
		
		else if (value < 0.9 && value >=0.8)
			return Label.A;
		
		else if (value >=0.6 && value < 0.8 )
			return Label.B;
		
		else if (value >=0.2 && value < 0.6 )
			return Label.C;
		
		else if (value >=0 && value < 0.2 )
			return Label.D;
		
		
					
		return "*ERROR*";
		
	}
	
}
