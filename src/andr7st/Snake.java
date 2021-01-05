package andr7st;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Toolkit;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

public class Snake extends JFrame{ private static final long serialVersionUID = 1L;
    int widht_ = 640;
    int height_ = 480;
    Point snake;
    Point comida;
    ImagenSnake imagenSnake;
    ArrayList<Point> lista;// = new ArrayList<Point>();

    boolean gameOver = false;

    int widthPoint  = 10; 
    int heightPoint = 10;

    long frecuencia = 25; // milisegundos

    int direccion = KeyEvent.VK_LEFT;

    public Snake () {
        
        this.setTitle("Snake | Andr7st");
        this.addKeyListener(new Teclas());

        this.setSize(widht_,height_);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        // this.setLocationRelativeTo(null);
        this.setLocation(dim.width/2-widht_/2, dim.height/2-height_/2);
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        snake = new Point(widht_/2, height_/2);

        strartGame();
        imagenSnake = new ImagenSnake();
        this.getContentPane().add(imagenSnake);

        this.setVisible(true);

        /// Iniciatr ejecusion
        Momento momento = new Momento();
        Thread trid = new Thread(momento);
        trid.start();
    }

    public void strartGame() {
        comida = new Point(200,200);
        snake = new Point(widht_/2, height_/2);

        lista = new ArrayList<Point>();
        //lista.add(snake);

        crearComida();
    }
    public void crearComida() {
        Random rdm = new Random();

        comida.x = rdm.nextInt(widht_);
        comida.y = rdm.nextInt(height_);
        
        ////// x
        if((comida.x % 5) > 0 ) {
            comida.x = comida.x - (comida.x % 5);
        }
        if (comida.x < 5) {
            comida.x = comida.x + 10;
        }

        ////// y
        if((comida.y % 5) > 0 ) {
            comida.y = comida.y - (comida.y % 5);
        }
        if (comida.y < 5) {
            comida.y = comida.y + 10;
        }

    }
    public void actualizar() {
        imagenSnake.repaint();

        lista.add(0, new Point(snake.x,snake.y));
        lista.remove((lista.size()-1));

        for(int i=1; i<lista.size(); i++) {
            Point punto = lista.get(i);

            if(snake.x == punto.x && snake.y == punto.y) {
                gameOver = true;
            }
        }

        if((snake.x > (comida.x - 10)) && (snake.x < (comida.x + 10))  && (snake.y > (comida.y - 10)) && (snake.y < (comida.y + 10)) ){
            /// Si se cumple todo esto es porque está dentro de la zona.
            /// cada vez que la serpiente come un punto, se creará otro punto de comida.

            lista.add(0, new Point(snake.x,snake.x));
            crearComida();
        }
    }


    public class ImagenSnake extends JPanel {  private static final long serialVersionUID = 1L;

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(new Color(0,0,255));

            g.fillRect(snake.x, snake.y, widthPoint, heightPoint);

            for (int i=0; i<lista.size(); i++) {
                
                Point point = (Point)lista.get(i);

                g.fillRect(point.x, point.y, widthPoint, heightPoint);
            }
            
            g.setColor(new Color(255,0,0));
            g.fillRect(comida.x, comida.y, widthPoint, heightPoint);

            if(gameOver) {
                g.drawString("GAME OVER", 200, 320);
            }
        }
    }

    public class Teclas extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {

            if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            }
            else if (e.getKeyCode() == KeyEvent.VK_UP) {
                if(direccion != KeyEvent.VK_DOWN) {
                    direccion = KeyEvent.VK_UP;
                }
            }
            else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                if(direccion != KeyEvent.VK_UP) {
                    direccion = KeyEvent.VK_DOWN;
                }
            }
            else if (e.getKeyCode() == KeyEvent.VK_LEFT) {  
                if(direccion != KeyEvent.VK_RIGHT) {
                    direccion = KeyEvent.VK_LEFT;
                }
            }
            else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                if(direccion != KeyEvent.VK_LEFT) {
                    direccion = KeyEvent.VK_RIGHT;
                }
            }
        }
    }

    public class Momento extends Thread {
		
		private long last = 0;
		
        public Momento() {}
        
		public void run() {
			while(true) {
				if((java.lang.System.currentTimeMillis() - last) > frecuencia) {
					if(!gameOver) {

                        if(direccion == KeyEvent.VK_RIGHT) {
                            snake.x = snake.x + widthPoint;
                            if(snake.x > widht_) {
                                snake.x = 0;
                            }
                        } else if(direccion == KeyEvent.VK_LEFT) {
                            snake.x = snake.x - widthPoint;
                            if(snake.x < 0) {
                                snake.x = widht_ - widthPoint;
                            }                        
                        } else if(direccion == KeyEvent.VK_UP) {
                            snake.y = snake.y - heightPoint;
                            if(snake.y < 0) {
                                snake.y = height_;
                            }                        
                        } else if(direccion == KeyEvent.VK_DOWN) {
                            snake.y = snake.y + heightPoint;
                            if(snake.y > height_) {
                                snake.y = 0;
                            }                        
                        }
                    }
                    actualizar();
					
					last = java.lang.System.currentTimeMillis();
				}
			}
		}
	}
}