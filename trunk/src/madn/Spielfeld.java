package madn;

import java.util.HashSet;
import java.util.Set;



/**
 * beschreibt das Spielfeld mit den Positionen der Figuren.
 * @author daniel
 *
 */
public class Spielfeld {

	int spielfeld_team[] = new int[40]; //belegte felder haben teamnr, sonst -1
	int spielfeld_figur[] = new int[40]; //belegte felder haben figurnr, sonst -1

	int anzTeams = 4; //kann spaeter angepasst werden
	int figuren[][] = new int[anzTeams][4]; //feldpositionen der figuren, -1:start 10X:haus

	int haeuschen[][] = new int[anzTeams][4];

	static int startfeld[] = new int[]{0,10,20,30};

	/**
	 * erstellt eine Kopie des Spielfeldes 'feld'.
	 * @param feld
	 */
	public Spielfeld(Spielfeld feld) {
		super();
		for(int i=0; i<40; i++) {
			spielfeld_figur[i] = feld.spielfeld_figur[i];
			spielfeld_team[i] = feld.spielfeld_team[i];			
		}
		anzTeams = feld.anzTeams;
		for(int i=0; i<anzTeams; i++) {
			for(int k=0; k<4; k++) {
				figuren[i][k] = feld.figuren[i][k];
				haeuschen[i][k] = feld.haeuschen[i][k];
			}
		}
	}

	
	public Spielfeld() {
		for(int i=0; i<anzTeams; i++) {
			for(int k=0; k<4; k++) {
				figuren[i][k] = -1;
				haeuschen[i][k] = -1;
			}
		}
		for(int i=0; i<40; i++) {
			spielfeld_figur[i] = -1;
			spielfeld_team[i] = -1;			
		}
	}
	
	/**
	 * fuert den zug auf dem Spielfeld aus. Inkl schlagen von Figuren.
	 * Es <b>muss</b> gezogen werden, d.h. wenn ein inkorrekter zug
	 * uebergeben wurden und ein zug moeglich ist wird eine beliebige figur
	 * gezogen.
	 * @param zug
	 * @return false, falls der zug nicht moeglich war (bel. zug!). true sonst.
	 */
	public Boolean zieheSpieler(Zug zug) {
		Set<Zug> moeglicheZuege = moeglicheZuege(zug.team, zug.wurf);
		if(!moeglicheZuege.contains(zug)) {
			if(moeglicheZuege.size()>0) {
				zieheSpieler(moeglicheZuege.iterator().next());
				return false;
			} else { //es ist eh kein zug moeglich
				return true;
			}
			
		}
		//zug ist moeglich!

		if(zug.ziel>100) { //wenn figur im haeuschen
			haeuschen[zug.team][zug.ziel-100-1] = zug.figur;
			figuren[zug.team][zug.figur] = zug.ziel;
		} else {
			if(spielfeld_team[zug.ziel]!=-1) { //schlagen
				figuren[spielfeld_team[zug.ziel]][spielfeld_figur[zug.ziel]] = -1;
			}
			spielfeld_team[zug.ziel] = zug.team;
			spielfeld_figur[zug.ziel] = zug.figur;
			figuren[zug.team][zug.figur] = zug.ziel;
		}
		
		if(zug.start!=-1)
			if(zug.start<=40) {
				spielfeld_team[zug.start] = -1;
				spielfeld_figur[zug.start] = -1;
				
			} else
				haeuschen[zug.team][zug.start-100-1] = -1;

		return true;
	}

	public int anzSpielerImStart(int team) {
		int imStart=0;
		for(int i=0; i<4; i++)
			if(figuren[team][i]==-1)
				imStart++;
		return imStart;
	}

	public Set<Integer> SpielerImStart(int team) {
		Set<Integer> ergebnis = new HashSet<Integer>(4);
		for(int i=0; i<4; i++)
			if(figuren[team][i]==-1)
				ergebnis.add(i);
		return ergebnis;
	}

	/**
	 * 
	 * @param start
	 * @param wurf
	 * @param team
	 * @return das feld auf dem die figur landen wuerde ohne 
	 * mitspieler zu beachten. -1:start, 10X:ziel
	 */
	public static int getZiel(int start, int wurf, int team) {
		if(start==-1)
			if(wurf==6)
				return startfeld[team];
			else
				return -1;
		
		if(start>100)
			if(start+wurf<=104)
				return start+wurf;
			else
				return start;
		
		int naivesZiel = (start+wurf)%40;
		if((start-startfeld[team]+40)%40>(naivesZiel-startfeld[team]+40)%40) { //ins haus
			if(100+wurf-((startfeld[team]-start-1+40)%40)<=104)
				return 100+wurf-((startfeld[team]-start-1+40)%40);
			else
				return start;
		} else { //nicht ins haus
			return naivesZiel;
		}
		
	}
	
