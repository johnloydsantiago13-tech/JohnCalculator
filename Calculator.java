import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

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
    "7","8","9","/",
    "4","5","6","*",
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
        if (!expression.isEmpty()) {
            try {
                double result = evaluateExpression(expression);
                historyList.add(expression + " = " + result);
            if (historyVisible) updateHistory();
                display.setText(String.valueOf(result));
                expression = String.valueOf(result);
            } catch (Exception ex) {
                display.setText("Error");
                expression = "";
            }
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
        if (btn.matches("[+\\-*/]")) {
            if (!expression.isEmpty() && !expression.endsWith("+") && !expression.endsWith("-") 
                && !expression.endsWith("*") && !expression.endsWith("/")) {
                expression += " " + btn + " ";
                display.setText(expression);
            }
        } else if (btn.equals(".")) {
            if (!expression.isEmpty()) {
                String lastNum = expression.substring(expression.lastIndexOf(" ") + 1);
                if (!lastNum.contains(".")) {
                    expression += ".";
                    display.setText(expression);
                }
            }
        } else {
            expression = expression.equals("0") ? btn : expression + btn;
            display.setText(expression);
        }
    }
}

void toggleHistory() {
    if (historyVisible) {
    mainPanel.remove(scrollPane);
    historyVisible = false;
    setSize(400, 400);
    } else {
    updateHistory();
    mainPanel.add(scrollPane, BorderLayout.EAST);
    historyVisible = true;
    setSize(600, 400);
    }
    mainPanel.revalidate();
    mainPanel.repaint();
}

void updateHistory() {
    historyArea.setText("");
    for (String h : historyList) historyArea.append(h + "\n");
}

double evaluateExpression(String expr) {
    String[] tokens = expr.split(" ");
    double result = Double.parseDouble(tokens[0]);
    for (int i = 1; i < tokens.length; i += 2) {
        String op = tokens[i];
        double num = Double.parseDouble(tokens[i + 1]);
        switch (op) {
            case "+": result += num; break;
            case "-": result -= num; break;
            case "*": result *= num; break;
            case "/": result /= num; break;
        }
    }
    return result;
}
public static void main(String[] args) {
    new Calculator();
}
}