
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

	private int PKT_ERSTER =868;//83;// 10;
	private int PKT_FAKTOR_VORNE =604;// 1;
	private int PKT_GEGNER_UEBERHOLEN =23;// 900;
	private int PKT_VERFOLGE_GEGNER =703;// 10;
	private int PKT_WEG_VON_GEGENSPIELER =449;// 20;
	private int PKT_INS_HAUS_BEI_SCHLAGRISIKO =375;// 1000;
	private int PKT_SCHLAGEN =546;// 100;
	
	int teamNr = -1;
	
	/**
	 * setzt die parameter in folgender Reihenfolge:<br>
	 *  PKT_ERSTER,
	*	PKT_FAKTOR_VORNE,
	*	PKT_GEGNER_UEBERHOLEN, 
	*	PKT_VERFOLGE_GEGNER, 
	*	PKT_WEG_VON_GEGENSPIELER, 
	*	PKT_INS_HAUS_BEI_SCHLAGRISIKO, 
	*	PKT_SCHLAGEN 
	 * @param param
	 */
	public void setParameters(int param[]) {
		if(param.length!=7)
			System.err.println("Falsche Anzahl von Parametern!!");
		PKT_ERSTER = param[0];
		PKT_FAKTOR_VORNE = param[1];
		PKT_GEGNER_UEBERHOLEN = param[2];
		PKT_VERFOLGE_GEGNER = param[3];
		PKT_WEG_VON_GEGENSPIELER = param[4];
		PKT_INS_HAUS_BEI_SCHLAGRISIKO = param[5];
		PKT_SCHLAGEN = param[6];
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
				zug.punkte += PKT_SCHLAGEN;
			
			//auf jeden fall ins haus falls man evtl geschlagen wird:
			if(zug.ziel>=100 && feld.entfernungGegenspielerZurueck(zug.start, teamNr)<7)
				zug.punkte += PKT_INS_HAUS_BEI_SCHLAGRISIKO;
			
			//wenn man durch ziehen aus der reichweite eines gegenspielers kommt:
			if((feld.entfernungGegenspielerZurueck(zug.start, teamNr)<7 
					&& feld.entfernungGegenspielerZurueck(zug.ziel, teamNr)>=7)
					|| zug.start%10==0)
				zug.punkte += PKT_WEG_VON_GEGENSPIELER;
			
			//wenn man durch ziehen naeher an einen gegenspieler kommt (und nicht ueberholt)
			//der schon 3 im haus hat:
			for(int i=0; i<feld.anzTeams; i++) {
				if(i==teamNr) continue;
				/*TODO: evtl anzSpielerImHaus als Faktor??*/
				if(feld.anzSpielerImHaus(i)==3) {
					int entfernung = feld.entfernungGegenspielerVorwaerts(zug.ziel, teamNr, i);
					if(entfernung<Integer.MAX_VALUE) {
						zug.punkte += (41-entfernung)*PKT_VERFOLGE_GEGNER;
					}
				}
			}
			
			//wenn man durch ziehen naeher an einen gegenspieler kommt und ueberholt:
			if(feld.entfernungGegenspielerZurueck(zug.start, teamNr)>6/*TODO: oder 5?*/ 
					&& feld.entfernungGegenspielerZurueck(zug.ziel, teamNr)<=6)
				zug.punkte -= PKT_GEGNER_UEBERHOLEN;
			
			//je weiter vorne, je wichtiger: (Faktor?, linear??) (bringt viel :) )
			zug.punkte += PKT_FAKTOR_VORNE*Spielfeld.distanz(Spielfeld.startfeld[teamNr], zug.start);
			
			if(zug.ziel>erstePos)
				erstePos = zug.ziel;
		}
		for (Zug zug : zuege) {
			if(zug.ziel==erstePos)
				zug.punkte += PKT_ERSTER;
		}
		return besterZug(zuege);
	}
	
	@Override
	public void setTeamNr(int nr) {
		teamNr = nr;
	}

}
