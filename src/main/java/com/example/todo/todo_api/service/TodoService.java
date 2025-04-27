package com.example.todo.todo_api.service;

import java.util.List;
import java.util.UUID;

import com.example.todo.todo_api.model.Todo;

public interface TodoService {
	List<Todo> getAllTodos();
	Todo getTodoByID(UUID id);
	Todo creatTodo(Todo todo);
	Todo updateTodo(UUID id, Todo updatedTodo);
	void deleteTodo(UUID id);
}
