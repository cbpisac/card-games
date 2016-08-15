package cdg.siebzehn_und_vier;

import java.util.Random;

public class Utilities {
  
  private static Random rd = new Random();
  
  public static double normalDistribution(double variance) {
    return 1.0 - (1.0/(1+Math.abs(variance*rd.nextGaussian())));
  }
}
