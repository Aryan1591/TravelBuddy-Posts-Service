package com.travelbuddy.post.mapper;

import com.travelbuddy.post.entities.DeletedPosts;
import com.travelbuddy.post.entities.InactivePosts;
import com.travelbuddy.post.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommonMapper {

    CommonMapper INSTANCE = Mappers.getMapper(CommonMapper.class);

    DeletedPosts postToDeletedPost(Post post);

    InactivePosts postToInactivePost(Post post);
}
