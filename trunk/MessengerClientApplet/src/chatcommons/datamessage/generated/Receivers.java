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
 * Class Receivers.
 * 
 * @version $Revision$ $Date$
 */
public class Receivers implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _receiverList.
     */
    private java.util.Vector _receiverList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Receivers() {
        super();
        this._receiverList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vReceiver
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addReceiver(
            final java.lang.String vReceiver)
    throws java.lang.IndexOutOfBoundsException {
        this._receiverList.addElement(vReceiver);
    }

    /**
     * 
     * 
     * @param index
     * @param vReceiver
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addReceiver(
            final int index,
            final java.lang.String vReceiver)
    throws java.lang.IndexOutOfBoundsException {
        this._receiverList.add(index, vReceiver);
    }

    /**
     * Method enumerateReceiver.
     * 
     * @return an Enumeration over all java.lang.String elements
     */
    public java.util.Enumeration enumerateReceiver(
    ) {
        return this._receiverList.elements();
    }

    /**
     * Method getReceiver.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the java.lang.String at the given index
     */
    public java.lang.String getReceiver(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._receiverList.size()) {
            throw new IndexOutOfBoundsException("getReceiver: Index value '" + index + "' not in range [0.." + (this._receiverList.size() - 1) + "]");
        }
        
        return (java.lang.String) _receiverList.get(index);
    }

    /**
     * Method getReceiver.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public java.lang.String[] getReceiver(
    ) {
        java.lang.String[] array = new java.lang.String[0];
        return (java.lang.String[]) this._receiverList.toArray(array);
    }

    /**
     * Method getReceiverCount.
     * 
     * @return the size of this collection
     */
    public int getReceiverCount(
    ) {
        return this._receiverList.size();
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
    public void removeAllReceiver(
    ) {
        this._receiverList.clear();
    }

    /**
     * Method removeReceiver.
     * 
     * @param vReceiver
     * @return true if the object was removed from the collection.
     */
    public boolean removeReceiver(
            final java.lang.String vReceiver) {
        boolean removed = _receiverList.remove(vReceiver);
        return removed;
    }

    /**
     * Method removeReceiverAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public java.lang.String removeReceiverAt(
            final int index) {
        java.lang.Object obj = this._receiverList.remove(index);
        return (java.lang.String) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vReceiver
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setReceiver(
            final int index,
            final java.lang.String vReceiver)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._receiverList.size()) {
            throw new IndexOutOfBoundsException("setReceiver: Index value '" + index + "' not in range [0.." + (this._receiverList.size() - 1) + "]");
        }
        
        this._receiverList.set(index, vReceiver);
    }

    /**
     * 
     * 
     * @param vReceiverArray
     */
    public void setReceiver(
            final java.lang.String[] vReceiverArray) {
        //-- copy array
        _receiverList.clear();
        
        for (int i = 0; i < vReceiverArray.length; i++) {
                this._receiverList.add(vReceiverArray[i]);
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
     * chatcommons.datamessage.generated.Receivers
     */
    public static chatcommons.datamessage.generated.Receivers unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (chatcommons.datamessage.generated.Receivers) Unmarshaller.unmarshal(chatcommons.datamessage.generated.Receivers.class, reader);
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
