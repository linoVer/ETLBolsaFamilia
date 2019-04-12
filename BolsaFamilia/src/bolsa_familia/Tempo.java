package bolsa_familia;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;



public class Tempo {


	private Date date;
	private String nu_ano;
	private String nu_mes;
	private String nu_dia;
	private String nu_anoMes;
	private String nm_mes;
	private String sg_mes;
	private String nm_mesAno;


	private int id_tempo;	
	private int nuAno;
	private int nuMes;
	private int nuAnoMes;
	private String sgMes;
	private String nmMesAno;
	private String nmMes;
	private int nuDia;
	private int bimestre;
	private int trimestre;
	private int semestre;
	private static int count = 1;

	private static LinkedList<Tempo> tempos = new LinkedList<>();
	private static LinkedList<Tempo> temposTransform = new LinkedList<>();


	private static String sql;
	@SuppressWarnings("unused")
	private static ResultSet res;

	public Tempo(Date date, String nu_ano, String nu_mes, String nu_dia, String nu_anoMes, String nm_mes, String sg_mes,
			String nm_mesAno) {
		this.date = date;
		this.nu_ano = nu_ano;
		this.nu_mes = nu_mes;
		this.nu_dia = nu_dia;
		this.nu_anoMes = nu_anoMes;
		this.nm_mes = nm_mes;
		this.sg_mes = sg_mes;
		this.nm_mesAno = nm_mesAno;
	}


	public Tempo(int nuAno, int nuMes, int nuAnoMes, String sgMes, String nmMesAno, String nmMes,
			int nuDia, int bimestre, int trimestre, int semestre, Date date) {
		this.id_tempo = count;
		this.nuAno = nuAno;
		this.nuMes = nuMes;
		this.nuAnoMes = nuAnoMes;
		this.sgMes = sgMes;
		this.nmMesAno = nmMesAno;
		this.nmMes = nmMes;
		this.nuDia = nuDia;
		this.bimestre = bimestre;
		this.trimestre = trimestre;
		this.semestre = semestre;
		this.date = date;
		count++;
	}


	private static LinkedList<Tempo> extract() throws SQLException {

		Conexao con = new Conexao();

		con.getStmt().execute("ALTER SESSION SET CURRENT_SCHEMA = BOLSA_FAMILIA");    

		sql = "SELECT  TO_CHAR(DAT_BASE, 'YYYY') AS nu_ano, \r\n" + 
				"		TO_CHAR(DAT_BASE, 'MM') AS nu_mes, \r\n" + 
				"		TO_CHAR(DAT_BASE, 'YYYY') || TO_CHAR(DAT_BASE, 'MM') AS nu_anomes, \r\n" + 
				"		TO_CHAR(DAT_BASE, 'Mon') AS sg_mes, \r\n" + 
				"		TO_CHAR(DAT_BASE, 'Mon') ||'/'|| TO_CHAR(DAT_BASE, 'YYYY') AS nm_mesano, \r\n" + 
				"		TO_CHAR(DAT_BASE, 'Month') AS nm_mes, \r\n" + 
				"		TO_CHAR(DAT_BASE, 'DD') AS nu_dia, \r\n" + 
				"		TO_CHAR(DAT_BASE, 'YYYY-MM-DD') dt_tempo\r\n" + 
				"FROM TEMPO_IBGE \r\n" + 
				"WHERE TO_CHAR(DAT_BASE, 'DD') = 1";

		ResultSet res = con.getStmt().executeQuery(sql);

		while (res.next()) {    
			String nu_ano = res.getString("nu_ano");
			String nu_mes = res.getString("nu_mes");
			String nu_anoMes = res.getString("nu_anomes");         	
			String sg_mes = res.getString("sg_mes");
			String nm_mesAno = res.getString("nm_mesano");
			String nm_mes = res.getString("nm_mes");
			String nu_dia = res.getString("nu_dia");
			Date date = res.getDate("dt_tempo");

			tempos.add(new Tempo(date, nu_ano, nu_mes, nu_dia, nu_anoMes, nm_mes, sg_mes, nm_mesAno));              
		}		

		return tempos;		
	}

	private static LinkedList<Tempo> transform(){    	
		while(!tempos.isEmpty()) {

			int nuAno = Integer.parseInt(tempos.peek().getNu_ano());
			int nuMes = Integer.parseInt(tempos.peek().getNu_mes());
			int nuAnoMes = Integer.parseInt(tempos.peek().getNu_anoMes());
			String sgMes = tempos.peek().getSg_mes();
			String nmMesAno = tempos.peek().getNm_mesAno();
			String nmMes = tempos.peek().getNm_mes();
			int nuDia = Integer.parseInt(tempos.peek().getNu_dia());
			Date date = tempos.peek().getDate();

			int bimestre = unidadeTempo(nuMes).get(2);
			int trimestre = unidadeTempo(nuMes).get(1);
			int semestre = unidadeTempo(nuMes).get(0);


			tempos.remove();
			temposTransform.add(new Tempo(nuAno, nuMes, nuAnoMes, sgMes, nmMesAno, nmMes, nuDia, bimestre, trimestre, semestre, date));
		}	
		return temposTransform;		
	}


