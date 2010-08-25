
package teams.myTeam;

import java.util.Set;

import madn.Spielfeld;
import madn.Team;
import madn.Zug;

/**
 * Spieler der mit Genetischen Algorithmen optimiert wird.
 * @author daniel
 */
public class myTeam implements Team {	

	private myTeamParameters parameter;
	int teamNr = -1;

	public myTeam() {
		super();
		parameter = new myTeamParameters();
	}

	/**
	 * @param param setzt die parameter auf param - macht keine Kopie,
	 * dh wenn param spaeter geaendert wird aendern sich auch die parameter
	 * in der Instanz!
	 */
	public void setParameters(myTeamParameters param) {
		parameter = param;
	}

	/**
	 * gibt den Zug mit den meisten {@link Zug#punkte} zurueck
	 * @param zuege
	 * @return
	 */
	Zug besterZug(Set<Zug> zuege) {
		double max = Double.NEGATIVE_INFINITY;
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
				zug.punkte += parameter.getSCHLAGEN();

			//auf jeden fall ins haus falls man evtl geschlagen wird:
			if(zug.ziel>=100 && feld.entfernungGegenspielerZurueck(zug.start, teamNr)<7)
				zug.punkte += parameter.getINS_HAUS_BEI_SCHLAGRISIKO();

			//wenn man durch ziehen aus der reichweite eines gegenspielers kommt:
			if((feld.entfernungGegenspielerZurueck(zug.start, teamNr)<7 
					&& feld.entfernungGegenspielerZurueck(zug.ziel, teamNr)>=7)
					|| zug.start%10==0)
				zug.punkte += parameter.getWEG_VON_GEGENSPIELER();

			//wenn man durch ziehen naeher an einen gegenspieler kommt (und nicht ueberholt)
			//der schon 3 im haus hat:
			for(int i=0; i<feld.anzTeams; i++) {
				if(i==teamNr) continue;
				/*TODO: evtl anzSpielerImHaus als Faktor??*/
				if(feld.anzSpielerImHaus(i)==3) {
					int entfernung = feld.entfernungGegenspielerVorwaerts(zug.ziel, teamNr, i);
					if(entfernung<Integer.MAX_VALUE) {
						zug.punkte += (41-entfernung)*parameter.getFAKTOR_VERFOLGE_GEGNER();
					}
				}
			}

			//wenn man durch ziehen naeher an einen gegenspieler kommt und ueberholt:
			if(feld.entfernungGegenspielerZurueck(zug.start, teamNr)>6/*TODO: oder 5?*/ 
					&& feld.entfernungGegenspielerZurueck(zug.ziel, teamNr)<=6)
				zug.punkte -= parameter.getGEGNER_UEBERHOLEN();

			//je weiter vorne, je wichtiger: (Faktor?, linear??) (bringt viel :) )
			zug.punkte += parameter.getFAKTOR_VORNE()*Spielfeld.distanz(Spielfeld.startfeld[teamNr], zug.start);

			if(zug.ziel>erstePos)
				erstePos = zug.ziel;
		}
		for (Zug zug : zuege) {
			if(zug.ziel==erstePos)
				zug.punkte += parameter.getERSTER();
		}
		return besterZug(zuege);
	}

	@Override
	public void setTeamNr(int nr) {
		teamNr = nr;
	}

}

