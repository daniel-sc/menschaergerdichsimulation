package madn;

public interface Team {

	/**
	 * 
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
