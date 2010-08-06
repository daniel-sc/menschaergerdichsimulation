package madn;

/**
 * beschreibt einen Zug auf einem {@link Spielfeld}.
 * Der Zug muss nicht korrekt/konsistent sein!
 * @author daniel
 *
 */
public class Zug {
	
	int wurf;
	int team;
	int figur;
	
	/** haus: 10X */
	int start;
	/** haus: 10X */
	int ziel;
	
	boolean schlagen;
	int teamGeschlagen;
	
	/**
	 * kann genutzt werden um Zuege zu werten und anschliessend den besten auszuwaehlen
	 */
	int punkte = 0;
	
	/**
	 * kann nicht erlaubte zuege erstellen!
	 * @param wurf
	 * @param team
	 * @param figur
	 * @param feld
	 */
	public Zug(int wurf, int team, int figur, Spielfeld feld) {
		this.team = team;
		this.figur = figur;
		this.wurf = wurf;
		
		start = feld.figuren[team][figur];
		ziel = Spielfeld.getZiel(start, wurf, team);

		schlagen = (ziel>=0 && ziel<40 ? feld.spielfeld_team[ziel] != -1 : false);
		teamGeschlagen = schlagen ? feld.spielfeld_team[ziel] : -1;				
		
	}

	@Override
	public int hashCode() {
		return wurf
			+10*team
			+100*figur
			+1000*start
			+10000*ziel 
			/*ziel und start kann 3 stellig sein, hat aber in der mitte 
			 * immer eine null, kann also so verschachtelt codiert werden.*/
			+10000000*teamGeschlagen
			+100000000*(schlagen ? 1 : 0);
	}
	
	/**
	 * kann nicht erlaubte zuege erstellen!
	 * @param wurf
	 * @param team
	 * @param figur
	 * @param start
	 * @param ziel
	 * @param schlagen
	 * @param teamGeschlagen
	 */
	public Zug(int wurf, int team, int figur, int start, int ziel,
			boolean schlagen, int teamGeschlagen) {
		super();
		this.wurf = wurf;
		this.team = team;
		this.figur = figur;
		this.start = start;
		this.ziel = ziel;
		this.schlagen = schlagen;
		this.teamGeschlagen = teamGeschlagen;
	}

	@Override
	public boolean equals(Object obj) {
		Zug zug2 = (Zug) obj;
		if(wurf==zug2.wurf 
				&& team == zug2.team
				&& figur == zug2.figur
				&& start == zug2.start
				&& ziel == zug2.ziel
				&& schlagen==zug2.schlagen
				&& teamGeschlagen==zug2.teamGeschlagen)
			return true;
		else
			return false;
	}
	
	
}
