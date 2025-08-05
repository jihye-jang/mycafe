import com.cafe.cafesite.entity.Category;
import com.cafe.cafesite.entity.Role;
import com.cafe.cafesite.entity.User;
import com.cafe.cafesite.repository.CategoryRepository;
import com.cafe.cafesite.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // 카테고리 초기 데이터
        if (categoryRepository.count() == 0) {
            createDefaultCategories();
        }
        
        // 관리자 계정 생성
        if (!userRepository.existsByUsername("admin")) {
            createAdminUser();
        }
    }
    
    private void createDefaultCategories() {
        Category[] categories = {
            new Category(null, "자유게시판", "자유롭게 소통하는 공간입니다", 1, null),
            new Category(null, "공지사항", "중요한 공지사항을 확인하세요", 2, null),
            new Category(null, "질문/답변", "궁금한 것을 물어보세요", 3, null),
            new Category(null, "정보공유", "유용한 정보를 공유해주세요", 4, null),
            new Category(null, "취미/여가", "취미와 여가 활동에 대해 이야기해요", 5, null)
        };
        
        for (Category category : categories) {
            categoryRepository.save(category);
        }
    }
    
    private void createAdminUser() {
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setEmail("admin@cafe.com");
        admin.setNickname("관리자");
        admin.setRole(Role.ADMIN);
        
        userRepository.save(admin);
    }
}