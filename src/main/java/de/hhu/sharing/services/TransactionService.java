package de.hhu.sharing.services;

import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.data.RequestRepository;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.Request;
import de.hhu.sharing.propay.Transaction;
import de.hhu.sharing.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class TransactionService {

    @Autowired
    ItemRepository itemRepo;

    @Autowired
    RequestRepository reqRepo;

    @Autowired
    ProPayService proService;

    public int createTransaction(Long requestId, Long itemId) {
        Item item = itemRepo.findById(itemId).get();
        Request request = reqRepo.findById(requestId).get();
        User lender = item.getLender();
        User borrower = request.getRequester();

        long days = DAYS.between(request.getStartdate(),request.getEnddate());
        int rent = item.getRental()*(int) days;

        proService.createAccount(lender.getUsername());
        proService.createAccount(borrower.getUsername());

        Transaction trans = new Transaction(rent, item.getDeposit(), item, lender, borrower);
        int amount = proService.showAccount(borrower.getUsername()).getAmount();
        if(amount >= (rent+item.getDeposit())){
            int responseTransfer = proService.transferMoney(borrower.getUsername(), lender.getUsername(), rent);
            int responseDeposit = proService.createDeposit(borrower.getUsername(), lender.getUsername(), trans);
            //Transaction will be saved in proService due to reasons...

            if(responseDeposit!=200)
                return responseDeposit;
            else if(responseTransfer!=200)
                return responseTransfer;
        } else {
            return -1;
        }
        return 200;
    }
}
