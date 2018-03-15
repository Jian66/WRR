package wrr;

import com.lindo.Lingd17;

/**
 * 
 * @author JianWang,DLUT
 *
 */

public class UpdateOpt {
	static String[] initname = {"L_YNHYDROINIT_O_1.lng","L_YNHYDROINIT_O_2.lng","L_YNHYDROINIT_O_3.lng"
		,"L_YNHYDROINIT_O_4.lng","L_YNHYDROINIT_O_5.lng","L_YNHYDROINIT_O_6.lng","L_YNHYDROINIT_O_7.lng"
		,"L_YNHYDROINIT_O_8.lng","L_YNHYDROINIT_O_9.lng","L_YNHYDROINIT_O_10.lng","L_YNHYDROINIT_O_11.lng"
		,"L_YNHYDROINIT_O_12.lng"};
	
	static String[] hydroname = {"L_YNHYDRO_O_1.lng","L_YNHYDRO_O_2.lng","L_YNHYDRO_O_3.lng"
		,"L_YNHYDRO_O_4.lng","L_YNHYDRO_O_5.lng","L_YNHYDRO_O_6.lng","L_YNHYDRO_O_7.lng"
		,"L_YNHYDRO_O_8.lng","L_YNHYDRO_O_9.lng","L_YNHYDRO_O_10.lng","L_YNHYDRO_O_11.lng"
		,"L_YNHYDRO_O_12.lng"};
	
	static String[] thermalname = {"L_YNTHERMAL_O_1.lng","L_YNTHERMAL_O_2.lng","L_YNTHERMAL_O_3.lng"
		,"L_YNTHERMAL_O_4.lng","L_YNTHERMAL_O_5.lng","L_YNTHERMAL_O_6.lng","L_YNTHERMAL_O_7.lng"
		,"L_YNTHERMAL_O_8.lng","L_YNTHERMAL_O_9.lng","L_YNTHERMAL_O_10.lng","L_YNTHERMAL_O_11.lng"
		,"L_YNTHERMAL_O_12.lng"};
	
	static String[] htOpt = {"L_YNHYDROTHERMAL_O_1.lng","L_YNHYDROTHERMAL_O_2.lng","L_YNHYDROTHERMAL_O_3.lng"
		,"L_YNHYDROTHERMAL_O_4.lng","L_YNHYDROTHERMAL_O_5.lng","L_YNHYDROTHERMAL_O_6.lng","L_YNHYDROTHERMAL_O_7.lng"
		,"L_YNHYDROTHERMAL_O_8.lng","L_YNHYDROTHERMAL_O_9.lng","L_YNHYDROTHERMAL_O_10.lng","L_YNHYDROTHERMAL_O_11.lng"
		,"L_YNHYDROTHERMAL_O_12.lng"};
	
	static String[] htSim = {"L_YNHYDROTHERMAL_S_1.lng","L_YNHYDROTHERMAL_S_2.lng","L_YNHYDROTHERMAL_S_3.lng"
		,"L_YNHYDROTHERMAL_S_4.lng","L_YNHYDROTHERMAL_S_5.lng","L_YNHYDROTHERMAL_S_6.lng","L_YNHYDROTHERMAL_S_7.lng"
		,"L_YNHYDROTHERMAL_S_8.lng","L_YNHYDROTHERMAL_S_9.lng","L_YNHYDROTHERMAL_S_10.lng","L_YNHYDROTHERMAL_S_11.lng"
		,"L_YNHYDROTHERMAL_S_12.lng"};
	
	// Load the Lingo JNI interface
	static {
		System.loadLibrary("Lingj64_17");
	}

	// Create a new Lingo object, which we will use to interface with Lingo
	Lingd17 lng = new Lingd17();

	// Stores the Lingo JNI environment pointer
	Object pnLngEnv;

	int nLastIterationCount;

