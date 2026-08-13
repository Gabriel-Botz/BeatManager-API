package br.com.gabriel.beatmanager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gabriel.beatmanager.model.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    Page<Evento> findByAdministradorId(Long administradorId, Pageable pageable);
}