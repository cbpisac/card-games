package cdg.siebzehn_und_vier;
/**
 * generisches Interface, um Ein- und Ausgaben aus der RealPlayerklasse zu transportieren. 
 * 
 * @author Carsten
 *
 * @param <T>  für jeden beliebige Typ
 */
public interface IInputDisplay<T> {
	
	/**
	 * die Klasse, die diese abstrakte Methode implementiert, sorgt optional für eine Ausgabe
	 * für den Benutzer und ermöglicht dem Benutzer eine Eingabe, die zurückgegeben wird.   
	 * 
	 * @param message Attribut, das zu der Ausgabe hinzugefügt werden soll.
	 * @return der eingebene Wert 
	 */
    public T inputDisplay(String message); 


    /**
	 * Diese Methode ist nur für eine Ausgabe gedacht, die in der gleichen Implementierung erfolgt.

	 * @param message Text, der ausgegeben werden soll
	 */

    public void DisplayMessage(String message);
}
