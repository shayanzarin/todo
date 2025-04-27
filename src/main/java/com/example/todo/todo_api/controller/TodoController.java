package com.example.todo.todo_api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.todo_api.model.Todo;
import com.example.todo.todo_api.service.TodoService;
import com.example.todo.todo_api.service.TodoServiceImpl;

@RestController
@RequestMapping("/api/todos")
public class TodoController {
	
	private final TodoService todoService;
	
	@Autowired
	public TodoController(TodoService todoService) {
		super();
		this.todoService = todoService;
	}
	
	@GetMapping
	public ResponseEntity<List<Todo>> getAllTodos(){
		List<Todo> todos = todoService.getAllTodos();
		return new ResponseEntity<>(todos, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Todo> getTodoById(@PathVariable UUID id){
		Todo todo = todoService.getTodoByID(id);
		
		if(todo != null) {
			return new ResponseEntity<>(todo, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping
	public ResponseEntity<Todo> creatTodo(@RequestBody Todo todo){
		Todo createdTodo = todoService.creatTodo(todo);
		return new ResponseEntity<>(createdTodo, HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Todo> updateTodo(@PathVariable UUID id, @RequestBody Todo todo){
		Todo todo1 = todoService.updateTodo(id, todo);
		if (todo1 != null) {
			return new ResponseEntity<>(todo1, HttpStatus.OK);
		}else {
			return new ResponseEntity<Todo>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTodo(@PathVariable UUID id ){
		todoService.deleteTodo(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	

	
		

}
