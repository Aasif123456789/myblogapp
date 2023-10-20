package com.myblog.myblog.payload;


import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class PostDto {
    private long id;
@NotEmpty
@Size(min=2,message = "post title should have at least 10 character")
    private String title;

    @NotEmpty
    @Size(min=10,message = "post description should have at least 10 character")
    private String description;

    private String content;
}
