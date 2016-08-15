package cdg.siebzehn_und_vier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Klasse Cards<p>
 * beinhaltet folgende Funktionen:<p>
 * 
 * - Karten ziehen<br>
 * - Karte oder Karten auf Stapel legen<br> 
 * - Kartentext ausgeben<br>
 * <p>
 * Diese Version benutzt keinen Talon (verdeckten Stapel), sondern
 * benutzt lediglich zwei Stapel (Karten aller Spieler und Ablagestapel).
 * <p>
 * Den Karten wird ein numerischer Kartenwert (von 0 bis Kartenanzahl -1)
 * zugeordnet (s. Tabelle unten).
 * <p> 
 * Die Eindeutigkeit der Karten wird durch Vergleich der Zufallszahl mit 
 * den schon ermittelten Zufallszahlen (in den zwei Stapeln) und entprechend 
 * wiederholtem Ermitteln einer Zufallszahl erreicht.
 * <p> 
 * Bedingung, dass alle Karten gespielt wurden, ist, dass die Summe beider
 * Stapel die Anzahl der Spielkarten erreicht hat.
 * <p> 
 * Beispiel für 32 Karten:<p>
 * <pre>
 *              (0) (1) (2) (3) (4) (5) (6) (7)
 *               7   8   9   10  B   D   K   A
 *           ----------------------------------------- 
 *  (0) Karo  |  0   1   2   3   4   5   6   7
 *  (1) Herz  |  8   9  10  11  12  13  14  15
 *  (2) Pik   | 16  17  18  19  20  21  22  23
 *  (3) Kreuz | 24  25  26  27  28  29  30  31
 * </pre>
 * 
 * @author Carsten
 */

/**
 * @author Carsten
 *
 */
public class Cards {
//	private boolean[][] stapel;
//	private int cardsCounter;

	public static final int CARDS32 = 32;
	public static final int CARDS52 = 52;
  
	private static int numOfCards;

	private List<Integer> cardPack = new ArrayList<>();  // Ablagestapel
	private List<Integer> cardList = new ArrayList<>();  // quasi Karten aller Spieler

	private Random rd = new Random();
  
	private String[] arColour = {"Karo", "Herz", "Pik", "Kreuz"};
	private String[] arValue32 = {"7", "8", "9", "10", "Bube", "Dame", "König", "Ass"};
	private String[] arValue52 = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Bube", "Dame", "König", "Ass"};
 
	private static Cards cards;
  
	/**
	 * Konstruktor für Cards <br>
	 * 
	 * @param NumOfCards Anzahl der Karten (32 oder 52) 
	 * @throws IllegalArgumentException wenn die Anzahl der Karten nicht stimmt.
	 * 
	 */
	private Cards(int numOfCards) throws IllegalArgumentException{
		Cards.numOfCards = numOfCards;
		if ( (numOfCards != CARDS32) && ( numOfCards != CARDS52) ) {
			throw new IllegalArgumentException( "Die Anzahl der Karten stimmt nicht!\nNur 32 oder 52 sind erlaubt!" );
		}
	}  

	
	/**
	 * Gibt eine neue Referenz auf Cards zurück<br>
	 * Diese Methode sollte nur dann aufgerufen werden, wenn tatsächlich ein neues
	 * Kartenspiel benötigt wird.
	 * 
	 * @param numOfCards Anzahl der Karten
	 * @return neue Referenz auf Cards
	 * @throws IllegalArgumentException
	 */
	public static Cards getNewInstance(int numOfCards) throws IllegalArgumentException {
		cards = new Cards(numOfCards);
		return cards;
	}
	
	
	/**
	 * Gibt eine bestehende Referenz auf Cards zurück<br>
	 * Diese Methode sollte immer dann benutzt werden, wenn ein Kartenspiel
	 * in Benutzung ist.
	 * @return bestehende Referenz auf Cards
	 */
	public static Cards getCurrentInstance() {
		return cards;
	}
	  
	
	/**
	 * Gibt die Kartenanzahl als statischen Wert zurück
	 * 
	 * @return the stNumOfCards
	 * 
	 */
	public static int getNumOfCards() throws NotValueSetException {
		if (numOfCards == 0) {
			throw new NotValueSetException("numOfCards wurde noch nicht gesetzt!");
		}
		return numOfCards;
	}