	private static int MySolverCallback(Object pnLng, int iLoc, Object jobj) {
		UpdateOpt s = (UpdateOpt) jobj;
		int nIterations[] = new int[1];
		s.lng.LSgetCallbackInfoIntLng(pnLng, Lingd17.LS_IINFO_ITERATIONS_LNG,
				nIterations);
		if (nIterations[0] != s.nLastIterationCount) {
			s.nLastIterationCount = nIterations[0];
			// System.out.println("In Java callback function...iterations = " +
			// s.nLastIterationCount);
		}

		return (0);
	}

	private static int MyErrorCallback(Object pnLng, int nErrorCode,
			String jsErrMessage, Object jobj) {
		System.out.println("Lingo error code: " + nErrorCode);
		System.out.println("Lingo error message:\n\n " + jsErrMessage);
		return (0);
	}
	
	private void solve1(int ii){

		int nErr;
		System.out.println("\nSolving...");

		// create the Lingo environment
		pnLngEnv = lng.LScreateEnvLng();
		if (pnLngEnv == null) {
			System.out.println("***Unable to create Lingo environment***");
			return;
		}

		// open a log file
		nErr = lng.LSopenLogFileLng(pnLngEnv, "lingo.log");
		if (nErr != lng.LSERR_NO_ERROR_LNG) {
			System.out.println("***LSopenLogFileLng() error***: " + nErr);
			return;
		}

		int[] nPointersNow = new int[1];
		double status[] = new double[1];
		status[0]=-1;
		nErr = lng.LSsetPointerLng(pnLngEnv, status, nPointersNow);
		if (nErr != lng.LSERR_NO_ERROR_LNG) {
			System.out.println("***LSsetPointerLng() error***: " + nErr);
			return;
		}
		

		// pass Lingo the name of the solver callback function...
//		nErr = lng.LSsetCallbackSolverLng(pnLngEnv, "MySolverCallback", this);

		// ...and the error callback function
//		nErr = lng.LSsetCallbackErrorLng(pnLngEnv, "MyErrorCallback", this);

		// construct the script

		// echo input to log file
		String sScript = "SET ECHOIN 1" + "\n";

		// load the model from disk
		sScript = sScript
				+ "TAKE C:\\LINGO64_17\\AllProjects\\YNHT\\update\\"+ initname[ii]
				+ "\n";
		
		// view the formulation
		sScript = sScript + "LOOK ALL" + "\n";

		// solve
		sScript = sScript + "GO" + "\n";

		// exit script processor
		sScript = sScript + "QUIT" + "\n";

		// run the script
		nLastIterationCount = -1;
		nErr = lng.LSexecuteScriptLng(pnLngEnv, sScript);
		if (nErr != lng.LSERR_NO_ERROR_LNG) {
			System.out.println("***LSexecuteScriptLng error***: " + nErr);
			return;
		}

		// clear the pointers to force update of local arrays
		// ***NOTE*** solution won't get passed to local arrays until
		// LSclearPointersLng is called!!!
		nErr = lng.LSclearPointersLng(pnLngEnv);
		if (nErr != lng.LSERR_NO_ERROR_LNG) {
			System.out.println("***LSclearPointerLng() error***: " + nErr);
			return;
		}
     
		if (status[0] == lng.LS_STATUS_GLOBAL_LNG){
			System.out.println("***Achieve GLOBAL***:" + status[0]);
		}else if(status[0] == lng.LS_STATUS_FEASIBLE){
			System.out.println("***Achieve FEASIBLE***:" + status[0]);
		}else if(status[0] == lng.LS_STATUS_LOCAL_LNG){
			System.out.println("***Achieve LOCAL OPT***:" + status[0]);
		}else{
			System.out.println("***NOT GOOD***:" + status[0]);
		}


		// close Lingo's log file
		nErr = lng.LScloseLogFileLng(pnLngEnv);
		if (nErr != lng.LSERR_NO_ERROR_LNG) {
			System.out.println("***LScloseLogFileLng() error***: " + nErr);
			return;
		}

		// delete the Lingo environment
		lng.LSdeleteEnvLng(pnLngEnv);

		System.out.println("month:"+ii+"\tinitHydro£º"+status[0]);
//		System.out.println("Set members passed back from Lingo:");

		System.out.println("free Java memory: " + Runtime.getRuntime().freeMemory());
	}

