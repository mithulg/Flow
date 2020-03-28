package main;




import utils.GUIUtils;
import utils.SpotifyUtils;
import utils.TimeUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import static utils.ColorUtils.getAvgColor;
import static utils.ImageUtils.blurImage;
import static utils.ImageUtils.resizeIcon;

public class MainPanel extends JPanel implements Runnable{
    BufferedImage artwork;
    BufferedImage settingsImage;
    boolean settingsMode = false;
    JSlider slider = new JSlider();
    String oldId = "";
    URL sURl = null;
    int width = 320;
    public static boolean pause = false;
    int height = 320;
    JLabel name;
    JLabel info;
    JFrame frame;
    JLabel start ;
    JLabel end ;
    KeyListener keyListener;
    MouseListener mouseListener;
    AbstractButton b;
    AbstractButton nex;
    AbstractButton prev;
    public static void main(String[] args) {
        new MainPanel();
    }

    public MainPanel()
    {
        start = new JLabel(TimeUtils.secondsToString((int) SpotifyUtils.playerPosition()));
        start.setForeground(Color.white);
        nex = GUIUtils.makeButton("", resizeIcon(new ImageIcon(getClass().getResource("/resources/348227_thumb.png").getPath()), 40, 40));
        prev = GUIUtils.makeButton("", resizeIcon(new ImageIcon(getClass().getResource("/resources/image.png").getPath()), 40, 40));
        b = GUIUtils.makeButton("", resizeIcon(new ImageIcon(getClass().getResource("/resources/684787-200.png").getPath()), 80, 80));
        setLayout(new GridLayout(3, 0));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        sURl = SpotifyUtils.getArtworkURL();
        change();

        JPanel grid1 = new JPanel();
        grid1.setOpaque(false);
        grid1.setLayout(new GridBagLayout());
        JPanel grid3 = new JPanel();
        grid3.setOpaque(false);
        grid3.setLayout(new GridLayout(2,0));
        JPanel grid2 = new JPanel();
        grid2.setLayout(new GridLayout(0,3));
        JPanel gridTa = new JPanel();
        JPanel gridTb = new JPanel();
        JPanel gridTc = new JPanel();
        gridTa.setOpaque(false);
        gridTb.setOpaque(false);
        gridTc.setOpaque(false);
        grid2.add(gridTa);
        grid2.add(gridTb);
        grid2.add(gridTc);
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SpotifyUtils.togglePlay();
            }
        });
        gridTb.add(b);
        gridTa.add(prev);
        gridTc.add(nex);
        grid2.setOpaque(false);
        grid2.setLayout(new GridBagLayout());
        add(grid1);
        add(grid2);
        add(grid3);
        frame = new JFrame(SpotifyUtils.getTrackName());
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        //frame.setMinimumSize(new Dimension());
        frame.setAlwaysOnTop(true);
        String tempTrackName = SpotifyUtils.getTrackName();
        name = new JLabel(tempTrackName);
        name.setForeground(Color.white);
        String tempAlbum = SpotifyUtils.getTrackAlbum();

       /* if(SpotifyUtils.single())
            info = new JLabel(SpotifyUtils.getTrackArtist());
        else*/
            info = new JLabel(SpotifyUtils.getTrackArtist() + " - " + tempAlbum);
        info.setForeground(Color.white);
        grid1.add(info);

        JPanel grid3a = new JPanel();
        grid3a.setOpaque(false);
        JPanel grid3b = new JPanel();

        grid3b.setOpaque(false);
        grid3a.setLayout(new GridBagLayout());
        grid3a.add(name);
        grid3.add(grid3a);
        grid3.add(grid3b);
        int dur = SpotifyUtils.duration()/1000;
        slider.setMaximum(dur);
        end = new JLabel(TimeUtils.secondsToString(dur));
        end.setForeground(Color.white);
        slider.addChangeListener(e -> {
            if(pause)
            {
                start.setText(TimeUtils.secondsToString(slider.getValue()));
            }
        });
        Thread fucker = new Thread(() -> {
            while (true)
            {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(!pause) {
                    int ti = (int)SpotifyUtils.playerPosition();
                    slider.setValue(ti);
                    start.setText(TimeUtils.secondsToString(ti));
                }
            }
        });
        fucker.start();
        slider.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                pause = true;
                start.setText(TimeUtils.secondsToString(slider.getValue()));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                SpotifyUtils.setPlayBackPosition(slider.getValue());
                pause = false;
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        grid3b.add(start);
        grid3b.add(slider);
        grid3b.add(end);
        frame.add(this);
        frame.setSize(width, height + 20);

        frame.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                height  = frame.getHeight() - 20;
                width = frame.getWidth();
                Rectangle b = e.getComponent().getBounds();
                e.getComponent().setBounds(b.x, b.y, b.width, b.width);
                repaint();
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });
        keyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {

                        SpotifyUtils.previousTrack();
                        update();
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        SpotifyUtils.nextTrack();
                        update();
                }

                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    SpotifyUtils.increaseVolume(5);
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    SpotifyUtils.decreaseVolume(5);
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE)
                        SpotifyUtils.togglePlay();

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        };
        slider.addKeyListener(keyListener);
        frame.addKeyListener(keyListener);
        b.addKeyListener(keyListener);
        nex.addKeyListener(keyListener);
        prev.addKeyListener(keyListener);
        mouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                    settingsMode = true;
                    repaint();

            }

            @Override
            public void mouseExited(MouseEvent e) {
                    settingsMode = false;
                    repaint();
            }
        };
        frame.addMouseListener(mouseListener);
        slider.addMouseListener(mouseListener);
        b.addMouseListener(mouseListener);
        nex.addMouseListener(mouseListener);
        nex.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SpotifyUtils.nextTrack();
            }
        });
        prev.addMouseListener(mouseListener);
        prev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SpotifyUtils.previousTrack();
            }
        });
        frame.setVisible(true);
        Thread songChangeListener = new Thread(this);
        songChangeListener.start();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (settingsMode) {
            g.drawImage(settingsImage, 0, 0, width, height, this);
            /*new Thread(() -> {
                if (SpotifyUtils.playing())
                    b.setIcon(pauseIcon);
                else
                    b.setIcon(playIcon);
            }).start();*/
            b.setVisible(true);
            name.setVisible(true);
            info.setVisible(true);
            nex.setVisible(true);
            prev.setVisible(true);
            slider.setVisible(true);
            end.setVisible(true);
            start.setVisible(true);
        }
        else {
            g.drawImage(artwork, 0, 0, width, height, this);
            name.setVisible(false);
            info.setVisible(false);
            start.setVisible(false);
            end.setVisible(false);
            b.setVisible(false);
            nex.setVisible(false);
            prev.setVisible(false);
            slider.setVisible(false);
        }
    }
    @Override
    public void run() {
        while (true) {

                String nID = SpotifyUtils.getTrackId();
                if(!oldId.equals(nID)) {
                    update();
                    oldId = nID;
                }
                URL nURl = SpotifyUtils.getArtworkURL();
                assert nURl != null;
                if (!nURl.equals(sURl)) {
                    sURl = nURl;
                    change();
                }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void update()
    {
        String tempTrackName = SpotifyUtils.getTrackName();
        name.setText(tempTrackName);
        frame.setTitle(tempTrackName);
        int dur = SpotifyUtils.duration()/1000;
        slider.setMaximum(dur);
        end.setText(TimeUtils.secondsToString(dur));
        String tempAlbum = SpotifyUtils.getTrackAlbum();
        /*if(SpotifyUtils.single())
            info.setText(SpotifyUtils.getTrackArtist());
        else**/
            info.setText(SpotifyUtils.getTrackArtist() + " - " + tempAlbum);

    }
    private void change()
    {
        try {
            artwork = ImageIO.read(sURl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            setBackground(getAvgColor(artwork));
            repaint();
            /*new Thread(() -> {
                File nFile = new File(("/Users/naveen/Documents/Shrobin/images/photo"+Math.random()+".png"));
                try {
                    ImageIO.write(artwork, "png", nFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    setWallpaper(nFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();*/

        } catch (Exception e) {
            e.printStackTrace();
        }
        settingsImage = blurImage(artwork, 0);
        repaint();
    }

}
