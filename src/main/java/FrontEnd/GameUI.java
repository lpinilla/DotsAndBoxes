package FrontEnd;

import javax.swing.*;
import javax.swing.event.ListDataListener;

public class GameUI {

    private static int boardSize = 2; //testing
    private JButton playButton;
    private JButton clearButton;
    private JComboBox Dir;
    private JComboBox YCoor;
    private JComboBox XCoor;
    private JPanel Container;
    private JPanel Drawable;


    public GameUI(){
        populateComboBox(XCoor);
        populateComboBox(YCoor);
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("GameUI");
        frame.setContentPane(new GameUI().Container);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void populateComboBox(JComboBox comboBox){
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


    private void createPoints(JPanel p){

    }
}