	private static void load() throws SQLException {

		Conexao con = new Conexao();
		con.getStmt().execute("ALTER SESSION SET CURRENT_SCHEMA = DW_BOLSA_FAMILIA");         

		sql = "INSERT INTO DM_TEMPO(ID_TEMPO, \"DATA\", NU_ANO, NU_MES, NU_DIA, NU_ANOMES, NM_MES, SG_MES, NM_MESANO, BIMESTRE, TRIMESTRE, SEMESTRE)" + 
				"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


		PreparedStatement ins = con.getCon().prepareStatement(sql);
		while(!temposTransform.isEmpty()){
			ins.setInt(1, temposTransform.peek().getId_tempo());
			ins.setDate(2, temposTransform.peek().getDate()); 
			ins.setInt(3, temposTransform.peek().getNuAno() );
			ins.setInt(4, temposTransform.peek().getNuMes());
			ins.setInt(5, temposTransform.peek().getNuDia()); 
			ins.setInt(6, temposTransform.peek().getNuAnoMes()); 
			ins.setString(7, temposTransform.peek().getNmMes()); 
			ins.setString(8, temposTransform.peek().getSgMes()); 
			ins.setString(9, temposTransform.peek().getNmMesAno()); 
			ins.setInt(10, temposTransform.peek().getBimestre()); 
			ins.setInt(11, temposTransform.peek().getTrimestre()); 
			ins.setInt(12, temposTransform.peek().getSemestre()); 

			ins.executeUpdate();
			temposTransform.remove();
		}
		ins.close();               
		con.getCon().close();
	}


	public static void executeETL() throws SQLException {		
		extract();
		transform();
		load();	   
	}



	@SuppressWarnings("unused")
	private static LinkedList<Integer> unidadeTempo(int nuMes) {

		LinkedList<Integer> unidades = new LinkedList<>();
		int semestre = 0;
		int trimestre = 0;
		int bimestre = 0;
		if(nuMes >= 1 && nuMes <= 6) {
			semestre = 1;
			if(nuMes >= 1 && nuMes <= 3) {
				trimestre = 1;
			}else if(nuMes >= 4 && nuMes <= 6) {
				trimestre = 2;
			}
			if(nuMes == 1 || nuMes == 2) {
				bimestre = 1;
			}else if(nuMes == 3 || nuMes == 4) {
				bimestre = 2;
			}else if(nuMes == 5 || nuMes == 6) {
				bimestre = 3;
			}
		}else if(nuMes >= 7 && nuMes <= 12) {
			semestre = 2;
			if(nuMes >= 7 && nuMes <= 9) {
				trimestre = 3;
			}else if(nuMes >= 10 && nuMes <= 12) {
				trimestre = 4;
			}
			if(nuMes == 7 || nuMes == 8) {
				bimestre = 4;
			}else if(nuMes == 9 || nuMes == 10) {
				bimestre = 5;
			}else if(nuMes == 11 || nuMes == 12) {
				bimestre = 6;
			}
		}
		unidades.add(semestre);
		unidades.add(trimestre);
		unidades.add(bimestre);	

		return unidades;
	}

	private Date getDate() {
		return date;
	}


	private String getNu_ano() {
		return nu_ano;
	}


	private String getNu_mes() {
		return nu_mes;
	}


	private String getNu_dia() {
		return nu_dia;
	}


	private String getNu_anoMes() {
		return nu_anoMes;
	}


	private String getNm_mes() {
		return nm_mes;
	}


	private String getSg_mes() {
		return sg_mes;
	}


	private String getNm_mesAno() {
		return nm_mesAno;
	}


	private int getId_tempo() {
		return id_tempo;
	}


	private int getNuAno() {
		return nuAno;
	}


	private int getNuMes() {
		return nuMes;
	}


	private int getNuAnoMes() {
		return nuAnoMes;
	}


	private String getSgMes() {
		return sgMes;
	}


	private String getNmMesAno() {
		return nmMesAno;
	}


	private String getNmMes() {
		return nmMes;
	}


	private int getNuDia() {
		return nuDia;
	}


	private int getBimestre() {
		return bimestre;
	}


	private int getTrimestre() {
		return trimestre;
	}


	private int getSemestre() {
		return semestre;
	}





}
