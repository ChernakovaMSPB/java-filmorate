package rru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("mpa")
public class Mpa {
    @Column("rating_id")
    int id;
    String name;

    public Mpa(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Mpa(int id) {
        this.id = id;
    }
}
