package fr.cpe.filmforyou.userlib.webservice;

import fr.cpe.filmforyou.userlib.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "users")
public interface UserWebService {

    @PostMapping("/private/auth/user")
    UserDTO getUser(String token);

    @PostMapping(value = "/private/token/valid")
    Boolean isTokenValid(String token);

}
