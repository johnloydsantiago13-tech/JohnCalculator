import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Calculator extends JFrame {
    JTextField display;
    JTextArea historyArea;
    ArrayList<String> historyList = new ArrayList<>();
    String expression = "";
    boolean historyVisible = false;
    JPanel mainPanel;
    JScrollPane scrollPane;


public Calculator() {
    setTitle("Calculator");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(400, 400);
    setLocationRelativeTo(null);
    setResizable(false);

    mainPanel = new JPanel(new BorderLayout(5, 5));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    display = new JTextField();
    display.setFont(new Font("Arial", Font.PLAIN, 20));
    display.setEditable(false);
    display.setHorizontalAlignment(JTextField.RIGHT);
    display.setText("0");
    mainPanel.add(display, BorderLayout.NORTH);


JPanel buttonPanel = new JPanel(new GridLayout(5, 4, 3, 3));
    String[] buttons = {
    "C","Del","%","History",
    "7","8","9","÷",
    "4","5","6","×",
    "1","2","3","-",
    "0",".","=","+"
    };

for (String btn : buttons) {
    JButton button = new JButton(btn);
    button.setFont(new Font("Arial", Font.PLAIN, 16));
    buttonPanel.add(button);
    button.addActionListener(e -> handleButton(btn));
    }


historyArea = new JTextArea(15, 15);
historyArea.setFont(new Font("Arial", Font.PLAIN, 11));
historyArea.setEditable(false);
historyArea.setBackground(new Color(240, 240, 240));
scrollPane = new JScrollPane(historyArea);
scrollPane.setBorder(BorderFactory.createTitledBorder("History"));


mainPanel.add(buttonPanel, BorderLayout.CENTER);
add(mainPanel);
setVisible(true);
}

void handleButton(String btn) {
    switch (btn) {
        case "=":
            if (!expression.isEmpty()) try {
                double result = evaluateExpression(expression);
                historyList.add(expression + " = " + result);
                if (historyVisible) updateHistory();
                display.setText(String.valueOf(result));
                expression = String.valueOf(result);
            } catch (Exception ex) {
                display.setText("Error");
                expression = "";
            }
        break;
    case "C":
        expression = "";
        display.setText("0");
        break;
    case "Del":
        if (!expression.isEmpty()) {
            expression = expression.substring(0, expression.length() - 1);
            display.setText(expression.isEmpty() ? "0" : expression);
        }
        break;
    case "History":
        toggleHistory();
        break;
    case "%":
        try {
            double current = Double.parseDouble(expression);
            current = current / 100;
            expression = String.valueOf(current);
            display.setText(expression);
        } catch (Exception e) {
            display.setText("Error");
            expression = "";
        }
        break;
    default:
        if ("+-×÷".contains(btn)) {
            String t = expression.trim();
            if (!t.isEmpty() && "+-×÷".indexOf(t.charAt(t.length() - 1)) == -1) {
                expression += " " + btn + " ";
                display.setText(expression);
            }
        } else if (btn.equals(".")) {
            if (!expression.isEmpty()) {
                String t = expression.trim();
                String last = t.substring(t.lastIndexOf(' ') + 1);
                if ("+-×÷".contains(last)) expression += " 0.";
                else if (!last.contains(".")) expression += ".";
                display.setText(expression);
            }
        } else {
            expression = expression.equals("0") ? btn : expression + btn;
            display.setText(expression);
        }
    }
}

void toggleHistory() {
    if (historyVisible) { mainPanel.remove(scrollPane); setSize(400, 400); historyVisible = false; }
    else { updateHistory(); mainPanel.add(scrollPane, BorderLayout.EAST); setSize(600, 400); historyVisible = true; }
    mainPanel.revalidate();
    mainPanel.repaint();
}

void updateHistory() {
    historyArea.setText(String.join("\n", historyList));
}

double evaluateExpression(String expr) {
    String[] t = expr.trim().split("\\s+");
    Deque<Double> values = new ArrayDeque<>();
    Deque<Character> ops = new ArrayDeque<>();

    values.push(Double.parseDouble(t[0]));
    for (int i = 1; i < t.length; i += 2) {
        char op = t[i].charAt(0);
        double nextNum = Double.parseDouble(t[i + 1]);

        while (!ops.isEmpty() && precedence(ops.peek()) >= precedence(op)) {
            applyOp(values, ops.pop());
        }
        ops.push(op);
        values.push(nextNum);
    }
    while (!ops.isEmpty()) applyOp(values, ops.pop());
    return values.pop();
}

int precedence(char op) {
    return (op == '×' || op == '÷') ? 2 : 1;
}

void applyOp(Deque<Double> values, char op) {
    double b = values.pop();
    double a = values.pop();
    switch (op) {
        case '+': values.push(a + b); break;
        case '-': values.push(a - b); break;
        case '×': values.push(a * b); break;
        case '÷': values.push(a / b); break;
    }
}
public static void main(String[] args) {
    new Calculator();
}
}