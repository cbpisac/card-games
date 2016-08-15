
package cdg.siebzehn_und_vier;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import cdg.launch.Gameable;

/**
 * Dies ist ein kleines Spielprogramm, das das Kartenspiel 17 und 4 bzw. Blackjack nachbildet.
 * Es wächst und ändert sich kontinuierlich und hebt keinen Anspruch auf otpimale Nutzung
 * der Java-Sprache. Auch können einzelne Abschnitte oder ganze Methoden zwar lauffähig,
 * aber noch nicht optimiert sein, da manche Funktionalität noch fehlt. 
 * <p>
 * 
 * Derzeitige Festlegungen:<br>
 * Va Banque wird es nicht geben, vielleicht mal bei nur einem Spieler.<br>
 * weitere Karten werden nicht gekauft (Regeln im Internet sind bei Siebzehn und Vier zu schwammig)<br>
 * 
 * @author Carsten Brandes
 * @version Fork aus V0.2, August 2016
 *
 */

public class SiebzehnUndVierGame implements Gameable {
  
	private List<AbstractPlayer> playerList = new ArrayList<>();
	private BankPlayer bank;

	private Cards cards;

	private int[] arValuesCard32 = {7, 8, 9, 10, 2, 3, 4, 11};
	private int[] arValuesCard52 = {2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11}; 

	private PoolRecord poolRecord;  // quasi Record für Parameter beim Bieten
	private Scanner scanner = new Scanner( System.in );  // Für Eingaben
	
  
	public SiebzehnUndVierGame() {
		HumanPlayer humanPlayer = new HumanPlayer();
		humanPlayer.setIsHumanPlayer();
		playerList.add( humanPlayer );
		
		for (int i = 0; i < 4; i++) {
			playerList.add( new VirtualPlayer("VPlayer_" + (i+1)) );
		}

		bank = new BankPlayer();

		try {
			cards = Cards.getNewInstance( Options.getInstance().getNumOfCards() );
		}
		catch ( IllegalArgumentException | BlackJackException e ) {
			//TODO: ExceptionHandling überarbeiten, wenn Menüführung feststeht
			System.out.println( e.getMessage() );
			System.out.println( "Das Programm wird beendet!" );
			throw e;
		}

	
	}
  
  

	/* (non-Javadoc)
	 * @see blackJackApp.Gameable#startGame()
	 */
	@Override
	public void startGame() {
		char playAgain;

		do {
			// Einsätze festlegen
			if ( !placePool() ) {
				break;
			}
			else {
				// Spiel starten
				if ( !runGame() ) {
					break;
				}
			}

			System.out.println( "Möchten Sie noch einmal spielen (J/N)?" );
			playAgain = Character.toUpperCase( scanner.next().charAt( 0 ) );
		}
		while ( playAgain == 'J' );
	}

	
	class PoolHumanInput implements IInputDisplay<Integer> {

		@Override
		public Integer inputDisplay( String message ) {
		    int einsatz = -1;
	    	System.out.println( message );
	    	System.out.println( "Wieviel Euro setzen Sie? (0 = Ende)" );

		    while( einsatz < 0) {
		    	try {
		    		einsatz = scanner.nextInt();
		    	}
		      
		      catch ( InputMismatchException e ) {
		    	  System.out.println( "Fehler: Bitte geben Sie eine Zahl ein!" );
		      }
		    }
		    return einsatz;
		}

		@Override
		public void DisplayMessage( String message ) {
			System.out.println( message );
		}

	
	}
		
	/**
	 * Einsätze setzen
	 * 
	 * @return Einsatz setzen erfolgreich (true), Abbruch (false)
	 */
	private boolean placePool() {
		boolean result = true;
		int einsatz;
	    poolRecord = new PoolRecord(playerList.size());
		
		// Spieler zurücksetzen
		bank.ResetPlayer();
		for ( AbstractPlayer player : playerList ) {
			player.ResetPlayer();
		}

		// Bankeinsatz festlegen
		poolRecord.addMaxEinsatz( bank.placingPool( playerList.size() ) );
		System.out.printf( "Die Bank hat %d Euro gesetzt.%n", poolRecord.getMaxEinsatz());
      
		// Spielereinsätze setzen
		for ( AbstractPlayer player : playerList ) {
			
			if ( player.isHumanPlayer()) {
				// Human Player
				System.out.printf("%s, die anderen Spieler haben schon insgesamt %d Euro gesetzt.%n"
						, player.getName(), poolRecord.getSumAllPlayer() ); 

				einsatz = player.placingPool( poolRecord, new PoolHumanInput() );
				if ( einsatz != 0 )	{
					updatePoolRecord( einsatz );
				}
				else {
					result = false;
				}
			}
			else {
				// Virtual Player
				einsatz = player.placingPool( poolRecord, null );
				updatePoolRecord( einsatz );
				System.out.printf("Der Spieler %s hat %d Euro gesetzt.%n", player.getName(), einsatz);
					
			}
		}

//		for (AbstractPlayer ap : playerList) {
//			System.out.println(ap);
//		}

		return result;
	}

	private void updatePoolRecord( int einsatz ) {
		poolRecord.addSumAllPlayer( einsatz );
		poolRecord.decCountPlayerRest();
	}


	
	class RunHumanInput implements IInputDisplay<Character> {

