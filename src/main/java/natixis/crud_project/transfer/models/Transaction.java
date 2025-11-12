package natixis.crud_project.transfer.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originAccount;
    private String destinationAccount;

    @Column(nullable = false)
    private BigDecimal amount;

    private LocalDate scheduledDate;

    @Column()
    private BigDecimal fee;

    private LocalDateTime createdAt;

    public Transaction() {}
}
