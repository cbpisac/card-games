package cdg.launch;


public class GameConsole {
  
	public void start() {
		System.out.println( "Herzlich Willkommen! \n" );
		new GameProxy().startGame( GameTypes.SIEBZEHN_UND_VIER );
		System.out.println( "Auf Wiedersehen!" );
	}
}
