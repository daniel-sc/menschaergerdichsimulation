package madn;

import java.util.Set;

public class zieheLetzten implements Team {

	int teamNr = -1;

	public zieheLetzten() {
		
	}
	
	public Zug ziehen(Spielfeld feld, int aktWurf) {
		Set<Zug> zuege = feld.moeglicheZuege(teamNr, aktWurf);
		int letztePos = 200;
		for (Zug zug : zuege) {
			if(zug.ziel<letztePos)
				letztePos = zug.ziel;
		}
		for (Zug zug : zuege) {
			if(zug.ziel==letztePos)
				return zug;
		}
		//sowieso kein zug moeglich:
		return null; //kommt vor wenn alle spieler im start oder haus sind
	}
	
	@Override
	public void setTeamNr(int nr) {
		teamNr = nr;
	}

}
