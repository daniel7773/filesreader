package filesreader;

import javax.swing.*;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import javax.swing.JTextArea;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.awt.event.*;
import java.io.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.apache.commons.io.FileUtils;

/**
 *
 * @author Данил
 */
public class FilesReader implements ActionListener {

    /**
     * @param args the command line arguments
     */
    
    JTextField jte; // Extension of files
    JTextField jtt; // Text to search
    JTextField jtd; // Directory
    JButton Enter = new JButton("Enter");
    JScrollPane pane = new JScrollPane();
    JTextArea area = new JTextArea();
    
    public FilesReader() {
        
        JFrame jfrm = new JFrame("Поиск файлов по их формату");
        jfrm.setLocationRelativeTo(null);
        jfrm.setLayout(new FlowLayout());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // задаю размеры окна, чтобы корректно отображалось на любому устройстве
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();

        int h = (int) height;
        int w = (int) width;
        h = h / 2;
        w = w / 2;

        jfrm.setSize(w, h);

        jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jfrm.setLayout(null);

        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createEmptyBorder(4, 4, 80, 4));
        area.setSize(new Dimension(350, 300));
        pane.getViewport().add(area);
        area.setText("Тут будет текст из файла");

        jtt = new JTextField(10); // поле для ввода текста, который будет искаться в файлах
        jtt.setText("Искомый текст");
        jtt.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                if (jtt.getText().trim().equals("Искомый текст")) {
                    jtt.setText("");
                } else {
                    jtt.setText(jtt.getText());
                }
            }

            public void focusLost(FocusEvent e) {
                if (jtt.getText().trim().equals("")) {
                    jtt.setText("Искомый текст");
                } else {
                    jtt.setText(jtt.getText());
                }
            }
        });

        jte = new JTextField(10); // поле для задачи типа файлов 
        jte.setText("Тип файла");
        jte.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                if (jte.getText().trim().equals("Тип файла")) {
                    jte.setText("log");
                } else {
                    jte.setText(jte.getText());
                }
            }

            public void focusLost(FocusEvent e) {
                if (jte.getText().trim().equals("")) {
                    jte.setText("log");
                } else {
                    jte.setText(jte.getText());
                }
            }
        });

        jtd = new JTextField(15); // поле для задачи "area" поиска файлов
        jtd.setText("Укажите вашу директорию");
        jtd.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                if (jtd.getText().trim().equals("Укажите вашу директорию")) {
                    jtd.setText("");
                } else {
                    jtd.setText(jtd.getText());
                }
            }

            public void focusLost(FocusEvent e) {
                if (jtd.getText().trim().equals("")) {
                    jtd.setText("Укажите вашу директорию");
                } else {
                    jtd.setText(jtd.getText());
                }
            }
        });

        jtt.setActionCommand("SearchText");
        jte.setActionCommand("FileExtension");
        jtd.setActionCommand("SearchDirectory");


             
        Enter.addActionListener(this);

        jfrm.add(jtt); // я понимаю, что код будет проверяться на компьютере и в силу того, что дисплеи более менее одинаковы это было не обязательно, однако всё же решил сделать это, как вариант исключение потенциальных ошибок
        jtt.setBounds(w / 25, (int) (h * 0.01), w / 7, h/10);
        jfrm.add(jte);
        jte.setBounds(w / 25 + w / 7, (int) (h * 0.01), w / 7, h/10);
        jfrm.add(jtd);
        jtd.setBounds((int) (w / 25 + w / 3.5), (int) (h * 0.01), w / 7, h/10);
        jfrm.add(Enter);
        Enter.setBounds(w / 5, (int) (h * 0.5), w / 7, h/10);
        jfrm.add(new JScrollPane(pane));
        jfrm.add(pane);
        pane.setBounds(w / 2, (int) (h * 0.01), (int) (w * 0.45), (int) (h*0.6));
        jfrm.setVisible(true);
        
    }

    static String Arr[]; // массив для дерева

    public void actionPerformed(ActionEvent ae) {

        File dir = new File(jtd.getText());
        String[] extensions = new String[]{jte.getText()};

        List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, true); // использование метода из библиотеки Apache
        List<File> flist = new ArrayList<>();
        if (jtt.getText().equals("") || jtt.getText().equals("Искомый текст")) { // проверка введен ли текст для поиска
        } else {
            for (File file : files) {
                try {
                    StringBuilder sb = new StringBuilder("Тест");
                    BufferedReader in = new BufferedReader(new FileReader(file.getAbsoluteFile()));
                    try {
                        String s;
                        while ((s = in.readLine()) != null) {
                            sb.append(s);
                            sb.append("\n");
                        }
                        if (sb.toString().contains(jtt.getText())) {
                        } else {
                            flist.add(file);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        in.close();
                    }

                } catch (IOException ex) {
                    Logger.getLogger(FilesReader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        files.removeAll(flist);
        /*
                                исключение из списка, полученного методом библиотеки Apache всех файлов 
                                подходящего расширения, но без искомого текста 
         */
        int ListOfFiles = files.size(); // переменная для того, чтобы корректно задать размер массива
        try {
            finalize();
        } catch (Throwable ex) {
            Logger.getLogger(FilesReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (ListOfFiles > 0) {
            String Arr[] = new String[ListOfFiles];

            int i = 0;
            for (File file : files) {
                try {
                    System.out.println("file: " + file.getCanonicalPath());

                    Arr[i] = file.getCanonicalPath();
                    i++;
                } catch (IOException ex) {
                    Logger.getLogger(FilesReader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            for (String Str : Arr) {  // цикл для просмотра того, что содержится в списке, чисто для разработки
                System.out.println(Str);
            }
            int Counter = 0;
            int BiggestCounter = 0; // Переменная хранящая наибольшее кол-во символов "/" в строке
            int p = 1; // кол-во символов в строке
            int[][] InfoArr = new int[Arr.length][p + 1]; // массив, хранящий информацию о кол-ве символов "/" в строке
            // [номер строки в исходном массиве][кол-во символов "/"]

            for (char element : Arr[0].toCharArray()) {  // начало сортировки массива, чтобы понять как расположены строки в списке по кол-ву символов /
                if (element == '\\') {
                    BiggestCounter++;
                }
            }
            for (i = 0; i < Arr.length; i++) {
                for (char element : Arr[i].toCharArray()) {
                    if (element == '\\') {
                        Counter++;
                    }
                }
                if (Counter > BiggestCounter) {
                    BiggestCounter = Counter;
                }
                InfoArr[i][0] = i;
                InfoArr[i][1] = Counter;
                Counter = 0;
            }

            for (i = InfoArr.length - 1; i > 0; i--) { // сортировка методом пузырька
                for (int j = 0; j < i; j++) {

                    if (InfoArr[j][1] > InfoArr[j + 1][1]) {
                        int changer0 = InfoArr[j][0];
                        int changer1 = InfoArr[j][1];
                        InfoArr[j][0] = InfoArr[j + 1][0];
                        InfoArr[j][1] = InfoArr[j + 1][1];
                        InfoArr[j + 1][0] = changer0;
                        InfoArr[j + 1][1] = changer1;
                    }
                }
            }

            JFrame frame = new JFrame("Дерево");
            JTree tree;
            DefaultTreeModel treeModel;

            DefaultMutableTreeNode root = new DefaultMutableTreeNode(Arr[0].substring(0, Arr[0].indexOf('\\')));
            int k = Arr[InfoArr[0][0]].indexOf('\\'); // получение самого первого элемента "/" для корректного задания имени исходной папки
            DefaultMutableTreeNode changer = root;

            for (i = 0; i < Arr.length; i++) { //создание ветвей дерева
                if (i > 0) { //помещает конечный файл в ветвь
                    DefaultMutableTreeNode file = new DefaultMutableTreeNode(Arr[InfoArr[i][0]].substring(Arr[InfoArr[i][0]].lastIndexOf('\\')));
                    changer.add(file);
                }
                changer = root;
                for (int j = 0; j < InfoArr[i][1]; j++) {
                    DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(Arr[InfoArr[i][0]].substring(Arr[InfoArr[i][0]].indexOf(changer.toString()), Arr[InfoArr[i][0]].indexOf('\\', k)));

                    changer.add(childNode);
                    changer = childNode;
                    k = Arr[InfoArr[i][0]].indexOf('\\', k + 1);
                }
            }
            treeModel = new DefaultTreeModel(root);
            tree = new JTree(treeModel);

            frame.add(tree);
            frame.add(new JScrollPane(tree));

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("Дерево");
            frame.pack();
            frame.setVisible(true);
            tree.addTreeSelectionListener(new TreeSelectionListener() {

                @Override
                public void valueChanged(TreeSelectionEvent e) {
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                    if (selectedNode == null) {
                        return;
                    }

                    if (selectedNode.toString().contains(".") && selectedNode.isLeaf()) {
                        BufferedReader in2 = null;
                        try {
                            for (int z = 0; z < Arr.length; z++) {
                                if (Arr[z].contains(selectedNode.toString())) {
                                    in2 = new BufferedReader(new FileReader(Arr[0]));
                                }
                            }
                            StringBuilder sb = new StringBuilder("");
                            String s;
                            while ((s = in2.readLine()) != null) {
                                sb.append(s);
                                sb.append("\n");
                            }
                            String q = sb.toString();
                            area.setText(q);
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(FilesReader.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(FilesReader.class.getName()).log(Level.SEVERE, null, ex);
                        } finally {
                            try {
                                in2.close();
                            } catch (IOException ex) {
                                Logger.getLogger(FilesReader.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            });

            for (i = 0; i < InfoArr.length; i++) { // проверка сортировки
                System.out.print(InfoArr[i][0]);
                System.out.println(InfoArr[i][1]);
            }

        } else {
            System.out.println("По введенному запросу совпадений не найдено!");
        }
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> {
            new FilesReader();
        });
    }
}
