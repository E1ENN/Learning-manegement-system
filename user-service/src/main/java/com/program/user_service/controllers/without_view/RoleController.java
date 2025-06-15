package com.program.user_service.controllers.without_view;

import com.program.user_service.entities.db_entities.Role;
import com.program.user_service.entities.support_entities.request_entities.RoleCreateRequest;
import com.program.user_service.entities.support_entities.request_entities.RoleUpdateRequest;
import com.program.user_service.services.RoleService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/non-view/api/roles")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleController {

    @NonNull private RoleService roleService;

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoleById(@PathVariable("id") Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/create")
    public ResponseEntity<Role> createRole(@RequestBody RoleCreateRequest request) {
        return new ResponseEntity<>(roleService.createRole(request.getName()), HttpStatus.CREATED);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable("id") Long id,
                                           @Valid @RequestBody RoleUpdateRequest request) {
        return ResponseEntity.ok(roleService.updateRole(id, request.getName()));
    }

    @DeleteMapping("/{roleId}/users/{userId}")
    public ResponseEntity<Void> deleteRoleFromUser(@PathVariable("roleId") Long roleId,
                                                   @PathVariable("userId") Long userId) {
        roleService.deleteRoleFromUser(roleId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{roleId}/users/{userId}")
    public ResponseEntity<Void> addRoleToUser(@PathVariable("roleId") Long roleId,
                                              @PathVariable("userId") Long userId) {
        roleService.addRoleToUser(roleId, userId);
        return ResponseEntity.noContent().build();
    }
}
