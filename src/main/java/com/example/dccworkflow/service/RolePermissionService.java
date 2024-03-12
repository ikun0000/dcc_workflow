package com.example.dccworkflow.service;

import com.example.dccworkflow.dto.PermissionDto;
import com.example.dccworkflow.dto.RoleDto;
import com.example.dccworkflow.entity.Permission;
import com.example.dccworkflow.entity.QRole;
import com.example.dccworkflow.entity.Role;
import com.example.dccworkflow.exception.RoleNotFoundException;
import com.example.dccworkflow.repository.PermissionRepository;
import com.example.dccworkflow.repository.RoleRepository;
import com.example.dccworkflow.utils.LikeWrap;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RolePermissionService {
    private RoleRepository roleRepository;
    private PermissionRepository permissionRepository;

    public RolePermissionService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Transactional(readOnly = true)
    public Page<RoleDto> getRoleDto(String search, Pageable pageable) {
        QRole qRole = QRole.role;

        BooleanBuilder whereCase = new BooleanBuilder();
        whereCase.or(qRole.name.like(LikeWrap.like(search)));

        Page<Role> rolePage = roleRepository.findAll(whereCase, pageable);

        List<RoleDto> content = rolePage.getContent()
                .stream().map(role -> {
                    RoleDto dto = new RoleDto();
                    BeanUtils.copyProperties(role, dto);
                    return dto;
                })
                .toList();

        return new PageImpl<>(content, pageable, rolePage.getTotalElements());
    }

    @Transactional
    public void removeRole(Long roleId) {
        roleRepository.deleteById(roleId);
    }

    @Transactional
    public Role addRole(String name) {
        Role role = new Role();
        role.setName(name);
        role = roleRepository.save(role);

        // 添加默认权限
        Permission permission = new Permission();
        permission.setId(1L);
        Set<Permission> permissionSet = new HashSet<>();
        permissionSet.add(permission);
        role.setPermissions(permissionSet);

        return role;
    }

    @Transactional(readOnly = true)
    public List<Permission> getAllPermission() {
        return permissionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<PermissionDto> getRoleHasPermissionDto(Long roleId) throws RoleNotFoundException {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(RoleNotFoundException::new);

        return role.getPermissions().stream().map(permission -> {
            PermissionDto dto = new PermissionDto();
            BeanUtils.copyProperties(permission, dto);
            return dto;
        }).toList();
    }

    @Transactional
    public Role editRolePermission(Long roleId, List<Long> permissionIds) throws RoleNotFoundException {
        List<Permission> permissionList = permissionRepository.findAllById(permissionIds);

        Role role = roleRepository.findById(roleId)
                .orElseThrow(RoleNotFoundException::new);
        role.setPermissions(permissionList.stream().collect(Collectors.toSet()));

        return role;
    }
}
