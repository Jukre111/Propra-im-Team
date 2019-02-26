package de.hhu.sharing.services;

import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.BorrowingProcess;
import de.hhu.sharing.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    @Autowired
    private UserRepository users;

    public User get(String username) {
        User user = this.users.findByUsername(username)
                .orElseThrow(
                        () -> new RuntimeException("User not found!"));
        return user;
    }

    public User getBorrowerFromBorrowingProcessId(Long processId) {
        User user = this.users.findByBorrowed_id(processId)
                .orElseThrow(
                        () -> new RuntimeException("User not found!"));
        return user;
    }

    public void removeProcessFromProcessLists(BorrowingProcess process) {
        User lender = process.getItem().getOwner();
        lender.removeFromLend(process);
        users.save(lender);
        User borrower = this.getBorrowerFromBorrowingProcessId(process.getId());
        borrower.removeFromBorrowed(process);
        users.save(borrower);
    }

    public boolean userIsInvolvedToProcess(User user, BorrowingProcess process) {
        User borrower = this.getBorrowerFromBorrowingProcessId(process.getId());
        User lender = process.getItem().getOwner();
        return user == borrower || user == lender;
    }
}
