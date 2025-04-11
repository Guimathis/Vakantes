package com.DevProj.Vakantes.controller;

import com.DevProj.Vakantes.model.candidato.Candidato;
import com.DevProj.Vakantes.model.empresa.Cliente;
import com.DevProj.Vakantes.model.util.Status;
import com.DevProj.Vakantes.model.vaga.Vaga;
import com.DevProj.Vakantes.model.vaga.VagaDTO;
import com.DevProj.Vakantes.repository.CandidatoRepository;
import com.DevProj.Vakantes.repository.ClienteRepository;
import com.DevProj.Vakantes.repository.VagaRepository;
import com.DevProj.Vakantes.service.CandidatoService;
import com.DevProj.Vakantes.service.ClienteService;
import com.DevProj.Vakantes.service.CookieService;
import com.DevProj.Vakantes.service.VagaService;
import com.DevProj.Vakantes.service.exceptions.ObjectNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller("VC")
@RequestMapping("/vaga")
public class VagaController {

	@Autowired
	private VagaRepository vr;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	CandidatoService candidatoService;
	@Autowired
	ClienteService clienteService;

	@Autowired
	VagaService vagaService;

    @Autowired
    private CandidatoRepository candidatoRepository;

	// CADASTRA VAGA
	@GetMapping(value = "/cadastro")
	public String form(Model model) {
		model.addAttribute("clientes", clienteService.buscarTodos());
		model.addAttribute("vaga" , model.getAttribute("vaga") == null ? new VagaDTO() : model.getAttribute("vaga"));
		return "entities/vaga/cadastro";
	}

	@GetMapping(value = "/cadastro/{id}")
	public String form(@PathVariable Long id,  Model model, RedirectAttributes attributes) {
		VagaDTO vaga = new VagaDTO();

        try {
			Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Cliente não encontrado"));
			vaga.setIdCliente(cliente.getId());
			model.addAttribute("clientes", clienteService.buscarTodos());
			model.addAttribute("vaga", vaga);
        } catch (ObjectNotFoundException e) {
			attributes.addFlashAttribute("mensagem_erro", e.getMessage());
        }

		return "entities/vaga/cadastro";
	}

	@PostMapping("/salvar")
	public String form(@Valid VagaDTO vagaDTO, BindingResult result, RedirectAttributes attributes, Model model) {

		if (result.hasErrors()) {
			model.addAttribute("vaga", vagaDTO);
			model.addAttribute("clientes", clienteService.buscarTodos());
			attributes.addFlashAttribute("mensagem_erro", result.getAllErrors().get(0).getDefaultMessage());
			attributes.addFlashAttribute("vaga", vagaDTO);
			return "redirect:/vaga/cadastro";
		}

		Long id = vagaService.cadastrarVaga(vagaDTO);

        if(vagaDTO.getCodigo() != 0){
			attributes.addFlashAttribute("mensagem", "Vaga editada com sucesso!");
		} else {
			attributes.addFlashAttribute("mensagem", "Vaga cadastrada com sucesso!");
		}
		return "redirect:/vaga/buscar/" + id;
	}

	// LISTA VAGAS
	@GetMapping("/buscar")
	public String listaVaga(Model model, HttpServletRequest request) throws UnsupportedEncodingException {
		String idUsuario = CookieService.getCookie(request, "usuarioId");
		model.addAttribute("idUsuario",idUsuario);
		model.addAttribute("vagas", vagaService.buscarTodas());
		return "entities/vaga/buscar";
	}

	//
	@GetMapping ("/buscar/{codigo}")
	public String detalhesVaga(@PathVariable("codigo") long codigo, Model model) {
		Vaga vaga = vr.findByCodigoAndStatus(codigo, Status.ATIVO).orElseThrow(() -> new ObjectNotFoundException("Vaga não encontrada"));
		VagaDTO vagaDTO = new VagaDTO(vaga);
		model.addAttribute("vaga", vagaDTO);
		model.addAttribute("clientes", clienteService.buscarTodos());
		model.addAttribute("editar", false);
		return "entities/vaga/cadastro";
	}

	// Métodos que atualizam vaga
	// formulário edição de vaga
	@GetMapping(value = "/editar/{codigo}")
	public String editarVaga(Model model, @PathVariable long codigo) {
		Vaga vaga = vr.findByCodigoAndStatus(codigo, Status.ATIVO).orElseThrow(() -> new ObjectNotFoundException("Vaga não encontrada"));;
		VagaDTO vagaDTO = new VagaDTO(vaga);
		model.addAttribute("vaga", vagaDTO);
		model.addAttribute("clientes", clienteService.buscarTodos());
		model.addAttribute("editar", true);
		return "entities/vaga/cadastro";
	}


	@GetMapping(value = "/detalhes/{codigo}")
	public String detalhesVaga(Model model, @PathVariable long codigo){
		Vaga vaga = vr.findByCodigoAndStatus(codigo, Status.ATIVO).orElseThrow(() -> new ObjectNotFoundException("Vaga não encontrada"));;
		VagaDTO vagaDTO = new VagaDTO(vaga);
		model.addAttribute("vaga", vagaDTO);
		model.addAttribute("candidato", new Candidato());
		model.addAttribute("candidatosCadastrados", vaga.getCandidatos());
		model.addAttribute("candidatosSistema", candidatoRepository.findAll());
		return "entities/vaga/detalhes";
	}



	// DELETA VAGA
	@RequestMapping("/deletar/{id}")
	public String deletarVaga(@PathVariable Long id, RedirectAttributes attributes) {
		try {
			vagaService.deletarVaga(id);
			attributes.addFlashAttribute("mensagem", "Vaga deletada com sucesso!");
		} catch (Exception e) {
			attributes.addFlashAttribute("mensagem_erro", e.getMessage());
		}
		return "redirect:/vaga/buscar";
	}

	// ADICIONAR CANDIDATO
	@PostMapping("/candidato/novo/{codigo}")
	public String detalhesVagaPost(@PathVariable("codigo") long codigo, @Valid Candidato candidato,
			BindingResult result, RedirectAttributes attributes) {

		if (result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos");
			return "redirect:/vaga/detalhes/{codigo}";
		}

        try {
            vagaService.adicionarCandidatoAVaga(codigo, candidato);
			attributes.addFlashAttribute("mensagem", "Candidato cadastrado com sucesso!");
        } catch (Exception e) {
			attributes.addFlashAttribute("mensagem_erro", e.getMessage());
        }

		return "redirect:/vaga/detalhes/{codigo}";
	}

	@PostMapping("/candidato/deletar/{cpf}")
	public String deletarCandidato(@PathVariable String cpf,  HttpServletRequest request, RedirectAttributes attributes) {
		candidatoService.deletarCandidato(cpf);
		attributes.addFlashAttribute("mensagem", "Candidato removido com sucesso!");
		return "redirect:" + request.getHeader("Referer");
	}

	@PostMapping("/candidato/existente/{id}")
	public String associarCandidatoExistente( @PathVariable("id") Long vagaId, @RequestParam("cpfs") List<String> cpfs,
			RedirectAttributes redirectAttributes) {

		try {
			vagaService.inscreverCandidatos(vagaId, cpfs);
			redirectAttributes.addFlashAttribute("mensagem", "Candidato inscrito com sucesso!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("mensagem_erro", e.getMessage());
		}

		return "redirect:/vaga/detalhes/" + vagaId;
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
