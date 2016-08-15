package cdg.siebzehn_und_vier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NormalDistribution {

    /**
     * @param args
     */
    @SuppressWarnings( "unused" )
	public static void main(String[] args) {
        List<Integer> intList = new ArrayList<>();
        Random rand = new Random();
       
        int k = 10000;
       
       
        double variance = 0.2;
       
        double val=0;
        for (int i=0; i<k;i++) {
           
           
            double r = 1.0 - (1.0/(1+Math.abs(variance*rand.nextGaussian())));
            val+=r;
//            System.out.println(r);

            int rInt = (int) (r*500);
            System.out.println(rInt);
//            intList.add( rInt );
            
        }
       
        val/=k;

        System.out.println("Average: "+val);
       
//        for (Integer i : intList) {
//          if (i > 30) {
//            System.out.println(i);
//          }
//        }

    }

}