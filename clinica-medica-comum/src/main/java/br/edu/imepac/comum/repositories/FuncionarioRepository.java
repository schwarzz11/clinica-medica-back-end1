package br.edu.imepac.comum.repositories;

import br.edu.imepac.comum.models.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;



@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    /**
     * Define um método para buscar um funcionário pelo seu nome de usuário.
     * O Spring Data JPA cria a implementação para este método automaticamente.
     * @param usuario O nome de usuário a ser procurado.
     * @return um Optional contendo o funcionário se encontrado, ou vazio caso contrário.
     */
    Optional<Funcionario> findByUsuario(String usuario);
}
