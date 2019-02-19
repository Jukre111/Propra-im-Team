package de.hhu.sharing.data;

import com.github.javafaker.Faker;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.RentPeriod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZoneId;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Service
public class DatabaseInitializerService {

    @Autowired
    private ItemRepository items;

    @Transactional
    public void test(){
        Item item = items.findById(Long.valueOf(1)).get();
        final Faker faker = new Faker(Locale.GERMAN);
        System.out.println(item.getPeriods());
        item.addToPeriods(new RentPeriod(
                faker.date().past(10, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                faker.date().future(10,TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
        System.out.println(item.getPeriods());
        items.save(item);
    }
}
