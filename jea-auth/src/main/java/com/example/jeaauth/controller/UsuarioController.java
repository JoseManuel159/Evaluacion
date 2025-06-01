package com.example.jeaauth.controller;

import com.example.jeaauth.dto.UsuarioDto;
import com.example.jeaauth.entity.Usuario;
import com.example.jeaauth.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/crear")
    public ResponseEntity<Usuario> crearUsuario(@RequestBody UsuarioDto usuarioDto) {
        Usuario usuario = usuarioService.save(usuarioDto);
        if (usuario == null) {
            return ResponseEntity.badRequest().build(); // Ya existe el username, por ejemplo
        }
        return ResponseEntity.ok(usuario);
    }
}
