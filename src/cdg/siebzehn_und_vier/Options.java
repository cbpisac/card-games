package cdg.siebzehn_und_vier;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;


public final class Options {
	private final static String NUMBER_CARDS_TEXT = "NumberCards";

	private static Options options;
	private Properties properties = new Properties();
	final String path = "Properties/Siebzehn_und_Vier.properties";

	private Options() {
		try {
			properties.load(new FileReader(path));
		}
		catch ( FileNotFoundException e ) {
			throw new BlackJackException(
					"Die Datei 'Siebzehn_und_Vier.properties' wurde nicht gefunden!" , e);
		}	
		catch (IOException e) {
			throw new BlackJackException(e);
		}

	}

	/**
	 * erstellt ein Exemplar/Instanz
	 * 
	 */
	public static synchronized Options getInstance() {
		if ( options == null )
			options = new Options();
		return options;
	}

	
	public void setNumOfCards( int numOfCards ) {
		String str = properties.getProperty( NUMBER_CARDS_TEXT ).trim();
		if ( Integer.parseInt( str ) != numOfCards ) {
			properties.setProperty( NUMBER_CARDS_TEXT, Integer.toString( numOfCards ));
			writeProperties();
		}
	}

	
	public int getNumOfCards() throws BlackJackException {
		String str = properties.getProperty( NUMBER_CARDS_TEXT ).trim();
		return Integer.parseInt( str );

	}


	private boolean writeProperties() {
		try {
			properties.store( new FileWriter(path), ""  );
		}
		catch ( IOException e ) {
			throw new BlackJackException(e);
		};
		return true;
	}
}
