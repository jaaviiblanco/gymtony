package cat.institutmarianao.gymtony.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("responsable")
public class Responsable extends Monitor {

    private static final long serialVersionUID = 1L;

    public Responsable() {
        super();
    }

    public Responsable(String username, String passwd, String name, String dni, String email, int age) {
        super(username, passwd, name, dni, email, age);
    }
}

