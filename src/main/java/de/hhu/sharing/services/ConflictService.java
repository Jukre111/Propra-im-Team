package de.hhu.sharing.services;

import de.hhu.sharing.data.ConflictRepository;
import de.hhu.sharing.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConflictService {

    @Autowired
    private ConflictRepository conflicts;

    public Conflict get(Long id){
        return this.conflicts.findById(id)
                .orElseThrow(
                        ()-> new NotFoundException("Konflikt nicht gefunden!"));
    }

    public Conflict getFromBorrowingProcess(BorrowingProcess process) {
        return this.conflicts.findByProcess(process);
    }

    public List<Conflict> getAll() {
        return conflicts.findAll();
    }

    public void create(User lender, User borrower, BorrowingProcess process, User prosecuter, String problem) {
        Conflict conflict = new Conflict(lender, borrower, process, new Message(prosecuter.getUsername(), problem));
        conflicts.save(conflict);
    }

    public void delete(Conflict conflict){
        conflicts.delete(conflict);
    }

    public boolean noConflictWith(LendableItem lendableItem) {
        return conflicts.findAllByProcess_Item(lendableItem).isEmpty();
    }

    public void addToMessages(Conflict conflict, User user, String message) {
        conflict.addToMessages(new Message(user.getUsername(), message));
        conflicts.save(conflict);
    }
}