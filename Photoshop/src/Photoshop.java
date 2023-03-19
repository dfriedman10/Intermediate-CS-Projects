

import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

public class Photoshop extends Component {

	// the name of the output file. will be determined by which methods are called
    private String outputName;
    
    // the 2d array of colors representing the image
    private Color[][] pixels;
    
    // the width and height of the image 
    private int w,h;

    // runs the UI, calling the various manipulation methods
    public void run() {
    	JFileChooser fc = new JFileChooser();
//		File workingDirectory = new File(System.getProperty("user.dir")+System.getProperty("file.separator")+ "Images");
//		fc.setCurrentDirectory(workingDirectory);
		fc.showOpenDialog(null);
		File my_file = fc.getSelectedFile();
		if (my_file == null)
			System.exit(-1);
		
		// reads the image file and creates our 2d array
		BufferedImage image;
		try {
			image = ImageIO.read(my_file);
		}
		catch(IOException e) {
			System.out.println("Image does not exist :(");
			return;
		}
        BufferedImage new_image = new BufferedImage(image.getWidth(),
                        image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        create_pixel_array(image);
		outputName = my_file.getName();
		
		// runs the manipulations determined by the user
		System.out.println("Enter the manipulations you would like to run on the image.\nYour "
				+ "choices are: brighten, flip, negate, blur, edge, or simplify.\nEnter each "
				+ "manipulation you'd like to run, then type in 'done'.");
		Scanner in = new Scanner(System.in);
		String action = in.next().toLowerCase();
		while (!action.equals("done")) {
    			try {
	    			if (action.equals("brighten")) {
	    				System.out.println("enter an amount to increase the brightness by");
	    				int brightness = in.nextInt();
	        			Method m = getClass().getDeclaredMethod(action, int.class);
	        			m.invoke(this, brightness);
	    			}
	    			else if (action.equals("kmeans")) {
	    				System.out.println("enter n");
	    				int brightness = in.nextInt();
	        			Method m = getClass().getDeclaredMethod(action, int.class);
	        			m.invoke(this, brightness);
	    			}
	    			else if (action.equals("flip")) {
	    				System.out.println("enter \"h\" to flip horizontally, anything else to flip vertically.");
	        			Method m = getClass().getDeclaredMethod(action, boolean.class);
	        			m.invoke(this, in.next().equals("h"));
	    			}
	    			else {
	        			Method m = getClass().getDeclaredMethod(action);
	        			m.invoke(this, new Object[0]);
	    			}
	    			System.out.println("done. enter another action, or type 'done'");
    			}
    			catch (NoSuchMethodException e) {
    				System.out.println("not a valid action, try again");
    			} catch (IllegalAccessException e) {} 
    			catch (IllegalArgumentException e) {}
    			catch (InvocationTargetException e) {}
    			
    			action = in.next().toLowerCase();
    		} 
        in.close();
        
        // turns our 2d array of colors into a new png file
        create_new_image(new_image);
        File output_file = new File("Images/" + outputName);
        
        try {
			ImageIO.write(new_image, "png", output_file);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public void kmeans(int n) {
    		outputName = "kmeaned_"+outputName;
//    		Color[] newColors = {Color.red, Color.blue, Color.green};
    		Color[] newColors = new Color[n], oldColors = new Color[n];
    		for (int i = 0; i < n; i++) 
    			newColors[i] = pixels[(int)(Math.random()*h)][(int)(Math.random()*h)];
    		
    		do {
    			oldColors = newColors;
    			newColors = avg(cluster(oldColors));
    		} while(distance(oldColors, newColors) > 10);
    		
    		
    		
    		
    		
    		
    		
    		
    		simplify(newColors);
    		
    }
    
    private ArrayList<ArrayList<Color>> cluster(Color[] colors) {
    		ArrayList<ArrayList<Color>> clusters = new ArrayList<ArrayList<Color>>();
	    	for (Color c : colors)
			clusters.add(new ArrayList<Color>());
	    	for (int i = 0; i < h; i++) {
	    		for (int j = 0; j < w; j++) {
	    			double minD = Double.MAX_VALUE;
	    			int minI = 0;
	    			for (int k = 0; k < colors.length; k++) {
	    				double d = distance(pixels[i][j],colors[k]);
	    				if (d < minD) {
	    					minD = d; minI = k;
	    				}
	    			}
	    			clusters.get(minI).add(pixels[i][j]);
	    		}
	    	}
	    	return clusters;
    }
    
    private Color[] avg(ArrayList<ArrayList<Color>> clusters) {
    		Color[] avg = new Color[clusters.size()];
    		for (int i = 0; i < clusters.size(); i++) {
    			int sumR = 0, sumG = 0, sumB = 0;
    			for (Color c : clusters.get(i)) {
    				sumR+=c.getRed(); sumG+=c.getGreen();sumB+=c.getBlue();
    			}
    			avg[i] = new Color(sumR/clusters.get(i).size(),sumG/clusters.get(i).size(),sumB/clusters.get(i).size());
    		}
    		return avg;
    }
    
    private double distance(Color[] c1, Color[] c2) {
    		double sum = 0;
    		for (int i = 0; i < c1.length; i++) 
    			sum += distance(c1[i],c2[i]);
    		return sum;
    }
    
    public void edge() {
        outputName = "edged_" + outputName;

        Color[][] pixels_copy=new Color[h][w];

        // creating a new 2d array
        for (int i = 0; i < h; i++) {
        // going through columns and rows

            for (int j = 0; j < w; j++) {
            	
                // to check if the pixel is not on the first row or column
                if((i!=0 && j!=0) && (i!=h-1 && j!=w-1)){

	                Color c = pixels[i][j];
	
	                // finding the eight neighbouring pictures and the middle pixels
	
	                int sumRed = c.getRed()*9, sumGreen = c.getGreen()*9, sumBlue = c.getBlue()*9;
	                for (int m = i-1; m <= i+1; m++) {
	                		for (int n = j-1; n <= j+1; n++) {
	                			sumRed -= pixels[m][n].getRed();
	                			sumGreen -= pixels[m][n].getGreen();
	                			sumBlue -= pixels[m][n].getBlue();
	                		}
	                }
	                // for each pixel, do the following: multiply each of it's 8 neighbor's values
	                pixels_copy[i][j]=new Color(Math.max(Math.min(sumRed,255),0),Math.max(Math.min(sumGreen,255),0),Math.max(0,Math.min(sumBlue,255)));
	                // giving a new value to the pixel
                } 
                else
                		pixels_copy[i][j]=pixels[i][j];
            }
        }
        pixels=pixels_copy;
    }

    
    public void sharpen() {
        outputName = "sharpened_" + outputName;
        Color[][] copy = new Color[h][];
        for (int i= 0; i < h; i++) {
            copy[i] = new Color[w];
            for (int j = 0; j < w; j++) {
                int new_red = 0, new_blue = 0, new_green = 0;
                if (i+1 < h && i-1 > 0 && j+1 < w && j-1 > 0) {
                    for (int m = -1; m <= 1; m++) {
                        for (int n = -1; n <= 1; n++) {
                            if (i+m != i || j+n != j) {
                                new_red += pixels[i+m][j+n].getRed()*-1;
                                new_blue += pixels[i+m][j+n].getBlue()*-1;
                                new_green += pixels[i+m][j+n].getGreen()*-1;
                            }
                            else {
                                new_red += pixels[i+m][j+n].getRed()*9;
                                new_blue += pixels[i+m][j+n].getBlue()*9;
                                new_green += pixels[i+m][j+n].getGreen()*9;
                            }

                        }
                    }
                    if (new_red>255)
                            new_red=255;
                    if (new_blue>255)
                            new_blue=255;
                    if(new_green>255)
                            new_green=255;
                    if (new_red < 0)
                            new_red=0;
                    if (new_blue < 0)
                            new_blue=0;
                    if (new_green < 0)
                            new_green=0;
                    copy[i][j] = new Color(new_red,new_green,new_blue);
                }
                else
                        copy[i][j] = pixels[i][j];
            }  
        }
        pixels = copy;
    }
    
    public void blur() {
		outputName = "blurred_" + outputName;
		
		Color[][] pixels_copy=new Color[h][w];

        // creating a new 2d array
        for (int i = 0; i < h; i++) {
        // going through columns and rows

            for (int j = 0; j < w; j++) {
            	
                // to check if the pixel is not on the first row or column
                if((i!=0 && j!=0) && (i!=h-1 && j!=w-1)){
	
	                // finding the eight neighbouring pictures and the middle pixels
	
	                int sumRed = 0, sumGreen = 0, sumBlue = 0;
	                for (int m = i-1; m <= i+1; m++) {
	                		for (int n = j-1; n <= j+1; n++) {
	                			sumRed += pixels[m][n].getRed();
	                			sumGreen += pixels[m][n].getGreen();
	                			sumBlue += pixels[m][n].getBlue();
	                		}
	                }
	                // for each pixel, do the following: multiply each of it's 8 neighbor's values
	                pixels_copy[i][j]=new Color(sumRed/9,sumGreen/9,sumBlue/9);
	                // giving a new value to the pixel
                } 
                else
                		pixels_copy[i][j]=pixels[i][j];
            }
        }
        pixels=pixels_copy;
    }
          
    // for each pixel, find the basic color that is closest to
    // the pixel's rgb value. Set the pixel's rgb value to this
    // basic color
    public void simplify() {
    		Color[] color_list = {Color.BLUE, Color.RED,Color.ORANGE, Color.MAGENTA,
                    Color.BLACK, Color.WHITE, Color.GREEN, Color.YELLOW, Color.CYAN};
        outputName = "simplified_" + outputName;
       simplify(color_list);
    }
    
    public void simplify(Color[] color_list) {
    	 Color colr = color_list[0];
         Color a = pixels[0][0];
         double min = distance (a,colr);
         //we use a double because it cannot be an integer
         for (int i = 0; i < h; i++) {
             for (int j = 0; j < w; j++) {
                 for (int k = 0; k < color_list.length; k++) {
                     if(min > distance(pixels[i][j], color_list[k])) {
                         min = distance(pixels[i][j], color_list[k]);
                         a = color_list[k];
                     }
                 }
                 min = Integer.MAX_VALUE;
                 pixels[i][j] = a;
             }
         }
    }

    // calculate the distance between two colors. Think of each
    // color as a 3d point (r,g,b), then use the distance formula
    // this can be used in closest_colors(), but does not have to be
    public double distance(Color c1, Color c2) {

        double x1 = c1.getRed(), x2=c2.getRed();
        //x
        double y1 = c1.getGreen(), y2=c2.getGreen();
        //y
        double z1 = c1.getBlue(), z2=c2.getBlue();
        //z
        double x = (x2 - x1)*(x2 - x1), y = (y2 - y1)*(y2 - y1), z = (z2 - z1)*(z2 - z1); //finds the x,y,z averages and squares them
        double distance = Math.sqrt(x+y+z);
        //finds the distance formula
        return distance;
    }
    public void negate() {
        outputName = "negated_" + outputName;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                Color c = pixels [i][j];

                int r = (255 - c.getRed());

                // we subtract from 255 for r,g,b
                int g = (255 - c.getGreen());
                int b = (255 - c.getBlue());
                pixels[i][j] = new Color(r,g,b);
            }
        }

    }

    // flip an image horizontally. It should look as if you're holding
    // the image in front of a mirror.
    public void flip(boolean horizontally) {
    	outputName = (horizontally?"h":"v") + "_flipped_" + outputName;
    	
    	if (horizontally)
	        for(int i = 0; i < h; i++) {
			    for(int j = 0; j < w/2; j++) {
			    //it flips the image by changing the locations of pixels that are identical.
			        Color c = pixels[i][j];
			        pixels[i][j] = pixels[i][w-1-j];
			        //i added the -1 because the last pixels in x and y is not an element, just like in arrays
			        pixels[i][w-1-j] = c;
			     }
	        }
    	
    	else         
    		for(int i = 0; i < h/2; i++) {
			    for(int j = 0; j < w; j++) {
			    //it flips the image by changing the locations of pixels that are identical.
			        Color c = pixels[i][j];
			        pixels[i][j] = pixels[h-i-1][j];
			        //i added the -1 because the last pixels in x and y is not an element, just like in arrays
			        pixels[h-i-1][j] = c;
			     }
        }
    }

    // example method. This method increases each color's rgb value by a given amount.
    // Since rgb values cannot exceed 255, we take the minimum of this new value
    // and 255.
    public void brighten(int amount) {
        outputName = "brightened_" + outputName;

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                Color c = pixels[i][j];
                int r = Math.max(0,Math.min(c.getRed()+amount,255));
                int g = Math.max(0,Math.min(c.getGreen()+amount,255));
                int b = Math.max(0,Math.min(c.getBlue()+amount,255));
                pixels[i][j] = new Color(r,g,b);
            }
        }
    }




    public void create_pixel_array(BufferedImage image) {
        w = image.getWidth();
        h = image.getHeight();
        pixels = new Color[h][];
        for (int i = 0; i < h; i++) {
            pixels[i] = new Color[w];
            for (int j = 0; j < w; j++) {
                pixels[i][j] = new Color(image.getRGB(j,i));
            }
        }
    }

    public void create_new_image(BufferedImage new_image) {
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
            		new_image.setRGB(j, i, pixels[i][j].getRGB());
            }
        }
    }

    public static void main(String[] args) {
		new Photoshop();
	}

    public Photoshop() {
        run();
    }
}