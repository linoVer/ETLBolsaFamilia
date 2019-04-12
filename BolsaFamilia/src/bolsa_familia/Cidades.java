package bolsa_familia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;


public class Cidades {
	
	private int cod_ibge;
	private String municipio;
	private String uf;
	
	private String regiao;
	
	private static LinkedList<Cidades> cidades = new LinkedList<>();
	private static LinkedList<Cidades> cidadesTransform = new LinkedList<>();


	private static String sql;
	@SuppressWarnings("unused")
	private static ResultSet res;
	
	
	public Cidades(int cod_ibge, String municipio, String uf) {
		this.cod_ibge = cod_ibge;
		this.municipio = municipio;
		this.uf = uf;
	}
	
	public Cidades(String regiao, int cod_ibge, String municipio, String uf) {
		this.cod_ibge = cod_ibge;
		this.municipio = municipio;
		this.uf = uf;
		this.regiao = regiao;
	}
	
	
	private static Queue<Cidades> extract() throws SQLException{
		Conexao con = new Conexao();

		con.getStmt().execute("ALTER SESSION SET CURRENT_SCHEMA = BOLSA_FAMILIA");      

		sql = "SELECT * FROM CIDADES_IBGE";

		ResultSet res = con.getStmt().executeQuery(sql);

		while (res.next()) {
			int cod_ibge = res.getInt("cod_ibge");      				
			String municipio = res.getString("cid_ibge");
			String uf = res.getString("uf"); 
			cidades.add(new Cidades(cod_ibge, municipio, uf));              
		}
		return cidades;		
	}
	
	private static Queue<Cidades> transform() throws SQLException{    	
		while(!cidades.isEmpty()) {
			int cod_ibge = cidades.peek().getCod_ibge();
			String municipio = cidades.peek().getMunicipio();
			String uf = cidades.peek().getUf();
			String regiao = getRegiao(uf);			
			cidades.remove();
			cidadesTransform.add(new Cidades(regiao, cod_ibge, municipio, uf));
		}
		return cidadesTransform;		
	}
	
	private static void load() throws SQLException {
		Conexao con = new Conexao();
		con.getStmt().execute("ALTER SESSION SET CURRENT_SCHEMA = DW_BOLSA_FAMILIA"); 
        
	       
        sql = "INSERT INTO DW_BOLSA_FAMILIA.DM_LOCAL(ID_LOCAL, MUNICIPIO, UF, REGIAO) " + 
        	  "VALUES(?, ?, ?, ?)";


        PreparedStatement ins = con.getCon().prepareStatement(sql);
        while(!cidadesTransform.isEmpty()){
        	ins.setInt(1, cidadesTransform.peek().getCod_ibge());
        	ins.setString(2, cidadesTransform.peek().getMunicipio() );
        	ins.setString(3, cidadesTransform.peek().getUf());
        	ins.setString(4, cidadesTransform.peek().getRegiao()); 
       
        	ins.executeUpdate();
        	cidadesTransform.remove();
        }
        ins.close();               
        con.getCon().close();
	}
	
	public static void executeETL() throws SQLException {		
		extract();
		transform();
		load();	   
	}
	
	private static String getRegiao(String regiao) {
		String ne[] = {"AL","BA", "CE", "MA", "PB", "PE","PI", "RN", "SE"};
		String n[] = {"AC", "AM", "AP", "PA", "TO", "RO", "RR"};
		String s[] = {"PR", "SC", "RS"};
		String sd[] = {"ES", "MG", "SP", "RJ"};
		String co[] = {"GO", "MS", "MT", "DF"};		

		for (int i = 0; i < 9; i++) {
			if(i < 3 && regiao.equals(co[i])) {
				regiao = "Centro-Oeste";
				break;
			}else if(i < 3 && regiao.equals(s[i])) {
				regiao = "Sul";
				break;
			}else if(i < 4 && regiao.equals(sd[i])) {
				regiao = "Sudeste";
				break;
			}else if(i < 7 && regiao.equals(n[i])) {
				regiao = "Norte";
				break;
			}else if(regiao.equals(ne[i])) {
				regiao = "Nordeste";
				break;
			}
		}	
		return regiao;
	}


	private int getCod_ibge() {
		return cod_ibge;
	}


	private String getMunicipio() {
		return municipio;
	}


	private String getUf() {
		return uf;
	}

	private String getRegiao() {
		return regiao;
	}
	
	

}
