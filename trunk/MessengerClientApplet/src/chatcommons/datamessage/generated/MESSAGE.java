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
 * Class MESSAGE.
 * 
 * @version $Revision$ $Date$
 */
public class MESSAGE implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _type.
     */
    private java.lang.String _type;

    /**
     * Field _name.
     */
    private java.lang.String _name;

    /**
     * Field _receivers.
     */
    private chatcommons.datamessage.generated.Receivers _receivers;

    /**
     * Field _parameters.
     */
    private chatcommons.datamessage.generated.Parameters _parameters;

    /**
     * Field _contents.
     */
    private chatcommons.datamessage.generated.Contents _contents;

    /**
     * Field _data.
     */
    private byte[] _data;

    /**
     * Field _time.
     */
    private java.util.Date _time;

    /**
     * Field _sender.
     */
    private java.lang.String _sender;


      //----------------/
     //- Constructors -/
    //----------------/

    public MESSAGE() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'contents'.
     * 
     * @return the value of field 'Contents'.
     */
    public chatcommons.datamessage.generated.Contents getContents(
    ) {
        return this._contents;
    }

    /**
     * Returns the value of field 'data'.
     * 
     * @return the value of field 'Data'.
     */
    public byte[] getData(
    ) {
        return this._data;
    }

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'Name'.
     */
    public java.lang.String getName(
    ) {
        return this._name;
    }

    /**
     * Returns the value of field 'parameters'.
     * 
     * @return the value of field 'Parameters'.
     */
    public chatcommons.datamessage.generated.Parameters getParameters(
    ) {
        return this._parameters;
    }

    /**
     * Returns the value of field 'receivers'.
     * 
     * @return the value of field 'Receivers'.
     */
    public chatcommons.datamessage.generated.Receivers getReceivers(
    ) {
        return this._receivers;
    }

    /**
     * Returns the value of field 'sender'.
     * 
     * @return the value of field 'Sender'.
     */
    public java.lang.String getSender(
    ) {
        return this._sender;
    }

    /**
     * Returns the value of field 'time'.
     * 
     * @return the value of field 'Time'.
     */
    public java.util.Date getTime(
    ) {
        return this._time;
    }

    /**
     * Returns the value of field 'type'.
     * 
     * @return the value of field 'Type'.
     */
    public java.lang.String getType(
    ) {
        return this._type;
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
     * Sets the value of field 'contents'.
     * 
     * @param contents the value of field 'contents'.
     */
    public void setContents(
            final chatcommons.datamessage.generated.Contents contents) {
        this._contents = contents;
    }

    /**
     * Sets the value of field 'data'.
     * 
     * @param data the value of field 'data'.
     */
    public void setData(
            final byte[] data) {
        this._data = data;
    }

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(
            final java.lang.String name) {
        this._name = name;
    }

    /**
     * Sets the value of field 'parameters'.
     * 
     * @param parameters the value of field 'parameters'.
     */
    public void setParameters(
            final chatcommons.datamessage.generated.Parameters parameters) {
        this._parameters = parameters;
    }

    /**
     * Sets the value of field 'receivers'.
     * 
     * @param receivers the value of field 'receivers'.
     */
    public void setReceivers(
            final chatcommons.datamessage.generated.Receivers receivers) {
        this._receivers = receivers;
    }

    /**
     * Sets the value of field 'sender'.
     * 
     * @param sender the value of field 'sender'.
     */
    public void setSender(
            final java.lang.String sender) {
        this._sender = sender;
    }

    /**
     * Sets the value of field 'time'.
     * 
     * @param time the value of field 'time'.
     */
    public void setTime(
            final java.util.Date time) {
        this._time = time;
    }

    /**
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(
            final java.lang.String type) {
        this._type = type;
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
     * chatcommons.datamessage.generated.MESSAGE
     */
    public static chatcommons.datamessage.generated.MESSAGE unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (chatcommons.datamessage.generated.MESSAGE) Unmarshaller.unmarshal(chatcommons.datamessage.generated.MESSAGE.class, reader);
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
