package com.apap.tugas1.service;

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
		double presentaseTunjangan = pegawai.getInstansi().getProvinsi().getPresentase_tunjangan();
		double max_gaji = 0;
		for (JabatanModel jabatan : pegawai.getListJabatan()) {
			if (jabatan.getGaji_pokok() > max_gaji) {
				max_gaji = jabatan.getGaji_pokok();
			}
		}
		double total = max_gaji + ((presentaseTunjangan / 100) * max_gaji);
		return total;
	}
}