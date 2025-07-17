package com.quantum.auth.config;

import com.quantum.auth.model.Role;
import com.quantum.auth.model.User;
import com.quantum.auth.repo.IUserRepo;
import com.quantum.auth.repo.IRoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private IUserRepo userRepo;

    @Autowired
    private IRoleRepo roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.ldap}")
    private Boolean ldap;

    @Value("${roles}")
    private String roles;

    @Value("${roles.descriptions}")
    private String roleDescriptions;

    @Override
    public void run(String... args) throws Exception {
        // Crear los roles si no existen
        List<String> roleList = Arrays.asList(roles.split(","));
        List<String> roleDescriptionList = Arrays.asList(roleDescriptions.split(","));
        for (int i = 0; i < roleList.size(); i++) {
            String roleName = roleList.get(i);
            String roleDescription = roleDescriptionList.get(i);
            Role role = roleRepo.findByName(roleName);
            if (role == null) {
                role = new Role();
                role.setName(roleName);
                role.setDescription(roleDescription);
                roleRepo.save(role);
            } else {
                System.out.println("Role " + roleName + " ya existe.");
            }
        }

        // Verificar si ya existe un usuario administrador
        if (userRepo.findOneByUsername(adminUsername) == null) {
            // Crear el usuario administrador
            Role adminRole = roleRepo.findByName("ADMIN");
            User adminUser = new User();
            adminUser.setUsername(adminUsername);
            adminUser.setEmail(adminEmail);
            adminUser.setLdap(ldap);
            adminUser.setPassword(passwordEncoder.encode(adminPassword));
            adminUser.setEnabled(true);
            adminUser.setRoles(Collections.singletonList(adminRole));
            userRepo.save(adminUser);

            System.out.println("Usuario administrador creado: " + adminUsername + "/" + adminPassword);
        } else {
            System.out.println("Usuario administrador ya existe.");
        }
    }
}