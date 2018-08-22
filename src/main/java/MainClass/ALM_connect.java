package MainClass;

import java.io.IOException;

public class ALM_connect {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		Process p = Runtime.getRuntime().exec(System.getProperty("user.dir") + "/ALM_Connect.vbs");// "sh " +projPath +
														// filePath
		p.waitFor();
	}

}
