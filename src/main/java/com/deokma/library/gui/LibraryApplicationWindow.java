package com.deokma.library.gui;

/**
 * @author Denis Popolamov
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class LibraryApplicationWindow extends JFrame {

    private JTextArea consoleTextArea;
    private JButton restartServerButton;

    public LibraryApplicationWindow() {
        setTitle("Library Application");

        consoleTextArea = new JTextArea();
        consoleTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(consoleTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        restartServerButton = new JButton("Restart Server");
        restartServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartServer();
            }
        });

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.add(restartServerButton);

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonsPanel, BorderLayout.SOUTH);

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // redirect System.out and System.err to consoleTextArea
        OutputStream outputStream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                consoleTextArea.append(String.valueOf((char) b));
                consoleTextArea.setCaretPosition(consoleTextArea.getDocument().getLength());
            }
        };
        System.setOut(new PrintStream(outputStream, true));
        System.setErr(new PrintStream(outputStream, true));
    }

    private void restartServer() {
        // TODO: restart server
    }

}
