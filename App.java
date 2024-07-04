import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.*;

public class App {
    private JLabel label;
    private JFrame frame;
    public App(){
        // Create a new JFrame

        String[] symb = {"7", "8", "9", "S","D", "4", "5", "6", "*","/", "1", "2","3","+","-","0",",","^2","^3","="};
        assert symb.length==16;
        this.frame = new JFrame("Calculatrice");
        
        // Set the size of the frame
        this.frame.setResizable(false);
        this.frame.setMinimumSize(new Dimension(300, 300));
        //frame.setMaximumSize(new Dimension(400, 400));
        
        // Set default close operation
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create a label
        this.label = new JLabel(" ");
        
        //label.setSize(new Dimension(400,25));
        this.label.setHorizontalAlignment(SwingConstants.LEFT);
        
        this.label.setBackground(Color.WHITE);
        this.label.setOpaque(true);
        EmptyBorder paddingBorder = new EmptyBorder(0, 10, 0, 0);
        
        LineBorder blackBorder = new LineBorder(Color.BLACK,5);

        // Create a compound border with black border and empty border
        CompoundBorder compoundBorder = new CompoundBorder(blackBorder, paddingBorder);

        // Set the compound border to the label
        this.label.setBorder(compoundBorder);
        this.label.setPreferredSize(new Dimension(300, 35));
        JPanel mainpanel = new JPanel(new BorderLayout());
        
        JPanel bpanel = new JPanel(new GridLayout(4, 5)); // 4 rows, 5 columns

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 5; col++) {
                int i = row * 5 + col;
                JButton button = new JButton(symb[i]);
                button.setPreferredSize(new Dimension(50, 50)); // Set specific size for each button
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Action to be performed when the button is clicked
                        if (button.getText().equals("=")){
                            exec(label.getText());
                        }
                        else if (button.getText().equals("S")){
                            label.setText(" ");
                        }
                        else if (button.getText().equals("D")){
                            delete();
                        }
                        else if (!button.getText().isEmpty()){
                            label.setText(label.getText()+button.getText());
                        }
                        
                    }
                });
                bpanel.add(button);
            }
        }
        
        
        
        
        // Create a panel
        
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        // Add components to the panel
        panel.add(bpanel);

        mainpanel.add(label,BorderLayout.NORTH);
        mainpanel.add(panel);
        
        // Add the panel to the frame
        this.frame.getContentPane().add(mainpanel);
        
        // Set the frame to be visible
        this.frame.setVisible(true);
    }

    public void exec(String ch){
        List<String> values = new ArrayList<>();
        String mot="";
        for (char car :ch.toCharArray()){
            if (car == '+' || car=='-' || car =='*' || car =='/' || car=='^'){
                values.add(mot);
                values.add(String.valueOf(car));
                mot="";
            }
            else if (car ==','){
                mot = mot+".";
            }
            else{
                mot = mot + String.valueOf(car);
            }
        };

        if (!mot.equals("")){
            values.add(mot);
            mot="";
        }

        while (values.contains(("^"))){
            for (int i=0; i< values.size();i++){
                if (values.get(i).equals("^")){
                    if (i!=0 && (i+1<values.size())){
                        String val = this.eval(values.get(i-1),values.get(i),values.get(i+1));
                        if (val.equals("PointError")){
                            JOptionPane.showMessageDialog(this.frame, "Format Error : to many ','");
                            return ;
                        }
                        values.set(i-1, val);
                        values.remove(i);
                        values.remove(i);
                        break;
                    }
                    else{
                        JOptionPane.showMessageDialog(this.frame, "Syntax Error : Invalid syntax");
                        return ;
                    }
                }
            }
        }

        while (values.contains("*") || values.contains("/")){
            for (int i=0; i< values.size();i++){
                if (values.get(i).equals("*")||values.get(i).equals("/")){
                    if (i!=0 && (i+1<values.size())){
                        String val = this.eval(values.get(i-1),values.get(i),values.get(i+1));
                        if (val.equals("Null")){
                            JOptionPane.showMessageDialog(this.frame, "Math Error : Not able to divie by 0");
                            return ;
                        }
                        else if (val.equals("PointError")){
                            JOptionPane.showMessageDialog(this.frame, "Format Error : to many ','");
                            return ;
                        }
                        values.set(i-1, val);
                        values.remove(i);
                        values.remove(i);
                        break;
                    }
                    else{
                        JOptionPane.showMessageDialog(this.frame, "Syntax Error : Invalid syntax");
                        return ;
                    }
                }
            }
        }


        while (values.contains("+") || values.contains("-")){
            for (int i=0; i< values.size();i++){
                if (values.get(i).equals("+")||values.get(i).equals("-")){
                    if ((i+1<values.size())){
                        if (i==0){
                            String val = this.eval("0",values.get(i),values.get(i+1));
                            if (val.equals("PointError")){
                                JOptionPane.showMessageDialog(this.frame, "Format Error : to many ','");
                                return ;
                            }
                            values.set(i, val);
                            values.remove(i+1);
                        }
                        else{
                            String val = this.eval(values.get(i-1),values.get(i),values.get(i+1));
                            if (val.equals("PointError")){
                                JOptionPane.showMessageDialog(this.frame, "Format Error : to many ','");
                                return ;
                            }
                            values.set(i-1, val);
                            values.remove(i);
                            values.remove(i);
                        }
                        break;
                    }
                    else{
                        JOptionPane.showMessageDialog(this.frame, "Syntax Error : Invalid syntax");
                    }
                }
            }
        }

        assert values.size()==1;
        String v=values.get(0);
        v=v.replace(".",",");
        while (v.endsWith("0")){
            v = v.substring(0, v.length()-1);
        }
        if (v.endsWith(",")){
            v = v.substring(0, v.length()-1);
        }
        this.label.setText(v);
    }

    public String eval(String thing1, String op, String thing2){
        Float v1,v2,r;
        
        if (!this.valid(thing1) || !this.valid(thing2)){
            return "PointError";
        }
        
        r=Float.valueOf(0);
        v1=Float.valueOf(thing1);
        v2=Float.valueOf(thing2);
        
        if (op.equals("^")){
            double dr=Math.pow(v1,v2);
            float fr = (float)dr;
            r=Float.valueOf(fr);
        }
        else if (op.equals("*")){
            r = v1 * v2;
        }
        else if (op.equals("/")){
            if (v2.equals(Float.valueOf(0))){
                return "Null";
            }
            r=v1/v2;
        }
        else if (op.equals("+")){
            r = v1+v2;
        }
        else{
            r = v1-v2;
        }

        return String.valueOf(r);
    }

    public boolean valid(String thing){
        int count=0;
        for (int i = 0; i < thing.length(); i++) {
            // Check if the current character matches the target character
            if (thing.charAt(i) == '.') {
                count++; // Increment count if match found
            }
            if (count>=2){
                return false;
            }
        }
        return true;
    }

    public void delete(){
        if (this.label.getText().equals(" ")){return;}
        this.label.setText(this.label.getText().substring(0, this.label.getText().length()-1));
    }
}

