package cat.institutmarianao.gymtony.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "role", visible = true)
@JsonSubTypes({
    @Type(value = Monitor.class, name = Usuario.MONITOR),
    @Type(value = Responsable.class, name = Usuario.RESPONSABLE),
    @Type(value = Cliente.class, name = Usuario.CLIENTE)
})
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "usuarios")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
public abstract class Usuario implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;

    public static final String MONITOR = "monitor";
    public static final String RESPONSABLE = "responsable";
    public static final String CLIENTE = "cliente";

    public enum Role {
        MONITOR, RESPONSABLE, CLIENTE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 25)
    @NotBlank(message = "El usuario no puede estar vacío")
    @Size(min = 2, max = 25, message = "El usuario debe tener entre 2 y 25 caracteres")
    @NonNull
    @EqualsAndHashCode.Include
    private String username;

    @JsonInclude(Include.NON_NULL)
    @Column(nullable = false)
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 10, message = "La contraseña debe tener al menos 10 caracteres")
    private String password;

    @Column(nullable = false)
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, message = "El nombre debe tener al menos 2 caracteres")
    @NonNull
    private String name;

    @Column(unique = true, nullable = false, length = 9)
    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{8}[A-Za-z]", message = "El DNI debe tener 8 números y una letra")
    @NonNull
    private String dni;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ser un correo válido")
    @NonNull
    private String email;

    @Column(nullable = false)
    @Min(value = 16, message = "Debes tener al menos 16 años")
    @Max(value = 100, message = "Edad no válida")
    private int age;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", insertable = false, updatable = false)
    private Role role = Role.CLIENTE; // ✅ Cliente por defecto

    public Usuario(String username, String password, String name, String dni, String email, int age, Role role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.dni = dni;
        this.email = email;
        this.age = age;
        this.role = role != null ? role : Role.CLIENTE; // Evita valores nulos
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
