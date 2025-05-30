package nyla.solutions.core.patterns.search;

import nyla.solutions.core.util.Text;

import java.util.*;

/**
 * <pre>
 *
 * The RELookup acts as a lookup table where the key is a complex
 * regular expression. The expression and values are stored in a hash map.
 * Then RELookup.get(argument) method is called,
 * RELookup operation will iterate through the given get argument expressions
 * looking for a match on the corresponding complex regular expression key.
 *
 * The value column of the lookup table is used if the regular expression matches
 * the argument.
 *
 * <code>v
 *
 * 		ReLookup<FaultError> lookup = new ReLookup<FaultError>();
 *
 * Assert.assertTrue(lookup instanceof Map);
 *
 * lookup.put("(001)*.*Green.*${AND}${NOT}.*Blue.*", new FaultError("0001","ERROR"));
 * lookup.put("(002)*.*Green.*${AND}.*Blue.*", new FaultError("0002","GB"));
 * lookup.put("(003)*.*Blue.*", new FaultError("0003","BLUE"));
 *
 * Assert.assertEquals(lookup.get("Green").getCode(), "0001");
 * Assert.assertEquals(lookup.get("Blue Green").getCode(), "0002");
 * Assert.assertEquals(lookup.get("Blue with Live of pure").getCode(), "0003");
 * </code>
 *
 * <h2> Complex Regular Expression (And/Not) </h2>
 * By default, regular expressions do not have an easy way to chain expressions together
 * using AND/NOT logic. The OR logical expression is supported with the character &quot;|&quot;. The RELookup
 * operation combines regular expressions with a special syntax to support AND/NOT logic.
 *
 * <h3>AND Operation</h3>
 *
 * The RELookup supports chaining expressions together with &quot;AND&quot; logic. This is accomplished
 * by chaining expressions together with <code>${AND}</code>. The string &quot;{AND}&quot; can be used to separate
 * two regular expressions. If any of the regular expressions return false then the entire regular
 * expression is false. In the following example, the regular expression .*USA.*${AND}.*Greece.*,
 * only returns true if the text contains both USA and Greece.
 *
 * <table>
 * <thead>
 * <tr>
 * <th>Complex RE</th><th>Value</th><th>	Matches</th>
 * </tr>
 * </thead>
 * <tbody>
 * <tr><td><code>.*USA.*${AND}.*Greece.*</code>	</td><td>USA and Greece</td><td>True</td></tr>
 * <tr><td><code>.*USA.*${AND}.*Greece.*</code>	</td><td>USA</td><td>False</td></tr>
 * <tr><td><code>.*USA.*${AND}.*Greece.*</code>	</td><td>Greece</td><td>False</td></tr>
 * <tr><td><code>.*USA.*${AND}.*Greece.*</code>	</td><td>Greece USA </td><td>True</td></tr>
 * </tbody>
 * </table>
 *
 * <h3>NOT Operation</h3>
 * The RELookup supports negative logic (NOT) for expressions. This is accomplished by prefixing the
 * expressions with ${NOT}. In the following example, the regular expression ${NOT}.*USA.* only returns true
 * if the text does not contain the word USA. Note that multiple ${NOT} can be chained together
 * with ${AND}(s) (see table below).
 *
 * <table>
 * <thead>
 * <tr>
 * <th>Complex RE</th><th>Value</th><th>Matches</th>
 * </tr>
 * </thead>
 * <tbody>
 * <tr>
 * <td>${NOT}.*USA.*	</td><td>USA and Greece	</td><td>	False</td>
 * </tr>
 * <tr>
 * <td>${NOT}.*USA.*</td><td>	USA</td><td>		False</td>
 * </tr>
 * <tr>
 * <td>${NOT}.*USA.*</td><td>	Greece</td><td>		True</td>
 * </tr>
 *
 * <tr>
 * <td>${NOT}.*USA.*</td><td>	Greece USA</td><td>	 	False</td>
 * </tr>
 * <tr>
 * <td>.*Greece.*${AND}${NOT}.*USA.* ${AND}${NOT}.*Turkey.*</td><td>	Greece Turkey</td><td>		False</td>
 * </tr>
 * <tr>
 * <td>.*Greece.*${AND}${NOT}.*USA.* ${AND}${NOT}.*Turkey.*</td><td>	Greece Africa</td><td>		True</td>
 * </tr>
 * </tbody>
 * </table>
 *
 * </pre>
 *
 * @param <T> the value map type
 * @author Gregory Green
 */
public class ReLookup<T> implements Map<String, T>
{
    /**
     * Default constructor
     */
    public ReLookup()
    {
    }// ---------------------------

    /**
     * Lookup a value of the dictionary
     *
     * @param text the value to compare the keys
     * @return the match value for the key
     */
    public T lookup(String text)
    {
        if (text == null)
            return null;

        for (Entry<String, T> entry : lookupMap.entrySet()) {
            if (Text.matches(text, entry.getKey()))
                return lookupMap.get(entry.getKey());

        }

        return null;

    }// ------------------------------

    /**
     * @param reExpression the Regular expression
     * @return collection of values the match text RE expression
     */
    public Collection<T> lookupCollection(String reExpression)
    {
        if (reExpression == null)
            return null;

        ArrayList<T> collection = new ArrayList<T>(lookupMap.size());

        for (Entry<String, T> entry : lookupMap.entrySet()) {
            if (Text.matches(entry.getKey(), reExpression))
                collection.add(lookupMap.get(entry.getKey()));
        }


        collection.trimToSize();

        return collection;

    }// ------------------------------

    /**
     * Add a lookup item
     * param name=""
     *
     * @see java.util.Map#get(java.lang.Object)
     */
    public T get(Object regularExpKey)
    {
        return lookup((String) regularExpKey);
    }// --------------------------------------------------------

    public T put(String regularExpKey, T value)
    {
        return this.lookupMap.put(regularExpKey, value);
    }// --------------------------------------------------------

    public boolean isEmpty()
    {
        return lookupMap.isEmpty();
    }

    /**
     * @param obj
     * @return true if the object contains the keys
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    public boolean containsKey(Object obj)
    {
        return lookupMap.containsKey(obj);
    }

    /**
     * @param obj
     * @return true of the RE contains the value
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    public boolean containsValue(Object obj)
    {
        return lookupMap.containsValue(obj);
    }

    /**
     * @see java.util.Map#clear()
     */
    public void clear()
    {
        lookupMap.clear();
    }//------------------------------------------------

    public Set<String> keySet()
    {
        return lookupMap.keySet();
    }//------------------------------------------------

    public Set<java.util.Map.Entry<String, T>> entrySet()
    {
        return lookupMap.entrySet();
    }//------------------------------------------------

    public boolean equals(Object obj)
    {
        return lookupMap.equals(obj);
    }//------------------------------------------------

    public int hashCode()
    {
        return lookupMap.hashCode();
    }//------------------------------------------------

    public int size()
    {
        return lookupMap.size();
    }// --------------------------------------------------------

    /**
     * @param obj the key
     * @return the removed object
     * @see java.util.Map#remove(java.lang.Object)
     */
    public T remove(Object obj)
    {
        return lookupMap.remove(obj);
    }// --------------------------------------------------------

    /**
     * @param map
     * @see java.util.Map#putAll(java.util.Map)
     */
    public void putAll(Map<? extends String, ? extends T> map)
    {
        lookupMap.putAll(map);
    }// --------------------------------------------------------

    public Collection<T> values()
    {
        return lookupMap.values();
    }// --------------------------------------------------------

    private Map<String, T> lookupMap = new TreeMap<String, T>();

}
