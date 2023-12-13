package me.eokasta.appvirtualbank.user;

import jakarta.validation.Valid;
import me.eokasta.appvirtualbank.utils.MessageResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRepository repository;

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

    @GetMapping("/get/{cpf}")
    public ResponseEntity getUser(@PathVariable String cpf) {
        final User user = this.repository.findByCpf(cpf);
        if (user == null)
            return ResponseEntity.status(404).body(new MessageResponseDTO("Usuário não encontrado."));

        final GetUserResponseDTO response = new GetUserResponseDTO(
                user.getId(),
                user.getCpf(),
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt(),
                user.getBalance()
        );
        return ResponseEntity.ok(response);
    }

}
