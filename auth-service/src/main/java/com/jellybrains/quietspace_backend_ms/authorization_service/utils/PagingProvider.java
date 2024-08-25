package com.jellybrains.quietspace_backend_ms.authorization_service.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PagingProvider {
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;
    public static final Sort DEFAULT_SORT_OPTION = Sort.by("createDate").descending();
    public static final Sort BY_CREATED_DATE_ASC = Sort.by("createDate").ascending();

    public static PageRequest buildPageRequest(Integer pageNumber, Integer pageSize, Sort sortOption) {
        int queryPageNumber;
        int queryPageSize;
        Sort querySortOption;

        if (pageNumber != null && pageNumber > 0) queryPageNumber = pageNumber - 1;
        else queryPageNumber = DEFAULT_PAGE;

        if (pageSize == null) queryPageSize = DEFAULT_PAGE_SIZE;
        else {
            if (pageSize > 1000) queryPageSize = 1000;
            else queryPageSize = pageSize;
        }

        if (sortOption == null) querySortOption = DEFAULT_SORT_OPTION;
        else querySortOption = sortOption;

        return PageRequest.of(queryPageNumber, queryPageSize, querySortOption);
    }
}
