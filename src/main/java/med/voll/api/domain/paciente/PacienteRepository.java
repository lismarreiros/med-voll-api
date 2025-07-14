package med.voll.api.domain.paciente;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    @Query("""
            SELECT p.ativo
            FROM Paciente p
            WHERE p.id = :id
            """)
    boolean findAtivoById(Long id);
}
