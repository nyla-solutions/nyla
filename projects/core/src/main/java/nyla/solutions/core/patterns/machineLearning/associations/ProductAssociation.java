package nyla.solutions.core.patterns.machineLearning.associations;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProductAssociation
{
    private final Map<String,Integer> associateMapCnt = new HashMap<>();
    private final String productName;

    public ProductAssociation(String product)
    {
        this.productName = product;
    }

    private ProductAssociation(String productName, String associate, Integer associateCnt)
    {
        this.productName = productName;
        associateMapCnt.put(associate,associateCnt);
    }

    public void addAssociate(String associate)
    {
        Integer cnt = associateMapCnt.get(associate);
        if(cnt == null)
            cnt = 0;

        associateMapCnt.put(associate,cnt+1);
    }

    public int countAssociate(String associate)
    {
        Integer cnt = associateMapCnt.get(associate);
        if(cnt == null)
            return 0;

        return cnt;
    }

    public ProductAssociation filterAssociate(String associate)
    {
        return new ProductAssociation(productName,associate,this.associateMapCnt.get(associate));
    }

    public String getProductName()
    {
        return productName;
    }

    public Set<String> getAssociateNames()
    {
        return this.associateMapCnt.keySet();

    }
}
