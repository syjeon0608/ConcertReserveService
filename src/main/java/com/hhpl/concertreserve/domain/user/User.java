package com.hhpl.concertreserve.domain.user;

import jakarta.persistence.*;

@Entity
@Table(name = "`user`")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String uuid;
}
