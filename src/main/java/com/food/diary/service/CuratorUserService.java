package com.food.diary.service;

import com.food.diary.response.user.UserListResponse;

public interface CuratorUserService {
    UserListResponse getAllUsersByCurator(final Long id, int pageNumber, int pageSize, final String data);
    void addUserToCurator(final Long curatorId, final Long userId);
    void removeUserFromCuratorUserList(final Long curatorId, final Long userId);
}
