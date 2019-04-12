package bolsa_familia;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexao {

	private String server;
	private String port;
	private String database;
	private String user;
	private String passwd;
	private String url;
	private Connection con;
	private Statement stmt;
	

	public Conexao() throws SQLException {
		this.server = "localhost";
		this.port = "1521"; 
		this.database = "xe";
		this.user = "system";
		this.passwd = "Senha123";
		this.url = "jdbc:oracle:thin:@" + this.server + ":" + this.port + ":" + this.database;
		this.con = DriverManager.getConnection(url, user, passwd);
		this.stmt =  con.createStatement();
	}

	public Statement getStmt() {
		return stmt;
	}

	public Connection getCon() {
		return con;
	}

	



}