	private void solve2(int ii){

		int nErr;
		System.out.println("\nSolving...");

		// create the Lingo environment
		pnLngEnv = lng.LScreateEnvLng();
		if (pnLngEnv == null) {
			System.out.println("***Unable to create Lingo environment***");
			return;
		}

		// open a log file
		nErr = lng.LSopenLogFileLng(pnLngEnv, "lingo.log");
		if (nErr != lng.LSERR_NO_ERROR_LNG) {
			System.out.println("***LSopenLogFileLng() error***: " + nErr);
			return;
		}

		int[] nPointersNow = new int[1];
		double status[] = new double[1];
		status[0]=-1;
		nErr = lng.LSsetPointerLng(pnLngEnv, status, nPointersNow);
		if (nErr != lng.LSERR_NO_ERROR_LNG) {
			System.out.println("***LSsetPointerLng() error***: " + nErr);
			return;
		}
		

		// pass Lingo the name of the solver callback function...
//		nErr = lng.LSsetCallbackSolverLng(pnLngEnv, "MySolverCallback", this);

		// ...and the error callback function
//		nErr = lng.LSsetCallbackErrorLng(pnLngEnv, "MyErrorCallback", this);

		// construct the script

		// echo input to log file
		String sScript = "SET ECHOIN 1" + "\n";

		// load the model from disk
		sScript = sScript
				+ "TAKE C:\\LINGO64_17\\AllProjects\\YNHT\\update\\"+ hydroname[ii]
				+ "\n";
		
		// view the formulation
		sScript = sScript + "LOOK ALL" + "\n";

		// solve
		sScript = sScript + "GO" + "\n";

		// exit script processor
		sScript = sScript + "QUIT" + "\n";

		// run the script
		nLastIterationCount = -1;
		nErr = lng.LSexecuteScriptLng(pnLngEnv, sScript);
		if (nErr != lng.LSERR_NO_ERROR_LNG) {
			System.out.println("***LSexecuteScriptLng error***: " + nErr);
			return;
		}

		// clear the pointers to force update of local arrays
		// ***NOTE*** solution won't get passed to local arrays until
		// LSclearPointersLng is called!!!
		nErr = lng.LSclearPointersLng(pnLngEnv);
		if (nErr != lng.LSERR_NO_ERROR_LNG) {
			System.out.println("***LSclearPointerLng() error***: " + nErr);
			return;
		}
     
		if (status[0] == lng.LS_STATUS_GLOBAL_LNG){
			System.out.println("***Achieve GLOBAL***:" + status[0]);
		}else if(status[0] == lng.LS_STATUS_FEASIBLE){
			System.out.println("***Achieve FEASIBLE***:" + status[0]);
		}else if(status[0] == lng.LS_STATUS_LOCAL_LNG){
			System.out.println("***Achieve LOCAL OPT***:" + status[0]);
		}else{
			System.out.println("***NOT GOOD***:" + status[0]);
		}


		// close Lingo's log file
		nErr = lng.LScloseLogFileLng(pnLngEnv);
		if (nErr != lng.LSERR_NO_ERROR_LNG) {
			System.out.println("***LScloseLogFileLng() error***: " + nErr);
			return;
		}

		// delete the Lingo environment
		lng.LSdeleteEnvLng(pnLngEnv);

		System.out.println("month:"+ii+"\tHydroOpt£º"+status[0]);
//		System.out.println("Set members passed back from Lingo:");

		System.out.println("free Java memory: " + Runtime.getRuntime().freeMemory());
	}
	
