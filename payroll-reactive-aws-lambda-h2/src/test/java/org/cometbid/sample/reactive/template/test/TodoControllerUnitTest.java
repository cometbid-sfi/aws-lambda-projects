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
package org.cometbid.sample.reactive.template.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import org.cometbid.sample.reactive.template.controller.TodoController;
import org.cometbid.sample.reactive.template.exceptions.TodoNotFoundException;
import org.cometbid.sample.reactive.template.response.ErrorResponse;
import org.cometbid.sample.reactive.template.todo.Todo;
import org.cometbid.sample.reactive.template.todo.TodoService;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author samueladebowale
 */
@WebFluxTest(controllers = TodoController.class)
@AutoConfigureWebTestClient
public class TodoControllerUnitTest {

    private final WebTestClient webTestClient;

    @MockBean
    private final TodoService todoService;

    private final String url = "/api/v1/todos";

    @Autowired
    public TodoControllerUnitTest(WebTestClient webTestClient, TodoService todoService) {
        this.webTestClient = webTestClient;
        this.todoService = todoService;
    }

    @Test
    void findTodoById_test_should_return_todo_object_and_status_should_be_ok() {
        var todo = Todo.builder().id(1).title("test title 1").description("test description 1").build();

        when(todoService.findTodoById(todo.getId())).thenReturn(Mono.just(todo));

        String findTodoByIdUrl = String.format("%s/%s", url, todo.getId());
        webTestClient
                .get()
                .uri(findTodoByIdUrl)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Todo.class)
                .consumeWith(result -> {
                    var existTodo = result.getResponseBody();

                    assert existTodo != null;
                    assertEquals(todo.getId(), existTodo.getId());
                    assertEquals(todo.getTitle(), existTodo.getTitle());

                });
    }

    @Test
    void findTodoById_test_should_return_error_response_object_and_status_should_be_not_found() {
        var todo = Todo.builder().id(3).title("test title 3").description("test description 3").build();

        when(todoService.findTodoById(todo.getId())).thenReturn(
                Mono.error(new TodoNotFoundException(String.format("Todo not found. ID: %s", todo.getId()))));

        String findTodoByIdUrl = String.format("%s/%s", url, todo.getId());
        webTestClient
                .get()
                .uri(findTodoByIdUrl)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ErrorResponse.class)
                .consumeWith(result -> {
                    var errorResponse = result.getResponseBody();

                    assertEquals(false, errorResponse.isSuccess());
                    assertEquals("Todo not found. ID: 3", errorResponse.getMessage());
                });
    }

    @Test
    void findAllTodos_test_should_return_todo_list_and_status_should_be_ok() {
        var todos = List.of(
                Todo.builder().id(1).title("test title 1")
                        .description("test description 1")
                        .build(),
                Todo.builder().id(2).title("test title 2")
                        .description("test description 2")
                        .build(),
                Todo.builder().id(3).title("test title 3")
                        .description("test description 3")
                        .build()
        );

        when(todoService.findAllTodos()).thenReturn(Flux.fromIterable(todos));

        webTestClient
                .get()
                .uri(url)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Todo.class)
                .hasSize(3);
    }

    @Test
    void saveTodo_test_should_return_saved_todo_and_status_should_be_created() {
        var todo = Todo.builder().id(1).title("test title 1").description("test description 1").build();

        when(todoService.saveTodo(isA(Todo.class))).thenReturn(Mono.just(todo));

        webTestClient
                .post()
                .uri(url)
                .bodyValue(todo)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Todo.class)
                .consumeWith(result -> {
                    var savedTodo = result.getResponseBody();

                    assert savedTodo != null;
                    assertEquals(todo.getId(), savedTodo.getId());
                    assertEquals(todo.getTitle(), savedTodo.getTitle());

                });
    }

    @Test
    void deleteTodoById_test_should_return_empty_and_status_should_be_no_content() {
        int todoId = 3;

        when(todoService.deleteTodoById(todoId)).thenReturn(Mono.empty());

        String deleteUrl = String.format("%s/%s", url, todoId);
        webTestClient
                .delete()
                .uri(deleteUrl)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

}
