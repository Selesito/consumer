package morozov.vu.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import morozov.vu.domain.ShopTwo;
import morozov.vu.repository.ShopTwoRepository;
import morozov.vu.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link morozov.vu.domain.ShopTwo}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ShopTwoResource {

    private final Logger log = LoggerFactory.getLogger(ShopTwoResource.class);

    private static final String ENTITY_NAME = "consumerShopTwo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShopTwoRepository shopTwoRepository;

    public ShopTwoResource(ShopTwoRepository shopTwoRepository) {
        this.shopTwoRepository = shopTwoRepository;
    }

    /**
     * {@code POST  /shop-twos} : Create a new shopTwo.
     *
     * @param shopTwo the shopTwo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shopTwo, or with status {@code 400 (Bad Request)} if the shopTwo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shop-twos")
    public ResponseEntity<ShopTwo> createShopTwo(@RequestBody ShopTwo shopTwo) throws URISyntaxException {
        log.debug("REST request to save ShopTwo : {}", shopTwo);
        if (shopTwo.getId() != null) {
            throw new BadRequestAlertException("A new shopTwo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShopTwo result = shopTwoRepository.save(shopTwo);
        return ResponseEntity
            .created(new URI("/api/shop-twos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shop-twos/:id} : Updates an existing shopTwo.
     *
     * @param id the id of the shopTwo to save.
     * @param shopTwo the shopTwo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shopTwo,
     * or with status {@code 400 (Bad Request)} if the shopTwo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shopTwo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shop-twos/{id}")
    public ResponseEntity<ShopTwo> updateShopTwo(@PathVariable(value = "id", required = false) final Long id, @RequestBody ShopTwo shopTwo)
        throws URISyntaxException {
        log.debug("REST request to update ShopTwo : {}, {}", id, shopTwo);
        if (shopTwo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shopTwo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shopTwoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ShopTwo result = shopTwoRepository.save(shopTwo);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shopTwo.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /shop-twos/:id} : Partial updates given fields of an existing shopTwo, field will ignore if it is null
     *
     * @param id the id of the shopTwo to save.
     * @param shopTwo the shopTwo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shopTwo,
     * or with status {@code 400 (Bad Request)} if the shopTwo is not valid,
     * or with status {@code 404 (Not Found)} if the shopTwo is not found,
     * or with status {@code 500 (Internal Server Error)} if the shopTwo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/shop-twos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShopTwo> partialUpdateShopTwo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ShopTwo shopTwo
    ) throws URISyntaxException {
        log.debug("REST request to partial update ShopTwo partially : {}, {}", id, shopTwo);
        if (shopTwo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shopTwo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shopTwoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShopTwo> result = shopTwoRepository
            .findById(shopTwo.getId())
            .map(existingShopTwo -> {
                if (shopTwo.getShopName() != null) {
                    existingShopTwo.setShopName(shopTwo.getShopName());
                }
                if (shopTwo.getOwner() != null) {
                    existingShopTwo.setOwner(shopTwo.getOwner());
                }
                if (shopTwo.getCategory() != null) {
                    existingShopTwo.setCategory(shopTwo.getCategory());
                }
                if (shopTwo.getEmail() != null) {
                    existingShopTwo.setEmail(shopTwo.getEmail());
                }

                return existingShopTwo;
            })
            .map(shopTwoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shopTwo.getId().toString())
        );
    }

    /**
     * {@code GET  /shop-twos} : get all the shopTwos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shopTwos in body.
     */
    @GetMapping("/shop-twos")
    public List<ShopTwo> getAllShopTwos() {
        log.debug("REST request to get all ShopTwos");
        return shopTwoRepository.findAll();
    }

    /**
     * {@code GET  /shop-twos/:id} : get the "id" shopTwo.
     *
     * @param id the id of the shopTwo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shopTwo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shop-twos/{id}")
    public ResponseEntity<ShopTwo> getShopTwo(@PathVariable Long id) {
        log.debug("REST request to get ShopTwo : {}", id);
        Optional<ShopTwo> shopTwo = shopTwoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(shopTwo);
    }

    /**
     * {@code DELETE  /shop-twos/:id} : delete the "id" shopTwo.
     *
     * @param id the id of the shopTwo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shop-twos/{id}")
    public ResponseEntity<Void> deleteShopTwo(@PathVariable Long id) {
        log.debug("REST request to delete ShopTwo : {}", id);
        shopTwoRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
