package cat.institutmarianao.gymtony.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("cliente")
public class Cliente extends Usuario {

    private static final long serialVersionUID = 1L;

    public Cliente() {
        super();
    }

    public Cliente(String username, String passwd, String name, String dni, String email, int age) {
        super(username, passwd, name, dni, email, age, Role.cliente);
    }
}

