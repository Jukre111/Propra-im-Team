package de.hhu.sharing.RepositoryTests;

import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.Address;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.User;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepo;
    @Autowired
    UserRepository userRepo;



    public ArrayList<Item> createItems(){
        LocalDate date = LocalDate.of(2000,1,1);
        Address address = new Address("unistrase","duesseldorf", 40233);
        User user = new User("user","password", "role", "lastnmae", "forname", "email",date,address);
        userRepo.save(user);
        User userEntity = userRepo.findByUsername("user").get();
        userRepo.save(userEntity);
        Item item1 = new Item("apfel", "lecker",1,1 ,user );
        Item item2 = new Item("granatapfel", "grosse Kerne",2,2 ,user );
        Item item3 = new Item("banane", "kleine Kerne",3,3 ,user );
        ArrayList<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        items.add(item3);
        return items;
    }


    @Test
    public void testFindAll() {
        ArrayList<Item> items = createItems();
        itemRepo.save(items.get(0));
        itemRepo.save(items.get(1));
        Assertions.assertThat(itemRepo.findAll().size()).isEqualTo(2);
    }

    @Test
    public void testFindById(){
        ArrayList<Item> items = createItems();
        itemRepo.save(items.get(0));
        Optional<Item> optionalItem = itemRepo.findById(items.get(0).getId());

        Assert.assertTrue(optionalItem.isPresent());
        if(optionalItem.isPresent()){
            Assertions.assertThat(optionalItem.get().getId()).isEqualTo(items.get(0).getId());
        }
    }

    @Test
    public void testFindAllByLender(){
        ArrayList<Item> items = createItems();
        itemRepo.save(items.get(0));
        itemRepo.save(items.get(1));

        List<Item> itemList = itemRepo.findAllByLender(items.get(0).getLender());
        Assertions.assertThat(itemList.size()).isEqualTo(2);

    }

    @Test
    public void  testFindAllByNameContainingOrDescriptionContaining(){
        ArrayList<Item> items = createItems();
        itemRepo.save(items.get(0));
        itemRepo.save(items.get(1));
        itemRepo.save(items.get(2));

        List<Item> itemList1 = itemRepo.findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("apf", "egal");
        List<Item> itemList2 = itemRepo.findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("banane", "Kern");
        
        Assertions.assertThat(itemList1.size()).isEqualTo(2);
        Assertions.assertThat(itemList2.size()).isEqualTo(2);


    }
}
