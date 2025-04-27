package com.example.todo.todo_api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.example.todo.todo_api.model.Todo;

@Service
public class TodoServiceImpl implements TodoService{
	private final Map<UUID, Todo> todos = new HashMap<>();

	@Override
	public List<Todo> getAllTodos() {
		return new ArrayList<>(todos.values());
	}

	@Override
	public Todo getTodoByID(UUID id) {
//		Todo todo=new Todo();
//		todo = todos.get(id);
//		return todo;
		return todos.get(id);
	}

	@Override
	public Todo creatTodo(Todo todo) {
		todos.put(todo.getId(), todo);
		return todo;
	}

	@Override
	public Todo updateTodo(UUID id, Todo updatedTodo) {
		if(todos.containsKey(id)) {
			updatedTodo.setId(id);
			todos.put(id, updatedTodo);
			return updatedTodo;
		}
		return null;
	}

	@Override
	public void deleteTodo(UUID id) {
		todos.remove(id);
	}

}
