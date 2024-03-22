package vn.com.gsoft.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import vn.com.gsoft.security.entity.UserProfile;
import vn.com.gsoft.security.repository.UserProfileRepository;

import java.util.Optional;

@SpringBootApplication
@EnableTransactionManagement
public class SecurityApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }
    @Autowired
    UserProfileRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Khi chương trình chạy
        // Insert vào csdl một user.
        Optional<UserProfile> userProfile = userRepository.findByUserName("webnt.Tung.GD");
        if(userProfile.isPresent()){
            userProfile.get().setPassword(passwordEncoder.encode("loda"));
            userRepository.save(userProfile.get());
        }
    }
}
