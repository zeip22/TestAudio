import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class myclass extends JFrame{
	JSlider slid;
	JLabel label;
	
	public myclass(){
		super("JSlider");
		setLayout(new GridBagLayout());
		
		GridBagConstraints g = new GridBagConstraints();
		g.gridx = 1;
		g.gridy = 0;
		
		slid = new JSlider(JSlider.VERTICAL, 0, 100, 20);
		slid.setMajorTickSpacing(50);
		slid.setPaintTicks(true);
		slid.setForeground(Color.RED);
		
		add(slid, g);
		
		label = new JLabel("Volume 20 %");
		g.gridx = 0;
		g.gridy = 1;
		add(label, g);
		
		slid.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent event){
				label.setText("Volume "+slid.getValue()+" %");
			}
		}
		);
	}
	
	public static void main(String[] args){
		myclass m = new myclass();
		m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m.setSize(500,350);
		m.setVisible(true);
	}
}

//import javax.sound.midi.*;
//import java.awt.event.*;
//import javax.swing.*;
//
///**
// * This program the MIDI percussion channel with a Swing window.  It monitors
// * keystrokes and mouse motion in the window and uses them to create music.
// * Keycodes between 35 and 81, inclusive, generate different percussive sounds.
// * See the VK_ constants in java.awt.event.KeyEvent, or just experiment.
// * Mouse position controls volume: move the mouse to the right of the window
// * to increase the volume.
// */
//public class myclass extends JFrame {
//    MidiChannel channel;  // The channel we play on: 10 is for percussion
//    int velocity = 64;    // Default volume is 50%
//
//    public static void main(String[] args) throws MidiUnavailableException
//    {
//        // We don't need a Sequencer in this example, since we send MIDI
//        // events directly to the Synthesizer instead.
//        Synthesizer synthesizer = MidiSystem.getSynthesizer( );
//        synthesizer.open( );
//        JFrame frame = new myclass(synthesizer);
//
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(50, 128);  // We use window width as volume control
//        frame.setVisible(true);
//    }    
//
//    public myclass(Synthesizer synth) {
//        super("Volume");
//
//        // Channel 10 is the GeneralMidi percussion channel.  In Java code, we
//        // number channels from 0 and use channel 9 instead.
//        channel = synth.getChannels( )[9];
//
//        addKeyListener(new KeyAdapter( ) {
//                public void keyPressed(KeyEvent e) {
//                    int key = e.getKeyCode( );
//                    if (key >= 35 && key <= 81) {
//                        channel.noteOn(key, velocity);
//                    }
//                }
//                public void keyReleased(KeyEvent e) {
//                    int key = e.getKeyCode( );
//                    if (key >= 35 && key <= 81) channel.noteOff(key);
//                }
//            });
//
//        addMouseMotionListener(new MouseMotionAdapter( ) {
//                public void mouseMoved(MouseEvent e) {
//                    velocity = e.getX( );
//                }
//            });
//    }
//}