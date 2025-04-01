package com.DevProj.Vakantes.controller;

import com.DevProj.Vakantes.model.candidato.Candidato;
import com.DevProj.Vakantes.model.empresa.Cliente;
import com.DevProj.Vakantes.model.vaga.Vaga;
import com.DevProj.Vakantes.model.vaga.VagaDTO;
import com.DevProj.Vakantes.repository.CandidatoRepository;
import com.DevProj.Vakantes.repository.ClienteRepository;
import com.DevProj.Vakantes.repository.VagaRepository;
import com.DevProj.Vakantes.service.CookieService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/vaga")
public class VagaController {

	@Autowired
	private VagaRepository vr;

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private CandidatoRepository cr;

	// CADASTRA VAGA
	@GetMapping(value = "/cadastro")
	public String form(Model model) {
		model.addAttribute("clientes", clienteRepository.findAll());
		model.addAttribute("vaga" , new VagaDTO());
		return "entities/vaga/cadastro";
	}

	@PostMapping("/salvar")
	public String form(@Valid VagaDTO vagaDTO, BindingResult result, RedirectAttributes attributes, Model model) {

		if (result.hasErrors()) {
			model.addAttribute("vaga", vagaDTO);
			model.addAttribute("clientes", clienteRepository.findAll());
			attributes.addFlashAttribute("mensagem_erro", "Verifique os campos...");
			return "entities/vaga/cadastro";
		}
		Vaga vaga = new Vaga(vagaDTO, clienteRepository.findById(vagaDTO.getIdCliente()).get());

		vr.save(vaga);
		attributes.addFlashAttribute("mensagem", "Vaga cadastrada com sucesso!");
		return "redirect:/vaga/buscar/" + vaga.getCodigo();
	}

	// LISTA VAGAS
	@GetMapping("/buscar")
	public String listaVaga(Model model, HttpServletRequest request) throws UnsupportedEncodingException {
		String idUsuario = CookieService.getCookie(request, "usuarioId");
		model.addAttribute("idUsuario",idUsuario);
		Iterable<Vaga> vagas = vr.findAll();
		model.addAttribute("vagas", vagas);
		return "entities/vaga/buscar";
	}

	//
	@GetMapping ("/buscar/{codigo}")
	public String detalhesVaga(@PathVariable("codigo") long codigo, Model model) {
		Vaga vaga = vr.findByCodigo(codigo);
		VagaDTO vagaDTO = new VagaDTO(vaga);
		model.addAttribute("vaga", vagaDTO);
		model.addAttribute("clientes", clienteRepository.findAll());
		model.addAttribute("editar", false);
		return "entities/vaga/cadastro";
	}

	// DELETA VAGA
	@RequestMapping("/deletarVaga")
	public String deletarVaga(long codigo) {
		Vaga vaga = vr.findByCodigo(codigo);
		vr.delete(vaga);
		return "redirect:/vagas";
	}

	// ADICIONAR CANDIDATO
	@RequestMapping(value = "/{codigo}", method = RequestMethod.POST)
	public String detalhesVagaPost(@PathVariable("codigo") long codigo, @Valid Candidato candidato,
			BindingResult result, RedirectAttributes attributes) {

		if (result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos");
			return "redirect:/{codigo}";
		}

		// rg duplicado
		if (cr.findByRg(candidato.getRg()) != null) {
			attributes.addFlashAttribute("mensagem_erro", "RG duplicado");
			return "redirect:/{codigo}";
		}

		Vaga vaga = vr.findByCodigo(codigo);
		candidato.setVaga(vaga);
		cr.save(candidato);
		attributes.addFlashAttribute("mensagem", "Candidato adionado com sucesso!");
		return "redirect:/{codigo}";
	}

	// DELETA CANDIDATO pelo RG
	@RequestMapping("/deletarCandidato")
	public String deletarCandidato(String rg) {
		Candidato candidato = cr.findByRg(rg);
		Vaga vaga = candidato.getVaga();
		String codigo = "" + vaga.getCodigo();

		cr.delete(candidato);

		return "redirect:/" + codigo;

	}

	// Métodos que atualizam vaga
	// formulário edição de vaga
	@GetMapping(value = "/editar/{codigo}")
	public String editarVaga(Model model, @PathVariable long codigo) {
		Vaga vaga = vr.findByCodigo(codigo);
		VagaDTO vagaDTO = new VagaDTO(vaga);
		model.addAttribute("vaga", vagaDTO);
		model.addAttribute("clientes", clienteRepository.findAll());
		model.addAttribute("editar", true);
		return "entities/vaga/cadastro";
	}

	// UPDATE vaga
	@RequestMapping(value = "/editar-vaga", method = RequestMethod.POST)
	public String updateVaga(@Valid Vaga vaga, BindingResult result, RedirectAttributes attributes) {
		vr.save(vaga);
		attributes.addFlashAttribute("success", "Vaga alterada com sucesso!");

		long codigoLong = vaga.getCodigo();
		String codigo = "" + codigoLong;
		return "redirect:/" + codigo;
	}

}
