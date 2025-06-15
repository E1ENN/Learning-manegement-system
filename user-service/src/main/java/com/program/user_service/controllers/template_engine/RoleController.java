package com.program.user_service.controllers.template_engine;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/roles")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleController {

    @NonNull
    private RoleService roleService;

    @GetMapping
    public String getAllRolesPage(Model model) {
        model.addAttribute("roles", roleService.getAllRoles());
        return "all_roles";
    }

    @GetMapping("/{id}")
    public String getRoleById(@PathVariable("id") Long id, Model model) {
        model.addAttribute("role", roleService.getRoleById(id));
        return "role";
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteRoleById(@PathVariable("id") Long id) {
//        roleService.deleteRole(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PostMapping("/create")
//    public ResponseEntity<Role> createRole(@RequestBody RoleCreateRequest request) {
//        return new ResponseEntity<>(roleService.createRole(request.getName()), HttpStatus.CREATED);
//    }
//
//    @PatchMapping("/update/{id}")
//    public ResponseEntity<Role> updateRole(@PathVariable("id") Long id,
//                                           @Valid @RequestBody RoleUpdateRequest request) {
//        return ResponseEntity.ok(roleService.updateRole(id, request.getName()));
//    }
//
//    @DeleteMapping("/{roleId}/users/{userId}")
//    public ResponseEntity<Void> deleteRoleFromUser(@PathVariable("roleId") Long roleId,
//                                                   @PathVariable("userId") Long userId) {
//        roleService.deleteRoleFromUser(roleId, userId);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PostMapping("/{roleId}/users/{userId}")
//    public ResponseEntity<Void> addRoleToUser(@PathVariable("roleId") Long roleId,
//                                              @PathVariable("userId") Long userId) {
//        roleService.addRoleToUser(roleId, userId);
//        return ResponseEntity.noContent().build();
//    }
}
