package de.hhu.sharing.data;

import de.hhu.sharing.model.*;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SellableItemRepositoryTest {

    @Autowired
    SellableItemRepository itemRepo;

    @Autowired
    UserRepository userRepo;

    public User createUser(String username){
        LocalDate birthdate = LocalDate.of(2000,1,1);
        Address address = new Address("unistrase","duesseldorf", 40233);
        User user = new User(username,"password", "role", "lastnmae", "forname", "email",birthdate,address);
        userRepo.save(user);
        return userRepo.findByUsername(username).get();
    }


    public ArrayList<SellableItem> createItems(){
        User user = createUser("testman");
        SellableItem sellableItem1 = new SellableItem("apfel", "lecker",1, user);
        SellableItem sellableItem2 = new SellableItem("granatapfel", "grosse Kerne",2, user );
        SellableItem sellableItem3 = new SellableItem("banane", "kleine Kerne",3,user );
        ArrayList<SellableItem> sellableItems = new ArrayList<>();
        sellableItems.add(sellableItem1);
        sellableItems.add(sellableItem2);
        sellableItems.add(sellableItem3);
        return sellableItems;
    }


    @Test
    public void testFindAll() {
        ArrayList<SellableItem> sellableItems = createItems();
        itemRepo.save(sellableItems.get(0));
        itemRepo.save(sellableItems.get(1));
        Assertions.assertThat(itemRepo.findAll().size()).isEqualTo(2);
    }

    @Test
    public void testFindAllByOwner(){
        ArrayList<SellableItem> sellableItems = createItems();
        itemRepo.save(sellableItems.get(0));
        itemRepo.save(sellableItems.get(1));

        List<SellableItem> sellableItemList = itemRepo.findAllByOwner(sellableItems.get(0).getOwner());
        Assertions.assertThat(sellableItemList.size()).isEqualTo(2);
        Assertions.assertThat(sellableItemList.get(0).getOwner().getUsername()).isEqualTo("testman");

    }

    @Test
    public void  testFindAllByNameContainingOrDescriptionContaining(){
        ArrayList<SellableItem> sellableItems = createItems();
        itemRepo.save(sellableItems.get(0));
        itemRepo.save(sellableItems.get(1));
        itemRepo.save(sellableItems.get(2));

        List<SellableItem> sellableItemList1 = itemRepo.findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("apf", "egal");
        List<SellableItem> sellableItemList2 = itemRepo.findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("banane", "Kern");

        Assertions.assertThat(sellableItemList1.size()).isEqualTo(2);
        Assertions.assertThat(sellableItemList2.size()).isEqualTo(2);

    }
}
