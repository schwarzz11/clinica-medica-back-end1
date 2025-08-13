package br.edu.imepac.comum.models;

import br.edu.imepac.comum.domain.EnumTipoFuncionario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "funcionarios")
@SQLDelete(sql = "UPDATE funcionarios SET ativo = false WHERE id = ?")
@Where(clause = "ativo = true")
// Implementa UserDetails para integrar com o Spring Security
public class Funcionario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true, length = 50)
    private String usuario; // Este será o "username" para o Spring Security

    @Column(nullable = false)
    private String senha; // Esta será a "password"

    private char sexo;
    private LocalDate dataNascimento;
    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;

    @Enumerated(EnumType.STRING)
    private EnumTipoFuncionario tipoFuncionario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "perfil_id")
    private Perfil perfil;

    private boolean ativo = true;

    // =======================================================
    // MÉTODOS DA INTERFACE UserDetails
    // =======================================================

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // A "authority" (permissão) do nosso usuário será o nome do seu perfil.
        // Ex: "ADMINISTRADOR", "MEDICO".
        if (this.perfil != null) {
            return List.of(new SimpleGrantedAuthority(this.perfil.getNome()));
        }
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.usuario;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // A conta não expira
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // A conta não é bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // As credenciais não expiram
    }

    @Override
    public boolean isEnabled() {
        return this.ativo; // A conta está ativa se o funcionário estiver ativo
    }
}