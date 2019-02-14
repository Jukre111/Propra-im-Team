package de.hhu.sharing;

import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.model.Item;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepo;

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
}
