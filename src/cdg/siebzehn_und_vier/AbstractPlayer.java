package cdg.siebzehn_und_vier;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese abstrakte Klasse beinhaltet die grundlegenden Attribute 
 * und Methoden für alle Spielerarten.
 * <p>
 * Sie soll möglichst frei von irgendwelchen Spielregeln sein.
 * 
 * @author Carsten
 */

public abstract class AbstractPlayer {

	private List<Integer> karten = new ArrayList<Integer>(); // Liste aller Karten
	private boolean wasAss = false; // für BlackJack/17+4
	private boolean humanPlayer = false;
	private boolean outOfGame = false;

	protected final int minEinsatz = 3;

	protected int einsatz; // (Geld-)Einsatz
	protected int konto = 500; // Startkonto
	protected int summe; // Summe aller Karten
	protected String name = "Default";
	protected boolean noMoreCards = false;


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName( String name ) {
		this.name = name;
	}

	
	public boolean isHumanPlayer() {
		return humanPlayer;
	}
	
	
	public void setIsHumanPlayer() {
		humanPlayer = true;
	}
	
	
	public boolean isOutOfGame() {
		return outOfGame;
	}
	
	
	public void setOutOfGame() {
		outOfGame = true;
	}
	
	
	/**
	 * Getter für einsatz
	 * 
	 * @return einsatz
	 */
	// public int getEinsatz() {
	// return einsatz;
	// }


	/**
	 * Getter für summe
	 * 
	 * @return summe
	 */
	public int getSumme() {
		return summe;
	}

	
	public boolean wantMoreCards() {
		return noMoreCards == false;
	}
	
	
	/**
	 * Erhöht das Konto um den Einsatz mal dem übergebenenen Faktor
	 * 
	 * @param einsatz Der Faktor, mit dem der Einsatz multipliziert werden soll.
	 */
	public void addToKonto( int faktor ) {
		konto += einsatz * faktor;
	}

	/**
	 * Mindert das Konto um den Einsatz mal dem übergebenenen Faktor
	 * 
	 * @param einsatz Der Faktor, mit dem der Einsatz multipliziert werden soll.
	 */
	public void subFromKonto( int faktor ) {
		konto -= einsatz * faktor;
	}
	
	
	/**
	 * Setzt einige Felder auf ihre Defaultwerte und leert die Kartenliste
	 * 
	 */
	void ResetPlayer() {
		summe = 0;
		noMoreCards = false;
		outOfGame = false;
		karten.clear();
	}

	/**
	 * Fügt den übergebenen Kartenzahlenwert der Kartenliste hinzu und speichert
	 * die Summe der Kartenwerte
	 * 
	 * @param karte
	 *            Kartenzahlenwert
	 */
	void addKarte( int karte, int value ) {
		karten.add( karte );
		if ( karten.size() == 1 && value == 11 ) { // erstes Ass
			wasAss = true;
		}
		summe += checkValue( value ); // nur für BlackJack/17+4
	}

	/**
	 * Gibt die Kartenliste des Spielers zurück
	 * 
	 * @return Liste der Spielerkarten
	 */
	List<Integer> getCards() {
		return karten;
	}

	/**
	 * Gibt die Karten des Spielers als Text in einem String zurück
	 * 
	 * @return String mit Kartentext
	 */
	public String listCards() {
		Cards cards = Cards.getCurrentInstance();
		StringBuilder sb = new StringBuilder();

		for ( int i = 0; i <= karten.size() - 1; i++ ) {
			sb.append( cards.kartenText( karten.get( i ) ) );
			if ( i < karten.size() - 1 ) {
				sb.append( ", " );
			}
		}
		return sb.toString();
	}

	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( this.getClass().getSimpleName() );
		sb.append( " = [name=" + this.name );
		sb.append( ", einsatz=" + this.einsatz );
		sb.append( ", konto=" + this.konto );
		sb.append( ", noMoreCards=" + this.noMoreCards );
		sb.append( ", outOfGame=" + this.outOfGame );
		sb.append( ",\n  karten= " + this.karten.toString() );
		sb.append( " -> " + ( this.karten.size() > 0 ? this.listCards() : "" ) );
		sb.append( "]" );
		return sb.toString();
	}

	/**
	 * Überprüft, ob zweite Karte auch ein Ass ist, und korrigiert das Ergebnis
	 * (diese Methode kommt in class SiebzehnUndVierGame, Zugriff über
	 * Interface)
	 * 
	 * @param value
	 * @return den Parameterwert, ggf. das korrigiertes Ergebnis, falls
	 *         Bedingung zutrifft
	 */
	private int checkValue( int value ) {
		if ( Cards.getNumOfCards() == Cards.CARDS32 ) {
			if ( value == 11 && karten.size() == 2 && wasAss ) {
				value = 10;
				wasAss = false;
			}
		}
		return value;
	}


	/**
	 * Diese Methode dient zum Setzen der Einsätze der Spieler
	 * <p>
	 * Die Paramter (außer idisp) werden für die Überprüfung des Einsatzes gebraucht.<br>
	 * Dabei gilt:
	 * <ol>
	 * <li> Der Einsatz darf nicht größer sein als der vorhandene Kontostand</li>
	 * <li> Der Einsatz darf weder dem der Bank entsprechen noch größer sein<br>
	 * 		als er (Va Banque wird nicht unterstützt)</li>
	 * <li> Der Einsatz darf in Summe mit denen der anderen Spieler den <br>
	 * 		Einsatz der Bank nicht überschreiten</li>
	 * <li> Der Einsatz darf den Mindesteinsatz nicht unterschreiten</li>
	 * </ol>
	 * Der Spieler kann auch einen Betrag setzen, der, wenn er ihn verliert, 
	 * sein Konto unterhalb des Mindesteinsatzes verringern läßt. In diesem Fall 
	 * ist er dann raus aus dem Spiel.
	 * <p>
	 * Die Bank hat eine eigene Methode, die sich von der Anzahl und dem Inhalt
	 * der Parameter von dieser unterscheidet. 
	 * 
	 * @param poolRecord Recordobjekt mit Informationen zur Überprüfung des Einsatzes
	 * @param idisp erwartet eine Implementation zur Ein- und Ausgabe (nur RealPlayer)
	 * @return Einsatz der Spieler (ermittelt oder eingegeben)
	 */
	
	 // Eine Implementation von BlackJack könnte diese Methode ebenso nutzen, der
	 // Parameter bankEinsatz würde dann aber den Höchstbetrag beinhalten.

	abstract public int placingPool( PoolRecord poolRecord , IInputDisplay<Integer> idisp );
	
	abstract public void needMoreCard( IInputDisplay<Character> cdisp );

}
