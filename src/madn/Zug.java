package madn;

public class Zug {
	
	int wurf;
	int team;
	int figur;
	
	int start;
	int ziel;
	
	boolean schlagen;
	int teamGeschlagen;
	
	public Zug(int wurf, int team, int figur, Spielfeld feld) {
		this.team = team;
		this.figur = figur;
		this.wurf = wurf;
		
		start = feld.figuren[team][figur];
		if(start==-1)
			if(wurf==6)
				ziel = feld.startfeld[team];
			else
				ziel = -1;
		else
			if(start>100)
				if(start+wurf<=104)
					ziel = start+wurf;
				else
					ziel = start;
			else
				if(feld.startfeld[team]==start || (feld.startfeld[team]-start+40)%40>wurf) //nicht ins haeuschen
					ziel = start+wurf;
				else
					if(((-feld.startfeld[team]+start+wurf+40)%40)+1>4)
						ziel = start;
					else
						ziel = 100+((-feld.startfeld[team]+start+wurf+40)%40)+1;
		schlagen = (ziel>=0 && ziel<100 ? feld.spielfeld_team[ziel] != -1 : false);
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
