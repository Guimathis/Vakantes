package com.vakantes.Vakantes.model;

import com.vakantes.Vakantes.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Date;


@ToString
@AllArgsConstructor
@Getter
@Setter
@Entity
@SequenceGenerator(name = "usuario_id", sequenceName = "vakantes.usuario_id_seq", allocationSize = 1, initialValue = 1)
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "usuario_id")
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "nome_completo", length = 100)
    private String nomeCompleto;

    @Column(name = "login", nullable = false, length = 50, unique = true)
    private String login;

    @Column(name = "senha", nullable = false, unique = true)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserRole userRole;

    @OneToOne
    private Contato contato;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "criado_em", updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "atualizado_em", nullable = true)
    private Date updatedAt;

    public Usuario(String login, String senha, UserRole userRole) {
        this.login = login;
        this.senha = senha;
        this.userRole = userRole;
    }

    public Usuario() {
    }


    public Usuario(String nomeCompleto, Long id, String login, String senha, UserRole userRole, Date createdAt, Date updatedAt, Contato contato) {
        this.nomeCompleto = nomeCompleto;
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.userRole = userRole;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.contato = contato;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.userRole == UserRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getLogin() {
        return login;
    }
}
