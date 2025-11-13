package natixis.crud_project.transfer.utils;

import natixis.crud_project.transfer.exceptions.BusinessException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TaxCalculatorTest {

    @Test
    void calculatesTaxAWhenScheduledToday() {
        // Regra A: valor até 1000 e agendamento no mesmo dia
        BigDecimal amount = new BigDecimal("500.00");
        LocalDate scheduled = LocalDate.now();

        BigDecimal fee = TaxCalculator.calculateFee(amount, scheduled);

        // 3% de 500 = 15 + 3 fixo = 18.00
        assertEquals(new BigDecimal("18.00"), fee);
    }

    @Test
    void calculatesTaxBForDaysBetween1And10() {
        // Regra B: valor entre 1001 e 2000 e agendado entre 1 e 10 dias
        BigDecimal amount = new BigDecimal("1500.00");
        LocalDate scheduled = LocalDate.now().plusDays(5);

        BigDecimal fee = TaxCalculator.calculateFee(amount, scheduled);

        // 9% de 1500 = 135
        assertEquals(new BigDecimal("135.00"), fee);
    }

    @Test
    void calculatesTaxCForDaysBetween11And20() {
        // Regra C: valor acima de 2000 e agendamento entre 11 e 20 dias
        BigDecimal amount = new BigDecimal("3000.00");
        LocalDate scheduled = LocalDate.now().plusDays(15);

        BigDecimal fee = TaxCalculator.calculateFee(amount, scheduled);

        // 8.2% de 3000 = 246
        assertEquals(new BigDecimal("246.00"), fee);
    }

    @Test
    void throwsErrorWhenScheduledDateIsInThePast() {
        // Agendamento não pode ser no passado
        BigDecimal amount = new BigDecimal("500.00");
        LocalDate scheduled = LocalDate.now().minusDays(1);

        assertThrows(BusinessException.class,
                () -> TaxCalculator.calculateFee(amount, scheduled));
    }

    @Test
    void throwsErrorWhenNoRuleApplies() {
        // Regra A exige o mesmo dia; como esta data é futura, nenhuma regra se aplica
        BigDecimal amount = new BigDecimal("500.00");
        LocalDate scheduled = LocalDate.now().plusDays(2);

        assertThrows(BusinessException.class,
                () -> TaxCalculator.calculateFee(amount, scheduled));
    }
}
