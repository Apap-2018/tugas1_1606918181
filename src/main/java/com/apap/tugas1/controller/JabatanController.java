package com.apap.tugas1.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.repository.JabatanDb;
import com.apap.tugas1.service.InstansiService;
import com.apap.tugas1.service.JabatanService;
import com.apap.tugas1.service.PegawaiService;
import com.apap.tugas1.service.ProvinsiService;

@Controller
public class JabatanController {
	
	@Autowired
	private InstansiService instansiService;
	
	@Autowired
	private JabatanService jabatanService;
	
	@Autowired
	private PegawaiService pegawaiService;
	
	@Autowired
	private ProvinsiService provinsiService;
	
	//Fitur 5: Menambahkan Jabatan 
	@RequestMapping(value = "/jabatan/tambah", method = RequestMethod.GET)
	private String add(Model model) {
		model.addAttribute("jabatan", new JabatanModel());
		return "addJabatan";
	}

	@RequestMapping(value = "/jabatan/tambah", method = RequestMethod.POST)
	private String addJabatanSubmit(@ModelAttribute JabatanModel jabatan) {
		jabatanService.addJabatan(jabatan);
		return "add";
	}
	
	//Fitur 6: Menampilkan Data Suatu Jabatan
	@RequestMapping(value = "/jabatan/view", method = RequestMethod.GET)
	public String viewPegawai(@RequestParam (value = "idJabatan") long id, Model model) {
		JabatanModel jabatan = jabatanService.getJabatanDetailById(id);
		model.addAttribute("jabatan", jabatan);
		return "viewJabatan";
	}
	
	//Fitur 7: Mengubah Data Jabatan
	@RequestMapping(value = "/jabatan/ubah", method = RequestMethod.GET)
	private String update(@RequestParam(value = "idJabatan") long id, Model model) {
		JabatanModel jabatan = jabatanService.getJabatanDetailById(id);
		model.addAttribute("jabatan", jabatan);
		return "updateJabatan";
	}
	
	@RequestMapping(value = "/jabatan/ubah", method = RequestMethod.POST)
	private String updateJabatanSubmit(@RequestParam(value = "idJabatan") long id, @ModelAttribute JabatanModel jabatan) {
		jabatanService.updateJabatan(id, jabatan);
		return "update";
	}
	
	//Fitur 8: Menghapus Jabatan
	@RequestMapping(value = "/jabatan/hapus", method = RequestMethod.GET)
	private String deleteJabatan(@RequestParam("idJabatan") long id, Model model) {
		JabatanModel jabatan = jabatanService.getJabatanDetailById(id);
		if (jabatan.sizeJabatan() < 1) {
			jabatanService.delete(jabatan);
			return "delete";
		}
		return "deleteJabatan-Error";
	}
	
	//Fitur 9: Menampilkan Daftar Jabatan
	@RequestMapping(value = "/jabatan/viewall", method = RequestMethod.GET)
	private String viewAllJabatan(Model model) { 
		List<JabatanModel> listJabatan = jabatanService.listJabatan();
		model.addAttribute("listJabatan", listJabatan);
		return "viewAllJabatan";
	}
}
