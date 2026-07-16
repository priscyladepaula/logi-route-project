package br.com.priscyladepaula.logirouteproject.infrastructure.database;

import br.com.priscyladepaula.logirouteproject.domain.ConsultaLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataLogRepository extends JpaRepository<ConsultaLog, Long> {
}