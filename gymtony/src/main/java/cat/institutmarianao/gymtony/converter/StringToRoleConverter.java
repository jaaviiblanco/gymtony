package cat.institutmarianao.gymtony.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cat.institutmarianao.gymtony.model.Usuario;

@Component
public class StringToRoleConverter implements Converter<String, Usuario.Role> {

    @Override
    public Usuario.Role convert(String source) {
        try {
            return Usuario.Role.valueOf(source.toLowerCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}

