package com.food.diary.repository;

import com.food.diary.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query(value = "select * from users where username LIKE %:data% limit :pageSize offset :offset",
    nativeQuery = true)
    List<UserEntity> getAllUsers(final int pageSize, final int offset, final String data);

    @Query(value = "select * from users u where u.role_name =:roleName and u.username LIKE %:data% limit :pageSize offset :offset",
    nativeQuery = true)
    List<UserEntity> getAllUserByRole(final String roleName,final int pageSize, final int offset, final String data);

    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query(value = "select * from users u where u.id NOT IN (select user_id from curator_user where curator_id =:curatorId) and u.role_name = 'ROLE_USER' and u.username LIKE %:data% limit :pageSize offset :offset",
    nativeQuery = true)
    List<UserEntity> getAllUsersByNotInCurator(Long curatorId, final int pageSize, final int offset, final String data);
}
