
package cdg.siebzehn_und_vier;

public class HumanPlayer extends AbstractPlayer {
	private String message; // Nachrichtenaustausch

	public HumanPlayer() {
		
	}
	
	public HumanPlayer(String name) {
		this.name = name;
	}

	/** 
	 * RealPlayer benötigt Eingaben, die im Aufrufer implementiert sind<br>
	 * Hier bleibt nur die Überprüfung der Eingabe übrig
	 * @param bankEinsatz Einsatz der Bank
	 * @see cdg.siebzehn_und_vier.AbstractPlayer#placingPool(int, int)
	 */
	public int placingPool( PoolRecord poolRecord, IInputDisplay<Integer> idisp ) {
		einsatz = 0;
		String message = "Sie haben noch %d Euro."; 

		while (true) {
			einsatz = idisp.inputDisplay( String.format( message, konto ) );
			if ( !checkPool( poolRecord ) ) {
				idisp.DisplayMessage( this.message );
			}
			else {
				break;
			}
		}
		return einsatz;
	}

	
	/**
	 * Überprüft den Einsatz des Spielers
	 * 
	 * @param einsatz eingesetzter Wert
	 * @param bankEinsatz von der Bank eingesetzter Wert
	 * @return true = Einsatz ok, false = Einsatz nicht ok 
	 */
	private boolean checkPool( PoolRecord poolRecord ) {
		boolean result = true;

		// Einsatz höher als Konto?
		if ( konto - einsatz < 0 ) {
			message = "Sie können nicht mehr bieten, als Sie haben!";
			result = false;

		} // Einsatz höher oder gleich als der Bankeinsatz (Va Banque wird nicht unterstützt)? 
		else if ( einsatz > poolRecord.getMaxEinsatz() ) {
			message = "Ihr Einsatz übersteigt den der Bank!";
			result = false;

		}	// Einsatz mit der Summe der anderen Spieler über Bankeinsatz? 
		else if ( einsatz > poolRecord.getMaxEinsatz() - poolRecord.getSumAllPlayer() - 
				minEinsatz * poolRecord.getCountPlayerRest() ) {
			message = "Ihr Einsatz übersteigt mit den der anderen den Einsatz der Bank!";
			result = false;

		} // Einsatz zu niedrig (kleiner als minimaler Einsatz)?
		else if ( einsatz != 0 && einsatz < minEinsatz ) {
			message = "Der Einsatz unterschreitet den Mindestbetrag (" + minEinsatz + " Euro)!";
			result = false;
		}
		
		// Der Spieler kann auch einen Betrag setzen, der, wenn er ihn verliert, sein Konto unterhalb des
		// Mindesteinsatzes verringern läßt. In diesem Fall ist er dann raus aus dem Spiel
		
		return result;
	}

	@Override
	public void needMoreCard( IInputDisplay<Character> cdisp ) {
		String message = "Möchten Sie eine weitere Karte? (J=Ja / N=Nein)";
		char input;
		do {
			input = cdisp.inputDisplay( message );
			if (input == 'N') {
				noMoreCards = true;

			}
		} while (input != 'J' && input != 'N');
			

	}




}
