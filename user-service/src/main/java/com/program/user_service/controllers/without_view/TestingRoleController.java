package com.program.user_service.controllers.without_view;

import com.program.user_service.entities.db_entities.Role;
import com.program.user_service.entities.support_entities.request_entities.RoleCreateRequest;
import com.program.user_service.services.RoleService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/non-view/roles")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestingRoleController {

    @NonNull private RoleService roleService;

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Role> createRole(@RequestBody RoleCreateRequest request) {
        return new ResponseEntity<>(roleService.createRole(request.getName()), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteRoleByName(@RequestParam(name = "name") String name) {
        roleService.deleteRoleByName(name);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRoleById(@PathVariable("id") Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{roleId}/users/{userId}")
    public ResponseEntity<Void> addRoleToUser(@PathVariable("roleId") Long roleId,
                                              @PathVariable("userId") Long userId) {
        roleService.addRoleToUser(roleId, userId);
        return ResponseEntity.noContent().build();
    }
}

