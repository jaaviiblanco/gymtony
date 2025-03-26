package cat.institutmarianao.gymtony.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("monitor")
public class Monitor extends Usuario {

    private static final long serialVersionUID = 1L;

    public Monitor() {
        super();
    }

    public Monitor(String username, String passwd, String name, String dni, String email, int age) {
        super(username, passwd, name, dni, email, age, Role.MONITOR);
    }
}

