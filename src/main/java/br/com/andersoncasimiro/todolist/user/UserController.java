package br.com.andersoncasimiro.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * Tipos Modificador:
 * public - essa classe permite o acesso publico
 * private - essa classe não permite o acesso publico
 * protected - pacote de restrições.
 */

@RestController
@RequestMapping("/users")
public class UserController {
    
    /**
     * String (texto)
     * Integer (int) numeros inteiros
     * Double (double) numeros decimais : 0.0000
     * Float (float) numeros decimais c/ qty limitada:  0.000
     * Char (caracteres)
     * Date(data)
     * void - sem retorno
     */

    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel) {
        var user = this.userRepository.findByUsername(userModel.getUsername());

        if(user != null) {
            //vai retornar mensagem de erro
            // status code
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe");
        }

       var passwordHashed = BCrypt.withDefaults()
                .hashToString(12, userModel.getPassword().toCharArray());

       userModel.setPassword(passwordHashed);

        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.OK).body(userCreated);
    }
}
