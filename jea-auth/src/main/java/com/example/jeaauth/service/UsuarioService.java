package com.example.jeaauth.service;

import com.example.jeaauth.dto.UsuarioDto;
import com.example.jeaauth.entity.Usuario;

public interface UsuarioService {
    Usuario save(UsuarioDto usuarioDto);
}
