package com.example.jeaauth.service.serviceImpl;

import com.example.jeaauth.dto.UsuarioDto;
import com.example.jeaauth.entity.AuthUser;
import com.example.jeaauth.entity.Usuario;
import com.example.jeaauth.repository.AuthUserRepository;
import com.example.jeaauth.repository.UsuarioRepository;
import com.example.jeaauth.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuthUserRepository authUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
}
