package madn;

import java.util.HashSet;
import java.util.Set;



public class Spielfeld {

	int spielfeld_team[] = new int[40]; //belegte felder haben teamnr, sonst -1
	int spielfeld_figur[] = new int[40]; //belegte felder haben figurnr, sonst -1

	int anzTeams = 4; //kann spaeter angepasst werden
	int figuren[][] = new int[anzTeams][4]; //feldpositionen der figuren, -1:start 10X:haus

	int haeuschen[][] = new int[anzTeams][4];

	int startfeld[] = new int[]{0,10,20,30};

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
	 * 
	 * @param team
	 * @param figur
	 * @param wurf
	 * @return false, falls der zug nicht moeglich war. true sonst.
	 */
	public Boolean zieheSpieler(Zug zug) {
		Set<Zug> moeglicheZuege = moeglicheZuege(zug.team, zug.wurf);
		if(!moeglicheZuege.contains(zug)) return false;
		//zug ist moeglich!
		int team = zug.team;
		int figur = zug.figur;
		int wurf = zug.wurf;

		if(figuren[team][figur]==-1) { //wenn figur am start
			if(spielfeld_team[startfeld[team]]!=-1) //schlagen
				figuren[spielfeld_team[startfeld[team]]][spielfeld_figur[startfeld[team]]] = -1;

			spielfeld_figur[startfeld[team]] = figur;
			spielfeld_team[startfeld[team]] = team;
			figuren[team][figur] = startfeld[team];
			return true;
		}

		if(figuren[team][figur]>100) { //wenn figur im haeuschen
			int start=figuren[team][figur]-100-1;
			int ziel =start+wurf;
			haeuschen[team][ziel] = figur;
			haeuschen[team][start] = -1;
			figuren[team][figur] += wurf;
			return true;
		}

		if(startfeld[team]==figuren[team][figur] 
		                                  || (startfeld[team]-figuren[team][figur]+40)%40>wurf) { //nicht ins haeuschen
			int zielpos = (figuren[team][figur]+wurf)%40;
			if(spielfeld_team[zielpos]!=-1) //schlagen
				figuren[spielfeld_team[zielpos]][spielfeld_figur[zielpos]] = -1;

			spielfeld_figur[figuren[team][figur]] = -1;
			spielfeld_team[figuren[team][figur]] = -1;
			spielfeld_figur[zielpos] = figur;
			spielfeld_team[zielpos] = team;
			figuren[team][figur] = zielpos;

		} else { //ins haeuschen?
			//System.out.println("ins haus..");
			int hausnr = ((-startfeld[team]+figuren[team][figur]+wurf+40)%40)+1; //1-4 !

			haeuschen[team][hausnr-1] = figur;					
			spielfeld_team[figuren[team][figur]] = -1;
			spielfeld_figur[figuren[team][figur]] = -1;
			figuren[team][figur] = 100+hausnr;
		}
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
	 * 
	 * @return gibt die nr des gewinnerteams zurueck oder -1 falls das spiel nicht zuende ist.
	 */
	public int ende() {
		for(int team=0; team<anzTeams; team++) {
			int f=0;
			for(f=0; f<4; f++) {
				if(haeuschen[team][f]==-1)
					break;
			}
			if(f==4) return team;
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
			Boolean kannZiehen = true;
			if(figuren[team][figur]==-1) //aus haus ziehen haben wir oben behandelt
				continue;

			if(figuren[team][figur]>100) { //wenn figur im haeuschen
				if(figuren[team][figur]-100+wurf>4)
					continue;
				int start=figuren[team][figur]-100-1;
				int ziel =start+wurf;
				for(int i=start+1; i<ziel+1; i++) {
					if(haeuschen[team][i]!=-1)
						kannZiehen = false;
				}
				if(kannZiehen)
					ergebnis.add(new Zug(wurf,
							team,
							figur,
							figuren[team][figur],
							figuren[team][figur]+wurf,
							false,
							-1));
				continue;
			}

			if(startfeld[team]==figuren[team][figur] 
			                                  || (startfeld[team]-figuren[team][figur]+40)%40>wurf) { //nicht ins haeuschen
				int zielpos = (figuren[team][figur]+wurf)%40;
				if(spielfeld_team[zielpos]==team) 
					continue; //zug nicht moeglich!
				else
					ergebnis.add(new Zug(wurf,
							team,
							figur,
							figuren[team][figur],
							zielpos,
							spielfeld_team[zielpos]!=-1,
							spielfeld_team[zielpos]==-1 ? -1 : spielfeld_team[zielpos]));

				continue;

			} else { //ins haeuschen?
				int hausnr = ((-startfeld[team]+figuren[team][figur]+wurf+40)%40)+1; //1-4 !
				if(hausnr>4)
					continue; // zug nicht moeglich!
				for(int k=0; k<hausnr; k++) //im haeuschen darf nicht uebersprungen werden
					if(haeuschen[team][k]>=0)
						kannZiehen = false; //zug nicht möglich!

				if(kannZiehen)
					ergebnis.add(new Zug(wurf,
							team,
							figur,
							figuren[team][figur],
							100+hausnr,
							false,
							-1));
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

