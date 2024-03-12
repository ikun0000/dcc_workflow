package com.example.dccworkflow.controller;

import com.example.dccworkflow.dto.BTResult;
import com.example.dccworkflow.dto.PermissionDto;
import com.example.dccworkflow.dto.Result;
import com.example.dccworkflow.dto.RoleDto;
import com.example.dccworkflow.enums.ResultType;
import com.example.dccworkflow.exception.RoleNotFoundException;
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
        rolePermissionService.addRole(name);
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
                                       @RequestParam List<Long> permissionSet) throws RoleNotFoundException {
        rolePermissionService.editRolePermission(roleId, permissionSet);
        return new RedirectView("/systemManagement/rolePermissionManagementPage");
    }
}
