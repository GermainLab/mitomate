package rServe;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class InvokeRserve {
	
	public static void invoke() {
        try {

            // run the Unix ""R CMD RServe --vanilla"" command
            // using the Runtime exec method:
            
        	Runtime.getRuntime().exec("killall Rserve ");
        	TimeUnit.MILLISECONDS.sleep(2000);
        	
        	String[] cmd = {"/usr/local/bin/Rscript",  "/Users/marc 1/Dropbox/workspace_marc/PointPattern/script.r"};
        	Runtime.getRuntime().exec(cmd);

        	
			TimeUnit.MILLISECONDS.sleep(2000);
			
            
        }
        catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
