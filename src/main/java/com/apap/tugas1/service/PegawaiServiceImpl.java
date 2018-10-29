package com.apap.tugas1.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.repository.PegawaiDb;


@Service
@Transactional
public class PegawaiServiceImpl implements PegawaiService {

	@Autowired
	private PegawaiDb pegawaiDb;

	@Override
	public PegawaiModel getPegawaiDetailByNip(String nip) {
		return pegawaiDb.findByNip(nip);
	}

	@Override
	//Gaji = gaji pokok + (%tunjangan x gaji pokok)
	//gaji_pokok di jabatan
	//presentase_tunjangan di provinsi
	//lebih dari 1 jabatan, gaji pokok yang paling besar.
	public double getGajiByNip(String nip) {
		PegawaiModel pegawai = this.getPegawaiDetailByNip(nip);
		double presentaseTunjangan = pegawai.getInstansi().getProvinsi().getPresentaseTunjangan();
		double maxGaji = 0;
		for (JabatanModel jabatan : pegawai.getJabatanPegawai()) {
			if (jabatan.getGajiPokok() > maxGaji) {
				maxGaji = jabatan.getGajiPokok();
			}
		}
		double total = maxGaji + ((presentaseTunjangan / 100) * maxGaji);
		return total;
	}

	@Override
	public void addPegawai(PegawaiModel pegawai) {
		pegawaiDb.save(pegawai);
		
	}

	@Override
	public PegawaiModel getPegawaiById(Long id) {
		return pegawaiDb.getOne(id);
	}

	@Override
	public List<PegawaiModel> getListPegawai() {
		return pegawaiDb.findAll();
	}
}