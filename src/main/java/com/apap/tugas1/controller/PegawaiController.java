package com.apap.tugas1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.service.InstansiService;
import com.apap.tugas1.service.JabatanService;
import com.apap.tugas1.service.PegawaiService;
import com.apap.tugas1.service.ProvinsiService;

@Controller
public class PegawaiController {
	@Autowired
	private InstansiService instansiService;
	
	@Autowired
	private JabatanService jabatanService;
	
	@Autowired
	private PegawaiService pegawaiService;
	
	@Autowired
	private ProvinsiService provinsiService;
	
	@RequestMapping("/")
	private String home(Model model) {
		
		//Fitur 6: Menampilkan Data Suatu Jabatan
		List<JabatanModel> listJabatan = jabatanService.getAllJabatan();
		model.addAttribute("listJabatan", listJabatan);
		
		//Fitur 10: Menampilkan Pegawai Termuda dan Tertua di Setiap Instansi
		List<InstansiModel> listInstansi = instansiService.getAllInstansi();
		model.addAttribute("listInstansi", listInstansi);
		
		return "home";
	}
	
	//Fitur 1: Menampilkan Data Pegawai Berdasarkan NIP
	@RequestMapping(value = "/pegawai", method = RequestMethod.GET)
	public String viewPegawai(@RequestParam (value = "nip") String nip, Model model) {
		PegawaiModel pegawai = pegawaiService.getPegawaiDetailByNip(nip);
		List<JabatanModel> jabatanPegawai = pegawai.getListJabatan();
		double gaji = pegawaiService.getGajiByNip(nip);
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("jabatanPegawai", jabatanPegawai);
		model.addAttribute("gaji", gaji);
		return "viewPegawai";
	}
	
	//Fitur 10: Menampilkan Pegawai Termuda dan Tertua di Setiap Instansi
	@RequestMapping(value = "/pegawai/termuda-tertua", method = RequestMethod.GET)
	public String viewPegawaiTermudaTertua(@RequestParam (value = "idInstansi") long id, Model model) {
		
		InstansiModel instansi = instansiService.getInstansiDetailById(id);
		
		PegawaiModel pegawaiTermuda = instansi.getListPegawai().get(instansi.getListPegawai().size()-1);
		PegawaiModel pegawaiTertua = instansi.getListPegawai().get(0);
		
		String jabatanPegawaiTermuda = instansi.getNama();
		String jabatanPegawaiTertua = instansi.getNama();
		
		
		model.addAttribute("pegawaiTermuda", pegawaiTermuda);
		model.addAttribute("jabatanPegawaiTermuda", instansi.getNama());
		
		model.addAttribute("pegawaiTertua", pegawaiTertua);
		model.addAttribute("jabatanPegawaiTertua", instansi.getNama());
		
		return "view-pegawai-termuda-tertua";
	}
}
