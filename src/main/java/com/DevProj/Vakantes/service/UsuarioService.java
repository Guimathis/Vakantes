package com.DevProj.Vakantes.service;

import com.DevProj.Vakantes.repository.UsuarioRepository;
import com.DevProj.Vakantes.model.usuario.Usuario;
import com.DevProj.Vakantes.service.exceptions.DataBindingViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UsuarioService {

    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    @Autowired
    private final UsuarioRepository usuarioRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void salvar(Usuario usuario) throws DataBindingViolationException {
        if (isEmailValido(usuario.getEmail())) {
            throw new DataBindingViolationException("O e-mail informado não é válido.");
        }
        if (isEmailValido(usuario.getContato().getEmailContato())) {
            throw new DataBindingViolationException("O e-mail de contato informado não é válido.");
        }
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new DataBindingViolationException("Este e-mail já está em uso.");
        }
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void atualizar(Usuario usuario) {
        Optional<Usuario> usuarioAtual = usuarioRepository.findById(usuario.getId());
        if(usuarioAtual.isPresent()) {
            usuarioAtual.get().setEmail(usuario.getEmail());
            usuarioAtual.get().setNomeCompleto(usuario.getNomeCompleto());
            usuarioAtual.get().setContato(usuario.getContato());
            usuarioRepository.save(usuarioAtual.get());
        }
    }

    @Transactional
    public void deletar(Long id) {
        usuarioRepository.deleteUsuarioById(id);
    }


    public List<Usuario> listarUsuarios() {
        return (List<Usuario>) usuarioRepository.findAll();
    }

    public Usuario autenticarUsuario(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario != null && passwordEncoder.matches(senha, usuario.getSenha())) {
            return usuario;
        }
        return null;
    }

    public Iterable<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    private boolean isEmailValido(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
}