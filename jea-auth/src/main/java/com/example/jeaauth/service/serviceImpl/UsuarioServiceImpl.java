package com.example.jeaauth.service.serviceImpl;

import com.example.jeaauth.dto.AccesoDto;
import com.example.jeaauth.dto.UsuarioDto;
import com.example.jeaauth.dto.UsuarioRolDto;
import com.example.jeaauth.entity.*;
import com.example.jeaauth.repository.*;
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

    @Autowired
    private RolRepository rolRepository;


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
        usuario.setEstado(usuarioDto.getEstado() != null ? usuarioDto.getEstado() : true);
        usuario.setAuthUser(authUser); // Relación

        // Guardar Usuario
        usuario = usuarioRepository.save(usuario);

        // Si hay un rolId, asignar el rol al usuario
        if (usuarioDto.getRolId() != null) {
            Rol rol = rolRepository.findById(usuarioDto.getRolId())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

            UsuarioRolPK pk = new UsuarioRolPK();
            pk.setUsuarioId(usuario.getId());
            pk.setRolId(rol.getIdRol());

            UsuarioRol usuarioRol = new UsuarioRol();
            usuarioRol.setId(pk);
            usuarioRol.setUsuario(usuario);
            usuarioRol.setRol(rol);

            usuarioRolRepository.save(usuarioRol);
        }

        return usuario;
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
                .map(a -> new AccesoDto(a.getNombre(), a.getUrl(), a.getIcono(), a.getOrden()))
                .collect(Collectors.toList());
    }

    @Override
    public void asignarRol(UsuarioRolDto dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Rol rol = rolRepository.findById(dto.getRolId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        UsuarioRolPK pk = new UsuarioRolPK();
        pk.setUsuarioId(usuario.getId());
        pk.setRolId(rol.getIdRol());

        UsuarioRol usuarioRol = new UsuarioRol();
        usuarioRol.setId(pk);
        usuarioRol.setUsuario(usuario);
        usuarioRol.setRol(rol);

        usuarioRolRepository.save(usuarioRol);
    }

    @Override
    public List<Usuario> listarPorEstado(boolean estado) {
        return usuarioRepository.findByEstado(estado);
    }


}
