package com.apap.tugas1.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.repository.JabatanDb;

@Service
@Transactional
public class JabatanServiceImpl implements JabatanService {
	@Autowired
	private JabatanDb jabatanDb;

	@Override
	public void addJabatan(JabatanModel jabatan) {
		jabatanDb.save(jabatan);
	}

	@Override
	public JabatanModel getJabatanDetailById(long id) {
		return jabatanDb.getOne(id);
	}

	@Override
	public List<JabatanModel> listJabatan() {
		return jabatanDb.findAll();
	}

	@Override
	public void updateJabatan(long id, JabatanModel newJabatan) {
		JabatanModel jabatan = jabatanDb.getOne(id);
		jabatan.setNama(newJabatan.getNama());
		jabatan.setDeskripsi(newJabatan.getDeskripsi());
		jabatan.setGaji_pokok(newJabatan.getGaji_pokok());
		jabatanDb.save(jabatan);
	}

	@Override
	public void delete(JabatanModel jabatan) {
		jabatanDb.delete(jabatan);
	}
}