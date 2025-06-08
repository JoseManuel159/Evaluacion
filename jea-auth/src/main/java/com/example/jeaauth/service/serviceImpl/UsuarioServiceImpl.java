package com.example.jeaauth.service.serviceImpl;

import com.example.jeaauth.dto.AccesoDto;
import com.example.jeaauth.dto.UsuarioDto;
import com.example.jeaauth.entity.*;
import com.example.jeaauth.repository.AccesoRolRepository;
import com.example.jeaauth.repository.AuthUserRepository;
import com.example.jeaauth.repository.UsuarioRepository;
import com.example.jeaauth.repository.UsuarioRolRepository;
import com.example.jeaauth.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuthUserRepository authUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRolRepository usuarioRolRepository;

    @Autowired
    private AccesoRolRepository accesoRolRepository;

    @Override
    public Usuario save(UsuarioDto usuarioDto) {
        // Verifica si el username ya existe
        Optional<AuthUser> existingUser = authUserRepository.findByUserName(usuarioDto.getUserName());
        if (existingUser.isPresent()) {
            return null; // O lanzar excepción personalizada
        }

        // Crear las credenciales (AuthUser)
        AuthUser authUser = new AuthUser();
        authUser.setUserName(usuarioDto.getUserName());
        authUser.setPassword(passwordEncoder.encode(usuarioDto.getPassword()));

        // Guardar AuthUser primero
        authUser = authUserRepository.save(authUser);

        // Crear Usuario
        Usuario usuario = new Usuario();
        usuario.setNombres(usuarioDto.getNombres());
        usuario.setApellidoPaterno(usuarioDto.getApellidoPaterno());
        usuario.setApellidoMaterno(usuarioDto.getApellidoMaterno());
        usuario.setDni(usuarioDto.getDni());
        usuario.setDireccion(usuarioDto.getDireccion());
        usuario.setTelefono(usuarioDto.getTelefono());
        usuario.setEstado(usuarioDto.getEstado());
        usuario.setAuthUser(authUser); // Relación

        // Guardar Usuario
        return usuarioRepository.save(usuario);
    }

    public List<AccesoDto> obtenerAccesosPorUsuario(Long usuarioId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (!usuarioOpt.isPresent()) {
            return Collections.emptyList();
        }
        Usuario usuario = usuarioOpt.get();

        // Obtener roles del usuario
        List<UsuarioRol> usuarioRoles = usuarioRolRepository.findByUsuario(usuario);

        // Obtener accesos desde roles
        Set<Acceso> accesosSet = new HashSet<>();
        for (UsuarioRol ur : usuarioRoles) {
            List<AccesoRol> accesoRoles = accesoRolRepository.findByRol(ur.getRol());
            for (AccesoRol ar : accesoRoles) {
                accesosSet.add(ar.getAcceso());
            }
        }

        // Convertir a DTO
        return accesosSet.stream()
                .map(a -> new AccesoDto(a.getNombre(), a.getUrl(), a.getIcono()))
                .collect(Collectors.toList());
    }
}
