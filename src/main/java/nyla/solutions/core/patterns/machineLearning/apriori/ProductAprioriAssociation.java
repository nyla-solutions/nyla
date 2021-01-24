package nyla.solutions.core.patterns.machineLearning.apriori;

import java.util.HashMap;
import java.util.Map;

public class ProductAprioriAssociation
{
    private final Map<String,Integer> associateMapCnt = new HashMap<>();
    private final String productName;

    public ProductAprioriAssociation(String product)
    {
        this.productName = product;
    }

    private ProductAprioriAssociation(String productName, String associate, Integer associateCnt)
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

    public ProductAprioriAssociation filterAssociate(String associate)
    {
        return new ProductAprioriAssociation(productName,associate,this.associateMapCnt.get(associate));
    }

    public String getProductName()
    {
        return productName;
    }
}
