package com.luxoft.blog.mapper;

import com.luxoft.blog.dto.TagWithPostsDto;
import com.luxoft.blog.dto.TagWithoutPostsDto;
import com.luxoft.blog.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper(uses = TagMapper.class)
public interface TagListMapper {

    TagListMapper TAG_LIST_MAPPER = Mappers.getMapper(TagListMapper.class);

    Set<TagWithoutPostsDto> tagsToTagWithoutPostDtos(Set<Tag> tags);

    Set<TagWithPostsDto> tagsToTagWithPostsDtos(Set<Tag> tags);
}
