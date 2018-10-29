package com.apap.tugas1.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
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
		
		if (listPegawai.size() > 0) {
			pegawaiTermuda = listPegawai.get(1);
			pegawaiTertua = listPegawai.get(1);
			
			for (PegawaiModel pegawai : listPegawai) {
				if (pegawai.getTanggalLahir().before(pegawaiTertua.getTanggalLahir())) {
					pegawaiTertua = pegawai;
				}
				else if (pegawai.getTanggalLahir().after(pegawaiTermuda.getTanggalLahir())) {
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
	
	//Fitur 2:  Menambahkan Data Pegawai di Suatu Instansi
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.GET)
	private String addPegawai(Model model) {
		PegawaiModel pegawai = new PegawaiModel();
		pegawai.setInstansi(new InstansiModel());
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("listProvinsi", provinsiService.getListProvinsi());
		model.addAttribute("listJabatan", jabatanService.listJabatan());
		return "addPegawai";
	}
	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.POST)
	private String addPegawaiSubmit(@ModelAttribute PegawaiModel pegawai, Model model) {
		//inisiasi NIP yang terdiri dari 16 digit
		String nip = "";
		
		//4 digit awal merupakan kode instansi, terdiri dari 2 digit pertama merupakan kode
		//provinsi, 2 digit berikutnya adalah nomor urut instansi di provinsi tersebut.
		nip += pegawai.getInstansi().getId();
		
		//6 digit setelahnya merupakan tanggal lahir pegawai (dd-MM-yy)
		String[] tglLahir = pegawai.getTanggalLahir().toString().split("-");
		String tglLahirString = tglLahir[2] + tglLahir[1] + tglLahir[0].substring(2, 4);
		nip += tglLahirString;
		
		//4 digit setelahnya merupakan tahun pegawai mulai bekerja di instansi tersebut.
		nip += pegawai.getTahunMasuk();
		
		//2 digit terakhir adalah nomor berdasarkan urutan pegawai yang memiliki tanggal lahir dan tahun mulai bekerja yang sama
		int counter = 1;
		for (PegawaiModel pegawaiInstansi:pegawai.getInstansi().getPegawaiInstansi()) {
			
			//jika ada pegawai baru diterima di instansi yang sama, pada tahun yang sama
			//dengan tanggal lahir yang sama, maka akan mendapatkan 0n
			if (pegawaiInstansi.getTahunMasuk().equals(pegawai.getTahunMasuk()) && pegawaiInstansi.getTanggalLahir().equals(pegawai.getTanggalLahir())) {
				counter += 1;
			}	
		}
		nip += "0" + counter;
		
		//menambahkan object pegawai ke Db
		pegawai.setNip(nip);
		pegawaiService.addPegawai(pegawai);
		model.addAttribute("pegawai", pegawai);
		return "addPegawai-done";
	}
	
	//Fitur 4: Menampilkan Data Pegawai Berdasarkan Instansi, Provinsi, dan/atau Jabatan Tertentu
	@RequestMapping(value= "/pegawai/cari", method=RequestMethod.GET)
	private String cariPegawaiSubmit(
			@RequestParam(value="idProvinsi", required = false) String idProvinsi,
			@RequestParam(value="idInstansi", required = false) String idInstansi,
			@RequestParam(value="idJabatan", required = false) String idJabatan,
			Model model) {
		
		
		
		model.addAttribute("listProvinsi", provinsiService.getListProvinsi());
		model.addAttribute("listInstansi", instansiService.listInstansi());
		model.addAttribute("listJabatan", jabatanService.listJabatan());
		List<PegawaiModel> listPegawai = pegawaiService.getListPegawai();
		
		if ((idProvinsi==null || idProvinsi.equals("")) && (idInstansi==null||idInstansi.equals("")) && (idJabatan==null||idJabatan.equals(""))) {
			return "searchPegawai";
		}
		else {
			if (idProvinsi!=null && !idProvinsi.equals("")) {
				List<PegawaiModel> temp = new ArrayList<PegawaiModel>();
				for (PegawaiModel peg: listPegawai) {
					if (((Long)peg.getInstansi().getProvinsi().getId()).toString().equals(idProvinsi)) {
						temp.add(peg);
					}
				}
				listPegawai = temp;
				model.addAttribute("idProvinsi", Long.parseLong(idProvinsi));
			}
			else {
				model.addAttribute("idProvinsi", "");
			}
			if (idInstansi!=null&&!idInstansi.equals("")) {
				List<PegawaiModel> temp = new ArrayList<PegawaiModel>();
				for (PegawaiModel peg: listPegawai) {
					if (((Long)peg.getInstansi().getId()).toString().equals(idInstansi)) {
						temp.add(peg);
					}
				}
				listPegawai = temp;
				model.addAttribute("idInstansi", Long.parseLong(idInstansi));
			}
			else {
				model.addAttribute("idInstansi", "");
			}
			if (idJabatan!=null&&!idJabatan.equals("")) {
				List<PegawaiModel> temp = new ArrayList<PegawaiModel>();
				for (PegawaiModel peg: listPegawai) {
					for (JabatanModel jabatan:peg.getJabatanPegawai()) {
						if (((Long)jabatan.getId()).toString().equals(idJabatan)) {
							temp.add(peg);
							break;
						}
					}
					
				}
				listPegawai = temp;
				model.addAttribute("idJabatan", Long.parseLong(idJabatan));
			}
			else {
				model.addAttribute("idJabatan", "");
			}
		}
		model.addAttribute("listPegawai",listPegawai);
		return "searchPegawai";
		
	}
	
	//Fitur 3: Mengubah Data Pegawai
	@RequestMapping(value = "/pegawai/ubah")
	public String changePegawai(@RequestParam("nip") String nip, Model model) {
		PegawaiModel pegawai = pegawaiService.getPegawaiDetailByNip(nip);
		List<InstansiModel> listInstansi = pegawai.getInstansi().getProvinsi().getListInstansi();
		
		model.addAttribute("listProvinsi", provinsiService.getListProvinsi());
		model.addAttribute("listJabatan", jabatanService.listJabatan());
		model.addAttribute("listInstansi", listInstansi);
		model.addAttribute("pegawai", pegawai);
		return "updatePegawai";	
	}	
	
	@RequestMapping(value = "/pegawai/ubah", method = RequestMethod.POST)
	private String ubahPegawaiSubmit(@ModelAttribute PegawaiModel pegawai, Model model) {
		String nip = "";
		
		nip += pegawai.getInstansi().getId();
		System.out.println(pegawai.getInstansi().getId());
		System.out.println(pegawai.getId());
		
		String[] tglLahir = pegawai.getTanggalLahir().toString().split("-");
		String tglLahirString = tglLahir[2] + tglLahir[1] + tglLahir[0].substring(2, 4);
		nip += tglLahirString;
		System.out.println(pegawai.getTanggalLahir());
		
		nip += pegawai.getTahunMasuk();
		System.out.println(pegawai.getTahunMasuk());
		
		int counterSama = 1;
		for (PegawaiModel pegawaiInstansi : pegawai.getInstansi().getPegawaiInstansi()) {
			if (pegawaiInstansi.getTahunMasuk().equals(pegawai.getTahunMasuk()) && pegawaiInstansi.getTanggalLahir().equals(pegawai.getTanggalLahir()) && pegawaiInstansi.getId() != pegawai.getId()) {
				counterSama += 1;
			}	
		}
		nip += "0" + counterSama;

		pegawai.setNip(nip);
		System.out.println(pegawai.getNip());
		pegawaiService.addPegawai(pegawai);
		model.addAttribute("pegawai", pegawai);
		return "updatePegawai-done";
	}
	
	
}
