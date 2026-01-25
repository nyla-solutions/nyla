//package nyla.solutions.core.io;
//
//import java.io.File;
//import java.util.EventObject;
//
///**
// * <pre>
// * FileEvent represent files that have been added, changed or remove
// * @see FileMonitor
// *
// *
// *
// * </pre>
// * @author Gregory Green
// * @version 1.0
// */
//public class FileEvent extends EventObject
//{
//   public static final short ADDED   = 1;
//   public static final short CHANGED = 3;
//   public static final short REMOVED = 2;
//
//   /**
//    *
//    * @param source the java.io.File instance
//    * @param eventType the ADD, change or removed event type
//    */
//   public FileEvent(Object source, short eventType)
//   {
//      super(source);
//
//      if(!(source instanceof File))
//         throw new IllegalArgumentException("Provided source must be a file");
//
//
//      this.setEventType(eventType);
//   }//---------------------------
//   /**
//    * Factory method
//    * @param aFile the file that was added
//    * @return new FileEvent(aFile, FileEvent.ADDED)
//    */
//   public static FileEvent createAddedEvent(File aFile)
//   {
//     return new FileEvent(aFile, FileEvent.ADDED);
//   }//-------------------------
//   /**
//    * Factory method
//    * @param aFile the file that was added
//    * @return new FileEvent(aFile, FileEvent.CHANGED)
//    */
//   public static FileEvent createChangedEvent(File aFile)
//   {
//     return new FileEvent(aFile, FileEvent.CHANGED);
//   }//-------------------------
//   /**
//    * Factory method
//    * @param aFile the file that was added
//    * @return new FileEvent(aFile, FileEvent.REMOVED)
//    */
//   public static FileEvent createRemovedEvent(File aFile)
//   {
//     return new FileEvent(aFile, FileEvent.REMOVED);
//   }//-------------------------
//   /**
//    * Uses  FileMonitor.waitFor(file)
//    * @return the source file
//    */
//   public File getFile()
//   {
//      File file = (File)this.getSource();
//
//      if(file != null)
//          FileMonitor.waitFor(file);
//
//      return file;
//
//   }//--------------------------------------------
//
//   /**
//    * @return Returns the eventType.
//    *
//    */
//   public short getEventType() {
//      return eventType;
//   }//--------------------------------
//
//   /**
//    *
//    * @return this.eventType == ADDED
//    */
//   public boolean isAdded()
//   {
//      return this.eventType == ADDED;
//   }//----------------------------------
//   /**
//    *
//    * @return this.eventType == CHANGED
//    */
//   public boolean isChanged()
//   {
//      return this.eventType == CHANGED;
//   }//----------------------------------
//   /**
//    * @return debug string with file name and event type
//    */
//   public String toString()
//   {
//	   File file = (File)getSource();
//
//	   if(file == null)
//		   return "null";
//
//	   String name = file.getName();
//
//      switch(this.eventType)
//      {
//         case ADDED:   return "ADDED   "+name;
//         case REMOVED: return "REMOVED "+name;
//         default: return "CHANGED "+name;
//      }
//   }//--------------------------------------------------
//   /**
//    *
//    * @return this.eventType == REMOVED
//    */
//   public boolean isRemoved()
//   {
//      return this.eventType == REMOVED;
//   }//----------------------------------
//
//   /**
//    * @param eventType The eventType to set.
//    *
//    * @uml.property name="eventType"
//    */
//   private void setEventType(short eventType) {
//      if (eventType != ADDED && eventType != REMOVED && eventType != CHANGED) {
//         throw new IllegalArgumentException("Invalid eventType code "
//            + eventType);
//      }
//
//      this.eventType = eventType;
//   }//---------------------------------------
//
//   private short eventType = CHANGED;
//   static final long serialVersionUID = FileEvent.class.getName()
//   .hashCode();
//}