	private void solve3(int ii){

		int nErr;
		System.out.println("\nSolving...");

		// create the Lingo environment
		pnLngEnv = lng.LScreateEnvLng();
		if (pnLngEnv == null) {
			System.out.println("***Unable to create Lingo environment***");
			return;
		}

		// open a log file
		nErr = lng.LSopenLogFileLng(pnLngEnv, "lingo.log");
		if (nErr != lng.LSERR_NO_ERROR_LNG) {
			System.out.println("***LSopenLogFileLng() error***: " + nErr);
			return;
		}

		int[] nPointersNow = new int[1];
		double status[] = new double[1];
		status[0]=-1;
		nErr = lng.LSsetPointerLng(pnLngEnv, status, nPointersNow);
		if (nErr != lng.LSERR_NO_ERROR_LNG) {
			System.out.println("***LSsetPointerLng() error***: " + nErr);
			return;
		}
		

		// pass Lingo the name of the solver callback function...
//		nErr = lng.LSsetCallbackSolverLng(pnLngEnv, "MySolverCallback", this);

		// ...and the error callback function
//		nErr = lng.LSsetCallbackErrorLng(pnLngEnv, "MyErrorCallback", this);

		// construct the script

		// echo input to log file
		String sScript = "SET ECHOIN 1" + "\n";

		// load the model from disk
		sScript = sScript
				+ "TAKE C:\\LINGO64_17\\AllProjects\\YNHT\\update\\"+thermalname[ii]
				+ "\n";
		
		// view the formulation
		sScript = sScript + "LOOK ALL" + "\n";

		// solve
		sScript = sScript + "GO" + "\n";

		// exit script processor
		sScript = sScript + "QUIT" + "\n";

		// run the script
		nLastIterationCount = -1;
		nErr = lng.LSexecuteScriptLng(pnLngEnv, sScript);
		if (nErr != lng.LSERR_NO_ERROR_LNG) {
			System.out.println("***LSexecuteScriptLng error***: " + nErr);
			return;
		}

		// clear the pointers to force update of local arrays
		// ***NOTE*** solution won't get passed to local arrays until
		// LSclearPointersLng is called!!!
		nErr = lng.LSclearPointersLng(pnLngEnv);
		if (nErr != lng.LSERR_NO_ERROR_LNG) {
			System.out.println("***LSclearPointerLng() error***: " + nErr);
			return;
		}
     
		if (status[0] == lng.LS_STATUS_GLOBAL_LNG){
			System.out.println("***Achieve GLOBAL***:" + status[0]);
		}else if(status[0] == lng.LS_STATUS_FEASIBLE){
			System.out.println("***Achieve FEASIBLE***:" + status[0]);
		}else if(status[0] == lng.LS_STATUS_LOCAL_LNG){
			System.out.println("***Achieve LOCAL OPT***:" + status[0]);
		}else{
			System.out.println("***NOT GOOD***:" + status[0]);
		}


		// close Lingo's log file
		nErr = lng.LScloseLogFileLng(pnLngEnv);
		if (nErr != lng.LSERR_NO_ERROR_LNG) {
			System.out.println("***LScloseLogFileLng() error***: " + nErr);
			return;
		}

		// delete the Lingo environment
		lng.LSdeleteEnvLng(pnLngEnv);

		System.out.println("month:"+ii+"\tThermalOpt£º"+status[0]);
//		System.out.println("Set members passed back from Lingo:");

		System.out.println("free Java memory: " + Runtime.getRuntime().freeMemory());
	}
	
