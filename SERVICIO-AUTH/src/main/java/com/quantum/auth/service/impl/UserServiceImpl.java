package com.quantum.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quantum.auth.model.Dependency;
import com.quantum.auth.model.Role;
import com.quantum.auth.model.User;
import com.quantum.auth.repo.IDependencyRepo;
import com.quantum.auth.repo.IGenericRepo;
import com.quantum.auth.repo.IRoleRepo;
import com.quantum.auth.repo.IUserRepo;
import com.quantum.auth.service.IUserService;

@Service
public class UserServiceImpl extends CRUDImpl<User, Integer> implements IUserService {

    @Autowired
    private IUserRepo userRepo;

    @Autowired
    private IRoleRepo roleRepo;

    @Autowired
    private IDependencyRepo dependencyRepo;

    @Override
    protected IGenericRepo<User, Integer> getRepo() {
        return userRepo;
    }

    @Override
    public User findOneByUsername(String username) {
        return userRepo.findOneByUsername(username);
    }

    @Override
    public void addRole(Integer userId, Integer roleId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepo.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            userRepo.save(user);
        }
    }

    @Override
    public void removeRole(Integer userId, Integer roleId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepo.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
        user.getRoles().remove(role);
        userRepo.save(user);
    }

    @Override
    public void addDependency(Integer userId, Integer dependencyId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Dependency dependency = dependencyRepo.findById(dependencyId).orElseThrow(() -> new RuntimeException("Dependency not found"));
        boolean hasUDependency = user.getRoles().stream().anyMatch(r -> "UDEPENDENCY".equalsIgnoreCase(r.getName()));
        if (hasUDependency && user.getDependencies().size() >= 1) {
            throw new IllegalStateException("Un usuario con rol UDEPENDENCY solo puede tener una dependencia.");
        }
        if (!user.getDependencies().contains(dependency)) {
            user.getDependencies().add(dependency);
            userRepo.save(user);
        }
    }

    @Override
    public void removeDependency(Integer userId, Integer dependencyId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Dependency dependency = dependencyRepo.findById(dependencyId).orElseThrow(() -> new RuntimeException("Dependency not found"));
        user.getDependencies().remove(dependency);
        userRepo.save(user);
    }
    
}