package com.jabulani.ligiopen.entity.location;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "counties")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class County {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "region")
    private String region;

    @OneToMany(mappedBy = "county")
    private List<Stadium> stadiums = new ArrayList<>();
}