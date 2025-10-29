package Pz13;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

public class SecretMessageApp {
    private JFrame frame;
    private JButton button1, button2, button3;
    private int secretButtonNumber;
    private Random random;

    public SecretMessageApp() {
        random = new Random();
        secretButtonNumber = random.nextInt(3) + 1;

        initializeFrame();
        createComponents();
        addListeners();
        layoutComponents();
    }

    private void initializeFrame() {
        frame = new JFrame("Секретное сообщение");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);
    }

    private void createComponents() {
        button1 = new JButton("Кнопка 1");
        button2 = new JButton("Кнопка 2");
        button3 = new JButton("Кнопка 3");
    }

    private void addListeners() {
        button1.addActionListener(new ButtonActionListener());
        button2.addActionListener(new ButtonActionListener());
        button3.addActionListener(new ButtonActionListener());

        MouseListener mouseListener = new ButtonMouseListener();
        button1.addMouseListener(mouseListener);
        button2.addMouseListener(mouseListener);
        button3.addMouseListener(mouseListener);
    }

    private void layoutComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(button1);
        panel.add(button2);
        panel.add(button3);

        frame.add(panel);
    }

    public void show() {
        frame.setVisible(true);
    }

    private class ButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            String buttonText = clickedButton.getText();

            int clickedButtonNumber = Integer.parseInt(buttonText.split(" ")[1]);

            if (clickedButtonNumber == secretButtonNumber) {
                JOptionPane.showMessageDialog(frame, "Ты нашёл секретную кнопку!");
            } else {
                JOptionPane.showMessageDialog(frame, "Попробуй снова!");
            }
        }
    }

    private class ButtonMouseListener implements MouseListener {
        @Override
        public void mouseEntered(MouseEvent e) {
            System.out.println("Проверяешь?");
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SecretMessageApp().show();
            }
        });
    }
}
