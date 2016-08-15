package cdg.siebzehn_und_vier;


/**
 * 
 * @author Carsten
 *
 */
public class PoolRecord {
	// maximaler Einsatz (auch für Bankeinsatz bei 17+4)
	private int maxEinsatz;
	
	// Summe aller Einsätze der Spieler, die schon gesetzt haben
	private int sumAllPlayer;
	
	// Anzahl der Spieler, die noch setzen müssen
	private int countPlayerRest;
	
	
	public PoolRecord(int playerNumber) {
		countPlayerRest = playerNumber;
	}
	
	/**
	 * @return the maxEinsatz
	 */
	public int getMaxEinsatz() {
		return maxEinsatz;
	}
	/**
	 * @param maxEinsatz the maxEinsatz to set
	 */
	public void addMaxEinsatz( int maxEinsatz ) {
		this.maxEinsatz += maxEinsatz;
	}
	/**
	 * @return the sumAllPlayer
	 */
	public int getSumAllPlayer() {
		return sumAllPlayer;
	}
	/**
	 * @param sumAllPlayer the sumAllPlayer to set
	 */
	public void addSumAllPlayer( int sumAllPlayer ) {
		this.sumAllPlayer += sumAllPlayer;
	}
	/**
	 * @return the countPlayerRest
	 */
	public int getCountPlayerRest() {
		return countPlayerRest;
	}
	/**
	 * @param countPlayerRest the countPlayerRest to set
	 */
	public void decCountPlayerRest() {
		countPlayerRest--;
	}
}
