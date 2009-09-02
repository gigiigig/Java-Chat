/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id$
 */

package chatcommons.datamessage.generated;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class Contents.
 * 
 * @version $Revision$ $Date$
 */
public class Contents implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _contentList.
     */
    private java.util.Vector _contentList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Contents() {
        super();
        this._contentList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vContent
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addContent(
            final chatcommons.datamessage.generated.Content vContent)
    throws java.lang.IndexOutOfBoundsException {
        this._contentList.addElement(vContent);
    }

    /**
     * 
     * 
     * @param index
     * @param vContent
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addContent(
            final int index,
            final chatcommons.datamessage.generated.Content vContent)
    throws java.lang.IndexOutOfBoundsException {
        this._contentList.add(index, vContent);
    }

    /**
     * Method enumerateContent.
     * 
     * @return an Enumeration over all
     * chatcommons.datamessage.generated.Content elements
     */
    public java.util.Enumeration enumerateContent(
    ) {
        return this._contentList.elements();
    }

    /**
     * Method getContent.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * chatcommons.datamessage.generated.Content at the given index
     */
    public chatcommons.datamessage.generated.Content getContent(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._contentList.size()) {
            throw new IndexOutOfBoundsException("getContent: Index value '" + index + "' not in range [0.." + (this._contentList.size() - 1) + "]");
        }
        
        return (chatcommons.datamessage.generated.Content) _contentList.get(index);
    }

    /**
     * Method getContent.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public chatcommons.datamessage.generated.Content[] getContent(
    ) {
        chatcommons.datamessage.generated.Content[] array = new chatcommons.datamessage.generated.Content[0];
        return (chatcommons.datamessage.generated.Content[]) this._contentList.toArray(array);
    }

    /**
     * Method getContentCount.
     * 
     * @return the size of this collection
     */
    public int getContentCount(
    ) {
        return this._contentList.size();
    }

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid(
    ) {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    }

    /**
     * 
     * 
     * @param out
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void marshal(
            final java.io.Writer out)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, out);
    }

    /**
     * 
     * 
     * @param handler
     * @throws java.io.IOException if an IOException occurs during
     * marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     */
    public void marshal(
            final org.xml.sax.ContentHandler handler)
    throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, handler);
    }

    /**
     */
    public void removeAllContent(
    ) {
        this._contentList.clear();
    }

    /**
     * Method removeContent.
     * 
     * @param vContent
     * @return true if the object was removed from the collection.
     */
    public boolean removeContent(
            final chatcommons.datamessage.generated.Content vContent) {
        boolean removed = _contentList.remove(vContent);
        return removed;
    }

    /**
     * Method removeContentAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public chatcommons.datamessage.generated.Content removeContentAt(
            final int index) {
        java.lang.Object obj = this._contentList.remove(index);
        return (chatcommons.datamessage.generated.Content) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vContent
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setContent(
            final int index,
            final chatcommons.datamessage.generated.Content vContent)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._contentList.size()) {
            throw new IndexOutOfBoundsException("setContent: Index value '" + index + "' not in range [0.." + (this._contentList.size() - 1) + "]");
        }
        
        this._contentList.set(index, vContent);
    }

    /**
     * 
     * 
     * @param vContentArray
     */
    public void setContent(
            final chatcommons.datamessage.generated.Content[] vContentArray) {
        //-- copy array
        _contentList.clear();
        
        for (int i = 0; i < vContentArray.length; i++) {
                this._contentList.add(vContentArray[i]);
        }
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * chatcommons.datamessage.generated.Contents
     */
    public static chatcommons.datamessage.generated.Contents unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (chatcommons.datamessage.generated.Contents) Unmarshaller.unmarshal(chatcommons.datamessage.generated.Contents.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate(
    )
    throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
