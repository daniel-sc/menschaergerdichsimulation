/**
 * 
 */
package madn;

/**
 * Team dass NICHT zieht.
 * Kann insbesondere genutzt werden wenn mit weniger Teams gespielt
 * werden soll.
 * @author daniel
 * @deprecated es muss gezogen werden!
 */
public class dummyTeam implements Team {

	/* (non-Javadoc)
	 * @see madn.Team#setTeamNr(int)
	 */
	@Override
	public void setTeamNr(int nr) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see madn.Team#ziehen(madn.Spielfeld, int)
	 */
	@Override
	public Zug ziehen(Spielfeld feld, int aktWurf) {
		// TODO Auto-generated method stub
		return null;
	}

}
