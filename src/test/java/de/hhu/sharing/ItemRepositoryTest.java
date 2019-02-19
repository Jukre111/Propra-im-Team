package de.hhu.sharing;

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
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepo;
    @Autowired
    UserRepository userRepo;

    @Test
    public void testFindAll() {
        Item item1 = new Item();
        Item item2 = new Item();

        item1.setName("item1");
        item1.setDescription("descr1");

        item2.setName("item2");
        item2.setDescription("descr2");

        itemRepo.save(item1);
        itemRepo.save(item2);

        Assertions.assertThat(itemRepo.findAll().size()).isEqualTo(2);
    }

    @Test
    public void testFindById(){

        Item item = new Item();

        item.setName("item");
        item.setDescription("description");

        itemRepo.save(item);

        Optional<Item> optionalItem = itemRepo.findById(item.getId());

        Assert.assertTrue(optionalItem.isPresent());
        if(optionalItem.isPresent()){
            Assertions.assertThat(optionalItem.get().getId()).isEqualTo(item.getId());
        }
    }

    @Test
    public void testFindAllByLender(){

        Address add = new Address();

        add.setCity("Duesseldorf");
        add.setPostcode(40233);
        add.setStreet("Universitaetsstrasse 1");

        User user = new User();

        user.setUsername("Nutzer8");
        user.setForename("Joe");
        user.setLastname("Karl");
        user.setRole("ROLE_ADMIN");
        user.setEmail("Person1@test.de");
        user.setPassword("pswd");
        user.setAddress(add);

        LocalDate date1 = LocalDate.of(2019, 5, 13);
        user.setBirthdate(date1);

        userRepo.save(user);
        User userEntity = userRepo.findByUsername("Nutzer8").get();

        Item item1 = new Item();
        Item item2 = new Item();

        item1.setName("item1");
        item1.setDescription("descr1");
        item1.setLender(userEntity);

        item2.setName("item2");
        item2.setDescription("descr2");
        item2.setLender(userEntity);


        itemRepo.save(item1);
        itemRepo.save(item2);


        List<Item> itemList = itemRepo.findAllByLender(userEntity);
        Assertions.assertThat(itemList.size()).isEqualTo(2);

    }

    @Test
    public void  testFindAllByNameContainingOrDescriptionContaining(){
        Item item1 = new Item();
        Item item2 = new Item();
        Item item3 = new Item();

        item1.setName("apfel");
        item1.setDescription("Kerne");

        item2.setName("granatapfel");
        item2.setDescription("lecker");

        item3.setName("banane");
        item3.setDescription("lecker");

        itemRepo.save(item1);
        itemRepo.save(item2);
        itemRepo.save(item3);

        List<Item> itemList1 = itemRepo.findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("apf", "Kern");
        List<Item> itemList2 = itemRepo.findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("banane", "leck");
        
        Assertions.assertThat(itemList1.size()).isEqualTo(2);
        Assertions.assertThat(itemList2.size()).isEqualTo(2);


    }
}
