import javax.imageio.*;
import java.io.*;
import java.awt.image.*;

class ShareGenerator{
	public static void main(String args[]){	
		
		File f = new File("University Logo.jpg");		// change the "filename" with your image file.
		generateShares(f);			// This function generates the public & private share of the above mention image.
		

		generateOriginal(new File("publicshare.png"), new File("privateshare.png"));	// This function merge the private & public share and generate original share.
	}
	public static File[] generateShares(File original){
		File arr[] = new File[2];
		try{
			BufferedImage org = ImageIO.read(original);
			int height = org.getHeight();
			int width = org.getWidth();
			BufferedImage publicS = new BufferedImage(width,height,BufferedImage.TYPE_4BYTE_ABGR);
			BufferedImage privateS = new BufferedImage(width,height,BufferedImage.TYPE_4BYTE_ABGR);
	
			int pos;
			int a,r,g,b,n;
			
			for(int i=0; i< height; i++){
				for(int j=0; j < width; j++){

					pos = (i * width) + j;

					n = org.getRGB(j,i);
					b = (n & 0xFF);
					g = (n & 0xFF00) >>> 8;
					r = (n & 0xFF0000) >>> 16;
					a = (n & 0xFF000000) >>> 24;

					n = 0;
					n = n | a;
					n = n << 8;
					n = n | (r % 100 + (pos * 3) % 100);
					n = n << 8;
					n = n | (g % 100 + (pos * 7) %  100);
					n = n << 8;
					n = n | (b % 100 + (pos * 9) % 100);

					privateS.setRGB(j,i,n);

					n = 0;
					n = n | a;
					n = n << 8;
					n = n | (255 - (r / 100));
					n = n << 8;
					//if(pos%10 < 5){
						n = n | (255 - (g / 100) - 253);
					//}else{
					//	n = n | (255 - (g / 100));
					//}
					n = n << 8;
					n = n | (255 - (b / 100));

					publicS.setRGB(j,i,n);
				}
			}
			arr[0] = new File("publicshare.png");	// Public share
			arr[1] = new File("privateshare.png");	// Private share
			ImageIO.write(publicS,"PNG",arr[0]);
			ImageIO.write(privateS,"PNG",arr[1]);
		}catch(Exception e){
			e.printStackTrace();
		}
		return arr;
	}
	public static File generateOriginal(File publicS, File privateS){
		File f = null;
		try{
			BufferedImage publicBuff = ImageIO.read(publicS);
			BufferedImage privateBuff = ImageIO.read(privateS);
			int width = publicBuff.getWidth();
			int height = publicBuff.getHeight();
			BufferedImage org = new BufferedImage(width,height,BufferedImage.TYPE_4BYTE_ABGR);
			int n1,a1,r1,g1,b1;
			int n2,a2,r2,g2,b2;
			int n,r,g,b;
			int pos;
			
			for(int i=0; i< height; i++){
				for(int j=0; j< width; j++){

					pos = (i * width) + j;

					n1 = publicBuff.getRGB(j,i);
					b1 = (n1 & 0xFF);
					g1 = (n1 & 0xFF00) >>> 8;
					r1 = (n1 & 0xFF0000) >>> 16;
					a1 = (n1 & 0xFF000000) >>> 24;

					n2 = privateBuff.getRGB(j,i);
					b2 = (n2 & 0xFF);
					g2 = (n2 & 0xFF00) >>> 8;
					r2 = (n2 & 0xFF0000) >>> 16;
					a2 = (n2 & 0xFF000000) >>> 24;

					n = 0;
					n = n | a1;
					n = n << 8;
					r = ((255 - r1) * 100) + (r2 - (pos * 3) % 100);
					n = n | r;
					n = n << 8;
					//if(pos%10 < 5){
						g = ((255 - (g1 + 253)) * 100) + (g2 - (pos * 7) % 100);
					//}else{
					//	g = ((255 - g1) * 100) + (g2 - (pos * 7) % 100);
					//}
					n = n | g;
					n = n << 8;
					b = ((255 - b1) * 100) + (b2 - (pos * 9) % 100);
					n = n | b;

					org.setRGB(j,i,n);
				}
			}
			f = new File("originalshare.png");	//Original share
			ImageIO.write(org,"PNG",f);
		}catch(Exception e){
			e.printStackTrace();
		}
		return f;
	}
}

// compile and run the java file