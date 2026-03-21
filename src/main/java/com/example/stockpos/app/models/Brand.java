package com.example.stockpos.app.models;

import com.example.stockpos.app.models.base.BaseModel;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;


@Entity
@Table(name = "brands")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@SQLRestriction("is_deleted = false")
public class Brand extends BaseModel {

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String slug;

    @Column(length = 255)
    private String description;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Builder.Default
    private Boolean status = true;
}
