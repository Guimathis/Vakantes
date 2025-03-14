package com.DevProj.Vakantes.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@SequenceGenerator(name = "contato_id", sequenceName = "vakantes.contato_id_seq", allocationSize = 1, initialValue = 1)
public class Contato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "contato_id")
    private Long id;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "email", unique = true)
    private String email;
}
