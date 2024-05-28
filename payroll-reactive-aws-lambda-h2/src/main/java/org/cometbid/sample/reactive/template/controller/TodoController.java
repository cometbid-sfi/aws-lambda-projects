/*
 * The MIT License
 *
 * Copyright 2024 samueladebowale.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.cometbid.sample.reactive.template.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.cometbid.sample.reactive.template.todo.Todo;
import org.cometbid.sample.reactive.template.todo.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author samueladebowale
 */
@RestController
@RequestMapping("/api/v1/todos")
public class TodoController {

    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    /**
     * 
     * @param id
     * @return 
     */
    @GetMapping("/{id}")
    @Operation(summary = "Find Todo by id.")
    Mono<ResponseEntity<Todo>> findTodoById(@PathVariable int id) {

        return todoService.findTodoById(id)
                .map(todo -> ResponseEntity.ok(todo));
    }

    /**
     * 
     * @return 
     */
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Find all Todos")
    Flux<Todo> findAllTodos() {
        return todoService.findAllTodos().log();
    }

    /**
     * 
     * @param todo
     * @return 
     */
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create Todo.")
    Mono<ResponseEntity<Todo>> saveTodo(@RequestBody Todo todo) {
        return todoService.saveTodo(todo)
                .map(savedTodo -> ResponseEntity.status(HttpStatus.CREATED).body(savedTodo));
    }

    /**
     * 
     * @param id
     * @return 
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete Todo by id.")
    Mono<Void> deleteTodoById(@PathVariable int id) {
        return todoService.deleteTodoById(id);
    }

    /**
     * 
     * @param title
     * @return 
     */
    @GetMapping("/search")
    @Operation(summary = "Search Todo by title.")
    Mono<ResponseEntity<Todo>> searchTodoByTitle(@RequestParam String title) {
        return todoService.searchTodoByTitle(title)
                .map(todo -> ResponseEntity.ok(todo))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
