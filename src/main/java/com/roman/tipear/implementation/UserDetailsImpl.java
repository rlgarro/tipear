package com.roman.tipear.implementation;

        import com.roman.tipear.model.entity.UserModel;
        import com.roman.tipear.repository.UserRepository;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.security.core.authority.SimpleGrantedAuthority;
        import org.springframework.security.core.userdetails.User;
        import org.springframework.security.core.userdetails.UserDetails;
        import org.springframework.security.core.userdetails.UserDetailsService;
        import org.springframework.security.core.userdetails.UsernameNotFoundException;
        import org.springframework.stereotype.Service;


@Service
public class UserDetailsImpl implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // implementation to let the user log in by username or email
        UserModel user = new UserModel();
        Boolean foundByUsername = repository.findByUsername(username) != null;
        Boolean foundByEmail = repository.findByEmail(username) != null;

        if (foundByUsername && foundByEmail) {
            user = repository.findByUsername(username);
        } else if (foundByUsername == false && foundByEmail == true) {
            user = repository.findByEmail(username);
        } else if (foundByUsername == true && foundByEmail == false) {
            user = repository.findByUsername(username);
        } else {
            throw new UsernameNotFoundException("Username or email not found");
        }

        User.UserBuilder builder = User.withUsername(user.getUsername());
        Boolean userEnabled = user.userIsActive();
        builder.disabled(!userEnabled);
        builder.password(user.getPassword());
        builder.authorities(new SimpleGrantedAuthority("ROLE_USER"));

        return builder.build();
    }
}