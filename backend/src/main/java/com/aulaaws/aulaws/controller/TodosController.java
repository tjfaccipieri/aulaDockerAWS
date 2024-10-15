package com.aulaaws.aulaws.controller;

import com.aulaaws.aulaws.model.Todos;
import com.aulaaws.aulaws.repository.TodosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/todos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TodosController {

    @Autowired
    private TodosRepository todosRepository;

    @GetMapping
    public ResponseEntity<List<Todos>> getAllTodos() {
        return ResponseEntity.ok(todosRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todos> getTodoById(@PathVariable Long id) {
        return todosRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Todos> createNewTodo(@RequestBody Todos todo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(todosRepository.save(todo));
    }

    @PutMapping
    public ResponseEntity<Todos> updateTodo(@RequestBody Todos todo) {
        return todosRepository.findById(todo.getId())
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(todosRepository.save(todo)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodo(@PathVariable Long id) {
        Optional<Todos> todo = todosRepository.findById(id);

        if (todo.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        todosRepository.deleteById(id);
    }
}
