package nyla.solutions.core.patterns.machineLearning.associations;

import java.util.Objects;

public class ProductTransition<K>
{
    private final K transitionId;
    private final String productName;

    public ProductTransition(K transitionId, String productname)
    {
        this.transitionId = transitionId;
        this.productName = productname;
    }

    public K getTransitionId()
    {
        return transitionId;
    }

    public String getProductName()
    {
        return productName;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductTransition<?> that = (ProductTransition<?>) o;
        return Objects.equals(transitionId, that.transitionId) &&
                Objects.equals(productName, that.productName);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(transitionId, productName);
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("ProductTransition{");
        sb.append("transitionId=").append(transitionId);
        sb.append(", productName='").append(productName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
