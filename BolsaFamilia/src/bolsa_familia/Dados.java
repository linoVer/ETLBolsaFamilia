package bolsa_familia;

import com.google.gson.annotations.SerializedName;

public class Dados {
	
	private int id;
    @SerializedName(value = "dataReferencia")
	private String dataReferencia;
	private Municipio municipio;
	private Tipo tipo;
	private double valor;
	private int quantidadeBeneficiados;
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDataReferencia() {
		return dataReferencia;
	}
	public void setDataReferencia(String dataReferencia) {
		this.dataReferencia = dataReferencia;
	}
	public Municipio getMunicipio() {
		return municipio;
	}
	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}
	public Tipo getTipo() {
		return tipo;
	}
	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
	public int getQuantidadeBeneficiados() {
		return quantidadeBeneficiados;
	}
	public void setQuantidadeBeneficiados(int quantidadeBeneficiados) {
		this.quantidadeBeneficiados = quantidadeBeneficiados;
	}
	

	
	
	
	

}
