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
		List<JabatanModel> listJabatan = jabatanService.listJabatan();
		model.addAttribute("listJabatan", listJabatan);
		
		//Fitur 10: Menampilkan Pegawai Termuda dan Tertua di Setiap Instansi
		List<InstansiModel> listInstansi = instansiService.listInstansi();
		model.addAttribute("listInstansi", listInstansi);
		
		return "home";
	}
	
	//Fitur 1: Menampilkan Data Pegawai Berdasarkan NIP
	@RequestMapping(value = "/pegawai", method = RequestMethod.GET)
	public String viewPegawai(@RequestParam (value = "nip") String nip, Model model) {
		PegawaiModel pegawai = pegawaiService.getPegawaiDetailByNip(nip);
		List<JabatanModel> jabatanPegawai = pegawai.getJabatanPegawai();
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
		
		List<PegawaiModel> listPegawai = instansi.getPegawaiInstansi();
		
		List<JabatanModel> listJabatan = jabatanService.listJabatan();
		model.addAttribute("listJabatan", listJabatan);
		
		PegawaiModel pegawaiTermuda;
		PegawaiModel pegawaiTertua;
		
		if (listPegawai.size()>0) {
			pegawaiTermuda = listPegawai.get(1);
			pegawaiTertua = listPegawai.get(1);
			
			for (PegawaiModel pegawai : listPegawai) {
				if (pegawai.getTanggal_lahir().before(pegawaiTertua.getTanggal_lahir())) {
					pegawaiTertua = pegawai;
				}
				else if (pegawai.getTanggal_lahir().after(pegawaiTermuda.getTanggal_lahir())) {
					pegawaiTermuda = pegawai;
				}
			}
			
			model.addAttribute("listPegawai", listPegawai);
			model.addAttribute("listJabatan", listJabatan);
			
			model.addAttribute("pegawaiTermuda", pegawaiTermuda);
			model.addAttribute("pegawaiTertua", pegawaiTertua);
		}
		return "view-pegawai-termuda-tertua";
	}
}
