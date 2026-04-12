import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class CLayout extends JFrame implements ActionListener {
    
    private JButton L = new JButton("L Button");
    private JButton R = new JButton("R Button");

    private Container c = getContentPane();
    private FlowLayout flow = new FlowLayout();

    public CLayout() {
        setTitle("Button Example");
        setSize(450, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        c.setLayout(flow);
        c.add(L);
        c.add(R);
        L.addActionListener(this);
        R.addActionListener(this);
    }
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if(source == L)
            flow.setAlignment(FlowLayout.LEFT);
        else if(source == R)
            flow.setAlignment(FlowLayout.RIGHT);
        c.invalidate();
        c.validate();
    }

    public static void main(String[] args) {
        CLayout CLayout = new CLayout();
        CLayout.setVisible(true);
        CLayout.setLocationRelativeTo(null);
    }
}