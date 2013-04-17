import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.sound.sampled.*;
import javax.swing.event.*;

/*<Applet code="kkk" height=400 width=400></Applet>*/
public class kkk extends JComponent {
  static File f1;
	int prog;
	static JFrame jf;
	int al;
	JLabel time;
	Timer tr;
	Button b;
	int pos = 0;
	Clip c;
	AudioInputStream a;
	JSlider s;

	public static void main(String args[]) {
		f1 = new File("SatelliteNoise.wav");
		jf = new JFrame();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		kkk kp = new kkk(f1);
		jf.getContentPane().add(kp, "Center");
		jf.setSize(200, 70);
		jf.setVisible(true);
	}

	kkk(File f1) {
		try {
			a = AudioSystem.getAudioInputStream(f1);
			AudioFormat af = a.getFormat();
			DataLine.Info di = new DataLine.Info(Clip.class, af);
			c = (Clip) AudioSystem.getLine(di);
			c.open(a);
		} catch (Exception e) {
			System.out.println("Exception caught ");
		} finally {
			try {
				a.close();
			} catch (Exception e) {
				System.out.println("Exception caught ");
			}
		}
		al = (int) (c.getMicrosecondLength() / 1000);
		s = new JSlider();
		Button b = new Button("play");
		time = new JLabel();
		Box row = Box.createHorizontalBox();
		row.add(s);
		row.add(b);
		row.add(time);
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent eee) {
				play();
			}
		});
		s.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ee) {
				// repaint();
				prog = s.getValue();
				time.setText(prog / 1000 + "." + (prog % 1000) / 100);
				// if(prog!=ap)
				// skip(prog);
			}
		});
		tr = new javax.swing.Timer(100, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tick();
			}
		});
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(row);
	}

	public void play() {
		try {
			FloatControl volume = (FloatControl) c
					.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue(-20.63f);
		} catch (Exception e) {
		}
		c.start();
		tr.start();
	}

	// public void skip(
	public void tick() {
		pos = (int) (c.getMicrosecondPosition() / 1000);
		s.setValue(pos);
	}
}
