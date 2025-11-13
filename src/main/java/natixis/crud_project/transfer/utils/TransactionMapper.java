package natixis.crud_project.transfer.utils;

import natixis.crud_project.transfer.dto.TransactionRequest;
import natixis.crud_project.transfer.dto.TransactionResponse;
import natixis.crud_project.transfer.models.Transaction;

public final class TransactionMapper {

    private TransactionMapper() {}


    public static Transaction toEntity(TransactionRequest dto) {
        Transaction t = new Transaction();
        t.setOriginAccount(dto.getOriginAccount());
        t.setDestinationAccount(dto.getDestinationAccount());
        t.setAmount(dto.getAmount());
        t.setScheduledDate(dto.getScheduledDate());
        return t;
    }


    public static TransactionResponse toResponse(Transaction t) {
        TransactionResponse dto = new TransactionResponse();
        dto.setId(t.getId());
        dto.setOriginAccount(t.getOriginAccount());
        dto.setDestinationAccount(t.getDestinationAccount());
        dto.setAmount(t.getAmount());
        dto.setScheduledDate(t.getScheduledDate());
        dto.setFee(t.getFee());
        dto.setCreatedAt(t.getCreatedAt());
        return dto;
    }
}
