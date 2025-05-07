package com.example.todo.todo_api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.todo.todo_api.model.Todo;
import com.example.todo.todo_api.model.User;
import com.example.todo.todo_api.repository.TodoRepository;
import com.example.todo.todo_api.repository.UserRepository;

@Service
public class TodoServiceImpl implements TodoService{
	
//	private final Map<UUID, Todo> todos = new HashMap<>();
	
	private final TodoRepository todoRepository;
	private final UserRepository userRepository; 
	
	@Autowired
	public TodoServiceImpl (TodoRepository todoRepository, UserRepository userRepository) {
		this.todoRepository = todoRepository;
		this.userRepository = userRepository;
	}

	   private User getCurrentUser() {
	        org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
	            String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
	            return userRepository.findByUsername(username)
	                    .orElseThrow(() -> new RuntimeException("Current user not found"));
	        }
	        throw new IllegalStateException("No authenticated user found.");
	    }
	@Override
	public List<Todo> getAllTodos() {
		User user = getCurrentUser();
		return todoRepository.findByUser(user);
	}

	@Override
	public Todo getTodoByID(UUID id) {
		User currentUser = getCurrentUser();
		return todoRepository.findById(id).filter(todo -> todo.getUser().equals(currentUser)).orElse(null);
	}

	@Override
	public Todo creatTodo(Todo todo) {
		User currentUser = getCurrentUser();
		todo.setUser(currentUser);
		return todoRepository.save(todo);
	}

	@Override
	public Todo updateTodo(UUID id, Todo updatedTodo) {
		User currentUsser = getCurrentUser();
		return todoRepository.findById(id).filter(todo -> todo.getUser().equals(currentUsser)).map(existedTodo ->{
			updatedTodo.setId(id);
			updatedTodo.setUser(currentUsser);
			return todoRepository.save(updatedTodo);
		}).orElse(null);
	}

	@Override
	public void deleteTodo(UUID id) {
		User currentUser = getCurrentUser();
			todoRepository.findById(id).filter(todo -> todo.getUser().equals(currentUser))
			.ifPresent(todo -> todoRepository.deleteById(id));
	}

    @Override
    public List<Todo> findByUser(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.map(todoRepository::findByUser).orElse(List.of());
    }

    @Override
    public Todo creatTodoForUser(Todo todo, String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.map(user -> {
            todo.setUser(user);
            return todoRepository.save(todo);
        }).orElse(null);
    }

}
