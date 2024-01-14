import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import javax.imageio.ImageIO;

public class ForkBlur extends RecursiveAction {
    protected static int sThreshold = 10000;

    private int[] mSource;
    private int mStart;
    private int mLength;
    private int[] mDestination;
    private int mBlurWidth = 15; // Processing window size, should be odd.

    public ForkBlur(int[] src, int start, int length, int[] dst) {
        mSource = src;
        mStart = start;
        mLength = length;
        mDestination = dst;
        //System.out.println("  ..Create ForkBlur");
    }

    // Plumbing follows.
    public static void main(String[] args) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) 97);
        buffer.put((byte) 98);
        buffer.put((byte) 99);
        buffer.flip(); 
        buffer.get();
        buffer.get();
        buffer.compact(); //Put the rest to the first, position to the next. ( copy 99 to 0, position is 1)
        buffer.put((byte) 100);
        buffer.put((byte) 101);
        buffer.flip();      // 切换到读模式
        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }
    }
    
    static void forkBlur() throws Exception {
        BufferedImage image = ImageIO.read(new File("d:/big.jpg"));                
        BufferedImage blurredImage = blur(image);        
        ImageIO.write(blurredImage, "jpg", new File("d:/big-blured.jpg"));    //ImageWriterSpi: wbmp, bmp, gif, jpg, png 
        System.out.println("Output image OK.");     
    }
    
    // Average pixels from source, write results into destination.
    protected void computeDirectly() {
        int sidePixels = (mBlurWidth - 1) / 2;
        for (int index = mStart; index < mStart + mLength; index++) {
            // Calculate average.
            float rt = 0, gt = 0, bt = 0;
            for (int mi = -sidePixels; mi <= sidePixels; mi++) {
                int mindex = Math.min(Math.max(mi + index, 0), mSource.length - 1);
                int pixel = mSource[mindex];
                rt += (float) ((pixel & 0x00ff0000) >> 16) / mBlurWidth;
                gt += (float) ((pixel & 0x0000ff00) >> 8) / mBlurWidth;
                bt += (float) ((pixel & 0x000000ff) >> 0) / mBlurWidth;
            }

            // Re-assemble destination pixel.
            int dpixel = (0xff000000)
                    | (((int) rt) << 16)
                    | (((int) gt) << 8)
                    | (((int) bt) << 0);
            mDestination[index] = dpixel;
        }
    }    

    @Override
    protected void compute() {
        //computeDirectly();
        
        if (mLength < sThreshold) {
            computeDirectly();
            return;
        }

        int split = mLength / 2;

        invokeAll(new ForkBlur(mSource, mStart, split, mDestination), new ForkBlur(mSource, mStart + split, mLength - split,  mDestination));
    }

    public static BufferedImage blur(BufferedImage srcImage) {
        int w = srcImage.getWidth();
        int h = srcImage.getHeight();

        int[] src = srcImage.getRGB(0, 0, w, h, null, 0, w);
        int[] dst = new int[src.length];
        System.out.println("Array size is " + src.length);
        System.out.println("Threshold is " + sThreshold);
        int processors = Runtime.getRuntime().availableProcessors();
        System.out.println(Integer.toString(processors) + " processor" + (processors != 1 ? "s are " : " is ") + "available");

        ForkBlur fb = new ForkBlur(src, 0, src.length, dst);
        ForkJoinPool pool = new ForkJoinPool();
        long startTime = System.currentTimeMillis();
        pool.invoke(fb);
        System.out.println("Image blur took " + (System.currentTimeMillis() - startTime) + " milliseconds.");

        BufferedImage dstImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        dstImage.setRGB(0, 0, w, h, dst, 0, w);

        return dstImage;
    }
}
