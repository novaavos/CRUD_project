package natixis.crud_project.transfer.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

    private Long id;
    private String originAccount;
    private String destinationAccount;
    private BigDecimal amount;
    private LocalDate scheduledDate;
    private BigDecimal fee;
    private LocalDateTime createdAt;
}