	private void solve4(int ii){

		int nErr;
		System.out.println("\nSolving...");

		// create the Lingo environment
		pnLngEnv = lng.LScreateEnvLng();
		if (pnLngEnv == null) {
			System.out.println("***Unable to create Lingo environment***");
			return;
		}

		// open a log file
		nErr = lng.LSopenLogFileLng(pnLngEnv, "lingo.log");
		if (nErr != lng.LSERR_NO_ERROR_LNG) {
			System.out.println("***LSopenLogFileLng() error***: " + nErr);
			return;
		}

		int[] nPointersNow = new int[1];
		double status[] = new double[1];
		status[0]=-1;
		nErr = lng.LSsetPointerLng(pnLngEnv, status, nPointersNow);
		if (nErr != lng.LSERR_NO_ERROR_LNG) {
			System.out.println("***LSsetPointerLng() error***: " + nErr);
			return;
		}
		

		// pass Lingo the name of the solver callback function...
//		nErr = lng.LSsetCallbackSolverLng(pnLngEnv, "MySolverCallback", this);

		// ...and the error callback function
//		nErr = lng.LSsetCallbackErrorLng(pnLngEnv, "MyErrorCallback", this);

		// construct the script

		// echo input to log file
		String sScript = "SET ECHOIN 1" + "\n";

		// load the model from disk
		sScript = sScript
				+ "TAKE C:\\LINGO64_17\\AllProjects\\YNHT\\update\\"+htOpt[ii]
				+ "\n";
		
		// view the formulation
		sScript = sScript + "LOOK ALL" + "\n";

		// solve
		sScript = sScript + "GO" + "\n";

		// exit script processor
		sScript = sScript + "QUIT" + "\n";

		// run the script
		nLastIterationCount = -1;
		nErr = lng.LSexecuteScriptLng(pnLngEnv, sScript);
		if (nErr != lng.LSERR_NO_ERROR_LNG) {
			System.out.println("***LSexecuteScriptLng error***: " + nErr);
			return;
		}

		// clear the pointers to force update of local arrays
		// ***NOTE*** solution won't get passed to local arrays until
		// LSclearPointersLng is called!!!
		nErr = lng.LSclearPointersLng(pnLngEnv);
		if (nErr != lng.LSERR_NO_ERROR_LNG) {
			System.out.println("***LSclearPointerLng() error***: " + nErr);
			return;
		}
     
		if (status[0] == lng.LS_STATUS_GLOBAL_LNG){
			System.out.println("***Achieve GLOBAL***:" + status[0]);
		}else if(status[0] == lng.LS_STATUS_FEASIBLE){
			System.out.println("***Achieve FEASIBLE***:" + status[0]);
		}else if(status[0] == lng.LS_STATUS_LOCAL_LNG){
			System.out.println("***Achieve LOCAL OPT***:" + status[0]);
		}else{
			System.out.println("***NOT GOOD***:" + status[0]);
		}


		// close Lingo's log file
		nErr = lng.LScloseLogFileLng(pnLngEnv);
		if (nErr != lng.LSERR_NO_ERROR_LNG) {
			System.out.println("***LScloseLogFileLng() error***: " + nErr);
			return;
		}

		// delete the Lingo environment
		lng.LSdeleteEnvLng(pnLngEnv);

		System.out.println("month:"+ii+"\tHydrothermalOpt£º"+status[0]);
//		System.out.println("Set members passed back from Lingo:");

		System.out.println("free Java memory: " + Runtime.getRuntime().freeMemory());
	}
	
