import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class skillbuildApplet {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Applet Viewer: TestProgram");
        JLabel label = new JLabel("Hello. Who are you?");

        label.setFont(new Font("Arial", Font.BOLD, 30));

        frame.setSize(400, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setBackground(Color.YELLOW);
        panel.setLayout(new FlowLayout());

        JTextField text = new JTextField(15);
        JButton button = new JButton("Press me");

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String user = text.getText();

                label.setText("Hello, " + user);
                label.setFont(new Font("Arial", Font.BOLD, 40));
                panel.setBackground(Color.PINK);
                panel.remove(button);
                panel.remove(text);
            }
        });

        panel.add(label);
        panel.add(text);
        panel.add(button);
        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}