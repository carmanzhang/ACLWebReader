import java.io.File;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {
    private static ExecutorService threadPool = new ThreadPoolExecutor(4, 4, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(1), new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) throws IOException {
        for (String arg : args) {
            System.out.println(arg);
        }
        String doadLoadPath = args[0];// "/home/zhangli/mydisk-2t/scholarly-bigdata/acl/acl-anthonogy-crawl-pdf-2021-01-10";
        String metadataPath = args[1];//"/home/zhangli/mydisk-2t/scholarly-bigdata/acl/acl-anthonogy-crawl-pdf-2021-01-10/metadata.txt";
        String aclBibFile = args[2]; // "/home/zhangli/mydisk-2t/repo/ACLWebReader/ACLWebReader/src/anthology.bib";

//        String doadLoadPath = "/home/zhangli/mydisk-2t/scholarly-bigdata/acl/acl-anthonogy-crawl-pdf-2021-01-10.bak";
//        String metadataPath = "/home/zhangli/mydisk-2t/scholarly-bigdata/acl/acl-anthonogy-crawl-pdf-2021-01-10.bak/metadata.txt";
//        String aclBibFile = "/home/zhangli/mydisk-2t/repo/ACLWebReader/ACLWebReader/src/anthology.bib";

        int needSkip = 0;
        if (args.length >= 4 && args[3] != null) {
            needSkip = Integer.parseInt(args[3]);
        }

        analyzeAnthology a = new analyzeAnthology(aclBibFile);
        for (int i = needSkip; i <= a.MyACLPaperInfo.length; i++) {
            System.out.println(i);
            String paperUrl = a.MyACLPaperInfo[i].url;
            String fineName = a.MyACLPaperInfo[i].name;
            if (fineName == null) {
                continue;
            }
            if (!fineName.endsWith(".pdf")) {
                fineName = fineName + ".pdf";
            }

            File file = new File(doadLoadPath + File.separator + fineName);
            if (file.exists()) {
                System.out.println(fineName + " exist!, skip!");
                continue;
            }

            if (paperUrl == null)
                continue;

            String finalFineName = fineName;
            threadPool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        ACLwebReader.downloadPapers(paperUrl, finalFineName, doadLoadPath);
                        ACLwebReader.getInformation(paperUrl, metadataPath);
                    } catch (Exception e) {
                        System.out.println(paperUrl + "\t" + finalFineName + "\t" + doadLoadPath);
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
