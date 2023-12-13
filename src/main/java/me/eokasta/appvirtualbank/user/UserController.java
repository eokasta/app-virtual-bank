package me.eokasta.appvirtualbank.user;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserRepository repository;

    @Autowired
    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid UserRegisterDTO data) {
        if (this.repository.findByCpf(data.cpf()) != null)
            return ResponseEntity.status(409).body(new MessageResponseDTO("CPF já cadastrado."));

        final User user = data.toUser();
        repository.save(user);

        final RegisterUserResponseDTO response = new RegisterUserResponseDTO(
                user.getId(),
                user.getCpf(),
                user.getFullName(),
                user.getEmail(),
                user.getRole());
        return ResponseEntity.ok(response);
    }

}
