package cdg.siebzehn_und_vier;

import java.util.Random;

public class VirtualPlayer extends AbstractPlayer {


// Die Möglichkeit, dass jeder die Bank übernehmen kann, wird noch nicht untertützt
// Die Funktionalität wird dann eher in die abstrakte Klasse verlagert.
//	private boolean bank;
//
//  VirtualPlayer() {
//  	this ( false );
//  }
//  
//  VirtualPlayer( boolean bank ) {
//  	this.bank = bank;
////  	if ( this.bank == true )
////  	  maxBankEinsatz = (int)( Math.random() * 450 + 50);
//  }

	public VirtualPlayer() {
		
	}
	
	public VirtualPlayer(String name) {
		this.name = name;
	}
	
	
  
	@Override
	public int placingPool( PoolRecord poolRecord, IInputDisplay<Integer> notUsed ) {
		Random rand = new Random();
		double variance = Utilities.normalDistribution( 1.476887 );
		int rest = poolRecord.getMaxEinsatz() - poolRecord.getSumAllPlayer() - 
				minEinsatz * poolRecord.getCountPlayerRest(); 

		while ( true ) {
			
			// Bei Restwerten unter 30 ist es besser, einen einfachen Zufallswert zu ermitteln
			// Wahrscheinlich ist ein kleinerer Wert als 30 möglich, was aber erst
			// ausgetestet werden muß.
			if ( rest > 30) {
				einsatz = (int) (Utilities.normalDistribution( variance ) * konto);
//				System.out.println("variance");
			}
			else {
				einsatz = rand.nextInt(rest) + 1;
//				System.out.println("random");
			}
			if ( checkPool( rest ) ) {
				break;
			}
		}
		return einsatz;
	}


  	/**
	 * Überprüft den Einsatz des Spielers
	 * 
	 * @return true = Einsatz ok, false = Einsatz nicht ok 
	 */

	private boolean checkPool( int rest ) {
		boolean result = true;
		
		// Einsatz zu niedrig (kleiner als minimaler Einsatz)?
		if ( einsatz < minEinsatz ) {
			result = false;
		}
		// Einsatz mit der Summe der anderen Spieler über Bankeinsatz? 
		else if ( einsatz > rest ) {
			result = false;
		}
		return result;
	}

	@Override
	public void needMoreCard( IInputDisplay<Character> cdisp ) {
		if (summe >= 18) {
			noMoreCards = true;
		}
		
	}



//	public boolean isBank( VirtualPlayer player ) {
//  	return this.bank == player.bank ? true : false;
//  }
}