	public Set<Integer> SpielerImHaus(int team) {
		Set<Integer> ergebnis = new HashSet<Integer>(4);
		for(int i=0; i<4; i++)
			if(figuren[team][i]>100)
				ergebnis.add(i);
		return ergebnis;
	}

	public int anzSpielerImHaus(int team) {
		int imHaus=0;
		for(int i=0; i<4; i++)
			if(figuren[team][i]>100)
				imHaus++;
		return imHaus;
	}

	/**
	 * nimmt an dass nur ein team gewonnen hat!
	 * @return gibt die nr des gewinnerteams zurueck oder -1 falls das spiel nicht zuende ist.
	 */
	public int ende() {
		for(int team=0; team<anzTeams; team++) {
			if(anzSpielerImHaus(team)==4) return team;
		}
		return -1;
	}

	/**
	 * gibt eine array mit den figurennr der figuren zurueck die gezogen 
	 * werden koennen ohne die reglen zu verlezten.
	 * (man muss nicht schlagen!)
	 * @param team
	 * @param wurf
	 * @return
	 */
	public Set<Zug> moeglicheZuege(int team, int wurf) {
		Set<Zug> ergebnis = new HashSet<Zug>(4);

		if(wurf==6 && anzSpielerImStart(team)>0) { //falls 6 geworfen und Spieler im start
			if(spielfeld_team[startfeld[team]]!=team) { //wenn kein eigener spieler auf dem start sitzt
				for (int figur : SpielerImStart(team)) {
					ergebnis.add(new Zug(wurf,
							team,
							figur,
							-1,
							startfeld[team],
							spielfeld_team[startfeld[team]]!=-1,
							spielfeld_team[startfeld[team]]==-1 ? -1 : spielfeld_team[startfeld[team]]));
				}
				return ergebnis;
			} else { //eigener spieler auf start
				int aktZielFeld = (startfeld[team]+wurf)%40;
				while(spielfeld_team[aktZielFeld]==team)
					aktZielFeld = (aktZielFeld+wurf)%40;
				ergebnis.add(new Zug(wurf,
						team,
						spielfeld_figur[(aktZielFeld-wurf+40)%40],
						(aktZielFeld-wurf+40)%40,
						aktZielFeld,
						spielfeld_team[aktZielFeld]!=-1,
						spielfeld_team[aktZielFeld]==-1 ? -1 : spielfeld_team[aktZielFeld]));
				return ergebnis;
			}
		} //wir muessen nicht aus dem haus rausziehen

		for(int figur=0; figur<4; figur++) {
			int naivZiel = getZiel(figuren[team][figur], wurf, team);
			if(naivZiel==figuren[team][figur]) //dann ist es kein zug:
				continue;
			if(naivZiel<100)
				if(spielfeld_team[naivZiel]==team)
					continue;
				else {
					ergebnis.add(new Zug(wurf,
							team,
							figur,
							figuren[team][figur],
							naivZiel,
							spielfeld_team[naivZiel]!=-1 ? true : false,
							spielfeld_team[naivZiel]!=-1 ? spielfeld_team[naivZiel] : -1));
				}
			else { //naivZiel im ziel
				boolean ok = true;
				for(int i=(figuren[team][figur]>100 ? figuren[team][figur]-100 : 0); i<naivZiel-100; i++)
					if(haeuschen[team][i]!=-1)
						ok = false;
				if(ok)
					ergebnis.add(new Zug(wurf,
							team,
							figur,
							figuren[team][figur],
							naivZiel,
							false,
							-1));
				else
					continue;
					
			}
		}
		return ergebnis;
	}

	/**
	 * alte version
	 * @param team
	 * @param figur
	 * @param wurf
	 * @return
	 * @deprecated besser: zieheSpieler(Zug zug)
	 */
	public boolean zieheSpieler(int team, int figur, int wurf) {
		Zug zug = new Zug(wurf,team,figur,this);
		return zieheSpieler(zug);
	}

}

