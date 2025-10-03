package com.gabrielrodrigues.encurtador_de_links.services;

import com.gabrielrodrigues.encurtador_de_links.exceptions.customExceptions.EntityNotFoundException;
import com.gabrielrodrigues.encurtador_de_links.models.User;
import com.gabrielrodrigues.encurtador_de_links.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getById(Long id) {
        return this.userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("O usuário com o ID " + id + " não foi encontrado!"));
    }

    public List<User> getAll() {
        return this.userRepository.findAll();
    }
}
