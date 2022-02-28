package morozov.vu.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A ShopTwo.
 */
@Entity
@Table(name = "shop_two")
public class ShopTwo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "shop_name")
    private String shopName;

    @Column(name = "owner")
    private String owner;

    @Column(name = "category")
    private String category;

    @Column(name = "email")
    private String email;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ShopTwo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShopName() {
        return this.shopName;
    }

    public ShopTwo shopName(String shopName) {
        this.setShopName(shopName);
        return this;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getOwner() {
        return this.owner;
    }

    public ShopTwo owner(String owner) {
        this.setOwner(owner);
        return this;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCategory() {
        return this.category;
    }

    public ShopTwo category(String category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEmail() {
        return this.email;
    }

    public ShopTwo email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShopTwo)) {
            return false;
        }
        return id != null && id.equals(((ShopTwo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShopTwo{" +
            "id=" + getId() +
            ", shopName='" + getShopName() + "'" +
            ", owner='" + getOwner() + "'" +
            ", category='" + getCategory() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
