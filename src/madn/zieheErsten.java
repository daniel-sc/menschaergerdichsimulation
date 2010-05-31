package madn;

import java.util.Set;

public class zieheErsten implements Team {

	int teamNr = -1;

	public zieheErsten() {
		
	}
	
	public Zug ziehen(Spielfeld feld, int aktWurf) {
		Set<Zug> zuege = feld.moeglicheZuege(teamNr, aktWurf);
		int erstePos = -1;
		for (Zug zug : zuege) {
			if(zug.schlagen) //zur Zeit egal wo geschlagen wird
				return zug;
			if(zug.ziel>erstePos)
				erstePos = zug.ziel;
		}
		for (Zug zug : zuege) {
			if(zug.ziel==erstePos)
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
