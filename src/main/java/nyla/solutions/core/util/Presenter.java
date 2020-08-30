package nyla.solutions.core.util;


import nyla.solutions.core.exception.SystemException;

import java.util.*;

/**
 * 
 * <pre>
 *  Presenter message view helper.
 *  
 *  All presentation messages can be written by the system configuration module.
 *  @see Config
 * 
 * </pre>
 * 
 * @author Gregory Green
 * 
 * @version 1.0
 * 
 */

public class Presenter
{
   private ResourceBundle resourceBundle = null;

   /**
    * 
    * Constructor for Presenter initalizes internal 
    * data settings.
    */
   private Presenter()
   {
      this(Locale.US);

   }// --------------------------------------------

   /**
    *
    * Constructor for Presenter initalizes internal
    * data settings.
    * @param aLocale the local
    */
   private Presenter(Locale aLocale)
   {
      this(Presenter.class.getName(), aLocale);
   }// --------------------------------------------
   /**
    * Constructor for Presenter initalizes internal data settings.
    * 
    */
   protected Presenter(String aBundleName, Locale aLocale)
   {

      if (aBundleName == null)

         throw new IllegalArgumentException("aBundleName required in Presenter");

      if (aLocale == null)

         throw new IllegalArgumentException("aLocale required in Presenter");

      init(ResourceBundle.getBundle(aBundleName, aLocale));
      
   }// --------------------------------------------
   /**
    * 
    * Constructor for Presenter initalizes internal 
    * data settings.
    * @param aResourceBundle
    */
   private Presenter(ResourceBundle aResourceBundle)
   {
      init(aResourceBundle);
   }// --------------------------------------------
   /**
    * Initial the present with the given resource bundle
    * @param aResourceBundle
    */
   private void init(ResourceBundle aResourceBundle)
   {
      if (aResourceBundle == null)
         throw new IllegalArgumentException(
         "aResourceBundle required in Presenter.init");
      
      resourceBundle = aResourceBundle; 
   }// --------------------------------------------
   /**
    * 
    * Singleton factory method
    * @return a single instance of the Presenter object
    * for the JVM
    * @param bundle the resource bundle
    */

   public static Presenter getPresenter(ResourceBundle bundle)
   {
      return new Presenter(bundle);
   }// --------------------------------------------
   /**
    * 
    * @return a new instance of the Present with en_US locale
    */
   public static Presenter getPresenter()
   {
      return new Presenter();
      
   }// --------------------------------------------
   /**
    * 
    * New instance for the locale
    * @param aLocale the locale based for the resource bundle
    * @return a new instance of the Presenter
    * 
    */

   public static Presenter getPresenter(Locale aLocale)
   {
      return new Presenter(aLocale);
   }// --------------------------------------------
   /**
    * 
    * New instance for the class
    * @param aClass the class/path that contains the resource bundle
    * @return a new instance of the Presenter
    */
   public static Presenter getPresenter(Class<?> aClass)
   {
      if (aClass == null)
         throw new IllegalArgumentException(
         "aClass required in Presenter.getPresenter");
      
      return new Presenter(ResourceBundle.getBundle(aClass.getName()));
   }// --------------------------------------------
   /**
    * 
    * 
    * 
    * @param aKey
    *           bundle message key
    * 
    * @return resourceBundle.getString(aKey)
    * 
    */

   public String getText(String aKey)
   {
      String defaultMessage = "";
      
      try
      {
         defaultMessage = resourceBundle.getString(aKey);
      }
      catch(MissingResourceException e)
      {
         Debugger.printWarn(e);
      }
      
      return Config.getProperty(aKey, defaultMessage);

   }// --------------------------------------------
   /**
    * 
    * 
    * @param <K> the key 
    * @param <V> the value
    * @param key  bundle message key
    * @param parameters the name/key to insert the placeholders
    * @return resourceBundle.getString(aKey)
    * 
    */
   public <K,V> String getText(String key, Map<K,V>  parameters)
   {
      try
      {

         return Text.format(getText(key), parameters);
      }

      catch (Exception e)
      {
         throw new SystemException(e);
      }
   }// --------------------------------------------

   /**
    *
    * @param key the resource bundle key
    * @param values the single place holder in resource text i.e.
    *  test=A single value placed here ${0}
    * @return the formatted text
    */
   public String getText(String key, String [] values)
   {
      if(values == null)
         throw new IllegalArgumentException("aValues required in Presenter.getText");
      try
      {
         HashMap<String,String> map = new HashMap<String,String>();

         for (int i = 0; i < values.length; i++)
         {
            map.put(Integer.toString(i),values[i]);
         }


         return getText(key,map);
      }
      catch (Exception e)
      {
         throw new SystemException(e);
      }
   }// --------------------------------------------

   public String[] getTexts(String propertyName, String textsSplitRegEx)
   {
      String text = this.getText(propertyName);

      return text.split(textsSplitRegEx);
   }
}

