package com.example.FirstAppSpringStripe.controller;

import com.example.FirstAppSpringStripe.model.User;
import com.example.FirstAppSpringStripe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @PostMapping("/{userId}/roles/add")
    public ResponseEntity<String> addRoleToUser(@RequestParam String roleName, @PathVariable String userId) {
        try {
            if(roleName == null || roleName.isEmpty()) {
                return ResponseEntity.badRequest().body("Role name is required!");
            }

            userService.addRoleToUser(userId, roleName);
            return ResponseEntity.ok("Role " + roleName + " has been added to user with ID: " + userId + ".");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding the role.");
        }
    }

    @PostMapping("/{userId}/roles/remove")
    public ResponseEntity<String> removeRoleFromUser(@RequestParam String roleName, @PathVariable String userId) {
        try {
            if(roleName == null || roleName.isEmpty()) {
                return ResponseEntity.badRequest().body("Role name is required!");
            }

            userService.removeRoleFromUser(userId, roleName);
            return ResponseEntity.ok("Role " + roleName + " has been removed from user with ID: " + userId + "."); // Translated and added period
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while removing the role.");
        }
    }

    @GetMapping("/test-role")
    public String testRole(@AuthenticationPrincipal UserDetails userDetails) {
        return "Your roles: " + userDetails.getAuthorities().toString();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        try {
            userService.deleteById(id);
            return ResponseEntity.ok("User with ID: " + id + " deleted.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the user.");
        }
    }
}