package bolsa_familia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


public class Fatos {
	
	private int id_local;
	private int id_tempo;
	private int nu_beneficiados;
	private double valor_total;
	
	private int codigoIbge;
	private double valor;
	private int quantidadeBeneficiados;
	private int id;
	private String nome;
	
	private static LinkedList<Fatos> fatos = new LinkedList<>();
	private static LinkedList<Fatos> fatosTransform = new LinkedList<>();
	private static LinkedList<Dados> aux = new LinkedList<>();
	private static LinkedList<Integer> codIBGE = new LinkedList<>();
	
	private static int count = 0;
	
	private static String sql;
	@SuppressWarnings("unused")
	private static ResultSet res;
	
	
	public Fatos(int id_tempo, int codigoIbge, double valor, int quantidadeBeneficiados, int id, String nome) {
		this.id_tempo = id_tempo;
		this.codigoIbge = codigoIbge;
		this.valor = valor;
		this.quantidadeBeneficiados = quantidadeBeneficiados;
		this.id = id;
		this.nome = nome;
	}

	public Fatos(int id_local, int id_tempo, int nu_beneficiados, double valor_total) {
		this.id_local = id_local;
		this.id_tempo = id_tempo;
		this.nu_beneficiados = nu_beneficiados;
		this.valor_total = valor_total;
	}

	private static LinkedList<Fatos> extract() throws SQLException, ParseException, InterruptedException {

		Conexao con = new Conexao();
		
		//caso haja proxy
//		System.setProperty("http.proxyHost","ip");
//      System.setProperty("http.proxyPort", "port");	
		
		Client client = ClientBuilder.newClient();

		con.getStmt().execute("ALTER SESSION SET CURRENT_SCHEMA = BOLSA_FAMILIA");   
		
		int numberMes = 1;
		int numberAno = 2016;
		int num = 0;
		DecimalFormat decimalFormatMes = new DecimalFormat("00");
		DecimalFormat decimalFormatAno = new DecimalFormat("0000");
		DecimalFormat decimalFormatcod = new DecimalFormat("0000000");
		String nuMes = decimalFormatMes.format(numberMes);
		String nuAno = decimalFormatAno.format(numberAno);
		String ibge = decimalFormatcod.format(num);
		String mesAno = nuAno+nuMes;
		int teste = 0;
		
		sql = "SELECT * FROM CIDADES_IBGE";

		res = con.getStmt().executeQuery(sql);
		
		while (res.next()) { 
			int codigoIbge = res.getInt("COD_IBGE");
			codIBGE.add(codigoIbge);
		}	
		res.close();
		
		while(!codIBGE.isEmpty()) {
			System.out.println(codIBGE.size());
			ibge = codIBGE.getFirst().toString();
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 12; j++) {
					count++;
					//pausa para evitar que o site derrube a requisão devido ao quantidade de requisições ininterruptas
					if(count == 5000) {
						count = 0;
						Thread.sleep(300000);
					}
					Response resp = client.target("http://www.transparencia.gov.br/api-de-dados/bolsa-familia-por-municipio?mesAno="+mesAno+"&codigoIbge="+ibge+"&pagina=1").request(MediaType.APPLICATION_JSON).get();
					String retorno = resp.readEntity(String.class);
					
					GsonBuilder gsonBuilder = new GsonBuilder();		
					
					Gson gson = new Gson();
					List<Dados> dados = gson.fromJson(retorno, new TypeToken<List<Dados>>(){}.getType());
					
					for (Dados dados2 : dados) {					
						aux.add(dados2);		
					}
					
					numberMes++;
					nuMes = decimalFormatMes.format(numberMes);	
					mesAno = nuAno+nuMes;
				}
				numberMes = 1;
				nuMes = decimalFormatMes.format(numberMes);	
				numberAno++;
				nuAno = decimalFormatAno.format(numberAno);
				mesAno = nuAno+nuMes;
			}
			numberAno = 2016;
			nuAno = decimalFormatAno.format(numberAno);
			mesAno = nuAno+nuMes;
			
			sql = "INSERT INTO DADOS_AUX(ID, DATA_REFERENCIA, COD_IBGE, NOME_IBGE, VALOR, QUANTIDADE_BENEFICIADOS) " + 
					"VALUES(?, ?, ?, ?, ?, ?)";
			
