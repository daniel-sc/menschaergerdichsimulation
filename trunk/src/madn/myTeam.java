package madn;

import java.util.Set;

/**
 * naiver Spieler, der wenn kein Spieler zu schlagen ist immer den
 * vorderst moeglichsten Spieler zieht. Ansonsten wird ein nicht 
 * definierter Spieler geschlagen.
 * @author daniel
 */
public class myTeam implements Team {

	int teamNr = -1;
	
	/**
	 * gibt den Zug mit den meisten {@link Zug#punkte} zurueck
	 * @param zuege
	 * @return
	 */
	Zug besterZug(Set<Zug> zuege) {
		int max = Integer.MIN_VALUE;
		Zug ergebnis = null;
		for (Zug zug : zuege) {
			if(zug.punkte > max) {
				max = zug.punkte;
				ergebnis = zug;
			}
		}
		return ergebnis;
	}
	
	public Zug ziehen(Spielfeld feld, int aktWurf) {
		Set<Zug> zuege = feld.moeglicheZuege(teamNr, aktWurf);
		
		//triviale Faelle:
		if(zuege.size()==0) return null;
		if(zuege.size()==1) return zuege.iterator().next();
		
		int erstePos = -1;
		for (Zug zug : zuege) {
			if(zug.schlagen)
				zug.punkte += 100;
			//auf jeden fall ins haus falls man evtl geschlagen wird:
			if(zug.ziel>=100 && feld.entfernungGegenspieler(zug.start, teamNr)<7)
				zug.punkte += 1000;
			if(zug.ziel>erstePos)
				erstePos = zug.ziel;
		}
		for (Zug zug : zuege) {
			if(zug.ziel==erstePos)
				zug.punkte += 10;
		}
		return besterZug(zuege);
	}
	
	@Override
	public void setTeamNr(int nr) {
		teamNr = nr;
	}

}
