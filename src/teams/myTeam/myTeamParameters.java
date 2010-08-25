package teams.myTeam;

/**
 * Klasse die die einstellbaren Parameter für myTeam kapselt.
 * @author daniel
 */
public class myTeamParameters {
	
	//{ParmeterValue, Min, Max}
	private double ERSTER[] =                    {236.6,0,1000};//83;// 10;
	private double FAKTOR_VORNE[] =              {36.1,0,100};// 1;
	private double GEGNER_UEBERHOLEN[] =         {950.9,0,1000};// 900;
	private double FAKTOR_VERFOLGE_GEGNER[] =    {37.8,0,100};//751;// 10;
	private double WEG_VON_GEGENSPIELER[] =      {315.6,0,1000};// 20;
	private double INS_HAUS_BEI_SCHLAGRISIKO[] = {276.0,0,1000};// 1000;
	private double SCHLAGEN[] =                  {805.7,0,1000};// 100;
	
	
	public static final int ANZAHL_PARAMETER = 7;
	
	private double[] getParam(int i) {
		switch (i) {
		case 0: return ERSTER;
		case 1: return FAKTOR_VORNE;
		case 2: return GEGNER_UEBERHOLEN;
		case 3: return FAKTOR_VERFOLGE_GEGNER;
		case 4: return WEG_VON_GEGENSPIELER;
		case 5: return INS_HAUS_BEI_SCHLAGRISIKO;
		case 6: return SCHLAGEN;
		default:
			System.err.println("myTeamParameters.getParam: Parameter gibt es nicht!");
			return null;
		}
	}
	
	public double getMin(int i) {
		return getParam(i)[1];
	}
	
	public double getMax(int i) {
		return getParam(i)[2];
	}

	public double getParameter(int i) {
		return getParam(i)[0];
	}	
	
	public void setParameter(int i, double value) {
		//es muessen beide variablen gesetzt werden:
		getParam(i)[0] = value;
	}
	
	public void setParameters(double values[]) {
		if(values.length!=ANZAHL_PARAMETER)
			System.out.println("myTeamParameters.setParameters(double[]): Warning: Unkorrekte Anzahl von Parametern!");
		for(int i=0; i<values.length; i++)
			setParameter(i, values[i]);
	}
	
	public double[] getParameters() {
		double result[] = new double[ANZAHL_PARAMETER];
		for(int i=0; i<ANZAHL_PARAMETER; i++)
			result[i] = getParameter(i);
		return result;
	}
	
	/**
	 * @param d
	 * @param decimalPlaces
	 * @return String representation of d with correct decimal places.
	 */
	public static String doubleToString(double d, int decimalPlaces) {
		String result = Double.toString(d);
		int dot = result.indexOf('.');
		int end;
		if(dot==-1)
			end = result.length();
		else
			end = Math.min(dot+decimalPlaces+1, result.length());
		return result.substring(0, end);
	}

	public double getERSTER() {
		return ERSTER[0];
	}

	public void setERSTER(double a_ERSTER) {
		ERSTER[0] = a_ERSTER;
	}

	public double getFAKTOR_VORNE() {
		return FAKTOR_VORNE[0];
	}

	public void setFAKTOR_VORNE(double a_FAKTORVORNE) {
		FAKTOR_VORNE[0] = a_FAKTORVORNE;
	}

	public double getGEGNER_UEBERHOLEN() {
		return GEGNER_UEBERHOLEN[0];
	}

	public void setGEGNER_UEBERHOLEN(double a_GEGNERUEBERHOLEN) {
		GEGNER_UEBERHOLEN[0] = a_GEGNERUEBERHOLEN;
	}

	public double getFAKTOR_VERFOLGE_GEGNER() {
		return FAKTOR_VERFOLGE_GEGNER[0];
	}

	public void setFAKTOR_VERFOLGE_GEGNER(double a_FAKTORVERFOLGEGEGNER) {
		FAKTOR_VERFOLGE_GEGNER[0] = a_FAKTORVERFOLGEGEGNER;
	}

	public double getWEG_VON_GEGENSPIELER() {
		return WEG_VON_GEGENSPIELER[0];
	}

	public void setWEG_VON_GEGENSPIELER(double a_WEGVONGEGENSPIELER) {
		WEG_VON_GEGENSPIELER[0] = a_WEGVONGEGENSPIELER;
	}

	public double getINS_HAUS_BEI_SCHLAGRISIKO() {
		return INS_HAUS_BEI_SCHLAGRISIKO[0];
	}

	public void setINS_HAUS_BEI_SCHLAGRISIKO(double a_INSHAUSBEISCHLAGRISIKO) {
		INS_HAUS_BEI_SCHLAGRISIKO[0] = a_INSHAUSBEISCHLAGRISIKO;
	}

	public double getSCHLAGEN() {
		return SCHLAGEN[0];
	}

	public void setSCHLAGEN(double a_SCHLAGEN) {
		SCHLAGEN[0] = a_SCHLAGEN;
	}
}
