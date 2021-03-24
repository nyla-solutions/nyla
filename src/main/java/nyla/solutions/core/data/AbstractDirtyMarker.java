package nyla.solutions.core.data;

public abstract class AbstractDirtyMarker implements DirtyMarker
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -465283367943502334L;
	public AbstractDirtyMarker()
    {

        dirty = false;

        New = true;

        deleted = false;

    }
    /**
     * 
     * @param aDirty
     */
    public void setDirty(boolean aDirty)
    {

        dirty = aDirty;

    }//--------------------------------------------
    /**
     * if(aId1 != aId2)
          aDirtyMarker.setDirty(true);
     * @param aDirtyMarker
     * @param aId1 the ID 2
     * @param aId2 the ID 2
     */
    public static void setDirty(DirtyMarker aDirtyMarker, int aId1, int aId2)
    {
       if(aDirtyMarker == null)
          return;
       
       if(aId1 != aId2)
          aDirtyMarker.setDirty(true);
    }//--------------------------------------------
    /**
     * if(aObject1 == null || !aObject1.equals(aObject2))
          aDirtyMarker.setDirty(true)
          
     * @param aDirtyMarker
     * @param aObject1
     * @param aObject2
     */
    
    public static void setDirty(DirtyMarker aDirtyMarker, Object aObject1, Object aObject2)
    {
       if(aDirtyMarker == null)
          return;
       
       if(aObject1 == null || !aObject1.equals(aObject2))
          aDirtyMarker.setDirty(true);
       
    }//--------------------------------------------
    /**
     * 
     * Set dirty flag
     */
    public void resetDirty()
    {

        dirty = false;
    }//--------------------------------------------
    /**
     * 
     * @return dirty flag
     * @see nyla.solutions.core.data.DirtyMarker#isDirty()
     */
    public boolean isDirty()

    {

        return dirty;

    }//--------------------------------------------
    /**
     * 
     * 
     * @see nyla.solutions.core.data.DirtyMarker#setNew(boolean)
     */
    public void setNew(boolean aNew)
    {

    	New = aNew;

    }//--------------------------------------------
    /**
     * 
     * set is new to false
      */
    public void resetNew()
    {

    	New = false;

    }//--------------------------------------------
    /**
     * 
     * @return isnew boolean
     * @see nyla.solutions.core.data.DirtyMarker#isNew()
     */
    public boolean isNew()
    {
        return New;
    }//--------------------------------------------
    /**
     * Set deleted flag
     * 
     * @see nyla.solutions.core.data.DirtyMarker#setDeleted(boolean)
     */
    public void setDeleted(boolean aDeleted)
    {
        deleted = aDeleted;
    }//--------------------------------------------
    /**
     * @return deleted flag
     * 
     * @see nyla.solutions.core.data.DirtyMarker#isDeleted()
     */
    public boolean isDeleted()
    {

        return deleted;
    }//--------------------------------------------
    public void resetDeleted()
    {
        deleted = false;
    }//--------------------------------------------
    public AbstractDirtyMarker clone()
    throws CloneNotSupportedException
    {
        return (AbstractDirtyMarker)super.clone();
    }//--------------------------------------------

    private boolean dirty;
    private boolean New;
    private boolean deleted;
}

