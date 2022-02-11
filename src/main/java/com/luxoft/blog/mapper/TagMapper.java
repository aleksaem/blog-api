package com.luxoft.blog.mapper;

import com.luxoft.blog.dto.TagWithPostsDto;
import com.luxoft.blog.dto.TagWithoutPostsDto;
import com.luxoft.blog.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TagMapper {

    TagMapper TAG_MAPPER = Mappers.getMapper(TagMapper.class);

    TagWithoutPostsDto tagToTagWithoutPostsDto(Tag tag);

    TagWithPostsDto tagToTagWithPostsDto(Tag tag);

}