/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thesaurusEditor.gui;
import javax.swing.JButton;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import java.awt.Component;

/**
 *
 * @author sopr058
 */
public class OptionPane extends javax.swing.JOptionPane
{
    public JOptionPane pane;
    public OptionPane(Component ParentComponent, Object message, String title, String[] options)
    {
        pane = new JOptionPane();
        pane.setMessage(message);
        pane.setMessageType(JOptionPane.QUESTION_MESSAGE);
        Icon icoJa = new ImageIcon(getClass().getResource("/thesaurusEditor/img/dialog_ok_apply.png"));
        Icon icoNein = new ImageIcon(getClass().getResource("/thesaurusEditor/img/delete.png"));
        Icon icoCancel = new ImageIcon(getClass().getResource("/thesaurusEditor/img/dialog_cancel.png"));
        if(options.length == 1)
        {
            JButton btnJa = getButton(pane, options[0], icoJa);
            JButton btnCancel = getButton(pane, "Abbrechen", icoCancel);
            pane.setOptions(new Object[] { btnJa, btnCancel });
        }
        else
        {
            JButton btnJa = getButton(pane, options[0], icoJa);
            JButton btnNein = getButton(pane, options[1], icoNein);
            JButton btnCancel = getButton(pane, "Abbrechen", icoCancel);
            pane.setOptions(new Object[] { btnJa, btnNein, btnCancel });
        }
        JDialog dialog = pane.createDialog(ParentComponent, title);
        dialog.setVisible(true);

    }

    public static JButton getButton(final JOptionPane optionPane, String text, Icon icon)
            {
                final JButton button = new JButton(text, icon);
                ActionListener actionListener = new ActionListener()
                {
                    public void actionPerformed(ActionEvent actionEvent)
                    {
                        // Return current text label, instead of argument to method
                        optionPane.setValue(button.getText());
                    }
                };
                button.addActionListener(actionListener);
                return button;
            }


}
