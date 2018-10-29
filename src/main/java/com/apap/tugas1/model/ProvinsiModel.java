package com.apap.tugas1.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "provinsi")
public class ProvinsiModel implements Serializable{

	//id
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	//nama
	@NotNull
	@Size(max = 255)
	@Column(name = "nama", nullable = false)
	private String nama;
	
	//presentasi_tunjangan
	@NotNull
	@Column(name = "presentase_tunjangan", nullable = false)
	private Double presentaseTunjangan;
	
	//relationship ke instansi
	@OneToMany(mappedBy = "provinsi", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private List<InstansiModel> instansiProvinsi;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public Double getPresentaseTunjangan() {
		return presentaseTunjangan;
	}

	public void setPresentaseTunjangan(Double presentaseTunjangan) {
		this.presentaseTunjangan = presentaseTunjangan;
	}

	public List<InstansiModel> getInstansiProvinsi() {
		return instansiProvinsi;
	}

	public void setInstansiProvinsi(List<InstansiModel> instansiProvinsi) {
		this.instansiProvinsi = instansiProvinsi;
	}

	public List<InstansiModel> getListInstansi() {
		return instansiProvinsi;
	}

	public void setListInstansi(List<InstansiModel> listInstansi) {
		this.instansiProvinsi = listInstansi;
	}
}
