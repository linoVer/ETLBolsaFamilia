package bolsa_familia;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.function.LongToIntFunction;

public class Executor {

	public static void main(String[] args) throws ParseException, SQLException {

		long start = System.nanoTime();
		Fatos.executeETL();
		long end = System.nanoTime();
		System.out.println("Tempo de execução");
	    System.out.println((end-start)/1000000000); 
	         
	}

}
