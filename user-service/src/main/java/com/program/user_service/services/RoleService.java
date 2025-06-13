package com.program.user_service.services;

import com.program.user_service.entities.db_entities.Role;
import com.program.user_service.entities.db_entities.User;
import com.program.user_service.repositories.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleService {

    @NonNull
    private RoleRepository roleRepository;
    @NonNull
    private UserService userService;

    @Transactional
    public Role createRole(String name) {
        if (roleRepository.existsByName(name))
            throw new DataIntegrityViolationException("Role with name " + name + " already exists");
        Role role = new Role();
        role.setName(name);
        return roleRepository.save(role);
    }

    @Transactional
    public Role updateRole(long id, String updatedName) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role with id " + id + " not found"));
        if (!role.getName().equals(updatedName) && roleRepository.existsByName(updatedName)) {
            throw new DataIntegrityViolationException("Role with name " + updatedName + " already exists");
        }
        role.setName(updatedName);
        return roleRepository.save(role);
    }

    @Transactional
    public void deleteRole(long id) {
        if (!roleRepository.existsById(id))
            throw new EntityNotFoundException("Role with id " + id + " not found");
        roleRepository.deleteById(id);
    }

    @Transactional
    public void deleteRoleByName(String name) {
        if (!roleRepository.existsByName(name))
            throw new EntityNotFoundException("Role with name " + name + " not found");
        roleRepository.deleteByName(name);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role with id " + id + " not found"));
    }

    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Role with name " + name + " not found"));
    }

    @Transactional
    public void deleteRoleFromUser(Long roleId, Long userId) {
        Role role = getRoleById(roleId);
        User user = userService.getUserFromDB(userId);
        Set<Role> roles = user.getRoles();
        if (!roles.contains(role)) {
            throw new EntityNotFoundException("User does not have role with id: " + roleId);
        }
        roles.remove(role);
        userService.saveUser(user);
        SecurityContextHolder.clearContext();
    }

    @Transactional
    public void addRoleToUser(Long roleId, Long userId) {
        Role role = getRoleById(roleId);
        User user = userService.getUserFromDB(userId);
        Set<Role> roles = user.getRoles();
        roles.add(role);
        userService.saveUser(user);
        SecurityContextHolder.clearContext();
    }
}
