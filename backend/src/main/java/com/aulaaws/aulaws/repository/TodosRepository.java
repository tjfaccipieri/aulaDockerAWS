package com.aulaaws.aulaws.repository;

import com.aulaaws.aulaws.model.Todos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TodosRepository extends JpaRepository<Todos, Long> {
    public Optional<Todos> findAllByTituloContainingIgnoreCase(String titulo);
}
