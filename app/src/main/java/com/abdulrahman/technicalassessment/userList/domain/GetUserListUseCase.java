package com.abdulrahman.technicalassessment.userList.domain;

import com.abdulrahman.technicalassessment.userList.data.remote.UserListDataSource;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetUserListUseCase {

    private final UserListDataSource dataSource;

    @Inject
    public GetUserListUseCase(UserListDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Single<List<User>> execute() {
        return dataSource.fetchUsers()
                .map(userResponse -> userResponse.data.stream()
                        .map(
                                user -> new User(
                                        user.id,
                                        user.name,
                                        user.email,
                                        user.gender,
                                        user.status
                                )
                        ).collect(Collectors.toList())
                );
    }

}
