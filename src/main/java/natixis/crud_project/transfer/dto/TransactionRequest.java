package natixis.crud_project.transfer.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

    @NotBlank(message = "originAccount is required")
    private String originAccount;

    @NotBlank(message = "destinationAccount is required")
    private String destinationAccount;

    @NotNull(message = "amount is required")
    @Positive(message = "amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "scheduledDate is required")
    private LocalDate scheduledDate;
}
