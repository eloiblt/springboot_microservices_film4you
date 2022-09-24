package fr.cpe.filmforyou.preferencecore.bo;


import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "preference", schema = "public")
public class Preference {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "film_id")
    private String filmId;

    @Column(name = "mark")
    private BigDecimal mark;

}
