package com.example.dccworkflow.controller;

import com.example.dccworkflow.dto.*;
import com.example.dccworkflow.enums.ResultType;
import com.example.dccworkflow.exception.RoleNotFoundException;
import com.example.dccworkflow.exception.UserNotFoundException;
import com.example.dccworkflow.service.RolePermissionService;
import com.example.dccworkflow.service.UserService;
import org.activiti.engine.RepositoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/systemManagement")
public class SystemManagementController {
    private RepositoryService repositoryService;
    private UserService userService;
    private RolePermissionService rolePermissionService;

    public SystemManagementController(RepositoryService repositoryService, UserService userService, RolePermissionService rolePermissionService) {
        this.repositoryService = repositoryService;
        this.userService = userService;
        this.rolePermissionService = rolePermissionService;
    }

    @GetMapping("/rolePermissionManagementPage")
    public String rolePermissionManagementPage(Model model) {
        model.addAttribute("permissionList", rolePermissionService.getAllPermission());
        return "systemManagement/rolePermissionManagement";
    }

    @GetMapping("/roleList.bt")
    @ResponseBody
    public Result<BTResult<RoleDto>> roleListBT(@RequestParam String search,
                                                @RequestParam(defaultValue = "1") Integer page,
                                                @RequestParam(defaultValue = "10") Integer size) {
        Page<RoleDto> roleDtos = rolePermissionService.getRoleDto(search,
                PageRequest.of(page - 1, size,
                        Sort.by(Sort.Direction.DESC, "id")));

        return Result.of(ResultType.SUCCESS,
                BTResult.of(roleDtos.getContent(), roleDtos.getTotalElements()));
    }

    @PostMapping("/removeRole")
    @ResponseBody
    public Result<Object> removeRole(@RequestParam Long id) {
        try {
            rolePermissionService.removeRole(id);
        } catch (Exception e) {
            return Result.of(ResultType.ROLE_REF, null);
        }
        return Result.of(ResultType.SUCCESS, null);
    }

    @PostMapping("/addRole")
    public RedirectView addRole(@RequestParam String name) {
        try {
            rolePermissionService.addRole(name);
        } catch (Exception e) {
            return new RedirectView("/systemManagement/rolePermissionManagementPage?addFail");
        }
        return new RedirectView("/systemManagement/rolePermissionManagementPage");
    }


    @GetMapping("/roleHasPermission.json")
    @ResponseBody
    public Result<List<PermissionDto>> roleHasPermissionJSON(@RequestParam Long roleId) throws RoleNotFoundException {
        List<PermissionDto> roleHasPermissionDto = rolePermissionService.getRoleHasPermissionDto(roleId);
        return Result.of(ResultType.SUCCESS, roleHasPermissionDto);
    }

    @PostMapping("/editPermission")
    public RedirectView editPermission(@RequestParam Long roleId,
                                       @RequestParam(required = false) List<Long> permissionSet) throws RoleNotFoundException {
        if (permissionSet == null) {
            permissionSet = new ArrayList<>();
        }
        rolePermissionService.editRolePermission(roleId, permissionSet);
        return new RedirectView("/systemManagement/rolePermissionManagementPage");
    }

    @GetMapping("/userManagementPage")
    public String userManagementPage(Model model) {
        model.addAttribute("roleList", rolePermissionService.getAllRole());
        return "systemManagement/userManagementPage";
    }

    @GetMapping("/userList.bt")
    @ResponseBody
    public Result<BTResult<UserDto>> userListBT(@RequestParam String search,
                                                @RequestParam(defaultValue = "1") Integer page,
                                                @RequestParam(defaultValue = "10") Integer size) {
        Page<UserDto> userDtos = userService.getUserDto(search,
                PageRequest.of(page - 1, size,
                        Sort.by(Sort.Direction.DESC, "id")));

        return Result.of(ResultType.SUCCESS,
                BTResult.of(userDtos.getContent(), userDtos.getTotalElements()));
    }

    @PostMapping("/addUser")
    public RedirectView addUser(@RequestParam String username,
                                String password,
                                String email,
                                String phone,
                                @RequestParam(defaultValue = "false") Boolean enable) {
        try {
            userService.addUser(username.trim(),
                    password.trim(),
                    email,
                    phone,
                    enable);
        } catch (Exception e) {
            return new RedirectView("/systemManagement/userManagementPage?addFail");
        }
        return new RedirectView("/systemManagement/userManagementPage");
    }

    @PostMapping("/updateUser")
    public RedirectView updateUser(@RequestParam Long userId,
                                   @RequestParam String username,
                                   String password,
                                   String email,
                                   String phone,
                                   @RequestParam(defaultValue = "false") Boolean enable) throws UserNotFoundException {
        userService.updateUser(userId,
                username.trim(),
                password.trim(),
                email,
                phone,
                enable);
        return new RedirectView("/systemManagement/userManagementPage");
    }

    @PostMapping("/removeUser")
    @ResponseBody
    public Result<Object> removeUser(@RequestParam Long id) {
        try {
            userService.removeUser(id);
        } catch (Exception e) {
            return Result.of(ResultType.USER_REF, null);
        }
        return Result.of(ResultType.SUCCESS, null);
    }

    @PostMapping("/editUserRole")
    public RedirectView editUserRole(@RequestParam Long userId,
                                     @RequestParam(required = false) List<Long> roleSet) throws UserNotFoundException {
        if (roleSet == null) {
            roleSet = new ArrayList<>();
        }
        userService.editUserRole(userId, roleSet);
        return new RedirectView("/systemManagement/userManagementPage");
    }

    @GetMapping("/userHasRole.json")
    @ResponseBody
    public Result<List<RoleDto>> userHasRoleJSON(@RequestParam Long userId) throws UserNotFoundException {
        List<RoleDto> roleDtos = userService.getUserHasRoleDto(userId);
        return Result.of(ResultType.SUCCESS, roleDtos);
    }
}