	private void solve5(int ii){

		int nErr;
		System.out.println("\nSolving...");

		// create the Lingo environment
		pnLngEnv = lng.LScreateEnvLng();
		if (pnLngEnv == null) {
			System.out.println("***Unable to create Lingo environment***");
			return;
		}

		// open a log file
		nErr = lng.LSopenLogFileLng(pnLngEnv, "lingo.log");
		if (nErr != lng.LSERR_NO_ERROR_LNG) {
			System.out.println("***LSopenLogFileLng() error***: " + nErr);
			return;
		}

		int[] nPointersNow = new int[1];
		double status[] = new double[1];
		status[0]=-1;
		nErr = lng.LSsetPointerLng(pnLngEnv, status, nPointersNow);
		if (nErr != lng.LSERR_NO_ERROR_LNG) {
			System.out.println("***LSsetPointerLng() error***: " + nErr);
			return;
		}
		

		// pass Lingo the name of the solver callback function...
//		nErr = lng.LSsetCallbackSolverLng(pnLngEnv, "MySolverCallback", this);

		// ...and the error callback function
//		nErr = lng.LSsetCallbackErrorLng(pnLngEnv, "MyErrorCallback", this);

		// construct the script

		// echo input to log file
		String sScript = "SET ECHOIN 1" + "\n";

		// load the model from disk
		sScript = sScript
				+ "TAKE C:\\LINGO64_17\\AllProjects\\YNHT\\update\\"+htSim[ii]
				+ "\n";
		
		// view the formulation
		sScript = sScript + "LOOK ALL" + "\n";

		// solve
		sScript = sScript + "GO" + "\n";

		// exit script processor
		sScript = sScript + "QUIT" + "\n";

		// run the script
		nLastIterationCount = -1;
		nErr = lng.LSexecuteScriptLng(pnLngEnv, sScript);
		if (nErr != lng.LSERR_NO_ERROR_LNG) {
			System.out.println("***LSexecuteScriptLng error***: " + nErr);
			return;
		}

		// clear the pointers to force update of local arrays
		// ***NOTE*** solution won't get passed to local arrays until
		// LSclearPointersLng is called!!!
		nErr = lng.LSclearPointersLng(pnLngEnv);
		if (nErr != lng.LSERR_NO_ERROR_LNG) {
			System.out.println("***LSclearPointerLng() error***: " + nErr);
			return;
		}
     
		if (status[0] == lng.LS_STATUS_GLOBAL_LNG){
			System.out.println("***Achieve GLOBAL***:" + status[0]);
		}else if(status[0] == lng.LS_STATUS_FEASIBLE){
			System.out.println("***Achieve FEASIBLE***:" + status[0]);
		}else if(status[0] == lng.LS_STATUS_LOCAL_LNG){
			System.out.println("***Achieve LOCAL OPT***:" + status[0]);
		}else{
			System.out.println("***NOT GOOD***:" + status[0]);
		}


		// close Lingo's log file
		nErr = lng.LScloseLogFileLng(pnLngEnv);
		if (nErr != lng.LSERR_NO_ERROR_LNG) {
			System.out.println("***LScloseLogFileLng() error***: " + nErr);
			return;
		}

		// delete the Lingo environment
		lng.LSdeleteEnvLng(pnLngEnv);

		System.out.println("month:"+ii+"\tSimulation£º"+status[0]);
//		System.out.println("Set members passed back from Lingo:");

		System.out.println("free Java memory: " + Runtime.getRuntime().freeMemory());
	}
	
	public static void main(String[] args) {
		UpdateOpt up = new UpdateOpt();
		
		long aa = System.currentTimeMillis();

		for (int i = 0; i < 12; i++) {
			System.out.println("-------"+(i+1)+"----ÔÂ----");
			long a = System.currentTimeMillis();
			
			up.solve1(i);
			long b = System.currentTimeMillis();
			System.out.println((b-a)/1000+"s");

			up.solve2(i);
			long c = System.currentTimeMillis();
			System.out.println((c-b)/1000+"s");

			up.solve3(i);
			long d = System.currentTimeMillis();
			System.out.println((d-c)/1000+"s");

			up.solve4(i);
			long e = System.currentTimeMillis();
			System.out.println((e-d)/1000+"s");

			up.solve5(i);
			long f = System.currentTimeMillis();
			System.out.println((f-e)/1000+"s");
		}
		
		long bb = System.currentTimeMillis();
		System.out.println("total:"+(bb-aa)/1000+"s");

	}
}
