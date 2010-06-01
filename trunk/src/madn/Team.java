package madn;

/**
 * Interface fuer alle Teams.
 * @author daniel
 *
 */
public interface Team {

	/**
	 * Entscheidungsfunktion welche figur gezogen werden soll.
	 * Sinnvollerweise sollte {@link Spielfeld#moeglicheZuege(int, int)}
	 * aufgerufen werden und aus diesen Zuegen ein Zug ausgewaehlt werden.
	 * @param feld das aktuelle spielfeld
	 * @param aktWurf die aktuelle wuerfelzahl
	 * @return zug den man durchfuehren moechte oder null falls man nicht ziehen moechte/kann
	 */
	Zug ziehen(Spielfeld feld, int aktWurf);
	
	/**
	 * muss aufgerufen werden vor dem ersten aufruf von 'ziehen'!
	 * setter ist einfacher als konstruktor wegen dynamischer klassenladung..
	 * @param nr
	 */
	void setTeamNr(int nr);

}
