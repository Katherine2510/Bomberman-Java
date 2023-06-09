import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BombermanFrame extends JFrame
{
    private Floor floor;
    private BombermanComponent bombermanComponent;
    public static Clip clip;
    private Player player;



    public BombermanFrame(final String title, Floor floor) throws HeadlessException {


        super(title);
        this.floor = floor;

        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        bombermanComponent = new BombermanComponent(floor);
        floor.createPlayer(bombermanComponent, floor);
        setKeyStrokes();

        if(floor.getFast()==3) {
            floor.createPlayer(bombermanComponent,floor);
        }

        //JToolBar tb = new JToolBar();
        // create a panel



        //this.getContentPane().setBackground(Color.BLACK);
        JPanel panel = new JPanel();
        this.requestFocusInWindow();
        this.add(bombermanComponent, BorderLayout.SOUTH);


        //this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

    }

    public BombermanComponent getBombermanComponent() {
        return bombermanComponent;
    }

    private void setKeyStrokes() {

        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        bombermanComponent.getInputMap().put(stroke, "q");
        bombermanComponent.getActionMap().put("q", quit);
    }

    private final Action quit = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e) {
            dispose();

        }
    };


    AbstractAction eventWatcher = new AbstractAction()
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            Object source = ae.getSource();

        }
    };
}
