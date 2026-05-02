package Server.Managers;



import Common.Model.Classes.Product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.PriorityQueue;

public class CollectionManager {
    private PriorityQueue<Product> collection;
    private ArrayList<Long> idListProduct = new ArrayList<>();
    private ArrayList<Long> idListOrganization = new ArrayList<>();
    private final LocalDateTime initializationDate;

    public CollectionManager() {
        this.collection = new PriorityQueue<>();
        this.idListProduct = new ArrayList<>();
        this.idListOrganization = new ArrayList<>();
        this.initializationDate = LocalDateTime.now();
    }

    public CollectionManager(PriorityQueue<Product> collection) {
        this.collection = new PriorityQueue<>(collection);
        this.initializationDate = LocalDateTime.now();
        this.collection.stream().forEach(product -> {
            idListProduct.add(product.getId());
            if (product.getManufacturer() != null) {
                idListOrganization.add(product.getManufacturer().getId());
            }
        });
        idListProduct.sort(null);
        idListOrganization.sort(null);
    }

    public Long getFirstNotUsedId(ArrayList<Long> idList) {
        idList.sort(null);
        if (!idList.isEmpty()) {
            if (idList.get(0) > 1L) {
                return 1L;
            } else {
                for (int i = 1; i < idList.size(); i++) {
                    if (idList.get(i) - idList.get(i-1) > 1) {
                        return idList.get(i-1) + 1;
                    }
                }
                return (idList.get(idList.size() - 1) + 1);
            }
        } else {
            return 1L;
        }
    }

    public Long getFirstNotUsedIdProduct() {
        return getFirstNotUsedId(idListProduct);
    }

    public Long getFirstNotUsedIdOrganization() {
        return getFirstNotUsedId(idListOrganization);
    }

    public boolean checkIsIdUsed(ArrayList<Long> idList, Product product) {
        Long productId = product.getId();
        return idList.stream().anyMatch(id -> Objects.equals(id,productId));
    }

    public boolean checkIsIdUsedProduct(Product product) {
        return checkIsIdUsed(idListProduct, product);
    }

    public boolean checkIsIdUsedOrganization(Product product) {
        return checkIsIdUsed(idListOrganization, product);
    }

    public boolean addProduct(Product product) {
        if (product == null) {
            return false;
        }
        idListProduct.add(product.getId());
        idListProduct.sort(null);
        if (product.getManufacturer() != null) {
            idListOrganization.add(product.getManufacturer().getId());
        }
        return collection.add(product);
    }

    public Product getProductById(Long id) {
        return collection.stream().filter(product -> Objects.equals(product.getId(), id)).findFirst().orElse(null);
    }

    public boolean removeById(Long id) {
        Product product = getProductById(id);
        if (product != null) {
            idListProduct.remove(product.getId());

            if (product.getManufacturer() != null) {
                idListOrganization.remove(product.getManufacturer().getId());
            }
            return collection.remove(product);
        }
        return false;
    }

    public PriorityQueue<Product> getCollection() {
        return new PriorityQueue<Product>(collection);
    }

    public LocalDateTime getInitializationDate() {
        return initializationDate;
    }

    public boolean isEmpty() {
        return collection.isEmpty();
    }

    public void clear() {
        collection.clear();
        idListProduct.clear();
    }
}