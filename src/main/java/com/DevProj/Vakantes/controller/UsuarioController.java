package com.DevProj.Vakantes.controller;

import com.DevProj.Vakantes.model.usuario.Usuario;
import com.DevProj.Vakantes.repository.UsuarioRepository;
import com.DevProj.Vakantes.service.CookieService;
import com.DevProj.Vakantes.service.UsuarioService;
import com.DevProj.Vakantes.service.exceptions.ObjectNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.buscarTodos());
        return "/list";
    }

    @GetMapping("/perfil")
    public String redirecionarPerfil(@ModelAttribute("currentUser") Usuario currentUser) {
        if (currentUser == null) {
            return "redirect:/auth/login";
        }
        return "redirect:/usuario/" + currentUser.getId();
    }

    @GetMapping("/{id}")
    public String perfil(@PathVariable Long id, Model model, @ModelAttribute("currentUser") Usuario currentUser) {
        if(currentUser == null) {
            throw new ObjectNotFoundException("Usuário não encontrado.");
        }
        Optional<Usuario> usuarioRequisitado = usuarioService.buscarPorId(id);
        if(usuarioRequisitado.isEmpty()) {
            throw new ObjectNotFoundException("Usuário não encontrado.");
        }
        if ((!id.equals(currentUser.getId())) && !currentUser.getUserRole().name().equals("ADMIN")) {
            throw new ObjectNotFoundException("Voce não tem permissão para acesar esse perfil.");
        }
        model.addAttribute("usuario", usuarioRequisitado.get());
        return "entities/usuario/perfil";
    }

    @GetMapping("/delete/{id}")
    public String excluirUsuario(@PathVariable Long id) {
        usuarioService.deletar(id);
        return "redirect:/usuarios";
    }

    @PostMapping("/salvar/{id}")
    public String uploadFoto(@PathVariable Long id,
                             @RequestParam("imagem") MultipartFile imagem,
                             RedirectAttributes redirectAttributes) {
        try {
            Usuario usuarioSave = usuarioRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado."));
            String nomeArquivo = usuarioService.salvarFoto(imagem);
            usuarioSave.setFotoPerfil(nomeArquivo);
            usuarioRepository.save(usuarioSave);
            redirectAttributes.addFlashAttribute("mensagem", "Perfil atualizado com sucesso!");
        } catch (IllegalArgumentException | IOException e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao enviar foto: " + e.getMessage());
        }

        return "redirect:/usuario/" + id;
    }

    @GetMapping("/{id}/foto")
    @ResponseBody
    public ResponseEntity<Resource> exibirFoto(@PathVariable Long id) throws IOException {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();

        if (usuario.getFotoPerfil() == null) return ResponseEntity.notFound().build();

        Path path = Paths.get("uploads").resolve(usuario.getFotoPerfil());
        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // ou detectar dinamicamente se quiser
                .body(resource);
    }
}
