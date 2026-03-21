package com.example.stockpos.app.models;

import com.example.stockpos.app.models.base.BaseModel;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;


@Entity
@Table(name = "categories")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_deleted = false")
public class Category extends BaseModel {

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Builder.Default
    private Boolean status = true;
}