			PreparedStatement ins = con.getCon().prepareStatement(sql);
		
			
			while(!aux.isEmpty()) {
				
				String startDate= aux.peek().getDataReferencia();
				SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
				java.util.Date date = sdf1.parse(startDate);
				java.sql.Date qlStartDate = new java.sql.Date(date.getTime());
				
				ins.setInt(1, aux.peek().getId());			
				ins.setDate(2, qlStartDate); 
				ins.setInt(3, aux.peek().getMunicipio().getCodigoIBGE());
				ins.setString(4, aux.peek().getMunicipio().getNomeIBGE());
				ins.setDouble(5, aux.peek().getValor()); 
				ins.setInt(6, aux.peek().getQuantidadeBeneficiados()); 


				ins.executeUpdate();
				aux.remove();
			}
			ins.close();
			codIBGE.remove();
		}
		
		sql = "SELECT ID, NOME_IBGE, VALOR, DATA_REFERENCIA, QUANTIDADE_BENEFICIADOS, COD_IBGE, t.ID_TEMPO FROM DADOS_AUX d \r\n" + 
				"JOIN DW_BOLSA_FAMILIA.DM_TEMPO t ON t.NU_ANO = TO_CHAR(d.DATA_REFERENCIA, 'YYYY') AND t.NU_MES = TO_CHAR(d.DATA_REFERENCIA, 'MM')";

		res = con.getStmt().executeQuery(sql);
		
		while (res.next()) { 			
			String nome = res.getString("NOME_IBGE");
			double valor = res.getDouble("VALOR");			
			int quantidadeBeneficiados = res.getInt("QUANTIDADE_BENEFICIADOS");
			int codigoIbge = res.getInt("COD_IBGE");
			int id_tempo = res.getInt("ID_TEMPO");

			fatos.add(new Fatos(id_tempo, codigoIbge, valor, quantidadeBeneficiados, quantidadeBeneficiados, nome));              
		}
		res.close();
		
		return fatos;		
	}
	
	private static LinkedList<Fatos> transform(){    	
		while(!fatos.isEmpty()) {

			int id_local = fatos.peek().getCodigoIbge();
			int id_tempo = fatos.peek().getId_tempo();
			int nu_beneficiados = fatos.peek().getQuantidadeBeneficiados();
			double valor_total = fatos.peek().getValor();


			fatos.remove();
			fatosTransform.add(new Fatos(id_local, id_tempo, nu_beneficiados, valor_total));
		}	
		return fatosTransform;		
	}
	
	private static void load() throws SQLException {

		Conexao con = new Conexao();
		con.getStmt().execute("ALTER SESSION SET CURRENT_SCHEMA = DW_BOLSA_FAMILIA");         

		sql = "INSERT INTO FT_BOLSA_FAMILIA(ID_LOCAL, ID_TEMPO, NU_BENEFICIADOS, VALOR_TOTAL) " + 
				"VALUES(?, ?, ?, ?)";


		PreparedStatement ins = con.getCon().prepareStatement(sql);
		while(!fatosTransform.isEmpty()){
			ins.setInt(1, fatosTransform.peek().getId_local());
			ins.setInt(2, fatosTransform.peek().getId_tempo()); 
			ins.setInt(3, fatosTransform.peek().getNu_beneficiados() );
			ins.setDouble(4, fatosTransform.peek().getValor_total());
			 

			ins.executeUpdate();
			fatosTransform.remove();
		}
		ins.close();               
		con.getCon().close();
	}
	
	public static void executeETL() throws SQLException, ParseException, InterruptedException {		
		extract();
		transform();
		load();	   
	}

	private int getId_local() {
		return id_local;
	}

	private int getId_tempo() {
		return id_tempo;
	}

	private int getNu_beneficiados() {
		return nu_beneficiados;
	}

	private double getValor_total() {
		return valor_total;
	}

	private int getCodigoIbge() {
		return codigoIbge;
	}

	private double getValor() {
		return valor;
	}

	private int getQuantidadeBeneficiados() {
		return quantidadeBeneficiados;
	}
}
