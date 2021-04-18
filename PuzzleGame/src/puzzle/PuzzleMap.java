
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author AnhTu
 */
public class PuzzleMap {

    Puzzle p;
    private JPanel pnLayout;
    private JLabel lbTime;
    private JLabel lbCount;
    public boolean isStarting = false;
    private ArrayList<JButton> arrBtn;
    private int numMove = 0;
    private Timer t;
    private int second;

    public PuzzleMap(JPanel pnLayout, JLabel lbTime, JLabel lbCount) {
        this.pnLayout = pnLayout;
        this.lbTime = lbTime;
        this.lbCount = lbCount;
    }

    public void setUp() {
        p.lbCount.setText("0");
        p.lbTime.setText("0");
    }

    public void countTime() {
        if (t != null) {
            t.stop();
        }

        second = 0;
        t = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                second++;
                /*if (second == 60) {
                    second = 0;
                    minutes++;
                }*/
                DecimalFormat df = new DecimalFormat("0");
                lbCount.setText(df.format(second));
            }
        });
        t.start();
    }

    private boolean checkMove(JButton btn, int size) {
        if (btn.getText().equals("")) {
            return false;
        }
        int startCol = 0;
        int startRow = 0;
        int desCol = 0;
        int desRow = 0;
        // get vi tri
        for (int i = 0; i < arrBtn.size(); i++) {
            if (arrBtn.get(i).getText().equals(btn.getText())) {
                startCol = i % size;
                startRow = i / size;
            }
            if (arrBtn.get(i).getText().equals("")) {
                desCol = i % size;
                desRow = i / size;
            }
        }
        if (startCol == desCol) {
            if (startRow == (desRow - 1) || startRow == (desRow + 1)) {
                return true; // move up or down
            }
        }
        if (startRow == desRow) {
            if (startCol == (desCol - 1) || startCol == (desCol + 1)) {
                return true; // move left or right
            }
        }
        return false;
    }
//count button move

    private void moveBtn(JButton btn) { // move Button
        for (int i = 0; i < arrBtn.size(); i++) {
            if (arrBtn.get(i).getText().equals("")) {
                arrBtn.get(i).setText(btn.getText());
                break;
            }
        }
        btn.setText("");
        numMove++;
        lbTime.setText(String.valueOf(numMove));
    }

    private ArrayList randomMatrix(int size) { // return a random array
        ArrayList<String> data = new ArrayList<>();
        ArrayList<Integer> d = new ArrayList<>();
        Random rd = new Random();
        int num = size * size - 1;
        for (int i = 0; i < num; i++) {
            d.add(i + 1);
        }
        int dem = 0;
        int s = 0;
        
        int emptyBox = rd.nextInt(num);
        do {
            Collections.shuffle(d);
            s = 0;
            
            for (int i = 0; i < num; i++) {
                for (int j = i + 1; j < num; j++) {
                    if (d.get(i).compareTo(d.get(j)) > 0) {
                        dem++;
                    }
                }
                s = s + dem;
                dem = 0;
            }
            if (size % 2 == 1 && s > 0 && s % 2 == 0) {
                break;
            }
            if (size % 2 == 0) {
                if (emptyBox / size == 1 && s > 0 && s % 2 == 0) { //  hang chan thi tong chan 
                    break;
                }
                if (emptyBox / size == 0 && s > 0 && s % 2 == 1) { // hang le thi tong le
                    break;
                }
            }
        } while (true);
        System.out.println(emptyBox / size);
        System.out.println("sss=" + s);
        for (int i = 0; i < emptyBox; i++) {
            data.add(String.valueOf(d.get(i)));
        }
        data.add("");
        for (int i = emptyBox; i < num; i++) {
            data.add(String.valueOf(d.get(i)));
        }
        return data;
    }

    public void initButon(int size) { // khởi tạo button ban đầu
        arrBtn = new ArrayList<>();
        ArrayList<String> data = new ArrayList<>();
        
        data = randomMatrix(size);
        double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 100;

        int btSize = (int) height / size;
        pnLayout.removeAll();
        pnLayout.setLayout(new GridLayout(size, size, 10, 10));

        for (int i = 0; i < data.size(); i++) {
            JButton btn = new JButton(data.get(i));
            btn.setFont(new Font("Arial", Font.PLAIN, btSize / 4));
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (isStarting == true) {
                        if (checkMove(btn, size)) {
                            moveBtn(btn);
                            if (checkWin()) {
                                t.stop();
                                isWon();
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(pnLayout, "Please enter new game to start");
                    }
                }
            });

            pnLayout.add(btn);
            arrBtn.add(btn);
        }

    }

    private boolean checkWin() {
        ArrayList<String> btnText = new ArrayList<>();
        for (int i = 0; i < arrBtn.size() - 1; i++) {
            btnText.add(arrBtn.get(i).getText());
        }
        for (int i = 0; i < btnText.size(); i++) {
            for (int j = i; j < btnText.size(); j++) {
                if (btnText.get(j).equals("")) {
                    return false;
                }
                if (Integer.parseInt(btnText.get(j)) < Integer.parseInt(btnText.get(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    // set panel
    private void setWindowsSize(int size) {

        double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 100;
//        int minButton = 60;
//        int heightButton = (int) height/minButton;
//        for (int i = 3; i < heightButton; i++) {
//            p.cmbSize.addItem((i + "x" + i));
//        }
        int btSize = (int) height / size;
        //pnLayout.setSize(new Dimension(widthButton*num, heightButton*num));
//        pnLayout.setLayout(new GridLayout(size,size,10,10));
        pnLayout.setPreferredSize(new Dimension(btSize * size, btSize * size));
        //pnLayout.setBackground(Color.red);
        p.setResizable(false);
        p.pack();

        System.out.println(pnLayout.getSize());
    }

    public void addCombobox() {
        double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 100;
        int minButton = 60;
        int heightButton = (int) height / minButton;
        for (int i = 3; i < heightButton; i++) {
            p.cmbSize.addItem((i + "x" + i));
        }
    }

    public void startMatrix() {
        String s = p.cmbSize.getSelectedItem().toString();
        String[] temp = s.split("x");
        initButon(Integer.parseInt(temp[0]));
        setWindowsSize(Integer.parseInt(temp[0]));

    }

    private void isWon() {
        JOptionPane.showMessageDialog(p, "You Won!");
    }

    public void newGame() {
        t.stop();
        int confirm = JOptionPane.showConfirmDialog(null, "Do you must be want to make new game?", "New Game", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            setUp();
            countTime();
            startMatrix();
            numMove = 0;
        } else if (confirm == JOptionPane.NO_OPTION) {
            t.start();
        }

    }

}
