package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ITEM_SEQ")
    @SequenceGenerator(name="ITEM_SEQ", sequenceName = "ITEM_SEQ", allocationSize = 1)

    private Long id;
    @Column(name= "FIRST_NAME")
    private String firstName;

    @Column(name= "LAST_NAME")
    private String lastName;


    @NaturalId(mutable = true)
    @Column(name= "EMAIL")
    private String email;

    @Column(name= "PRODUCT_NAME")
    private String productName;


}
