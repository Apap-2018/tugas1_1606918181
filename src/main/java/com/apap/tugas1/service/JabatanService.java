package com.apap.tugas1.service;

import java.util.List;
import java.util.Optional;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.repository.JabatanDb;

public interface JabatanService {

	void addJabatan(JabatanModel jabatan);

	JabatanModel getJabatanDetailById(long id);

	void updateJabatan(long id, JabatanModel jabatan);

	void delete(JabatanModel jabatan);

	List<JabatanModel> listJabatan();
}
