package morozov.vu.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import morozov.vu.IntegrationTest;
import morozov.vu.domain.ShopTwo;
import morozov.vu.repository.ShopTwoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ShopTwoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShopTwoResourceIT {

    private static final String DEFAULT_SHOP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHOP_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_OWNER = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/shop-twos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ShopTwoRepository shopTwoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShopTwoMockMvc;

    private ShopTwo shopTwo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShopTwo createEntity(EntityManager em) {
        ShopTwo shopTwo = new ShopTwo().shopName(DEFAULT_SHOP_NAME).owner(DEFAULT_OWNER).category(DEFAULT_CATEGORY).email(DEFAULT_EMAIL);
        return shopTwo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShopTwo createUpdatedEntity(EntityManager em) {
        ShopTwo shopTwo = new ShopTwo().shopName(UPDATED_SHOP_NAME).owner(UPDATED_OWNER).category(UPDATED_CATEGORY).email(UPDATED_EMAIL);
        return shopTwo;
    }

    @BeforeEach
    public void initTest() {
        shopTwo = createEntity(em);
    }

    @Test
    @Transactional
    void createShopTwo() throws Exception {
        int databaseSizeBeforeCreate = shopTwoRepository.findAll().size();
        // Create the ShopTwo
        restShopTwoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shopTwo)))
            .andExpect(status().isCreated());

        // Validate the ShopTwo in the database
        List<ShopTwo> shopTwoList = shopTwoRepository.findAll();
        assertThat(shopTwoList).hasSize(databaseSizeBeforeCreate + 1);
        ShopTwo testShopTwo = shopTwoList.get(shopTwoList.size() - 1);
        assertThat(testShopTwo.getShopName()).isEqualTo(DEFAULT_SHOP_NAME);
        assertThat(testShopTwo.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testShopTwo.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testShopTwo.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void createShopTwoWithExistingId() throws Exception {
        // Create the ShopTwo with an existing ID
        shopTwo.setId(1L);

        int databaseSizeBeforeCreate = shopTwoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShopTwoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shopTwo)))
            .andExpect(status().isBadRequest());

        // Validate the ShopTwo in the database
        List<ShopTwo> shopTwoList = shopTwoRepository.findAll();
        assertThat(shopTwoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllShopTwos() throws Exception {
        // Initialize the database
        shopTwoRepository.saveAndFlush(shopTwo);

        // Get all the shopTwoList
        restShopTwoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shopTwo.getId().intValue())))
            .andExpect(jsonPath("$.[*].shopName").value(hasItem(DEFAULT_SHOP_NAME)))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getShopTwo() throws Exception {
        // Initialize the database
        shopTwoRepository.saveAndFlush(shopTwo);

        // Get the shopTwo
        restShopTwoMockMvc
            .perform(get(ENTITY_API_URL_ID, shopTwo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shopTwo.getId().intValue()))
            .andExpect(jsonPath("$.shopName").value(DEFAULT_SHOP_NAME))
            .andExpect(jsonPath("$.owner").value(DEFAULT_OWNER))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getNonExistingShopTwo() throws Exception {
        // Get the shopTwo
        restShopTwoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewShopTwo() throws Exception {
        // Initialize the database
        shopTwoRepository.saveAndFlush(shopTwo);

        int databaseSizeBeforeUpdate = shopTwoRepository.findAll().size();

        // Update the shopTwo
        ShopTwo updatedShopTwo = shopTwoRepository.findById(shopTwo.getId()).get();
        // Disconnect from session so that the updates on updatedShopTwo are not directly saved in db
        em.detach(updatedShopTwo);
        updatedShopTwo.shopName(UPDATED_SHOP_NAME).owner(UPDATED_OWNER).category(UPDATED_CATEGORY).email(UPDATED_EMAIL);

        restShopTwoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedShopTwo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedShopTwo))
            )
            .andExpect(status().isOk());

        // Validate the ShopTwo in the database
        List<ShopTwo> shopTwoList = shopTwoRepository.findAll();
        assertThat(shopTwoList).hasSize(databaseSizeBeforeUpdate);
        ShopTwo testShopTwo = shopTwoList.get(shopTwoList.size() - 1);
        assertThat(testShopTwo.getShopName()).isEqualTo(UPDATED_SHOP_NAME);
        assertThat(testShopTwo.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testShopTwo.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testShopTwo.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void putNonExistingShopTwo() throws Exception {
        int databaseSizeBeforeUpdate = shopTwoRepository.findAll().size();
        shopTwo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShopTwoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shopTwo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shopTwo))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShopTwo in the database
        List<ShopTwo> shopTwoList = shopTwoRepository.findAll();
        assertThat(shopTwoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShopTwo() throws Exception {
        int databaseSizeBeforeUpdate = shopTwoRepository.findAll().size();
        shopTwo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShopTwoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shopTwo))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShopTwo in the database
        List<ShopTwo> shopTwoList = shopTwoRepository.findAll();
        assertThat(shopTwoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShopTwo() throws Exception {
        int databaseSizeBeforeUpdate = shopTwoRepository.findAll().size();
        shopTwo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShopTwoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shopTwo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShopTwo in the database
        List<ShopTwo> shopTwoList = shopTwoRepository.findAll();
        assertThat(shopTwoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShopTwoWithPatch() throws Exception {
        // Initialize the database
        shopTwoRepository.saveAndFlush(shopTwo);

        int databaseSizeBeforeUpdate = shopTwoRepository.findAll().size();

        // Update the shopTwo using partial update
        ShopTwo partialUpdatedShopTwo = new ShopTwo();
        partialUpdatedShopTwo.setId(shopTwo.getId());

        partialUpdatedShopTwo.category(UPDATED_CATEGORY).email(UPDATED_EMAIL);

        restShopTwoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShopTwo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShopTwo))
            )
            .andExpect(status().isOk());

        // Validate the ShopTwo in the database
        List<ShopTwo> shopTwoList = shopTwoRepository.findAll();
        assertThat(shopTwoList).hasSize(databaseSizeBeforeUpdate);
        ShopTwo testShopTwo = shopTwoList.get(shopTwoList.size() - 1);
        assertThat(testShopTwo.getShopName()).isEqualTo(DEFAULT_SHOP_NAME);
        assertThat(testShopTwo.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testShopTwo.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testShopTwo.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void fullUpdateShopTwoWithPatch() throws Exception {
        // Initialize the database
        shopTwoRepository.saveAndFlush(shopTwo);

        int databaseSizeBeforeUpdate = shopTwoRepository.findAll().size();

        // Update the shopTwo using partial update
        ShopTwo partialUpdatedShopTwo = new ShopTwo();
        partialUpdatedShopTwo.setId(shopTwo.getId());

        partialUpdatedShopTwo.shopName(UPDATED_SHOP_NAME).owner(UPDATED_OWNER).category(UPDATED_CATEGORY).email(UPDATED_EMAIL);

        restShopTwoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShopTwo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShopTwo))
            )
            .andExpect(status().isOk());

        // Validate the ShopTwo in the database
        List<ShopTwo> shopTwoList = shopTwoRepository.findAll();
        assertThat(shopTwoList).hasSize(databaseSizeBeforeUpdate);
        ShopTwo testShopTwo = shopTwoList.get(shopTwoList.size() - 1);
        assertThat(testShopTwo.getShopName()).isEqualTo(UPDATED_SHOP_NAME);
        assertThat(testShopTwo.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testShopTwo.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testShopTwo.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void patchNonExistingShopTwo() throws Exception {
        int databaseSizeBeforeUpdate = shopTwoRepository.findAll().size();
        shopTwo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShopTwoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shopTwo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shopTwo))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShopTwo in the database
        List<ShopTwo> shopTwoList = shopTwoRepository.findAll();
        assertThat(shopTwoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShopTwo() throws Exception {
        int databaseSizeBeforeUpdate = shopTwoRepository.findAll().size();
        shopTwo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShopTwoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shopTwo))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShopTwo in the database
        List<ShopTwo> shopTwoList = shopTwoRepository.findAll();
        assertThat(shopTwoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShopTwo() throws Exception {
        int databaseSizeBeforeUpdate = shopTwoRepository.findAll().size();
        shopTwo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShopTwoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(shopTwo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShopTwo in the database
        List<ShopTwo> shopTwoList = shopTwoRepository.findAll();
        assertThat(shopTwoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShopTwo() throws Exception {
        // Initialize the database
        shopTwoRepository.saveAndFlush(shopTwo);

        int databaseSizeBeforeDelete = shopTwoRepository.findAll().size();

        // Delete the shopTwo
        restShopTwoMockMvc
            .perform(delete(ENTITY_API_URL_ID, shopTwo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ShopTwo> shopTwoList = shopTwoRepository.findAll();
        assertThat(shopTwoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
