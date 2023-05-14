package com.mycompany.phonebookmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class PhonebookManagementSystem {
    class Contact {
        private String name;
        private String phoneNumber;

        public Contact(String name, String phoneNumber) {
            this.name = name;
            this.phoneNumber = phoneNumber;
        }

        public String getName() {
            return name;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }

    class PhonebookFrame extends JFrame{
        private ArrayList<Contact> contacts;
        private DefaultListModel<String> listModel;
        private JList<String> contactList;
        private JTextField nameField;
        private JTextField phoneField;
        private JButton addButton;
        private JButton editButton;
        private JButton deleteButton;

        public PhonebookFrame() {
            contacts = new ArrayList<>();
            listModel = new DefaultListModel<>();

            contactList = new JList<>(listModel);
            JScrollPane scrollPane = new JScrollPane(contactList);
            scrollPane.setPreferredSize(new Dimension(200, 200));

            nameField = new JTextField(20);
            phoneField = new JTextField(20);

            addButton = new JButton("Add");
            addButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addContact();
                }
            });

            editButton = new JButton("Edit");
            editButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    editContact();
                }
            });

            deleteButton = new JButton("Delete");
            deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    deleteContact();
                }
            });

            JPanel inputPanel = new JPanel(new GridLayout(3, 2));
            inputPanel.add(new JLabel("Name:"));
            inputPanel.add(nameField);
            inputPanel.add(new JLabel("Phone Number:"));
            inputPanel.add(phoneField);
            inputPanel.add(addButton);
            inputPanel.add(editButton);
            inputPanel.add(deleteButton);

            setLayout(new BorderLayout());
            add(scrollPane, BorderLayout.CENTER);
            add(inputPanel, BorderLayout.SOUTH);

            setTitle("Phonebook Management System");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            pack();
            setLocationRelativeTo(null);
            setVisible(true);
        }

        private void addContact() {
            String name = nameField.getText();
            String phoneNumber = phoneField.getText();
            if (!name.isEmpty() && !phoneNumber.isEmpty()) {
                Contact contact = new Contact(name, phoneNumber);
                contacts.add(contact);
                listModel.addElement(contact.getName());
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a name and phone number.");
            }
        }

        private void editContact() {
            int selectedIndex = contactList.getSelectedIndex();
            if (selectedIndex != -1) {
                String newName = nameField.getText();
                String newPhoneNumber = phoneField.getText();
                if (!newName.isEmpty() && !newPhoneNumber.isEmpty()) {
                    Contact contact = contacts.get(selectedIndex);
                    contact.setName(newName);
                    contact.setPhoneNumber(newPhoneNumber);
                    listModel.set(selectedIndex, contact.getName());
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Please enter a name and phone number.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a contact to edit.");
            }
        }

        private void deleteContact() {
            int selectedIndex = contactList.getSelectedIndex();
            if (selectedIndex != -1) {
                contacts.remove(selectedIndex);
                listModel.remove(selectedIndex);
            }
        }

        private void clearFields() {
            nameField.setText("");
            phoneField.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PhonebookManagementSystem().createAndShowGUI();
            }
        });
    }

    private void createAndShowGUI() {
        PhonebookFrame frame = new PhonebookFrame();
    }
}

