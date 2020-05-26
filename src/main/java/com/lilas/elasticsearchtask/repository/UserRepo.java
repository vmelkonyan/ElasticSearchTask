package com.lilas.elasticsearchtask.repository;

import com.lilas.elasticsearchtask.models.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserRepo extends ElasticsearchRepository<User, String> {
}
