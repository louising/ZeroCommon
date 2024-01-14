import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class StudyDemo {
    static Set<String> extSet = new HashSet<>();
    int a = 0;
    int b = 0;
    
    public static void main(String[] args) {
        //listFile(new File("d:/ljcnote"));
        //new StudyDemo().doIt();
        System.out.println(StudyDemo.class.getClassLoader().getParent());
    }
    
    void doIt() {
        //
    }
    
    void one() { a++; b++; }
    void two() { System.out.printf("A=%d B=%d\n", a, b); }
    
    static void listFile(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for(File file: files) {
                if (file.isFile()) {
                    int index = file.getPath().lastIndexOf(".");
                    if (index > 0) {
                        String ext = file.getPath().substring(index + 1);
                        extSet.add(ext);
                        if (ext.equals("xml")) {
                            //System.out.println(file.getPath().substring(0, index));
                            boolean b = file.renameTo(new File(file.getPath().substring(0, index) + ".txt"));
                            if (!b) {
                                b = file.renameTo(new File(file.getPath().substring(0, index) + "2.txt"));
                                System.out.println(file.getAbsolutePath() + "(retry): " + b);
                            }
                        }
                    }
                    else {                        
                        //System.out.println("  " + file.getName());
                    }
                } else {
                    listFile(file);
                }
            }
        }
    }
}
