package rServe;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

public class Connection {

	RConnection c;

	public Connection() {
		try {
			c = new RConnection();
			System.out.println("connection is :" + c.isConnected());
			c.eval("2+2");
			c.eval("library(plyr)");

			REXP rResponseObject = c.parseAndEval("try(eval( library(spatstat) ),silent=TRUE)");
			if (rResponseObject.inherits("try-error")) {
				System.out.println("R Serve Eval Exception : " + rResponseObject.asString());
			}
			REXP rResponseObject2 = c.parseAndEval("try(eval( library(qpcR)),silent=TRUE)");
			if (rResponseObject2.inherits("try-error")) {
				System.out.println("R Serve Eval Exception : " + rResponseObject2.asString());
			}
			

			REXP rResponseObject3 = c.parseAndEval("try(eval( library(spatstat) ),silent=TRUE)");
			if (rResponseObject3.inherits("try-error")) {
				System.out.println("R Serve Eval Exception : " + rResponseObject3.asString());
			}

			// c.eval("");
			// c.eval("");
		} catch (RserveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (REngineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (REXPMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean checkConnection() {
		return c.isConnected();
	}

	public REXP runRserve(String command) throws Exception {

		REXP t1 = null;

		t1 = c.parseAndEval("try(" + command + ",silent=TRUE)  ");
		if (t1 != null && t1.inherits("try-error")) {
			System.err.println("Error: " + t1.asString());
			throw (new Exception());
		}

		return t1;

	}

	public REXP runRserveTalk(String command) throws Exception {

		REXP t1 = null;

		t1 = c.parseAndEval("try(" + command + ",silent=FALSE)  ");

		if (t1 != null)
			System.out.println(t1.toDebugString());
		if (t1.inherits("try-error")) {
			System.err.println("Error: " + t1.asString());
			throw (new Exception());
		}

		return t1;

	}

	public void clear() throws Exception {
		c.parseAndEval("rm(list=ls())");

	}

	public void quit() {
		try {
			c.eval("rm(list = setdiff(ls(), lsf.str()))");
		} catch (RserveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.finalize();
		c.close();
	}

	public static void quit(RConnection c) {
		try {
			c.eval("rm(list = setdiff(ls(), lsf.str()))");
		} catch (RserveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.finalize();
		c.close();
	}

}
