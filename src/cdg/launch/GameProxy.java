package cdg.launch;

import cdg.siebzehn_und_vier.SiebzehnUndVierGame;

public class GameProxy {
	Gameable service;
  
	public void startGame(GameTypes gameType) {
    
		switch (gameType) {
		case SIEBZEHN_UND_VIER: 
			service = new SiebzehnUndVierGame();
			break;
		default:
			throw new UnsupportedOperationException( "Dieses Spiel wird z.Zt. nicht unterstüzt!" );
		}
    
		service.startGame();
	}
}
