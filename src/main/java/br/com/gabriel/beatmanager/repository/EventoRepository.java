package br.com.gabriel.beatmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gabriel.beatmanager.model.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    List<Evento> findByAdministradorId(Long administradorId);
}