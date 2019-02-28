package de.hhu.sharing.model;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ImageTest {

    @Test
    public void testSetImageData() {
        byte[] byteArray = new byte[100];
        Image image = new Image();
        image.setMimeType("type");

        image.setImageData(byteArray);
        Assertions.assertThat(image.getImageData().length).isEqualTo(100);
    }

    @Test
    public void testGetImageData() {
        byte[] byteArray = new byte[100];
        Image image = new Image();
        image.setMimeType("type");

        image.setImageData(byteArray);
        Assertions.assertThat(image.getImageData()).isEqualTo(byteArray);
    }
}
