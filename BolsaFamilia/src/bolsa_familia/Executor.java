package bolsa_familia;

import java.sql.SQLException;
import java.text.ParseException;

public class Executor {

	public static void main(String[] args) throws ParseException, SQLException, InterruptedException {

		Tempo.executeETL();
		Cidades.executeETL();
		long start = System.nanoTime();
		Fatos.executeETL();
		long end = System.nanoTime();
		System.out.println("Tempo de execução");
	    System.out.println((end-start)/1000000000); 
	         
	}

}
