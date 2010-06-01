package madn;

import java.util.Set;

/**
 * naiver Spieler, der wenn kein Spieler zu schlagen ist immer den
 * letzten moeglichsten Spieler zieht. Ansonsten wird ein nicht 
 * definierter Spieler geschlagen.
 * @author daniel
 */
public class zieheLetzten implements Team {

	int teamNr = -1;
	
	public Zug ziehen(Spielfeld feld, int aktWurf) {
		Set<Zug> zuege = feld.moeglicheZuege(teamNr, aktWurf);
		int letztePos = 200;
		for (Zug zug : zuege) {
			if(zug.schlagen) //zur Zeit egal wo geschlagen wird
				return zug;
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
