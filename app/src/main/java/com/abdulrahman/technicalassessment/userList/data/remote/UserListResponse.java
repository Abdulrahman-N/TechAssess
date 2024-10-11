package com.abdulrahman.technicalassessment.userList.data.remote;

import java.util.List;

// Root response object
public class UserListResponse {
    public int code;
    public Meta meta;
    public List<User> data;
}

// Meta information containing pagination details
class Meta {
    public Pagination pagination;
}

// Pagination details
class Pagination {
    public int total;
    public int pages;
    public int page;
    public int limit;
}

