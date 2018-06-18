package FrontEnd;

import com.sun.corba.se.impl.orbutil.graph.Graph;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GameUI {

    private static int boardSize = 3; //testing
    private JButton playButton;
    private JButton undoButton;
    private JComboBox Dir;
    private JComboBox YCoor;
    private JComboBox XCoor;
    private JPanel Container;
    private JPanel Drawable;


    public GameUI() {
        populateComboBox(XCoor);
        populateComboBox(YCoor);
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                pressClearButton();
            }
        });
        //Drawable = new MyPaint();
    }

    private void pressClearButton() {
        this.XCoor.setSelectedIndex(0);
        XCoor.repaint();
        this.YCoor.setSelectedIndex(0);
        YCoor.repaint();
        this.Dir.setSelectedIndex(0);
    }


    public static void main(String[] args) {
        GameUI ui = new GameUI();
        JFrame frame = new JFrame("GameUI");
        frame.setContentPane(ui.Container);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        //ui.Drawable.setPreferredSize(new Dimension(400,400));
        ui.Drawable.setBackground(Color.LIGHT_GRAY);
        ui.paintBoard(ui.Drawable);
        //ui.createPoints(ui.Drawable);
    }

    private void paintBoard(JPanel panel){
        Graphics g = panel.getGraphics();
        panel.repaint();
        panel.setBackground(Color.YELLOW);
        g.setColor(Color.BLUE);
        g.clearRect(10,10,100,100);
        panel.paint(g);
        //panel.
        panel.repaint();
    }

    private void populateComboBox(JComboBox comboBox) {
        String aux[] = new String[boardSize];
        for (int i = 0; i < boardSize; i++) {
            aux[i] = Integer.toString(i);
        }
        ComboBoxModel cbm = new ComboBoxModel() {

            String[] data = aux;
            String selection = data[0];

            @Override
            public void setSelectedItem(Object o) {
                selection = (String) o;
            }

            @Override
            public Object getSelectedItem() {
                return selection;
            }

            @Override
            public int getSize() {
                return data.length;
            }

            @Override
            public Object getElementAt(int i) {
                return data[i];
            }

            @Override
            public void addListDataListener(ListDataListener listDataListener) {
            }

            @Override
            public void removeListDataListener(ListDataListener listDataListener) {
            }
        };

        comboBox.setModel(cbm);
        comboBox.setMaximumRowCount(aux.length);
    }


    private void createPoints(JPanel p) {
        Graphics g = p.getGraphics();
        p.paintComponents(g);
        //g.clearRect(0,0, 600,600);
        g.setColor(Color.BLACK);
        g.fillRect(100, 100, 100, 100);

        p.repaint();
    }

}
