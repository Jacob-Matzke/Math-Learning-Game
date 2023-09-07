import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class MathGame {
    // GAME CONTROL VARIABLES
    // Operands Per Game
    private final int OPERAND_COUNT = 3;
    // Max and Min Operand Values
    private final int MAX = 6 + 1;
    private final int MIN = 3;
    // Font Size
    private int FONT_SIZE = 45;

    // Gui Components
    private JPanel pnlMain;
    private JLabel[] labels = new JLabel[OPERAND_COUNT];
    private JTextField[] textFields = new JTextField[OPERAND_COUNT - 1];

    // Functional Variables
    private String solution = randomSolution();
    private int correctAns;
    private int middle = OPERAND_COUNT - 1;

    public MathGame() {
        pnlMain = new JPanel();
        pnlMain.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // ROW 0
        JLabel title = new JLabel("Math!!!!");
        title.setForeground(Color.ORANGE);
        title.setFont(new Font("Courier", Font.BOLD, FONT_SIZE * 2));
        gbc.gridx = middle * 2;
        gbc.gridy = 0;
        gbc.gridwidth = Math.max(OPERAND_COUNT, 5);
        gbc.insets = new Insets(5, 0, 150, 0);
        gbc.weightx = 1;
        pnlMain.add(title, gbc);

        // ROW 1
        for (int i = 0; i < OPERAND_COUNT * 2 - 1; i++) {
            gbc = new GridBagConstraints();
            if (i % 2 == 0) {
                labels[i / 2] = new JLabel("" + randomNumber(MAX, MIN));
                gbc.gridx = i * 2;
                gbc.gridy = 1;
                gbc.insets = new Insets(10, 5, 10, 5);
                gbc.weightx = 1;
                labels[i / 2].setFont(new Font("Courier", Font.BOLD, FONT_SIZE));
                labels[i / 2].setForeground(Color.BLACK);
                pnlMain.add(labels[i / 2], gbc);
            } else {
                textFields[i / 2] = new JTextField(1);
                gbc.gridx = i * 2;
                gbc.gridy = 1;
                gbc.insets = new Insets(10, 5, 10, 5);
                gbc.weightx = 1;
                textFields[i / 2].setBackground(Color.LIGHT_GRAY);
                textFields[i / 2].setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK, 2));
                textFields[i / 2].setToolTipText("Enter operators here or click a button below");
                textFields[i / 2].setFont(new Font("Courier", Font.BOLD, FONT_SIZE));
                textFields[i / 2].setForeground(Color.BLACK);
                pnlMain.add(textFields[i / 2], gbc);
            }
        }

        correctAns = mathSolver(solutionExpression(labels));
        System.out.println(solutionExpression(labels));
        JLabel answer = new JLabel("= " + correctAns);
        answer.setForeground(Color.BLACK);
        answer.setFont(new Font("Courier", Font.BOLD, FONT_SIZE));
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = OPERAND_COUNT * 4;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 10, 10, 5);
        gbc.weightx = 1;
        pnlMain.add(answer, gbc);

        // ROW 2
        JLabel isCorrect = new JLabel("");
        isCorrect.setFont(new Font("Courier", Font.BOLD, FONT_SIZE * 2 / 3));
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = (middle + 2) * 2;
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.weightx = 1;
        pnlMain.add(isCorrect, gbc);

        JButton resetButton = new JButton("RESET");
        resetButton.setFocusPainted(false);
        resetButton.setBackground(Color.RED);
        resetButton.setBorderPainted(false);
        resetButton.setFont(new Font("Courier", Font.BOLD, FONT_SIZE));
        gbc = new GridBagConstraints();
        gbc.gridx = middle * 2;
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 0, 10, 5);
        gbc.weightx = 1;

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetLabels(MAX, MIN);
                solution = randomSolution();
                correctAns = mathSolver(solutionExpression(labels));
                System.out.println(solutionExpression(labels));
                answer.setText("= " + correctAns);
                isCorrect.setText("");
                for (JTextField field : textFields)
                    field.setText("");
            }
        });
        pnlMain.add(resetButton, gbc);

        JButton checkButton = new JButton("CHECK");
        checkButton.setFocusPainted(false);
        checkButton.setBackground(Color.GREEN);
        checkButton.setBorderPainted(false);
        checkButton.setFont(new Font("Courier", Font.BOLD, FONT_SIZE));
        gbc = new GridBagConstraints();
        gbc.gridx = (middle + 1) * 2;
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 5, 10, 0);
        gbc.weightx = 1;

        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String posAnswer = getExpression(labels, textFields);
                if (posAnswer.equals("false")) {
                    isCorrect.setText("INVALID");
                    return;
                }

                int answer = mathSolver(posAnswer);
                if (answer == correctAns)
                    isCorrect.setText("CORRECT");
                else
                    isCorrect.setText("INCORRECT: " + answer);
            }
        });
        pnlMain.add(checkButton, gbc);

        // ROW 3
        JButton addBtn = new JButton("+");
        addBtn.setFocusPainted(false);
        addBtn.setContentAreaFilled(false);
        addBtn.setBorderPainted(false);
        addBtn.setFont(new Font("Courier", Font.BOLD, FONT_SIZE * 2 / 3));
        gbc = new GridBagConstraints();
        gbc.gridx = (middle - 1) * 2 - 1;
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 5, 10, 0);
        gbc.weightx = 1;

        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fillFirstEmptyField('+');
            }
        });
        pnlMain.add(addBtn, gbc);

        JButton subBtn = new JButton("-");
        subBtn.setFocusPainted(false);
        subBtn.setContentAreaFilled(false);
        subBtn.setBorderPainted(false);
        subBtn.setFont(new Font("Courier", Font.BOLD, FONT_SIZE * 2 / 3));
        gbc = new GridBagConstraints();
        gbc.gridx = (middle) * 2 - 1;
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 5, 10, 0);
        gbc.weightx = 1;

        subBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fillFirstEmptyField('-');
            }
        });
        pnlMain.add(subBtn, gbc);

        JButton multBtn = new JButton("x");
        multBtn.setFocusPainted(false);
        multBtn.setContentAreaFilled(false);
        multBtn.setBorderPainted(false);
        multBtn.setFont(new Font("Courier", Font.BOLD, FONT_SIZE * 2 / 3));
        gbc = new GridBagConstraints();
        gbc.gridx = (middle + 1) * 2 - 1;
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 5, 10, 0);
        gbc.weightx = 1;

        multBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fillFirstEmptyField('x');
            }
        });
        pnlMain.add(multBtn, gbc);

        JButton divBtn = new JButton("/");
        divBtn.setFocusPainted(false);
        divBtn.setContentAreaFilled(false);
        divBtn.setBorderPainted(false);
        divBtn.setFont(new Font("Courier", Font.BOLD, FONT_SIZE * 2 / 3));
        gbc = new GridBagConstraints();
        gbc.gridx = (middle + 2) * 2 - 1;
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 5, 10, 0);
        gbc.weightx = 1;

        divBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fillFirstEmptyField('/');
            }
        });
        pnlMain.add(divBtn, gbc);

        JButton backspaceBtn = new JButton("BACK");
        backspaceBtn.setFocusPainted(false);
        backspaceBtn.setContentAreaFilled(false);
        backspaceBtn.setBorderPainted(false);
        backspaceBtn.setFont(new Font("Courier", Font.BOLD, FONT_SIZE * 2 / 3));
        gbc = new GridBagConstraints();
        gbc.gridx = (middle + 3) * 2 - 1;
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 5, 10, 0);
        gbc.weightx = 1;

        backspaceBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backspaceButton();
            }
        });
        pnlMain.add(backspaceBtn, gbc);

    }

    public void backspaceButton() {
        for (int i = OPERAND_COUNT - 2; i >= 0; i--) {
            if (!textFields[i].getText().equals("")) {
                textFields[i].setText("");
                return;
            }
        }
    }

    public void fillFirstEmptyField(char x) {
        for (int i = 0; i < OPERAND_COUNT - 1; i++) {
            if (textFields[i].getText().equals("")) {
                textFields[i].setText("" + x);
                return;
            }
        }
    }

    public void resetLabels(int max, int min) {
        for (int i = 0; i < OPERAND_COUNT; i++)
            labels[i].setText("" + randomNumber(max, min));
    }

    public String getExpression(JLabel[] labels, JTextField[] textFields) {
        String operators = "+-x/";
        String toRet = labels[0].getText();
        for (int i = 0; i < OPERAND_COUNT - 1; i++) {
            if (!operators.contains(textFields[i].getText()) || textFields[i].getText().length() != 1)
                return "false";
            toRet += textFields[i].getText() + "" + labels[i + 1].getText();
        }
        return toRet;
    }

    public int randomNumber(int max, int min) {
        Random r = new Random();
        return r.nextInt(max - min) + min;
    }

    public String randomSolution() {
        Random r = new Random();
        String operators = "+-x/";
        String toRet = "";
        for (int i = 0; i < OPERAND_COUNT - 1; i++)
            toRet += "" + operators.charAt(r.nextInt(4));
        return toRet;
    }

    public String solutionExpression(JLabel[] labels) {
        String toRet = labels[0].getText();
        for (int i = 0; i < OPERAND_COUNT - 1; i++) {
            toRet += solution.charAt(i) + "" + labels[i + 1].getText();
        }
        return toRet;
    }

    public int mathSolver(String expression) {
        String operators = "+-x/";
        String recompile = "";
        ArrayList<Integer> answers = new ArrayList<>();
        if (expression.contains("x") || expression.contains("/")) {
            for (String x : expression.split("[+-]")) {
                boolean isSolo = true;
                for (char y : x.toCharArray())
                    if (operators.contains("" + y))
                        isSolo = false;
                if (isSolo) {
                    answers.add(Integer.parseInt(x.trim()));
                    continue;
                }
                ArrayList<Integer> opIndexes = new ArrayList<>();
                for (int i = 0; i < x.length(); i++)
                    if (x.charAt(i) == 'x' || x.charAt(i) == '/')
                        opIndexes.add(i);
                ArrayList<Integer> answersLower = new ArrayList<>();
                answersLower.add(Integer.parseInt(x.substring(0, opIndexes.get(0))));
                while (opIndexes.size() > 0) {
                    int n1 = answersLower.get(0);
                    int n2 = Integer.parseInt(x.substring(opIndexes.get(0) + 1,
                            opIndexes.size() > 1 ? opIndexes.get(1) : x.length()));
                    char op = x.charAt(opIndexes.get(0));
                    int solution = calculator(n1, n2, op);
                    opIndexes.remove(0);
                    answersLower.set(0, solution);
                }

                answers.add(answersLower.get(0));

            }
            recompile = "" + answers.get(0);
            String ops = "";
            for (char x : expression.toCharArray())
                if (x == '+' || x == '-')
                    ops += x;
            for (int i = 0; i < ops.length(); i++)
                recompile += ops.charAt(i) + "" + answers.get(i + 1);
        } else
            recompile = expression;

        boolean hasOperators = false;
        for (char x : recompile.toCharArray())
            if (operators.contains("" + x))
                hasOperators = true;

        if (hasOperators) {
            ArrayList<Integer> opIndexes = new ArrayList<>();
            for (int i = 0; i < recompile.length(); i++)
                if (operators.contains("" + recompile.charAt(i)))
                    opIndexes.add(i);
            int sum = Integer.parseInt(recompile.substring(0, opIndexes.get(0)));
            while (opIndexes.size() > 0) {
                int n2 = Integer.parseInt(recompile.substring(opIndexes.get(0) + 1,
                        opIndexes.size() > 1 ? opIndexes.get(1) : recompile.length()));
                char op = recompile.charAt(opIndexes.get(0));
                sum = calculator(sum, n2, op);
                opIndexes.remove(0);
            }
            return sum;
        } else {
            return Integer.parseInt(recompile);
        }
    }

    public int calculator(int a, int b, char c) {
        switch (c) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case 'x':
                return a * b;
            case '/':
                return a / b;
            default:
                return -1;
        }
    }

    public JPanel getUI() {
        return pnlMain;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Math Game");
                frame.getContentPane().add(new MathGame().getUI()).setBackground(Color.GRAY);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setExtendedState(Frame.MAXIMIZED_BOTH);
                frame.setVisible(true);
            }
        });
    }

}