	/**
	 * Zieht die Karte und überprüft sie
	 * 
	 * @return gezogene Karte (int)
	 * 
	 */
	public int takeCard() {
		int karte;

		gotCard:
		while ( true ) {
			if ( (cardList.size() + cardPack.size()) >= numOfCards ) {
				System.out.println( "Die Karten werden neu gemischt!" );
				resetPack();
			}	

			karte = getRandomCard();
			// Hatten wir die Karte schon?
			if ( !cardList.contains( karte ) && !cardPack.contains( karte )) {
				cardList.add( karte );
				break gotCard;
			}	
		}  // while
//		System.out.println(karte + ", " + cardList + " / " + cardPack + " / " + cardPack.size() + " / " + NumOfCards);

		return karte;  
	}


	public int getLastCardOnPack() {
		return cardPack.get( cardPack.size() - 1 );
	}
	
	
	 /**
	  * Karte auf Stapel legen
	  * 
	  */
	public void discardCard(int karte) throws IllegalArgumentException {
		if (isNotInRange( karte ) ) {
			throw new IllegalArgumentException("Ungültiger Wert für die Karte: " + karte);
		}
		cardPack.add( karte );
		cardList.remove( cardList.indexOf( karte ));
	}

	
	
	/**
	 * Karten aller Spieler auf Stapel legen
	 * 
	 */
	public void discardCards() {
		// Liste mit ausgeteilten Karten auf den Kartenstapel legen
		for ( int karte : cardList )
			cardPack.add( karte );
			cardList.clear();
		}
	
	
	/**
	 * Leert den Stapel
	 * 
	 */
	private void resetPack() {
		// benutzen Kartenstapel leeren 
		cardPack.clear();
	}

  
	/**
	 * Die zu ziehende Zahl wird per Zufallsgenerator bestimmt
	 * 
	 * @return Kartenzahl (int)
	 * 
	 */
	private int getRandomCard() {
		return rd.nextInt(numOfCards);
	}


	/**
	 * Prüft, ob die Karte außerhalb des Bereichs ist
	 * 
	 * @param karte die Karte
	 * @return true, wenn die Karte außerhalb des Bereichs ist 
	 * 
	 */
	private boolean isNotInRange(int karte) {
		boolean result = false;
		if ( karte < 0 || karte >= CARDS32 || karte >= CARDS52) {
			result = true;
		}
		return result;
	}
  
	
	/**
	 * gibt den Kartenwert als Text zurück
	 * 
	 * @param karte ein Kartenwert
	 * @return Kartentext (String)
	 */
	public String kartenText( int karte ) throws IllegalArgumentException {
		if (isNotInRange( karte ) ) {
			throw new IllegalArgumentException("Ungültiger Wert für die Karte: " + karte);
		}
    
		StringBuilder kartenString = new StringBuilder("");
		int colour = calcColour( karte );
		int value = calcValue( karte );
 
		kartenString.append( arColour[ colour ]);
		kartenString.append( " " );
		if ( numOfCards == CARDS32 ) {
			kartenString.append( arValue32[ value ] );
		} 
		else {
			kartenString.append( arValue52[ value ] );
		}
 
		return kartenString.toString();
	}

    
	/**
	 * Berechnet den Zeilenwert (Farbe) aus dem übergebenen Kartenzahlenwert
	 * 
	 * @param karte Kartenzahlenwert 
	 * @return Zeilenwert (int) 
	 */
	public int calcColour( int karte ) {
		return (int)( karte / ( numOfCards / 4 ) );  // ( numOfCards / 4 ) => 8 oder 13
	}

	/**
	 * Berechnet den Spaltenwert (Wert) aus dem übergebenen Kartenzahlenwert
	 * 
	 * @param karte Kartenzahlenwert  
	 * @param row Zeilenwert
	 * @return Spaltenwert (int) 
	 */
	public int calcValue( int karte) {
		return karte % ( numOfCards / 4 );  // ( numOfCards / 4 ) => 8 oder 13
	}

}
