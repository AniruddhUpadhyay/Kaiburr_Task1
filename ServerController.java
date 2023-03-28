package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/servers")
public class ServerController {

    @Autowired
    private ServerRepository serverRepository;

    @GetMapping
    public List<Server> getServers(@RequestParam(required = false) String id) {
        if (id != null) {
            Optional<Server> optionalServer = serverRepository.findById(id);
            if (optionalServer.isPresent()) {
                return List.of(optionalServer.get());
            } else {
                throw new ServerNotFoundException("Server not found with ID: " + id);
            }
        } else {
            return serverRepository.findAll();
        }
    }

    @PutMapping
    public ResponseEntity<Server> createServer(@RequestBody Server server) {
        Server createdServer = serverRepository.save(server);
        return new ResponseEntity<>(createdServer, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public void deleteServer(@PathVariable String id) {
        serverRepository.deleteById(id);
    }

    @GetMapping("/search")
    public List<Server> findServersByName(@RequestParam String name) {
        Server foundServer = serverRepository.findByNameContaining(name);
        if (foundServer == null) {
            throw new ServerNotFoundException("Server not found with name containing: " + name);
        } else {
            return List.of(foundServer);
        }
    }
}
