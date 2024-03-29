/*
 * SelectClientsToAdd.java
 *
 * Created on 7 maggio 2008, 19.38
 */
package chatclient.forms;

import chatclient.chatwindow.ChatWindow;
import chatclient.*;
import chatcommons.Client;
import chatcommons.datamessage.MESSAGE;
import chatcommons.datamessage.MessageManger;
import java.net.SocketException;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.application.Action;
import static chatcommons.Commands.*;

/**
 *
 * @author  Administrator
 */
public class SelectClientsToAdd extends javax.swing.JFrame {

    private ArrayList<Client> clients;
    private ChatWindow cv;
    private ChatClientView ccv;
    private Log log = LogFactory.getLog(SelectClientsToAdd.class);

    /** Creates new form SelectClientsToAdd */
    public SelectClientsToAdd(ChatWindow cv, ChatClientView ccv) {
        ArrayList<Client> inChat = cv.getClients();
        ArrayList<Client> tutti = ccv.getClients();
        clients = new ArrayList<Client>();


        // aggiungo a clients gli utenti non presenti nella chat
        for (Client elem : tutti) {
            boolean presente = false;
            for (Client elem2 : inChat) {
                if (elem.getNick().equals(elem2.getNick()) || elem.getNick().equals(cv.getNick())) {
                    presente = true;
                }
            }
            if (!presente) {
                clients.add(elem);
            }
        }
        initComponents();
        this.cv = cv;
        this.ccv = ccv;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        jScrollPane1 = new javax.swing.JScrollPane();
        nickList = new javax.swing.JList();
        addButton = new javax.swing.JButton();

        setName("Form"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        nickList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        nickList.setName("nickList"); // NOI18N

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${clients}");
        org.jdesktop.swingbinding.JListBinding jListBinding = org.jdesktop.swingbinding.SwingBindings.createJListBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, eLProperty, nickList);
        jListBinding.setDetailBinding(org.jdesktop.beansbinding.ELProperty.create("${nick}"));
        bindingGroup.addBinding(jListBinding);

        jScrollPane1.setViewportView(nickList);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(chatclient.ChatClientApp.class).getContext().getActionMap(SelectClientsToAdd.class, this);
        addButton.setAction(actionMap.get("addSeletedToChat")); // NOI18N
        addButton.setName("addButton"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addButton)
                .addContainerGap(54, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addButton)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE))
                .addContainerGap())
        );

        bindingGroup.bind();

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList nickList;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
    @Action
    public void addSeletedToChat() {
        String nickSelected = (String) nickList.getSelectedValue();

        MESSAGE message = MessageManger.createRequest(Request.ADDTOCONFERENCE, null, null);

        for (Client client : cv.getClients()) {
            MessageManger.addReceiver(message, client.getNick());
        }
//        message.getParameters().add(nickSelected);
        MessageManger.addParameter(message,"nick", nickSelected);
        try {
            MessageManger.directWriteMessage(message, ccv.getOutputStream());
        } catch (SocketException socketException) {
            log.error(socketException);
        }

        //lo aggiungo alla chat locale
        cv.addClientToChat(nickSelected);
        this.setVisible(false);

    }

    public javax.swing.JList getNickList() {
        return nickList;
    }

    public void setNickList(javax.swing.JList nickList) {
        this.nickList = nickList;
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public void setClients(ArrayList<Client> clients) {
        this.clients = clients;
    }
}
