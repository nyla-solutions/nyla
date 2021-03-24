// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.

// Jad home page: http://www.geocities.com/kpdus/jad.html

// Decompiler options: packimports(3) 

// Source File Name:   DirtyMarker.java



package nyla.solutions.core.data;

import java.io.Serializable;

/**
 * 
 * <pre>
 * DirtyMarker provides interface to indicate whether a 
 * value object is new, deleted, or updated/dirty.
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public interface DirtyMarker extends Serializable, Cloneable
{



    public abstract void setDirty(boolean aDirty);



    //public abstract void resetDirty();



    public abstract boolean isDirty();

    public abstract void setNew(boolean aNew);

    /**
     * @return true when data is new
     * 
     */
    public abstract boolean isNew();


    public abstract void setDeleted(boolean aDeleted);


    public abstract boolean isDeleted();


}

