package data;
import java.util.Random;

public class RandomSingle {

		   private static RandomSingle instance = null;
		   private Random rand;
		   
		   protected RandomSingle() {
		      // Exists only to defeat instantiation.
		   }
		   public static RandomSingle getInstance() {
		      if(instance == null) {
		         instance = new RandomSingle();
		         instance.rand = new Random();
		      }
		      return instance;
		   }
		
		   public Random getRand() {
			   return rand;
		   }
}
