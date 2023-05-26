package com.food.diary.repository;

import com.food.diary.entity.CuratorUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CuratorUserRepository extends JpaRepository<CuratorUserEntity, Long> {

    @Query(value = "select * from curator_user cu where  cu.curator_id =:curatorId and cu.user_id IN (select id from users u where u.username LIKE %:data% and u.role_name='ROLE_USER') limit :pageSize offset :offset",
    nativeQuery = true)
    List<CuratorUserEntity> getAllUsersByCurator(final Long curatorId, final int pageSize, final int offset, final String data);

    @Query(value = "select * from curator_user where curator_id =:curatorId and user_id =:userId",
    nativeQuery = true)
    Optional<CuratorUserEntity> isUserInCuratorUserList(final Long curatorId, final Long userId);
}
