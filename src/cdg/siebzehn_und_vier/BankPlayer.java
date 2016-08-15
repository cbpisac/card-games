package cdg.siebzehn_und_vier;

import java.util.Random;

public class BankPlayer extends AbstractPlayer {

	private int minEinsatz;

	public BankPlayer() {
		konto = 5000;
		name = "Bank";
	}

	/**
	 * Getter für einsatz
	 * 
	 * @return einsatz
	 * 
	 */
	public int getEinsatz() {
		return einsatz;
	}

	/**
	 * Bank setzt den Einsatz
	 * 
	 * @param numPlayer
	 *            Anzahl der Spieler
	 * @see cdg.siebzehn_und_vier.AbstractPlayer#placingPool(int, int)
	 * 
	 */
	public int placingPool( int numPlayer ) {
		Random rand = new Random();

		minEinsatz = super.minEinsatz * numPlayer;

		if ( konto > 30) {
			einsatz = (int)( Utilities.normalDistribution( 0.4 ) * konto / 4 + minEinsatz );
		}
		else {
			einsatz = rand.nextInt(konto) + 1;
		}
	
		return einsatz;
	}

	/**
	 * Diese Methode wird nicht unterstützt
	 * 
	 */
	@Override
	public int placingPool( PoolRecord poolRecord, IInputDisplay<Integer> idisp ) {
		throw new UnsupportedOperationException(
				"checkPook(int, int, InputDisplay) is not supported..." );
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( super.toString() );
		sb.append( "\nminEinsatz=(" + minEinsatz + "), einsatz=(" + einsatz + ")" );
		return sb.toString();
	}

	@Override
	public void needMoreCard( IInputDisplay<Character> cdisp ) {
		if (summe >= 18) {
			noMoreCards = true;
		}
	}

}
