package com.dw.communityWeb.infrastructure;

import com.dw.communityWeb.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT COUNT(*) FROM User WHERE id = :id")
    Long checkId(String id);  // id 존재 여부를 반환하는 메소드

    @Query(value = "SELECT u FROM User u WHERE u.name = :name AND u.email = :email")
    User findByInfo(String name, String email);

    @Query(value = "SELECT u FROM User u WHERE u.id = :id AND u.email = :email")
    User findPwd(String id, String email);

    @Query(value = "SELECT u FROM User u WHERE u.id = :id")
    User findUserById(String id);

    @Modifying
    @Query(value = "UPDATE User u set u.pwd = :pwd WHERE u.id = :id")
    void updatePwd(String id, String pwd);
}
