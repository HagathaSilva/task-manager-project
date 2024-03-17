package com.example.taskmanager.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.taskmanager.entities.Task;
import com.example.taskmanager.entities.User;
import com.example.taskmanager.repositories.TaskRepository;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserService userService;

    public Task findById(Long id) {
        Optional<Task> task = this.taskRepository.findById(id);
        return task.orElseThrow(() -> new RuntimeException(
                "Tarefa não encontrada!"));
    }

    @Transactional
    public Task crateTask(Task task) {
        User user = this.userService.findById(task.getUser().getId());
        task.setId(null);
        task.setUser(user);
        this.taskRepository.save(task);
        return task;
    }

    @Transactional
    public Task updateTask(Task task) {
        Task newTask = findById(task.getId());
        newTask.setStatus(task.getStatus());
        newTask.setDescription(task.getDescription());
        return this.taskRepository.save(newTask);
    }

    public void delete(Long id) {
        findById(id);
        try {
            this.taskRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Não foi possivel deletar, pois há entidades relacionadas!");
        }
    }
}
