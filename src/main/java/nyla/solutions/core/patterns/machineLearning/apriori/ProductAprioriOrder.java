package nyla.solutions.core.patterns.machineLearning.apriori;

public class ProductAprioriOrder
{
    private final Integer orderid;
    private final String productname;

    public ProductAprioriOrder(Integer orderid, String productname)
    {
        this.orderid = orderid;
        this.productname = productname;
    }

    public Integer getOrderid()
    {
        return orderid;
    }

    public String getProductname()
    {
        return productname;
    }
}
