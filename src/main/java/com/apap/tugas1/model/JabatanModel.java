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
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.apap.tugas1.model.PegawaiModel;

@Entity
@Table(name = "jabatan")
public class JabatanModel implements Serializable{

	//id
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	//nama
	@NotNull
	@Size(max = 255)
	@Column(name = "nama", nullable = false)
	private String nama;
	
	//deskripsi
	@NotNull
	@Size(max = 255)
	@Column(name = "deskripsi", nullable = false)
	private String deskripsi;
	
	//gaji_pokok
	@NotNull
	@Column(name = "gaji_pokok", nullable = false)
	private Double gajiPokok;
	
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "jabatanPegawai")
    private List<PegawaiModel> listPegawai;
    
    //default value untuk jumlah pegawai
    private int jumlahPegawai = 0;
    
	public int getJumlahPegawai() {
		return jumlahPegawai;
	}

	public void setJumlahPegawai(int jumlahPegawai) {
		this.jumlahPegawai = jumlahPegawai;
	}

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

	public String getDeskripsi() {
		return deskripsi;
	}

	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}

	public Double getGajiPokok() {
		return gajiPokok;
	}

	public void setGajiPokok(Double gajiPokok) {
		this.gajiPokok = gajiPokok;
	}

	public int sizeJabatan() {
		return listPegawai.size();
	}
}

