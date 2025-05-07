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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.todo.todo_api.model.Todo;
import com.example.todo.todo_api.service.TodoService;
import com.example.todo.todo_api.service.TodoServiceImpl;


@Controller
@RequestMapping("/todos")
public class TodoController {


    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public String listTodos(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<Todo> todos = todoService.findByUser(username); 
        model.addAttribute("todos", todos);
        model.addAttribute("username", username);
        return "todo-list";
    }

    @GetMapping("/new")
    public String showAddTodoForm(Model model) {
        model.addAttribute("todo", new Todo());
        return "add-todo";
    }

    @PostMapping("/save")
    public String saveTodo(@ModelAttribute("todo") Todo todo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        todoService.creatTodoForUser(todo, username); 
        return "redirect:/todos";
    }

    @GetMapping("/edit/{id}")
    public String showEditTodoForm(@PathVariable("id") UUID id, Model model) {
        Todo todo = todoService.getTodoByID(id);
        if (todo != null) {
            model.addAttribute("todo", todo);
            return "edit-todo";
        }
        return "redirect:/todos";
    }

    @PostMapping("/update/{id}")
    public String updateTodo(@PathVariable("id") UUID id, @ModelAttribute("todo") Todo updatedTodo) {
        todoService.updateTodo(id, updatedTodo);
        return "redirect:/todos";
    }

    @GetMapping("/delete/{id}")
    public String deleteTodo(@PathVariable("id") UUID id) {
        todoService.deleteTodo(id);
        return "redirect:/todos";
    }

	
		

}
