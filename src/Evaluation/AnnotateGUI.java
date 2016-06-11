package Evaluation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by RobbinNi on 3/16/16.
 */
public class AnnotateGUI {
    private JTextArea textArea1;
    private JButton button1;
    private JButton button2;
    private JButton passButton;
    private JPanel panel1;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private Frame frame;
    private int bottonPressed;
    private Thread main;

    private AnnotateGUI(Frame frame) {
        this.frame = frame;
        main = Thread.currentThread();
        textArea1.setLineWrap(true);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bottonPressed = 0;
                main.interrupt();
            }

        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bottonPressed = 1;
                main.interrupt();
            }
        });

        passButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bottonPressed = 2;
                main.interrupt();
            }
        });
        textArea1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyChar() == 'j') {
                    bottonPressed = 0;
                    main.interrupt();
                }
                if (e.getKeyChar() == 'k') {
                    bottonPressed = 1;
                    main.interrupt();
                }
                if (e.getKeyChar() == 'l') {
                    bottonPressed = 2;
                    main.interrupt();
                }
            }
        });
        textArea1.requestFocus();
    }

    public void setDomain(String domain) {
        label3.setText(domain);
    }

    public int getAnnotation(String text, int cntAnt, int BARAnt, int curID, int idTot, boolean oc, boolean nc) {
        textArea1.setText(text);
        label1.setText("Annotated: " + cntAnt + "/" + BARAnt);
        label2.setText("Current : " + curID + "/" + idTot);
        label4.setText((oc ? "+" : "-") + " " + (nc ? "+" : "-"));
        try {
            Thread.sleep(1000000000);
        } catch (InterruptedException ie) {

        }
        int ret = 0;
        if (bottonPressed == 0) {
            ret = 1;
        } else if (bottonPressed == 1) {
            ret = -1;
        }
        return ret;
    }

    public void complete(int BAR) {
        textArea1.setText("Annotation complete.");
        label1.setText("Annotated: " + BAR + "/" + BAR);
        label2.setText("Current : Completed");
        label4.setText("");
    }

    public static AnnotateGUI launchGUI() {
        JFrame frame = new JFrame("AnnotateGUI");
        AnnotateGUI ret = new AnnotateGUI(frame);
        frame.setContentPane(ret.panel1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        return ret;
    }
}
