package natixis.crud_project.transfer.utils;

import natixis.crud_project.transfer.exceptions.BusinessException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public final class TaxCalculator {

    private TaxCalculator() {}

    /**
     * Calculate fee according to the rules:
     * - Tax A: amount <= 1000 and scheduledDate == today -> 3% of amount + 3€
     * - Tax B: amount > 1000 and amount <= 2000 and days between scheduledDate and today in [1..10] -> 9% of amount
     * - Tax C: amount > 2000:
     *     11..20 days -> 8.2%
     *     21..30 days -> 6.9%
     *     31..40 days -> 4.7%
     *     >40 days -> 1.7%
     *
     * If no rule applies -> throws BusinessException
     */
    public static BigDecimal calculateFee(BigDecimal amount, LocalDate scheduledDate) {
        Objects.requireNonNull(amount, "amount is required");
        Objects.requireNonNull(scheduledDate, "scheduledDate is required");

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Amount must be greater than 0");
        }

        LocalDate today = LocalDate.now();
        long days = ChronoUnit.DAYS.between(today, scheduledDate);

        if (scheduledDate.isBefore(today)) {
            throw new BusinessException("Scheduled date cannot be in the past");
        }

        /*Taxa A (valor da transferencia entre 0€ e 1000€)
        *- Data do Agendamento igual Data Atual - 3% do valor da transação + 3€
        */
        if (amount.compareTo(new BigDecimal("1000.00")) <= 0) {
            if (days == 0) {
                BigDecimal fee = amount.multiply(new BigDecimal("0.03")).add(new BigDecimal("3.00"));
                return fee.setScale(2, RoundingMode.HALF_UP);
            } else {
                throw new BusinessException("No tax rule applies for amount <= 1000 when scheduled date is not today");
            }
        }

        /*Taxa B (valor da transferencia entre 1001€ e 2000€)
        *- Data de agendamento entre 1 e 10 dias da data atual - 9%
        */
        if (amount.compareTo(new BigDecimal("1000.00")) > 0 && amount.compareTo(new BigDecimal("2000.00")) <= 0) {
            if (days >= 1 && days <= 10) {
                BigDecimal fee = amount.multiply(new BigDecimal("0.09"));
                return fee.setScale(2, RoundingMode.HALF_UP);
            } else {
                throw new BusinessException("No tax rule applies for amount in (1000,2000] with the given scheduled date");
            }
        }

        /*Taxa C (valor da transferencia maior que 2000€)
        *- Data de agendamento entre 11 e 20 dias da data atual - 8.2% do valor da transação
        *        - Data de agendamento entre 21 e 30 dias da data atual - 6.9% do valor da transação
        *        - Data de agendamento entre 31 e 40 dias da data atual - 4.7% do valor da transação
        *        - Data de agendamento maior que 40 dias da data atual - 1.7% do valor da transação
        */
        if (amount.compareTo(new BigDecimal("2000.00")) > 0) {
            if (days >= 11 && days <= 20) {
                return amount.multiply(new BigDecimal("0.082")).setScale(2, RoundingMode.HALF_UP);
            } else if (days >= 21 && days <= 30) {
                return amount.multiply(new BigDecimal("0.069")).setScale(2, RoundingMode.HALF_UP);
            } else if (days >= 31 && days <= 40) {
                return amount.multiply(new BigDecimal("0.047")).setScale(2, RoundingMode.HALF_UP);
            } else if (days > 40) {
                return amount.multiply(new BigDecimal("0.017")).setScale(2, RoundingMode.HALF_UP);
            } else {
                throw new BusinessException("No tax rule applies for amount > 2000 with the given scheduled date");
            }
        }

        throw new BusinessException("No tax rule applies for the given amount/date combination");
    }
}