		@Override
		public Character inputDisplay( String message ) {
			System.out.println( message );
			return Character.toUpperCase( scanner.next().charAt( 0 ) );
		}

		@Override
		public void DisplayMessage( String message ) {
			System.out.println( message );
		}
		
	}
	
	private boolean runGame() {
		int loopsToTake;
		boolean runOver = true;
		boolean moreCardsNeeded = true;

		// Karten ziehen
		loopsToTake = 2;
		while ( moreCardsNeeded ) {
			// Spieler ziehen Karten (beim ersten Mal 2)
			moreCardsNeeded = takeCardPlayers( loopsToTake );

			// Bank bekommt 1 Karte
			if ( loopsToTake == 2 ) { 
				takeCardBank();
				loopsToTake = 1;
			}
			
			// Spieler: Weitere Karte ziehen?
   			for (AbstractPlayer player : playerList) {
   				if  (player.wantMoreCards() && ! player.isOutOfGame() ) {
   					player.needMoreCard( player.isHumanPlayer() ? new RunHumanInput() : null);
   				}
   			}
		}
		
		// Bank zieht restliche Karten
		moreCardsNeeded = true;
		while ( moreCardsNeeded  ) {
    		bank.needMoreCard( null );
			moreCardsNeeded = bank.wantMoreCards() && ! bank.isOutOfGame();
			if ( moreCardsNeeded ) {
				takeCardBank();
			}
		}
		
		// Auflistung
		// mit Karten und Summen...
		
		for (AbstractPlayer ap : playerList) {
			System.out.println(ap);
		}
		System.out.println(bank);
		
		cards.discardCards();
		
//    --> in eigene Methode...
//		  und bitte komplett überarbeiten! 
//    // Vergleichen
//    if ( ( player.getSumme() < 22 ) && ( ( player.getSumme() > bank.getSumme() ) || ( bank.getSumme() >= 22 ) ) ) {
//      player.setKonto( player.getKonto() + player.getEinsatz() );
//      bank.setKonto( bank.getKonto() - player.getEinsatz() );
//
//    } else if ( ( bank.getSumme() < 22 ) & ( ( bank.getSumme() >= player.getSumme() ) | ( player.getSumme() >= 22 ) ) ) {
//      bank.setKonto( bank.getKonto() + player.getEinsatz());
//      player.setKonto( player.getKonto() - player.getEinsatz() );
//
//    } else if ( ( bank.getSumme() >= 22 ) & ( player.getSumme() >= 22 ) ) {
//      player.setKonto( player.getKonto() - player.getEinsatz() );
//      bank.setKonto( bank.getKonto() + player.getEinsatz());
//    }
//
//    System.out.println( "Sie haben noch " + player.getKonto() + " Euro." );
//    System.out.println( "Die Bank hat noch " + bank.getKonto() + " Euro." );
//    if ( player.getKonto() <= player.minEinsatz ) {
//      System.out.println( "Sie können nicht mehr setzen! Das Spiel zu Ende!" );
//      runOver = false;
//    }
//    if ( bank.getKonto() <= bank.minEinsatz ) {
//      System.out.println( "Die Bank kann nicht mehr setzen! Das Spiel zu Ende!" );
//      runOver = false;
//    }
    return runOver;
  }

	
	private boolean takeCardPlayers( int count ) {
		int karte;
		int ctrNoMoreCards = 0;
		
		for ( AbstractPlayer player : playerList ) {
			if (player.wantMoreCards() && ! player.isOutOfGame()) {
    			for ( int i = 0; i < count; i++ ) {
    				// Karten ziehen
    				karte = cards.takeCard();
    				player.addKarte( karte, getValue( karte ) );
    
    				// Textausgabe für menschliche Spieler
    				if ( player.isHumanPlayer() ) {
    					System.out.printf( "%s, Sie haben die Karte %s gezogen.%n",
    							player.getName(), cards.kartenText( karte ) );
    				}
    				
    				// Wenn summe der Karten > 21, dann raus
    				if ( player.getSumme() > 21 ) {
    					System.out.printf("%s%s aus dem Spiel mit %d Punkten!%n",
    							player.getName(),
    							player.isHumanPlayer() ? ", Sie sind" : " ist",
    							player.getSumme());
    					player.setOutOfGame();
    					ctrNoMoreCards++;
    				}
    			}
			}
			else {
				ctrNoMoreCards++;
			}
		}
		return (ctrNoMoreCards != playerList.size());
	}

	
	private void takeCardBank() {
		int karte;
		
		karte = cards.takeCard();
		bank.addKarte( karte, getValue( karte ) );

		// Wenn summe der Karten > 21, dann raus
		if ( bank.getSumme() > 21 ) {
			System.out.printf("Die %s ist aus dem Spiel mit %d Punkten!%n",
					bank.getName(),
					bank.getSumme());
			bank.setOutOfGame();
		}
	}

	
  /**
   * Ermittelt den Kartenwert anhand der übergebenen Kartennummer
   * 
   * @param karte Kartennummer
   * @return Wert der Karten (int) 
   * 
   */
  private int getValue( int karte ) {
    int value = cards.calcValue( karte );
    return Cards.getNumOfCards() == Cards.CARDS32 ? arValuesCard32[ value ] : arValuesCard52[ value ];
  }
  
}
