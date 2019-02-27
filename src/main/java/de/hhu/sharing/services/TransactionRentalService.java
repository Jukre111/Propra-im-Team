package de.hhu.sharing.services;

import de.hhu.sharing.data.TransactionRentalRepository;
import de.hhu.sharing.model.BorrowingProcess;
import de.hhu.sharing.model.LendableItem;
import de.hhu.sharing.propay.TransactionRental;
import de.hhu.sharing.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class TransactionRentalService {

    @Autowired
    private TransactionRentalRepository transactions;

    @Autowired
    private ProPayService proPayService;

    public TransactionRental getFromProcessId(Long processId){
        return transactions.findByProcessId(processId)
                .orElseThrow(
                    () -> new RuntimeException("LendableItem not found!"));
    }

    public List<TransactionRental> getAllFromSender(User user){
        return transactions.findAllBySender(user);
    }

    public List<TransactionRental> getAllFromReceiver(User user){
        return transactions.findAllByReceiver(user);
    }

    public void createTransactionRental(BorrowingProcess process, User borrower, User lender){
        LendableItem lendableItem = process.getItem();
        int days = (int) DAYS.between(process.getPeriod().getStartdate(),process.getPeriod().getEnddate()) + 1;
        int rent = lendableItem.getRental() * days;
        TransactionRental transRen = new TransactionRental(rent, lendableItem.getDeposit(), process.getId(), lendableItem, borrower, lender);
        proPayService.initiateTransactionRental(transRen);
        transactions.save(transRen);
    }

    public void setDepositRevoked (TransactionRental transRen, String status){
        transRen.setDepositRevoked(status);
        transactions.save(transRen);
    }
}
