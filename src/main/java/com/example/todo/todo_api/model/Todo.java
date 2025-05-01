package com.example.todo.todo_api.model;

import java.util.UUID;

public class Todo {
	private UUID id;
	private String title;
	private String description;
	private boolean completed;
	
	public Todo() {
		this.id=UUID.randomUUID();
	}

	public Todo(String title, String description) {
		this();
		this.title = title;
		this.description = description;
		this.completed = false;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getdescription() {
		return description;
	}

	public void setdescription(String description) {
		this.description = description;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
	
	
	

}
