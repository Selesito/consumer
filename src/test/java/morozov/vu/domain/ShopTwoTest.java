package morozov.vu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import morozov.vu.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShopTwoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShopTwo.class);
        ShopTwo shopTwo1 = new ShopTwo();
        shopTwo1.setId(1L);
        ShopTwo shopTwo2 = new ShopTwo();
        shopTwo2.setId(shopTwo1.getId());
        assertThat(shopTwo1).isEqualTo(shopTwo2);
        shopTwo2.setId(2L);
        assertThat(shopTwo1).isNotEqualTo(shopTwo2);
        shopTwo1.setId(null);
        assertThat(shopTwo1).isNotEqualTo(shopTwo2);
    }
}
